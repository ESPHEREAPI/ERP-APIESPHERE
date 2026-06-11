import { HttpInterceptorFn, HttpRequest, HttpHandlerFn, HttpEvent } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../auth/auth.service';

const PUBLIC_PATHS = [
  '/users/login',
  '/users/compagnie/login',
  '/users/adherent/login',
  '/users/logout',
  '/auth/login',
  '/auth/refresh-token',
  '/subscriber/activate',
];

export const jwtInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {

  const authService = inject(AuthService);

  const isPublic = PUBLIC_PATHS.some(p => req.url.includes(p));
  if (isPublic) {
    console.debug('[JWT] Route publique, pas de token:', req.url);
    return next(req);
  }

  // Diagnostic complet — à retirer une fois le 401 résolu
  const session       = authService.currentUserValue;
  const tokenMemory   = session?.token;
  const tokenStorage  = localStorage.getItem('auth_token');

  console.log('[JWT] URL:', req.url);
  console.log('[JWT] session en mémoire:', !!session);
  console.log('[JWT] token mémoire:', tokenMemory ? tokenMemory.substring(0, 30) + '...' : 'ABSENT');
  console.log('[JWT] token localStorage:', tokenStorage ? tokenStorage.substring(0, 30) + '...' : 'ABSENT');

  // Prendre le token depuis la mémoire, sinon depuis localStorage
  const token = tokenMemory ?? tokenStorage;

  if (!token) {
    console.error('[JWT] ⛔ Aucun token disponible pour', req.url);
    return next(req);
  }

  console.log('[JWT] ✅ Token attaché pour', req.url);
  return next(req.clone({
    setHeaders: { Authorization: `Bearer ${token}` }
  }));
};