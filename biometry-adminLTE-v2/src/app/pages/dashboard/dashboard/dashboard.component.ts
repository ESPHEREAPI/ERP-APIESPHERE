import { Component, OnInit, OnDestroy, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { Subject, takeUntil } from 'rxjs';
import { CanvasJSAngularChartsModule } from '@canvasjs/angular-charts';
import { AuthService } from '../../../auth/auth.service';
import { DashboardService, DashboardSsResponse, DashboardPrestataireResponse, StatMoisResponse } from '../../../services/dashboard.service';
import { User } from '../../../models/user';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, TranslateModule, CanvasJSAngularChartsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {

  user: User | null = null;
  profilCode = '';
  isLoading = true;
  anneeSelectionnee = new Date().getFullYear();
  anneesDisponibles: number[] = [];

  // Stats SS
  statsSS: DashboardSsResponse | null = null;
  // Stats Prestataire
  statsPrestataire: DashboardPrestataireResponse | null = null;

  // Config graphique CanvasJS
  chartOptions: any = {};

  private destroy$ = new Subject<void>();

  constructor(
    private authService: AuthService,
    private dashboardService: DashboardService
  ) {
    for (let y = 2018; y <= 2034; y++) this.anneesDisponibles.push(y);
  }

  ngOnInit(): void {
    this.user = this.authService.getStoredUser();
    this.profilCode = this.user?.profilCode || '';
    this.loadDashboard();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get isServiceSante(): boolean { return this.profilCode === 'SERVICE_SANTE'; }
  get isAdmin(): boolean { return this.profilCode === 'SUP_ADMIN'; }
  get isPrestataire(): boolean { return !this.isServiceSante && !this.isAdmin; }

  loadDashboard(): void {
    this.isLoading = true;

    if (this.isServiceSante || this.isAdmin) {
      const employeId = this.user?.utilisateurId || '';
      this.dashboardService.getDashboardSS(employeId, this.anneeSelectionnee)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: data => {
            this.statsSS = data;
            this.buildChartSS(data);
            this.isLoading = false;
          },
          error: () => this.isLoading = false
        });
    } else if (this.user?.prestataireId) {
      // Récupérer la catégorie depuis le JWT si disponible, sinon générique
      const cat = (this.user as any).categorieId || 'CENTRE_HOSPITALIER';
      this.dashboardService.getDashboardPrestataire(
        this.user.prestataireId, cat, this.anneeSelectionnee
      ).pipe(takeUntil(this.destroy$))
        .subscribe({
          next: data => {
            this.statsPrestataire = data;
            this.buildChartPrestataire(data);
            this.isLoading = false;
          },
          error: () => this.isLoading = false
        });
    } else {
      this.isLoading = false;
    }
  }

  private buildChartSS(data: DashboardSsResponse): void {
    const mois = data.consultationsParMois || [];
    this.chartOptions = {
      animationEnabled: true,
      theme: 'light2',
      title: {
        text: `Consommation par type de prestations, Année: ${this.anneeSelectionnee}`,
        fontSize: 18,
        fontFamily: 'Arial',
        fontWeight: 'bold'
      },
      axisX: { title: 'Mois', labelAngle: -30, labelFontSize: 11 },
      axisY: { title: 'Nombre', includeZero: true, gridThickness: 1, gridColor: '#E8E8E8' },
      toolTip: { shared: true },
      legend: { cursor: 'pointer', fontSize: 13 },
      data: [
        {
          type: 'column',
          name: 'Consultations',
          legendText: 'Consultations',
          showInLegend: true,
          color: '#00C0EF',
          dataPoints: this.toDataPoints(data.consultationsParMois)
        },
        {
          type: 'column',
          name: 'Ordonnances',
          legendText: 'Ordonnances',
          showInLegend: true,
          color: '#00A65A',
          dataPoints: this.toDataPoints(data.ordonnancesParMois)
        },
        {
          type: 'column',
          name: 'Examens',
          legendText: 'Examens',
          showInLegend: true,
          color: '#F39C12',
          dataPoints: this.toDataPoints(data.examensParMois)
        }
      ]
    };
  }

  private buildChartPrestataire(data: DashboardPrestataireResponse): void {
    this.chartOptions = {
      animationEnabled: true,
      theme: 'light2',
      title: {
        text: `Encaissements par mois — ${this.anneeSelectionnee}`,
        fontSize: 18,
        fontFamily: 'Arial'
      },
      axisX: { labelAngle: -30, labelFontSize: 11 },
      axisY: { title: 'Montant (FCFA)', includeZero: true },
      data: [{
        type: 'column',
        name: 'Montant encaissé',
        color: '#00A65A',
        dataPoints: (data.encaissementsParMois || []).map(s => ({
          label: s.libelleMois,
          y: s.montant
        }))
      }]
    };
  }

  private toDataPoints(stats: StatMoisResponse[]): any[] {
    return (stats || []).map(s => ({ label: s.libelleMois, y: s.nombre }));
  }

  onAnneeChange(event: Event): void {
    const annee = parseInt((event.target as HTMLSelectElement).value, 10);
    if (annee && annee !== this.anneeSelectionnee) {
      this.anneeSelectionnee = annee;
      this.loadDashboard();
    }
  }

  formatMontant(montant: number): string {
    if (!montant) return '0 FCFA';
    return new Intl.NumberFormat('fr-FR').format(Math.round(montant)) + ' FCFA';
  }

  formatNumber(n: number): number { return n || 0; }
}
