import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { Subject, finalize, takeUntil } from 'rxjs';
import { AuthService } from '../../../auth/auth.service';
import { LangueService } from '../../../services/langue.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, TranslateModule, FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

  loginForm!: FormGroup;
  isLoading = false;
  loadingProgress = 0;
  errorMessage = '';
  isOpen = false;
  currentYear = new Date().getFullYear();
  appVersion = '1.0.0';

  languages = [
    { code: 'fr', name: 'Français' },
    { code: 'en', name: 'English' }
  ];
  currentLanguage = this.languages[0];

  private readonly destroy$ = new Subject<void>();

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private authService: AuthService,
    private langueService: LangueService
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(2)]],
      password: ['', [Validators.required, Validators.minLength(4)]],
      remember: [false]
    });

    const idx = this.languages.findIndex(l => l.code === this.langueService._userLanguage);
    if (idx !== -1) this.currentLanguage = this.languages[idx];
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get f() { return this.loginForm.controls; }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.errorMessage = '';
    this.isLoading = true;
    this.animateProgress();

    const { username, password, remember } = this.loginForm.value;

    this.authService.login({ username, password, remember })
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => {
          this.isLoading = false;
          this.loadingProgress = 0;
        })
      )
      .subscribe({
        next: (session) => {
          console.log('✅ Connexion réussie:', session.login);
          setTimeout(() => {
            this.router.navigate(['/public/admin/accueil']);
          }, 500);
        },
        error: (err) => {
          this.errorMessage = err.message || 'Erreur de connexion';
          console.error('❌ Login error:', err);
        }
      });
  }

  private animateProgress(): void {
    const interval = setInterval(() => {
      if (this.loadingProgress >= 90 || !this.isLoading) {
        clearInterval(interval);
      } else {
        this.loadingProgress += Math.random() * 20;
        if (this.loadingProgress > 90) this.loadingProgress = 90;
      }
    }, 200);
  }

  toggleDropdown(): void { this.isOpen = !this.isOpen; }

  selectLanguage(lang: { code: string; name: string }): void {
    this.currentLanguage = lang;
    this.isOpen = false;
    this.langueService.setLanguage(lang.code);
  }
}
