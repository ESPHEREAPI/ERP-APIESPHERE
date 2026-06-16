import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { Subject, forkJoin } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import {
  OrdonnanceService,
  LigneOrdonnance,
  ValidationLigneRequest
} from '../../../services/ordonnance.service';
import { ConsommationResponse } from '../../../services/consultation.service';
import { AuthService } from '../../../auth/auth.service';

@Component({
  selector: 'app-ordonnance-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, TranslateModule, DecimalPipe],
  templateUrl: './ordonnance-detail.component.html',
  styleUrls: ['./ordonnance-detail.component.css']
})
export class OrdonnanceDetailComponent implements OnInit, OnDestroy {

  prestationId!: number;
  lignes: LigneOrdonnance[] = [];
  isLoading = true;
  isSubmitting = false;
  nomPrestataire: string = '';

  // Infos prestation (depuis la première ligne)
  visiteId: string = '';
  nomAssure: string = '';
  nomAyantDroit: string = '';   // ← propriété manquante
  souscripteur: string = '';
  groupe: number | null = null;
  natureAffection: string = '';

  // Consommation
  consommation: ConsommationResponse | null = null;
  isLoadingConso = false;

  // Modal confirmation
  showModalConfirmation = false;
  tauxErreur = false;

  // Totaux
  get montantTotalValide(): number {
    return this.lignes.reduce((sum, l) => {
      if (l.decisionLocale === 'valide') {
        const pu = l.valeurModifLocal ?? l.valeur;
        const qte = l.nbreModifLocal ?? l.nbre;
        return sum + (pu * qte);
      }
      return sum;
    }, 0);
  }

  get montantZenithe(): number {
    return this.lignes.reduce((sum, l) => {
      if (l.decisionLocale === 'valide') {
        const pu = l.valeurModifLocal ?? l.valeur;
        const qte = l.nbreModifLocal ?? l.nbre;
        const taux = l.tauxLocal ?? l.taux ?? 100;
        return sum + (pu * qte * taux / 100);
      }
      return sum;
    }, 0);
  }

  get montantAssure(): number {
    return this.montantTotalValide - this.montantZenithe;
  }

  private destroy$ = new Subject<void>();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private ordonnanceService: OrdonnanceService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.prestationId = Number(this.route.snapshot.paramMap.get('prestationId'));
    this.chargerLignes();
     // DEBUG — à supprimer après
  const user = this.authService.getStoredUser();
  console.log('profilCode:', user?.profilCode);
  console.log('isPrestataire:', this.isPrestataire);
  console.log('isSS:', this.isSS);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  chargerLignes(): void {
    this.isLoading = true;
    this.ordonnanceService.getLignesByPrestation(this.prestationId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (lignes) => {
          this.lignes = lignes.map(l => ({
            ...l,
            valeurModifLocal: l.valeurModif ?? l.valeur,
            nbreModifLocal: l.nbreModif ?? l.nbre,
            // ✅ Ne force pas 80 si le taux existe déjà
            tauxLocal: l.taux != null ? Number(l.taux) : 0,
            observationsLocal: l.observations ?? '',
            decisionLocale: this.isEnAttente(l.etat) ? null : l.etat as any

          }));

          this.isLoading = false;

          // Charger consommation si on a un visiteId
          if (lignes.length > 0) {
            // Le visiteId vient de la prestation — on le récupère via LigneEnAttenteResponse
            const premiere = lignes[0] as any;
            console.log(premiere);
            if (premiere.visiteId) {
              this.visiteId = premiere.visiteId;
              this.chargerConsommation(premiere.visiteId);
            }
            this.nomAssure = premiere.nomAssure || premiere.codeAdherent || '';
            this.souscripteur = premiere.souscripteur || '';
            this.groupe = premiere.groupe || null;
            this.natureAffection = premiere.natureAffection || '';
            this.nomPrestataire = premiere.nomPrestataire || premiere.prestataireId || '';
            this.nomAyantDroit = premiere.nomAyantDroit || premiere.codeAyantDroit || '';
          }
        },
        error: () => { this.isLoading = false; }
      });
  }

  chargerConsommation(visiteId: string): void {
    this.isLoadingConso = true;
    this.ordonnanceService.getConsommation(visiteId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (d) => { this.consommation = d; this.isLoadingConso = false; },
        error: () => { this.consommation = null; this.isLoadingConso = false; }
      });
  }

  // ── Logique lignes ────────────────────────────────────────────────

  isEnAttente(etat: string): boolean {
    return etat === 'attente_validation' || etat === 'enregistre';
  }

  // Ligne sur laquelle l'agent SS peut encore prendre une décision
  isDecidable(etat: string): boolean {
    return this.isEnAttente(etat) || (this.isSS && etat === 'valide');
  }

  setDecision(ligne: LigneOrdonnance, decision: 'valide' | 'rejete'): void {
    const modifiable = this.isEnAttente(ligne.etat)
      || (this.isSS && ligne.etat === 'valide');
    if (!modifiable) return;
    ligne.decisionLocale = ligne.decisionLocale === decision ? null : decision;
  }

  onTauxChange(ligne: LigneOrdonnance): void {
    if ((ligne.tauxLocal ?? 0) > 100) {
      ligne.tauxLocal = 100;
      this.tauxErreur = true;
    } else if ((ligne.tauxLocal ?? 0) < 0) {
      ligne.tauxLocal = 0;
      this.tauxErreur = true;
    } else {
      this.tauxErreur = false;
    }
  }

  get lignesEnAttente(): LigneOrdonnance[] {
    return this.lignes.filter(l =>
      this.isEnAttente(l.etat) || (this.isSS && l.etat === 'valide')
    );
  }

  get toutesDecidees(): boolean {
    return this.lignesEnAttente.every(l => l.decisionLocale !== null);
  }

  // ── Soumission ────────────────────────────────────────────────────

  soumettre(): void {
    if (!this.toutesDecidees) return;
    this.showModalConfirmation = true;
  }

  annulerConfirmation(): void {
    this.showModalConfirmation = false;
  }

  confirmerSoumission(): void {
    this.isSubmitting = true;
    this.showModalConfirmation = false;
    const user = this.authService.getStoredUser();
    const employeId = user?.utilisateurId ?? 0;

    const appels = this.lignesEnAttente
      .filter(l => l.decisionLocale !== null && (this.isEnAttente(l.etat) || l.decisionLocale !== l.etat))
      .map(l => {
        // ✅ Conversion en number pour comparaison fiable
        const valeurModifLocal = l.valeurModifLocal != null ? Number(l.valeurModifLocal) : null;
        const nbreModifLocal = l.nbreModifLocal != null ? Number(l.nbreModifLocal) : null;
        const tauxLocal = l.tauxLocal != null ? Number(l.tauxLocal) : null;
        const request: ValidationLigneRequest = {
          decision: l.decisionLocale!,
          employeId,
          observations: l.observationsLocal || null,
          // ✅ Envoie la valeur si différente, sinon null
          valeurModif: valeurModifLocal !== null && valeurModifLocal !== Number(l.valeur)
            ? valeurModifLocal
            : null,
          nbreModif: nbreModifLocal !== null && nbreModifLocal !== Number(l.nbre)
            ? nbreModifLocal
            : null,
          actePrelevementModif: null,
          taux: tauxLocal !== null && tauxLocal !== Number(l.taux)
            ? tauxLocal : null
        };
        return this.ordonnanceService.validerLigne(l.id, request);
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
    this.router.navigate(['/public/admin/ordonnance']);
  }

  // ── Helpers ───────────────────────────────────────────────────────

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
      case 'valide': return 'text-success';
      case 'rejete': return 'text-danger';
      case 'encaisse': return 'text-primary';
      default: return 'text-muted';
    }
  }

  getEtatLabel(etat: string): string {
    switch (etat) {
      case 'attente_validation': return 'Attente de validation';
      case 'valide': return 'Validé';
      case 'rejete': return 'Rejeté';
      case 'encaisse': return 'Encaissé';
      default: return etat;
    }
  }

  // Consommation helpers
  getBarreEncaisseClass(): string {
    if (!this.consommation) return 'progress-bar-success';
    const p = this.consommation.pourcentageEncaisse;
    if (p >= 100) return 'progress-bar-danger';
    if (p >= 80) return 'progress-bar-warning';
    return 'progress-bar-success';
  }

  getBarreProjectionClass(): string {
    if (!this.consommation) return 'progress-bar-info';
    const p = this.consommation.pourcentageProjecte;
    if (p >= 100) return 'progress-bar-danger';
    if (p >= 80) return 'progress-bar-warning';
    return 'progress-bar-info';
  }

  min100(val: number): number { return Math.min(val || 0, 100); }

  getSoldeClass(solde: number): string {
    if (solde < 0) return 'text-danger';
    if (solde < 50000) return 'text-warning';
    return 'text-success';
  }

  get anneeEncours(): number { return new Date().getFullYear(); }

  trackById(index: number, item: LigneOrdonnance): number { return item.id; }

  getEtatIcon(etat: string): string {
    switch (etat) {
      case 'attente_validation': return 'fa fa-clock-o';
      case 'valide': return 'fa fa-check';
      case 'rejete': return 'fa fa-times';
      case 'encaisse': return 'fa fa-dollar';
      default: return 'fa fa-question';
    }
  }

  // Ajoutez après isSubmitting
get isPrestataire(): boolean {
    const profilCode = this.authService
        .getStoredUser()?.profilCode || '';
    return !['SERVICE_SANTE',
             'SUP_ADMIN'].includes(profilCode);
}

get isSS(): boolean {
    return !this.isPrestataire;
}

// Toutes les lignes sont encaissées
get toutesEncaissees(): boolean {
    return this.lignes.length > 0
      && this.lignes.some(l => l.etat === 'encaisse')
      && this.lignes.every(l => l.etat !== 'valide');
}
imprimerBon(): void {
    window.open(
        `/public/admin/ordonnance/bon/`
        + `${this.prestationId}`,
        '_blank');
}

// Vérifie s'il y a des lignes à encaisser
get hasLignesValides(): boolean {
  return this.lignes.some(l => l.etat === 'valide');
}

// Encaisse toutes les lignes avec etat === 'valide'

get countLignesValides(): number {
  return this.lignes.filter(l => l.etat === 'valide').length;
}




encaisserLigne(ligne: LigneOrdonnance): void {
  if (ligne.etat !== 'valide') return;
  this.isSubmitting = true;

  this.ordonnanceService.encaisserLigne(ligne.id)
    .pipe(takeUntil(this.destroy$))
    .subscribe({
      next: () => {
        this.isSubmitting = false;
        this.chargerLignes();
      },
      error: (err) => {
        console.error('Erreur encaissement:', err);
        this.isSubmitting = false;
      }
    });
}

encaisserLignes(): void {
  if (!this.hasLignesValides) return;
  this.isSubmitting = true;

  const appels = this.lignes
    .filter(l => l.etat === 'valide')
    .map(l => this.ordonnanceService.encaisserLigne(l.id));

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
}