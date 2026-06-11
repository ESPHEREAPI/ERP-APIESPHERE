export interface AyantDroitFilter {
  search?: string;
  codeAdherent?: string;
  sexe?: string;
  lienpare?: string;
  statut?: string;
  enrole?: string;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: 'ASC' | 'DESC';
}

export const LIEN_PARENTE_OPTIONS = [
  { value: 'CONJOINT', label: 'Conjoint(e)' },
  { value: 'ENFANT', label: 'Enfant' },
  { value: 'PERE', label: 'Père' },
  { value: 'MERE', label: 'Mère' },
  { value: 'FRERE', label: 'Frère' },
  { value: 'SOEUR', label: 'Sœur' },
  { value: 'AUTRE', label: 'Autre' }
];

export const TYPE_PIECE_OPTIONS = [
  { value: 'CNI', label: 'Carte Nationale d\'Identité' },
  { value: 'PASSEPORT', label: 'Passeport' },
  { value: 'ACTE_NAISSANCE', label: 'Acte de Naissance' },
  { value: 'AUTRE', label: 'Autre' }
];