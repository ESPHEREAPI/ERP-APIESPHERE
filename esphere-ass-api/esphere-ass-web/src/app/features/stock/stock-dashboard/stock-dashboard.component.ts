import { Component, OnInit }  from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { CommonModule }       from '@angular/common';
import { RouterModule }       from '@angular/router';
import { StockService }       from '../../../core/services/stock.service';
import { AuthService }        from '../../../core/auth/auth.service';
import { StockAttestation, MouvementStock } from '../../../core/model/StockAttestation';

@Component({
  selector: 'app-stock-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, TranslatePipe],
  templateUrl: './stock-dashboard.component.html',
  styleUrl: './stock-dashboard.component.css'
})
export class StockDashboardComponent implements OnInit {

  stocks:     StockAttestation[] = [];
  mouvements: MouvementStock[]   = [];
  isLoading  = true;
  officeCode = '';

  stats = { total: 0, normal: 0, alerte: 0, critique: 0, rupture: 0,
            totalAppro: 0, totalConso: 0 };

  constructor(
    private stockService: StockService,
    private authService:  AuthService
  ) {}

  ngOnInit(): void {
    this.officeCode = this.authService.currentUserValue?.agencyCode ?? '';
    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;
    this.stockService.getStocksParBureau(this.officeCode).subscribe({
      next: r => {
        this.stocks = r.data ?? [];
        this.calcStats();
        this.loadHistorique();
      },
      error: () => { this.isLoading = false; }
    });
  }

  private loadHistorique(): void {
    this.stockService.getHistorique(this.officeCode).subscribe({
      next: r => {
        this.mouvements = (r.data ?? []).slice(0, 10);
        this.isLoading  = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  private calcStats(): void {
    this.stats.total    = this.stocks.length;
    this.stats.normal   = this.stocks.filter(s => s.statut === 'NORMAL').length;
    this.stats.alerte   = this.stocks.filter(s => s.statut === 'ALERTE').length;
    this.stats.critique = this.stocks.filter(s => s.statut === 'CRITIQUE').length;
    this.stats.rupture  = this.stocks.filter(s => s.statut === 'RUPTURE').length;
    this.stats.totalAppro = this.stocks.reduce((a, s) => a + (s.quantiteTotaleApprovisionnee ?? 0), 0);
    this.stats.totalConso = this.stocks.reduce((a, s) => a + (s.quantiteTotalConsommee ?? 0), 0);
  }

  pct(s: StockAttestation): number  { return this.stockService.pourcentageStock(s); }
  pctColor(s: StockAttestation): string { return this.stockService.progressColor(this.pct(s)); }
  statutColor(st: string): string   { return this.stockService.statutColor(st); }
  statutIcon(st: string): string    { return this.stockService.statutIcon(st); }
  typeLabel(t: string): string      { return this.stockService.typeMouvementLabel(t); }
  typeColor(t: string): string      { return this.stockService.typeMouvementColor(t); }

  get stocksEnAlerte(): StockAttestation[] {
    return this.stocks.filter(s => s.statut !== 'NORMAL');
  }

  deltaSign(m: MouvementStock): string {
    return ['APPROVISIONNEMENT','AJUSTEMENT_PLUS','ANNULATION'].includes(m.typeMouvement) ? '+' : '−';
  }

  deltaClass(m: MouvementStock): string {
    return ['APPROVISIONNEMENT','AJUSTEMENT_PLUS','ANNULATION'].includes(m.typeMouvement)
      ? 'text-success' : 'text-danger';
  }

  getDay(m: MouvementStock): string {
    if (!m.createdAt) return '';
    return new Date(m.createdAt).toLocaleDateString('fr-FR', { day: '2-digit', month: 'short', year: 'numeric' });
  }

  getTime(d: string): string {
    if (!d) return '';
    return new Date(d).toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' });
  }
}
