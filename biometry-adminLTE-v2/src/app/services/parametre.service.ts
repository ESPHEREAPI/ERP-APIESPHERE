import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

export interface Parametre {
  cle: string;
  valeur: string;
  description: string;
  dateModification: string;
}

@Injectable({ providedIn: 'root' })
export class ParametreService {

  constructor(private http: HttpClient) {}

  getAll(): Observable<Parametre[]> {
    return this.http.get<Parametre[]>('/parametres');
  }

  getBoolean(cle: string, defaut = false): Observable<boolean> {
    return this.http.get<{ cle: string; valeur: string }>(`/parametres/${cle}`).pipe(
      map(r => r.valeur.trim().toLowerCase() === 'true'),
      catchError(() => of(defaut))
    );
  }

  getString(cle: string, defaut = ''): Observable<string> {
    return this.http.get<{ cle: string; valeur: string }>(`/parametres/${cle}`).pipe(
      map(r => r.valeur),
      catchError(() => of(defaut))
    );
  }

  set(cle: string, valeur: string): Observable<any> {
    return this.http.put(`/parametres/${cle}`, { valeur });
  }
}
