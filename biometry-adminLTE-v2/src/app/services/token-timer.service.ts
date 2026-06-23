import { Injectable, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, interval, Subscription } from 'rxjs';
import { AuthService } from '../auth/auth.service';

export interface TokenStatus {
  secondesRestantes: number;   // -1 si pas de token
  label: string;               // "1h 23m 45s"
  niveau: 'ok' | 'warning' | 'danger' | 'expire';
  // ok = >30min, warning = 10-30min, danger = <10min, expire = 0
}

@Injectable({ providedIn: 'root' })
export class TokenTimerService implements OnDestroy {

  private readonly SEUIL_WARNING = 30 * 60;  // 30 minutes
  private readonly SEUIL_DANGER  = 10 * 60;  // 10 minutes

  private status$ = new BehaviorSubject<TokenStatus>({ secondesRestantes: -1, label: '--', niveau: 'ok' });
  readonly tokenStatus$ = this.status$.asObservable();

  private tick$!: Subscription;
  private dejaDeconnecte = false;

  constructor(private authService: AuthService, private router: Router) {
    // Premier calcul après injection
    this.status$.next(this.calculerStatus());

    // Tick toutes les secondes
    this.tick$ = interval(1000).subscribe(() => {
      const status = this.calculerStatus();
      this.status$.next(status);

      if (status.niveau === 'expire' && !this.dejaDeconnecte) {
        this.dejaDeconnecte = true;
        this.authService.logout().subscribe(() => {
          this.router.navigate(['/login'], {
            queryParams: { raison: 'session_expiree' }
          });
        });
      }
    });
  }

  /** Lit et décode le JWT pour extraire `exp` */
  getExpiry(): number | null {
    const token = this.authService.getToken();
    if (!token) return null;
    try {
      const payload = token.split('.')[1];
      const decoded = JSON.parse(atob(payload.replace(/-/g, '+').replace(/_/g, '/')));
      return decoded.exp ? decoded.exp * 1000 : null; // ms
    } catch {
      return null;
    }
  }

  private calculerStatus(): TokenStatus {
    const expiry = this.getExpiry();
    if (!expiry) {
      return { secondesRestantes: -1, label: '--:--', niveau: 'expire' };
    }

    const restMs = expiry - Date.now();
    const restSec = Math.floor(restMs / 1000);

    if (restSec <= 0) {
      return { secondesRestantes: 0, label: '00:00:00', niveau: 'expire' };
    }

    const h  = Math.floor(restSec / 3600);
    const m  = Math.floor((restSec % 3600) / 60);
    const s  = restSec % 60;
    const label = h > 0
      ? `${h}h ${String(m).padStart(2,'0')}m ${String(s).padStart(2,'0')}s`
      : `${String(m).padStart(2,'0')}m ${String(s).padStart(2,'0')}s`;

    let niveau: TokenStatus['niveau'] = 'ok';
    if (restSec <= this.SEUIL_DANGER)  niveau = 'danger';
    else if (restSec <= this.SEUIL_WARNING) niveau = 'warning';

    return { secondesRestantes: restSec, label, niveau };
  }

  /** Réinitialise le flag déconnexion (appelé après login/refresh) */
  reset(): void {
    this.dejaDeconnecte = false;
    this.status$.next(this.calculerStatus());
  }

  ngOnDestroy(): void {
    this.tick$.unsubscribe();
  }
}
