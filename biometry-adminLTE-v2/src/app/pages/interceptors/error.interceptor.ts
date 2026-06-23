import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      // Les requêtes de polling en arrière-plan (sidebar badges) ne doivent jamais
      // déclencher une navigation globale — elles échouent silencieusement.
      if (req.headers.has('X-Background-Poll')) {
        return throwError(() => error);
      }

      // Les endpoints reporting/validations gèrent leurs propres erreurs via catchError
      const silentPaths = [
        '/reporting/',
        '/validations/dashboard/prestataire/'
      ];
      if (silentPaths.some(p => req.url.includes(p))) {
        return throwError(() => error);
      }

      const errorCode = error.status;
      const errorDetails = {
        url: req.url,
        timestamp: new Date(),
        message: error.message,
        status: errorCode
      };

      console.error('🔴 ErrorInterceptor caught:', {
        status: errorCode,
        url: req.url,
        message: error.message
      });

      // ✅ NE PAS rediriger automatiquement sur erreur 0 (SSL)
      // Laisser le AuthService gérer ces erreurs
      if (error.status === 0) {
        console.warn('⚠️ Network/SSL error detected - letting AuthService handle it');
        // Ne pas rediriger, juste propager l'erreur
        return throwError(() => error);
      }

      // Gérer les erreurs HTTP spécifiques SAUF pour les routes d'authentification
      const isAuthRoute = req.url.includes('/auth/');
      
      switch (errorCode) {
        case 401:
          // ✅ Si route d'auth (login), ne pas rediriger automatiquement
          if (isAuthRoute) {
            console.log('❌ Login failed - showing error in component');
            return throwError(() => error);
          }
          
          // Sinon, rediriger vers login (session expirée)
          console.error('🔐 Unauthorized access - redirecting to login');
          router.navigate(['/login'], {
            queryParams: { returnUrl: router.url, sessionExpired: 'true' }
          });
          break;

        case 403:
          console.error('⚠️ Forbidden:', req.url);
          break;

        case 404:
          // Ne pas rediriger pour 404 sur API, juste logger
          console.warn('⚠️ Resource not found:', req.url);
          return throwError(() => error);

        case 408:
          console.error('⚠️ Timeout:', req.url);
          break;

        case 429:
          console.error('⚠️ Too many requests:', req.url);
          break;

        case 500:
        case 502:
        case 503:
        case 504:
          // Erreurs serveur — NE PAS rediriger, laisser le composant gérer
          console.error(`⚠️ Server error ${errorCode} on:`, req.url);
          break;

        default:
          // Autres erreurs 4xx ou 5xx
          if (errorCode >= 400 && !isAuthRoute) {
            console.warn(`⚠️ HTTP ${errorCode} error on:`, req.url);
            // Ne pas rediriger systématiquement, laisser le composant gérer
          }
      }

      return throwError(() => error);
    })
  );
};