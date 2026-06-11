import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { Router } from '@angular/router';
import { DashboardAdherentDTO, DashboardAdherentService, FiltreVisitesDTO } from '../../core/services/DashboardAdherentService';
import { SessionContext } from '../../core/auth/SessionContext';
import { ReferentielService, SouscripteurActifDTO, AdherentSimpleDTO } from '../../core/services/referentiel.service';


/**
 * Tableau de bord personnel de l'adhérent.
 *
 * Affiche :
 *  - Identité + statut contrat + alerte échéance
 *  - Jauge plafond global
 *  - KPIs consommation (PEC, TM, visites)
 *  - Liste des ayants droit avec leur consommation
 *  - 5 dernières visites
 *
 * Route : /dashboard/adherent
 * Guard : profils ADHERENT, DII, SERVICE_SANTE
 */
@Component({
  selector: 'app-dashboard-adherent',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard-adherent.component.html',
  styleUrl: './dashboard-adherent.component.css'
})
export class DashboardAdherentComponent implements OnInit, OnDestroy {

  data: DashboardAdherentDTO | null = null;
  loading  = false;
  error: string | null = null;

  // Sélecteurs DII / Service Santé
  souscripteursActifs: SouscripteurActifDTO[] = [];
  selectedSouscripteur: SouscripteurActifDTO | null = null;
  adherentsList: AdherentSimpleDTO[] = [];
  selectedAdherent: AdherentSimpleDTO | null = null;

  // Supervision DII/Service Santé (recherche manuelle)
  codeRecherche = '';

  // Mémorise le dernier code chargé pour refresh sans repasser par dropdown
  private lastLoadedCode: string | undefined;

  // Pagination des visites
  pageVisites  = 0;
  sizeVisites  = 10;
  totalVisites = 0;

  get totalPages(): number {
    return Math.ceil(this.totalVisites / this.sizeVisites);
  }
  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }

  // Filtres de recherche sur l'historique des visites
  filtrePrestataire   = '';
  filtreDateDebut     = '';
  filtreDateFin       = '';
  filtreType          = '';
  filtreAyantDroit    = '';
  filtresActifs       = false;

  private readonly destroy$ = new Subject<void>();
  private sessionLoaded = false;

  constructor(
    public service: DashboardAdherentService,
    public session: SessionContext,
    private router: Router,
    private referentielService: ReferentielService
  ) {}

  isDIIOrServiceSante(): boolean {
    return this.session.isDII() || this.session.isServiceSante();
  }

  goToSouscripteurDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

  onSouscripteurChange(): void {
    this.adherentsList    = [];
    this.selectedAdherent = null;
    this.lastLoadedCode   = undefined;  // réinitialiser au changement de souscripteur
    this.data             = null;
    this.error            = null;
    if (this.selectedSouscripteur) {
      this.referentielService.getAdherentsByPolice(this.selectedSouscripteur.police).subscribe({
        next: list => { this.adherentsList = list; },
        error: err => console.error('[DashboardAdherent] Adhérents par police:', err)
      });
    }
  }

  onAdherentChange(): void {
    if (this.selectedAdherent) {
      this.load(this.selectedAdherent.codeAdherent);
    }
  }

  ngOnInit(): void {
    // Charger la liste des souscripteurs actifs pour DII / Service Santé
    if (this.isDIIOrServiceSante()) {
      this.referentielService.getSouscripteursActifs().subscribe({
        next: list => { this.souscripteursActifs = list; },
        error: err => console.error('[DashboardAdherent] Souscripteurs actifs:', err)
      });
    }

    this.service.loading$
      .pipe(takeUntil(this.destroy$))
      .subscribe(v => this.loading = v);

    this.service.data$
      .pipe(takeUntil(this.destroy$))
      .subscribe(d => {
        this.data = d;
        if (d) {
          this.totalVisites = d.totalVisites ?? 0;
          this.pageVisites  = d.pageVisites  ?? 0;
        }
      });

    // Charger après que la session soit prête
    this.session.session$
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        if (!this.sessionLoaded) {
          this.sessionLoaded = true;
          this.load();
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // ── Chargement ─────────────────────────────────────────────────────────────

  load(code?: string): void {
    this.error = null;

    let codeToUse: string | undefined;

    if (this.isDIIOrServiceSante()) {
      // DII / Service Santé : utiliser UNIQUEMENT le code explicite (dropdown ou recherche)
      // Jamais la session — un DII ne doit voir que l'adhérent qu'il a sélectionné
      codeToUse = code?.trim() || this.lastLoadedCode;
      if (!codeToUse) {
        // Pas encore de sélection → attendre, sans message d'erreur
        return;
      }
    } else {
      // ADHERENT connecté : son propre code depuis la session
      codeToUse = code?.trim() || this.session.codeAdherent;
      if (!codeToUse) {
        this.error = 'Code adhérent introuvable dans la session.';
        return;
      }
    }

    // Mémoriser pour les refresh suivants
    this.lastLoadedCode = codeToUse;

    const filtres: FiltreVisitesDTO = {};
    if (this.filtrePrestataire.trim())  filtres.prestataireId  = this.filtrePrestataire.trim();
    if (this.filtreDateDebut)           filtres.dateDebut       = this.filtreDateDebut;
    if (this.filtreDateFin)             filtres.dateFin         = this.filtreDateFin;
    if (this.filtreType)                filtres.typePrestation  = this.filtreType;
    if (this.filtreAyantDroit.trim())   filtres.codeAyantDroit  = this.filtreAyantDroit.trim();

    this.service.load(codeToUse, this.pageVisites, this.sizeVisites, filtres).subscribe({
      error: err => {
        console.error('[DashboardAdherent]', err);
        this.error = 'Erreur lors du chargement du tableau de bord.';
      }
    });
  }

  rechercher(): void {
    if (this.codeRecherche.trim()) {
      this.load(this.codeRecherche.trim());
    }
  }

  goToPage(p: number): void {
    if (p < 0 || p >= this.totalPages) return;
    this.pageVisites = p;
    this.load(this.lastLoadedCode);
  }

  // Refresh : réutilise le dernier code chargé (dropdown ou recherche)
  refresh(): void { this.load(this.lastLoadedCode); }

  appliquerFiltres(): void {
    this.filtresActifs = !!(this.filtrePrestataire || this.filtreDateDebut ||
                            this.filtreDateFin || this.filtreType || this.filtreAyantDroit);
    this.pageVisites = 0;
    this.load(this.lastLoadedCode);
  }

  reinitialiserFiltres(): void {
    this.filtrePrestataire  = '';
    this.filtreDateDebut    = '';
    this.filtreDateFin      = '';
    this.filtreType         = '';
    this.filtreAyantDroit   = '';
    this.filtresActifs      = false;
    this.pageVisites        = 0;
    this.load(this.lastLoadedCode);
  }

  // ── Helpers template ───────────────────────────────────────────────────────

  getPlafondBarWidth(): string {
    const p = this.data?.plafond?.pourcentageConsomme ?? 0;
    return `${Math.min(p, 100)}%`;
  }

  getPlafondBarClass(): string {
    return this.service.getPlafondBarClass(this.data?.plafond?.niveau ?? 'NORMAL');
  }

  getEcheanceClass(): string {
    return this.service.getEcheanceClass(this.data?.niveauAlertEcheance ?? 'NORMAL');
  }

  getEcheanceLabel(): string {
    const j = this.data?.joursAvantEcheance ?? 0;
    const n = this.data?.niveauAlertEcheance;
    if (n === 'EXPIRE')  return 'Contrat expiré';
    if (n === 'DANGER')  return `Expire dans ${j} jour(s) !`;
    if (n === 'WARNING') return `Expire dans ${j} jours`;
    return `${j} jours restants`;
  }

  getEtatBadgeClass(etat: string): string {
    return this.service.getEtatBadgeClass(etat);
  }

  formatCurrency(v: number): string { return this.service.formatCurrency(v); }
  formatDate(d: string): string      { return this.service.formatDate(d); }

  getTauxBadgeClass(taux: number | null): string {
    if (taux == null) return 'badge-secondary';
    if (taux >= 80)   return 'badge-success';
    if (taux >= 50)   return 'badge-warning';
    return 'badge-danger';
  }

  getTauxTextClass(taux: number | null): string {
    if (taux == null) return '';
    if (taux >= 80)   return 'text-success';
    if (taux >= 50)   return 'text-warning';
    return 'text-danger';
  }
}