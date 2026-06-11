import { PlafondParCategorie } from "./PlafondParCategorie";

export interface PlafondAdherent {
  plafondGlobal: number;
  montantUtilise: number;
  montantRestant: number;
  pourcentageUtilisation: number;
  detailsParCategorie: PlafondParCategorie[];
}