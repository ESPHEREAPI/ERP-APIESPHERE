import { Component, OnInit } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { CertificateService } from '../../../core/services/certificate.service';
import { CertificatePreviewComponent } from '../certificate-preview/certificate-preview.component';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ApiResponse } from '../../../core/model/ApiResponse';
import { ProductionData } from '../../../core/model/ProductionData';

@Component({
  selector: 'app-certificate-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, CertificatePreviewComponent, TranslatePipe],
  templateUrl: './certificate-list.component.html',
  styleUrl: './certificate-list.component.css'
})
export class CertificateListComponent  implements OnInit {
 
  productions:         any[] = [];
  filteredProductions: any[] = [];
  selectedProduction:  any   = null;
  isLoading    = true;
  showModal    = false;
  errorMessage = '';
  searchTerm   = '';
 
  // Pagination
  currentPage = 1;
  pageSize    = 10;
  totalPages  = 1;
  pages:      number[] = [];
 
  // Tri
  sortField = '';
  sortAsc   = true;

  // États téléchargement
  downloadingCertRef: string | null = null;
  downloadingProdRef: string | null = null;

  // Prévisualisation / impression
  previewProduction: any = null;
  showPreview = false;
 
  constructor(private certificateService: CertificateService) {}
 
  ngOnInit(): void { this.loadProductions(); }
 
  loadProductions(): void {
    this.isLoading = true;
    this.certificateService.getAllProductions().subscribe({
      next: (data) => {
        this.productions         = data;
        this.filteredProductions = data;
        this.calcPagination();
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Erreur lors du chargement des productions.';
        this.isLoading    = false;
      }
    });
  }
 
  filterProductions(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredProductions = this.productions.filter(p => {
      if (!term) return true;
      // Recherche sur référence, agence, assuré ET tous les n° de police
      const policeMatch = (p.data?.certificates ?? []).some(
        (c: any) => c.police_number?.toLowerCase().includes(term)
      );
      return (
        p.data?.reference?.toLowerCase().includes(term) ||
        p.data?.office?.name?.toLowerCase().includes(term) ||
        p.data?.certificates?.[0]?.insured_name?.toLowerCase().includes(term) ||
        policeMatch
      );
    });
    this.currentPage = 1;
    this.calcPagination();
  }
 
  sort(field: string): void {
    this.sortAsc   = this.sortField === field ? !this.sortAsc : true;
    this.sortField = field;
    this.filteredProductions.sort((a, b) => {
      const va = a.data?.[field] || '';
      const vb = b.data?.[field] || '';
      return this.sortAsc
        ? va.localeCompare(vb)
        : vb.localeCompare(va);
    });
  }
 
  calcPagination(): void {
    this.totalPages = Math.ceil(this.filteredProductions.length / this.pageSize);
    this.pages      = Array.from({ length: this.totalPages }, (_, i) => i + 1);
  }
 
  changePage(p: number): void {
    if (p >= 1 && p <= this.totalPages) this.currentPage = p;
  }
 
  viewDetails(prod: any): void {
    this.selectedProduction = prod;
    this.showModal          = true;
  }

  closeModal(): void {
    this.showModal          = false;
    this.selectedProduction = null;
  }

  openPreview(prod: any): void {
    this.previewProduction = prod;
    this.showPreview       = true;
  }

  closePreview(): void {
    this.showPreview       = false;
    this.previewProduction = null;
  }

  /** Retourne le premier n° de police d'une production */
  getFirstPolice(prod: any): string {
    return prod.data?.certificates?.[0]?.police_number || '—';
  }

  /** Retourne tous les n° de police distincts (pour le tooltip) */
  getAllPolice(prod: any): string {
    const certs: any[] = prod.data?.certificates ?? [];
    return certs.map((c: any) => c.police_number).filter(Boolean).join(', ') || '—';
  }
 
  /** Télécharge un seul certificat converti en PDF */
  async downloadSingleCert(cert: any): Promise<void> {
    if (!cert?.download_link) return;
    this.downloadingCertRef = cert.reference;
    try {
      await this.certificateService.downloadSingleCertAsPdf(
        cert.download_link,
        cert.reference
      );
    } catch (err) {
      console.error('Erreur téléchargement individuel :', err);
    } finally {
      this.downloadingCertRef = null;
    }
  }

  /** Télécharge toutes les attestations d'une production fusionnées en 1 PDF */
  async downloadAllCertificates(prod: ApiResponse<ProductionData>): Promise<void> {
    const certs = (prod?.data?.certificates ?? []).map((c: any) => ({
      download_link: c.download_link,
      reference:     c.reference,
    }));
    if (!certs.length) return;

    this.downloadingProdRef = prod?.data?.reference ?? 'production';
    try {
      await this.certificateService.downloadProductionAsPdf(
        certs,
        prod?.data?.reference ?? 'production'
      );
    } catch (err) {
      console.error('Erreur téléchargement production :', err);
    } finally {
      this.downloadingProdRef = null;
    }
  }
}
