import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
import { CertificateService } from '../../../core/services/certificate.service';

interface Motif { code: string; typeAction: string; libelle: string; }
interface CertInfo { reference: string; policeNumber: string; licencePlate: string; chassisNumber: string; insuredName: string; insuredPhone: string; certTypeName: string; certVariantName: string; startsAt: string; endsAt: string; stateLabel: string; }

@Component({
  selector: 'app-suspension',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslatePipe],
  templateUrl: './suspension.component.html',
  styleUrl: './suspension.component.css'
})
export class SuspensionComponent implements OnInit, OnDestroy {

  searchType: 'REFERENCE' | 'POLICE' | 'PLATE' = 'REFERENCE';
  searchValue = '';
  plateValue  = '';
  resolvedReference = '';

  motifCode  = '';
  motifs:    Motif[] = [];
  historique: any[] = [];
  certInfo:  CertInfo | null = null;

  // Prévisualisation live
  previewCert:  CertInfo | null = null;
  fleetCerts:   CertInfo[] = [];   // liste flotte quand police sans plaque
  isSearching   = false;
  previewError  = '';
  private debounceTimer: any = null;

  // Filtre historique
  histFilter = '';

  isLoading    = false;
  isLoadingInfo = false;
  isConfirming = false;
  successMsg   = '';
  errorMsg     = '';
  infoError    = '';

  constructor(private certService: CertificateService) {}

  ngOnInit(): void {
    this.certService.getMotifs('SUSPENSION').subscribe({
      next: m  => this.motifs = m,
      error: () => this.errorMsg = 'SUSPENSION.ERROR_LOAD_MOTIFS'
    });
    this.loadHistorique();
  }

  ngOnDestroy(): void {
    if (this.debounceTimer) clearTimeout(this.debounceTimer);
  }

  loadHistorique(): void {
    this.certService.getHistoriqueByOfficeAndType('SUSPENSION').subscribe({
      next: h => this.historique = h,
      error: () => {}
    });
  }

  onSearchTypeChange(): void {
    this.searchValue       = '';
    this.plateValue        = '';
    this.resolvedReference = '';
    this.certInfo          = null;
    this.previewCert       = null;
    this.fleetCerts        = [];
    this.previewError      = '';
    this.infoError         = '';
    if (this.debounceTimer) clearTimeout(this.debounceTimer);
  }

  onSearchInput(): void {
    this.previewCert  = null;
    this.fleetCerts   = [];
    this.previewError = '';
    this.resolvedReference = '';
    if (this.debounceTimer) clearTimeout(this.debounceTimer);
    const val = this.searchValue.trim();
    if (val.length < 3) return;

    this.isSearching = true;
    this.debounceTimer = setTimeout(() => {
      // Police sans plaque → charger toute la flotte pour sélection
      if (this.searchType === 'POLICE' && !this.plateValue.trim()) {
        this.certService.getFleetByPolice(val).subscribe({
          next: list => {
            if (list.length === 1) {
              this.previewCert = list[0];
              this.resolvedReference = list[0].reference;
            } else {
              this.fleetCerts = list;
            }
            this.isSearching = false;
          },
          error: () => { this.previewError = 'CONFIRM.INFO_NOT_FOUND'; this.isSearching = false; }
        });
      } else {
        this.certService.searchCertificateInfo(this.searchType, val, this.plateValue || undefined).subscribe({
          next: info => {
            this.previewCert = info;
            this.resolvedReference = info.reference;
            this.isSearching = false;
          },
          error: () => { this.previewError = 'CONFIRM.INFO_NOT_FOUND'; this.isSearching = false; }
        });
      }
    }, 600);
  }

  selectFromFleet(cert: CertInfo): void {
    this.previewCert       = cert;
    this.fleetCerts        = [];
    this.resolvedReference = cert.reference;
    this.plateValue        = cert.licencePlate;
  }

  get stateWarning(): string {
    const state = (this.previewCert?.stateLabel ?? '').toLowerCase();
    if (state.includes('suspendu') || state.includes('suspended')) return 'SUSPENSION.ALREADY_SUSPENDED';
    return '';
  }

  get filteredHistorique(): any[] {
    if (!this.histFilter.trim()) return this.historique;
    const f = this.histFilter.toLowerCase();
    return this.historique.filter(h =>
      h.certificateReference?.toLowerCase().includes(f) ||
      h.motifLibelle?.toLowerCase().includes(f) ||
      h.usernameExecutant?.toLowerCase().includes(f)
    );
  }

  get canSubmit(): boolean {
    if (!this.motifCode) return false;
    if (!this.searchValue.trim()) return false;
    if (this.searchType === 'POLICE' && !this.plateValue.trim() && !this.resolvedReference) return false;
    return true;
  }

  openConfirm(): void {
    if (!this.canSubmit) return;
    this.successMsg        = '';
    this.errorMsg          = '';
    this.infoError         = '';
    this.certInfo          = null;
    this.isLoadingInfo     = true;

    if (this.previewCert && this.resolvedReference) {
      this.certInfo      = this.previewCert;
      this.isLoadingInfo = false;
      this.isConfirming  = true;
      return;
    }

    this.certService.searchCertificateInfo(this.searchType, this.searchValue, this.plateValue || undefined).subscribe({
      next: info => {
        this.certInfo          = info;
        this.resolvedReference = info.reference;
        this.isLoadingInfo     = false;
        this.isConfirming      = true;
      },
      error: () => {
        this.isLoadingInfo = false;
        this.infoError     = 'CONFIRM.INFO_NOT_FOUND';
        this.isConfirming  = true;
      }
    });
  }

  cancelConfirm(): void { this.isConfirming = false; }

  confirm(): void {
    if (!this.resolvedReference) return;
    this.isLoading = true;
    this.certService.suspendCertificate(this.resolvedReference, this.motifCode).subscribe({
      next: () => {
        this.isLoading         = false;
        this.isConfirming      = false;
        this.successMsg        = 'SUSPENSION.SUCCESS';
        this.searchValue       = '';
        this.plateValue        = '';
        this.resolvedReference = '';
        this.motifCode         = '';
        this.certInfo          = null;
        this.previewCert       = null;
        this.fleetCerts        = [];
        this.loadHistorique();
      },
      error: err => {
        this.isLoading    = false;
        this.isConfirming = false;
        this.errorMsg     = err?.error?.message ?? err?.message ?? 'SUSPENSION.ERROR_API';
      }
    });
  }

  get selectedMotifLibelle(): string {
    return this.motifs.find(m => m.code === this.motifCode)?.libelle ?? '';
  }
}
