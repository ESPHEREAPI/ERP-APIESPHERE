import { ChangeDetectionStrategy, ChangeDetectorRef, Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Subscriber } from '../../../core/models/Subscriber';
import { AbstractControl, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { catchError, debounceTime, distinctUntilChanged, finalize, of, Subject, switchMap, takeUntil } from 'rxjs';
import { SouscripteurService } from '../../../core/services/souscripteur.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-subscriber-form',
  standalone: true,
  imports: [CommonModule,FormsModule,  ReactiveFormsModule],
  templateUrl: './subscriber-form.component.html',
   changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrl: './subscriber-form.component.css'
})
export class SubscriberFormComponent implements OnInit, OnDestroy {
  @Input() subscriberId?: number;
  @Output() saved = new EventEmitter<Subscriber>();
  @Output() cancelled = new EventEmitter<void>();

  form!: FormGroup;
  isEditMode = false;
  isSubmitting = false;
  isCheckingPolicy = false;
  isCheckingUsername = false;
  policyCheckError = '';
  policyCheckSuccess = false;
  showPassword = false;
  showConfirmPassword = false;
  activationLinkSent = false;
  activationLinkSending = false;
  activationLinkExpiry?: Date;
  activationCountdown = '';
  private countdownInterval?: ReturnType<typeof setInterval>;

  private destroy$ = new Subject<void>();

  activationDurationOptions = [
    { value: 24, label: '24 heures' },
    { value: 48, label: '48 heures' },
    { value: 72, label: '72 heures' },
    { value: 168, label: '7 jours' },
  ];

  constructor(
    private fb: FormBuilder,
    private subscriberService: SouscripteurService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.isEditMode = !!this.subscriberId;
    this.buildForm();
    this.setupPolicyCheck();
    this.setupUsernameCheck();
    this.setupPasswordModeChange();

    if (this.isEditMode) {
      this.loadSubscriber();
    }
  }

  private buildForm(): void {
    this.form = this.fb.group(
      {
        policyNumber: ['', [Validators.required, Validators.pattern(/^\d{4}-213\d{7}$/)]],
        fullName: [{ value: '', disabled: true }, Validators.required],
        phoneNumber: ['', [Validators.required, Validators.pattern(/^\+?\d[\d\s\-]{8,14}$/)]],
        email: ['', [Validators.required, Validators.email]],
        username: ['', [Validators.required, Validators.minLength(3), Validators.pattern(/^[a-zA-Z0-9._-]+$/)]],
        isActive: [true],
        passwordMode: ['manual'],
        password: [''],
        confirmPassword: [''],
        activationDuration: [48],
      },
      { validators: this.passwordMatchValidator }
    );
  }

  private passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
    const mode = group.get('passwordMode')?.value;
    if (mode !== 'manual') return null;
    const pwd = group.get('password')?.value;
    const confirm = group.get('confirmPassword')?.value;
    if (!pwd && !confirm) return null;
    return pwd === confirm ? null : { passwordMismatch: true };
  }

  private setupPolicyCheck(): void {
    this.form
      .get('policyNumber')!
      .valueChanges.pipe(
        debounceTime(600),
        distinctUntilChanged(),
        takeUntil(this.destroy$)
      )
      .subscribe((value) => {
        if (!value || value.length < 8) {
          this.resetPolicyCheck();
          return;
        }
        this.checkPolicy(value);
      });
  }

  private setupUsernameCheck(): void {
    this.form
      .get('username')!
      .valueChanges.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        switchMap((value) => {
          if (!value || value.length < 3) return of(null);
          this.isCheckingUsername = true;
          this.cdr.markForCheck();
          return this.subscriberService
            .checkUsernameAvailability(value, this.subscriberId)
            .pipe(catchError(() => of(null)));
        }),
        takeUntil(this.destroy$)
      )
      .subscribe((available) => {
        this.isCheckingUsername = false;
        if (available === false) {
          this.form.get('username')!.setErrors({ usernameNotAvailable: true });
        }
        this.cdr.markForCheck();
      });
  }

  private setupPasswordModeChange(): void {
    this.form
      .get('passwordMode')!
      .valueChanges.pipe(takeUntil(this.destroy$))
      .subscribe((mode) => {
        const pwd = this.form.get('password')!;
        const confirm = this.form.get('confirmPassword')!;

        if (mode === 'manual') {
          pwd.setValidators([Validators.required, Validators.minLength(8)]);
          confirm.setValidators([Validators.required]);
        } else {
          pwd.clearValidators();
          confirm.clearValidators();
          pwd.setValue('');
          confirm.setValue('');
        }

        pwd.updateValueAndValidity();
        confirm.updateValueAndValidity();
        this.cdr.markForCheck();
      });

    // Trigger initial state
    this.form.get('passwordMode')!.updateValueAndValidity();
  }

  private checkPolicy(policyNumber: string): void {
    this.isCheckingPolicy = true;
    this.policyCheckError = '';
    this.policyCheckSuccess = false;
    this.cdr.markForCheck();

    this.subscriberService
      .checkPolicy(policyNumber)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => {
          this.isCheckingPolicy = false;
          this.cdr.markForCheck();
        })
      )
      .subscribe((result) => {
        if (result.exists) {
          this.policyCheckSuccess = true;
          this.form.get('fullName')!.setValue(result.fullName);
        } else {
          this.policyCheckError = `Numéro de police "${policyNumber}" introuvable`;
          this.form.get('fullName')!.setValue('');
          this.form.get('policyNumber')!.setErrors({ policyNotFound: true });
        }
        this.cdr.markForCheck();
      });
  }

  private resetPolicyCheck(): void {
    this.policyCheckError = '';
    this.policyCheckSuccess = false;
    this.form.get('fullName')!.setValue('');
  }

  private loadSubscriber(): void {
    this.subscriberService
      .getById(this.subscriberId!)
      .pipe(takeUntil(this.destroy$))
      .subscribe((sub) => {
        this.form.patchValue({
          policyNumber: sub.policyNumber,
          fullName: sub.fullName,
          phoneNumber: sub.phoneNumber,
          email: sub.email,
          username: sub.username,
          isActive: sub.active,
          passwordMode: sub.passwordMode,
          activationDuration: sub.activationDuration || 48,
        });
        this.policyCheckSuccess = true;
        if (sub.activationExpiry) {
          this.activationLinkSent = true;
          this.activationLinkExpiry = sub.activationExpiry;
          this.startCountdown();
        }
        this.cdr.markForCheck();
      });
  }

  sendActivationLink(): void {
    if (!this.form.get('email')!.valid) {
      this.form.get('email')!.markAsTouched();
      return;
    }

    const duration = this.form.get('activationDuration')!.value;
    const email = this.form.get('email')!.value;

    this.activationLinkSending = true;
    this.cdr.markForCheck();

    const request = {
      subscriberId: this.subscriberId || 0,
      email,
      duration,
    };

    this.subscriberService
      .sendActivationLink(request)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => {
          this.activationLinkSending = false;
          this.cdr.markForCheck();
        })
      )
      .subscribe((result) => {
        this.activationLinkSent = true;
        this.activationLinkExpiry = new Date(result.expiry);
        this.startCountdown();
        this.cdr.markForCheck();
      });
  }

  private startCountdown(): void {
    if (this.countdownInterval) clearInterval(this.countdownInterval);
    this.countdownInterval = setInterval(() => {
      if (!this.activationLinkExpiry) return;
      const now = new Date().getTime();
      const expiry = this.activationLinkExpiry.getTime();
      const diff = expiry - now;

      if (diff <= 0) {
        this.activationCountdown = 'Expiré';
        this.activationLinkSent = false;
        clearInterval(this.countdownInterval);
      } else {
        const hours = Math.floor(diff / (1000 * 60 * 60));
        const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((diff % (1000 * 60)) / 1000);
        this.activationCountdown = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
      }
      this.cdr.markForCheck();
    }, 1000);
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.cdr.markForCheck();

    const raw = this.form.getRawValue();
    const payload: Omit<Subscriber, 'id' | 'createdAt' | 'updatedAt'> = {
      policyNumber: raw.policyNumber,
      fullName: raw.fullName,
      phoneNumber: raw.phoneNumber,
      email: raw.email,
      username: raw.username,
      active: raw.isActive,
      passwordMode: raw.passwordMode,
      password: raw.passwordMode === 'manual' ? raw.password : undefined,
      activationDuration: raw.passwordMode === 'activation_link' ? raw.activationDuration : undefined,
    };

    const action$ = this.isEditMode
      ? this.subscriberService.update(this.subscriberId!, payload)
      : this.subscriberService.create(payload);

    action$
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => {
          this.isSubmitting = false;
          this.cdr.markForCheck();
        })
      )
      .subscribe((result) => {
        this.saved.emit(result);
      });
  }

  onCancel(): void {
    this.cancelled.emit();
  }

  // Helpers for template
  get isManualMode(): boolean {
    return this.form.get('passwordMode')?.value === 'manual';
  }

  get isActivationLinkMode(): boolean {
    return this.form.get('passwordMode')?.value === 'activation_link';
  }

  hasError(field: string, error: string): boolean {
    const ctrl = this.form.get(field);
    return !!(ctrl?.hasError(error) && (ctrl.touched || ctrl.dirty));
  }

  hasGroupError(error: string): boolean {
    return !!(this.form.hasError(error) && this.form.get('confirmPassword')?.touched);
  }

  ngOnDestroy(): void {
    if (this.countdownInterval) clearInterval(this.countdownInterval);
    this.destroy$.next();
    this.destroy$.complete();
  }

}
