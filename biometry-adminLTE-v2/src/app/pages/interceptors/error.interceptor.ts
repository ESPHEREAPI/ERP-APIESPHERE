import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
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
          router.navigate(['/error'], {
            queryParams: { code: 403 },
            state: { errorCode: 403, errorDetails }
          });
          break;

        case 404:
          // Ne pas rediriger pour 404 sur API, juste logger
          console.warn('⚠️ Resource not found:', req.url);
          return throwError(() => error);

        case 408:
          // Timeout - ne rediriger que si pas une route d'auth
          if (!isAuthRoute) {
            router.navigate(['/network-error'], {
              state: { previousUrl: router.url }
            });
          }
          break;

        case 429:
          router.navigate(['/error'], {
            queryParams: { code: 429 },
            state: { errorCode: 429, errorDetails }
          });
          break;

        case 500:
        case 502:
        case 503:
        case 504:
          // Erreurs serveur - ne rediriger que si pas une route d'auth
          if (!isAuthRoute) {
            router.navigate(['/error'], {
              queryParams: { code: errorCode },
              state: { errorCode, errorDetails }
            });
          }
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