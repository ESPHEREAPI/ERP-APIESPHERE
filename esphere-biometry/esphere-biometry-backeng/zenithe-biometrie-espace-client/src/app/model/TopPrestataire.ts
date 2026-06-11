export interface TopPrestataire {
  rang: number;
  prestataireId: string;
  nomPrestataire: string;
  categoriePrestataire: string;
  ville?: string;
  nombreVisites: number;
  montantTotal: number;
  pourcentageUtilisation: number;
}