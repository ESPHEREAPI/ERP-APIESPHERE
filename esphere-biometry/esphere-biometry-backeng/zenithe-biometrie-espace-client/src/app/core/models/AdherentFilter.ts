export interface AdherentFilter {
  search?: string;
  souscripteur?: string;
  police?: string;
  groupe?: number;
  statut?: string;
  sexe?: string;
  enrole?: string;
  dateAdhesionMin?: Date;
  dateAdhesionMax?: Date;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: 'ASC' | 'DESC';
}