import { Injectable } from '@angular/core';

import { HttpClient, HttpParams } from '@angular/common/http';

import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';
import { AyantDroitFilter } from '../models/AyantDroitFilter';
import { PageResponse } from '../models/PageResponse';
import { AyantDroit } from '../models/AyantDroit';


@Injectable({
  providedIn: 'root'
})
export class AyantDroitService {

private apiUrl = `/ayants-droit`;

  constructor(private http: HttpClient) {}

  /**
   * Recherche des ayants droit avec filtres
   */
  searchAyantsDroit(filter: AyantDroitFilter): Observable<PageResponse<AyantDroit>> {
    return this.http.post<PageResponse<AyantDroit>>(`${this.apiUrl}/search`, filter);
  }

  /**
   * Récupère la liste des ayants droit d'un adhérent
   */
  getAyantsDroitByAdherent(codeAdherent: string): Observable<AyantDroit[]> {
    return this.http.get<AyantDroit[]>(`${this.apiUrl}/adherent/${codeAdherent}`);
  }

  /**
   * Récupère le profil d'un ayant droit
   */
  getAyantDroitProfile(codeAyantDroit: string): Observable<AyantDroit> {
    return this.http.get<AyantDroit>(`${this.apiUrl}/${codeAyantDroit}`);
  }

  /**
   * Crée un nouvel ayant droit
   */
  createAyantDroit(ayantDroit: AyantDroit): Observable<AyantDroit> {
    return this.http.post<AyantDroit>(this.apiUrl, ayantDroit);
  }

  /**
   * Met à jour un ayant droit
   */
  updateAyantDroit(codeAyantDroit: string, ayantDroit: AyantDroit): Observable<AyantDroit> {
    return this.http.put<AyantDroit>(`${this.apiUrl}/${codeAyantDroit}`, ayantDroit);
  }

  /**
   * Supprime un ayant droit
   */
  deleteAyantDroit(codeAyantDroit: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${codeAyantDroit}`);
  }

  /**
   * Change le statut d'un ayant droit
   */
  changeStatut(codeAyantDroit: string, statut: string): Observable<AyantDroit> {
    const params = new HttpParams().set('statut', statut);
    return this.http.patch<AyantDroit>(
      `${this.apiUrl}/${codeAyantDroit}/statut`,
      null,
      { params }
    );
  }

  /**
   * Upload une pièce justificative
   */
  uploadDocument(codeAyantDroit: string, file: File, type: string): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('type', type);
    
    return this.http.post(`${this.apiUrl}/${codeAyantDroit}/documents`, formData);
  }
}
