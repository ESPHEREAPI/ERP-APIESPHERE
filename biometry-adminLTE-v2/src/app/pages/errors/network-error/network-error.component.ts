
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-network-error',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl: './network-error.component.html',
  styleUrls: ['./network-error.component.css']
})
export class NetworkErrorComponent implements OnInit, OnDestroy {
  currentYear = new Date().getFullYear();
  countdown = 30;
  isChecking = false;
  isConnected = false;
  progressOffset = 0;
  
  private countdownSubscription?: Subscription;
  private previousUrl: string = '/dashboard';
  private readonly COUNTDOWN_DURATION = 60;
  private readonly CIRCLE_CIRCUMFERENCE = 283; // 2 * π * 45

  constructor(private router: Router) {
    // Récupérer l'URL précédente depuis l'état de navigation
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras?.state) {
      this.previousUrl = navigation.extras.state['previousUrl'] || '/dashboard';
    }
  }

  ngOnInit(): void {
    this.startCountdown();
    this.checkConnectionPeriodically();
  }

  ngOnDestroy(): void {
    this.countdownSubscription?.unsubscribe();
  }

  private startCountdown(): void {
    this.countdown = this.COUNTDOWN_DURATION;
    this.updateProgress();

    this.countdownSubscription = interval(1000).subscribe(() => {
      this.countdown--;
      this.updateProgress();

      if (this.countdown <= 0) {
        this.countdownSubscription?.unsubscribe();
        this.retryConnection();
      }
    });
  }

  private updateProgress(): void {
    const progress = this.countdown / this.COUNTDOWN_DURATION;
    this.progressOffset = this.CIRCLE_CIRCUMFERENCE * (1 - progress);
  }

  private checkConnectionPeriodically(): void {
    // Vérifier la connexion toutes les 5 secondes
    interval(5000).subscribe(() => {
      if (!this.isChecking) {
        this.checkConnection();
      }
    });
  }

  private async checkConnection(): Promise<boolean> {
    this.isChecking = true;

    try {
      // Tenter une requête vers un endpoint fiable
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 5000);

      const response = await fetch('https://www.google.com/favicon.ico', {
        method: 'HEAD',
        mode: 'no-cors',
        signal: controller.signal
      });

      clearTimeout(timeoutId);
      this.isConnected = true;
      this.isChecking = false;

      // Si connexion restaurée, rediriger après 2 secondes
      setTimeout(() => {
        this.redirectToPreviousPage();
      }, 2000);

      return true;
    } catch (error) {
      this.isConnected = false;
      this.isChecking = false;
      return false;
    }
  }

  retryConnection(): void {
    this.isChecking = true;
    this.checkConnection().then(connected => {
      if (!connected) {
        // Réinitialiser le compte à rebours si la connexion échoue
        this.countdownSubscription?.unsubscribe();
        this.startCountdown();
      }
    });
  }

  private redirectToPreviousPage(): void {
    this.countdownSubscription?.unsubscribe();
    this.router.navigateByUrl(this.previousUrl);
  }

  goToLogin(): void {
    this.countdownSubscription?.unsubscribe();
    this.router.navigateByUrl('/login');
  }
}