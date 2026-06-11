export interface StatistiquePeriode {
  periode: string;
  libelle: string;
  dateDebut?: string;
  dateFin?: string;
  montantTotal: number;
  montantRembourse: number;
  nombreConsultations: number;
  nombrePrestations?: number;
}