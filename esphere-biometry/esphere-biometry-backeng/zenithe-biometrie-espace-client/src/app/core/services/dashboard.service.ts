import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, catchError, Observable, tap } from 'rxjs';
import { DashboardStatistics } from '../models/DashboardStatistics';
import { DashboardFilters } from '../models/DashboardFilters';
import { SessionContext } from '../auth/SessionContext';


/**
 * DashboardService — appels REST vers /dashboard/**
 *
 * Le codeSouscripteur n'est PLUS passé en paramètre à chaque méthode.
 * Il est résolu automatiquement depuis SessionContext :
 *   - SOUSCRIPTEUR  → son propre userName (numéro de police)
 *   - DII / SERVICE_SANTE → peut passer un code explicite en surcharge
 *
 * Le backend fait la même résolution via le header X-User-Login,
 * donc même sans paramètre le résultat est correct pour un SOUSCRIPTEUR.
 */
@Injectable({ providedIn: 'root' })
export class DashboardService {

  private readonly apiUrl = '/dashboard';

  private dashboardDataSubject = new BehaviorSubject<DashboardStatistics | null>(null);
  readonly dashboardData$ = this.dashboardDataSubject.asObservable();

  private loadingSubject = new BehaviorSubject<boolean>(false);
  readonly loading$ = this.loadingSubject.asObservable();

  constructor(
    private http: HttpClient,
    private session: SessionContext
  ) {}

  // ── Résolution du code souscripteur ────────────────────────────────────────

  /**
   * Retourne le code à utiliser pour les appels API :
   *   1. codeExplicite passé en paramètre (supervision DII)
   *   2. Code de la session courante (SOUSCRIPTEUR connecté)
   *   3. Chaîne vide → le backend utilisera X-User-Login
   */
  private resolveCode(codeExplicite: string = ''): string {
    if (codeExplicite.trim()) return codeExplicite.trim();
    return this.session.codeSouscripteur;
  }

  private setLoading(v: boolean): void {
    this.loadingSubject.next(v);
  }

  private handleData(data: DashboardStatistics): void {
    this.dashboardDataSubject.next(data);
    this.setLoading(false);
  }

  private handleError(error: any): never {
    this.setLoading(false);
    throw error;
  }

  // ── Méthodes publiques ─────────────────────────────────────────────────────

  /** Statistiques sur une période personnalisée */
  getStatistics(filters: DashboardFilters): Observable<DashboardStatistics> {
    this.setLoading(true);

    let params = new HttpParams();
    const code = this.resolveCode(filters.codeSouscripteur);
    if (code) params = params.set('codeSouscripteur', code);
    if (filters.dateDebut) params = params.set('dateDebut', filters.dateDebut);
    if (filters.dateFin)   params = params.set('dateFin',   filters.dateFin);

    return this.http.get<DashboardStatistics>(`${this.apiUrl}/statistics`, { params })
      .pipe(
        tap(data => this.handleData(data)),
        catchError(err => this.handleError(err))
      );
  }

  /** Statistiques du mois en cours */
  getCurrentMonthStatistics(codeExplicite: string = ''): Observable<DashboardStatistics> {
    this.setLoading(true);
    const params = this.buildParams(codeExplicite);
    return this.http.get<DashboardStatistics>(`${this.apiUrl}/statistics/current-month`, { params })
      .pipe(
        tap(data => this.handleData(data)),
        catchError(err => this.handleError(err))
      );
  }

  /** Statistiques de l'année en cours */
  getCurrentYearStatistics(codeExplicite: string = ''): Observable<DashboardStatistics> {
    this.setLoading(true);
    const params = this.buildParams(codeExplicite);
    return this.http.get<DashboardStatistics>(`${this.apiUrl}/statistics/current-year`, { params })
      .pipe(
        tap(data => this.handleData(data)),
        catchError(err => this.handleError(err))
      );
  }

  /** Statistiques des 7 derniers jours */
  getLastWeekStatistics(codeExplicite: string = ''): Observable<DashboardStatistics> {
    this.setLoading(true);
    const params = this.buildParams(codeExplicite);
    return this.http.get<DashboardStatistics>(`${this.apiUrl}/statistics/last-week`, { params })
      .pipe(
        tap(data => this.handleData(data)),
        catchError(err => this.handleError(err))
      );
  }

  /** Dispatch selon type de période — utilisé par DashboardComponent */
  loadStatisticsByPeriod(periodeType: string, codeExplicite: string = ''): Observable<DashboardStatistics> {
    switch (periodeType) {
      case 'last-week':    return this.getLastWeekStatistics(codeExplicite);
      case 'current-year': return this.getCurrentYearStatistics(codeExplicite);
      case 'current-month':
      default:             return this.getCurrentMonthStatistics(codeExplicite);
    }
  }

  // ── Helpers ────────────────────────────────────────────────────────────────

  private buildParams(codeExplicite: string = ''): HttpParams {
    const code = this.resolveCode(codeExplicite);
    return code
      ? new HttpParams().set('codeSouscripteur', code)
      : new HttpParams();
  }

  // ── Formatage (inchangé) ───────────────────────────────────────────────────

  formatNumber(value: number): string {
    return new Intl.NumberFormat('fr-FR', {
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(value);
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XAF',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(value);
  }

  formatPercentage(value: number): string {
    return `${value.toFixed(2)} %`;
  }

  getAlertClass(niveau: string): string {
    switch (niveau) {
      case 'CRITICAL': return 'danger';
      case 'WARNING':  return 'warning';
      default:         return 'info';
    }
  }

  getAlertIcon(type: string): string {
    switch (type) {
      case 'DEPASSEMENT_PLAFOND':  return 'fas fa-exclamation-triangle';
      case 'CONSOMMATION_ANORMALE': return 'fas fa-chart-line';
      case 'VISITE_REPETEE':       return 'fas fa-redo';
      default:                     return 'fas fa-info-circle';
    }
  }
}