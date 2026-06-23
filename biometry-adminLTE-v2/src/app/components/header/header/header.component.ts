import { Component, ElementRef, HostListener, OnDestroy, OnInit, Renderer2 } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { Subject, interval } from 'rxjs';
import { takeUntil, switchMap, startWith, catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { AuthService } from '../../../auth/auth.service';
import { User } from '../../../models/user';
import { TokenTimerService, TokenStatus } from '../../../services/token-timer.service';
import { DashboardService } from '../../../services/dashboard.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule, TranslateModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {

  user: User | null = null;
  consultationCount = 0;
  ordonnanceCount = 0;
  examenCount = 0;
  tokenStatus: TokenStatus = { secondesRestantes: -1, label: '--', niveau: 'ok' };
  showBanniereExpiration = false;

  dropdowns: { [key: string]: boolean } = {
    consultations: false,
    ordonnances: false,
    examens: false,
    user: false
  };

  private destroy$ = new Subject<void>();

  // Intervalle de rafraîchissement : 2 minutes
  private readonly REFRESH_MS = 120_000;

  constructor(
    private renderer: Renderer2,
    private router: Router,
    private elementRef: ElementRef,
    private authService: AuthService,
    private tokenTimer: TokenTimerService,
    private dashboardService: DashboardService
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getStoredUser();

    this.tokenTimer.tokenStatus$
      .pipe(takeUntil(this.destroy$))
      .subscribe(status => {
        this.tokenStatus = status;
        this.showBanniereExpiration = status.niveau === 'warning' || status.niveau === 'danger';
      });

    this.startPolling();
  }

  private startPolling(): void {
    if (!this.user) return;
    const profil = this.user.profilCode || '';
    if (profil !== 'SERVICE_SANTE' && profil !== 'SUP_ADMIN') return;

    interval(this.REFRESH_MS).pipe(
      startWith(0),
      takeUntil(this.destroy$),
      switchMap(() =>
        this.dashboardService.getDashboardSS(this.user!.utilisateurId, new Date().getFullYear())
          .pipe(catchError(() => of(null)))
      )
    ).subscribe(data => {
      if (data) {
        this.consultationCount = data.consultationsEnAttente || 0;
        this.ordonnanceCount   = data.ordonnancesEnAttente   || 0;
        this.examenCount       = data.examensEnAttente       || 0;
      }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get timerClass(): string {
    switch (this.tokenStatus.niveau) {
      case 'danger':  return 'token-timer danger clignotant';
      case 'warning': return 'token-timer warning';
      default:        return 'token-timer ok';
    }
  }

  get banniereClass(): string {
    return this.tokenStatus.niveau === 'danger' ? 'banniere-session danger' : 'banniere-session warning';
  }

  getInitiales(): string {
    if (!this.user) return '?';
    const p = this.user.prenom?.charAt(0) || '';
    const n = this.user.nom?.charAt(0) || '';
    return (p + n).toUpperCase() || this.user.login?.substring(0, 2).toUpperCase() || '?';
  }

  toggleSidebar(event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    const body = document.body;
    if (window.innerWidth < 768) {
      body.classList.toggle('sidebar-open');
    } else {
      body.classList.toggle('sidebar-collapse');
    }
  }

  toggleDropdown(dropdown: string, event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    Object.keys(this.dropdowns).forEach(k => {
      if (k !== dropdown) this.dropdowns[k] = false;
    });
    this.dropdowns[dropdown] = !this.dropdowns[dropdown];
  }

  closeAllDropdowns(): void {
    Object.keys(this.dropdowns).forEach(k => this.dropdowns[k] = false);
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.closeAllDropdowns();
    }
  }

  logout(): void {
    this.authService.logout().subscribe(() => {
      this.router.navigate(['/login']);
    });
  }

  goToConsultations(): void { this.closeAllDropdowns(); this.router.navigate(['/public/admin/consultation']); }
  goToOrdonnances():  void { this.closeAllDropdowns(); this.router.navigate(['/public/admin/ordonnance']); }
  goToExamens():      void { this.closeAllDropdowns(); this.router.navigate(['/public/admin/examen']); }
  toggleControlSidebar(): void { document.body.classList.toggle('control-sidebar-open'); }
}
