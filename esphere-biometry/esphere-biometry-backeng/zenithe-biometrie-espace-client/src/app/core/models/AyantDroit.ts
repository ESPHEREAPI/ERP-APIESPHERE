import { ConsommationAyantDroit } from "./ConsommationAyantDroit";
import { PieceIdentite } from "./PieceIdentite";

export interface AyantDroit {
  codeAyantDroit?: string;
  codeAdherent: string;
  nom: string;
  sexe: 'M' | 'F';
  naissance: Date;
  lienpare: string;
  telephone?: string;
  email?: string;
  statut: 'ACTIF' | 'SUSPENDU' | 'RESILIE';
  photo?: string;
  adresse?: string;
  ville?: string;
  enrole?: 'O' | 'N';
  dateEnrole?: Date;
  imprime?: 'O' | 'N';
  pieceIdentite?: PieceIdentite;
  consommation?: ConsommationAyantDroit;
  dateCreation?: Date;
  dateModification?: Date;
}