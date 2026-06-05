import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';

import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { Language } from '../../models/langue';
import { LangueService } from '../../services/langue.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, TranslateModule, FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {
  languages: Language[] = [
    { code: 'en', name: 'English' },
    { code: 'fr', name: 'Français' }
  ];

  currentYear = new Date().getFullYear();
  appVersion = '2.4.18';
  isOpen = false;
  currentLanguage: Language = this.languages[0];
  forgotPasswordForm!: FormGroup;
  
  // Loading states
  isLoading = false;
  loadingProgress = 0;
  loadingMessage = 'processing';
  
  // Flow states
  codeSent = false;
  successMessage = '';
  errorMessage = '';
  showPassword: boolean = false;
showConfirmPassword: boolean = false;

passwordCriteria = {
  minLength: false,
  upperCase: false,
  lowerCase: false,
  number: false,
  specialChar: false
};

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private langueService: LangueService
  ) {}

  ngOnInit(): void {
    this.forgotPasswordForm = this.fb.group({
      recoveryMethod: ['email', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: [''],
      securityCode: [''],
      newPassword: [''],
      confirmPassword: ['']
    }, { validators: this.passwordMatchValidator });

    // Observer les changements de méthode de récupération
    this.forgotPasswordForm.get('recoveryMethod')?.valueChanges.subscribe(value => {
      this.updateValidators(value);
    });

    const index = this.languages.findIndex(
      l => l.code === this.langueService._userLanguage
    );
    if (index !== -1) {
      this.currentLanguage = this.languages[index];
    }
  }
  checkPasswordStrength(password: string) {
  this.passwordCriteria.minLength = password?.length >= 8;
  this.passwordCriteria.upperCase = /[A-Z]/.test(password);
  this.passwordCriteria.lowerCase = /[a-z]/.test(password);
  this.passwordCriteria.number = /[0-9]/.test(password);
  this.passwordCriteria.specialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);
}

  get recoveryMethod(): string {
    return this.forgotPasswordForm.get('recoveryMethod')?.value || 'email';
  }

  // Mettre à jour les validateurs selon la méthode choisie
  private updateValidators(method: string): void {
    const emailControl = this.forgotPasswordForm.get('email');
    const phoneControl = this.forgotPasswordForm.get('phone');

    if (method === 'email') {
      emailControl?.setValidators([Validators.required, Validators.email]);
      phoneControl?.clearValidators();
    } else {
      phoneControl?.setValidators([Validators.required, Validators.pattern(/^[+]?[\d\s-]+$/)]);
      emailControl?.clearValidators();
    }

    emailControl?.updateValueAndValidity();
    phoneControl?.updateValueAndValidity();
  }

  // Validateur personnalisé pour vérifier que les mots de passe correspondent
  private passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const newPassword = control.get('newPassword');
    const confirmPassword = control.get('confirmPassword');

    if (!newPassword || !confirmPassword) {
      return null;
    }

    return newPassword.value === confirmPassword.value ? null : { passwordMismatch: true };
  }

  onSubmit(): void {
    if (!this.codeSent) {
      // Première étape : envoyer le code
      this.sendSecurityCode();
    } else {
      // Deuxième étape : réinitialiser le mot de passe
      this.resetPassword();
    }
  }

  private sendSecurityCode(): void {
    const method = this.recoveryMethod;
  const control = method === 'email' ? 'email' : 'phone';
  const controlRef = this.forgotPasswordForm.get(control);

  // ⚡ Forcer la validation avant test
  controlRef?.updateValueAndValidity({ onlySelf: true });

  console.log("value:", controlRef?.value, "errors:", controlRef?.errors);

  if (controlRef?.invalid) {
    controlRef.markAsTouched();
    return;
  }

  this.isLoading = true;
  this.loadingMessage = 'sendingCode';
  this.animateProgress();

  setTimeout(() => {
    this.isLoading = false;
    this.loadingProgress = 0;
    this.codeSent = true;

    this.forgotPasswordForm.get('securityCode')?.setValidators([Validators.required, Validators.minLength(6)]);
    this.forgotPasswordForm.get('newPassword')?.setValidators([
      Validators.required, 
      Validators.minLength(8),
      Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/)
    ]);
    this.forgotPasswordForm.get('confirmPassword')?.setValidators([Validators.required]);

    this.forgotPasswordForm.get('securityCode')?.updateValueAndValidity();
    this.forgotPasswordForm.get('newPassword')?.updateValueAndValidity();
    this.forgotPasswordForm.get('confirmPassword')?.updateValueAndValidity();

    this.successMessage = 'Code de sécurité envoyé avec succès!';
    console.log('Code sent to:', controlRef?.value);
  }, 2000);
  }

  private resetPassword(): void {
    if (this.forgotPasswordForm.invalid) {
      this.forgotPasswordForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.loadingMessage = 'resettingPassword';
    this.animateProgress();

    const { securityCode, newPassword } = this.forgotPasswordForm.value;

    // Simuler la réinitialisation (remplacer par votre service réel)
    setTimeout(() => {
      this.isLoading = false;
      this.loadingProgress = 0;
      
      // 👉 Ici votre vraie logique de réinitialisation
      console.log('Security Code:', securityCode);
      console.log('New Password:', newPassword);
      
      this.successMessage = 'Mot de passe réinitialisé avec succès! Redirection...';
      
      // Rediriger vers la page de connexion après 2 secondes
      setTimeout(() => {
        this.router.navigateByUrl('/login');
      }, 2000);
    }, 2000);
  }

  // Animation de la barre de progression
  private animateProgress(): void {
    this.loadingProgress = 0;
    const interval = setInterval(() => {
      if (this.loadingProgress >= 100) {
        clearInterval(interval);
      } else {
        this.loadingProgress += Math.random() * 30;
        if (this.loadingProgress > 100) {
          this.loadingProgress = 100;
        }
      }
    }, 200);
  }

  isFormInvalid(): boolean {
    if (!this.codeSent) {
      // Première étape : valider email ou téléphone
      const method = this.recoveryMethod;
      const control = method === 'email' ? 'email' : 'phone';
      return this.forgotPasswordForm.get(control)?.invalid || false;
    } else {
      // Deuxième étape : valider tous les champs
      return this.forgotPasswordForm.invalid;
    }
  }

  toggleDropdown(): void {
    this.isOpen = !this.isOpen;
  }

  selectLanguage(lang: Language): void {
    this.currentLanguage = lang;
    this.isOpen = false;
    this.langueService.setLanguage(this.currentLanguage.code);
  }

  get f() {
    return this.forgotPasswordForm.controls;
  }
}