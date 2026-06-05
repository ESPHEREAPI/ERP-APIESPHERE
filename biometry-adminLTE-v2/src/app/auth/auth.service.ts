import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, tap, finalize } from 'rxjs/operators';
import { Router } from '@angular/router';
import { User } from '../models/user';
import { UserSession } from '../models/user-session';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly LOGIN_ENDPOINT         = '/auth/login';
  private readonly REFRESH_TOKEN_ENDPOINT = '/auth/refresh-token';  // ← ajout

  private readonly TOKEN_KEY = 'esphere_token';
  private readonly USER_KEY  = 'esphere_user';

  private currentUserSubject: BehaviorSubject<User | null>;
  public  currentUser$: Observable<User | null>;

  private loadingSubject = new BehaviorSubject<boolean>(false);
  public  loading$ = this.loadingSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    const stored = this.getStoredUser();
    this.currentUserSubject = new BehaviorSubject<User | null>(stored);
    this.currentUser$ = this.currentUserSubject.asObservable();
  }

  get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  isLoggedIn(): boolean {
    return !!this.getToken() && !!this.currentUserValue;
  }

  // ── Login ──────────────────────────────────────────────────────────────────

  login(credentials: {
    username: string;
    password: string;
    remember?: boolean;
  }): Observable<UserSession> {
    this.loadingSubject.next(true);

    return this.http.post<UserSession>(this.LOGIN_ENDPOINT, {
      login:    credentials.username,
      password: credentials.password
    }).pipe(
      tap(session => this.storeSession(session)),
      catchError(error => this.handleLoginError(error)),
      finalize(() => this.loadingSubject.next(false))
    );
  }

  // ── Refresh Token ──────────────────────────────────────────────────────────

  /**
   * POST /auth/refresh-token
   * Envoie l'ancien token (expiré ou non), reçoit un nouveau.
   * Appelé automatiquement par l'intercepteur sur erreur 401.
   */
  refreshToken(): Observable<UserSession> {
    const token = this.getToken();

    if (!token) {
      return throwError(() => ({ message: 'Aucun token disponible', status: 401 }));
    }

    return this.http.post<UserSession>(this.REFRESH_TOKEN_ENDPOINT, { token }).pipe(
      tap(session => {
        // Mettre à jour le token et les infos utilisateur
        this.storeSession(session);
        console.log('✅ Token rafraîchi pour :', session.login);
      }),
      catchError(error => {
        console.error('❌ Refresh token échoué :', error);
        // Ne pas appeler logout() ici — l'intercepteur s'en charge
        return throwError(() => error);
      })
    );
  }

  // ── Logout ─────────────────────────────────────────────────────────────────

  logout(): Observable<any> {
    return new Observable(observer => {
      this.clearSession();
      this.router.navigate(['/login']);
      observer.next(true);
      observer.complete();
    });
  }

  // ── Storage ────────────────────────────────────────────────────────────────

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getStoredUser(): User | null {
    try {
      const json = localStorage.getItem(this.USER_KEY);
      return json ? JSON.parse(json) : null;
    } catch {
      this.clearSession();
      return null;
    }
  }

  /**
   * Expose publiquement pour l'intercepteur :
   * met à jour le token + user en mémoire et localStorage.
   */
  storeUser(session: UserSession): void {
    this.storeSession(session);
  }

  // ── Helpers privés ─────────────────────────────────────────────────────────

  /**
   * Construit le User depuis la session et persiste dans localStorage.
   * Utilisé par login() et refreshToken().
   */
  private storeSession(session: UserSession): void {
    localStorage.setItem(this.TOKEN_KEY, session.token);

    const user: User = {
      token:         session.token,
      utilisateurId: session.userId,
      login:         session.login,
      nom:           session.nom,
      prenom:        session.prenom,
      profilCode:    session.profilCode,
      profilLibelle: session.profilLibelle,
      prestataireId: session.prestataireId,
      menus:         session.menus,
      // Champs legacy compatibilité header/sidebar
      nomcomplet:    `${session.prenom} ${session.nom}`,
      profil_name:   session.profilLibelle,
      prestataire:   session.prestataireId || 'ZENITHE Insurance'
    };

    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    this.currentUserSubject.next(user);
    console.log('✅ Session stockée :', user.login, '|', user.profilCode);
  }

  private clearSession(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.currentUserSubject.next(null);
  }

  private handleLoginError(error: any): Observable<never> {
    let message = 'Erreur de connexion';

    if (error instanceof HttpErrorResponse) {
      if (error.status === 0) {
        message = 'Impossible de joindre le serveur. Vérifiez que le Gateway est démarré (port 8080).';
      } else if (error.status === 401) {
        message = error.error?.erreur || error.error?.message || 'Identifiants incorrects.';
      } else if (error.status === 403) {
        message = 'Accès refusé.';
      } else if (error.status === 500) {
        message = error.error?.message || 'Erreur interne du serveur.';
      } else {
        message = error.error?.message || `Erreur ${error.status}`;
      }
    }

    console.error('❌ Login error:', message);
    return throwError(() => ({ message, status: error?.status || 0 }));
  }
}