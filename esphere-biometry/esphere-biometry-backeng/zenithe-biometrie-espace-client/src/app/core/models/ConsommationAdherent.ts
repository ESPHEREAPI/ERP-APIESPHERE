export interface ConsommationAdherent {
  montantTotalDepense: number;
  montantTotalRembourse: number;
  montantTotalACharge: number;
  nombreConsultations: number;
  nombrePrestations: number;
  dateDerniereConsultation?: Date;
  moyenneParConsultation?: number;
  tendance: 'HAUSSE' | 'BAISSE' | 'STABLE';
}