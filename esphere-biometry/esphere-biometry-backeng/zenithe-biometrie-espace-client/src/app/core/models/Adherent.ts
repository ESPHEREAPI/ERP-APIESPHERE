import { AyantDroit } from "./AyantDroit";
import { ConsommationAdherent } from "./ConsommationAdherent";
import { PlafondAdherent } from "./PlafondAdherent";

export interface Adherent {
  codeAdherent?: string;
  numero?: number;
  assurePrincipal: string;
  naissance: Date;
  sexe: 'M' | 'F';
  matricule?: string;
  telephone?: string;
  email?: string;
  taux?: number;
  souscripteur: string;
  police: string;
  effetPolice?: Date;
  echeancePolice?: Date;
  groupe?: number;
  enrole?: string;
  dateEnrole?: Date;
  imprime?: '1' | '-1';
  statut: '1' | '-1' | '-1' | '-1';
  photo?: string;
  adresse?: string;
  ville?: string;
  ayantsDroits?: AyantDroit[];
  consommation?: ConsommationAdherent;
  plafonds?: PlafondAdherent;
  dateCreation?: Date;
  dateModification?: Date;
}