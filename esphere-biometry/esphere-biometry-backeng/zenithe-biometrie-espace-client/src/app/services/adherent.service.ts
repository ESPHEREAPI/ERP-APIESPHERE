import { Injectable } from '@angular/core';
import { Adherent } from '../model/Adherent';
import { PageResponse } from '../model/PageResponse';
import { Observable } from 'rxjs';
import { AdherentFilter } from '../model/AdherentFilter';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AdherentService {

  private apiUrl = `${environment.apiUrl}/gateway-proxy/api/service-biometrie-partenaire/adherents`;

  constructor(private http: HttpClient) {}

  /**
   * Recherche des adhérents avec filtres et pagination
   */
  searchAdherents(filter: AdherentFilter): Observable<PageResponse<Adherent>> {
    return this.http.post<PageResponse<Adherent>>(`${this.apiUrl}/search`, filter);
  }

  /**
   * Récupère le profil détaillé d'un adhérent
   */
  getAdherentProfile(codeAdherent: string): Observable<Adherent> {
    return this.http.get<Adherent>(`${this.apiUrl}/${codeAdherent}`);
  }

  /**
   * Crée un nouvel adhérent
   */
  createAdherent(adherent: Adherent): Observable<Adherent> {
    return this.http.post<Adherent>(this.apiUrl, adherent);
  }

  /**
   * Met à jour un adhérent
   */
  updateAdherent(codeAdherent: string, adherent: Adherent): Observable<Adherent> {
    return this.http.put<Adherent>(`${this.apiUrl}/${codeAdherent}`, adherent);
  }

  /**
   * Supprime un adhérent (suppression logique)
   */
  deleteAdherent(codeAdherent: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${codeAdherent}`);
  }

  /**
   * Change le statut d'un adhérent
   */
  changeStatut(codeAdherent: string, statut: string): Observable<Adherent> {
    const params = new HttpParams().set('statut', statut);
    return this.http.patch<Adherent>(
      `${this.apiUrl}/${codeAdherent}/statut`,
      null,
      { params }
    );
  }

  /**
   * Exporte les adhérents en Excel
   */
  exportToExcel(filter: AdherentFilter): Observable<Blob> {
    return this.http.post(`${this.apiUrl}/export/excel`, filter, {
      responseType: 'blob'
    });
  }

  /**
   * Exporte les adhérents en PDF
   */
  exportToPdf(filter: AdherentFilter): Observable<Blob> {
    return this.http.post(`${this.apiUrl}/export/pdf`, filter, {
      responseType: 'blob'
    });
  }
}
