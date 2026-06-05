export interface StatistiquesMensuelles {
     mois: string;
  consultations: number;
  ordonnances: number;
  examens: number;
  montantConsultations?: number;
  montantOrdonnances?: number;
  montantExamens?: number;
}
