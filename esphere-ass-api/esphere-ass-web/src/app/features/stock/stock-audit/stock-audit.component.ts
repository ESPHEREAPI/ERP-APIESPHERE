import { Component, OnInit } from '@angular/core';
import { CommonModule }      from '@angular/common';
import { TranslatePipe }     from '@ngx-translate/core';
import { StockService }      from '../../../core/services/stock.service';
import { AuthService }       from '../../../core/auth/auth.service';
import { StockAuditResponse, StockAuditItem, AuditStatutGlobal, AuditStatutItem } from '../../../core/model/StockAudit';

@Component({
  selector: 'app-stock-audit',
  standalone: true,
  imports: [CommonModule, TranslatePipe],
  templateUrl: './stock-audit.component.html'
})
export class StockAuditComponent implements OnInit {

  audit:      StockAuditResponse | null = null;
  isLoading   = false;
  errorMsg    = '';
  officeCode  = '';

  constructor(
    public  stockService: StockService,
    private authService:  AuthService
  ) {}

  ngOnInit(): void {
    this.officeCode = this.authService.currentUserValue?.agencyCode ?? '';
    this.load();
  }

  load(): void {
    if (!this.officeCode) return;
    this.isLoading = true;
    this.errorMsg  = '';
    this.stockService.getAudit(this.officeCode).subscribe({
      next:  r  => { this.audit = r.data; this.isLoading = false; },
      error: () => { this.errorMsg = 'STOCK_AUDIT.ERROR_LOAD'; this.isLoading = false; }
    });
  }

  // ── Helpers statut global ──────────────────────────────────────

  statutGlobalColor(s: AuditStatutGlobal): string {
    const map: Record<string, string> = { SYNCHRONISE: 'success', ATTENTION: 'warning', DIVERGENCE: 'danger', SANS_DONNEE_EXTERNE: 'secondary' };
    return map[s] ?? 'secondary';
  }

  statutGlobalIcon(s: AuditStatutGlobal): string {
    const map: Record<string, string> = { SYNCHRONISE: 'fas fa-check-circle', ATTENTION: 'fas fa-exclamation-triangle', DIVERGENCE: 'fas fa-times-circle', SANS_DONNEE_EXTERNE: 'fas fa-question-circle' };
    return map[s] ?? 'fas fa-circle';
  }

  ecartCardBg(): string {
    const map: Record<string, string> = { SYNCHRONISE: 'bg-success', ATTENTION: 'bg-warning', DIVERGENCE: 'bg-danger', SANS_DONNEE_EXTERNE: 'bg-secondary' };
    return map[this.audit?.statutGlobal ?? ''] ?? 'bg-secondary';
  }

  formatDelta(v: number | null, prefix = true): string {
    if (v === null) return '—';
    if (v === 0) return '0';
    return (prefix && v > 0 ? '+' : '') + v;
  }

  // ── Helpers ligne de comparaison ──────────────────────────────

  rowClass(statut: AuditStatutItem): string {
    const map: Record<string, string> = { SYNCHRONISE: '', ATTENTION: 'table-warning', DIVERGENCE: 'table-danger', LOCAL_ONLY: 'table-info', EXTERNE_ONLY: 'table-secondary' };
    return map[statut] ?? '';
  }

  statutItemColor(statut: AuditStatutItem): string {
    const map: Record<string, string> = { SYNCHRONISE: 'success', ATTENTION: 'warning', DIVERGENCE: 'danger', LOCAL_ONLY: 'info', EXTERNE_ONLY: 'secondary' };
    return map[statut] ?? 'secondary';
  }

  statutItemIcon(statut: AuditStatutItem): string {
    const map: Record<string, string> = { SYNCHRONISE: 'fas fa-check', ATTENTION: 'fas fa-exclamation', DIVERGENCE: 'fas fa-times', LOCAL_ONLY: 'fas fa-database', EXTERNE_ONLY: 'fas fa-cloud' };
    return map[statut] ?? 'fas fa-circle';
  }

  deltaTextClass(delta: number | null): string {
    if (delta === null) return 'text-muted';
    if (delta === 0)    return 'text-success font-weight-bold';
    if (Math.abs(delta) <= 5) return 'text-warning font-weight-bold';
    return 'text-danger font-weight-bold';
  }

  // ── Barre de progression disponible ───────────────────────────

  progressPct(item: StockAuditItem): number {
    if (!item.localApprovisionnement) return 0;
    return Math.round((item.localDisponible / item.localApprovisionnement) * 100);
  }

  progressColor(pct: number): string {
    if (pct <= 10) return 'danger';
    if (pct <= 30) return 'warning';
    return 'success';
  }
}
