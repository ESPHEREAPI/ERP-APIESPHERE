import { Component, OnInit } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { UserSession } from '../../../core/model/user-session';
import { AuthService } from '../../../core/auth/auth.service';
import { CertificateService } from '../../../core/services/certificate.service';
import { CertificatePreviewComponent } from '../../certificates/certificate-preview/certificate-preview.component';
import { ProductionData } from '../../../core/model/ProductionData';
import { ApiResponse } from '../../../core/model/ApiResponse';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-dashboard-payload',
  standalone: true,
  imports: [CommonModule, RouterModule, CertificatePreviewComponent, TranslatePipe],
  templateUrl: './dashboard-payload.component.html',
  styleUrl: './dashboard-payload.component.css'
})
export class DashboardPayloadComponent implements OnInit {

  session: UserSession | null = null;
  isLoading = true;

  // ── Dialog détail ───────────────────────────────────────────
  selectedProduction: ProductionData | null = null;
  showDetailModal    = false;

  // ── Prévisualisation / impression ───────────────────────────
  previewProduction: ApiResponse<ProductionData> | null = null;
  showPreview = false;

  // ── États téléchargement ────────────────────────────────────
  downloadingCertRef: string | null = null;
  downloadingProd    = false;

  stats = {
    totalProductions:  0,
    totalCertificates: 0,
    pdfDownloaded:     0,
    todayProductions:  0,
    stockRemaining:    450,
    stockPercent:      55,
    yellowCerts:       0,
    greenCerts:        0,
  };

  // Chaque élément est un ProductionData (déjà extrait de ApiResponse.data)
  recentProductions: ProductionData[] = [];

  constructor(
    private authService:        AuthService,
    private certificateService: CertificateService
  ) {}

  ngOnInit(): void {
    this.session = this.authService.currentUserValue;
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.isLoading = true;

    this.certificateService.getAllProductions().subscribe({
      next: (responses) => {
        // Extraire ProductionData depuis chaque ApiResponse<ProductionData>
        const productions: ProductionData[] = responses
          .filter(r => r.data != null)
          .map(r => r.data);

        this.recentProductions = productions.slice(0, 8);

        this.stats.totalProductions  = productions.length;
        this.stats.totalCertificates = productions.reduce(
          (sum, p) => sum + (p.certificates?.length ?? 0), 0
        );
        this.stats.pdfDownloaded = this.stats.totalCertificates;

        const todayStr = new Date().toLocaleDateString('fr-FR'); // "DD/MM/YYYY"
        this.stats.todayProductions = productions.filter(p =>
          p.formatted_created_at?.startsWith(todayStr)
        ).length;

        this.stats.yellowCerts = this.stats.totalCertificates;
        this.stats.greenCerts  = 0;

        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  // Retourne le nom de l'assuré depuis le premier certificat
  getInsuredName(prod: ProductionData): string {
    return prod.certificates?.[0]?.insured_name ?? '—';
  }

  // Retourne la plaque du premier certificat
  getLicencePlate(prod: ProductionData): string {
    return prod.certificates?.[0]?.licence_plate ?? '—';
  }

  openDetail(prod: ProductionData): void {
    this.selectedProduction = prod;
    this.showDetailModal = true;
    document.body.classList.add('modal-open');
  }

  closeDetail(): void {
    this.showDetailModal = false;
    this.selectedProduction = null;
    document.body.classList.remove('modal-open');
  }

  /** Ouvre le composant preview — enveloppe ProductionData dans ApiResponse */
  openPreview(prod: ProductionData): void {
    this.previewProduction = { status: 200, message: '', data: prod };
    this.showPreview = true;
  }

  closePreview(): void {
    this.showPreview = false;
    this.previewProduction = null;
  }

  /** Alias any pour éviter les erreurs strict-template sur les champs non-optionnels */
  get prod(): any { return this.selectedProduction; }

  /** Télécharge un certificat individuel (image → PDF via pdf-lib) */
  async downloadCertByRef(cert: { download_link: string; reference: string }): Promise<void> {
    if (!cert?.download_link) return;
    this.downloadingCertRef = cert.reference;
    try {
      await this.certificateService.downloadSingleCertAsPdf(
        cert.download_link,
        cert.reference
      );
    } catch (err) {
      console.error('Erreur téléchargement certificat :', err);
    } finally {
      this.downloadingCertRef = null;
    }
  }

  /** Télécharge toutes les attestations de la production sélectionnée → 1 PDF fusionné */
  async downloadProductionById(prod: ProductionData): Promise<void> {
    const certs = (prod?.certificates ?? []).map(c => ({
      download_link: c.download_link,
      reference:     c.reference,
    }));
    if (!certs.length) return;

    this.downloadingProd = true;
    try {
      await this.certificateService.downloadProductionAsPdf(certs, prod.reference);
    } catch (err) {
      console.error('Erreur téléchargement production :', err);
    } finally {
      this.downloadingProd = false;
    }
  }
}
