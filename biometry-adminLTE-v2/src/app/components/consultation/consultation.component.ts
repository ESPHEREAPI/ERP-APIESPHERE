import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import {
  ConsultationService,
  ConsultationEnAttente,
  ValidationConsultationRequest,
  AdherentInfo,
  ConsommationResponse          // ← ajouter
} from '../../services/consultation.service';
import { AuthService } from '../../auth/auth.service';

interface ConsultationRow extends ConsultationEnAttente {
  nomAdherent?: string;
  nomAyantDroit?: string;
  groupe?: number;
  souscripteur?: string;
  montantValide?: number;
  partZenithe?: number;
  partAssure?: number;
}

@Component({
  selector: 'app-consultation',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, TranslateModule],
  templateUrl: './consultation.component.html',
  styleUrls: ['./consultation.component.css']
})
export class ConsultationComponent implements OnInit, OnDestroy {

  // Données
  pagedConsultations: ConsultationRow[] = [];
  private toutesConsultations: ConsultationRow[] = [];
  isLoading = true;

  // Listes pour les dropdowns filtres
  // prestataires: string[] = [];
  // Changer l'interface pour stocker id + nom
  prestataires: { id: string; nom: string }[] = [];
  types: string[] = [];

  // Filtres
  filtrePrestataire = '';
  filtreType = '';
  filtreEtat = '';
  filtreDateMin = '';
  filtreDateMax = '';
  filtreSouscripteur = '';
  filtreAdherent = '';
  filtreAyantDroit = '';

  // Pagination
  pageSize = 10;
  currentPage = 0;
  totalItems = 0;
  totalPages = 0;

  // Modal valider
  showModalValider = false;
  showConfirmationValider = false;
  consultationSelectionnee: ConsultationRow | null = null;
  montantValideInput = 0;
  tauxInput = 0;
  observationInput = '';
  tauxErreur = false;
  // Consommation adhérent
  consommation: ConsommationResponse | null = null;
  isLoadingConso = false;

  // Modal rejeter
  showModalRejeter = false;

  // Modal encaisser
  showModalEncaisser = false;

  // Dropdown
  dropdownOuvertId: number | null = null;

  private destroy$ = new Subject<void>();
  private boundFermerDropdowns = () => this.fermerDropdowns();

  constructor(
    private consultationService: ConsultationService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.chargerPage(0);
    document.addEventListener('click', this.boundFermerDropdowns);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    document.removeEventListener('click', this.boundFermerDropdowns);
  }

  // ── Profil utilisateur ────────────────────────────────────────

  get isPrestataire(): boolean {
    const profilCode = this.authService.getStoredUser()?.profilCode || '';
    return !['SERVICE_SANTE', 'SUP_ADMIN'].includes(profilCode);
  }

  get isSS(): boolean {
    return !this.isPrestataire;
  }

  // ── Chargement ────────────────────────────────────────────────

  chargerPage(page: number): void {
    this.isLoading = true;
    console.log("chargement.....");
    
    this.consultationService.getConsultationsPaginees(page, this.pageSize, {
      prestataireId: this.filtrePrestataire || undefined,
      etat: this.filtreEtat || undefined,
      typeConsultation: this.filtreType || undefined,
      souscripteur:this.filtreSouscripteur || undefined,
      nomAdherent: this.filtreAdherent || undefined,
      nomAyantDroit: this.filtreAyantDroit || undefined,
      dateMin: this.filtreDateMin || undefined,
      dateMax: this.filtreDateMax || undefined
    }).pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (res) => {
          this.toutesConsultations = res.content.map(c => ({
            ...c,
            montantValide: (c as any).montantValide ?? c.montant,
            partZenithe: (c as any).partZenithe ?? this.calculPartZenithe((c as any).montantValide ?? c.montant, c.taux),
            partAssure: (c as any).partAssure ?? this.calculPartAssure((c as any).montantValide ?? c.montant, c.taux),
            
          }));
 console.log(res.content);
          // Alimenter les dropdowns une seule fois
          // Dans chargerPage(), remplacer le bloc dropdown prestataires :
          if (this.prestataires.length === 0) {
            const map = new Map<string, string>();
            res.content.forEach((c: any) => {
              if (c.prestataireId && c.prestataireNom) {
                map.set(c.prestataireId, c.prestataireNom);
              }
            });
            this.prestataires = Array.from(map.entries())
              .map(([id, nom]) => ({ id, nom }))
              .sort((a, b) => a.nom.localeCompare(b.nom));
          }
          if (this.types.length === 0) {
            this.types = [...new Set(
              res.content.map(c => c.typeConsultation).filter(Boolean)
            )].sort();
          }

          this.totalItems = res.totalElements;
          this.totalPages = res.totalPages;
          this.currentPage = res.currentPage;
          this.isLoading = false;

          // Charger les noms puis appliquer les filtres client
          this.chargerNomsAdherents();
        },
        error: () => { this.isLoading = false; }
      });
  }

  private chargerNomsAdherents(): void {
    const sans = this.toutesConsultations.filter(
      c => !c.nomAdherent && c.codeAdherent
    );

    // Aucun à charger → filtrer directement
    if (sans.length === 0) {
      this.appliquerFiltresClient();
      return;
    }

    let restants = sans.length;

    sans.forEach(c => {
      this.consultationService.getAdherent(c.codeAdherent)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (adherent: AdherentInfo) => {
            const idx = this.toutesConsultations.findIndex(x => x.id === c.id);
            if (idx >= 0) {
              this.toutesConsultations[idx].nomAdherent = adherent.assurePrincipal;
              this.toutesConsultations[idx].groupe = adherent.groupe;
              this.toutesConsultations[idx].souscripteur = adherent.souscripteur;
            }
            restants--;
            if (restants === 0) this.appliquerFiltresClient();
          },
          error: () => {
            restants--;
            if (restants === 0) this.appliquerFiltresClient();
          }
        });
    });
  }

  // ── Calculs ───────────────────────────────────────────────────

  calculPartZenithe(montant: number, taux: number): number {
    if (!taux) return 0;
    return Math.round((montant * taux) / 100);
  }

  calculPartAssure(montant: number, taux: number): number {
    return montant - this.calculPartZenithe(montant, taux);
  }

  // ── Filtres ───────────────────────────────────────────────────

  // Filtres SERVEUR : prestataire, etat, dates → recharge l'API
  appliquerFiltresServeur(): void {
    this.chargerPage(0);
  }





// Pour les champs texte nomAdherent et souscripteur
// → appeler appliquerFiltresServeur après un délai (debounce)
private searchTimeout: any = null;

onFiltreTexteChange(): void {
  if (this.searchTimeout) clearTimeout(this.searchTimeout);
  this.searchTimeout = setTimeout(() => {
    this.chargerPage(0);
  }, 500); // attendre 500ms après la dernière frappe
}

// Filtres CLIENT : uniquement type et ayantDroit
appliquerFiltresClient(): void {
  let result = [...this.toutesConsultations];


  if (this.filtreAyantDroit.trim()) {
    const ad = this.filtreAyantDroit.trim().toLowerCase();
    result = result.filter(c =>
      c.nomAyantDroit?.toLowerCase().includes(ad)
    );
  }

  this.pagedConsultations = result;
}

  // ── Pagination ────────────────────────────────────────────────

  get pageInfo(): string {
    const start = this.currentPage * this.pageSize + 1;
    const end = Math.min((this.currentPage + 1) * this.pageSize, this.totalItems);
    return `De ${start} à ${end} sur ${this.totalItems}`;
  }

  changerPage(page: number): void {
    if (page < 0 || page >= this.totalPages) return;
    this.chargerPage(page);
  }

  onPageSizeChange(): void {
    this.chargerPage(0);
  }

  getPagesArray(): number[] {
    const pages: number[] = [];
    const maxVisible = 5;
    const current = this.currentPage + 1;
    let start = Math.max(1, current - Math.floor(maxVisible / 2));
    let end = Math.min(this.totalPages, start + maxVisible - 1);
    if (end - start + 1 < maxVisible) start = Math.max(1, end - maxVisible + 1);
    for (let i = start; i <= end; i++) pages.push(i);
    return pages;
  }

  // ── Dropdown ──────────────────────────────────────────────────

  toggleDropdown(id: number, event: Event): void {
    event.stopPropagation();
    this.dropdownOuvertId = this.dropdownOuvertId === id ? null : id;
  }

  fermerDropdowns(): void {
    this.dropdownOuvertId = null;
  }

  survolItem(event: MouseEvent, survol: boolean): void {
    const el = event.currentTarget as HTMLElement;
    el.style.backgroundColor = survol ? '#f5f5f5' : 'transparent';
    el.style.color = survol ? '#262626' : '#333';
  }

  // ── Logique d'état ────────────────────────────────────────────

  isGrise(etat: string): boolean {
    return etat === 'rejete' ;
  }

  isEnAttente(etat: string): boolean {
    return etat === 'attente_validation';
  }

  // ── Modals ────────────────────────────────────────────────────

  ouvrirValider(consultation: ConsultationRow, event: Event): void {
    event.stopPropagation();
    event.preventDefault();
    this.dropdownOuvertId = null;
    this.consultationSelectionnee = { ...consultation };
    this.montantValideInput = consultation.montant;
    this.tauxInput = consultation.taux || 80;
    this.observationInput = '';
    this.showConfirmationValider = false;
    this.tauxErreur = false;
    this.showModalValider = true;
    this.chargerInfosAdherentModal(consultation);
    this.chargerConsommation(consultation);

  }

  ouvrirRejeter(consultation: ConsultationRow, event: Event): void {
    event.stopPropagation();
    event.preventDefault();
    this.dropdownOuvertId = null;
    this.consultationSelectionnee = { ...consultation };
    this.chargerInfosAdherentModal(consultation);
    this.showModalRejeter = true;
  }

  ouvrirEncaisser(consultation: ConsultationRow, event: Event): void {
    event.stopPropagation();
    event.preventDefault();
    this.dropdownOuvertId = null;
    this.consultationSelectionnee = { ...consultation };
    this.chargerInfosAdherentModal(consultation);
    this.showModalEncaisser = true;
  }

  private chargerInfosAdherentModal(consultation: ConsultationRow): void {
    if (consultation.nomAdherent && this.consultationSelectionnee) {
      this.consultationSelectionnee.nomAdherent = consultation.nomAdherent;
      this.consultationSelectionnee.groupe = consultation.groupe;
      this.consultationSelectionnee.souscripteur = consultation.souscripteur;
      if (consultation.codeAyantDroit && !consultation.nomAyantDroit) {
        this.chargerAyantDroit(consultation.codeAyantDroit);
      }
      return;
    }
    this.consultationService.getAdherent(consultation.codeAdherent)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (adherent: AdherentInfo) => {
          if (this.consultationSelectionnee?.id === consultation.id) {
            this.consultationSelectionnee!.nomAdherent = adherent.assurePrincipal;
            this.consultationSelectionnee!.groupe = adherent.groupe;
            this.consultationSelectionnee!.souscripteur = adherent.souscripteur;
          }
          if (consultation.codeAyantDroit) {
            this.chargerAyantDroit(consultation.codeAyantDroit);
          }
        },
        error: () => { }
      });
  }

  private chargerAyantDroit(codeAyantDroit: string): void {
    this.consultationService.getAyantDroit(codeAyantDroit)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (ad) => {
          if (this.consultationSelectionnee) {
            this.consultationSelectionnee.nomAyantDroit = ad.nom;
          }
        },
        error: () => { }
      });
  }

  fermerModals(): void {
    this.showModalValider = false;
    this.showModalRejeter = false;
    this.showModalEncaisser = false;
    this.showConfirmationValider = false;
    this.tauxErreur = false;
    this.consultationSelectionnee = null;
    this.consommation = null;
    this.isLoadingConso = false;
  }

  // ── Validation du taux ────────────────────────────────────────

  onTauxChange(): void {
    if (this.tauxInput > 100) {
      this.tauxInput = 100;
      this.tauxErreur = true;
    } else if (this.tauxInput < 0) {
      this.tauxInput = 0;
      this.tauxErreur = true;
    } else {
      this.tauxErreur = false;
    }
  }

  // ── Confirmations ─────────────────────────────────────────────

  confirmerValidation(): void {
    if (this.tauxInput < 0 || this.tauxInput > 100) {
      this.tauxErreur = true;
      return;
    }

    if (!this.showConfirmationValider) {
      this.showConfirmationValider = true;
      return;
    }

    if (!this.consultationSelectionnee) return;
    const user = this.authService.getStoredUser();
    const request: ValidationConsultationRequest = {
      decision: 'valide',
      employeId: user?.utilisateurId ?? 0,
      observations: this.observationInput || null,
     // montantModif: this.montantValideInput !== this.consultationSelectionnee.montant
       // ? this.montantValideInput : null,
       montantModif: this.montantValideInput,
      taux: this.tauxInput
    };
    this.consultationService.validerConsultation(
      this.consultationSelectionnee.id, request
    ).pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.showConfirmationValider = false;
          this.fermerModals();
          this.chargerPage(this.currentPage);
        },
        error: () => { }
      });
  }

  annulerConfirmationValider(): void {
    this.showConfirmationValider = false;
  }

  confirmerRejet(): void {
    if (!this.consultationSelectionnee) return;
    const user = this.authService.getStoredUser();
    const request: ValidationConsultationRequest = {
      decision: 'rejete',
      employeId: user?.utilisateurId ?? 0,
      observations: null,
      montantModif: null,
      taux: null
    };
    this.consultationService.validerConsultation(
      this.consultationSelectionnee.id, request
    ).pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => { this.fermerModals(); this.chargerPage(this.currentPage); },
        error: () => { }
      });
  }

  confirmerEncaissement(): void {
    if (!this.consultationSelectionnee) return;
    this.consultationService.encaisserConsultation(
      this.consultationSelectionnee.id
    ).pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => { this.fermerModals(); this.chargerPage(this.currentPage); },
        error: () => { }
      });
  }

  // ── Helpers ───────────────────────────────────────────────────

  getEtatClass(etat: string): string {
    switch (etat) {
      case 'attente_validation': return 'text-warning';
      case 'valide': return 'text-success';
      case 'rejete': return 'text-danger';
      case 'encaisse': return 'text-primary';
      default: return 'text-muted';
    }
  }

  getEtatIcon(etat: string): string {
    switch (etat) {
      case 'attente_validation': return 'fa fa-clock-o';
      case 'valide': return 'fa fa-check';
      case 'rejete': return 'fa fa-times';
      case 'encaisse': return 'fa fa-money';
      default: return 'fa fa-question';
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

  formatDate(date: string): string {
    if (!date) return '';
    return new Date(date).toLocaleDateString('fr-FR', {
      day: '2-digit', month: '2-digit', year: 'numeric',
      hour: '2-digit', minute: '2-digit'
    });
  }

  formatMontant(montant: number): string {
    if (!montant && montant !== 0) return '0';
    return montant.toLocaleString('fr-FR');
  }

  trackById(index: number, item: ConsultationRow): number {
    return item.id;
  }

  // ── Consommation ──────────────────────────────────────────────────────────

  chargerConsommation(consultation: ConsultationRow): void {
    if (!consultation.visiteId) return;
    this.consommation = null;
    this.isLoadingConso = true;
    this.consultationService.getConsommation(consultation.visiteId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (d) => {
          this.consommation = d;
          this.isLoadingConso = false;
        },
        error: () => {
          this.consommation = null;
          this.isLoadingConso = false;
        }
      });
  }

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

  min100(val: number): number {
    return Math.min(val || 0, 100);
  }

  get anneeEncours(): number {
    return new Date().getFullYear();
  }

  getSoldeClass(solde: number): string {
    if (solde < 0) return 'text-danger';
    if (solde < 50000) return 'text-warning';
    return 'text-success';
  }

  // ── Consommation temps réel ───────────────────────────────────────

// Calcul de la part Zenithe de la consultation EN COURS de validation
get partZenitheEnCours(): number {
  if (!this.montantValideInput || !this.tauxInput) return 0;
  return Math.round(this.montantValideInput * this.tauxInput / 100);
}

// Totaux projetés avec la consultation en cours incluse
get totalEnCoursAvecActuelle(): number {
  if (!this.consommation) return 0;
  return this.consommation.totalEnCours + this.partZenitheEnCours;
}

get totalProjecteAvecActuelle(): number {
  if (!this.consommation) return 0;
  return this.consommation.totalEncaisse
       + this.consommation.totalEnCours
       + this.partZenitheEnCours;
}

get soldeApresEncaisseActuel(): number {
  if (!this.consommation) return 0;
  return this.consommation.plafondGlobal - this.consommation.totalEncaisse;
}

get soldeProjecteAvecActuelle(): number {
  if (!this.consommation) return 0;
  return this.consommation.plafondGlobal - this.totalProjecteAvecActuelle;
}

get pourcentageEnCoursActuel(): number {
  if (!this.consommation || !this.consommation.plafondGlobal) return 0;
  return (this.totalProjecteAvecActuelle / this.consommation.plafondGlobal) * 100;
}

get niveauAlerteActuel(): 'NORMAL' | 'ATTENTION' | 'CRITIQUE' {
  const p = this.pourcentageEnCoursActuel;
  if (p >= 100) return 'CRITIQUE';
  if (p >= 80)  return 'ATTENTION';
  return 'NORMAL';
}

get messageAlerteActuel(): string {
  const p    = this.pourcentageEnCoursActuel;
  const solde = this.soldeProjecteAvecActuelle;
  if (p >= 100) {
    return `Plafond dépassé ! Projection ${this.formatMontant(this.totalProjecteAvecActuelle)} FCFA / Plafond ${this.formatMontant(this.consommation!.plafondGlobal)} FCFA`;
  }
  if (p >= 80) {
    return `Plafond bientôt atteint (${p.toFixed(0)}%). Solde projeté : ${this.formatMontant(solde)} FCFA`;
  }
  return `Solde disponible : ${this.formatMontant(solde)} FCFA (${p.toFixed(0)}% consommé)`;
}

getBarreProjectionActuelleClass(): string {
  const p = this.pourcentageEnCoursActuel;
  if (p >= 100) return 'progress-bar-danger';
  if (p >= 80)  return 'progress-bar-warning';
  return 'progress-bar-info';
}

imprimerBon(
    consultation: ConsultationRow,
    event: Event
): void {
    event.stopPropagation();
    event.preventDefault();
    this.dropdownOuvertId = null;

    // Ouvrir la page d'impression dans un nouvel onglet
    const url = `/public/admin/consultation/bon/${consultation.id}`;
    window.open(url, '_blank');
}
}