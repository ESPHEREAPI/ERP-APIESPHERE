import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, of } from 'rxjs';

export interface StatMoisResponse {
  mois: number;
  libelleMois: string;
  nombre: number;
  montant: number;
}

export interface DashboardSsResponse {
  consultationsEnAttente: number;
  ordonnancesEnAttente: number;
  examensEnAttente: number;
  bonsManuelEnAttente: number;
  totalValidesMois: number;
  totalRejetesMois: number;
  totalEncaissesMois: number;
  montantEncaisseMois: number;
  totalVisitesAnnee: number;
  montantTotalAnnee: number;
  consultationsParMois: StatMoisResponse[];
  ordonnancesParMois: StatMoisResponse[];
  examensParMois: StatMoisResponse[];
  montantsParMois: StatMoisResponse[];
  alertesNonLues: number;
}

export interface DashboardPrestataireResponse {
  prestataireId: string;
  categorieId: string;
  visitesAujourdhui: number;
  prestationsEnAttenteAujourdhui: number;
  consultationsMois: number;
  ordonnancesMois: number;
  examensMois: number;
  bonsManuelsMois: number;
  validesMois: number;
  rejetesMois: number;
  encaissesMois: number;
  montantEncaisseMois: number;
  totalVisitesAnnee: number;
  montantTotalAnnee: number;
  encaissementsParMois: StatMoisResponse[];
  alertesNonLues: number;
  accesConsultation: boolean;
  accesOrdonnance: boolean;
  accesExamen: boolean;
  accesBonManuel: boolean;
}

export interface EtatPrestationItem {
  id: number;
  nature: string;
  date: string;
  codeAdherent: string;
  codeAyantDroit: string | null;
  nomAssure: string | null;
  nomAyantDroit: string | null;
  souscripteur: string | null;
  taux: number;
  etatGlobal: string;
  nbreLignes: number;
  montantSoumis: number;
  montantValide: number;
}

export interface EtatPrestationPageResponse {
  content: EtatPrestationItem[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
  montantSoumisTotalPage: number;
  montantValideTotalPage: number;
}

@Injectable({ providedIn: 'root' })
export class DashboardService {

  constructor(private http: HttpClient) {}

  // Dashboard Agent SS
  // GET /reporting/dashboard/ss/{employeId}?annee=2026
  getDashboardSS(employeId: string | number, annee?: number): Observable<DashboardSsResponse> {
    const a = annee || new Date().getFullYear();
    return this.http.get<DashboardSsResponse>(`/reporting/dashboard/ss/${employeId}?annee=${a}`)
      .pipe(catchError(err => {
        console.error('Dashboard SS error:', err);
        return of({} as DashboardSsResponse);
      }));
  }

  // État des prestations prestataire (paginé + filtres)
  getEtatPrestationsPrestataire(
    prestataireId: string,
    filtres: { nature?: string; statut?: string; mois?: number; annee?: number; page?: number; size?: number } = {}
  ): Observable<EtatPrestationPageResponse> {
    const p = new URLSearchParams();
    if (filtres.nature)  p.set('nature',  filtres.nature);
    if (filtres.statut)  p.set('statut',  filtres.statut);
    if (filtres.mois)    p.set('mois',    String(filtres.mois));
    if (filtres.annee)   p.set('annee',   String(filtres.annee));
    p.set('page', String(filtres.page  ?? 0));
    p.set('size', String(filtres.size  ?? 20));
    return this.http.get<EtatPrestationPageResponse>(
      `/reporting/prestations/prestataire/${prestataireId}?${p.toString()}`
    ).pipe(catchError(() => of({
      content: [], totalElements: 0, totalPages: 0, currentPage: 0,
      pageSize: 20, montantSoumisTotalPage: 0, montantValideTotalPage: 0
    })));
  }

  // Dashboard Prestataire
  // GET /reporting/dashboard/prestataire/{prestataireId}?categorieId=&annee=
  getDashboardPrestataire(prestataireId: string, categorieId: string, annee?: number): Observable<DashboardPrestataireResponse> {
    const a = annee || new Date().getFullYear();
    return this.http.get<DashboardPrestataireResponse>(
      `/reporting/dashboard/prestataire/${prestataireId}?categorieId=${categorieId}&annee=${a}`
    ).pipe(catchError(err => {
      console.error('Dashboard Prestataire error:', err);
      return of({} as DashboardPrestataireResponse);
    }));
  }
}
