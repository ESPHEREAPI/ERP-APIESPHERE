import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { MediaService } from '../../services/media.service';
import { from, concatMap, toArray } from 'rxjs';

type EtatUpload = 'idle' | 'uploading' | 'success' | 'error' | 'deja_envoye';

@Component({
  selector: 'app-mobile-capture',
  standalone: true,
  imports: [CommonModule],
  template: `
<div style="min-height:100vh;background:#1a1a2e;display:flex;
            flex-direction:column;align-items:center;
            justify-content:center;padding:20px;font-family:sans-serif;">

  <div style="background:#fff;border-radius:16px;padding:28px 20px;
              max-width:420px;width:100%;box-shadow:0 8px 32px rgba(0,0,0,.4);">

    <!-- En-tête -->
    <div style="text-align:center;margin-bottom:24px;">
      <div style="font-size:48px;">📋</div>
      <h2 style="margin:8px 0 4px;font-size:20px;color:#1a1a2e;">
        {{ nature === 'ordonnance' ? 'Ordonnance' : 'Examen' }}
      </h2>
      <p style="color:#666;font-size:13px;margin:0;">
        {{ lang === 'en' ? 'Photograph each page of the physical document' : 'Photographiez chaque page du document physique' }}
      </p>
      <div style="margin-top:8px;background:#f0f4ff;border-radius:8px;
                  padding:8px 12px;font-size:12px;color:#3c6bc9;">
        <strong>Code visite :</strong> {{ codeCourt }}
      </div>
    </div>

    <!-- Déjà envoyé -->
    <div *ngIf="etat === 'deja_envoye'" style="text-align:center;padding:20px;">
      <div style="font-size:64px;">📄</div>
      <h3 style="color:#f57c00;margin:12px 0 8px;">
        {{ lang === 'en' ? 'Document already sent' : 'Document déjà envoyé' }}
      </h3>
      <p style="color:#666;font-size:13px;">
        {{ lang === 'en' ? 'A document has already been sent for this service.' : 'Un document a déjà été envoyé pour cette prestation.' }}
      </p>
      <p style="color:#999;font-size:12px;margin-top:16px;">
        {{ lang === 'en' ? 'You can close this page.' : 'Vous pouvez fermer cette page.' }}
      </p>
    </div>

    <!-- Zone upload -->
    <div *ngIf="etat === 'idle' || etat === 'error'">

      <!-- Bouton Caméra -->
      <label for="cameraInput"
             style="display:block;border:2px solid #3c6bc9;border-radius:12px;
                    padding:20px 16px;text-align:center;cursor:pointer;
                    background:#3c6bc9;color:#fff;margin-bottom:10px;">
        <div style="font-size:32px;margin-bottom:6px;">📸</div>
        <div style="font-weight:600;font-size:15px;">
          {{ lang === 'en' ? 'Take a photo' : 'Prendre une photo' }}
        </div>
        <div style="opacity:0.8;font-size:12px;margin-top:4px;">
          {{ lang === 'en' ? 'Add a photo (front, back...)' : 'Ajoute une photo (recto, verso...)' }}
        </div>
      </label>
      <input id="cameraInput" type="file"
             accept="image/*"
             capture="environment"
             style="display:none"
             (change)="ajouterFichier($event)" />

      <!-- Bouton Galerie -->
      <label for="fileInput"
             style="display:block;border:2px dashed #3c6bc9;border-radius:12px;
                    padding:16px;text-align:center;cursor:pointer;
                    background:#f8f9ff;margin-bottom:16px;">
        <div style="font-size:24px;margin-bottom:4px;">🖼️</div>
        <div style="font-weight:600;color:#3c6bc9;font-size:13px;">
          {{ lang === 'en' ? 'Choose from gallery' : 'Choisir depuis la galerie' }}
        </div>
        <div style="color:#999;font-size:11px;margin-top:4px;">
          {{ lang === 'en' ? 'Multiple files allowed' : 'Plusieurs fichiers possibles' }}
        </div>
      </label>
      <input id="fileInput" type="file"
             accept="image/*,video/*"
             multiple
             style="display:none"
             (change)="ajouterFichiers($event)" />

      <!-- Liste des fichiers -->
      <div *ngIf="fichiers.length > 0" style="margin-bottom:16px;">
        <div *ngFor="let f of fichiers; let i = index"
             style="background:#e8f5e9;border-radius:8px;padding:10px 12px;
                    margin-bottom:6px;display:flex;align-items:center;gap:10px;">
          <span style="font-size:20px;">🖼️</span>
          <div style="flex:1;min-width:0;">
            <div style="font-weight:600;font-size:12px;word-break:break-all;">
              {{ f.name }}
            </div>
            <div style="color:#666;font-size:11px;">{{ formatTaille(f.size) }}</div>
          </div>
          <button (click)="supprimerFichier(i)"
                  style="background:none;border:none;color:#c62828;font-size:18px;cursor:pointer;">
            ✕
          </button>
        </div>
        <div style="color:#666;font-size:12px;text-align:right;margin-top:4px;">
          {{ fichiers.length }} fichier(s)
        </div>
      </div>

      <div *ngIf="etat === 'error'"
           style="background:#ffebee;border-radius:8px;padding:12px;
                  margin-bottom:16px;color:#c62828;font-size:13px;">
        ❌ {{ erreur }}
      </div>

      <button [disabled]="fichiers.length === 0"
              (click)="uploader()"
              style="width:100%;padding:14px;border:none;border-radius:10px;
                     background:#3c6bc9;color:#fff;font-size:16px;
                     font-weight:600;cursor:pointer;"
              [style.opacity]="fichiers.length === 0 ? '0.5' : '1'">
        {{ lang === 'en'
          ? ('Send ' + (fichiers.length > 1 ? fichiers.length + ' documents' : 'the document'))
          : ('Envoyer ' + (fichiers.length > 1 ? fichiers.length + ' documents' : 'le document')) }}
      </button>
    </div>

    <!-- Upload en cours -->
    <div *ngIf="etat === 'uploading'" style="text-align:center;padding:20px;">
      <div style="font-size:48px;">⏳</div>
      <p style="color:#3c6bc9;font-weight:600;margin-top:12px;">
        Envoi {{ progression }} / {{ fichiers.length }}...
      </p>
    </div>

    <!-- Succès -->
    <div *ngIf="etat === 'success'" style="text-align:center;padding:20px;">
      <div style="font-size:64px;">✅</div>
      <h3 style="color:#2e7d32;margin:12px 0 8px;">
        {{ lang === 'en'
          ? (fichiers.length > 1 ? fichiers.length + ' documents sent!' : 'Document sent!')
          : (fichiers.length > 1 ? fichiers.length + ' documents envoyés !' : 'Document envoyé !') }}
      </h3>
      <p style="color:#666;font-size:13px;">
        {{ lang === 'en'
          ? 'The health service agent will review your document before validating your ' + nature + '.'
          : 'L\'agent de service de santé va examiner votre document avant de valider votre ' + nature + '.' }}
      </p>
      <p style="color:#999;font-size:12px;margin-top:16px;">
        {{ lang === 'en' ? 'You can close this page.' : 'Vous pouvez fermer cette page.' }}
      </p>
    </div>

  </div>
</div>
  `,
})
export class MobileCaptureComponent implements OnInit {

  codeCourt    = '';
  prestationId = 0;
  nature       = '';
  lang         = 'fr';

  fichiers: File[]    = [];
  progression         = 0;
  etat: EtatUpload    = 'idle';
  erreur              = '';

  constructor(
    private route:        ActivatedRoute,
    private mediaService: MediaService
  ) {}

  ngOnInit(): void {
    this.codeCourt    = this.route.snapshot.paramMap.get('codeCourt')    || '';
    this.prestationId = Number(this.route.snapshot.paramMap.get('prestationId') || 0);
    this.nature       = this.route.snapshot.paramMap.get('nature')       || 'ordonnance';
    this.lang         = this.route.snapshot.queryParamMap.get('lang')    || (navigator.language.startsWith('fr') ? 'fr' : 'en');

    // Vérifier si un document a déjà été envoyé
    const key = `capture_done_${this.prestationId}`;
    if (localStorage.getItem(key) === 'true') {
      this.etat = 'deja_envoye';
      return;
    }

    // Vérifier aussi côté serveur
    if (this.prestationId) {
      this.mediaService.getParPrestation(this.prestationId).subscribe({
        next: medias => {
          if (medias && medias.length > 0) {
            this.etat = 'deja_envoye';
            localStorage.setItem(key, 'true');
          }
        }
      });
    }
  }

  // Caméra : AJOUTE un fichier (ne remplace pas)
  ajouterFichier(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.fichiers.push(...Array.from(input.files));
      this.etat   = 'idle';
      this.erreur = '';
    }
    input.value = '';
  }

  // Galerie : AJOUTE plusieurs fichiers
  ajouterFichiers(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.fichiers.push(...Array.from(input.files));
      this.etat   = 'idle';
      this.erreur = '';
    }
    input.value = '';
  }

  supprimerFichier(index: number): void {
    this.fichiers.splice(index, 1);
  }

  uploader(): void {
    if (!this.fichiers.length || !this.codeCourt) return;
    this.etat        = 'uploading';
    this.progression = 0;

    from(this.fichiers).pipe(
      concatMap(fichier =>
        this.mediaService.uploadParCodeCourt(
          this.codeCourt, fichier, this.prestationId, this.nature
        ).pipe(
          concatMap(res => { this.progression++; return [res]; })
        )
      ),
      toArray()
    ).subscribe({
      next:  () => {
        this.etat = 'success';
        localStorage.setItem(`capture_done_${this.prestationId}`, 'true');
      },
      error: (err) => {
        this.etat   = 'error';
        this.erreur = err?.error?.message || 'Erreur lors de l\'envoi. Réessayez.';
      }
    });
  }

  formatTaille(octets: number): string {
    if (octets < 1024)        return octets + ' o';
    if (octets < 1024 * 1024) return Math.round(octets / 1024) + ' Ko';
    return (octets / (1024 * 1024)).toFixed(1) + ' Mo';
  }
}
