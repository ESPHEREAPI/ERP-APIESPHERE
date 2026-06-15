// acceuil.component.ts
import { Component, OnInit } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../core/auth/auth.service';
import { ModulePermissionService } from '../../core/services/module-permission.service';
import { Module } from '../../core/model/module';

@Component({
  selector: 'app-acceuil',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslatePipe],
  templateUrl: './acceuil.component.html',
  styleUrl: './acceuil.component.css'
})
export class AcceuilComponent implements OnInit {

  // ── Modules ────────────────────────────────────────────────────────────────

  modules: Module[] = [
    { id: 1,    code: 'auto_insurance',    title: 'Assurance Automobile',    icon: 'fas fa-car',           iconDisabled: 'fas fa-car',           enabled: false },
    { id: 11,   code: 'A_TIRD',            title: 'Assurance TIRD',          icon: 'fas fa-sync-alt',      iconDisabled: 'fas fa-sync-alt',      enabled: false },
    { id: 6,    code: 'adp_insurance',     title: 'Assurance ADP - Non Vie', icon: 'fas fa-shield-alt',    iconDisabled: 'fas fa-shield-alt',    enabled: false },
    { id: 7,    code: 'insured_management',title: 'Gestion Assurés',         icon: 'fas fa-users',         iconDisabled: 'fas fa-users',         enabled: false },
    { id: 2,    code: 'ird_insurance',     title: 'Assurance IRD',           icon: 'fas fa-hospital',      iconDisabled: 'fas fa-hospital',      enabled: false },
    { id: 3,    code: 'transport',         title: 'Transport',               icon: 'fas fa-truck',         iconDisabled: 'fas fa-truck',         enabled: false },
    { id: 4,    code: 'credit_caution',    title: 'Caution Crédit',          icon: 'fas fa-credit-card',   iconDisabled: 'fas fa-credit-card',   enabled: false },
    { id: 5,    code: 'agriculture',       title: 'Agriculture',             icon: 'fas fa-seedling',      iconDisabled: 'fas fa-seedling',      enabled: false },
    { id: 101,  code: 'ref_auto_insurance',title: 'Référentiel Auto',        icon: 'fas fa-car-alt',       iconDisabled: 'fas fa-car-alt',       enabled: false },
    { id: 1100, code: 'ref_A_TIRD',        title: 'Référentiel TIRD',        icon: 'fas fa-database',      iconDisabled: 'fas fa-database',      enabled: false },
    { id: 600,  code: 'ref_adp_insurance', title: 'Référentiel ADP',         icon: 'fas fa-layer-group',   iconDisabled: 'fas fa-layer-group',   enabled: false },
    { id: 8,    code: 'general_accounting',title: 'Comptabilité Générale',   icon: 'fas fa-calculator',    iconDisabled: 'fas fa-calculator',    enabled: false },
    { id: 10,   code: 'agency_accounting', title: 'Comptabilité Agence',     icon: 'fas fa-balance-scale', iconDisabled: 'fas fa-balance-scale', enabled: false },
    { id: 12,   code: 'reporting',         title: 'Gestion États',           icon: 'fas fa-chart-bar',     iconDisabled: 'fas fa-chart-bar',     enabled: false },
    { id: 13,   code: 'settings',          title: 'Paramétrage',             icon: 'fas fa-tools',         iconDisabled: 'fas fa-tools',         enabled: false },
    { id: 9,    code: 'admin',             title: 'Administration',          icon: 'fas fa-cogs',          iconDisabled: 'fas fa-cogs',          enabled: false },
  ];

  // ── État ───────────────────────────────────────────────────────────────────

  currentUser: any = null;
  showPasswordChangeModal = false;
  showNewPwd     = false;
  showConfirmPwd = false;

  passwordForm = {
    oldPassword    : '',
    newPassword    : '',
    confirmPassword: '',
    securityKey    : ''
  };

  // ── Lifecycle ──────────────────────────────────────────────────────────────

  constructor(
    private router: Router,
    private authService: AuthService,
    private modulePermissionService: ModulePermissionService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadUserData();
    this.checkModulePermissions();
  }

  // ── Données ────────────────────────────────────────────────────────────────

  private loadUserData(): void {
    const session = this.authService.currentUserValue;
    this.currentUser = session?.userDTO;

    if (!this.currentUser) {
      this.router.navigate(['/login']);
      return;
    }

    // PRODUCTEUR → rediriger directement vers la validation (sa seule page)
    if (session?.profilAgent === 'PRODUCTEUR') {
      this.router.navigate(['/certificates/validation']);
    }
  }

  private checkModulePermissions(): void {
    this.modules.forEach(m => {
      m.enabled = this.modulePermissionService.hasModuleAccess(m.code);
    });
  }

  // ── Navigation ─────────────────────────────────────────────────────────────

  navigateToModule(module: Module): void {
    if (!module.enabled) {
      this.toastr.warning('Vous n\'avez pas accès à ce module', 'Accès refusé');
      return;
    }
    this.router.navigate([`/modules/${module.code}`]);
  }

  // ── Compteurs (hero stats) ─────────────────────────────────────────────────

  getEnabledCount():  number { return this.modules.filter(m =>  m.enabled).length; }
  getDisabledCount(): number { return this.modules.filter(m => !m.enabled).length; }

  // ── Initiales avatar ───────────────────────────────────────────────────────

  getInitials(): string {
    const name: string = this.currentUser?.nomcomplet ?? '';
    const parts = name.trim().split(/\s+/);
    if (parts.length >= 2) {
      return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    }
    return name.slice(0, 2).toUpperCase() || 'U';
  }

  // ── Modal mot de passe ─────────────────────────────────────────────────────

  openPasswordChangeModal(): void {
    this.showPasswordChangeModal = true;
    this.resetPasswordForm();
  }

  closePasswordChangeModal(): void {
    this.showPasswordChangeModal = false;
    this.resetPasswordForm();
  }

  private resetPasswordForm(): void {
    this.passwordForm = { oldPassword: '', newPassword: '', confirmPassword: '', securityKey: '' };
    this.showNewPwd     = false;
    this.showConfirmPwd = false;
  }

  updatePassword(): void {
    if (!this.validatePasswordForm()) return;

    // TODO : appeler authService.updatePassword(this.passwordForm)
    this.toastr.info('Fonctionnalité en cours d\'implémentation', 'Info');
  }

  private validatePasswordForm(): boolean {
    const { oldPassword, newPassword, confirmPassword, securityKey } = this.passwordForm;

    if (!oldPassword || !newPassword || !confirmPassword || !securityKey) {
      this.toastr.error('Tous les champs sont obligatoires', 'Validation');
      return false;
    }
    if (newPassword !== confirmPassword) {
      this.toastr.error('Les mots de passe ne correspondent pas', 'Validation');
      return false;
    }
    if (newPassword.length < 6) {
      this.toastr.error('Le mot de passe doit contenir au moins 6 caractères', 'Validation');
      return false;
    }
    return true;
  }

  // ── Force du mot de passe ──────────────────────────────────────────────────

  getPasswordStrength(): number {
    const pwd = this.passwordForm.newPassword;
    if (!pwd) return 0;
    let score = 0;
    if (pwd.length >= 6)  score += 20;
    if (pwd.length >= 10) score += 15;
    if (this.hasUpperCase(pwd))    score += 20;
    if (this.hasLowerCase(pwd))    score += 15;
    if (this.hasNumber(pwd))       score += 15;
    if (this.hasSpecialChar(pwd))  score += 15;
    return Math.min(score, 100);
  }

  getPasswordStrengthText(): string {
    const s = this.getPasswordStrength();
    if (s < 40)  return 'Faible';
    if (s < 70)  return 'Moyen';
    return 'Fort';
  }

  // ── Helpers validation ─────────────────────────────────────────────────────

  hasUpperCase(pwd: string):    boolean { return /[A-Z]/.test(pwd ?? ''); }
  hasLowerCase(pwd: string):    boolean { return /[a-z]/.test(pwd ?? ''); }
  hasNumber(pwd: string):       boolean { return /[0-9]/.test(pwd ?? ''); }
  hasSpecialChar(pwd: string):  boolean { return /[^A-Za-z0-9]/.test(pwd ?? ''); }

  // ── Déconnexion ────────────────────────────────────────────────────────────

  logout(): void {
    this.authService.clearAuthData();
    this.router.navigate(['/login']);
  }
}