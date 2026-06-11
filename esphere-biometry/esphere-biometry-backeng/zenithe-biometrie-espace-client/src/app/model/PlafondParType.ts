export interface PlafondParType {
  typeId: string;
  typeNom: string;
  plafond: number;
  montantUtilise: number;
  montantRestant: number;
  pourcentageUtilisation: number;
  statut: 'NORMAL' | 'ATTENTION' | 'CRITIQUE';
}