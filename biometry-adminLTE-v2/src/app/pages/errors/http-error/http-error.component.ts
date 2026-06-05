import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Location, CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

interface ErrorDetails {
  url?: string;
  timestamp?: Date;
  message?: string;
}

@Component({
  selector: 'app-http-error',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl: './http-error.component.html',
  styleUrls: ['./http-error.component.css']
})
export class HttpErrorComponent implements OnInit {
  currentYear = new Date().getFullYear();
  errorCode: number = 404;
  errorDetails?: ErrorDetails;
  showDetails = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private location: Location
  ) {
    // Récupérer le code d'erreur depuis l'état de navigation
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras?.state) {
      this.errorCode = navigation.extras.state['errorCode'] || 404;
      this.errorDetails = navigation.extras.state['errorDetails'];
    }
  }

  ngOnInit(): void {
    // Récupérer le code d'erreur depuis les paramètres de route
    this.route.queryParams.subscribe(params => {
      if (params['code']) {
        this.errorCode = parseInt(params['code'], 10);
      }
    });

    // Définir les détails par défaut si non fournis
    if (!this.errorDetails) {
      this.errorDetails = {
        url: this.router.url,
        timestamp: new Date()
      };
    }
  }

  getErrorClass(): string {
    if (this.errorCode >= 400 && this.errorCode < 500) {
      return 'client-error';
    } else if (this.errorCode >= 500) {
      return 'server-error';
    }
    return 'not-found';
  }

  getIconClass(): string {
    switch (this.errorCode) {
      case 400:
        return 'fa-exclamation-triangle';
      case 401:
        return 'fa-lock';
      case 403:
        return 'fa-ban';
      case 404:
        return 'fa-search';
      case 408:
        return 'fa-clock-o';
      case 429:
        return 'fa-hand-paper-o';
      case 500:
        return 'fa-wrench';
      case 502:
        return 'fa-plug';
      case 503:
        return 'fa-cog';
      case 504:
        return 'fa-hourglass-end';
      default:
        return 'fa-question-circle';
    }
  }

  getErrorTitle(): string {
    const titles: { [key: number]: string } = {
      400: 'error400Title',
      401: 'error401Title',
      403: 'error403Title',
      404: 'error404Title',
      408: 'error408Title',
      429: 'error429Title',
      500: 'error500Title',
      502: 'error502Title',
      503: 'error503Title',
      504: 'error504Title'
    };
    return titles[this.errorCode] || 'errorGenericTitle';
  }

  getErrorMessage(): string {
    const messages: { [key: number]: string } = {
      400: 'error400Message',
      401: 'error401Message',
      403: 'error403Message',
      404: 'error404Message',
      408: 'error408Message',
      429: 'error429Message',
      500: 'error500Message',
      502: 'error502Message',
      503: 'error503Message',
      504: 'error504Message'
    };
    return messages[this.errorCode] || 'errorGenericMessage';
  }

  getSuggestions(): string[] {
    const suggestions: { [key: number]: string[] } = {
      400: ['suggestion400_1', 'suggestion400_2', 'suggestion400_3'],
      401: ['suggestion401_1', 'suggestion401_2', 'suggestion401_3'],
      403: ['suggestion403_1', 'suggestion403_2', 'suggestion403_3'],
      404: ['suggestion404_1', 'suggestion404_2', 'suggestion404_3', 'suggestion404_4'],
      408: ['suggestion408_1', 'suggestion408_2', 'suggestion408_3'],
      429: ['suggestion429_1', 'suggestion429_2', 'suggestion429_3'],
      500: ['suggestion500_1', 'suggestion500_2', 'suggestion500_3'],
      502: ['suggestion502_1', 'suggestion502_2', 'suggestion502_3'],
      503: ['suggestion503_1', 'suggestion503_2', 'suggestion503_3'],
      504: ['suggestion504_1', 'suggestion504_2', 'suggestion504_3']
    };
    return suggestions[this.errorCode] || ['suggestionGeneric_1', 'suggestionGeneric_2', 'suggestionGeneric_3'];
  }

  toggleDetails(): void {
    this.showDetails = !this.showDetails;
  }

  goBack(): void {
    this.location.back();
  }

  goToHome(): void {
    this.router.navigateByUrl('/dashboard');
  }

  navigateTo(route: string): void {
    this.router.navigateByUrl(route);
  }

  contactSupport(): void {
    // Rediriger vers la page de support ou ouvrir un modal
    this.router.navigateByUrl('/support');
  }
}