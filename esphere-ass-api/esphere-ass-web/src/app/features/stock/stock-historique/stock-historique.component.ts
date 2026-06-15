import { Component, OnInit }  from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { CommonModule }       from '@angular/common';
import { RouterModule }       from '@angular/router';
import { FormsModule }        from '@angular/forms';
import { StockService }       from '../../../core/services/stock.service';
import { AuthService }        from '../../../core/auth/auth.service';
import { MouvementStock }     from '../../../core/model/StockAttestation';

@Component({
  selector: 'app-stock-historique',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, TranslatePipe],
  templateUrl: './stock-historique.component.html',
  styleUrl: './stock-historique.component.css'
})
export class StockHistoriqueComponent implements OnInit {

  mouvements:  MouvementStock[] = [];
  filtered:    MouvementStock[] = [];
  isLoading    = true;
  errorMessage = '';
  officeCode   = '';

  // Filtres
  filterType  = '';
  filterDebut = '';
  filterFin   = '';

  types = [
    { value: '',                label: 'Tous les types' },
    { value: 'APPROVISIONNEMENT', label: 'Approvisionnement' },
    { value: 'DESTOCKAGE',      label: 'Déstockage' },
    { value: 'AJUSTEMENT_PLUS', label: 'Ajustement +' },
    { value: 'AJUSTEMENT_MOINS',label: 'Ajustement −' },
    { value: 'ANNULATION',      label: 'Annulation' },
  ];

  constructor(
    private stockService: StockService,
    private authService:  AuthService
  ) {}

  ngOnInit(): void {
    this.officeCode = this.authService.currentUserValue?.agencyCode ?? '';
    this.load();
  }

  load(): void {
    this.isLoading = true;

    if (this.filterDebut && this.filterFin) {
      const debut = new Date(this.filterDebut).toISOString();
      const fin   = new Date(this.filterFin + 'T23:59:59').toISOString();
      this.stockService.getHistoriqueParPeriode(this.officeCode, debut, fin).subscribe({
        next: r => { this.mouvements = r.data ?? []; this.applyFilter(); this.isLoading = false; },
        error: () => { this.isLoading = false; }
      });
    } else if (this.filterType) {
      this.stockService.getHistoriqueParType(this.officeCode, this.filterType).subscribe({
        next: r => { this.mouvements = r.data ?? []; this.applyFilter(); this.isLoading = false; },
        error: () => { this.isLoading = false; }
      });
    } else {
      this.stockService.getHistorique(this.officeCode).subscribe({
        next: r => { this.mouvements = r.data ?? []; this.applyFilter(); this.isLoading = false; },
        error: () => { this.isLoading = false; }
      });
    }
  }

  applyFilter(): void {
    this.filtered = this.filterType
      ? this.mouvements.filter(m => m.typeMouvement === this.filterType)
      : [...this.mouvements];
  }

  resetFiltres(): void {
    this.filterType  = '';
    this.filterDebut = '';
    this.filterFin   = '';
    this.load();
  }

  typeLabel(t: string): string  { return this.stockService.typeMouvementLabel(t); }
  typeColor(t: string): string  { return this.stockService.typeMouvementColor(t); }
  statutColor(st: string): string { return this.stockService.statutColor(st); }

  deltaSign(m: MouvementStock): string {
    return ['APPROVISIONNEMENT','AJUSTEMENT_PLUS','ANNULATION'].includes(m.typeMouvement) ? '+' : '−';
  }

  deltaClass(m: MouvementStock): string {
    return ['APPROVISIONNEMENT','AJUSTEMENT_PLUS','ANNULATION'].includes(m.typeMouvement)
      ? 'text-success' : 'text-danger';
  }

  formatDate(d: string): string {
    if (!d) return '—';
    return new Date(d).toLocaleString('fr-FR');
  }

  // Résumé stats filtrées
  get totalAppro(): number {
    return this.filtered
      .filter(m => m.typeMouvement === 'APPROVISIONNEMENT')
      .reduce((a, m) => a + m.quantite, 0);
  }

  get totalConso(): number {
    return this.filtered
      .filter(m => m.typeMouvement === 'DESTOCKAGE')
      .reduce((a, m) => a + m.quantite, 0);
  }
}
