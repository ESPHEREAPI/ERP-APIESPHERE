import { EtatPrestation } from "../enums/etat-prestation";
import { TypePrestation } from "./type-prestation";

export interface LignePrestation {
     id?: number;
  taux?: number;
  dentsConcernees?: string;
  codification?: string;
  nom?: string;
  valeur?: number;
  nbre?: number;
  actePrelevement: number;
  valeurModif?: number;
  nbreModif?: number;
  actePrelevementModif: number;
  posologie?: string;
  observations?: string;
  observationsActePrelevement?: string;
  date: Date;
  dateValideRejete?: Date;
  dateEncaisse?: Date;
  etat: EtatPrestation;
  supprime: string;
  employeValideRejeteId?: any;
  examenId?: any;
  medicamentId?: any;
  prestataireId?: any;
  prestationId: any;
  typeExamen?: TypePrestation;
  descriptionSoins?: TypePrestation;
}
