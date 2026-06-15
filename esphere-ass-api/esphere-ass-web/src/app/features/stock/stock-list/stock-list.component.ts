import { Component, OnInit }   from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { CommonModule }        from '@angular/common';
import { RouterModule }        from '@angular/router';
import { FormsModule }         from '@angular/forms';
import { StockService }        from '../../../core/services/stock.service';
import { AuthService }         from '../../../core/auth/auth.service';
import { InfosAdminAgenceService } from '../../../core/services/infos-admin-agence.service';
import { InfosAdminAgence }    from '../../../core/model/infos-admin-agence.model';
import {
  StockAttestation,
  ApprovisionnerRequest,
  AjustementRequest
} from '../../../core/model/StockAttestation';

@Component({
  selector: 'app-stock-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, TranslatePipe],
  templateUrl: './stock-list.component.html',
  styleUrl: './stock-list.component.css'
})
export class StockListComponent implements OnInit {

  stocks:           StockAttestation[] = [];
  filtered:         StockAttestation[] = [];
  bureaux:          InfosAdminAgence[] = [];
  isLoading         = true;
  loadingBureaux    = false;
  errorMessage      = '';
  searchTerm        = '';
  filterStatut      = '';
  officeCode        = '';
  isAdmin           = false;

  // ── Modal Approvisionnement ──────────────────────────────────
  showApproModal    = false;
  selectedStock:    StockAttestation | null = null;
  approForm: ApprovisionnerRequest = { quantite: 0, referenceSource: '', motif: '' };
  savingAppro       = false;

  // ── Modal Ajustement ─────────────────────────────────────────
  showAjustModal    = false;
  ajustForm: AjustementRequest = { delta: 0, motif: '' };
  savingAjust       = false;

  // ── Modal Init stock ─────────────────────────────────────────
  showInitModal     = false;
  initForm = {
    bureauSelectionne: null as InfosAdminAgence | null,
    certTypeCode:      '',
    certTypeName:      '',
    certVariantCode:   '',
    certVariantName:   '',
    quantiteInitiale:  0,
    seuilAlerte:       50,
    seuilCritique:     10,
    motif:             ''
  };
  savingInit        = false;

  constructor(
    private stockService:  StockService,
    private authService:   AuthService,
    private agenceService: InfosAdminAgenceService
  ) {}

  ngOnInit(): void {
    const session       = this.authService.currentUserValue;
    this.isAdmin        = session?.profilAgent === 'ADMINISTRATEUR';
    this.officeCode     = session?.agencyCode ?? '';
    this.loadBureaux();
    this.load();
  }

  loadBureaux(): void {
    if (!this.isAdmin) return; // seul l'admin voit la liste de tous les bureaux
    this.loadingBureaux = true;
    this.agenceService.getAll('', 0, 200).subscribe({
      next: r => {
        this.bureaux = r.data?.content ?? [];
        this.loadingBureaux = false;
      },
      error: () => { this.loadingBureaux = false; }
    });
  }

  load(): void {
    this.isLoading = true;
    // Admin sans officeCode sélectionné → charge toutes les alertes
    const obs = (this.isAdmin && !this.officeCode)
      ? this.stockService.getAlertes()
      : this.stockService.getStocksParBureau(this.officeCode);
    obs.subscribe({
      next: (r: any) => {
        const data = r.data ?? [];
        // getAlertes retourne un tableau plat, getStocksParBureau aussi
        this.stocks    = Array.isArray(data) ? data : (data.content ?? []);
        this.filter();
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Erreur lors du chargement des stocks.';
        this.isLoading    = false;
      }
    });
  }

  filter(): void {
    const t = this.searchTerm.toLowerCase();
    this.filtered = this.stocks.filter(s => {
      const matchSearch = !t ||
        s.officeCode?.toLowerCase().includes(t) ||
        s.officeName?.toLowerCase().includes(t) ||
        s.certTypeCode?.toLowerCase().includes(t) ||
        s.certTypeName?.toLowerCase().includes(t);
      const matchStatut = !this.filterStatut || s.statut === this.filterStatut;
      return matchSearch && matchStatut;
    });
  }

  // ── Approvisionnement ────────────────────────────────────────

  openAppro(s: StockAttestation): void {
    this.selectedStock = s;
    this.approForm = {
      quantite: 0,
      certTypeCode:    s.certTypeCode    ?? undefined,
      certVariantCode: s.certVariantCode ?? undefined,
      referenceSource: '',
      motif: ''
    };
    this.showApproModal = true;
  }

  saveAppro(): void {
    if (!this.approForm.quantite || this.approForm.quantite <= 0) return;
    this.savingAppro = true;
    this.stockService.approvisionner(this.selectedStock!.officeCode, this.approForm).subscribe({
      next: r => {
        this.updateStockInList(r.data);
        this.closeModals();
        this.savingAppro = false;
      },
      error: () => { this.savingAppro = false; }
    });
  }

  // ── Ajustement ───────────────────────────────────────────────

  openAjust(s: StockAttestation): void {
    this.selectedStock = s;
    this.ajustForm = {
      delta: 0,
      certTypeCode:    s.certTypeCode    ?? undefined,
      certVariantCode: s.certVariantCode ?? undefined,
      motif: ''
    };
    this.showAjustModal = true;
  }

  saveAjust(): void {
    if (!this.ajustForm.delta || this.ajustForm.delta === 0) return;
    this.savingAjust = true;
    this.stockService.ajuster(this.selectedStock!.officeCode, this.ajustForm).subscribe({
      next: r => {
        this.updateStockInList(r.data);
        this.closeModals();
        this.savingAjust = false;
      },
      error: () => { this.savingAjust = false; }
    });
  }

  // ── Init nouveau stock ───────────────────────────────────────

  ouvrirInit(): void {
    this.initForm = {
      bureauSelectionne: null,
      certTypeCode:     '',
      certTypeName:     '',
      certVariantCode:  '',
      certVariantName:  '',
      quantiteInitiale: 0,
      seuilAlerte:      50,
      seuilCritique:    10,
      motif:            ''
    };
    this.showInitModal = true;
  }

  saveInit(): void {
    if (!this.initForm.bureauSelectionne) return;
    this.savingInit = true;
    const bureau          = this.initForm.bureauSelectionne;
    const officeCodeCible = bureau.officeCode ?? String(bureau.codeAgence);
    this.stockService.initierStock({
      officeCode:       officeCodeCible,
      officeName:       bureau.libelleAgence ?? '',
      orgCode:          this.authService.currentUserValue?.agencyCode ?? '',
      certTypeCode:     this.initForm.certTypeCode  || null,
      certTypeName:     this.initForm.certTypeName  || null,
      certVariantCode:  this.initForm.certVariantCode || null,
      certVariantName:  this.initForm.certVariantName || null,
      quantiteInitiale: this.initForm.quantiteInitiale,
      seuilAlerte:      this.initForm.seuilAlerte,
      seuilCritique:    this.initForm.seuilCritique,
      motif:            this.initForm.motif,
    }).subscribe({
      next: () => {
        this.closeModals();
        this.savingInit = false;
        // Recharge depuis le serveur pour avoir la liste exacte, sans doublon
        // Non-admin : on bascule sur le bureau créé pour l'afficher immédiatement
        if (!this.isAdmin) this.officeCode = officeCodeCible;
        this.load();
      },
      error: () => { this.savingInit = false; }
    });
  }

  // ── Helpers ──────────────────────────────────────────────────

  closeModals(): void {
    this.showApproModal = false;
    this.showAjustModal = false;
    this.showInitModal  = false;
    this.selectedStock  = null;
  }

  private updateStockInList(updated: StockAttestation): void {
    const idx = this.stocks.findIndex(s => s.id === updated.id);
    if (idx >= 0) { this.stocks[idx] = updated; this.filter(); }
  }

  pct(s: StockAttestation): number      { return this.stockService.pourcentageStock(s); }
  pctColor(s: StockAttestation): string { return this.stockService.progressColor(this.pct(s)); }
  statutColor(st: string): string       { return this.stockService.statutColor(st); }
  statutIcon(st: string): string        { return this.stockService.statutIcon(st); }

  labelStock(s: StockAttestation): string {
    if (!s.certTypeCode) return 'Toutes attestations (global)';
    return s.certTypeName ?? s.certTypeCode;
  }

  badgeVariant(s: StockAttestation): string {
    return s.certVariantCode ? (s.certVariantName ?? s.certVariantCode) : '';
  }

  nomBureauSelectionne(): string {
    const b = this.initForm.bureauSelectionne;
    if (!b) return '';
    return `${b.libelleAgence} (${b.officeCode ?? b.codeAgence})`;
  }
}
