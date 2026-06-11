export interface PlafondParCategorie {
  categorieId: string;
  categorieNom: string;
  plafond: number;
  montantUtilise: number;
  montantRestant: number;
  pourcentageUtilisation: number;
  statut: 'DISPONIBLE' | 'ATTENTION' | 'EPUISE';
}