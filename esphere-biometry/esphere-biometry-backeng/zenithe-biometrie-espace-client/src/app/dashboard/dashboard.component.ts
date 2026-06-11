import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { Chart, registerables } from 'chart.js';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AdminLteService } from '../core/services/admin-lte-service';
import { DashboardService } from '../core/services/dashboard.service';
import { DashboardStatistics } from '../core/models/DashboardStatistics';
import { ConsommationGlobale } from '../core/models/ConsommationGlobale';
import { Alerte } from '../core/models/Alerte';
import { AuthService } from '../core/auth/auth.service';
import { UserSession } from '../core/models/user-session';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit, OnDestroy {

  pageTitle = 'Dashboard';
  breadcrumbItems = [
    { label: 'Home', route: '/dashboard' },
    { label: 'Dashboard', active: true }
  ];

  dashboardData: DashboardStatistics | null = null;
  consommationGlobale: ConsommationGlobale | null = null;
  alertes: Alerte[] = [];

  loading = false;
  error: string | null = null;

  codeSouscripteur = '';
  selectedPeriod = 'current-year';
  customDateDebut: string | null = null;
  customDateFin: string | null = null;
  currentSession: UserSession | null = null;

  chartConsommationPeriode: Chart | null = null;
  chartTypePrestation: Chart | null = null;
  chartTopPrestations: Chart | null = null;

  periodOptions = [
    { value: 'last-week',     label: '7 derniers jours'    },
    { value: 'current-month', label: 'Mois en cours'       },
    { value: 'current-year',  label: 'Année en cours'      },
    { value: 'custom',        label: 'Période personnalisée' }
  ];

  private readonly destroy$ = new Subject<void>();
  private sessionLoaded = false;

  constructor(
    private adminLteService: AdminLteService,
    public dashboardService: DashboardService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    // Écouter les données du service
    this.dashboardService.dashboardData$
      .pipe(takeUntil(this.destroy$))
      .subscribe(data => {
        if (data) {
          this.dashboardData        = data;
          this.consommationGlobale  = data.consommationGlobale;
          this.alertes              = data.alertes || [];
          this.initializeCharts();
          this.cdr.markForCheck();
        }
      });

    this.dashboardService.loading$
      .pipe(takeUntil(this.destroy$))
      .subscribe(loading => this.loading = loading);

    // Attendre la session PUIS charger — résout la race condition :
    // codeSouscripteur est garanti renseigné avant le premier appel HTTP
    this.authService.currentUser$
      .pipe(takeUntil(this.destroy$))
      .subscribe((session: UserSession | null) => {
        this.currentSession   = session;
        this.codeSouscripteur = session?.usersDTO?.lastname ?? '';
        this.cdr.markForCheck();

        // Premier chargement uniquement — les rechargements sont pilotés
        // par onPeriodChange() / refresh() pour éviter les appels en double
        if (!this.sessionLoaded) {
          this.sessionLoaded = true;
          this.loadDashboardData();
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.destroyCharts();
  }

  // ── Chargement ─────────────────────────────────────────────────────────────

  loadDashboardData(): void {
    this.error = null;

    if (this.selectedPeriod === 'custom') {
      if (this.customDateDebut && this.customDateFin) {
        this.dashboardService.getStatistics({
          codeSouscripteur: this.codeSouscripteur,
          dateDebut:        this.customDateDebut,
          dateFin:          this.customDateFin
        }).subscribe({ error: err => this.handleError(err) });
      }
      return;
    }

    // selectedPeriod en 1er, codeSouscripteur en 2e — ordre du DashboardService
    this.dashboardService.loadStatisticsByPeriod(
      this.selectedPeriod,
      this.codeSouscripteur
    ).subscribe({ error: err => this.handleError(err) });
  }

  onPeriodChange(): void {
    if (this.selectedPeriod !== 'custom') this.loadDashboardData();
  }

  applyCustomFilters(): void {
    if (this.customDateDebut && this.customDateFin) this.loadDashboardData();
  }

  refresh(): void { this.loadDashboardData(); }

  // ── Graphiques ─────────────────────────────────────────────────────────────

  initializeCharts(): void {
    setTimeout(() => {
      this.createConsommationPeriodeChart();
      this.createTypePrestationChart();
      this.createTopPrestationsChart();
    }, 100);
  }

  createConsommationPeriodeChart(): void {
    const canvas = document.getElementById('chartConsommationPeriode') as HTMLCanvasElement;
    if (!canvas || !this.dashboardData?.statistiquesParPeriode) return;
    this.destroyChart(this.chartConsommationPeriode);
    const ctx = canvas.getContext('2d');
    if (!ctx) return;
    const data = this.dashboardData.statistiquesParPeriode;
    this.chartConsommationPeriode = new Chart(ctx, {
      type: 'line',
      data: {
        labels: data.map(s => s.libelle),
        datasets: [
          {
            label: 'Montant Total Ticket Modérateur',
            data: data.map(s => s.montantTotal),
            borderColor: '#007bff',
            backgroundColor: 'rgba(0,123,255,0.1)',
            tension: 0.4, fill: true
          },
          {
            label: 'Montant Total Prise En Charge',
            data: data.map(s => s.montantRembourse),
            borderColor: '#28a745',
            backgroundColor: 'rgba(40,167,69,0.1)',
            tension: 0.4, fill: true
          }
        ]
      },
      options: {
        responsive: true, maintainAspectRatio: false,
        plugins: {
          legend: { display: true, position: 'top' },
          tooltip: { callbacks: { label: ctx => `${ctx.dataset.label}: ${this.dashboardService.formatCurrency(ctx.parsed.y ?? 0)}` } }
        },
        scales: { y: { beginAtZero: true, ticks: { callback: v => this.dashboardService.formatCurrency(+v) } } }
      }
    });
  }

  createTypePrestationChart(): void {
    const canvas = document.getElementById('chartTypePrestation') as HTMLCanvasElement;
    if (!canvas || !this.dashboardData?.statistiquesParTypePrestation) return;
    this.destroyChart(this.chartTypePrestation);
    const ctx = canvas.getContext('2d');
    if (!ctx) return;
    const data = this.dashboardData.statistiquesParTypePrestation;
    this.chartTypePrestation = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: data.map(s => s.typeNom),
        datasets: [{ data: data.map(s => s.montantRembourse), backgroundColor: ['#007bff','#28a745','#ffc107','#dc3545','#17a2b8','#6610f2','#e83e8c'] }]
      },
      options: {
        responsive: true, maintainAspectRatio: false,
        plugins: {
          legend: { display: true, position: 'right' },
          tooltip: { callbacks: { label: ctx => `${ctx.label}: ${this.dashboardService.formatCurrency(ctx.parsed)} (${data[ctx.dataIndex].pourcentageTotal.toFixed(1)}%)` } }
        }
      }
    });
  }

  createTopPrestationsChart(): void {
    const canvas = document.getElementById('chartTopPrestations') as HTMLCanvasElement;
    if (!canvas || !this.dashboardData?.topPrestations) return;
    this.destroyChart(this.chartTopPrestations);
    const ctx = canvas.getContext('2d');
    if (!ctx) return;
    const data = this.dashboardData.topPrestations;
    this.chartTopPrestations = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: data.map(p => p.typenom),
        datasets: [{ label: "Nombre d'utilisations", data: data.map(p => p.nombreUtilisations), backgroundColor: '#17a2b8' }]
      },
      options: {
        indexAxis: 'y', responsive: true, maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          tooltip: { callbacks: { label: ctx => [`Utilisations: ${ctx.parsed.x}`, `Montant: ${this.dashboardService.formatCurrency(data[ctx.dataIndex].montantTotal)}`] } }
        },
        scales: { x: { beginAtZero: true } }
      }
    });
  }

  destroyChart(chart: Chart | null): void { chart?.destroy(); }

  destroyCharts(): void {
    this.destroyChart(this.chartConsommationPeriode);
    this.destroyChart(this.chartTypePrestation);
    this.destroyChart(this.chartTopPrestations);
  }

  // ── Helpers template ───────────────────────────────────────────────────────

  getPlafondPercentage(): number {
    return this.dashboardData?.tauxUtilisationPlafond?.pourcentageUtilisation || 0;
  }

  getProgressBarClass(): string {
    const p = this.getPlafondPercentage();
    return p >= 90 ? 'bg-danger' : p >= 70 ? 'bg-warning' : 'bg-success';
  }

  handleError(error: any): void {
    console.error('[Dashboard] Erreur chargement:', error);
    this.error = 'Une erreur est survenue lors du chargement des données.';
  }

  getAlertClass(alerte: Alerte): string { return this.dashboardService.getAlertClass(alerte.niveau); }
  getAlertIcon(alerte:  Alerte): string { return this.dashboardService.getAlertIcon(alerte.type);  }
}