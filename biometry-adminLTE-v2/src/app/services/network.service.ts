import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, fromEvent, map, merge, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NetworkService {

   private onlineStatus$ = new BehaviorSubject<boolean>(navigator.onLine);
  
  constructor(private router: Router) {
    this.initNetworkMonitoring();
  }

  /**
   * Initialise la surveillance de l'état de la connexion réseau
   */
  private initNetworkMonitoring(): void {
    // Écouter les événements online/offline du navigateur
    merge(
      fromEvent(window, 'online').pipe(map(() => true)),
      fromEvent(window, 'offline').pipe(map(() => false))
    ).subscribe(status => {
      console.log('Network status changed:', status ? 'online' : 'offline');
      this.onlineStatus$.next(status);
      
      // Si la connexion est perdue, rediriger vers la page d'erreur réseau
      if (!status) {
        const currentUrl = this.router.url;
        // Ne rediriger que si on n'est pas déjà sur une page d'erreur
        if (!this.shouldSkipRedirection(currentUrl)) {
          this.router.navigate(['/network-error'], {
            state: { previousUrl: currentUrl }
          });
        }
      }
    });
  }
   /**
   * Vérifie si on doit ignorer la redirection pour certaines pages
   */
  private shouldSkipRedirection(url: string): boolean {
    const excludedRoutes = [
      '/network-error',
      '/error',
      '/login',
      '/forgot-password'
    ];
    
    return excludedRoutes.some(route => url.includes(route));
  }

  /**
   * Retourne un Observable de l'état de la connexion
   */
  get isOnline$(): Observable<boolean> {
    return this.onlineStatus$.asObservable();
  }

  /**
   * Retourne l'état actuel de la connexion
   */
  get isOnline(): boolean {
    return this.onlineStatus$.value;
  }

  /**
   * Vérifie activement la connexion en effectuant une requête
   */
  async checkConnection(): Promise<boolean> {
    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 5000);

      const response = await fetch('https://www.google.com/favicon.ico', {
        method: 'HEAD',
        mode: 'no-cors',
        signal: controller.signal
      });

      clearTimeout(timeoutId);
      const isOnline = true;
      this.onlineStatus$.next(isOnline);
      return isOnline;
    } catch (error) {
      const isOnline = false;
      this.onlineStatus$.next(isOnline);
      return isOnline;
    }
  }
}
