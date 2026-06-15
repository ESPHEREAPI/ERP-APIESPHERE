import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AuthService } from './auth.service';
import { ProfilAgent } from '../model/infos-admin-agence.model';

/**
 * Guard de contrôle d'accès par profilAgent.
 *
 * Utilisation dans les routes :
 *   data: { profilsAutorises: ['ADMINISTRATEUR', 'CHEF_BUREAU_AGENT'] }
 *
 * Si profilAgent non encore chargé (null/undefined) → accès refusé et redirection.
 * Redirection en cas de refus → /esphere-ass/certificates/validation (page par défaut PRODUCTEUR).
 */
@Injectable({ providedIn: 'root' })
export class ProfilGuard implements CanActivate {

  /** Route de repli pour les profils sans accès */
  private readonly FALLBACK = '/certificates/validation';

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    _state: RouterStateSnapshot
  ): boolean | UrlTree {

    const profilsAutorises: ProfilAgent[] | undefined = route.data['profilsAutorises'];

    // Pas de restriction définie sur cette route → accès libre
    if (!profilsAutorises || profilsAutorises.length === 0) {
      return true;
    }

    const profilAgent = this.authService.currentUserValue?.profilAgent;

    // profilAgent présent et autorisé → accès accordé
    if (profilAgent && profilsAutorises.includes(profilAgent)) {
      return true;
    }

    // Accès refusé → redirection silencieuse vers la page par défaut
    console.warn(`[ProfilGuard] Accès refusé : profilAgent="${profilAgent}" → route protégée exige ${profilsAutorises.join(',')}`);
    return this.router.createUrlTree([this.FALLBACK]);
  }
}
