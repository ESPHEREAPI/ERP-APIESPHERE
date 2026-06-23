import { Component, OnInit, OnDestroy, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import * as QRCode from 'qrcode';
import { CommonModule }                  from '@angular/common';
import { FormsModule }                   from '@angular/forms';
import { RouterModule, ActivatedRoute,
         Router }                        from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Subject }                       from 'rxjs';
import { takeUntil, debounceTime,
         distinctUntilChanged }          from 'rxjs/operators';
import { Medicament,
         PrestataireService }            from '../../../services/prestataire.service';
import { HttpClient }                    from '@angular/common/http';
import { AuthService }                   from '../../../auth/auth.service';
import { ParametreService }              from '../../../services/parametre.service';
import { MediaService }                  from '../../../services/media.service';

interface LigneMedicament {
  medicamentId: number | null;
  nom:          string;
  codification: string;
  posologie:    string;
  prix:         number | null;
  quantite:     number;
  nouveau?:     boolean;
}

@Component({
  selector:    'app-ordonnance-ajouter',
  standalone:  true,
  imports:     [CommonModule, FormsModule,
                RouterModule, TranslateModule],
  templateUrl: './ordonnance-ajouter.component.html',
  styleUrls:   ['./ordonnance-ajouter.component.css']
})
export class OrdonnanceAjouterComponent
       implements OnInit, OnDestroy {

  @ViewChild('qrCanvas') qrCanvasRef?: ElementRef<HTMLCanvasElement>;

  // ── Infos visite ──────────────────────────────────────
  codeVisite:    string      = '';
  visiteCode:    string      = '';
  nomAssure:     string      = '';
  nomAyantDroit: string      = '';
  lienParente:   string      = '';
  souscripteur:  string      = '';
  groupe:        number|null = null;
  prestataireId: string      = '';

  // ── Formulaire ────────────────────────────────────────
  natureAffection: string           = '';
  lignes:          LigneMedicament[] = [this.nouvelleLigne()];

  // ── Médicaments ───────────────────────────────────────
  medicaments:         Medicament[] = [];
  medicamentsFiltres:  Medicament[] = [];
  searchMedicament:    string       = '';
  dropdownOuvertIndex: number|null  = null;
  isLoadingMeds:       boolean      = false;

  // Position dropdown fixed
  dropdownX = 0;
  dropdownY = 0;
  dropdownW = 320;

  // ── Dialog ajout médicament ───────────────────────────
  dialogOuvert      = false;
  dialogNom         = '';
  dialogLoading     = false;
  dialogErreur      = '';
  dialogStatut: 'idle'|'local'|'bdd'|'nouveau' = 'idle';
  dialogMedTrouve: any = null;

  // ── Dialog confirmation ───────────────────────────────
  dialogConfirmOuvert = false;

  // ── Consommation ──────────────────────────────────────
  consommation:   any     = null;
  isLoadingConso: boolean = false;

  // ── État ──────────────────────────────────────────────
  isLoading:    boolean = false;
  isSubmitting: boolean = false;
  erreur:       string  = '';
  success:      boolean = false;

  // ── QR code après enregistrement ──────────────────────
  prestationIdCree: number | null = null;
  urlMobile:        string        = '';

  // ── Partage & contrôle document ───────────────────────
  showSmsModal     = false;
  smsNumero        = '';
  smsEnvoi         = false;
  smsResultat      = '';
  whatsappActif    = true;
  smsActif         = true;
  documentObligatoire = false;
  documentEnvoye   = false;

  private destroy$      = new Subject<void>();
  private searchSubject = new Subject<string>();

  constructor(
    private route:              ActivatedRoute,
    private router:             Router,
    private prestataireService: PrestataireService,
    private authService:        AuthService,
    private http:               HttpClient,
    private parametreService:   ParametreService,
    private mediaService:       MediaService,
    private translate:          TranslateService
  ) {}

  ngOnInit(): void {
    this.visiteCode = this.route.snapshot
      .paramMap.get('visiteCode') || '';
    const parts     = this.visiteCode.split('_');
    this.codeVisite = parts[parts.length - 1];

    const resolved = this.route.snapshot.data['visiteInfo'];
    if (resolved) {
      this.chargerInfosVisite(resolved);
    } else {
      this.chargerDepuisSession();
    }

    this.chargerMedicaments();
    this.initSearchDebounce();
    this.chargerParametres();
    document.addEventListener(
      'click', this.fermerDropdownExterieur);
  }

  private chargerParametres(): void {
    this.parametreService.getBoolean('PARTAGE_WHATSAPP_ACTIF', true)
      .pipe(takeUntil(this.destroy$)).subscribe(v => this.whatsappActif = v);
    this.parametreService.getBoolean('PARTAGE_SMS_ACTIF', true)
      .pipe(takeUntil(this.destroy$)).subscribe(v => this.smsActif = v);
    this.parametreService.getBoolean('DOCUMENT_OBLIGATOIRE', false)
      .pipe(takeUntil(this.destroy$)).subscribe(v => this.documentObligatoire = v);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    document.removeEventListener(
      'click', this.fermerDropdownExterieur);
  }

  // ── Chargement ────────────────────────────────────────

  private chargerInfosVisite(data: any): void {
    this.nomAssure     = data.nomAssure     || '';
    this.nomAyantDroit = data.nomAyantDroit || '';
    this.lienParente   = data.lienParente   || '';
    this.souscripteur  = data.souscripteur  || '';
    this.groupe        = data.groupe        || null;
    this.prestataireId = data.prestataireId || '';
    if (this.codeVisite) this.chargerConsommation();
  }

  private chargerDepuisSession(): void {
    const info = this.prestataireService.getVisiteInfo();
    if (info) this.prestataireId = info.prestataireId;
    if (this.codeVisite) {
      this.isLoading = true;
      this.prestataireService
        .getInfosVisite(this.codeVisite)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next:  d => {
            this.chargerInfosVisite(d);
            this.isLoading = false;
          },
          error: () => { this.isLoading = false; }
        });
    }
  }

  private chargerConsommation(): void {
    this.isLoadingConso = true;
    this.prestataireService
      .getConsommation(this.codeVisite)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next:  d => {
          this.consommation   = d;
          this.isLoadingConso = false;
        },
        error: () => { this.isLoadingConso = false; }
      });
  }

  private chargerMedicaments(): void {
    this.isLoadingMeds = true;
    this.prestataireService.getMedicaments()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: meds => {
          this.medicaments        = meds;
          this.medicamentsFiltres = meds;
          this.isLoadingMeds      = false;
        },
        error: () => { this.isLoadingMeds = false; }
      });
  }

  private initSearchDebounce(): void {
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(term => {
      this.medicamentsFiltres = term
        ? this.medicaments.filter(m =>
            m.nom.toUpperCase()
              .includes(term.toUpperCase()))
        : this.medicaments;
    });
  }

  // ── Dropdown médicaments ──────────────────────────────

  onSearchMedicament(term: string): void {
    this.searchSubject.next(term);
  }

  ouvrirDropdown(index: number, event: Event): void {
    event.stopPropagation();
    const el = (event.target as HTMLElement)
                 .closest('.input-group') as HTMLElement;
    if (el) {
      const rect      = el.getBoundingClientRect();
      this.dropdownX  = rect.left;
      this.dropdownY  = rect.bottom + 2;
      this.dropdownW  = Math.max(rect.width, 320);
    }
    this.dropdownOuvertIndex = index;
    this.medicamentsFiltres  = this.medicaments;
    this.searchMedicament    = '';
  }

  viderRecherche(): void {
    this.searchMedicament   = '';
    this.medicamentsFiltres = this.medicaments;
  }

  selectionnerMedicament(
    index: number,
    med:   Medicament
  ): void {
    this.lignes[index].medicamentId = med.id;
    this.lignes[index].nom          = med.nom;
    this.lignes[index].codification = med.codification || '';
    this.lignes[index].prix         = med.prix         || null;
    this.dropdownOuvertIndex        = null;
    this.searchMedicament           = '';
  }

  private fermerDropdownExterieur = (): void => {
    this.dropdownOuvertIndex = null;
  };

  // ── Dialog ajout médicament ───────────────────────────

  ouvrirDialogAjout(): void {
    this.dropdownOuvertIndex = null;
    this.dialogNom           = '';
    this.dialogErreur        = '';
    this.dialogStatut        = 'idle';
    this.dialogMedTrouve     = null;
    this.dialogLoading       = false;
    this.dialogOuvert        = true;
  }

  fermerDialog(): void {
    this.dialogOuvert = false;
  }

  confirmerAjoutMedicament(): void {
    const nom = this.dialogNom.trim();
    if (!nom) {
      this.dialogErreur = 'Veuillez saisir un nom.';
      return;
    }

    const local = this.medicaments.find(
      m => m.nom.toUpperCase() === nom.toUpperCase()
    );

    if (local) {
      this.dialogStatut    = 'local';
      this.dialogMedTrouve = local;
      this._appliquerMedicamentDialog(local);
      return;
    }

    this.dialogLoading = true;
    this.dialogErreur  = '';

    this.prestataireService
      .rechercherOuCreerMedicament(nom)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: med => {
          this.dialogLoading   = false;
          this.dialogStatut    = med.nouveau
            ? 'nouveau' : 'bdd';
          this.dialogMedTrouve = med;
          if (med.nouveau) {
            this.medicaments        =
              [...this.medicaments, med];
            this.medicamentsFiltres =
              [...this.medicamentsFiltres, med];
          }
          this._appliquerMedicamentDialog(med);
        },
        error: () => {
          this.dialogLoading = false;
          this.dialogErreur  =
            'Erreur lors de la recherche.';
        }
      });
  }

  private _appliquerMedicamentDialog(
    med: Medicament
  ): void {
    const idx = this.lignes.length - 1;
    this.selectionnerMedicament(idx, med);
    setTimeout(() => this.fermerDialog(), 900);
  }

  // ── Dialog confirmation ───────────────────────────────

  ouvrirConfirmation(): void {
    if (!this.formulaireValide) return;
    this.dialogConfirmOuvert = true;
  }

  fermerConfirmation(): void {
    this.dialogConfirmOuvert = false;
  }

  // ── Gestion lignes ────────────────────────────────────

  nouvelleLigne(): LigneMedicament {
    return {
      medicamentId: null,
      nom:          '',
      codification: '',
      posologie:    '',
      prix:         null,
      quantite:     1
    };
  }

  ajouterLigne(): void {
    this.lignes.push(this.nouvelleLigne());
  }

  supprimerLigne(index: number): void {
    if (this.lignes.length > 1) {
      this.lignes.splice(index, 1);
    }
  }

  // ── Calculs ───────────────────────────────────────────

  get montantTotal(): number {
    return this.lignes.reduce(
      (sum, l) => sum + ((l.prix || 0) * (l.quantite || 1)),
      0
    );
  }

  get partZenithe(): number {
    return Math.round(this.montantTotal * 80 / 100);
  }

  get partAssure(): number {
    return this.montantTotal - this.partZenithe;
  }

  // ── Validation ────────────────────────────────────────

  get formulaireValide(): boolean {
    return !!this.natureAffection
      && this.lignes.length > 0
      && this.lignes.every(l =>
           l.nom.trim() !== ''
           && (l.prix || 0) > 0
           && l.quantite > 0);
  }

  // ── Soumission ────────────────────────────────────────

  soumettre(): void {
    this.dialogConfirmOuvert = false;
    if (!this.formulaireValide) return;

    this.isSubmitting = true;
    this.erreur       = '';

    const user      = this.authService.getStoredUser();
    const employeId = user?.utilisateurId ?? 0;

    const payload = {
      visiteId:         this.codeVisite,
      prestataireId:    this.prestataireId,
      naturePrestation: 'ordonnance',
      natureAffection:  this.natureAffection,
      employeId,
      lignes: this.lignes.map(l => ({
        nom:          l.nom,
        codification: l.codification || null,
        posologie:    l.posologie    || null,
        valeur:       l.prix,
        nbre:         l.quantite,
        taux:         80
      }))
    };

    this.prestataireService
      .soumettreOrdonnance(payload)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (res: any) => {
          this.isSubmitting     = false;
          this.success          = true;
          this.prestationIdCree = res?.prestationId ?? null;
          const base = document.getElementsByTagName('base')[0]?.getAttribute('href') || '/';
          const lng = this.translate.currentLang || 'fr';
          this.urlMobile = `${window.location.origin}${base}mobile/capture`
            + `/${this.codeVisite}/${this.prestationIdCree}/ordonnance?lang=${lng}`;
          sessionStorage.removeItem('visite_code');
          sessionStorage.removeItem('visite_nature');
          // Génération QR code après le prochain cycle de détection
          setTimeout(() => this.genererQRCode(), 100);
        },
        error: err => {
          this.isSubmitting = false;
          this.erreur = err?.error?.message
            || 'Erreur lors de la soumission.';
        }
      });
  }

  imprimerBon(): void {
    if (!this.prestationIdCree) return;
    const base = document.querySelector('base')?.getAttribute('href') || '/biometry/';
    window.open(`${base}public/prestataire/prestation/bon/${this.prestationIdCree}`, '_blank');
  }

  terminer(): void {
    this.router.navigate(['/public/admin/ordonnance']);
  }

  retour(): void {
    this.router.navigate(['/public/admin/ordonnance']);
  }

  copierUrl(): void {
    if (this.urlMobile) {
      navigator.clipboard.writeText(this.urlMobile).catch(() => {});
    }
  }

  private genererQRCode(): void {
    const canvas = this.qrCanvasRef?.nativeElement;
    if (!canvas || !this.urlMobile) return;
    QRCode.toCanvas(canvas, this.urlMobile, {
      width: 280,
      margin: 4,
      errorCorrectionLevel: 'H',
      color: { dark: '#000000', light: '#ffffff' }
    }).catch(err => console.error('QR code error:', err));
  }

  private get msgCapture(): string {
    return this.translate.currentLang === 'en'
      ? `Please film the document by clicking on this link:\n${this.urlMobile}`
      : `Veuillez filmer le document en cliquant sur ce lien :\n${this.urlMobile}`;
  }

  partagerWhatsApp(): void {
    if (!this.urlMobile) return;
    window.open(`https://wa.me/?text=${encodeURIComponent(this.msgCapture)}`, '_blank');
  }

  ouvrirSmsModal(): void {
    this.showSmsModal = true;
    this.smsNumero = '';
    this.smsResultat = '';
  }

  fermerSmsModal(): void {
    this.showSmsModal = false;
  }

  envoyerSms(): void {
    if (!this.smsNumero || !this.urlMobile) return;
    this.smsEnvoi = true;
    this.smsResultat = '';
    const body = {
      destinataireId: this.smsNumero,
      typeDest: 'assure',
      canal: 'sms',
      message: this.msgCapture,
      telephone: this.smsNumero,
      eventType: 'lien_capture',
      referenceId: String(this.prestationIdCree || '')
    };
    this.http.post('/notifications/envoyer', body)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => { this.smsEnvoi = false; this.smsResultat = 'ok'; },
        error: () => { this.smsEnvoi = false; this.smsResultat = 'erreur'; }
      });
  }

  verifierDocumentEnvoye(): void {
    if (!this.prestationIdCree) return;
    this.mediaService.getParPrestation(this.prestationIdCree)
      .pipe(takeUntil(this.destroy$))
      .subscribe({ next: medias => { this.documentEnvoye = medias && medias.length > 0; } });
  }

  get peutTerminer(): boolean {
    if (!this.documentObligatoire) return true;
    return this.documentEnvoye;
  }

  // ── Helpers ───────────────────────────────────────────

  formatMontant(val: number): string {
    if (!val && val !== 0) return '0';
    return val.toLocaleString('fr-FR');
  }

  getSoldeClass(s: number): string {
    if (s < 0)     return 'text-danger';
    if (s < 50000) return 'text-warning';
    return 'text-success';
  }

  min100(v: number): number {
    return Math.min(v || 0, 100);
  }

  getBarreClass(p: number): string {
    if (p >= 100) return 'progress-bar-danger';
    if (p >= 80)  return 'progress-bar-warning';
    return 'progress-bar-success';
  }

  get anneeEncours(): number {
    return new Date().getFullYear();
  }

  trackByIndex(i: number): number { return i; }

  survolItem(event: MouseEvent, survol: boolean): void {
    const el = event.currentTarget as HTMLElement;
    el.style.background = survol
      ? '#f5f5f5' : 'transparent';
  }
}