export interface StatistiqueTypePrestation {
  typeId: string;
  typeNom: string;
  categorie: string;
  montantTotal: number;
  montantRembourse: number;
  nombreUtilisations: number;
  pourcentageTotal: number;
  tauxRemboursementMoyen: number;
}