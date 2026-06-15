export type ProfilAgent =
  | 'PRODUCTEUR'
  | 'CHEF_BUREAU_AGENT'
  | 'CHEF_BUREAU_DIRECT_SIEGE'
  | 'ADMINISTRATEUR';

export const PROFILS_AGENT: { value: ProfilAgent; label: string }[] = [
  { value: 'PRODUCTEUR',               label: 'ADMIN_AGENCE.PROFIL_PRODUCTEUR' },
  { value: 'CHEF_BUREAU_AGENT',        label: 'ADMIN_AGENCE.PROFIL_CHEF_AGENT' },
  { value: 'CHEF_BUREAU_DIRECT_SIEGE', label: 'ADMIN_AGENCE.PROFIL_CHEF_SIEGE' },
  { value: 'ADMINISTRATEUR',           label: 'ADMIN_AGENCE.PROFIL_ADMIN' },
];

export interface InfosAdminAgence {
  id?: number;
  codeAgence: number;
  libelleAgence: string;
  email: string;
  login: string;
  clientName: string;
  expiresAt: number;
  officeCode: string;
  username: string;
  profilAgent: ProfilAgent;
  canEdit: boolean;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  errors?: any;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}
