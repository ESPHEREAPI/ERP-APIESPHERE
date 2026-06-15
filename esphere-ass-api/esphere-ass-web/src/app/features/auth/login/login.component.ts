import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { MessagesModule } from 'primeng/messages';
import { Subject, takeUntil, finalize } from 'rxjs';
import { AuthService } from '../../../core/auth/auth.service';
import { AuthResponse } from '../../../core/model/auth-response';
import { UserSession } from '../../../core/model/user-session';
import { ProfilType } from '../../../shared/enum/ProfilType';
import { TranslatePipe } from '@ngx-translate/core';
import { LanguageService, AppLang } from '../../../core/services/language.service';



interface LoginCredentials {
  username: string;
  password: string;
  remember: boolean;
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ToastModule,
    MessagesModule,
    TranslatePipe
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [MessageService]
})
export class LoginComponent implements OnInit, OnDestroy {
  
  credentials: LoginCredentials = {
    username: '',
    password: '',
    remember: false
  };
  
  isLoading = false;
  showPassword = false;
  currentYear = new Date().getFullYear();
  returnUrl = '/home';
  
  private readonly destroy$ = new Subject<void>();
  private maxLoginAttempts = 3;
  private loginAttempts = 0;
  private lockoutTime = 1 * 60 * 1000; // 5 minutes
  
  get currentLang(): AppLang { return this.languageService.current; }

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService,
    public languageService: LanguageService
  ) {
    if (this.authService.isLoggedIn()) {
      this.router.navigate([this.returnUrl]);
    }
  }

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home';
    this.checkLoginAttempts();
    this.messageService.clear();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /**
   * Soumission du formulaire - VERSION ROBUSTE
   */
  onLogin(form: NgForm): void {
    console.log('🔐 Tentative de connexion...');
    
    if (form.invalid || this.isLoading) {
      this.markFormGroupTouched(form);
      return;
    }

    if (this.isAccountLocked()) {
      this.showLockoutMessage();
      return;
    }

    this.performLogin();
  }

  private performLogin(): void {
    this.isLoading = true;
    this.messageService.clear();

    const { username, password } = this.credentials;

    console.log('📤 Envoi requête login pour:', username);
  const profilType=ProfilType.ESPHERE;
    this.authService.login(username.trim(), password,profilType)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => {
          this.isLoading = false;
          console.log('🏁 Requête login terminée');
        })
      )
      .subscribe({
        next: (response: AuthResponse<UserSession>) => {
          console.log('📥 Réponse login reçue:', response);
          
          if (response.success && response.data) {
            this.messageService.add({
              severity: 'success', 
              summary: 'Connexion réussie', 
              detail: response.message || 'Bienvenue !'
            });
            
            this.handleLoginSuccess(response.data);
          
          } else {
            // Ne devrait jamais arriver ici car géré dans le service
            this.messageService.add({
              severity: 'error', 
              summary: 'Échec de connexion', 
              detail: response.message || 'Erreur inconnue'
            });
          }
        },
        error: (error) => {
          console.error('❌ Erreur login:', error);
          
          const errorMessage = error.error?.message || 'Erreur de connexion';
          const errorCode = error.error?.errorCode;
          
          let severity: 'error' | 'warn' = 'error';
          let summary = 'Échec de connexion';
          
          // Traitement spécial pour certains codes d'erreur
          if (errorCode === 'PASSWORD_GRACE') {
            severity = 'warn';
            summary = 'Attention';
          } else if (errorCode === 'PASSWORD_EXPIRED') {
            severity = 'warn';
            summary = 'Mot de passe expiré';
          }
          
          this.messageService.add({
            severity,
            summary,
            detail: errorMessage,
            life: 5000
          });
          
          this.handleLoginError(error);
        }
      });
  }

  private handleLoginSuccess(userSession: UserSession): void {
    console.log('✅ Login success, userSession:', userSession);
    
    const user = userSession.userDTO;
    
    if (!user) {
      this.showErrorMessage('Erreur', 'Données utilisateur non valides');
      return;
    }

    // Vérifier blocage compte
    if (user.echeck_connection === true) {
      this.showErrorMessage(
        'Compte bloqué', 
        user.messageEcheck || 'Votre compte est temporairement bloqué'
      );
      this.incrementLoginAttempts();
      return;
    }

    // Succès final
    this.resetLoginAttempts();

    console.log('🚀 Navigation vers:', this.returnUrl);

    setTimeout(() => {
      this.router.navigateByUrl("/home")
    }, 1000);
  }

  private handleLoginError(error: any): void {
    this.incrementLoginAttempts();
    this.credentials.password = '';
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  private checkLoginAttempts(): void {
    const attempts = localStorage.getItem('loginAttempts');
    const lastAttempt = localStorage.getItem('lastLoginAttempt');
    
    if (attempts && lastAttempt) {
      this.loginAttempts = parseInt(attempts, 10);
      const timeSinceLastAttempt = Date.now() - parseInt(lastAttempt, 10);
      
      if (timeSinceLastAttempt > this.lockoutTime) {
        this.resetLoginAttempts();
      }
    }
  }

  private incrementLoginAttempts(): void {
    this.loginAttempts++;
    localStorage.setItem('loginAttempts', this.loginAttempts.toString());
    localStorage.setItem('lastLoginAttempt', Date.now().toString());
  }

  private resetLoginAttempts(): void {
    this.loginAttempts = 0;
    localStorage.removeItem('loginAttempts');
    localStorage.removeItem('lastLoginAttempt');
  }

  private isAccountLocked(): boolean {
    return this.loginAttempts >= this.maxLoginAttempts;
  }

  private showLockoutMessage(): void {
    const remainingTime = this.lockoutTime - (Date.now() - parseInt(localStorage.getItem('lastLoginAttempt') || '0', 10));
    const minutes = Math.ceil(remainingTime / 60000);
    
    this.showErrorMessage(
      'Compte temporairement verrouillé',
      `Trop de tentatives échouées. Réessayez dans ${minutes} minute(s).`
    );
  }

  private markFormGroupTouched(form: NgForm): void {
    Object.keys(form.controls).forEach(key => {
      form.controls[key].markAsTouched();
    });
  }

  private showSuccessMessage(summary: string, detail: string): void {
    this.messageService.add({ severity: 'success', summary, detail, life: 3000 });
  }

  private showErrorMessage(summary: string, detail: string): void {
    this.messageService.add({ severity: 'error', summary, detail, life: 5000 });
  }

  private showWarningMessage(summary: string, detail: string): void {
    this.messageService.add({ severity: 'warn', summary, detail, life: 4000 });
  }
}