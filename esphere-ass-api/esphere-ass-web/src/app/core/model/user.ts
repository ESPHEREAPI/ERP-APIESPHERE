import { Roles } from "./roles";

export interface User {
  id?: number;
  codeagence: number;
  username: string;
  email: string;
  password?: string;
  nomcomplet: string;
 profil:number;
  isActive: boolean;
  createdAt?: Date;
  updatedAt?: Date;
  lastLogin?: Date;
  profileImageUrl: string;
  roleId: number;
  role?: Roles;
  messageEcheck: string;
  echeck_connection: boolean;

}
