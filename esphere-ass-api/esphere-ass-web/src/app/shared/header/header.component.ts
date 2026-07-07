// src/app/components/header/header.component.ts
import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from '../../core/auth/auth.service';
import { UserSession } from '../../core/model/user-session';
import { ProfilType } from '../enum/ProfilType';

const WARNING_THRESHOLD_MS = 10 * 60 * 1000; // 10 minutes

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit, OnDestroy {

  session: { agencyName: string; agencyCode: string; companyName: string; nomcomplet: string } | null = null;
  userInitials = 'AD';
  userName = 'Administrateur';
  userRole = '';

  // ── Token timer ────────────────────────────────────────────────
  tokenTimeLeft   = '';
  tokenWarning    = false;
  tokenExpired    = false;
  isRefreshing    = false;
  showExpiredAlert = false;

  private sub?: Subscription;
  private timerInterval?: ReturnType<typeof setInterval>;

  constructor(
    private router: Router,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.sub = this.authService.currentUser$.subscribe((user: UserSession | null) => {
      if (user) {
        this.session = {
          agencyName:  user.agencyName   ?? 'Bureau Direct Siège',
          agencyCode:  user.agencyCode   ?? '1000',
          companyName: user.companyName  ?? 'ZENITHE INSURANCE',
          nomcomplet:  user.userDTO.nomcomplet ?? 'No Comment',
        };
        this.userName     = user.userDTO.nomcomplet ?? 'Utilisateur';
        this.userRole     = user.userDTO.email      ?? '';
        this.userInitials = this.buildInitials(this.userName);
        this.startTokenTimer();
      } else {
        this.session      = { agencyName: 'Bureau Direct Siège', agencyCode: '1000', companyName: 'ZENITHE INSURANCE', nomcomplet: 'No Comment' };
        this.userInitials = 'AD';
        this.stopTokenTimer();
      }
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
    this.stopTokenTimer();
  }

  // ── Timer ──────────────────────────────────────────────────────

  private startTokenTimer(): void {
    this.stopTokenTimer();
    this.updateTimer();
    this.timerInterval = setInterval(() => this.updateTimer(), 1000);
  }

  private stopTokenTimer(): void {
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
      this.timerInterval = undefined;
    }
    this.tokenTimeLeft = '';
    this.tokenWarning  = false;
    this.tokenExpired  = false;
  }

  private updateTimer(): void {
    const expiresAt = this.authService.currentUserValue?.expiresAt;
    if (!expiresAt) { this.stopTokenTimer(); return; }

    const remaining = new Date(expiresAt).getTime() - Date.now();

    if (remaining <= 0) {
      this.tokenExpired    = true;
      this.tokenWarning    = false;
      this.tokenTimeLeft   = '00:00';
      this.showExpiredAlert = true;
      this.stopTokenTimer();
      // Déconnexion automatique après 3 secondes pour laisser l'alerte visible
      setTimeout(() => this.forceLogout(), 3000);
      return;
    }

    this.tokenExpired  = false;
    this.tokenWarning  = remaining < WARNING_THRESHOLD_MS;

    const totalSec = Math.floor(remaining / 1000);
    const h = Math.floor(totalSec / 3600);
    const m = Math.floor((totalSec % 3600) / 60);
    const s = totalSec % 60;

    this.tokenTimeLeft = h > 0
      ? `${this.pad(h)}:${this.pad(m)}:${this.pad(s)}`
      : `${this.pad(m)}:${this.pad(s)}`;
  }

  private pad(n: number): string {
    return n.toString().padStart(2, '0');
  }

  // ── Actions ────────────────────────────────────────────────────

  refreshToken(): void {
    if (this.isRefreshing) return;
    this.isRefreshing = true;
    this.authService.refreshToken().subscribe({
      next: () => {
        this.isRefreshing = false;
        this.startTokenTimer();
      },
      error: () => {
        this.isRefreshing = false;
        this.forceLogout();
      }
    });
  }

  private forceLogout(): void {
    const profilType = this.authService.currentUserValue?.profilType ?? null;
    this.authService.clearAuthData();
    if (profilType === ProfilType.PAYLOAD) {
      this.router.navigateByUrl('/login-payload');
    } else {
      this.router.navigateByUrl('/login');
    }
  }

  onLogout(): void {
    const currentUser = this.authService.currentUserValue;
    const profilType  = currentUser?.profilType ?? null;
    this.authService.clearAuthData();
    if (profilType === ProfilType.PAYLOAD) {
      this.router.navigateByUrl('/login-payload');
    } else {
      this.router.navigateByUrl('/login');
    }
  }

  // ── Helpers ────────────────────────────────────────────────────

  private buildInitials(name: string): string {
    const parts = name.trim().split(/\s+/);
    if (parts.length >= 2) return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    return name.slice(0, 2).toUpperCase();
  }

  getInitials(name: string): string {
    if (!name) return '';
    return name.split(' ').map(w => w.charAt(0).toUpperCase()).slice(0, 2).join('');
  }

  getAvatarColor(name: string): string {
    if (!name) return '#34495e';
    const colors = ['#e74c3c','#3498db','#2ecc71','#f39c12','#9b59b6','#1abc9c','#e67e22','#34495e'];
    let hash = 0;
    for (let i = 0; i < name.length; i++) hash = name.charCodeAt(i) + ((hash << 5) - hash);
    return colors[Math.abs(hash) % colors.length];
  }
}