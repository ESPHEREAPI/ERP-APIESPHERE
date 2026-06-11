import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';

import { ProfilType } from '../../shared/enums/ProfilType';
import { SessionContext } from './SessionContext';


/**
 * RoleGuard — protection des routes par permission ET par profil.
 *
 * Utilisation dans app.routes.ts :
 *
 *   // Par permission (existant)
 *   canActivate: [RoleGuard],
 *   data: { permissions: ['WRITE'], permissionCheckMethod: 'any' }
 *
 *   // Par profil
 *   canActivate: [RoleGuard],
 *   data: { profils: ['DII', 'SERVICE_SANTE'] }
 *
 *   // Combiné (profil ET permission)
 *   canActivate: [RoleGuard],
 *   data: {
 *     profils: ['DII'],
 *     permissions: ['WRITE'],
 *     permissionCheckMethod: 'all'
 *   }
 */
@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {

  constructor(
    private router: Router,
    private authService: AuthService,
    private session: SessionContext
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {

    // 1. Authentification obligatoire
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
      return false;
    }

    // 2. Vérification du profil (si défini sur la route)
    const requiredProfils = route.data['profils'] as string[] | undefined;
    if (requiredProfils && requiredProfils.length > 0) {
      const currentProfil = this.session.profilType;
      const profilAutorise = requiredProfils.some(
        p => p.toUpperCase() === currentProfil?.toString()
      );
      if (!profilAutorise) {
        console.warn(`[RoleGuard] Profil ${currentProfil} non autorisé pour ${state.url}`);
        this.router.navigate(['/unauthorized']);
        return false;
      }
    }

    // 3. Vérification des permissions (existant — inchangé)
    const requiredPermissions = route.data['permissions'] as string[] | undefined;
    if (!requiredPermissions || requiredPermissions.length === 0) {
      return true;
    }

    const checkMethod = route.data['permissionCheckMethod'] || 'any';
    const permitted = checkMethod === 'all'
      ? this.authService.hasAllPermissions(requiredPermissions)
      : this.authService.hasAnyPermission(requiredPermissions);

    if (!permitted) {
      console.warn(`[RoleGuard] Permissions insuffisantes pour ${state.url}`);
      this.router.navigate(['/unauthorized']);
      return false;
    }

    return true;
  }
}