import { ProfilType } from "../../shared/enums/ProfilType";

export interface LoginRequest {
   userName:string;
   passWord:string;
   profilType?:ProfilType
}
