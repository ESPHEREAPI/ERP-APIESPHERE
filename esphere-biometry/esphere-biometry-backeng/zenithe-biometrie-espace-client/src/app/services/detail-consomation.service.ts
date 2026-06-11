import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DetailConsomationAdherent } from '../model/DetailConsomationAdherent';
import { Adherent } from '../model/Adherent';

@Injectable({
  providedIn: 'root'
})
export class DetailConsomationService {

   private apiUrl = `${environment.apiUrl}/api/v1/adherents`;

  constructor(private http: HttpClient) {}

  getDetailConsomationAdherent(
    codeSouscripteur: string,
    dateDebut?: Date | null,
    dateFin?: Date | null
  ): Observable<DetailConsomationAdherent[]> {
    
    let params = new HttpParams()
      .set('codeSouscripteur', codeSouscripteur);

    if (dateDebut) {
      params = params.set('dateDebut', this.formatDate(dateDebut));
    }

    if (dateFin) {
      params = params.set('dateFin', this.formatDate(dateFin));
    }

    return this.http.get<DetailConsomationAdherent[]>(this.apiUrl, { params });
  }

  private formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

   /**
   * Récupère la liste de tous les souscripteurs
   */
  getSouscripteurs(souscripteur:string): Observable<Adherent[]> {
    return this.http.get<Adherent[]>(`${this.apiUrl}/all/${souscripteur}`);
  }

  /**
   * Récupère un souscripteur par son code
   */
  getSouscripteurByCode(code: string): Observable<Adherent> {
    return this.http.get<Adherent>(`${this.apiUrl}/code/${code}`);
  }

  /**
   * Recherche les souscripteurs par critères
   */
  searchSouscripteurs(searchTerm: string): Observable<Adherent[]> {
    return this.http.get<Adherent[]>(`${this.apiUrl}/search`, {
      params: { q: searchTerm }
    });
  }
}
