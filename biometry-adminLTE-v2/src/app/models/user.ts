// Modèle User adapté ESPHERE (tiré du JWT/session)
export interface User {
  token: string;
  utilisateurId: number;
  login: string;
  nom: string;
  prenom: string;
  profilCode: string;
  profilLibelle: string;
  prestataireId?: string;
  langueDefaut?: number;
  menus: any[];
  // Champs legacy pour compatibilité avec header/sidebar
  nomcomplet?: string;
  profil_name?: string;
  prestataire?: string;
  role?: any;
}
