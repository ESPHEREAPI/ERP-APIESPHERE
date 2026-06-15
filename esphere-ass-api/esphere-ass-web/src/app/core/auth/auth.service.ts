import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError, timer, Subject } from 'rxjs';
import { catchError, map, tap, retry, timeout, takeUntil } from 'rxjs/operators';

import { UserSession }   from '../model/user-session';
import { LoginRequest }  from '../model/login-request';
import { AuthResponse }  from '../model/auth-response';
import { ProfilType }    from '../../shared/enum/ProfilType';
import { environment }   from '../../../environments/environment';
import { InfosAdminAgence } from '../model/infos-admin-agence.model';

// ── Constantes ────────────────────────────────────────────────────────────────

const STORAGE_KEYS = {
  TOKEN : 'auth_token',
  USER  : 'current_user',
} as const;

const TIMEOUTS = {
  LOGIN   : 30_000,
  LOGOUT  : 10_000,
  REFRESH : 15_000,
} as const;

const REFRESH_THRESHOLD_MS = 5 * 60 * 1_000; // 5 min avant expiration

/** Messages d'erreur par code HTTP */
const HTTP_ERROR_MESSAGES: Record<number, string> = {
  0   : 'Impossible de contacter le serveur. Vérifiez votre connexion.',
  400 : 'Données de connexion invalides.',
  401 : 'Identifiants incorrects.',
  403 : 'Accès refusé. Contactez votre administrateur.',
  404 : "Service d'authentification non disponible.",
  408 : 'La requête a expiré. Réessayez.',
  429 : 'Trop de tentatives. Veuillez patienter.',
  500 : 'Erreur serveur. Veuillez réessayer plus tard.',
  502 : 'Erreur serveur. Veuillez réessayer plus tard.',
  503 : 'Erreur serveur. Veuillez réessayer plus tard.',
};

// ── Service ───────────────────────────────────────────────────────────────────

@Injectable({ providedIn: 'root' })
export class AuthService implements OnDestroy {

  // ── Config ─────────────────────────────────────────────────────────────────

  private readonly apiUrl = `${environment.apiUrl}/gateway-proxy/api/esphere-ass-microservice-admin/auth`;

  // ── État ───────────────────────────────────────────────────────────────────

  private readonly currentUserSubject = new BehaviorSubject<UserSession | null>(
    this.getUserFromStorage()
  );

  /** Stream public en lecture seule */
  public readonly currentUser$ = this.currentUserSubject.asObservable();

  /** Utilisé pour annuler le refresh auto à la destruction */
  private readonly destroy$ = new Subject<void>();

  private refreshTimer?: ReturnType<typeof setTimeout>;

  // ── Lifecycle ──────────────────────────────────────────────────────────────

  constructor(private http: HttpClient) {
    this.scheduleRefresh();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.cancelRefreshTimer();
  }

  // ── Getter synchrone ───────────────────────────────────────────────────────

  get currentUserValue(): UserSession | null {
    return this.currentUserSubject.value;
  }

  // ── Auth principale ────────────────────────────────────────────────────────

  login(username: string, password: string, profilType: ProfilType): Observable<AuthResponse<UserSession>> {
    if (!username?.trim() || !password?.trim()) {
      return throwError(() => new Error('Username et mot de passe requis.'));
    }

    const body: LoginRequest = {
      username  : username.trim().toUpperCase(),
      password,
      profilType,
    };

    return this.http.post<AuthResponse<UserSession>>(
      `${this.apiUrl}/login`,
      body,
      { headers: this.buildHeaders() }
    ).pipe(
      timeout(TIMEOUTS.LOGIN),
      retry({
        count : 2,
        delay : (err: HttpErrorResponse) =>
          // Retry uniquement sur erreurs serveur / réseau
          err.status >= 500 || err.status === 0
            ? timer(1_000)
            : throwError(() => err),
      }),
      tap(response => this.processLoginResponse(response)),
      catchError(err => this.handleError(err)),
    );
  }

  logout(): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/logout`,
      {},
      { headers: this.buildAuthHeaders() }
    ).pipe(
      timeout(TIMEOUTS.LOGOUT),
      tap(() => console.log('👋 Déconnexion serveur réussie')),
      catchError(err => {
        // Déconnexion locale même si le serveur échoue
        console.warn('⚠️ Erreur déconnexion serveur (nettoyage local forcé) :', err);
        return of(undefined as any);
      }),
      tap(() => this.clearAuthData()),
    );
  }

  refreshToken(): Observable<AuthResponse<UserSession>> {
    const token = this.currentUserValue?.token;

    if (!token) {
      return throwError(() => new Error('Aucun token à rafraîchir.'));
    }

    return this.http.post<AuthResponse<UserSession>>(
      `${this.apiUrl}/refresh`,
      { token },
      { headers: this.buildAuthHeaders() }
    ).pipe(
      timeout(TIMEOUTS.REFRESH),
      tap(response => {
        if (response.success && response.data && this.isValidSession(response.data)) {
          this.persistSession(response.data);
        }
      }),
      catchError(err => {
        console.error('❌ Échec refresh token :', err);
        this.clearAuthData();
        return throwError(() => err);
      }),
    );
  }

  // ── Enrichissement profil ──────────────────────────────────────────────────

  /**
   * Appeler juste après un login réussi pour récupérer profilAgent + canEdit
   * depuis /admin-agences/by-username/{username} et les stocker dans la session.
   */
  enrichProfileAfterLogin(): void {
    const session = this.currentUserValue;
    if (!session?.userapiasac) return;

    const url = `${environment.apiUrl}/gateway-proxy/api/esphere-ass-microservice-admin/admin-agences/by-username/${encodeURIComponent(session.userapiasac)}`;

    this.http.get<{ success: boolean; data: InfosAdminAgence }>(url, {
      headers: this.buildAuthHeaders()
    }).subscribe({
      next: res => {
        if (res?.success && res.data) {
          const enriched: UserSession = {
            ...session,
            profilAgent: res.data.profilAgent,
            canEdit:     res.data.canEdit,
          };
          this.persistSession(enriched);
        }
      },
      error: () => { /* profil non trouvé — session reste sans profilAgent */ }
    });
  }

  // ── Vérifications d'état ───────────────────────────────────────────────────

  isLoggedIn(): boolean {
    const user = this.currentUserValue;

    if (!user?.token || !user.expiresAt) return false;

    if (!this.isValidJwt(user.token)) {
      console.warn('⚠️ Structure JWT invalide — déconnexion forcée');
      this.clearAuthData();
      return false;
    }

    if (new Date(user.expiresAt) <= new Date()) {
      console.log('⏰ Token expiré — déconnexion forcée');
      this.clearAuthData();
      return false;
    }

    return true;
  }

  // ── Permissions ────────────────────────────────────────────────────────────

  hasPermission(code: string): boolean {
    if (!code?.trim()) return false;
    const perms = this.currentUserValue?.permissions;
    return Array.isArray(perms) && perms.includes(code.trim());
  }

  hasAnyPermission(codes: string[]): boolean {
    return codes?.length > 0 && codes.some(c => this.hasPermission(c));
  }

  hasAllPermissions(codes: string[]): boolean {
    return codes?.length > 0 && codes.every(c => this.hasPermission(c));
  }

  // ── Session ────────────────────────────────────────────────────────────────

  clearAuthData(): void {
    try {
      localStorage.removeItem(STORAGE_KEYS.USER);
      localStorage.removeItem(STORAGE_KEYS.TOKEN);
    } catch { /* localStorage indisponible (SSR, private browsing) */ }

    this.currentUserSubject.next(null);
    this.cancelRefreshTimer();
  }

  // ── Privé — traitement login ───────────────────────────────────────────────

  private processLoginResponse(response: AuthResponse<UserSession>): void {
    if (!response?.success || !response.data) {
      // Réponse 2xx mais success=false → on la force en erreur
      throw Object.assign(new Error(response?.message ?? "Échec de l'authentification"), {
        status     : 401,
        errorCode  : response?.errorCode,
      });
    }

    // Normaliser la date d'expiration
    if (response.data.expiresAt) {
      response.data.expiresAt = new Date(response.data.expiresAt);
    }

    if (!this.isValidSession(response.data)) {
      throw Object.assign(new Error('Session utilisateur invalide reçue du serveur.'), {
        status: 500,
      });
    }

    this.persistSession(response.data);
  }

  private persistSession(session: UserSession): void {
    try {
      const payload = {
        ...session,
        expiresAt: session.expiresAt instanceof Date
          ? session.expiresAt.toISOString()
          : session.expiresAt,
      };
      localStorage.setItem(STORAGE_KEYS.USER,  JSON.stringify(payload));
      localStorage.setItem(STORAGE_KEYS.TOKEN, session.token);
    } catch (err) {
      console.error('❌ Erreur écriture localStorage :', err);
    }

    this.currentUserSubject.next(session);
    this.scheduleRefresh();
  }

  // ── Privé — stockage ───────────────────────────────────────────────────────

  public getUserFromStorage(): UserSession | null {
    try {
      const raw = localStorage.getItem(STORAGE_KEYS.USER);
      if (!raw) return null;

      const user: UserSession = JSON.parse(raw);

      if (user.expiresAt) {
        user.expiresAt = new Date(user.expiresAt);
      }

      if (!this.isValidSession(user)) {
        this.clearStorageOnly();
        return null;
      }

      return user;
    } catch {
      this.clearStorageOnly();
      return null;
    }
  }

  private clearStorageOnly(): void {
    try {
      localStorage.removeItem(STORAGE_KEYS.USER);
      localStorage.removeItem(STORAGE_KEYS.TOKEN);
    } catch { /* silencieux */ }
  }

  // ── Privé — headers ────────────────────────────────────────────────────────

  private buildHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type'    : 'application/json',
      'X-Requested-With': 'XMLHttpRequest',
    });
  }

  private buildAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem(STORAGE_KEYS.TOKEN) ?? '';
    return this.buildHeaders().set('Authorization', token ? `Bearer ${token}` : '');
  }

  // ── Privé — validation ─────────────────────────────────────────────────────

  private isValidSession(s: any): s is UserSession {
    return (
      s != null &&
      typeof s === 'object' &&
      typeof s.token === 'string' && s.token.length > 0 &&
      s.userDTO != null &&
      s.expiresAt != null
    );
  }

  private isValidJwt(token: string): boolean {
    return token?.split('.').length === 3;
  }

  // ── Privé — gestion erreurs ────────────────────────────────────────────────

  private handleError(error: any): Observable<never> {
    // Message depuis le corps ApiResponse du backend
    const backendMessage: string | undefined =
      error?.error && typeof error.error === 'object'
        ? error.error.message
        : undefined;

    const message =
      backendMessage ??
      HTTP_ERROR_MESSAGES[error?.status as number] ??
      error?.message ??
      'Une erreur inattendue est survenue.';

    console.error(`❌ [${error?.status ?? 'ERR'}] ${message}`);

    return throwError(() =>
      Object.assign(new Error(message), {
        status    : error?.status,
        errorCode : error?.error?.errorCode ?? error?.errorCode,
      })
    );
  }

  // ── Privé — refresh timer ─────────────────────────────────────────────────

  private scheduleRefresh(): void {
    this.cancelRefreshTimer();

    const expiresAt = this.currentUserValue?.expiresAt;
    if (!expiresAt) return;

    const delay = new Date(expiresAt).getTime() - Date.now() - REFRESH_THRESHOLD_MS;
    if (delay <= 0) return;

    this.refreshTimer = setTimeout(() => {
      if (!this.isLoggedIn()) return;

      console.log('🔄 Rafraîchissement automatique du token…');
      this.refreshToken()
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next  : () => console.log('✅ Token rafraîchi automatiquement'),
          error : err => console.warn('⚠️ Échec refresh automatique :', err),
        });
    }, delay);
  }

  private cancelRefreshTimer(): void {
    if (this.refreshTimer) {
      clearTimeout(this.refreshTimer);
      this.refreshTimer = undefined;
    }
  }
}