import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AuthService } from '../../../auth/auth.service';
import {
  DashboardService,
  EtatPrestationItem,
  EtatPrestationPageResponse
} from '../../../services/dashboard.service';

@Component({
  selector: 'app-etat-prestations',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule],
  templateUrl: './etat-prestations.component.html',
  styleUrls: ['./etat-prestations.component.css']
})
export class EtatPrestationsComponent implements OnInit, OnDestroy {

  filtreNature  = '';
  filtreStatut  = '';
  filtreMois    = 0;
  filtreAnnee   = new Date().getFullYear();

  anneesDisponibles: number[] = [];
  tailles = [10, 25, 50, 100];
  moisDisponibles = [
    { val: 0,  lib: 'rpt_tous_mois' },
    { val: 1,  lib: 'rpt_janv' }, { val: 2,  lib: 'rpt_fevr' },
    { val: 3,  lib: 'rpt_mars' }, { val: 4,  lib: 'rpt_avri' },
    { val: 5,  lib: 'rpt_mai'  }, { val: 6,  lib: 'rpt_juin' },
    { val: 7,  lib: 'rpt_juil' }, { val: 8,  lib: 'rpt_aout' },
    { val: 9,  lib: 'rpt_sept' }, { val: 10, lib: 'rpt_octo' },
    { val: 11, lib: 'rpt_nove' }, { val: 12, lib: 'rpt_dece' }
  ];

  page: EtatPrestationPageResponse | null = null;
  isLoading = false;
  currentPage = 0;
  pageSize    = 25;

  selectedItem: EtatPrestationItem | null = null;

  private prestataireId = '';
  private destroy$ = new Subject<void>();

  constructor(
    private authService: AuthService,
    private dashboardService: DashboardService,
    private router: Router
  ) {
    for (let y = 2020; y <= 2035; y++) this.anneesDisponibles.push(y);
  }

  ngOnInit(): void {
    const user = this.authService.getStoredUser();
    // Ne rediriger que si vraiment aucun prestataireId renseigné
    const pid = user?.prestataireId;
    if (!pid || pid.trim() === '') {
      // Pas de prestataireId → ce n'est pas un prestataire, retour au dashboard
      this.router.navigate(['/public/admin/accueil']);
      return;
    }
    this.prestataireId = pid;
    this.charger(0);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  charger(page: number): void {
    this.currentPage = page;
    this.isLoading   = true;
    this.dashboardService.getEtatPrestationsPrestataire(this.prestataireId, {
      nature: this.filtreNature || undefined,
      statut: this.filtreStatut || undefined,
      mois:   this.filtreMois   || undefined,
      annee:  this.filtreAnnee,
      page,
      size:   this.pageSize
    }).pipe(takeUntil(this.destroy$))
      .subscribe({
        next:  data => { this.page = data; this.isLoading = false; },
        error: ()   => { this.isLoading = false; }
      });
  }

  changerTaille(taille: number): void {
    this.pageSize = taille;
    this.charger(0);
  }

  appliquerFiltres(): void  { this.charger(0); }

  reinitialiserFiltres(): void {
    this.filtreNature = '';
    this.filtreStatut = '';
    this.filtreMois   = 0;
    this.filtreAnnee  = new Date().getFullYear();
    this.charger(0);
  }

  ouvrirDetail(item: EtatPrestationItem): void {
    this.selectedItem = item;
  }

  fermerDetail(): void {
    this.selectedItem = null;
  }

  imprimerDetail(): void {
    window.print();
  }

  imprimer(): void {
    window.print();
  }

  // ── Patient : ayant droit si présent, sinon assuré ──
  nomPatient(item: EtatPrestationItem): string {
    return item.codeAyantDroit
      ? (item.nomAyantDroit || item.codeAyantDroit)
      : (item.nomAssure || item.codeAdherent || '—');
  }

  codePatient(item: EtatPrestationItem): string {
    return item.codeAyantDroit || item.codeAdherent || '';
  }

  get pages(): number[] {
    const total = this.page?.totalPages ?? 0;
    return Array.from({ length: total }, (_, i) => i);
  }

  get totalMontantSoumis(): number  { return this.page?.montantSoumisTotalPage ?? 0; }
  get totalMontantValide(): number  { return this.page?.montantValideTotalPage ?? 0; }

  get totalMontantZenithe(): number {
    return this.page?.content.reduce((s, i) => s + this.montantZenithe(i), 0) ?? 0;
  }

  get totalPartAssure(): number {
    return this.page?.content.reduce((s, i) => s + this.partAssure(i), 0) ?? 0;
  }

  montantZenithe(item: EtatPrestationItem): number {
    return Math.round(item.montantValide * (item.taux !== null && item.taux !== undefined ? item.taux : 100) / 100);
  }

  partAssure(item: EtatPrestationItem): number {
    return Math.round(item.montantValide - this.montantZenithe(item));
  }

  get dateImpression(): string {
    return new Date().toLocaleDateString('fr-FR', { day: '2-digit', month: 'long', year: 'numeric' });
  }

  get libellePeriode(): string {
    const m = this.moisDisponibles.find(x => x.val === this.filtreMois);
    return this.filtreMois > 0 ? `${m?.lib ?? ''} ${this.filtreAnnee}` : `Année ${this.filtreAnnee}`;
  }

  formatMontant(v: number): string {
    if (!v) return '0';
    return new Intl.NumberFormat('fr-FR').format(Math.round(v));
  }

  formatDate(d: string): string {
    if (!d) return '';
    return new Date(d).toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit', year: 'numeric' });
  }

  formatDateLong(d: string): string {
    if (!d) return '';
    return new Date(d).toLocaleDateString('fr-FR', { day: '2-digit', month: 'long', year: 'numeric' });
  }

  badgeClass(etat: string): string {
    switch (etat) {
      case 'attente_validation': return 'label label-warning';
      case 'valide':             return 'label label-success';
      case 'rejete':             return 'label label-danger';
      case 'encaisse':           return 'label label-primary';
      default:                   return 'label label-default';
    }
  }

  badgeNature(nature: string): string {
    switch (nature?.toLowerCase()) {
      case 'ordonnance': return 'label label-success';
      case 'examen':     return 'label label-warning';
      default:           return 'label label-info';
    }
  }

  iconeNature(nature: string): string {
    switch (nature?.toLowerCase()) {
      case 'ordonnance': return 'fa fa-file-text-o';
      case 'examen':     return 'fa fa-flask';
      default:           return 'fa fa-stethoscope';
    }
  }
}
