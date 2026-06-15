import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PrestationExamenResponse {
  id:                  number;
  visiteId:            string;
  prestataireId:       string;
  nomPrestataire:      string | null;
  naturePrestation:    string;
  date:                string;
  codeAdherent:        string;
  codeAyantDroit:      string | null;
  nomAssure:           string | null;
  nomAyantDroit:       string | null;
  souscripteur:        string | null;
  groupe:              number | null;
  natureAffection:     string | null;
  nbreLignes:          number;
  nbreLignesEnAttente: number;
  etatGlobal:          string;
  statutAdherent:      string;
}

export interface PageExamenResponse {
  content:       PrestationExamenResponse[];
  totalElements: number;
  totalPages:    number;
  currentPage:   number;
  pageSize:      number;
}

export interface LigneExamen {
  id:                    number;
  prestationId:          number;
  prestataireId:         string;
  nomPrestataire:        string | null;
  visiteId:              string | null;
  codeAdherent:          string | null;
  codeAyantDroit:        string | null;
  nomAssure:             string | null;
  nomAyantDroit:         string | null;
  souscripteur:          string | null;
  groupe:                number | null;
  natureAffection:       string | null;
  nom:                   string;
  codification:          string | null;
  typeExamen:            string | null;
  descriptionSoins:      string | null;
  taux:                  number | null;
  valeur:                number | null;
  nbre:                  number | null;
  actePrelevement:       number;
  actePrelevementModif:  number | null;
  valeurModif:           number | null;
  nbreModif:             number | null;
  observations:          string | null;
  etat:                  string;
  date:                  string;
  // Champs locaux
  valeurModifLocal?:     number;
  nbreModifLocal?:       number;
  tauxLocal?:            number;
  observationsLocal?:    string;
  decisionLocale?:       'valide' | 'rejete' | null;
  statutAdherent :        string;
}

export interface ValidationLigneExamenRequest {
  decision:             string;
  employeId:            number;
  observations:         string | null;
  valeurModif:          number | null;
  nbreModif:            number | null;
  taux:                 number | null;
  actePrelevementModif: number | null;
}

@Injectable({ providedIn: 'root' })
export class ExamenService {

  constructor(private http: HttpClient) {}

  getPrestationsPaginees(
    page: number,
    size: number,
    filtres: {
      prestataireId?: string;
      dateMin?:       string;
      dateMax?:       string;
      souscripteur?:  string;
      adherent?:      string;
      ayantDroit?:    string;
      etat?:          string;
    } = {}
  ): Observable<PageExamenResponse> {
    let params = new HttpParams()
      .set('page',   page.toString())
      .set('size',   size.toString())
      .set('nature', 'examen');

    if (filtres.prestataireId) params = params.set('prestataireId', filtres.prestataireId);
    if (filtres.dateMin)       params = params.set('dateMin',       filtres.dateMin);
    if (filtres.dateMax)       params = params.set('dateMax',       filtres.dateMax);
    if (filtres.souscripteur)  params = params.set('souscripteur',  filtres.souscripteur);
    if (filtres.adherent)      params = params.set('adherent',      filtres.adherent);
    if (filtres.ayantDroit)    params = params.set('ayantDroit',    filtres.ayantDroit);
    if (filtres.etat)          params = params.set('etat',          filtres.etat);

    return this.http.get<PageExamenResponse>(
      '/validations/prestations', { params });
  }

  getLignesByPrestation(prestationId: number): Observable<LigneExamen[]> {
    return this.http.get<LigneExamen[]>(
      `/validations/lignes/prestation/${prestationId}`
    );
  }

  validerLigne(
    id: number,
    request: ValidationLigneExamenRequest
  ): Observable<LigneExamen> {
    return this.http.put<LigneExamen>(
      `/validations/lignes/${id}`, request);
  }

  encaisserLigne(id: number): Observable<void> {
    return this.http.put<void>(
      `/validations/lignes/${id}/encaisser`, {});
  }

  getConsommation(visiteId: string): Observable<any> {
    const encoded = visiteId.replace(/\./g, '%2E');
    return this.http.get<any>(
      `/validations/consommation/visite/${encoded}`);
  }
}