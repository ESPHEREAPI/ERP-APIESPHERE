// src/app/components/header/header.component.ts
import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from '../../core/auth/auth.service';
import { UserSession } from '../../core/model/user-session';
import { ProfilType } from '../enum/ProfilType';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit, OnDestroy {

  /** Données de session affichées dans la pill */
  session: { agencyName: string; agencyCode: string; companyName: string;nomcomplet:string } | null = null;

  /** Initiales de l'avatar (calculées depuis le nom d'utilisateur) */
  userInitials = 'AD';

  /** Nom complet affiché dans le menu profil */
  userName = 'Administrateur';

  /** Rôle/email affiché sous le nom */
  userRole = '';

  private sub?: Subscription;

  constructor(
    private router: Router,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    // Écouter les changements de session en temps réel
    this.sub = this.authService.currentUser$.subscribe((user: UserSession | null) => {
      if (user) {
        this.session = {
          agencyName: user.agencyName  ?? 'Bureau Direct Siège',
          agencyCode: user.agencyCode  ?? '1000',
          companyName: user.companyName ?? 'ZENITHE INSURANCE',
          nomcomplet: user.userDTO.nomcomplet ?? 'No Comment',
        };
        this.userName     = user.userDTO.nomcomplet   ?? 'Utilisateur';
        this.userRole     = user.userDTO.email      ?? '';
        this.userInitials = this.buildInitials(this.userName);
      } else {
        // Fallback statique (développement / avant login)
        this.session = {
          agencyName: 'Bureau Direct Siège',
          agencyCode: '1000',
          companyName: 'ZENITHE INSURANCE',
           nomcomplet:  'No Comment',
        };
        this.userInitials = 'AD';
      }
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  // ── Actions ────────────────────────────────────────────────────────────────

  onLogout(): void {
    console.log('🚪 Déconnexion déclenchée');

    const currentUser = this.authService.currentUserValue;
    const profilType  = currentUser?.profilType ?? null;

    this.authService.clearAuthData();

    if (profilType === ProfilType.PAYLOAD) {
      this.router.navigateByUrl('/login-payload');
    } else {
      this.router.navigateByUrl('/login');
    }
  }

  // ── Helpers ────────────────────────────────────────────────────────────────

  /** Génère 2 initiales depuis un nom complet (ex: "Marie Koné" → "MK") */
  private buildInitials(name: string): string {
    const parts = name.trim().split(/\s+/);
    if (parts.length >= 2) {
      return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    }
    return name.slice(0, 2).toUpperCase();
  }

 getInitials(name: string): string {
  if (!name) return '';
  return name
    .split(' ')
    .map(word => word.charAt(0).toUpperCase())
    .slice(0, 2)
    .join('');
}

getAvatarColor(name: string): string {
  if (!name) return '#34495e'; // couleur par défaut
  const colors = [
    '#e74c3c', '#3498db', '#2ecc71', '#f39c12',
    '#9b59b6', '#1abc9c', '#e67e22', '#34495e'
  ];
  let hash = 0;
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash);
  }
  return colors[Math.abs(hash) % colors.length];
}
}