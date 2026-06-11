export interface Periode {
  dateDebut: string;
  dateFin: string;
  type: 'JOUR' | 'SEMAINE' | 'MOIS' | 'ANNEE' | 'PERSONNALISE';
}