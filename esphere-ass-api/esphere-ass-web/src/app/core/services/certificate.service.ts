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
    if (!downloadLink && !reference) throw new Error('Lien de téléchargement manquant');

    let arrayBuffer: ArrayBuffer;

    try {
      const response = await fetch(downloadLink);
      if (!response.ok) throw new Error(`HTTP ${response.status}`);
      arrayBuffer = await response.arrayBuffer();
    } catch {
      // Fallback : récupérer les bytes depuis le backend (Oracle BLOB)
      const blob = await this.downloadPdf(reference).toPromise();
      if (!blob) throw new Error(`PDF introuvable pour ${reference}`);
      const buf = await blob.arrayBuffer();
      const b = new Uint8Array(buf);
      if ((b[0] === 0xFF && b[1] === 0xD8) || (b[0] === 0x89 && b[1] === 0x50)) {
        // Bytes JPEG ou PNG stockés en BLOB → conversion en vrai PDF
        const fallbackPdf = await PDFDocument.create();
        const img = b[0] === 0xFF ? await fallbackPdf.embedJpg(buf) : await fallbackPdf.embedPng(buf);
        const { width, height } = img.scale(1);
        const p = fallbackPdf.addPage([width, height]);
        p.drawImage(img, { x: 0, y: 0, width, height });
        const pdfBytes2 = await fallbackPdf.save();
        this.triggerDownload(new Blob([pdfBytes2], { type: 'application/pdf' }), `attestation-${reference}.pdf`);
      } else {
        this.triggerDownload(blob, `attestation-${reference}.pdf`);
      }
      return;
    }

    const bytes = new Uint8Array(arrayBuffer);
    const pdf   = await PDFDocument.create();

    let image;
    if (bytes[0] === 0xFF && bytes[1] === 0xD8) {
      image = await pdf.embedJpg(arrayBuffer);
    } else if (bytes[0] === 0x89 && bytes[1] === 0x50) {
      image = await pdf.embedPng(arrayBuffer);
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
        let arrayBuffer: ArrayBuffer;

        try {
          const response = await fetch(cert.download_link);
          if (!response.ok) throw new Error(`HTTP ${response.status}`);
          arrayBuffer = await response.arrayBuffer();
        } catch {
          // Fallback : récupérer les bytes depuis le backend (Oracle BLOB)
          try {
            const blob = await this.downloadPdf(cert.reference).toPromise();
            if (!blob) { console.warn(`PDF introuvable pour ${cert.reference}`); continue; }
            const buf = await blob.arrayBuffer();
            const b = new Uint8Array(buf);
            if ((b[0] === 0xFF && b[1] === 0xD8) || (b[0] === 0x89 && b[1] === 0x50)) {
              const img = b[0] === 0xFF ? await mergedPdf.embedJpg(buf) : await mergedPdf.embedPng(buf);
              const { width, height } = img.scale(1);
              const page = mergedPdf.addPage([width, height]);
              page.drawImage(img, { x: 0, y: 0, width, height });
              added++;
            } else {
              console.warn(`Format BLOB inconnu pour ${cert.reference} — ignoré`);
            }
          } catch (fallbackErr) {
            console.error(`Fallback échoué pour ${cert.reference}:`, fallbackErr);
          }
          continue;
        }

        const bytes = new Uint8Array(arrayBuffer);

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
  // ════════════════════════════════════════════════════════════════
  // INFO CERTIFICAT PAR RÉFÉRENCE
  // ════════════════════════════════════════════════════════════════
  getCertificateInfo(reference: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/info/${encodeURIComponent(reference)}`)
      .pipe(catchError(err => throwError(() => err.error || err)));
  }

  /** Tous les certificats d'une police (flotte) */
  getFleetByPolice(policeNumber: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/fleet/${encodeURIComponent(policeNumber.trim())}`)
      .pipe(catchError(err => throwError(() => err.error || err)));
  }

  /** Recherche flexible : REFERENCE | POLICE (+ plate si flotte) | PLATE */
  searchCertificateInfo(type: 'REFERENCE' | 'POLICE' | 'PLATE', value: string, plate?: string): Observable<any> {
    let params = new HttpParams().set('type', type).set('value', value.trim());
    if (plate?.trim()) params = params.set('plate', plate.trim());
    return this.http.get<any>(`${this.apiUrl}/search`, { params })
      .pipe(catchError(err => throwError(() => err.error || err)));
  }

  // ════════════════════════════════════════════════════════════════
  // MOTIFS PAR TYPE
  // ════════════════════════════════════════════════════════════════
  getMotifs(typeAction: 'SUSPENSION' | 'ANNULATION' | 'RESILIATION'): Observable<{code:string;typeAction:string;libelle:string}[]> {
    return this.http.get<{code:string;typeAction:string;libelle:string}[]>(`${this.apiUrl}/motifs/${typeAction}`)
      .pipe(catchError(err => throwError(() => err.error || err)));
  }

  // ════════════════════════════════════════════════════════════════
  // SUSPENSION
  // ════════════════════════════════════════════════════════════════
  suspendCertificate(reference: string, motifCode: string): Observable<any> {
    const username = this.authService.getUserFromStorage()?.userapiasac ?? '';
    return this.http.post<any>(`${this.apiUrl}/${reference}/suspend?username=${encodeURIComponent(username)}`,
      { motifCode })
      .pipe(catchError(err => throwError(() => err.error || err)));
  }

  // ════════════════════════════════════════════════════════════════
  // ANNULATION
  // ════════════════════════════════════════════════════════════════
  cancelCertificate(reference: string, motifCode: string): Observable<any> {
    const username = this.authService.getUserFromStorage()?.userapiasac ?? '';
    return this.http.post<any>(`${this.apiUrl}/${reference}/cancel?username=${encodeURIComponent(username)}`,
      { motifCode })
      .pipe(catchError(err => throwError(() => err.error || err)));
  }

  // ════════════════════════════════════════════════════════════════
  // RESILIATION
  // ════════════════════════════════════════════════════════════════
  resiliationCertificate(reference: string, motifCode: string): Observable<any> {
    const username = this.authService.getUserFromStorage()?.userapiasac ?? '';
    return this.http.post<any>(`${this.apiUrl}/${reference}/resiliation?username=${encodeURIComponent(username)}`,
      { motifCode })
      .pipe(catchError(err => throwError(() => err.error || err)));
  }

  // ════════════════════════════════════════════════════════════════
  // HISTORIQUE
  // ════════════════════════════════════════════════════════════════
  getHistoriqueByReference(reference: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${reference}/historique`)
      .pipe(catchError(err => throwError(() => err.error || err)));
  }

  getHistoriqueByOffice(): Observable<any[]> {
    const officeCode = this.authService.getUserFromStorage()?.agencyCode ?? '';
    return this.http.get<any[]>(`${this.apiUrl}/historique/office/${officeCode}`)
      .pipe(catchError(err => throwError(() => err.error || err)));
  }

  getHistoriqueByOfficeAndType(typeAction: string): Observable<any[]> {
    const officeCode = this.authService.getUserFromStorage()?.agencyCode ?? '';
    return this.http.get<any[]>(`${this.apiUrl}/historique/office/${officeCode}/${typeAction}`)
      .pipe(catchError(err => throwError(() => err.error || err)));
  }

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