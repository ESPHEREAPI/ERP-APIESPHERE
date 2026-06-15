import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { catchError, map, Observable, throwError } from 'rxjs';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ModulePermissionService {

  private readonly API_URL = '/api/permissions';

  constructor(private http: HttpClient, private authService: AuthService) { }

  hasModuleAccess(moduleCode: string): boolean {
    const user = this.authService.currentUserValue?.userDTO;
    if (!user) return false;

    // For system users, grant all access
    //if (user.typeUtilisateur === 'UTILISATEUR_SYSTEM') {
     // return true;
    //}

    // Check user permissions from localStorage or session
    const permissions = this.getUserPermissions();
    return permissions.includes(moduleCode);
  }

  private getUserPermissions(): string[] {
    // In real implementation, this would come from the server
    // For now, return stored permissions or default set
    const storedPermissions = localStorage.getItem('userPermissions');
    if (storedPermissions) {
      return JSON.parse(storedPermissions);
    }

    // Default permissions for demo
    return ['admin', 'settings', 'reporting'];
  }

  loadUserPermissions(): Observable<string[]> {
    const user = this.authService.currentUserValue?.userDTO;
    if (!user) return throwError(() => new Error('No user logged in'));

    return this.http.get<string[]>(`${this.API_URL}/user/${user.username}/modules`)
      .pipe(
        map((permissions: string[]) => {
          localStorage.setItem('userPermissions', JSON.stringify(permissions));
          return permissions;
        }),
        catchError(error => {
          console.error('Error loading permissions:', error);
          return throwError(() => error);
        })
      );
  }
}

// password-strength.util.ts
export class PasswordStrengthUtil {
  static calculateStrength(password: string): number {
    if (!password) return 0;

    let score = 0;

    // Length check
    if (password.length >= 8) score += 25;
    if (password.length >= 12) score += 25;

    // Character variety checks
    if (/[a-z]/.test(password)) score += 10;
    if (/[A-Z]/.test(password)) score += 10;
    if (/[0-9]/.test(password)) score += 10;
    if (/[^A-Za-z0-9]/.test(password)) score += 20;

    return Math.min(score, 100);
  }

  static getStrengthText(score: number): string {
    if (score < 30) return 'Très faible';
    if (score < 50) return 'Faible';
    if (score < 70) return 'Moyen';
    if (score < 90) return 'Fort';
    return 'Très fort';
  }
}
