import { EtatPrestation } from "../enums/etat-prestation";
import { TypePrestation } from "./type-prestation";

export interface Consultation {
    id?: number;
  taux?: number;
  natureConsultation: string;
  natureAffection?: string;
  montant: number;
  montantModif?: number;
  date: Date;
  dateValideRejete?: Date;
  observations?: string;
  etatConsultation: EtatPrestation;
  supprime: string;
  employeValideRejeteId?: any;
  typeConsultation: TypePrestation;
  visiteId: any;
}
