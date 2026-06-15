import {
  Component, Input, Output, EventEmitter,
  OnChanges, SimpleChanges, HostListener
} from '@angular/core';
import { CommonModule }      from '@angular/common';
import { CertificateService } from '../../../core/services/certificate.service';
import { ApiResponse }        from '../../../core/model/ApiResponse';
import { ProductionData }     from '../../../core/model/ProductionData';
import { CertificateInfo }    from '../../../core/model/CertificateInfo';

@Component({
  selector: 'app-certificate-preview',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './certificate-preview.component.html',
  styleUrl: './certificate-preview.component.css'
})
export class CertificatePreviewComponent implements OnChanges {

  @Input()  production: ApiResponse<ProductionData> | null = null;
  @Input()  visible     = false;
  @Output() closeEvent  = new EventEmitter<void>();

  // Index du certificat affiché dans le carousel
  activeIndex = 0;

  // États chargement
  downloadingRef: string | null = null;
  downloadingAll = false;

  // Cache des URL d'images déjà chargées (évite refetch)
  imageUrls: Record<string, string> = {};
  imageErrors: Record<string, boolean> = {};

  get prod(): any { return this.production?.data; }

  get certs(): CertificateInfo[] {
    return this.production?.data?.certificates ?? [];
  }

  get activeCert(): CertificateInfo | null {
    return this.certs[this.activeIndex] ?? null;
  }

  constructor(private certificateService: CertificateService) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['visible']?.currentValue === true) {
      this.activeIndex  = 0;
      this.imageUrls    = {};
      this.imageErrors  = {};
      document.body.classList.add('modal-open');
    } else if (changes['visible']?.currentValue === false) {
      document.body.classList.remove('modal-open');
    }
  }

  @HostListener('document:keydown.escape')
  onEscape(): void {
    if (this.visible) this.close();
  }

  close(): void {
    document.body.classList.remove('modal-open');
    this.closeEvent.emit();
  }

  prev(): void {
    if (this.activeIndex > 0) this.activeIndex--;
  }

  next(): void {
    if (this.activeIndex < this.certs.length - 1) this.activeIndex++;
  }

  goTo(i: number): void {
    this.activeIndex = i;
  }

  // ── Impression d'UN certificat ───────────────────────────────────
  printSingle(cert: CertificateInfo): void {
    if (!cert.download_link) return;
    const win = window.open('', '_blank', 'width=900,height=700');
    if (!win) return;
    win.document.write(`
      <!DOCTYPE html>
      <html>
      <head>
        <title>Attestation ${cert.reference}</title>
        <style>
          * { margin: 0; padding: 0; box-sizing: border-box; }
          body { display: flex; justify-content: center; align-items: flex-start; }
          img { max-width: 100%; height: auto; display: block; }
          @media print {
            body { margin: 0; }
            img { width: 100%; page-break-after: avoid; }
          }
        </style>
      </head>
      <body>
        <img src="${cert.download_link}" onload="window.print();window.close();"
             onerror="document.body.innerHTML='<p>Impossible de charger l\\'image</p>'"/>
      </body>
      </html>
    `);
    win.document.close();
  }

  // ── Impression de TOUTES les attestations ───────────────────────
  printAll(): void {
    const certs = this.certs.filter(c => c.download_link);
    if (!certs.length) return;

    const imgTags = certs.map(c =>
      `<div class="page"><img src="${c.download_link}" /><p class="ref">${c.reference} — ${c.police_number ?? ''}</p></div>`
    ).join('');

    const win = window.open('', '_blank', 'width=900,height=700');
    if (!win) return;
    win.document.write(`
      <!DOCTYPE html>
      <html>
      <head>
        <title>Attestations — ${this.production?.data?.reference}</title>
        <style>
          * { margin: 0; padding: 0; box-sizing: border-box; }
          body { font-family: Arial, sans-serif; }
          .page { page-break-after: always; text-align: center; padding: 8px; }
          .page:last-child { page-break-after: avoid; }
          img { max-width: 100%; height: auto; }
          .ref { font-size: 11px; color: #555; margin-top: 4px; }
          @media print { .page { padding: 0; } }
        </style>
      </head>
      <body>
        ${imgTags}
        <script>
          let loaded = 0;
          const imgs = document.querySelectorAll('img');
          const total = imgs.length;
          imgs.forEach(img => {
            if (img.complete) { loaded++; if(loaded===total) window.print(); }
            else {
              img.onload  = () => { loaded++; if(loaded===total){ window.print(); } };
              img.onerror = () => { loaded++; if(loaded===total){ window.print(); } };
            }
          });
          if (total === 0) window.print();
        </script>
      </body>
      </html>
    `);
    win.document.close();
  }

  // ── Téléchargement PDF individuel ───────────────────────────────
  async downloadPdf(cert: CertificateInfo): Promise<void> {
    if (!cert.download_link) return;
    this.downloadingRef = cert.reference;
    try {
      await this.certificateService.downloadSingleCertAsPdf(cert.download_link, cert.reference);
    } catch (e) {
      console.error(e);
    } finally {
      this.downloadingRef = null;
    }
  }

  // ── Téléchargement PDF fusionné ──────────────────────────────────
  async downloadAllPdf(): Promise<void> {
    const certs = this.certs.map(c => ({ download_link: c.download_link, reference: c.reference }));
    if (!certs.length) return;
    this.downloadingAll = true;
    try {
      await this.certificateService.downloadProductionAsPdf(certs, this.production?.data?.reference ?? 'production');
    } catch (e) {
      console.error(e);
    } finally {
      this.downloadingAll = false;
    }
  }

  onImageError(ref: string): void {
    this.imageErrors[ref] = true;
  }
}
