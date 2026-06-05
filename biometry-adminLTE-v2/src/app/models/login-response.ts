import { User } from "./user";

export interface LoginResponse {
  success: boolean;
  token?: string;
  user?: User;
  message?: string;

  refreshToken?: string;
  expiresIn?: number;

}

