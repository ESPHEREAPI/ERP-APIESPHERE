// login.component.ts
import { Component, OnInit } from '@angular/core';

import { Router, RouterLink } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { MessagesModule } from 'primeng/messages';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LoginService } from '../../../core/services/login.service';
import { AuthService } from '../../../core/auth/auth.service';
import { ProfilType } from '../../../shared/enums/ProfilType';
import { UserSession } from '../../../core/models/user-session';


// =============================================
// ENUM des profils de connexion
// =============================================


interface ProfilOption {
  value: ProfilType;
  label: string;
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ToastModule, MessagesModule, CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  readonly ProfilType = ProfilType;

  selectedProfil: ProfilType | '' = '';

  readonly profilList: ProfilOption[] = [
    { value: ProfilType.SOUSCRIPTEUR,  label: 'Souscripteur'                            },
    { value: ProfilType.ADHERENT,      label: 'Adhérent'                                },
    { value: ProfilType.SERVICE_SANTE, label: 'Service Santé'                           },
    { value: ProfilType.DII,           label: 'DII – Division Informatique & Innovation'},
    { value: ProfilType.INTERMEDIAIRE, label: 'Intermédiaire'                           },
    { value: ProfilType.PRESTATAIRE,   label: 'Prestataire'                             },
  ];

  loading      = false;
  errorMessage = '';
  showPassword = false;
  currentYear: number = new Date().getFullYear();

  constructor(
    public  loginService:   LoginService,
    private router:         Router,
    private messageService: MessageService,
    private authService:    AuthService
  ) {}

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    }
  }

  onProfilChange(value: string): void {
    this.selectedProfil = value as ProfilType;
    this.errorMessage   = '';
  }

  isLoginFormEnabled(): boolean {
    return [
      ProfilType.SOUSCRIPTEUR,
      ProfilType.ADHERENT,
      ProfilType.SERVICE_SANTE,
      ProfilType.DII,
    ].includes(this.selectedProfil as ProfilType);
  }

  /**
   * Affiche "Mot de passe oublié" pour Souscripteur, Service Santé et DII.
   * Adhérent affiche "Contactez-nous" à la place.
   */
  showForgotPassword(): boolean {
    return [
      ProfilType.SOUSCRIPTEUR,
      ProfilType.SERVICE_SANTE,
      ProfilType.DII,
    ].includes(this.selectedProfil as ProfilType);
  }

  getUsernamePlaceholder(): string {
    switch (this.selectedProfil) {
      case ProfilType.SOUSCRIPTEUR:  return 'Numéro de police maladie';
      case ProfilType.ADHERENT:      return 'Numéro de téléphone d\'enrôlement';
      case ProfilType.SERVICE_SANTE: return 'Identifiant biométrique';
      case ProfilType.DII:           return 'Identifiant biométrique DII';
      default:                       return 'Identifiant';
    }
  }

  getPasswordPlaceholder(): string {
    switch (this.selectedProfil) {
      case ProfilType.SOUSCRIPTEUR:  return 'Mot de passe';
      case ProfilType.ADHERENT:      return 'Code adhérent';
      case ProfilType.SERVICE_SANTE: return 'Code biométrique';
      case ProfilType.DII:           return 'Code biométrique DII';
      default:                       return 'Mot de passe';
    }
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  onLogin(formData: any): void {
    this.errorMessage = '';
    this.loading      = true;

    if (!formData.profil) {
      this.showError('Veuillez sélectionner un profil de connexion');
      this.loading = false;
      return;
    }

    if (!formData.username || !formData.password) {
      this.showError('Veuillez remplir tous les champs');
      this.loading = false;
      return;
    }

    if (formData.password.length < 3) {
      this.showError('Le mot de passe doit contenir au moins 3 caractères');
      this.loading = false;
      return;
    }

    this.authService.login(formData.username, formData.password,formData.profil).subscribe({
      next: (data:UserSession) => {
        this.showSuccess('Connexion réussie ! Redirection en cours...');
        //connection on verifi le type de profile si c est adherent on lui redirige vers son profil   this.router.navigate(['/adherents', codeAdherent, 'edit']);
       if(this.selectedProfil===ProfilType.ADHERENT){
        setTimeout(() => this.router.navigate(['/adherents', data.usersDTO.codeAdherent, 'edit']), 1000);
       }
        setTimeout(() => this.router.navigate(['/dashboard']), 1000);
      },
      error: (error) => {
        this.loading = false;
        let errorMsg = 'Erreur de connexion';
        if (error.message)              errorMsg = error.message;
        else if (error.error?.message)  errorMsg = error.error.message;
        else if (error.status === 401)  errorMsg = 'Identifiant ou mot de passe incorrect';
        else if (error.status === 403)  errorMsg = 'Accès refusé. Contactez l\'administrateur';
        else if (error.status === 0)    errorMsg = 'Impossible de contacter le serveur';
        else if (error.status >= 500)   errorMsg = 'Erreur serveur. Réessayez plus tard';
        this.showError(errorMsg);
        this.errorMessage = errorMsg;
      }
    });
  }

  private showError(message: string): void {
    this.messageService.add({ severity: 'error', summary: 'Erreur', detail: message, life: 5000 });
  }

  private showSuccess(message: string): void {
    this.messageService.add({ severity: 'success', summary: 'Succès', detail: message, life: 3000 });
  }

  clearErrorMessage(): void {
    this.errorMessage = '';
  }
}