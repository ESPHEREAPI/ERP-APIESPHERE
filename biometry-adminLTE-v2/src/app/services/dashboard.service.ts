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
