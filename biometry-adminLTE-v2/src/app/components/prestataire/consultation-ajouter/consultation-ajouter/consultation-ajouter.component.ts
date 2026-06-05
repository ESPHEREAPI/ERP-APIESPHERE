import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule }                  from '@angular/common';
import { FormsModule }                   from '@angular/forms';
import { RouterModule, ActivatedRoute,
         Router }                        from '@angular/router';
import { TranslateModule }               from '@ngx-translate/core';
import { Subject }                       from 'rxjs';
import { takeUntil }                     from 'rxjs/operators';
import { PrestataireService }            from '../../../../services/prestataire.service';
import { AuthService }                   from '../../../../auth/auth.service';

@Component({
  selector:    'app-consultation-ajouter',
  standalone:  true,
  imports:     [CommonModule, FormsModule,
                RouterModule, TranslateModule],
  templateUrl: './consultation-ajouter.component.html',
  styleUrls:   ['./consultation-ajouter.component.css']
})
export class ConsultationAjouterComponent
       implements OnInit, OnDestroy {

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
  payante:          boolean      = true;
  typeConsultation: string       = '';
  montant:          number|null  = null;

  // ── Types consultation ────────────────────────────────
  typesConsultation: { code: string; libelle: string }[] = [
    { code: 'CS0', libelle: 'CONSULTATION GENERALISTE'     },
    { code: 'CS1', libelle: 'CONSULTATION SPECIALISTE'     },
    { code: 'CS2', libelle: 'CONSULTATION SPECIALISTE PRIVE' }
  ];

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

  private destroy$ = new Subject<void>();

  constructor(
    private route:              ActivatedRoute,
    private router:             Router,
    private prestataireService: PrestataireService,
    private authService:        AuthService
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
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
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
    const idVisite = this.visiteCode || this.codeVisite;
    this.prestataireService
      .getConsommation(idVisite)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next:  d => {
          this.consommation   = d;
          this.isLoadingConso = false;
        },
        error: () => { this.isLoadingConso = false; }
      });
  }

  // ── Calculs ───────────────────────────────────────────

  get partZenithe(): number {
    if (!this.montant || !this.payante) return 0;
    return Math.round(this.montant * 80 / 100);
  }

  get partAssure(): number {
    if (!this.montant || !this.payante) return 0;
    return this.montant - this.partZenithe;
  }

  // ── Validation ────────────────────────────────────────

  get formulaireValide(): boolean {
    return !!this.typeConsultation
      && (this.payante ? (this.montant || 0) > 0 : true);
  }

  // ── Dialog confirmation ───────────────────────────────

  ouvrirConfirmation(): void {
    if (!this.formulaireValide) return;
    this.dialogConfirmOuvert = true;
  }

  fermerConfirmation(): void {
    this.dialogConfirmOuvert = false;
  }

  // ── Soumission ────────────────────────────────────────

  soumettre(): void {
    this.dialogConfirmOuvert = false;
    this.isSubmitting        = true;
    this.erreur              = '';

    const user      = this.authService.getStoredUser();
    const employeId = user?.utilisateurId ?? 0;

    const payload = {
      visiteId:         this.codeVisite,
      prestataireId:    this.prestataireId,
      typeConsultation: this.typeConsultation,
      montant:          this.payante ? this.montant : 0,
      payante:          this.payante,
      employeId
    };

    this.prestataireService
      .soumettreConsultation(payload)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.isSubmitting = false;
          this.success      = true;
          sessionStorage.removeItem('visite_code');
          sessionStorage.removeItem('visite_nature');
          setTimeout(() =>
            this.router.navigate(
              ['/public/admin/consultation']), 2000);
        },
        error: err => {
          this.isSubmitting = false;
          this.erreur = err?.error?.message
            || 'Erreur lors de la soumission.';
        }
      });
  }

  retour(): void {
    this.router.navigate(['/public/admin/consultation']);
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
}