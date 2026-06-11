export interface ConsommationAyantDroit {
  montantTotalDepense: number;
  montantTotalRembourse: number;
  montantTotalACharge: number;
  nombreConsultations: number;
  nombrePrestations: number;
  dateDerniereConsultation?: Date;
  tendance: 'HAUSSE' | 'BAISSE' | 'STABLE';
}