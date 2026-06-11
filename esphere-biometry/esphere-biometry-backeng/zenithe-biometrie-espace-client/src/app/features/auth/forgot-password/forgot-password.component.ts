// forgot-password.component.ts
import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/auth/auth.service';
import { SouscripteurService } from '../../../core/services/souscripteur.service';
import { finalize, Subject, takeUntil } from 'rxjs';


import { maskEmail } from '../../../shared/utils/mask-email';
import { EmailConfirmDialogComponent } from '../email-confirm-dialog/email-confirm-dialog.component';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, EmailConfirmDialogComponent],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent implements OnInit, OnDestroy {

  private destroy$ = new Subject<void>();

  currentStep = 1;
  profil      = '';

  // ── Étape 1 ───────────────────────────────────────────────────────────────
  verifying        = false;
  verifyError      = '';
  verifySuccess    = false;
  foundAccountName = '';
  souscripteur     = '';
  user_biometry    = '';
  email_confirm!:  string;  // email de référence retourné par l'API

  // ── Étape 2 — email + champs mot de passe ─────────────────────────────────
  sending       = false;
  sendError     = '';
  emailSent     = false;

  /** Email résolu après la comparaison (saisi ou confirmé par le dialogue) */
  resolvedEmail = '';

  /**
   * showPasswordFields : true dès que la comparaison email est résolue.
   * Déclenche l'apparition des champs "Nouveau mot de passe" et "Confirmer".
   */
  showPasswordFields = false;

  // Visibilité des mots de passe (toggle œil)
  showNewPassword     = false;
  showConfirmPassword = false;

  // Valeurs des champs mot de passe (liées via ngModel)
  newPassword     = '';
  confirmPassword = '';

  // Erreur de validation mot de passe (côté frontend)
  passwordError = '';

  // ── Dialogue de discordance email ─────────────────────────────────────────
  showEmailDialog = false;
  emailSaisie: any = '';
  maskedConfirm    = '';

  private pendingEmail = '';

  currentYear = new Date().getFullYear();

  constructor(
    private route:            ActivatedRoute,
    private router:           Router,
    private authService:      AuthService,
    private souscribeService: SouscripteurService,
    private cdr:              ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.profil = params['profil'] || '';
    });
  }

  // ── Helpers ───────────────────────────────────────────────────────────────
  getIdentifierPlaceholder(): string {
    switch (this.profil) {
      case 'SOUSCRIPTEUR':  return 'Ex: 1017-213xxxxxx';
      case 'SERVICE_SANTE': return 'Identifiant biométrique enregistré';
      case 'DII':           return 'Identifiant DII biométrique';
      default:              return 'Votre identifiant';
    }
  }

  getProfilLabel(): string {
    switch (this.profil) {
      case 'SOUSCRIPTEUR':  return this.souscripteur;
      case 'SERVICE_SANTE': return this.user_biometry;
      case 'DII':           return 'DII – ' + this.user_biometry;
      default:              return 'Compte';
    }
  }

  /** Force du mot de passe : 'weak' | 'medium' | 'strong' */
  getPasswordStrength(): 'weak' | 'medium' | 'strong' {
    const pwd = this.newPassword;
    if (/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/.test(pwd)) return 'strong';
    if (/^(?=.*[a-zA-Z])(?=.*\d).{8,}$/.test(pwd))                          return 'medium';
    return 'weak';
  }

  getStrengthLabel(): string {
    return { weak: 'Faible', medium: 'Moyen', strong: 'Fort' }[this.getPasswordStrength()];
  }

  getStrengthClass(): string {
    return { weak: 'bg-danger', medium: 'bg-warning', strong: 'bg-success' }[this.getPasswordStrength()];
  }

  getStrengthWidth(): string {
    return { weak: '33%', medium: '66%', strong: '100%' }[this.getPasswordStrength()];
  }

  // ══════════════════════════════════════════════════════════════════════════
  // ÉTAPE 1 — Vérification de l'identifiant
  // ══════════════════════════════════════════════════════════════════════════
  verifyIdentifier(formData: any): void {
    this.verifyError   = '';
    this.verifySuccess = false;
    this.verifying     = true;

    if (this.profil === 'SOUSCRIPTEUR') {

      if (!formData.identifier || formData.identifier.trim().length < 15) {
        this.verifyError = 'Aucun compte trouvé pour cet identifiant. Vérifiez votre saisie.';
        this.verifying   = false;
        this.cdr.markForCheck();
        return;
      }

      this.souscribeService.checkPolicy(formData.identifier)
        .pipe(
          takeUntil(this.destroy$),
          finalize(() => { this.verifying = false; this.cdr.markForCheck(); })
        )
        .subscribe({
          next: (result) => {
            if (result.exists) {
              this.verifySuccess    = true;
              this.souscripteur     = result.fullName;
              this.foundAccountName = result.policyNumber;
              this.email_confirm    = result.email ?? '';
              this.maskedConfirm    = maskEmail(this.email_confirm);
              setTimeout(() => { this.currentStep = 2; this.cdr.markForCheck(); }, 800);
            } else {
              this.verifyError = `Numéro de police "${formData.identifier}" introuvable`;
            }
            this.cdr.markForCheck();
          },
          error: () => {
            this.verifyError = 'Erreur de connexion. Réessayez.';
            this.cdr.markForCheck();
          }
        });

    } else if (this.profil === 'SERVICE_SANTE' || this.profil === 'DII') {
      // TODO : vérification biométrique
      this.verifying = false;
    }
  }

  // ══════════════════════════════════════════════════════════════════════════
  // ÉTAPE 2 — sendResetEmail : point d'entrée sur le bouton "Suivant"
  //
  //  Flux :
  //  1️⃣  Comparer emailSaisie (any) vs email_confirm (string)
  //  2️⃣  Différents → dialogue → résolution → resolvedEmail stocké
  //  3️⃣  Identiques → resolvedEmail = saisie directement
  //  4️⃣  Dans les deux cas → showPasswordFields = true
  //       (les champs MDP apparaissent sous l'email)
  // ══════════════════════════════════════════════════════════════════════════
  sendResetEmail(formData: any): void {
    this.sendError     = '';
    this.passwordError = '';

    // Si les champs mot de passe sont déjà visibles,
    // le bouton "Envoyer" appelle directement doSendResetPassword
    if (this.showPasswordFields) {
      this.submitPassword();
      return;
    }

    // 1️⃣ Capturer l'email saisi (any)
    this.emailSaisie = formData.email;

    const saisieNorm:  string = String(this.emailSaisie ?? '').trim().toLowerCase();
    const confirmNorm: string = (this.email_confirm    ?? '').trim().toLowerCase();

    // 2️⃣ Comparaison
    const areDifferent = saisieNorm !== confirmNorm && confirmNorm !== '';

    if (areDifferent) {
      // Emails différents → suspendre et ouvrir le dialogue
      this.pendingEmail    = saisieNorm;
      this.showEmailDialog = true;
      this.cdr.markForCheck();
      return;
    }

    // 3️⃣ Emails identiques → résoudre directement
    this.resolveEmail(saisieNorm);
  }

  // ── Résolution dialogue : "Garder mon email saisi" ───────────────────────
  onKeepSaisie(): void {
    this.showEmailDialog = false;
    this.resolveEmail(this.pendingEmail);
    this.cdr.markForCheck();
  }

  // ── Résolution dialogue : "Utiliser l'email enregistré" ──────────────────
  onUseConfirmEmail(confirmedEmail: string): void {
    this.showEmailDialog = false;
    this.resolveEmail(confirmedEmail.trim().toLowerCase());
    this.cdr.markForCheck();
  }

  // ── Fermeture sans action ─────────────────────────────────────────────────
  onDialogCancel(): void {
    this.showEmailDialog = false;
    this.pendingEmail    = '';
    this.cdr.markForCheck();
  }

  /**
   * resolveEmail — appelé après résolution de la comparaison.
   * Stocke l'email résolu et affiche les champs mot de passe.
   */
  private resolveEmail(email: string): void {
    this.resolvedEmail      = email;
    this.showPasswordFields = true;   // ← les champs MDP apparaissent
    this.cdr.markForCheck();
  }

  // ══════════════════════════════════════════════════════════════════════════
  // Validation des champs mot de passe puis appel doSendResetPassword
  // ══════════════════════════════════════════════════════════════════════════
  private submitPassword(): void {
    this.passwordError = '';

    if (!this.newPassword || this.newPassword.length < 8) {
      this.passwordError = 'Le mot de passe doit contenir au moins 8 caractères.';
      return;
    }
    if (this.newPassword !== this.confirmPassword) {
      this.passwordError = 'Les mots de passe ne correspondent pas.';
      return;
    }

    this.doSendResetPassword(this.resolvedEmail, this.newPassword);
  }

  // ══════════════════════════════════════════════════════════════════════════
  // doSendResetPassword — envoi effectif à l'API
  // Appelé UNIQUEMENT après validation email + mot de passe
  // ══════════════════════════════════════════════════════════════════════════
  private doSendResetPassword(email: string, password: string): void {
    this.sending = true;
    this.cdr.markForCheck();

    // TODO: remplacer par l'appel HTTP réel, ex :
     this.souscribeService
       .resetPasswordByEmail(email,password)
       .pipe(
         takeUntil(this.destroy$),
         finalize(() => { this.sending = false; this.cdr.markForCheck(); })
       )
       .subscribe({
         next:  () => { this.emailSent = true; setTimeout(() => this.router.navigate(['/login']), 3000); },
        error: (e) => { this.sendError = e?.error?.message ?? 'Erreur. Réessayez.'; }
      });

    // Simulation (à remplacer)
    //setTimeout(() => {
      //this.sending   = false;
      //this.emailSent = true;
      //this.cdr.markForCheck();
      //setTimeout(() => this.router.navigate(['/login']), 3000);
    //}, 2000);
  }

  ngOnDestroy(): void { this.destroy$.next(); this.destroy$.complete(); }
}