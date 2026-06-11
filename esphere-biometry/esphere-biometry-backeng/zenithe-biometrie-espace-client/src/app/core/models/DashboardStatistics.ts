import { Alerte } from "./Alerte";
import { ConsommationGlobale } from "./ConsommationGlobale";
import { Periode } from "./Periode";
import { StatistiquePeriode } from "./StatistiquePeriode";
import { StatistiqueTypePrestation } from "./StatistiqueTypePrestation";
import { TauxUtilisationPlafond } from "./TauxUtilisationPlafond";
import { TopPrestataire } from "./TopPrestataire";
import { TopPrestation } from "./TopPrestation";

export interface DashboardStatistics {
  consommationGlobale: ConsommationGlobale;
  statistiquesParPeriode: StatistiquePeriode[];
  statistiquesParTypePrestation: StatistiqueTypePrestation[];
  topPrestations: TopPrestation[];
  topPrestataires: TopPrestataire[];
  tauxUtilisationPlafond: TauxUtilisationPlafond;
  alertes: Alerte[];
  dateGeneration: string;
  codeSouscripteur: string;
  nomSouscripteur?: string;
  periode: Periode;
}