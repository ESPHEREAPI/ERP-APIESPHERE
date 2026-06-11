import { ProfilType } from "../../shared/enums/ProfilType";
import { Roles } from "./roles";

export interface User {
    id?: number;
  username: string;
   userName: string;
  email: string;
  password?: string;
  firstName: string;
   lastName: string;
  lastname: string;
  isActive: boolean;
  createdAt?: Date;
  updatedAt?: Date;
  lastLogin?: Date;
  profileImageUrl: string;
  roleId: number;
  role?: Roles;
  messageEcheck: string;
  echeck_connection: boolean ; 
  profilType?: ProfilType;
   effet?:string;
   echeance?:string;
   codeAdherent?:string

}
