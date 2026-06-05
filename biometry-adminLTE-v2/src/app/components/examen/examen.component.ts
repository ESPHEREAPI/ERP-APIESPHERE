import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ExamenService, PrestationExamenResponse } from '../../services/examen.services';



@Component({
  selector: 'app-examen',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, TranslateModule],
  templateUrl: './examen.component.html',
  styleUrls: ['./examen.component.css']
})
export class ExamenComponent implements OnInit, OnDestroy {

  prestations:  PrestationExamenResponse[] = [];
  isLoading     = true;

  // Filtres
  filtrePrestataire  = '';
  filtreDateMin      = '';
  filtreDateMax      = '';
  filtreDateEncMin   = '';
  filtreDateEncMax   = '';
  filtreSouscripteur = '';
  filtreAdherent     = '';
  filtreAyantDroit   = '';
  filtreEtat         = '';



  // Pagination
  pageSize    = 10;
  currentPage = 0;
  totalItems  = 0;
  totalPages  = 0;

  // Dropdown
  dropdownOuvertId: number | null = null;
  private searchTimeout: any      = null;
  private boundFermerDropdowns    = () => this.fermerDropdowns();
  private destroy$                = new Subject<void>();

  constructor(
    private examenService: ExamenService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.chargerPage(0);
    document.addEventListener('click', this.boundFermerDropdowns);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    document.removeEventListener('click', this.boundFermerDropdowns);
    if (this.searchTimeout) clearTimeout(this.searchTimeout);
  }

  chargerPage(page: number): void {
    this.isLoading = true;
    this.examenService.getPrestationsPaginees(page, this.pageSize, {
      prestataireId: this.filtrePrestataire  || undefined,
      dateMin:       this.filtreDateMin      || undefined,
      dateMax:       this.filtreDateMax      || undefined,
      souscripteur:  this.filtreSouscripteur || undefined,
      adherent:      this.filtreAdherent     || undefined,
      ayantDroit:    this.filtreAyantDroit   || undefined,
      etat:          this.filtreEtat         || undefined
    }).pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (res) => {
          this.prestations  = res.content;
          this.totalItems   = res.totalElements;
          this.totalPages   = res.totalPages;
          this.currentPage  = res.currentPage;
          this.isLoading    = false;
        },
        error: () => { this.isLoading = false; }
      });
  }

  appliquerFiltres(): void { this.chargerPage(0); }

  onFiltreTexteChange(): void {
    if (this.searchTimeout) clearTimeout(this.searchTimeout);
    this.searchTimeout = setTimeout(() => this.chargerPage(0), 500);
  }

  reinitialiserFiltres(): void {
    if (this.searchTimeout) clearTimeout(this.searchTimeout);
    this.filtrePrestataire  = '';
    this.filtreDateMin      = '';
    this.filtreDateMax      = '';
    this.filtreDateEncMin   = '';
    this.filtreDateEncMax   = '';
    this.filtreSouscripteur = '';
    this.filtreAdherent     = '';
    this.filtreAyantDroit   = '';
    this.filtreEtat         = '';
    this.chargerPage(0);
  }

  ouvrirDetail(prestation: PrestationExamenResponse, event: Event): void {
    event.stopPropagation();
    event.preventDefault();
    this.dropdownOuvertId = null;
    this.router.navigate(['/public/admin/examen', prestation.id]);
  }

  toggleDropdown(id: number, event: Event): void {
    event.stopPropagation();
    this.dropdownOuvertId = this.dropdownOuvertId === id ? null : id;
  }

  fermerDropdowns(): void { this.dropdownOuvertId = null; }

  survolItem(event: MouseEvent, survol: boolean): void {
    const el = event.currentTarget as HTMLElement;
    el.style.backgroundColor = survol ? '#f5f5f5' : 'transparent';
  }

  get pageInfo(): string {
    if (this.totalItems === 0) return 'Aucun résultat';
    const start = this.currentPage * this.pageSize + 1;
    const end   = Math.min(
      (this.currentPage + 1) * this.pageSize, this.totalItems);
    return `De ${start} à ${end} sur ${this.totalItems}`;
  }

  changerPage(page: number): void {
    if (page < 0 || page >= this.totalPages) return;
    this.chargerPage(page);
  }

  onPageSizeChange(): void { this.chargerPage(0); }

  getPagesArray(): number[] {
    const pages: number[] = [];
    const max = 5;
    const cur = this.currentPage + 1;
    let start = Math.max(1, cur - Math.floor(max / 2));
    let end   = Math.min(this.totalPages, start + max - 1);
    if (end - start + 1 < max) start = Math.max(1, end - max + 1);
    for (let i = start; i <= end; i++) pages.push(i);
    return pages;
  }

  getNomAdherent(p: PrestationExamenResponse): string {
    return p.nomAssure || p.codeAdherent || '-';
  }

  getNomAyantDroit(p: PrestationExamenResponse): string {
    if (!p.codeAyantDroit) return '-';
    return p.nomAyantDroit || p.codeAyantDroit;
  }

  getEtatClass(etat: string): string {
    switch (etat) {
      case 'attente_validation': return 'text-warning';
      case 'partiel':            return 'text-info';
      case 'valide':             return 'text-success';
      case 'encaisse':           return 'text-primary';
      default:                   return 'text-muted';
    }
  }

  getEtatIcon(etat: string): string {
    switch (etat) {
      case 'attente_validation': return 'fa fa-clock-o';
      case 'partiel':            return 'fa fa-adjust';
      case 'valide':             return 'fa fa-check';
      case 'encaisse':           return 'fa fa-dollar';
      default:                   return 'fa fa-circle';
    }
  }

  getEtatLabel(etat: string): string {
    switch (etat) {
      case 'attente_validation': return 'Attente de validation';
      case 'partiel':            return 'Partiellement validé';
      case 'valide':             return 'Validé';
      case 'encaisse':           return 'Encaissé';
      default:                   return etat;
    }
  }

  isGrise(etat: string): boolean {
   // return etat === 'encaisse';
   return false;
  }

  formatDate(date: string): string {
    if (!date) return '';
    return new Date(date).toLocaleDateString('fr-FR', {
      day: '2-digit', month: '2-digit', year: 'numeric',
      hour: '2-digit', minute: '2-digit'
    });
  }

  trackById(index: number, item: PrestationExamenResponse): number {
    return item.id;
  }
 
}