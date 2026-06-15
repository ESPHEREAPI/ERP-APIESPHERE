// ╔══════════════════════════════════════════════════════════════╗
// ║  certificate.service.ts                                      ║
// ║  src/app/certificates/certificate.service.ts                 ║
// ║  Service Angular — Appels HTTP vers Spring Boot              ║
// ║  Utilisé par : Dashboard, Liste, Formulaire                  ║
// ╚══════════════════════════════════════════════════════════════╝
import { Injectable }                    from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError }        from 'rxjs';
import { catchError }                    from 'rxjs/operators';
import { PDFDocument }                   from 'pdf-lib';
import { ProductionData } from '../model/ProductionData';
import { ApiResponse } from '../model/ApiResponse';
import { InsuranceCertificateRequest } from '../model/InsuranceCertificateRequest';
import { ValidationErrorResponse } from '../model/ValidationErrorResponse';
import { environment } from '../../../environments/environment';
import { AuthService } from '../auth/auth.service';



@Injectable({ providedIn: 'root' })
export class CertificateService {

  // URL backend Spring Boot
  //private readonly apiUrl = 'http://localhost:8080/api/v1/certificates';
    private readonly apiUrl = `${environment.apiUrl}/gateway-proxy/api/esphere-ass-microservice-admin/certificates`;

  constructor(private http: HttpClient,private authService:AuthService) {}

  // ════════════════════════════════════════════════════════════════
  // MÉTHODE 1 — POST /api/v1/certificates
  // Créer une production → Spring Boot → API eattestation.cm
  // Appelée par : CertificateFormComponent.onSubmit()
  // ════════════════════════════════════════════════════════════════
  createCertificate(request: InsuranceCertificateRequest): Observable<ApiResponse<ProductionData>> {
    console.log(request)
    const username=this.authService.getUserFromStorage()?.userapiasac;
    return this.http
      .post<ApiResponse<ProductionData>>(`${this.apiUrl}/${username}`, request)
      .pipe(
        catchError((err: HttpErrorResponse) => {
          // Spring Boot renvoie notre ValidationErrorResponse (422/401/403)
          const error: ValidationErrorResponse = err.error;
          return throwError(() => error);
        })
      );
  }

  // ════════════════════════════════════════════════════════════════
  // MÉTHODE 2 — GET /api/v1/certificatesa
  // Appelée par : DashboardComponent + CertificateListComponent
  // ════════════════════════════════════════════════════════════════
  getAllProductions(): Observable<ApiResponse<ProductionData>[]> {
     const codeagence=this.authService.getUserFromStorage()?.agencyCode;
    return this.http
      .get<ApiResponse<ProductionData>[]>(`${this.apiUrl}/all/${codeagence}`)
      .pipe(
        catchError((err: HttpErrorResponse) =>
          throwError(() => err.error || err)
        )
      );
  }

  // ════════════════════════════════════════════════════════════════
  // MÉTHODE 3 — GET /api/v1/certificates/{id}
  // Une production par ID Oracle
  // Appelée par : vue détail
  // ════════════════════════════════════════════════════════════════
  getById(id: number): Observable<ApiResponse<ProductionData>> {
    return this.http
      .get<ApiResponse<ProductionData>>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError((err: HttpErrorResponse) =>
          throwError(() => err.error || err)
        )
      );
  }

  // ════════════════════════════════════════════════════════════════
  // MÉTHODE 4 — GET /api/v1/certificates/{id}/download
  // Télécharge le PDF stocké en BLOB Oracle
  // Appelée par : Dashboard + Liste + Modal
  // ════════════════════════════════════════════════════════════════
  downloadPdf(reference: string): Observable<Blob> {
     // ✅ Évite d'appeler /certificates/undefined/download
  if (!reference) {
    return throwError(() => new Error('ID certificat invalide'));
  }
    return this.http
      .get(`${this.apiUrl}/ref/${reference}/download`, {
        responseType: 'blob',                      // binaire PDF
        headers: { Accept: 'application/pdf' }
      })
      .pipe(
        catchError((err: HttpErrorResponse) =>
          throwError(() => err.error || err)
        )
      );
  }

    downloadPdff(id: number): Observable<Blob> {
     // ✅ Évite d'appeler /certificates/undefined/download
  if (!id) {
    return throwError(() => new Error('ID certificat invalide'));
  }
    return this.http
      .get(`${this.apiUrl}/${id}/download`, {
        responseType: 'blob',                      // binaire PDF
        headers: { Accept: 'application/pdf' }
      })
      .pipe(
        catchError((err: HttpErrorResponse) =>
          throwError(() => err.error || err)
        )
      );
  }

  // ════════════════════════════════════════════════════════════════
  // UTILITAIRE — Déclenche le téléchargement dans le navigateur
  // Usage : this.certificateService.triggerDownload(blob, 'cert.pdf')
  // ════════════════════════════════════════════════════════════════
  triggerDownload(blob: Blob, filename: string): void {
    const url  = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href     = url;
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);   // libère la mémoire
  }

  // ════════════════════════════════════════════════════════════════
  // MÉTHODE 5 — Convertit UNE image attestation → PDF d'une page
  // Les attestations sont stockées en JPEG/PNG côté serveur.
  // pdf-lib les embed dans un vrai PDF téléchargeable.
  // ════════════════════════════════════════════════════════════════
  async downloadSingleCertAsPdf(
    downloadLink: string,
    reference: string
  ): Promise<void> {
    if (!downloadLink) throw new Error('Lien de téléchargement manquant');

    const response     = await fetch(downloadLink);
    const arrayBuffer  = await response.arrayBuffer();
    const bytes        = new Uint8Array(arrayBuffer);
    const pdf          = await PDFDocument.create();

    let image;
    if (bytes[0] === 0xFF && bytes[1] === 0xD8) {
      image = await pdf.embedJpg(arrayBuffer);           // JPEG
    } else if (bytes[0] === 0x89 && bytes[1] === 0x50) {
      image = await pdf.embedPng(arrayBuffer);           // PNG
    } else {
      throw new Error(`Format d'image non supporté pour ${reference}`);
    }

    const { width, height } = image.scale(1);
    const page = pdf.addPage([width, height]);
    page.drawImage(image, { x: 0, y: 0, width, height });

    const pdfBytes = await pdf.save();
    this.triggerDownload(
      new Blob([pdfBytes], { type: 'application/pdf' }),
      `attestation-${reference}.pdf`
    );
  }

  // ════════════════════════════════════════════════════════════════
  // MÉTHODE 6 — Fusionne TOUTES les images d'une production → 1 PDF
  // ════════════════════════════════════════════════════════════════
  async downloadProductionAsPdf(
    certs: { download_link: string; reference: string }[],
    productionRef: string
  ): Promise<void> {
    if (!certs.length) throw new Error('Aucun certificat à télécharger');

    const mergedPdf = await PDFDocument.create();
    let   added     = 0;

    for (const cert of certs) {
      try {
        const response    = await fetch(cert.download_link);
        const arrayBuffer = await response.arrayBuffer();
        const bytes       = new Uint8Array(arrayBuffer);

        let image;
        if (bytes[0] === 0xFF && bytes[1] === 0xD8) {
          image = await mergedPdf.embedJpg(arrayBuffer);
        } else if (bytes[0] === 0x89 && bytes[1] === 0x50) {
          image = await mergedPdf.embedPng(arrayBuffer);
        } else {
          console.warn(`Format inconnu — ${cert.reference} ignoré`);
          continue;
        }

        const { width, height } = image.scale(1);
        const page = mergedPdf.addPage([width, height]);
        page.drawImage(image, { x: 0, y: 0, width, height });
        added++;
      } catch (err) {
        console.error(`Erreur pour ${cert.reference}:`, err);
      }
    }

    if (added === 0) throw new Error('Aucune attestation valide à générer');

    const pdfBytes = await mergedPdf.save();
    const date     = new Date().toISOString().split('T')[0];
    this.triggerDownload(
      new Blob([pdfBytes], { type: 'application/pdf' }),
      `attestations-${productionRef}-${date}.pdf`
    );
  }

  /* * Vérifie si un numéro de police existe en base Oracle.
   * Appelée automatiquement depuis le formulaire avec debounce 600ms.
   *
   * ✅ Police trouvée  → { exists: true,  ...données contrat/client/véhicule }
   * ❌ Police absente  → { exists: false, policeNumber: "POL-..." }
   *
   * Le composant utilise la réponse pour :
   *   - Pré-remplir tous les champs via patchValue() si exists=true
   *   - Afficher un dialog "introuvable" si exists=false
   *
   * @param policeNumber  Numéro de police saisi par l'utilisateur
   */
  checkPolice(policeNumber: string): Observable<InsuranceCertificateRequest> {
 
    // HttpParams encode proprement le paramètre dans l'URL
    // → GET /api/v1/police/check?number=POL-2026-00123
   const username=this.authService.getUserFromStorage()?.userapiasac;
    const params = new HttpParams()
      .set('police', policeNumber.trim().toUpperCase())
       .set('username', username ?? '');
 
    return this.http.get<InsuranceCertificateRequest>(
      `${this.apiUrl}/check`,
      { params }
    ).pipe(
      catchError(err => {
        // Erreur réseau → on propage pour que le composant gère l'état 'error'
        console.error('[CertificateService] checkPolice error:', err);
        return throwError(() => err.error || err);
      })
    );
  }
}