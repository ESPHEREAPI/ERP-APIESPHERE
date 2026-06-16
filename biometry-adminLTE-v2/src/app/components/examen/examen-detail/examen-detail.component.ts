import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Subject, forkJoin } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { ConsommationResponse } from '../../../services/consultation.service';
import { AuthService } from '../../../auth/auth.service';
import { ExamenService, LigneExamen, ValidationLigneExamenRequest } from '../../../services/examen.services';

@Component({
  selector: 'app-examen-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, TranslateModule, DecimalPipe],
  templateUrl: './examen-detail.component.html',
  styleUrls: ['./examen-detail.component.css']
})
export class ExamenDetailComponent implements OnInit, OnDestroy {

  prestationId!:   number;
  lignes:          LigneExamen[] = [];
  isLoading        = true;
  isSubmitting     = false;

  // Infos assuré
  visiteId:        string        = '';
  nomPrestataire:  string        = '';
  nomAssure:       string        = '';
  nomAyantDroit:   string        = '';
  souscripteur:    string        = '';
  groupe:          number | null = null;
  natureAffection: string        = '';

  // Consommation
  consommation:    ConsommationResponse | null = null;
  isLoadingConso   = false;

  // Modal
  showModalConfirmation = false;
  tauxErreur            = false;

  // Totaux calculés
  montantTotalValideCalc = 0;
  montantZenitheCalc     = 0;
  montantAssureCalc      = 0;

  private destroy$ = new Subject<void>();

  constructor(
    private route:         ActivatedRoute,
    private router:        Router,
    private examenService: ExamenService,
    private authService:   AuthService,
    private translate:     TranslateService
  ) {}

  ngOnInit(): void {
    this.prestationId = Number(
      this.route.snapshot.paramMap.get('prestationId')
    );
    this.chargerLignes();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // ── Profil ────────────────────────────────────────────────────

  get isPrestataire(): boolean {
    const profilCode = this.authService.getStoredUser()?.profilCode || '';
    return !['SERVICE_SANTE', 'SUP_ADMIN'].includes(profilCode);
  }

  get isSS(): boolean {
    return !this.isPrestataire;
  }

  // ── Lignes valides ────────────────────────────────────────────

  get hasLignesValides(): boolean {
    return this.lignes.some(l => l.etat === 'valide');
  }

  get countLignesValides(): number {
    return this.lignes.filter(l => l.etat === 'valide').length;
  }

  get toutesEncaissees(): boolean {
    return this.lignes.length > 0
      && this.lignes.some(l => l.etat === 'encaisse')
      && this.lignes.every(l => l.etat !== 'valide');
  }

  // ── Chargement ────────────────────────────────────────────────

  chargerLignes(): void {
    this.isLoading = true;
    this.examenService.getLignesByPrestation(this.prestationId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (lignes) => {
          this.lignes = lignes.map(l => ({
            ...l,
            valeurModifLocal:  l.valeurModif  ?? l.valeur          ?? 0,
            nbreModifLocal:    l.nbreModif    ?? l.nbre             ?? 1,
            tauxLocal:         l.taux != null ? Number(l.taux)     : 0,
            observationsLocal: l.observations ?? '',
            decisionLocale:    this.isEnAttente(l.etat) ? null : l.etat as any
          }));
          this.isLoading = false;
          this.recalculerTotaux();

          if (lignes.length > 0) {
            const p              = lignes[0];
            this.visiteId        = p.visiteId        || '';
            this.nomPrestataire  = p.nomPrestataire  || p.prestataireId || '';
            this.nomAssure       = p.nomAssure       || p.codeAdherent  || '';
            this.nomAyantDroit   = p.nomAyantDroit   || '';
            this.souscripteur    = p.souscripteur    || '';
            this.groupe          = p.groupe          || null;
            this.natureAffection = p.natureAffection || '';

            if (p.visiteId) {
              this.chargerConsommation(p.visiteId);
            }
          }
        },
        error: () => { this.isLoading = false; }
      });
  }

  chargerConsommation(visiteId: string): void {
    this.isLoadingConso = true;
    this.examenService.getConsommation(visiteId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next:  (d) => { this.consommation = d; this.isLoadingConso = false; },
        error: ()  => { this.consommation = null; this.isLoadingConso = false; }
      });
  }

  // ── Calculs ───────────────────────────────────────────────────

  recalculerTotaux(): void {
    this.montantTotalValideCalc = this.lignes.reduce((sum, l) => {
      if (l.decisionLocale === 'valide') {
        const val = l.valeurModifLocal ?? l.valeur ?? 0;
        const qte = l.nbreModifLocal   ?? l.nbre   ?? 1;
        return sum + (val * qte);
      }
      return sum;
    }, 0);

    this.montantZenitheCalc = this.lignes.reduce((sum, l) => {
      if (l.decisionLocale === 'valide') {
        const val  = l.valeurModifLocal ?? l.valeur ?? 0;
        const qte  = l.nbreModifLocal   ?? l.nbre   ?? 1;
        const taux = l.tauxLocal        ?? l.taux   ?? 100;
        return sum + (val * qte * taux / 100);
      }
      return sum;
    }, 0);

    this.montantAssureCalc =
      this.montantTotalValideCalc - this.montantZenitheCalc;
  }

  // ── Logique lignes ────────────────────────────────────────────

  isEnAttente(etat: string): boolean {
    return etat === 'attente_validation' || etat === 'enregistre';
  }

  isDecidable(etat: string): boolean {
    return this.isEnAttente(etat) || (this.isSS && etat === 'valide');
  }

  setDecision(ligne: LigneExamen, decision: 'valide' | 'rejete'): void {
    const modifiable = this.isEnAttente(ligne.etat)
      || (this.isSS && ligne.etat === 'valide');
    if (!modifiable) return;
    ligne.decisionLocale =
      ligne.decisionLocale === decision ? null : decision;
    this.recalculerTotaux();
  }

  onTauxChange(ligne: LigneExamen): void {
    if ((ligne.tauxLocal ?? 0) > 100) {
      ligne.tauxLocal = 100;
      this.tauxErreur = true;
    } else if ((ligne.tauxLocal ?? 0) < 0) {
      ligne.tauxLocal = 0;
      this.tauxErreur = true;
    } else {
      this.tauxErreur = false;
    }
    this.recalculerTotaux();
  }

  get lignesEnAttente(): LigneExamen[] {
    return this.lignes.filter(l =>
      this.isEnAttente(l.etat) || (this.isSS && l.etat === 'valide')
    );
  }

  get toutesDecidees(): boolean {
    return this.lignesEnAttente.length > 0
      && this.lignesEnAttente.every(l => l.decisionLocale !== null);
  }

  // ── Encaissement (Prestataire) ────────────────────────────────

  encaisserLigne(ligne: LigneExamen): void {
    if (ligne.etat !== 'valide') return;
    this.isSubmitting = true;

    this.examenService.encaisserLigne(ligne.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.isSubmitting = false;
          this.chargerLignes();
        },
        error: (err) => {
          console.error('Erreur encaissement ligne:', err);
          this.isSubmitting = false;
        }
      });
  }

  encaisserLignes(): void {
    if (!this.hasLignesValides) return;
    this.isSubmitting = true;

    const appels = this.lignes
      .filter(l => l.etat === 'valide')
      .map(l => this.examenService.encaisserLigne(l.id));

    forkJoin(appels)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.isSubmitting = false;
          this.chargerLignes();
        },
        error: (err) => {
          console.error('Erreur encaissement global:', err);
          this.isSubmitting = false;
        }
      });
  }

  // ── Soumission (SS) ───────────────────────────────────────────

  soumettre(): void {
    if (!this.toutesDecidees) return;
    this.showModalConfirmation = true;
  }

  annulerConfirmation(): void {
    this.showModalConfirmation = false;
  }

  confirmerSoumission(): void {
    this.showModalConfirmation = false;
    const lignesATraiter = this.lignesEnAttente
      .filter(l => l.decisionLocale !== null);

    if (lignesATraiter.length === 0) return;

    this.isSubmitting = true;
    const user      = this.authService.getStoredUser();
    const employeId = user?.utilisateurId ?? 0;

    const appels = lignesATraiter
      .filter(l => this.isEnAttente(l.etat) || l.decisionLocale !== l.etat)
      .map(l => {
      const valeurModifLocal        = l.valeurModifLocal != null ? Number(l.valeurModifLocal) : null;
      const nbreModifLocal          = l.nbreModifLocal   != null ? Number(l.nbreModifLocal)   : null;
      const tauxLocal               = l.tauxLocal        != null ? Number(l.tauxLocal)        : null;
      const request: ValidationLigneExamenRequest = {
        decision:            l.decisionLocale!,
        employeId,
        observations:        l.observationsLocal || null,
        valeurModif:         valeurModifLocal !== null
                             && valeurModifLocal !== Number(l.valeur ?? l.actePrelevement)
                             ? valeurModifLocal : null,
        nbreModif:           nbreModifLocal !== null
                             && nbreModifLocal !== Number(l.nbre ?? 1)
                             ? nbreModifLocal : null,
        taux:                tauxLocal !== null
                             && tauxLocal !== Number(l.taux)
                             ? tauxLocal : null,
        actePrelevementModif: null
      };
      return this.examenService.validerLigne(l.id, request);
    });

    forkJoin(appels)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.isSubmitting = false;
          this.chargerLignes();
        },
        error: () => { this.isSubmitting = false; }
      });
  }

  retour(): void {
    this.router.navigate(['/public/admin/examen']);
  }

  // ── Helpers ───────────────────────────────────────────────────

  formatMontant(val: number): string {
    if (!val && val !== 0) return '0';
    return val.toLocaleString('fr-FR');
  }

  formatDate(date: string): string {
    if (!date) return '';
    return new Date(date).toLocaleDateString('fr-FR', {
      day: '2-digit', month: '2-digit', year: 'numeric',
      hour: '2-digit', minute: '2-digit'
    });
  }

  getEtatClass(etat: string): string {
    switch (etat) {
      case 'attente_validation': return 'text-warning';
      case 'valide':             return 'text-success';
      case 'rejete':             return 'text-danger';
      case 'encaisse':           return 'text-primary';
      default:                   return 'text-muted';
    }
  }

  getEtatIcon(etat: string): string {
    switch (etat) {
      case 'attente_validation': return 'fa fa-clock-o';
      case 'valide':             return 'fa fa-check';
      case 'rejete':             return 'fa fa-times';
      case 'encaisse':           return 'fa fa-dollar';
      default:                   return 'fa fa-question';
    }
  }

  getEtatLabel(etat: string): string {
    switch (etat) {
      case 'attente_validation': return this.translate.instant('en_attente');
      case 'valide':             return this.translate.instant('valide');
      case 'rejete':             return this.translate.instant('rejete');
      case 'encaisse':           return this.translate.instant('encaisse');
      default:                   return etat;
    }
  }

  getBarreEncaisseClass(): string {
    if (!this.consommation) return 'progress-bar-success';
    const p = this.consommation.pourcentageEncaisse;
    if (p >= 100) return 'progress-bar-danger';
    if (p >= 80)  return 'progress-bar-warning';
    return 'progress-bar-success';
  }

  getBarreProjectionClass(): string {
    if (!this.consommation) return 'progress-bar-info';
    const p = this.consommation.pourcentageProjecte;
    if (p >= 100) return 'progress-bar-danger';
    if (p >= 80)  return 'progress-bar-warning';
    return 'progress-bar-info';
  }

  min100(val: number): number { return Math.min(val || 0, 100); }

  getSoldeClass(solde: number): string {
    if (solde < 0)     return 'text-danger';
    if (solde < 50000) return 'text-warning';
    return 'text-success';
  }

  get anneeEncours(): number { return new Date().getFullYear(); }

  trackById(index: number, item: LigneExamen): number { return item.id; }
}