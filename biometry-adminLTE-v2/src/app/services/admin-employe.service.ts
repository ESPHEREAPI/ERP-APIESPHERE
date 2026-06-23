import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface EmployeItem {
  id: number;
  login: string;
  nom: string;
  prenom: string;
  email: string;
  genre: string;
  telephone: string;
  statut: string;
  profilCode: string;
  profilLibelle: string;
  connexionAppli: string;
  prestataireId: string;
  profilId: number;
  langueDefaut: number;
  serialBiometrie: string | null;
}

export interface EmployePageResponse {
  data: EmployeItem[];
  total: number;
  totalPages: number;
  currentPage: number;
}

export interface CreateEmployeRequest {
  nom: string;
  prenom?: string;
  genre?: string;
  email: string;
  login: string;
  motPasse?: string;
  telephone?: string;
  profilId: number;
  prestataireId?: string;
  langueDefaut?: number;
  connexionAppli?: string;
  serialBiometrie?: string;
}

export interface ProfilOption {
  id: number;
  code: string;
  typeProfil: string;
  typeSousProfil: string | null;
}

@Injectable({ providedIn: 'root' })
export class AdminEmployeService {

  constructor(private http: HttpClient) {}

  lister(params: { page?: number; size?: number; search?: string }): Observable<EmployePageResponse> {
    let p = new HttpParams()
      .set('page', params.page ?? 0)
      .set('size', params.size ?? 10);
    if (params.search) p = p.set('search', params.search);
    return this.http.get<EmployePageResponse>('/auth/users', { params: p });
  }

  detail(id: number): Observable<EmployeItem> {
    return this.http.get<EmployeItem>(`/auth/users/${id}`);
  }

  creer(req: CreateEmployeRequest): Observable<EmployeItem> {
    return this.http.post<EmployeItem>('/auth/users', req);
  }

  modifier(id: number, req: CreateEmployeRequest): Observable<EmployeItem> {
    return this.http.put<EmployeItem>(`/auth/users/${id}`, req);
  }

  activer(id: number): Observable<EmployeItem> {
    return this.http.patch<EmployeItem>(`/auth/users/${id}/activate`, {});
  }

  desactiver(id: number): Observable<EmployeItem> {
    return this.http.patch<EmployeItem>(`/auth/users/${id}/deactivate`, {});
  }

  supprimer(id: number): Observable<void> {
    return this.http.delete<void>(`/auth/users/${id}`);
  }

  resetPassword(id: number): Observable<{ temporaryPassword: string }> {
    return this.http.post<{ temporaryPassword: string }>(`/auth/users/${id}/reset-password`, {});
  }

  profils(): Observable<ProfilOption[]> {
    return this.http.get<ProfilOption[]>('/auth/roles/all');
  }
}
