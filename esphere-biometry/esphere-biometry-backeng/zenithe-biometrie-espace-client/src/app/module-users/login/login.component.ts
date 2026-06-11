import { Component } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';
import { Users } from '../../model/Users';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { MessagesModule } from 'primeng/messages';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../auth/auth.service';
import { UserSession } from '../../model/user-session';
import { User } from '../../model/user';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ToastModule, MessagesModule, CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  userSession: any;
  user: any;
  loading = false;
  errorMessage = '';
  showPassword = false; // Pour le toggle du mot de passe
  erroMessage!: string;
  
 currentYear: number = new Date().getFullYear();

  constructor(
    public loginService: LoginService,
    private router: Router,
    private messageService: MessageService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    // Rediriger si déjà connecté
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    }
  }

  /**
   * Toggle la visibilité du mot de passe
   */
  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  /**
   * Gestion de la soumission du formulaire de connexion
   */
  onLogin(formData: any): void {
    this.errorMessage = '';
    this.loading = true;

    // Validation des champs
    if (!formData.username || !formData.password) {
      this.showError('Veuillez remplir tous les champs');
      this.loading = false;
      return;
    }

    // Validation de la longueur minimale
    if (formData.password.length < 3) {
      this.showError('Le mot de passe doit contenir au moins 3 caractères');
      this.loading = false;
      return;
    }

    console.log('Tentative de connexion avec:', formData.username);

    this.authService.login(formData.username, formData.password).subscribe({
      next: (response) => {
        console.log('Connexion réussie:', response);
        this.showSuccess('Connexion réussie ! Redirection en cours...');
        
        // Redirection après un court délai pour laisser l'utilisateur voir le message
        setTimeout(() => {
          this.router.navigate(['/dashboard']);
        }, 1000);
      },
      error: (error) => {
        console.error('Erreur de connexion:', error);
        this.loading = false;
        
        // Gestion des différents types d'erreurs
        let errorMsg = 'Erreur de connexion';
        
        if (error.message) {
          errorMsg = error.message;
        } else if (error.error?.message) {
          errorMsg = error.error.message;
        } else if (error.status === 401) {
          errorMsg = 'Login ou mot de passe incorrect';
        } else if (error.status === 403) {
          errorMsg = 'Accès refusé. Contactez l\'administrateur';
        } else if (error.status === 0) {
          errorMsg = 'Impossible de contacter le serveur. Vérifiez votre connexion';
        } else if (error.status >= 500) {
          errorMsg = 'Erreur serveur. Veuillez réessayer plus tard';
        }
        
        this.showError(errorMsg);
        this.errorMessage = errorMsg;
      },
      complete: () => {
        console.log('Processus de connexion terminé');
      }
    });
  }

  /**
   * Affiche un message d'erreur via PrimeNG Toast
   */
  private showError(message: string): void {
    this.messageService.add({
      severity: 'error',
      summary: 'Erreur de connexion',
      detail: message,
      life: 5000,
      styleClass: 'custom-toast-error'
    });
  }

  /**
   * Affiche un message de succès via PrimeNG Toast
   */
  private showSuccess(message: string): void {
    this.messageService.add({
      severity: 'success',
      summary: 'Succès',
      detail: message,
      life: 3000,
      styleClass: 'custom-toast-success'
    });
  }

  /**
   * Affiche un message d'information
   */
  private showInfo(message: string): void {
    this.messageService.add({
      severity: 'info',
      summary: 'Information',
      detail: message,
      life: 4000
    });
  }

  /**
   * Nettoie les messages d'erreur
   */
  clearErrorMessage(): void {
    this.errorMessage = '';
  }
}