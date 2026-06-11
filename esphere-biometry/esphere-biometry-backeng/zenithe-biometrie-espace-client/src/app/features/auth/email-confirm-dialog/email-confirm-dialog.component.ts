import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { maskEmail } from '../../../shared/utils/mask-email';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-email-confirm-dialog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './email-confirm-dialog.component.html',
  styleUrl: './email-confirm-dialog.component.css'
})
export class EmailConfirmDialogComponent implements OnChanges {

  /** Déclenche l'ouverture / fermeture */
  @Input() visible = false;

  /** Email saisi dans le formulaire (type any accepté) */
  @Input() emailSaisie: any = '';

  /** Email de référence / confirmé en base */
  @Input() emailConfirm: string = '';

  /** L'utilisateur confirme de garder l'email saisi */
  @Output() onKeepSaisie = new EventEmitter<void>();

  /** L'utilisateur choisit d'utiliser l'email confirmé */
  @Output() onUseConfirm = new EventEmitter<string>();

  /** Fermeture sans action */
  @Output() onCancel = new EventEmitter<void>();

  // ── État interne ──────────────────────────────────────────────────────────
  maskedConfirm  = '';
  emailSaisieStr = '';
  areDifferent   = false;

  ngOnChanges(changes: SimpleChanges): void {
    // Recalcul à chaque changement d'inputs
    this.emailSaisieStr = String(this.emailSaisie ?? '').trim().toLowerCase();
    const confirmStr    = (this.emailConfirm ?? '').trim().toLowerCase();

    this.areDifferent  = this.emailSaisieStr !== confirmStr;
    this.maskedConfirm = maskEmail(this.emailConfirm);
  }

  keepSaisie(): void  { this.onKeepSaisie.emit(); }
  useConfirm(): void  { this.onUseConfirm.emit(this.emailConfirm); }
  cancel(): void      { this.onCancel.emit(); }

}
