import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot
} from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class OtpGuard implements CanActivate {

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private router: Router
  ) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | boolean {
    //let passage_opt = 0;
    const otp = route.queryParamMap.get('otp');

    console.log("opt", otp);
    // ── Pas d'OTP → vérifier si déjà connecté normalement ──
    if (!otp) {
      //passage_opt=-1;
      console.log("opt", otp);
      if (this.authService.isLoggedIn()) {
        return true;
      }
      if (!localStorage.getItem('esphere_token') || !localStorage.getItem('esphere_user')) {
        this.router.navigate(['/login']);
        return false;
      }
     
      //passage_opt=1;
      return true;

    }

    // ── OTP présent → valider auprès du backend ─────────────
    return this.http.post<any>(
      '/auth/validate-otp',
      { otp }
    ).pipe(
      map(response => {
        // Stocker la session comme un login normal
        localStorage.setItem('esphere_token',
          response.token);
        localStorage.setItem('esphere_user',
          JSON.stringify(response));

        // Stocker les infos de visite pour pré-remplir
        if (response.codeVisite) {
          sessionStorage.setItem('visite_code',
            response.codeVisite);
          sessionStorage.setItem('visite_annee',
            response.annee);
          sessionStorage.setItem('visite_nature',
            response.naturePrestation);
          sessionStorage.setItem('visite_prestataire',
            response.prestataireId);
        }

        // Nettoyer l'OTP de l'URL après validation
        const urlSansOtp = state.url.split('?')[0];
        this.router.navigate([urlSansOtp],
          { replaceUrl: true });
        //passage_opt=1;
        return true;
      }),
      catchError(err => {
        console.error('OTP invalide ou expiré', err);
        this.router.navigate(['/login']);
        return of(false);
      })
    );
  }
}