import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface ProfilItem {
  id: number;
  typeProfil: string;
  typeSousProfil: string | null;
  code: string;
  statut: string;
  supprime: string;
}

export interface ProfilPageResponse {
  data: ProfilItem[];
  total: number;
  totalPages: number;
  page: number;
}

export interface ProfilRequest {
  typeProfil: string;
  typeSousProfil?: string;
  code: string;
}

@Injectable({ providedIn: 'root' })
export class AdminProfilService {

  private base = '/auth/roles';

  constructor(private http: HttpClient) {}

  lister(params: { page?: number; size?: number; search?: string }): Observable<ProfilPageResponse> {
    let p = new HttpParams()
      .set('page', params.page ?? 0)
      .set('size', params.size ?? 10);
    if (params.search) p = p.set('search', params.search);
    return this.http.get<ProfilPageResponse>(this.base, { params: p });
  }

  detail(id: number): Observable<ProfilItem> {
    return this.http.get<ProfilItem>(`${this.base}/${id}`);
  }

  creer(req: ProfilRequest): Observable<ProfilItem> {
    return this.http.post<ProfilItem>(this.base, req);
  }

  modifier(id: number, req: ProfilRequest): Observable<ProfilItem> {
    return this.http.put<ProfilItem>(`${this.base}/${id}`, req);
  }

  supprimer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
