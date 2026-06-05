// Modèle adapté pour l'API ESPHERE
export interface UserSession {
  token: string;
  userId: number;
  login: string;
  nom: string;
  prenom: string;
  profilCode: string;
  profilLibelle: string;
  prestataireId?: string;
  menus: MenuEsphere[];
}

export interface MenuEsphere {
  id: number;
  nomModule: string;
  nomControlleur: string;
  classImage?: string;
  niveauMenu: number;
  parentId?: number;
  position?: number;
  actif: boolean;
}
