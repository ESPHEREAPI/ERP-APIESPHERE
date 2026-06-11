// auth/auth.service.ts
import { Injectable }          from '@angular/core';
import { HttpClient }          from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { tap, catchError }     from 'rxjs/operators';
import { Router }              from '@angular/router';
import { environment } from '../../../environments/environment.prod';

// Modèle de réponse de connexion
interface LoginResponse {
  token:    string;
  username: string;
  role:     string;
}

@Injectable({ providedIn: 'root' })
export class AuthPayLoadService {

  private apiUrl = `${environment.apiUrl}/gateway-proxy/api/auth`;;

  constructor(
    private http:   HttpClient,
    private router: Router
  ) {}

  /**
   * Envoie les identifiants au backend Spring Boot
   * Stocke le token JWT dans localStorage si succès
   */
  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      `${this.apiUrl}/login`,
      { username, password }
    ).pipe(
      // Si succès → on stocke le token
      tap(response => {
        localStorage.setItem('token',    response.token);
        localStorage.setItem('username', response.username);
        localStorage.setItem('role',     response.role);
      }),
      // Si erreur → on transforme en message lisible
      catchError(err => {
        const msg = err.status === 401
          ? 'Identifiants incorrects. Veuillez réessayer.'
          : 'Erreur serveur. Veuillez réessayer plus tard.';
        return throwError(() => new Error(msg));
      })
    );
  }

  /**
   * Déconnexion → supprime le token et redirige vers login
   */
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    this.router.navigate(['/login']);
  }

  /**
   * Vérifie si l'utilisateur est connecté
   */
  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  /**
   * Retourne le token stocké (pour les requêtes HTTP)
   */
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUsername(): string {
    return localStorage.getItem('username') || '';
  }
}