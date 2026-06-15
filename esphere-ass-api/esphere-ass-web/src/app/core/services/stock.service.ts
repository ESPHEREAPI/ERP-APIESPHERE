import { Injectable }                          from '@angular/core';
import { HttpClient, HttpParams }              from '@angular/common/http';
import { Observable }                          from 'rxjs';
import { environment }                         from '../../../environments/environment';
import { AuthService }                         from '../auth/auth.service';
import {
  StockAttestation,
  MouvementStock,
  InitierStockRequest,
  ApprovisionnerRequest,
  AjustementRequest
} from '../model/StockAttestation';

interface ApiResponse<T> { success: boolean; message: string; data: T; errorCode?: string; }

@Injectable({ providedIn: 'root' })
export class StockService {

  private readonly base = `${environment.apiUrl}/gateway-proxy/api/esphere-ass-microservice-admin/stock`;

  constructor(private http: HttpClient, private auth: AuthService) {}

  // ── Lecture ──────────────────────────────────────────────────

  getStocksParBureau(officeCode: string): Observable<ApiResponse<StockAttestation[]>> {
    return this.http.get<ApiResponse<StockAttestation[]>>(`${this.base}/${officeCode}`);
  }

  getAlertes(): Observable<ApiResponse<StockAttestation[]>> {
    return this.http.get<ApiResponse<StockAttestation[]>>(`${this.base}/alertes`);
  }

  getAlertesParOrg(orgCode: string): Observable<ApiResponse<StockAttestation[]>> {
    return this.http.get<ApiResponse<StockAttestation[]>>(`${this.base}/alertes/org/${orgCode}`);
  }

  getHistorique(officeCode: string): Observable<ApiResponse<MouvementStock[]>> {
    return this.http.get<ApiResponse<MouvementStock[]>>(`${this.base}/${officeCode}/historique`);
  }

  getHistoriqueParPeriode(officeCode: string, debut: string, fin: string): Observable<ApiResponse<MouvementStock[]>> {
    const params = new HttpParams().set('debut', debut).set('fin', fin);
    return this.http.get<ApiResponse<MouvementStock[]>>(`${this.base}/${officeCode}/historique/periode`, { params });
  }

  getHistoriqueParType(officeCode: string, type: string): Observable<ApiResponse<MouvementStock[]>> {
    const params = new HttpParams().set('type', type);
    return this.http.get<ApiResponse<MouvementStock[]>>(`${this.base}/${officeCode}/historique/type`, { params });
  }

  // ── Écriture ─────────────────────────────────────────────────

  initierStock(request: InitierStockRequest): Observable<ApiResponse<StockAttestation>> {
    return this.http.post<ApiResponse<StockAttestation>>(`${this.base}/initier`, request);
  }

  approvisionner(officeCode: string, request: ApprovisionnerRequest): Observable<ApiResponse<StockAttestation>> {
    return this.http.post<ApiResponse<StockAttestation>>(`${this.base}/${officeCode}/approvisionner`, request);
  }

  ajuster(officeCode: string, request: AjustementRequest): Observable<ApiResponse<StockAttestation>> {
    return this.http.post<ApiResponse<StockAttestation>>(`${this.base}/${officeCode}/ajuster`, request);
  }

  annulerProduction(officeCode: string, refProduction: string): Observable<ApiResponse<StockAttestation>> {
    return this.http.delete<ApiResponse<StockAttestation>>(`${this.base}/${officeCode}/annuler/${refProduction}`);
  }

  // ── Helpers ──────────────────────────────────────────────────

  getOfficeCodeFromSession(): string {
    return this.auth.getUserFromStorage()?.agencyCode ?? '';
  }

  getOrgCodeFromSession(): string {
    return this.auth.getUserFromStorage()?.agencyCode ?? '';
  }

  /** Couleur Bootstrap selon statut */
  statutColor(statut: string): string {
    const map: Record<string, string> = {
      NORMAL:   'success',
      ALERTE:   'warning',
      CRITIQUE: 'danger',
      RUPTURE:  'dark',
    };
    return map[statut] ?? 'secondary';
  }

  /** Icône selon statut */
  statutIcon(statut: string): string {
    const map: Record<string, string> = {
      NORMAL:   'fas fa-check-circle',
      ALERTE:   'fas fa-exclamation-triangle',
      CRITIQUE: 'fas fa-exclamation-circle',
      RUPTURE:  'fas fa-times-circle',
    };
    return map[statut] ?? 'fas fa-circle';
  }

  /** Pourcentage stock restant vs total approvisionné */
  pourcentageStock(stock: StockAttestation): number {
    const total = stock.quantiteTotaleApprovisionnee;
    if (!total || total === 0) return 0;
    return Math.round((stock.quantiteDisponible / total) * 100);
  }

  /** Couleur barre de progression selon pourcentage */
  progressColor(pct: number): string {
    if (pct <= 10) return 'danger';
    if (pct <= 30) return 'warning';
    if (pct <= 60) return 'info';
    return 'success';
  }

  /** Label lisible pour type de mouvement */
  typeMouvementLabel(type: string): string {
    const map: Record<string, string> = {
      APPROVISIONNEMENT: 'Approvisionnement',
      DESTOCKAGE:        'Déstockage',
      AJUSTEMENT_PLUS:   'Ajustement +',
      AJUSTEMENT_MOINS:  'Ajustement −',
      ANNULATION:        'Annulation',
    };
    return map[type] ?? type;
  }

  /** Couleur badge type mouvement */
  typeMouvementColor(type: string): string {
    const map: Record<string, string> = {
      APPROVISIONNEMENT: 'success',
      DESTOCKAGE:        'primary',
      AJUSTEMENT_PLUS:   'info',
      AJUSTEMENT_MOINS:  'warning',
      ANNULATION:        'danger',
    };
    return map[type] ?? 'secondary';
  }
}
