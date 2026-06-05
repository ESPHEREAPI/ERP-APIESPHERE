import { Injectable }           from '@angular/core';
import { ActivatedRouteSnapshot,
         ResolveFn }            from '@angular/router';
import { HttpClient }           from '@angular/common/http';
import { Observable, of }      from 'rxjs';
import { catchError }           from 'rxjs/operators';
import { inject }               from '@angular/core';

export const visiteInfoResolver: ResolveFn<any> =
  (route: ActivatedRouteSnapshot) => {
    const http       = inject(HttpClient);
    const visiteCode = route.paramMap.get('visiteCode') || '';

    const parts      = visiteCode.split('_');
    const codeVisite = parts[parts.length - 1];

    if (!codeVisite) return of(null);

    // ← GET au lieu de POST
    return http.get<any>(
      `/validations/visite/${codeVisite}`
    ).pipe(
      catchError(() => of(null))
    );
  };