import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  RouterModule, ActivatedRoute,
  Router
} from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { from, of, Subject } from 'rxjs';
import {
  takeUntil, debounceTime,
  distinctUntilChanged,
  concatMap,
  catchError,
  first
} from 'rxjs/operators';
import {
  ExamenActe,
  PrestataireService,
  TypePrestation
} from '../../../../services/prestataire.service';
import { AuthService } from '../../../../auth/auth.service';

interface LigneExamen {
  examenId: number | null;
  nom: string;
  codification: string;
  typeExamen: string;
  valeur: number | null;
  nbre: number;
}

@Component({
  selector: 'app-examen-ajouter',
  standalone: true,
  imports: [CommonModule, FormsModule,
    RouterModule, TranslateModule],
  templateUrl: './examen-ajouter.component.html',
  styleUrls: ['./examen-ajouter.component.css']
})
export class ExamenAjouterComponent
  implements OnInit, OnDestroy {

  // ── Infos visite ─────────────────────────────────────
  codeVisite: string = '';
  visiteCode: string = '';
  nomAssure: string = '';
  nomAyantDroit: string = '';
  lienParente: string = '';
  souscripteur: string = '';
  groupe: number | null = null;
  prestataireId: string = '';

  // ── Formulaire ───────────────────────────────────────
  natureAffection: string = '';
  lignes: LigneExamen[] = [this.nouvelleLigne()];

  // ── Examens / dropdown ───────────────────────────────
  examens: ExamenActe[] = [];
  examensFiltres: ExamenActe[] = [];
  typesExamen: TypePrestation[] = [];
  searchExamen: string = '';
  dropdownOuvertIndex: number | null = null;
  isLoadingExamens: boolean = false;

  // Ajoutez ces propriétés
  taux: number = 0;
  police: string = '';
  groupeNum: number = 0;
  codeAdherent: string = '';

  dropdownX = 0;
  dropdownY = 0;
  dropdownW = 340;

  // ── Dialog ajout examen ──────────────────────────────
  dialogOuvert = false;
  dialogNom = '';
  dialogLoading = false;
  dialogErreur = '';
  dialogStatut: 'idle' | 'local' | 'bdd' | 'nouveau' = 'idle';
  dialogExamenTrouve: ExamenActe | null = null;

  // ── Dialog confirmation ───────────────────────────────
  dialogConfirmOuvert = false;

  // ── Consommation ─────────────────────────────────────
  consommation: any = null;
  isLoadingConso: boolean = false;

  // ── État ─────────────────────────────────────────────
  isLoading: boolean = false;
  isSubmitting: boolean = false;
  erreur: string = '';
  success: boolean = false;

  private destroy$ = new Subject<void>();
  private searchSubject = new Subject<string>();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private prestataireService: PrestataireService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.visiteCode = this.route.snapshot
      .paramMap.get('visiteCode') || '';
    const parts = this.visiteCode.split('_');
    this.codeVisite = parts[parts.length - 1];

    const resolved = this.route.snapshot.data['visiteInfo'];
    if (resolved) {
      this.chargerInfosVisite(resolved);
    } else {
      this.chargerDepuisSession();
    }

    this.chargerExamens();
    this.chargerTypesExamen();
    this.initSearchDebounce();
    document.addEventListener(
      'click', this.fermerDropdownExterieur);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    document.removeEventListener(
      'click', this.fermerDropdownExterieur);
  }

  // ── Chargement ────────────────────────────────────────



  private chargerDepuisSession(): void {
    const info = this.prestataireService.getVisiteInfo();
    if (info) this.prestataireId = info.prestataireId;
    if (this.codeVisite) {
      this.isLoading = true;
      this.prestataireService
        .getInfosVisite(this.codeVisite)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: d => {
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
        next: d => {
          this.consommation = d;
          this.isLoadingConso = false;
        },
        error: () => { this.isLoadingConso = false; }
      });
  }

  private chargerExamens(): void {
    this.isLoadingExamens = true;
    this.prestataireService.getExamensActes()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: e => {
          this.examens = e;
          this.examensFiltres = e;
          this.isLoadingExamens = false;
        },
        error: () => { this.isLoadingExamens = false; }
      });
  }

  private chargerTypesExamen(): void {
    this.prestataireService.getTypesExamen()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: t => { this.typesExamen = t; },
        error: () => {
          this.typesExamen = [
            {
              id: 'AH01', nom: 'Acte de Chirurgie',
              affiche: 1, categorie: 'CHIRURGIE'
            },
            {
              id: 'BI01', nom: 'Analyses Biologiques',
              affiche: 1, categorie: 'BIOLOGIE'
            },
            {
              id: 'IME01', nom: 'Echographie',
              affiche: 1, categorie: 'IMAGERIE'
            },
            {
              id: 'IMR01', nom: 'Radiologie',
              affiche: 1, categorie: 'IMAGERIE'
            },
            {
              id: '5', nom: 'Scanner',
              affiche: 1, categorie: 'IMAGERIE'
            },
            {
              id: '6', nom: 'Kinesitherapie',
              affiche: 1, categorie: 'REEDUCATION'
            },
            {
              id: '7',
              nom: 'Soins auxiliaires medicaux',
              affiche: 1, categorie: 'SOINS'
            }
          ];
        }
      });
  }

  private initSearchDebounce(): void {
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(term => {
      this.examensFiltres = term
        ? this.examens.filter(e =>
          e.nom.toUpperCase()
            .includes(term.toUpperCase()))
        : this.examens;
    });
  }

  // ── Dropdown examens ─────────────────────────────────

  onSearchExamen(term: string): void {
    this.searchSubject.next(term);
  }

  ouvrirDropdown(index: number, event: Event): void {
    event.stopPropagation();
    const el = (event.target as HTMLElement)
      .closest('.input-group') as HTMLElement;
    if (el) {
      const rect = el.getBoundingClientRect();
      this.dropdownX = rect.left;
      this.dropdownY = rect.bottom + 2;
      this.dropdownW = Math.max(rect.width, 340);
    }
    this.dropdownOuvertIndex = index;
    this.examensFiltres = this.examens;
    this.searchExamen = '';
  }

  viderRecherche(): void {
    this.searchExamen = '';
    this.examensFiltres = this.examens;
  }


  private fermerDropdownExterieur = (): void => {
    this.dropdownOuvertIndex = null;
  };

  // ── Dialog ajout examen ──────────────────────────────

  ouvrirDialogAjout(): void {
    this.dropdownOuvertIndex = null;
    this.dialogNom = '';
    this.dialogErreur = '';
    this.dialogStatut = 'idle';
    this.dialogExamenTrouve = null;
    this.dialogLoading = false;
    this.dialogOuvert = true;
  }

  fermerDialog(): void {
    this.dialogOuvert = false;
  }

  confirmerAjoutExamen(): void {
    const nom = this.dialogNom.trim();
    if (!nom) {
      this.dialogErreur =
        'Veuillez saisir un nom d\'examen.';
      return;
    }

    const local = this.examens.find(
      e => e.nom.toUpperCase() === nom.toUpperCase()
    );

    if (local) {
      this.dialogStatut = 'local';
      this.dialogExamenTrouve = local;
      this._appliquerExamenDialog(local);
      return;
    }

    this.dialogLoading = true;
    this.dialogErreur = '';

    this.prestataireService
      .rechercherOuCreerExamen(nom)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: ex => {
          this.dialogLoading = false;
          this.dialogStatut = ex.nouveau
            ? 'nouveau' : 'bdd';
          this.dialogExamenTrouve = ex;
          if (ex.nouveau) {
            this.examens = [...this.examens, ex];
            this.examensFiltres =
              [...this.examensFiltres, ex];
          }
          this._appliquerExamenDialog(ex);
        },
        error: () => {
          this.dialogLoading = false;
          this.dialogErreur =
            'Erreur lors de la recherche.';
        }
      });
  }

  private _appliquerExamenDialog(ex: ExamenActe): void {
    const derniere = this.lignes[this.lignes.length - 1];
    if (derniere.examenId !== null) {
      this.lignes.push(this.nouvelleLigne());
    }
    const idx = this.lignes.length - 1;
    this.selectionnerExamen(idx, ex);
    setTimeout(() => this.fermerDialog(), 900);
  }

  // ── Dialog confirmation ───────────────────────────────

ouvrirConfirmation(): void {
  if (!this.formulaireValide) return;
if (!this.police) {
  this.police = this.codeAdherent?.split("_")[1] ?? '';
}
  const typesUniques: string[] = [
    ...new Set(this.lignes.map(l => l.typeExamen))
  ];

  from(typesUniques)
    .pipe(
      // Appelle getTaux pour chaque type, un par un (séquentiel)
      concatMap(typeExamen =>
        this.prestataireService.getTaux(
          this.police,
          this.groupeNum,
          typeExamen,
          this.codeAdherent
        ).pipe(
          catchError(() => of({ taux: 0 }))
        )
      ),
      // Dès qu'on trouve un taux !== 0 → on s'arrête
      first(r => r.taux !== 0, { taux: 0 }),
      takeUntil(this.destroy$)
    )
    .subscribe(r => {
      this.taux = r.taux;
      this.dialogConfirmOuvert = true;
    });
}

  fermerConfirmation(): void {
    this.dialogConfirmOuvert = false;
  }

  // ── Gestion lignes ───────────────────────────────────

  nouvelleLigne(): LigneExamen {
    return {
      examenId: null,
      nom: '',
      codification: '',
      typeExamen: '',
      valeur: null,
      nbre: 1
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

  // ── Calculs ──────────────────────────────────────────

  get montantTotal(): number {
    return this.lignes.reduce(
      (sum, l) =>
        sum + (+(l.valeur || 0) * +(l.nbre || 1)),
      0
    );
  }



  // ── Validation ───────────────────────────────────────

  get formulaireValide(): boolean {
    return !!this.natureAffection
      && this.lignes.length > 0
      && this.lignes.every(l =>
        l.nom.trim() !== ''
        && l.typeExamen.trim() !== ''
        && (l.valeur || 0) > 0
        && l.nbre > 0
      );
  }

  // ── Soumission ───────────────────────────────────────

  soumettre(): void {
    this.dialogConfirmOuvert = false;
    if (!this.formulaireValide) return;

    this.isSubmitting = true;
    this.erreur = '';

    const user = this.authService.getStoredUser();
    const employeId = user?.utilisateurId ?? 0;

    const payload = {
      visiteId: this.codeVisite,
      prestataireId: this.prestataireId,
      naturePrestation: 'examen',
      natureAffection: this.natureAffection,
      employeId,
      lignes: this.lignes.map(l => ({
        nom: l.nom,
        codification: l.codification || null,
        typeExamen: l.typeExamen || null,
        valeur: l.valeur,
        nbre: l.nbre,
        taux: 0
      }))
    };

    this.prestataireService
      .soumettreExamen(payload)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.isSubmitting = false;
          this.success = true;
          sessionStorage.removeItem('visite_code');
          sessionStorage.removeItem('visite_nature');
          setTimeout(() =>
            this.router.navigate(
              ['/public/admin/examen']), 2000);
        },
        error: err => {
          this.isSubmitting = false;
          this.erreur = err?.error?.message
            || 'Erreur lors de la soumission.';
        }
      });
  }

  retour(): void {
    this.router.navigate(['/public/admin/examen']);
  }

  // ── Helpers ──────────────────────────────────────────

  formatMontant(val: number): string {
    if (!val && val !== 0) return '0';
    return val.toLocaleString('fr-FR');
  }

  getSoldeClass(s: number): string {
    if (s < 0) return 'text-danger';
    if (s < 50000) return 'text-warning';
    return 'text-success';
  }

  min100(v: number): number {
    return Math.min(v || 0, 100);
  }

  getBarreClass(p: number): string {
    if (p >= 100) return 'progress-bar-danger';
    if (p >= 80) return 'progress-bar-warning';
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



  // Modifiez chargerInfosVisite()
  private chargerInfosVisite(data: any): void {
    this.nomAssure = data.nomAssure || '';
    this.nomAyantDroit = data.nomAyantDroit || '';
    this.lienParente = data.lienParente || '';
    this.souscripteur = data.souscripteur || '';
    this.groupe = data.groupe || null;
    this.prestataireId = data.prestataireId || '';
    this.police = data.police || '';
    this.groupeNum = data.groupe || 0;
    this.codeAdherent = data.codeAdherent || '';
    if (this.codeVisite) this.chargerConsommation();
  }

  // Ajoutez cette méthode — appelée quand
  // l'utilisateur sélectionne un examen
  private chargerTauxPourExamen(
    typePrestation: string
  ): void {
    if (!this.police || !typePrestation) return;

    this.prestataireService
      .getTaux(
        this.police,
        this.groupeNum,
        typePrestation,
        this.codeAdherent)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: r => { this.taux = r.taux || 0; },
        error: () => { this.taux = 0; }
      });
  }

  // Modifiez selectionnerExamen() pour charger le taux
  selectionnerExamen(
    index: number,
    ex: ExamenActe
  ): void {
    this.lignes[index].examenId = ex.id;
    this.lignes[index].nom = ex.nom;
    this.lignes[index].codification = ex.codification || '';
    this.lignes[index].typeExamen = ex.typeExamen || '';
    this.lignes[index].valeur = ex.valeur || null;
    this.dropdownOuvertIndex = null;
    this.searchExamen = '';

    // ← Charger le taux pour ce type d'examen
    if (ex.typeExamen) {
      this.chargerTauxPourExamen(ex.typeExamen);
    }
  }

  get partZenithe(): number {
    return Math.round(
      this.montantTotal * this.taux / 100);
  }

  get partAssure(): number {
    return this.montantTotal - this.partZenithe;
  }
}