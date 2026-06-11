import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { environment } from '../../../../environments/environment.prod';
import { finalize, Subject, takeUntil } from 'rxjs';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

type PageState        = 'loading' | 'form' | 'success' | 'expired' | 'error';
type PasswordStrength = 'weak' | 'medium' | 'strong';

@Component({
  selector: 'app-activate',
  standalone: true,
  imports: [  CommonModule,
    ReactiveFormsModule,],
  templateUrl: './activate.component.html',
  styleUrl: './activate.component.css'
})

export class ActivateComponent implements OnInit, OnDestroy {

  state: PageState = 'loading';
  token            = '';
  errorMessage     = '';
  isSubmitting     = false;
  showPassword     = false;
  showConfirm      = false;

  form!: FormGroup;
  private destroy$ = new Subject<void>();

  constructor(
    private route:  ActivatedRoute,
    private router: Router,
    private fb:     FormBuilder,
    private http:   HttpClient,
    private cdr:    ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token') ?? '';

    if (!this.token) {
      this.state        = 'error';
      this.errorMessage = "Token d'activation manquant ou invalide.";
      this.cdr.markForCheck();
      return;
    }

    this.buildForm();
    this.state = 'form';
    this.cdr.markForCheck();
  }

  private buildForm(): void {
    this.form = this.fb.group(
      {
        password:        ['', [Validators.required, Validators.minLength(8)]],
        confirmPassword: ['', Validators.required],
      },
      { validators: this.passwordMatchValidator }
    );
  }

  private passwordMatchValidator(g: AbstractControl): ValidationErrors | null {
    const pwd     = g.get('password')?.value;
    const confirm = g.get('confirmPassword')?.value;
    return pwd && confirm && pwd !== confirm ? { mismatch: true } : null;
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }

    this.isSubmitting = true;
    this.cdr.markForCheck();

    const payload = {
      token:           this.token,
      password:        this.form.value.password,
      confirmPassword: this.form.value.confirmPassword,
    };

    this.http
      .post(`/subscriber/activate`, payload)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => { this.isSubmitting = false; this.cdr.markForCheck(); })
      )
      .subscribe({
        next:  () => { this.state = 'success'; this.cdr.markForCheck(); },
        error: (err) => {
          if (err.status === 410) {
            this.state = 'expired';
          } else {
            this.state        = 'error';
            this.errorMessage = err.error?.message ?? 'Une erreur est survenue.';
          }
          this.cdr.markForCheck();
        },
      });
  }

  // ── Jauge de force du mot de passe ────────────────────────────────────────
  getPasswordStrength(): PasswordStrength {
    const pwd: string = this.form.get('password')?.value ?? '';
    const strong = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/.test(pwd);
    const medium = /^(?=.*[a-zA-Z])(?=.*\d).{8,}$/.test(pwd);
    if (strong) return 'strong';
    if (medium) return 'medium';
    return 'weak';
  }

  getPasswordStrengthLabel(): string {
    const labels: Record<PasswordStrength, string> = {
      weak:   'Faible',
      medium: 'Moyen',
      strong: 'Fort',
    };
    return labels[this.getPasswordStrength()];
  }

  goToLogin(): void { this.router.navigate(['/login']); }

  hasError(field: string, error: string): boolean {
    const c = this.form.get(field);
    return !!(c?.hasError(error) && (c.touched || c.dirty));
  }

  ngOnDestroy(): void { this.destroy$.next(); this.destroy$.complete(); }
}
