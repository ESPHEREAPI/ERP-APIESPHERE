import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, catchError, Observable, tap } from 'rxjs';

// Chemins corrects selon la structure du projet

import { AuthService } from '../auth/auth.service';
import { DashboardStatistics } from '../core/models/DashboardStatistics';
import { DashboardFilters } from '../core/models/DashboardFilters';

@Injectable({ providedIn: 'root' })
export class DashboardService {

  private readonly apiUrl = '/dashboard';

  private dashboardDataSubject = new BehaviorSubject<DashboardStatistics | null>(null);
  readonly dashboardData$ = this.dashboardDataSubject.asObservable();

  private loadingSubject = new BehaviorSubject<boolean>(false);
  readonly loading$ = this.loadingSubject.asObservable();

  constructor(
    private http: HttpClient,
    private authService: AuthService   // AuthService existant — pas besoin de SessionContext
  ) {}

  // ── Résolution du code souscripteur ───────────────────────────────────────

  /**
   * Retourne le code souscripteur à envoyer au backend.
   *
   * Règle stricte :
   *   - Si un code explicite est passé (sélection dropdown DII ou code souscripteur propre)
   *     → on l'utilise directement
   *   - Sinon → chaîne vide (le backend retournera une erreur si requis)
   *
   * On n'utilise JAMAIS la session pour deviner le souscripteur :
   * un DII connecté ne doit voir que le souscripteur qu'il a sélectionné.
   */
  private resolveCode(codeExplicite: string = ''): string {
    return codeExplicite.trim();
  }

  private setLoading(v: boolean): void { this.loadingSubject.next(v); }

  private handleData(data: DashboardStatistics): void {
    this.dashboardDataSubject.next(data);
    this.setLoading(false);
  }

  private handleError(error: any): never {
    this.setLoading(false);
    throw error;
  }

  // ── Méthodes publiques ────────────────────────────────────────────────────

  getStatistics(filters: DashboardFilters): Observable<DashboardStatistics> {
    this.setLoading(true);
    let params = new HttpParams();
    const code = this.resolveCode(filters.codeSouscripteur);
    if (code) params = params.set('codeSouscripteur', code);
    if (filters.dateDebut) params = params.set('dateDebut', filters.dateDebut);
    if (filters.dateFin)   params = params.set('dateFin',   filters.dateFin);
    return this.http.get<DashboardStatistics>(`${this.apiUrl}/statistics`, { params })
      .pipe(tap(d => this.handleData(d)), catchError(e => this.handleError(e)));
  }

  getCurrentMonthStatistics(codeExplicite: string = ''): Observable<DashboardStatistics> {
    this.setLoading(true);
    return this.http.get<DashboardStatistics>(
      `${this.apiUrl}/statistics/current-month`, { params: this.buildParams(codeExplicite) })
      .pipe(tap(d => this.handleData(d)), catchError(e => this.handleError(e)));
  }

  getCurrentYearStatistics(codeExplicite: string = ''): Observable<DashboardStatistics> {
    this.setLoading(true);
    return this.http.get<DashboardStatistics>(
      `${this.apiUrl}/statistics/current-year`, { params: this.buildParams(codeExplicite) })
      .pipe(tap(d => this.handleData(d)), catchError(e => this.handleError(e)));
  }

  getLastWeekStatistics(codeExplicite: string = ''): Observable<DashboardStatistics> {
    this.setLoading(true);
    return this.http.get<DashboardStatistics>(
      `${this.apiUrl}/statistics/last-week`, { params: this.buildParams(codeExplicite) })
      .pipe(tap(d => this.handleData(d)), catchError(e => this.handleError(e)));
  }

  /** periodeType en 1er, codeExplicite en 2e */
  loadStatisticsByPeriod(periodeType: string, codeExplicite: string = ''): Observable<DashboardStatistics> {
    switch (periodeType) {
      case 'last-week':    return this.getLastWeekStatistics(codeExplicite);
      case 'current-year': return this.getCurrentYearStatistics(codeExplicite);
      case 'current-month':
      default:             return this.getCurrentMonthStatistics(codeExplicite);
    }
  }

  // ── Helpers ───────────────────────────────────────────────────────────────

  private buildParams(codeExplicite: string = ''): HttpParams {
    const code = this.resolveCode(codeExplicite);
    return code ? new HttpParams().set('codeSouscripteur', code) : new HttpParams();
  }

  // ── Formatage ─────────────────────────────────────────────────────────────

  formatNumber(value: number): string {
    return new Intl.NumberFormat('fr-FR', { minimumFractionDigits: 0, maximumFractionDigits: 0 }).format(value);
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'XAF', minimumFractionDigits: 0, maximumFractionDigits: 0 }).format(value);
  }

  formatPercentage(value: number): string { return `${value.toFixed(2)} %`; }

  getAlertClass(niveau: string): string {
    return niveau === 'CRITICAL' ? 'danger' : niveau === 'WARNING' ? 'warning' : 'info';
  }

  getAlertIcon(type: string): string {
    const icons: Record<string, string> = {
      'DEPASSEMENT_PLAFOND':   'fas fa-exclamation-triangle',
      'CONSOMMATION_ANORMALE': 'fas fa-chart-line',
      'VISITE_REPETEE':        'fas fa-redo'
    };
    return icons[type] ?? 'fas fa-info-circle';
  }
}