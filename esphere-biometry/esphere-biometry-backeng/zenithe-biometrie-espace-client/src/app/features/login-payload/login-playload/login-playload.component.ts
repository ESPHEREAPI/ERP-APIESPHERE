import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthPayLoadService } from '../../../core/services/AuthService-payload';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../auth/auth.service';

@Component({
  selector: 'app-login-playload',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule],
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
 
  constructor(
    private fb:          FormBuilder,
    private authService: AuthService,
    private router:      Router,
    private route:       ActivatedRoute  // Pour lire ?expired=true
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
    this.route.queryParams.subscribe(params => {
      if (params['expired']) {
        this.sessionExpired = true;
      }
    });
 
    // ── Si déjà connecté → redirige directement au dashboard ──
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
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
 
    const { username, password } = this.loginForm.value;
 
    this.authService.login(username, password).subscribe({
 
      // ── Succès → redirection vers le dashboard ──────────────
      next: () => {
        this.isLoading = false;
        // Le session_payload est déjà stocké dans AuthService.login()
        this.router.navigate(['/dashboard']);
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

