// src/app/interceptors/auth.interceptor.ts

import { HttpInterceptorFn, HttpErrorResponse, HttpRequest, HttpHandlerFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { BehaviorSubject, catchError, filter, Observable, switchMap, take, throwError } from 'rxjs';
import { AuthService } from '../auth/auth.service';

// ── État partagé du refresh (hors de la fonction pour persister entre appels) ──
let isRefreshing  = false;
const refreshDone$ = new BehaviorSubject<string | null>(null);

/**
 * Intercepteur d'authentification
 *
 * Rôles :
 * 1. Ajouter automatiquement le token Bearer à toutes les requêtes
 * 2. Gérer le refresh automatique du token si 401
 * 3. File d'attente des requêtes simultanées pendant le refresh
 * 4. Logout si le refresh échoue
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  // Routes qui n'ont pas besoin de token
  const publicRoutes = [
    '/auth/login',
    '/auth/refresh-token',   // ← important : évite la boucle infinie
    '/auth/register',
    '/auth/forgot-password',
    '/auth/reset-password',
    '/auth/verify-email'
  ];

  // ✅ Requêtes silencieuses : on ajoute le token mais on ne tente PAS de refresh
  // Les endpoints reporting gèrent leurs erreurs via catchError dans le service
  const silentRoutes = [
    '/reporting/dashboard/ss/',
    '/reporting/dashboard/prestataire/',
    '/reporting/prestations/prestataire/',
    '/validations/dashboard/prestataire/'
  ];

  const isPublicRoute = publicRoutes.some(route => req.url.includes(route));
  if (isPublicRoute) {
    return next(req);
  }

  // Ajouter le token si disponible
  const isSilentRoute = silentRoutes.some(route => req.url.includes(route));
  const isBackgroundPoll = req.headers.has('X-Background-Poll');
  const isGetRequest = req.method === 'GET';
  const token = authService.getToken();
  const reqWithToken = token ? addToken(req, token) : req;

  return next(reqWithToken).pipe(
    catchError((error: HttpErrorResponse) => {
      // Requêtes silencieuses ou background → jamais de refresh/logout
      if (isSilentRoute || isBackgroundPoll) {
        return throwError(() => error);
      }

      if (error.status === 401) {
        // Les GET automatiques (chargement données) ne doivent pas forcer logout
        // Seules les actions utilisateur (POST/PUT/DELETE) ou navigation déclenchent le refresh
        if (isGetRequest) {
          return throwError(() => error);
        }
        return handleRefresh(req, next, authService);
      }

      return throwError(() => error);
    })
  );
};

// ── Refresh avec file d'attente ───────────────────────────────────────────────

function handleRefresh(
  req: HttpRequest<unknown>,
  next: HttpHandlerFn,
  authService: AuthService
): Observable<any> {

  // Un refresh est déjà en cours → mettre en file d'attente
  if (isRefreshing) {
    return refreshDone$.pipe(
      filter(token => token !== null),   // attendre que le refresh soit fini
      take(1),
      switchMap(newToken => next(addToken(req, newToken!)))
    );
  }

  // Lancer le refresh
  isRefreshing = true;
  refreshDone$.next(null);

  return authService.refreshToken().pipe(
    switchMap(response => {
      isRefreshing = false;
      const newToken = response.token;
      refreshDone$.next(newToken);        // débloquer les requêtes en attente
      return next(addToken(req, newToken));
    }),
    catchError(refreshError => {
      isRefreshing = false;
      refreshDone$.next(null);
      console.warn('⚠️ Token refresh failed — session may have expired');
      return throwError(() => refreshError);
    })
  );
}

// ── Helper ────────────────────────────────────────────────────────────────────

function addToken(req: HttpRequest<unknown>, token: string): HttpRequest<unknown> {
  return req.clone({
    setHeaders: { Authorization: `Bearer ${token}` }
  });
}