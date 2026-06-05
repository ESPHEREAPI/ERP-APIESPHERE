import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ConsultationEnAttente {
  id: number;
  visiteId: string;
  codeAdherent: string;
  codeAyantDroit: string | null;
  prestataireId: string;
  prestataireNom: string
  typeConsultation: string;
  natureConsultation: string;
  natureAffection: string | null;
  montant: number;         // montant déclaré (immuable)
  montantValide?: number;  // ← AJOUTER : montantModif côté backend
  partZenithe?: number;    // ← AJOUTER
  partAssure?: number;     // ← AJOUTER
  //montant: number;
  taux: number;
  observations: string | null;
  etatConsultation: string;
  date: string;
}

export interface PageResponse {
  content: ConsultationEnAttente[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
}

export interface AdherentInfo {
  codeAdherent: string;
  assurePrincipal: string;
  groupe: number;
  souscripteur: string;
  taux: number;
  police: string;
}

export interface AyantDroitInfo {
  codeAyantDroit: string;
  nom: string;
  lienPare: string;
}

export interface ValidationConsultationRequest {
  decision: string;
  employeId: number;
  observations: string | null;
  montantModif: number | null;
  taux: number | null;
}
// ── Interface consommation ────────────────────────────────────────────────────
export interface ConsommationResponse {
  codeAdherent: string;
  nomAssure: string;
  souscripteur: string;
  groupe: number;
  annee: number;
  plafondGlobal: number;
  // Encaissés
  montantConsultationsEncaissees: number;
  nbreConsultationsEncaissees: number;
  montantOrdonnancesEncaissees: number;
  nbreOrdonnancesEncaissees: number;
  montantExamensEncaisses: number;
  nbreExamensEncaisses: number;
  montantBonsManuelsEncaisses: number;
  nbreBonsManuelsEncaisses: number;
  totalEncaisse: number;
  // En cours
  montantConsultationsEnCours: number;
  montantOrdonnancesEnCours: number;
  montantExamensEnCours: number;
  montantBonsManuelsEnCours: number;
  totalEnCours: number;
  // Projection
  totalProjecte: number;
  soldeApresEncaisse: number;
  soldeApresProjection: number;
  pourcentageEncaisse: number;
  pourcentageProjecte: number;
  // Alerte
  niveauAlerte: 'NORMAL' | 'ATTENTION' | 'CRITIQUE';
  messageAlerte: string;
}

@Injectable({ providedIn: 'root' })
export class ConsultationService {

  constructor(private http: HttpClient) { }

  getConsultationsPaginees(
    page: number,
    size: number,
    filtres: {
      prestataireId?: string;
      etat?: string;
      typeConsultation?: string;
      souscripteur?: string;
      nomAdherent?: string;
      nomAyantDroit?: string;
      dateMin?: string;
      dateMax?: string;
    } = {}
  ): Observable<PageResponse> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (filtres.prestataireId) params = params.set('prestataireId', filtres.prestataireId);

    if (filtres.etat) params = params.set('etat', filtres.etat);
    if (filtres.typeConsultation) params = params.set('typeConsultation', filtres.typeConsultation);
    if (filtres.souscripteur) params = params.set('souscripteur', filtres.souscripteur);
    if (filtres.nomAdherent) params = params.set('nomAdherent', filtres.nomAdherent);
    if (filtres.nomAyantDroit) params = params.set('nomAyantDroit', filtres.nomAyantDroit);
    if (filtres.dateMin) params = params.set('dateMin', filtres.dateMin);
    if (filtres.dateMax) params = params.set('dateMax', filtres.dateMax);

    return this.http.get<PageResponse>('/validations/consultations', { params });
  }

  validerConsultation(
    id: number,
    request: ValidationConsultationRequest
  ): Observable<ConsultationEnAttente> {
    return this.http.put<ConsultationEnAttente>(
      `/validations/consultations/${id}`, request);
  }

  encaisserConsultation(id: number): Observable<void> {
    return this.http.put<void>(`/validations/consultations/${id}/encaisser`, {});
  }

  getAdherent(codeAdherent: string): Observable<AdherentInfo> {
    return this.http.get<AdherentInfo>(`/adherents/${codeAdherent}`);
  }

  getAyantDroit(codeAyantDroit: string): Observable<AyantDroitInfo> {
    return this.http.get<AyantDroitInfo>(`/adherents/ayants-droit/${codeAyantDroit}`);
  }
  getConsommation(visiteId: string): Observable<ConsommationResponse> {
    return this.http.get<ConsommationResponse>(
      `/validations/consommation/visite/${encodeURIComponent(visiteId)}`
    );
  }
}