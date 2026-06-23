import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface MediaResponse {
  id:               number;
  visiteId:         string;
  prestationId:     number | null;
  naturePrestation: string | null;
  codeAdherent:     string;
  codeAyantDroit:   string | null;
  prestataireId:    string;
  nomFichier:       string;
  typeMedia:        string;
  extension:        string;
  taille:           number;
  demandeParSs:     boolean;
  employeId:        number | null;
  dateUpload:       string;
  statutDocument:   string;
  commentaireRejet: string | null;
}

@Injectable({ providedIn: 'root' })
export class MediaService {

  private base = environment.mediaUrl;

  constructor(private http: HttpClient) {}

  // Charger tous les médias d'une prestation
  getParPrestation(prestationId: number): Observable<MediaResponse[]> {
    return this.http.get<MediaResponse[]>(
      `${this.base}/medias/prestation/${prestationId}`
    );
  }

  // Charger tous les médias d'une visite
  getParVisite(visiteId: string): Observable<MediaResponse[]> {
    return this.http.get<MediaResponse[]>(
      `${this.base}/medias/visite/${visiteId}`
    );
  }

  // SS approuve le document
  approuver(mediaId: number, employeId: number): Observable<MediaResponse> {
    return this.http.put<MediaResponse>(
      `${this.base}/medias/${mediaId}/approuver`,
      null,
      { params: new HttpParams().set('employeId', employeId) }
    );
  }

  // SS rejette le document avec commentaire obligatoire
  rejeter(mediaId: number, commentaire: string, employeId: number): Observable<MediaResponse> {
    return this.http.put<MediaResponse>(
      `${this.base}/medias/${mediaId}/rejeter`,
      { commentaire },
      { params: new HttpParams().set('employeId', employeId) }
    );
  }

  // Upload via code court (mobile)
  uploadParCodeCourt(
    codeCourt:       string,
    fichier:         File,
    prestationId:    number,
    naturePrestation: string,
    employeId?:      number
  ): Observable<MediaResponse> {
    const form = new FormData();
    form.append('fichier', fichier);
    form.append('prestationId', String(prestationId));
    form.append('naturePrestation', naturePrestation);
    if (employeId != null) form.append('employeId', String(employeId));
    return this.http.post<MediaResponse>(
      `${this.base}/capture/${codeCourt}`, form
    );
  }

  // URL de lecture d'un média (pour <video> ou <img>)
  getUrlMedia(mediaId: number): string {
    return `${this.base}/medias/${mediaId}/telecharger`;
  }
}
