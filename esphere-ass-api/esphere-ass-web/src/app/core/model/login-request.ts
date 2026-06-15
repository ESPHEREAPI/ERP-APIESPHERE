import { ProfilType } from "../../shared/enum/ProfilType";

export interface LoginRequest {
   username:string;
   password:string;
   profilType?:ProfilType
}
