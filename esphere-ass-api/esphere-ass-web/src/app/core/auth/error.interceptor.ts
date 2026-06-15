import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandlerFn,
  HttpInterceptorFn,
  HttpRequest,
} from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { GlobalErrorService } from '../services/global-error.service';
import { BackendError } from '../model/api-error.model';

/**
 * Intercepteur fonctionnel (Angular 18 standalone) — remonte toutes les
 * erreurs HTTP backend vers GlobalErrorService.
 * À enregistrer dans app.config.ts via withInterceptors([errorInterceptor]).
 */
export const errorInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {
  const errorService = inject(GlobalErrorService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((err: HttpErrorResponse) => {
      const body: BackendError | null = err.error ?? null;
      const status = err.status;

      // Laisser le TokenInterceptor gérer les 401 (refresh token)
      if (status === 401) {
        return throwError(() => err);
      }

      errorService.handleAndNotify(status, body);

      // Redirection automatique sur 403
      if (status === 403) {
        router.navigate(['/home']);
      }

      return throwError(() => err);
    })
  );
};
