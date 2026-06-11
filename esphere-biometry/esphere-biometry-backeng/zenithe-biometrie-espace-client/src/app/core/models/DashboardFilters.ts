export interface DashboardFilters {
  codeSouscripteur?: string;
  dateDebut?: string;
  dateFin?: string;
  periodeType?: 'current-month' | 'current-year' | 'last-week' | 'custom';
}