import { ApplicationConfig, ErrorHandler, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeng/themes/aura';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { MessageService } from 'primeng/api';

import { routes } from './app.routes';
import { jwtInterceptor } from './core/interceptors/jwtInterceptor';


export class GlobalErrorHandler implements ErrorHandler {
  handleError(error: any): void {
    console.error('[GlobalErrorHandler]', error);
    if (error?.rejection) {
      console.error('Promise rejection:', error.rejection);
    }
  }
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    MessageService,

    // withInterceptors([jwtInterceptor]) : attache le token JWT sur chaque requête HTTP
    // withFetch() : utilise l'API Fetch native (requis Angular 18)
    provideHttpClient(
      withFetch(),
      withInterceptors([jwtInterceptor])
    ),

    provideAnimationsAsync(),
    providePrimeNG({
      theme: {
        preset: Aura,
        options: {
          prefix: 'p',
          darkModeSelector: 'system',
          cssLayer: false
        }
      },
      ripple: true,
    })
  ]
};