export interface PieceIdentite {
  type: string; // CNI, PASSEPORT, ACTE_NAISSANCE, etc.
  numero: string;
  dateDelivrance?: Date;
  dateExpiration?: Date;
  fichier?: string; // Base64 ou URL
}