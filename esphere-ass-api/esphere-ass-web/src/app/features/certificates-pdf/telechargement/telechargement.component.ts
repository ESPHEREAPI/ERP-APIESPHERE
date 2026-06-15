import { CommonModule } from '@angular/common';
import { TranslatePipe } from '@ngx-translate/core';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CertificateService } from '../../../core/services/certificate.service';
import { Toast } from 'ngx-toastr';
import { ApiResponse } from '../../../core/model/ApiResponse';
import { ProductionData } from '../../../core/model/ProductionData';
interface AppToast {
  message: string;
  type: 'success' | 'error' | 'info';
}

@Component({
  selector: 'app-telechargement',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, TranslatePipe],
  templateUrl: './telechargement.component.html',
  styleUrl: './telechargement.component.css'
})
export class TelechargementComponent implements OnInit {
 
  // ── Données ──────────────────────────────────────────────────
  productions:         ApiResponse<ProductionData>[] = [];
  filteredProductions: any[] = [];
  searchTerm          = '';
  isLoading           = true;
  downloadingAll      = false;
  downloadingCount    = 0;
  totalCertificates   = 0;
  toasts: AppToast[] = [];

 
  // ── État accordéon : openProductions[i] = true/false ─────────
  openProductions: boolean[] = [];
 
  // ── État téléchargement par cellule [prodIndex][certIndex] ───
  // Ex: downloadingMap['0-1'] = true → prod 0, cert 1 en cours
  downloadingMap: Record<string, boolean> = {};
 
  // ── Toasts ───────────────────────────────────────────────────
  //: Toast[] = [];
 
  constructor(private certificateService: CertificateService) {}
 
  ngOnInit(): void {
    this.loadData();
  }
 
  // ════════════════════════════════════════════════════════════
  // CHARGEMENT DES DONNÉES
  // ════════════════════════════════════════════════════════════
  loadData(): void {
    this.isLoading = true;
 
    this.certificateService.getAllProductions().subscribe({
      next: (data) => {
        this.productions         = data;
        this.filteredProductions = data;
        console.log(data);
 
        // Ouvre la première production par défaut
        this.openProductions = data.map((_: any, i: number) => i === 0);
 
        // Compte total des certificats
        this.totalCertificates = data.reduce(
          (sum: number, p: any) =>
            sum + (p.data?.certificates?.length || 0), 0
        );
 
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.showToast('Erreur lors du chargement des données', 'error');
      }
    });
  }
 
  // ════════════════════════════════════════════════════════════
  // FILTRAGE / RECHERCHE
  // ════════════════════════════════════════════════════════════
  filterCertificates(): void {
    const term = this.searchTerm.toLowerCase().trim();
 
    if (!term) {
      this.filteredProductions = this.productions;
      return;
    }
 
    // Filtre les productions qui ont au moins un certificat correspondant
    this.filteredProductions = this.productions
      .map(prod => {
        const filteredCerts = prod.data?.certificates?.filter((cert: any) =>
          cert.reference?.toLowerCase().includes(term)       ||
          cert.insured_name?.toLowerCase().includes(term)    ||
          cert.licence_plate?.toLowerCase().includes(term)   ||
          cert.police_number?.toLowerCase().includes(term)
        );
 
        // Retourne la production avec seulement les certificats filtrés
        return {
          ...prod,
          data: { ...prod.data, certificates: filteredCerts }
        };
      })
      .filter(prod => prod.data?.certificates?.length > 0);
 
    // Ouvre tous les résultats si recherche active
    if (term) {
      this.openProductions = this.filteredProductions.map(() => true);
    }
  }
 
  // ════════════════════════════════════════════════════════════
  // ACCORDÉON
  // ════════════════════════════════════════════════════════════
  toggleProduction(index: number): void {
    this.openProductions[index] = !this.openProductions[index];
  }
 
  // ════════════════════════════════════════════════════════════
  // TÉLÉCHARGEMENTS
  // ════════════════════════════════════════════════════════════
 
  /**
   * Télécharge un certificat via notre API backend (BLOB Oracle)
   * @param cert      Le certificat à télécharger
   * @param prodIndex Index de la production (pour le spinner)
   * @param certIndex Index du certificat dans la production
   */
  downloadFromApi(cert: any, prodIndex: number, certIndex: number): void {
    // ✅ Garde contre undefined
  if (!cert?.reference) {
    this.showToast(`ID manquant pour ${cert?.reference}`, 'error');
    console.error('cert.id est undefined :', cert);
    return;
  } 
    const key = `${prodIndex}-${certIndex}`;
    this.downloadingMap[key] = true;
    this.downloadingCount++;
 
    this.certificateService.downloadPdf(cert.reference).subscribe({
      next: (blob: Blob) => {
        // Crée un lien temporaire et déclenche le téléchargement
        this.triggerDownload(blob, `${cert.reference}.pdf`);
        this.downloadingMap[key] = false;
        this.downloadingCount    = Math.max(0, this.downloadingCount - 1);
        this.showToast(`PDF ${cert.reference} téléchargé`, 'success');
      },
      error: () => {
        this.downloadingMap[key] = false;
        this.downloadingCount    = Math.max(0, this.downloadingCount - 1);
        this.showToast(
          `Erreur téléchargement ${cert.reference}`, 'error'
        );
      }
    });
  }
 
  /**
   * Télécharge tous les PDFs d'une production
   * Décale chaque téléchargement de 400ms pour éviter le blocage navigateur
   */
  downloadProduction(prod: any, event: Event): void {
    // Empêche le toggle de l'accordéon
    event.stopPropagation();
 
    const certs = prod.data?.certificates || [];
    this.showToast(
      `Téléchargement de ${certs.length} certificat(s)...`, 'info'
    );
 
    certs.forEach((cert: any, i: number) => {
      setTimeout(() => {
        // Utilise le lien direct de l'API externe
        const link    = document.createElement('a');
        link.href     = cert.download_link;
        link.target   = '_blank';
        link.download = `${cert.reference}.pdf`;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      }, i * 400); // 400ms entre chaque pour éviter le blocage
    });
  }
 
  /**
   * Télécharge TOUS les PDFs de toutes les productions
   */
  downloadAll(): void {
    this.downloadingAll = true;
 
    let delay = 0;
    let total = 0;
 
    this.productions.forEach(prod => {
      const certs = prod.data?.certificates || [];
      certs.forEach((cert: any) => {
        setTimeout(() => {
          const link    = document.createElement('a');
          link.href     = cert.download_link;
          link.target   = '_blank';
          link.download = `${cert.reference}.pdf`;
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
          total++;
        }, delay);
        delay += 400;
      });
    });
 
    // Réinitialise après tous les téléchargements
    setTimeout(() => {
      this.downloadingAll = false;
      this.showToast(
        `${this.totalCertificates} PDF(s) téléchargés`, 'success'
      );
    }, delay + 500);
  }
 
  // ════════════════════════════════════════════════════════════
  // UTILITAIRES
  // ════════════════════════════════════════════════════════════
 
  /**
   * Vérifie si un certificat est en cours de téléchargement
   */
  isDownloading(prodIndex: number, certIndex: number): boolean {
    return !!this.downloadingMap[`${prodIndex}-${certIndex}`];
  }
 
  /**
   * Déclenche le téléchargement d'un Blob dans le navigateur
   */
  private triggerDownload(blob: Blob, filename: string): void {
    const url  = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href     = url;
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    // Libère la mémoire après 1s
    setTimeout(() => window.URL.revokeObjectURL(url), 1000);
  }
 
  /**
   * Affiche un toast et le supprime après 3 secondes
   */
  showToast(message: string, type: 'success' | 'error' | 'info'): void {
  const toast: AppToast = { message, type };
  this.toasts.push(toast);

  setTimeout(() => {
    this.toasts = this.toasts.filter(t => t !== toast);
  }, 3000);
}

}
