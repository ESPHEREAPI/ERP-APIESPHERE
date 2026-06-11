import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, catchError } from 'rxjs';
import { SessionContext } from '../auth/SessionContext';


export interface PlafondDTO {
  plafondGlobal: number;
  montantConsomme: number;
  montantRestant: number;
  pourcentageConsomme: number;
  niveau: 'NORMAL' | 'WARNING' | 'DANGER';
}

export interface ConsommationDTO {
  // Totaux
  montantTotalPriseEnCharge: number;
  montantTotalTicketModerateur: number;
  montantTotalDepense: number;
  montantAyantsDroits: number;
  // Consultation
  pecConsultation: number;
  tmConsultation: number;
  nombreConsultations: number;
  // Examen
  pecExamen: number;
  tmExamen: number;
  nombreExamens: number;
  // Ordonnance
  pecOrdonnance: number;
  tmOrdonnance: number;
  nombreOrdonnances: number;
}

export interface AyantDroitDTO {
  codeAyantDroit: string;
  nom: string;
  sexe: string;
  lienpare: string;
  statut: string;
  naissance: string;
  montantConsomme: number;
}

export interface VisiteRecenteDTO {
  date: string;
  prestataireId: string;
  nomPrestataire: string;
  typePrestation: string;
  montant: number;
  montantPriseEnCharge: number;
  montantTicketModerateur: number;
  /** Taux de couverture contractuel (%) de cette prestation */
  taux: number;
  etat: string;
  ayantDroit: boolean;
  nomBeneficiaire: string;
}

export interface DashboardAdherentDTO {
  codeAdherent: string;
  nom: string;
  sexe: string;
  telephone: string;
  matricule: string;
  statut: string;
  police: string;
  souscripteur: string;
  /** Groupe contractuel de l'adhérent */
  groupe: number | null;
  /** Taux de couverture contractuel (%) */
  taux: number | null;
  naissance: string;
  effetPolice: string;
  echeancePolice: string;
  joursAvantEcheance: number;
  niveauAlertEcheance: 'NORMAL' | 'WARNING' | 'DANGER' | 'EXPIRE';
  plafond: PlafondDTO;
  consommation: ConsommationDTO;
  ayantsDroits: AyantDroitDTO[];
  nombreAyantsDroits: number;
  dernieresVisites: VisiteRecenteDTO[];
  totalVisites: number;
  pageVisites: number;
  /** Montant base total des visites filtrées (toutes pages) */
  filteredTotalMontantBase: number;
  /** Montant PEC total des visites filtrées (toutes pages) */
  filteredTotalMontantPEC: number;
}

export interface FiltreVisitesDTO {
  prestataireId?: string;
  dateDebut?: string;
  dateFin?: string;
  typePrestation?: string;
  codeAyantDroit?: string;
}

@Injectable({ providedIn: 'root' })
export class DashboardAdherentService {

  private readonly apiUrl = '/dashboard/adherent/me';

  private dataSubject   = new BehaviorSubject<DashboardAdherentDTO | null>(null);
  private loadingSubject = new BehaviorSubject<boolean>(false);

  readonly data$    = this.dataSubject.asObservable();
  readonly loading$ = this.loadingSubject.asObservable();

  constructor(
    private http: HttpClient,
    private session: SessionContext
  ) {}

  load(codeAdherent?: string, page: number = 0, size: number = 10,
       filtres?: FiltreVisitesDTO): Observable<DashboardAdherentDTO> {
    this.loadingSubject.next(true);

    const params: string[] = [`page=${page}`, `size=${size}`];
    if (codeAdherent?.trim()) params.push(`codeAdherent=${encodeURIComponent(codeAdherent.trim())}`);
    if (filtres?.prestataireId)   params.push(`prestataireId=${encodeURIComponent(filtres.prestataireId)}`);
    if (filtres?.dateDebut)       params.push(`dateDebut=${filtres.dateDebut}`);
    if (filtres?.dateFin)         params.push(`dateFin=${filtres.dateFin}`);
    if (filtres?.typePrestation)  params.push(`typePrestation=${filtres.typePrestation}`);
    if (filtres?.codeAyantDroit)  params.push(`codeAyantDroit=${encodeURIComponent(filtres.codeAyantDroit)}`);
    const url = `${this.apiUrl}?${params.join('&')}`;

    return this.http.get<DashboardAdherentDTO>(url).pipe(
      tap(data => {
        this.dataSubject.next(data);
        this.loadingSubject.next(false);
      }),
      catchError(err => {
        this.loadingSubject.next(false);
        throw err;
      })
    );
  }

  // ── Formatage ─────────────────────────────────────────────────────────────

  formatCurrency(v: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency', currency: 'XAF',
      minimumFractionDigits: 0, maximumFractionDigits: 0
    }).format(v || 0);
  }

  formatDate(d: string): string {
    if (!d) return '—';
    return new Date(d).toLocaleDateString('fr-FR');
  }

  getPlafondBarClass(niveau: string): string {
    return niveau === 'DANGER' ? 'bg-danger'
         : niveau === 'WARNING' ? 'bg-warning'
         : 'bg-success';
  }

  getEcheanceClass(niveau: string): string {
    return niveau === 'EXPIRE' || niveau === 'DANGER' ? 'text-danger'
         : niveau === 'WARNING' ? 'text-warning'
         : 'text-success';
  }

  getEtatBadgeClass(etat: string): string {
    const map: Record<string, string> = {
      'encaisse': 'badge-success',
      'valide':   'badge-info',
      'rejete':   'badge-danger',
      'attente':  'badge-warning'
    };
    return map[etat?.toLowerCase()] ?? 'badge-secondary';
  }
}