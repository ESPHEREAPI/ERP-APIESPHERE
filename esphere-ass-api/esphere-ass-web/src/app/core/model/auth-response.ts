import { ProfilType } from "../../shared/enum/ProfilType";
import { UserSession } from "./user-session";

export interface AuthResponse<T> {
 success: boolean;
  message: string;
  data: UserSession | null;
  errorCode?: string;
  

}
