import { PlafondParType } from "./PlafondParType";

export interface TauxUtilisationPlafond {
  plafondGlobal: number;
  montantUtilise: number;
  montantRestant: number;
  pourcentageUtilisation: number;
  detailsParType: PlafondParType[];
}