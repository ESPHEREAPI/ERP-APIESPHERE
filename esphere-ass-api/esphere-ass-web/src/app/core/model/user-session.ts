import { ProfilType } from "../../shared/enum/ProfilType";
import { ProfilAgent } from "./infos-admin-agence.model";
import { User } from "./user";


export interface UserSession {
    userDTO: User;
    userapiasac: string;
    token: string;
    permissions: string[];
    expiresAt: Date;
    profilType?:  ProfilType;
    agencyName?:  string;
    agencyCode?:  string;
    companyName?: string;
    // Enrichi après login via /admin-agences/by-username/{username}
    profilAgent?: ProfilAgent;
    canEdit?:     boolean;
}
