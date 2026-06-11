import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Adherent } from '../models/Adherent';
import { PageResponse } from '../models/PageResponse';
import { AdherentFilter } from '../models/AdherentFilter';

@Injectable({ providedIn: 'root' })
export class AdherentService {

  // /partenaire/adherents → proxy dev : pathRewrite → :8090/adherents/...
  // /partenaire/adherents → Gateway prod : StripPrefix=1 → :8090/adherents/...
  private readonly apiUrl = '/partenaire/adherents';

  constructor(private http: HttpClient) {}

  searchAdherents(filter: AdherentFilter): Observable<PageResponse<Adherent>> {
    return this.http.post<PageResponse<Adherent>>(`${this.apiUrl}/search`, filter);
  }

  getAdherentProfile(codeAdherent: string): Observable<Adherent> {
    return this.http.get<Adherent>(`${this.apiUrl}/${codeAdherent}`);
  }

  createAdherent(adherent: Adherent): Observable<Adherent> {
    return this.http.post<Adherent>(this.apiUrl, adherent);
  }

  updateAdherent(codeAdherent: string, adherent: Adherent): Observable<Adherent> {
    return this.http.put<Adherent>(`${this.apiUrl}/${codeAdherent}`, adherent);
  }

  deleteAdherent(codeAdherent: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${codeAdherent}`);
  }

  changeStatut(codeAdherent: string, statut: string): Observable<Adherent> {
    const params = new HttpParams().set('statut', statut);
    return this.http.patch<Adherent>(`${this.apiUrl}/${codeAdherent}/statut`, null, { params });
  }

  exportToExcel(filter: AdherentFilter): Observable<Blob> {
    return this.http.post(`${this.apiUrl}/export/excel`, filter, { responseType: 'blob' });
  }

  exportToPdf(filter: AdherentFilter): Observable<Blob> {
    return this.http.post(`${this.apiUrl}/export/pdf`, filter, { responseType: 'blob' });
  }
}