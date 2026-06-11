import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit, OnDestroy {
  currentUser: any = null;
  loggingOut = false;
  private destroy$ = new Subject<void>();

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Souscrire avec unsubscribe automatique
    this.authService.currentUser$
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => {
        this.currentUser = user;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  logout(event: Event): void {
    event.preventDefault();
    event.stopPropagation(); // Empêcher la propagation de l'événement
    
    if (this.loggingOut) {
      return;
    }

    if (confirm('Êtes-vous sûr de vouloir vous déconnecter ?')) {
      this.loggingOut = true;
      
      this.authService.logout()
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (response) => {
            console.log('Déconnexion réussie:', response);
            // Navigation avec replaceUrl pour éviter de revenir en arrière
            this.router.navigate(['/login'], { replaceUrl: true });
          },
          error: (error) => {
            console.error('Erreur lors de la déconnexion:', error);
            // Forcer la navigation même en cas d'erreur
            this.router.navigate(['/login'], { replaceUrl: true });
          },
          complete: () => {
            this.loggingOut = false;
          }
        });
    }
  }
}