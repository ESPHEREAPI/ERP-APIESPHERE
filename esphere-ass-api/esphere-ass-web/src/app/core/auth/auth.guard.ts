import { Injectable } from "@angular/core";
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree
} from "@angular/router";
import { AuthService } from "./auth.service";
import { ProfilType } from "../../shared/enum/ProfilType";


/** Mapping profilType → route de login */
const LOGIN_ROUTES: Record<string, string> = {
  [ProfilType.PAYLOAD]: '/login-payload',
  [ProfilType.ESPHERE]: '/login',
};

/** Route de login par défaut si profilType inconnu */
const DEFAULT_LOGIN = '/login-payload';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean | UrlTree {

    // ✅ Connecté → accès autorisé
    if (this.authService.isLoggedIn()) {
      return true;
    }

    // ❌ Non connecté → redirection vers le bon login
    return this.buildLoginRedirect(state.url);
  }

  // ── Privé ────────────────────────────────────────────────────────────────

  private buildLoginRedirect(returnUrl: string): UrlTree {
    const profilType  = this.authService.currentUserValue?.profilType ?? null;
    const loginRoute  = (profilType && LOGIN_ROUTES[profilType]) ?? DEFAULT_LOGIN;

    return this.router.createUrlTree(
      [loginRoute],
      { queryParams: { returnUrl } }
    );
  }
}