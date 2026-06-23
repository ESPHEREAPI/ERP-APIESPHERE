import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface PrestataireAdminItem {
  id: string;
  categorieId: string;
  categorieNom: string;
  villeId: number | null;
  villeNom: string | null;
  nom: string;
  adresse: string | null;
  email: string | null;
  telephone: string | null;
  registre: string | null;
  logo: string | null;
  statut: string;
}

export interface PrestatairePageResponse {
  data: PrestataireAdminItem[];
  total: number;
  totalPages: number;
  currentPage: number;
}

export interface CategorieOption {
  id: string;
  nom: string;
}

export interface VilleOption {
  id: number;
  nom: string;
}

export interface PrestataireRequest {
  id?: string;
  categorieId: string;
  villeId?: number | null;
  nom: string;
  adresse?: string;
  email?: string;
  telephone?: string;
  registre?: string;
}

@Injectable({ providedIn: 'root' })
export class AdminPrestataireService {

  private base = '/admin/prestataires';

  constructor(private http: HttpClient) {}

  lister(params: {
    page?: number; size?: number;
    statut?: string; categorieId?: string;
    villeId?: number; search?: string;
  }): Observable<PrestatairePageResponse> {
    let p = new HttpParams()
      .set('page',  params.page  ?? 0)
      .set('size',  params.size  ?? 10)
      .set('statut',      params.statut      ?? '')
      .set('categorieId', params.categorieId ?? '')
      .set('search',      params.search      ?? '');
    if (params.villeId != null) p = p.set('villeId', params.villeId);
    return this.http.get<PrestatairePageResponse>(this.base, { params: p });
  }

  categories(): Observable<CategorieOption[]> {
    return this.http.get<CategorieOption[]>(`${this.base}/categories`);
  }

  villes(): Observable<VilleOption[]> {
    return this.http.get<VilleOption[]>(`${this.base}/villes`);
  }

  creer(req: PrestataireRequest): Observable<PrestataireAdminItem> {
    return this.http.post<PrestataireAdminItem>(this.base, req);
  }

  modifier(id: string, req: PrestataireRequest): Observable<PrestataireAdminItem> {
    return this.http.put<PrestataireAdminItem>(`${this.base}/${id}`, req);
  }

  activer(id: string): Observable<PrestataireAdminItem> {
    return this.http.patch<PrestataireAdminItem>(`${this.base}/${id}/activer`, {});
  }

  desactiver(id: string): Observable<PrestataireAdminItem> {
    return this.http.patch<PrestataireAdminItem>(`${this.base}/${id}/desactiver`, {});
  }

  supprimer(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  uploadLogo(id: string, file: File): Observable<PrestataireAdminItem> {
    const form = new FormData();
    form.append('file', file);
    return this.http.post<PrestataireAdminItem>(`${this.base}/${id}/logo`, form);
  }
}
