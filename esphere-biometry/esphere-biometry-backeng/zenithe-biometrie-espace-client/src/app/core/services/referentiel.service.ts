import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SouscripteurActifDTO {
  souscripteur: string;  // ex: CAMTE
  police: string;        // ex: 1017-2310000100
  libelle: string;       // ex: CAMTE (1017-2310000100)
}

export interface AdherentSimpleDTO {
  codeAdherent: string;
  nom: string;
  matricule: string;
  statut: string;
}

@Injectable({ providedIn: 'root' })
export class ReferentielService {

  constructor(private http: HttpClient) {}

  /**
   * Liste des souscripteurs actifs (police en vigueur aujourd'hui)
   * pour peupler le dropdown souscripteur dans les vues DII / Service Santé
   */
  getSouscripteursActifs(): Observable<SouscripteurActifDTO[]> {
    return this.http.get<SouscripteurActifDTO[]>('/partenaire/adherents/souscripteurs/actifs');
  }

  /**
   * Liste allégée des adhérents d'une police donnée
   * pour peupler la liste adhérent après sélection du souscripteur
   */
  getAdherentsByPolice(police: string): Observable<AdherentSimpleDTO[]> {
    return this.http.get<AdherentSimpleDTO[]>(`/partenaire/adherents/by-police/${police}`);
  }
}
