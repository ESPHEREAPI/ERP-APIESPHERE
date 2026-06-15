import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/auth/auth.service';
import { ProfilType } from '../../../shared/enum/ProfilType';
import { TranslatePipe } from '@ngx-translate/core';
import { LanguageService, AppLang } from '../../../core/services/language.service';


@Component({
  selector: 'app-login-playload',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TranslatePipe],
  templateUrl: './login-playload.component.html',
  styleUrl: './login-playload.component.css'
})
export class LoginPlayloadComponent {
/// ── Formulaire réactif ────────────────────────────────────────
  loginForm!: FormGroup;
 
  // ── États UI ──────────────────────────────────────────────────
  isLoading      = false;
  showPassword   = false;
  errorMessage   = '';
  sessionExpired = false;             // Affiché si redirigé après expiration
  currentYear    = new Date().getFullYear();
 
  get currentLang(): AppLang { return this.languageService.current; }

  constructor(
    private fb:          FormBuilder,
    private authService: AuthService,
    private router:      Router,
    private route:       ActivatedRoute,
    public  languageService: LanguageService
  ) {}
 
  ngOnInit(): void {
 
    // ── Initialisation du formulaire ──────────────────────────
    this.loginForm = this.fb.group({
      // Champ identifiant : obligatoire
      username: ['', [Validators.required, Validators.minLength(2)]],
      // Champ mot de passe : obligatoire
      password: ['', [Validators.required, Validators.minLength(2)]]
    });
 
    // ── Détection session expirée (?expired=true dans l'URL) ──
    // Ajouté par l'intercepteur HTTP quand le token expire (401)
    if(this.authService.isLoggedIn()===false){
 this.sessionExpired = true;
    }
   
 
    // ── Si déjà connecté → redirige directement au dashboard ──
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard-payload']);
    }
  }
 
  // ════════════════════════════════════════════════════════════════
  // SOUMISSION DU FORMULAIRE
  // ════════════════════════════════════════════════════════════════
  onSubmit(): void {
 
    // Marque tous les champs comme touchés → affiche les erreurs
    this.loginForm.markAllAsTouched();
 
    // Arrête si le formulaire est invalide
    if (this.loginForm.invalid) return;
 
    // Active le spinner de chargement
    this.isLoading    = true;
    this.errorMessage = '';
    this.sessionExpired = false;
    const profilType=ProfilType.PAYLOAD;
 
    const { username, password } = this.loginForm.value;
 
    this.authService.login(username, password,profilType).subscribe({
 
      // ── Succès → redirection vers le dashboard ──────────────
      next: () => {
        this.isLoading = false;
        // Le session_payload est déjà stocké dans AuthService.login()
        this.router.navigate(['/dashboard-payload']);
      },
 
      // ── Erreur → affiche le message sous le formulaire ──────
      error: (err: Error) => {
        this.isLoading    = false;
        this.errorMessage = err.message || 'Identifiants incorrects';
      }
    });
  }
 
  // ── Toggle affichage mot de passe ────────────────────────────
  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }
 
  // ── Vérifie si un champ est invalide ET touché ───────────────
  // Utilisé dans le template : [class.is-invalid]="isFieldInvalid('x')"
  isFieldInvalid(field: string): boolean {
    const control = this.loginForm.get(field);
    return !!control && control.invalid && control.touched;
  }
}

