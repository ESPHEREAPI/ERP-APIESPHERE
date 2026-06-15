// ╔══════════════════════════════════════════════════════════════╗
// ║  certificate-form.component.ts  — Mono + Flotte             ║
// ╚══════════════════════════════════════════════════════════════╝
import { Component, OnInit, OnDestroy } from '@angular/core';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import {
  FormBuilder, FormGroup,
  FormsModule,
  ReactiveFormsModule, Validators
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Subject, EMPTY } from 'rxjs';
import {
  debounceTime,
  switchMap, takeUntil, catchError
} from 'rxjs/operators';
import { CertificateService } from '../../../core/services/certificate.service';
import { AuthService } from '../../../core/auth/auth.service';
import { InsuranceCertificateRequest } from '../../../core/model/InsuranceCertificateRequest';
import { ProductionRequest } from '../../../core/model/ProductionRequest';

type PoliceStatus = 'idle' | 'checking' | 'found' | 'not_found' | 'error';
type ViewMode = 'form' | 'fleet';

export interface ParsedError {
  field: string;
  message: string;
  icon: string;
}

@Component({
  selector: 'app-certificate-form',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, FormsModule, TranslatePipe],
  templateUrl: './certificate-form.component.html',
  styleUrl: './certificate-form.component.css'
})
export class CertificateFormComponent implements OnInit, OnDestroy {

  productionForm!: FormGroup;
  isSubmitting = false;
  errorResponse: any = null;
  successResponse: any = null;
  fieldErrors: Record<string, string[]> = {};
  parsedErrors: ParsedError[] = [];

  policeStatus: PoliceStatus = 'idle';
  policeChecked = '';
  showPoliceDialog = false;

  foundProductions: ProductionRequest[] = [];
  selectedIds: Set<number> = new Set();
  viewMode: ViewMode = 'form';
  fleetVariantCode = 'JAUNE';

  showConfirmDialog = false;
  pendingRequest: InsuranceCertificateRequest | null = null;
  confirmJsonString = '';

  expandedIndex: number | null = null;

  private policeInput$ = new Subject<string>();
  private destroy$ = new Subject<void>();

  /** true si l'agent a la permission de modifier les champs du formulaire */
  get canEdit(): boolean {
    return this.authService.currentUserValue?.canEdit === true;
  }

  // ── Libellés français pour chaque champ technique ────────────
  private readonly FIELD_LABELS: Record<string, string> = {
    starts_at: 'Date d\'effet',
    ends_at: 'Date d\'échéance',
    rc: 'Prime RC',
    police_number: 'Numéro de police',
    certificate_variant_code: 'Variante',
    customer_name: 'Nom du client',
    customer_phone: 'Téléphone',
    customer_email: 'Email',
    customer_type: 'Type de client',
    insured_birthdate: 'Date de naissance',
    licence_plate: 'Immatriculation',
    vehicle_chassis: 'Numéro de châssis',
    vehicle_brand: 'Marque',
    vehicle_model: 'Modèle',
    vehicle_category: 'Catégorie du véhicule',
    vehicle_gross_weight: 'Poids total autorisé',
    nb_of_seats: 'Nombre de places',
    fiscal_power: 'Puissance fiscale (CV)',
    circulation_zone: 'Zone de circulation',
    vehicle_first_registration_date: 'Date de mise en circulation',
    trailer_licence_plate: 'Immatriculation remorque',
    driver_name: 'Nom du conducteur',
    driver_permis: 'Numéro de permis',
    driver_permis_categorie: 'Catégorie de permis',
    driver_birthdate: 'Date de naissance (conducteur)',
    driver_licence_issued_at: 'Date d\'obtention du permis',
    taxpayer_number: 'Numéro contribuable',
  };

  private readonly FIELD_ICONS: Record<string, string> = {
    starts_at: 'fa-calendar-alt',
    ends_at: 'fa-calendar-check',
    rc: 'fa-coins',
    police_number: 'fa-hashtag',
    certificate_variant_code: 'fa-tag',
    customer_name: 'fa-user',
    customer_phone: 'fa-phone',
    customer_email: 'fa-envelope',
    customer_type: 'fa-users',
    insured_birthdate: 'fa-birthday-cake',
    licence_plate: 'fa-id-card',
    vehicle_chassis: 'fa-barcode',
    vehicle_brand: 'fa-car',
    vehicle_model: 'fa-car-side',
    vehicle_category: 'fa-layer-group',
    vehicle_gross_weight: 'fa-weight-hanging',
    nb_of_seats: 'fa-chair',
    fiscal_power: 'fa-tachometer-alt',
    circulation_zone: 'fa-map-marker-alt',
    vehicle_first_registration_date: 'fa-calendar',
    driver_name: 'fa-id-badge',
    driver_permis: 'fa-id-card-alt',
  };

  constructor(
    private fb: FormBuilder,
    private certificateService: CertificateService,
    private authService: AuthService,
    private translate: TranslateService
  ) { }

  ngOnInit(): void { this.initForm(); this.setupPoliceCheck(); this.applyEditPermissions(); }
  ngOnDestroy(): void { this.destroy$.next(); this.destroy$.complete(); }

  // ════════════════════════════════════════════════════════════
  // PARSEUR D'ERREURS — gère TOUS les formats de l'API
  // ════════════════════════════════════════════════════════════
  /**
   * Structure réelle reçue de l'API (screenshot) :
   * {
   *   general: {
   *     type:       "https://ppeatt-api.asac.cm/docs#...",
   *     title:      "Validation Error",
   *     status:     422,
   *     detail:     "The given data was invalid.",
   *     instance:   "/api/v1/productions",
   *     request_id: "32f0969...",
   *     errors: [
   *       { status: "422", title: "Validation Error",
   *         detail: "La date productions.0.starts_at ne peut pas être antérieure...",
   *         source: { pointer: "/productions/0/starts_at" } },
   *       { status: "422", title: "Validation Error",
   *         detail: "Le champ productions.0.vehicle_gross_weight doit être un entier.",
   *         source: { pointer: "/productions/0/vehicle_gross_weight" } },
   *       { status: "422", title: "Validation Error",
   *         detail: "Le montant de la prime RC (109091) ne correspond pas...",
   *         source: { pointer: "/productions/0/rc" } }
   *     ]
   *   }
   * }
   */
  private parseApiErrors(err: any): ParsedError[] {
    const results: ParsedError[] = [];

    // ══════════════════════════════════════════════════════════════
    // CAS 1 : errors.general est un tableau de strings JSON
    // Structure : { errors: { general: ["{ ...json... }"] } }
    // ══════════════════════════════════════════════════════════════
    const generalArray = err?.errors?.general;
    if (Array.isArray(generalArray) && generalArray.length > 0) {
      for (const rawItem of generalArray) {
        let parsed: any = null;

        if (typeof rawItem === 'string') {
          try { parsed = JSON.parse(rawItem); } catch { /* pas du JSON */ }
        } else if (typeof rawItem === 'object' && rawItem !== null) {
          parsed = rawItem;
        }

        if (parsed) {
          // Sous-tableau errors[] dans l'objet parsé
          if (Array.isArray(parsed.errors) && parsed.errors.length > 0) {
            for (const e of parsed.errors) {
              const pointer = (e?.source?.pointer ?? e?.pointer ?? '').toString();
              const field = this.pointerToField(pointer);
              const label = this.FIELD_LABELS[field] ?? this.humanizeField(field);
              const msg = this.decodeMessage(e?.detail ?? e?.message ?? 'Erreur inconnue');
              results.push({
                field: label,
                message: msg,
                icon: this.FIELD_ICONS[field] ?? 'fa-exclamation-circle'
              });
            }
          }
          // Pas de sous-tableau : afficher le detail global
          else if (parsed.detail) {
            results.push({
              field: 'Erreur API',
              message: this.decodeMessage(parsed.detail),
              icon: 'fa-exclamation-triangle'
            });
          }
          // Afficher le title si pas de detail
          else if (parsed.title) {
            results.push({
              field: 'Erreur API',
              message: this.decodeMessage(parsed.title),
              icon: 'fa-exclamation-triangle'
            });
          }
        } else if (typeof rawItem === 'string') {
          // String brute non-JSON
          results.push({
            field: 'Général',
            message: rawItem,
            icon: 'fa-exclamation-triangle'
          });
        }
      }
      if (results.length) return results;
    }

    // ══════════════════════════════════════════════════════════════
    // CAS 2 : errors.general est une string directe (pas un tableau)
    // Structure : { errors: { general: "message simple" } }
    // ══════════════════════════════════════════════════════════════
    const generalString = err?.errors?.general;
    if (typeof generalString === 'string') {
      let parsed: any = null;
      try { parsed = JSON.parse(generalString); } catch { /* pas du JSON */ }

      if (parsed) {
        if (Array.isArray(parsed.errors) && parsed.errors.length > 0) {
          for (const e of parsed.errors) {
            const pointer = (e?.source?.pointer ?? e?.pointer ?? '').toString();
            const field = this.pointerToField(pointer);
            const label = this.FIELD_LABELS[field] ?? this.humanizeField(field);
            const msg = this.decodeMessage(e?.detail ?? e?.message ?? 'Erreur inconnue');
            results.push({
              field: label,
              message: msg,
              icon: this.FIELD_ICONS[field] ?? 'fa-exclamation-circle'
            });
          }
          return results;
        } else if (parsed.detail) {
          return [{ field: 'Erreur API', message: this.decodeMessage(parsed.detail), icon: 'fa-exclamation-triangle' }];
        }
      }
      // String simple non-JSON
      return [{ field: 'Général', message: generalString, icon: 'fa-exclamation-triangle' }];
    }

    // ══════════════════════════════════════════════════════════════
    // CAS 3 : err.general direct (sans passer par err.errors)
    // Structure : { general: "..." } ou { general: {...} }
    // ══════════════════════════════════════════════════════════════
    let generalObj: any = null;
    if (err?.general !== undefined && err.general !== null) {
      if (typeof err.general === 'string') {
        try { generalObj = JSON.parse(err.general); } catch { /* pas du JSON */ }
        if (!generalObj) {
          return [{ field: 'Général', message: err.general, icon: 'fa-exclamation-triangle' }];
        }
      } else if (typeof err.general === 'object') {
        generalObj = err.general;
      }
    }

    if (generalObj?.errors && Array.isArray(generalObj.errors) && generalObj.errors.length > 0) {
      for (const e of generalObj.errors) {
        const pointer = (e?.source?.pointer ?? e?.pointer ?? '').toString();
        const field = this.pointerToField(pointer);
        const label = this.FIELD_LABELS[field] ?? this.humanizeField(field);
        const msg = this.decodeMessage(e?.detail ?? e?.message ?? 'Erreur inconnue');
        results.push({
          field: label,
          message: msg,
          icon: this.FIELD_ICONS[field] ?? 'fa-exclamation-circle'
        });
      }
      return results;
    }

    if (generalObj?.detail) {
      return [{ field: 'Erreur API', message: this.decodeMessage(generalObj.detail), icon: 'fa-exclamation-triangle' }];
    }

    // ══════════════════════════════════════════════════════════════
    // CAS 4 : errors est un objet clé → tableau de strings
    // Structure : { errors: { rc: ["message"], nom: ["message"] } }
    // ══════════════════════════════════════════════════════════════
    if (err?.errors && typeof err.errors === 'object' && !Array.isArray(err.errors)) {
      for (const [key, messages] of Object.entries(err.errors)) {
        if (key === 'general') continue; // déjà traité

        const field = key.replace(/^productions\.\d+\./, '');
        const label = this.FIELD_LABELS[field] ?? this.humanizeField(field);
        const msgs = Array.isArray(messages) ? messages : [String(messages)];

        for (const msg of msgs) {
          // Chaque message peut lui-même être une string JSON
          let msgText = String(msg);
          try {
            const msgParsed = JSON.parse(msgText);
            msgText = msgParsed?.detail ?? msgParsed?.message ?? msgText;
          } catch { /* pas du JSON */ }

          results.push({
            field: label,
            message: this.decodeMessage(msgText),
            icon: this.FIELD_ICONS[field] ?? 'fa-exclamation-circle'
          });
        }
      }
      if (results.length) return results;
    }

    // ══════════════════════════════════════════════════════════════
    // CAS 5 : errors est un tableau direct d'objets erreur
    // Structure : { errors: [{detail: "...", source: {pointer: "..."}}] }
    // ══════════════════════════════════════════════════════════════
    if (Array.isArray(err?.errors) && err.errors.length > 0) {
      for (const e of err.errors) {
        const pointer = (e?.source?.pointer ?? e?.pointer ?? '').toString();
        const field = this.pointerToField(pointer);
        const label = this.FIELD_LABELS[field] ?? this.humanizeField(field);
        results.push({
          field: label,
          message: this.decodeMessage(e?.detail ?? e?.message ?? 'Erreur'),
          icon: this.FIELD_ICONS[field] ?? 'fa-exclamation-circle'
        });
      }
      return results;
    }

    // ══════════════════════════════════════════════════════════════
    // CAS 6 : server error (tableau sous errors.server)
    // Structure : { errors: { server: ["message"] } }
    // ══════════════════════════════════════════════════════════════
    const serverErrors = err?.errors?.server;
    if (Array.isArray(serverErrors) && serverErrors.length > 0) {
      for (const msg of serverErrors) {
        results.push({
          field: 'Serveur',
          message: this.decodeMessage(String(msg)),
          icon: 'fa-server'
        });
      }
      return results;
    }

    // ══════════════════════════════════════════════════════════════
    // CAS 7 : Fallback — message simple
    // ══════════════════════════════════════════════════════════════
    const msg = err?.message ?? err?.detail ?? 'Une erreur inattendue s\'est produite.';
    return [{
      field: 'Général',
      message: this.decodeMessage(String(msg)),
      icon: 'fa-exclamation-triangle'
    }];
  }

  /** /productions/0/rc  →  rc */
  private pointerToField(pointer: string): string {
    if (!pointer) return '';
    const parts = pointer.replace(/\//g, '.').replace(/^\./, '').split('.');
    return parts[parts.length - 1] ?? '';
  }

  /** vehicle_gross_weight → Vehicle Gross Weight */
  private humanizeField(field: string): string {
    if (!field) return 'Champ inconnu';
    return field.replace(/_/g, ' ').replace(/\b\w/g, c => c.toUpperCase());
  }

  /** Décode les \uXXXX et tronque si trop long */
  private decodeMessage(raw: string): string {
    if (!raw) return 'Erreur inconnue';
    const decoded = raw.replace(/\\u([\dA-Fa-f]{4})/g,
      (_, code) => String.fromCharCode(parseInt(code, 16)));
    return decoded.length > 300 ? decoded.substring(0, 300) + '…' : decoded;
  }

  // ════════════════════════════════════════════════════════════
  // FORMULAIRE
  // ════════════════════════════════════════════════════════════
  initForm(): void {
    this.productionForm = this.fb.group({
      certificate_variant_code: ['', Validators.required],
      rc: ['', Validators.required],
      police_number: ['', Validators.required],
      starts_at: ['', Validators.required],
      ends_at: ['', Validators.required],
      customer_name: ['', Validators.required],
      customer_phone: ['', Validators.required],
      customer_email: ['', [Validators.required, Validators.email]],
      customer_postal_code: [''],
      customer_type: ['TSPM', Validators.required],
      taxpayer_number: [''],
      insured_birthdate: ['', Validators.required],
      insured_name: [''],
      insured_phone: [''],
      insured_email: [''],
      insured_postal_code: [''],
      licence_plate: ['', Validators.required],
      vehicle_chassis: ['', Validators.required],
      vehicle_brand: ['', Validators.required],
      vehicle_model: ['', Validators.required],
      vehicle_category: ['', Validators.required],
      vehicle_genre: ['GV04', Validators.required],
      vehicle_type: ['TV10', Validators.required],
      vehicule_usage: ['UV01', Validators.required],
      vehicle_energy: ['SEES', Validators.required],
      nb_of_seats: [null, Validators.required],
      fiscal_power: [null, Validators.required],
      circulation_zone: ['A', Validators.required],
      vehicle_first_registration_date: ['', Validators.required],
      vehicle_gross_weight: [''],
      vehicle_has_trailer: [false],
      trailer_licence_plate: [''],
      driver_name: [''],
      driver_birthdate: [''],
      driver_licence_issued_at: [''],
      driver_permis: [''],
      driver_permis_categorie: [''],
    });
  }

  /**
   * Désactive tous les champs sauf police_number quand canEdit === false.
   * À appeler après initForm() et après prefillForm() (patchValue réactive les controls).
   */
  private applyEditPermissions(): void {
    if (this.canEdit) {
      // Activer tous les champs
      Object.keys(this.productionForm.controls).forEach(key => {
        this.productionForm.get(key)?.enable({ emitEvent: false });
      });
      return;
    }
    // Désactiver tout sauf police_number
    Object.keys(this.productionForm.controls).forEach(key => {
      if (key === 'police_number') {
        this.productionForm.get(key)?.enable({ emitEvent: false });
      } else {
        this.productionForm.get(key)?.disable({ emitEvent: false });
      }
    });
  }

  // ════════════════════════════════════════════════════════════
  // VÉRIFICATION POLICE
  // ════════════════════════════════════════════════════════════
  private setupPoliceCheck(): void {
    this.policeInput$.pipe(
      debounceTime(600),
      switchMap(policeNumber => {
        if (!policeNumber || policeNumber.trim().length < 5) { this.resetPoliceState(); return EMPTY; }
        this.policeStatus = 'checking';
        this.policeChecked = policeNumber.trim();
        return this.certificateService.checkPolice(policeNumber.trim()).pipe(
          catchError(() => {
            // L'erreur est absorbée ici — la pipe reste vivante pour les prochaines saisies
            this.policeStatus = 'error';
            this.showPoliceDialog = false;
            return EMPTY;
          })
        );
      }),
      takeUntil(this.destroy$)
    ).subscribe((response: InsuranceCertificateRequest) => {
      const list = response?.productions ?? [];
      if (list.length === 0) {
        this.policeStatus = 'not_found'; this.foundProductions = [];
        this.showPoliceDialog = true; this.viewMode = 'form'; return;
      }
      this.policeStatus = 'found'; this.foundProductions = list; this.showPoliceDialog = false;
      if (list.length === 1) {
        this.viewMode = 'form';
        this.prefillForm(list[0]);
        this.applyEditPermissions();
      } else {
        this.viewMode = 'fleet';
        this.selectedIds = new Set(list.map((_, i) => i));
      }
    });
  }

  onPoliceNumberInput(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    if (!value || value.trim().length < 5) {
      this.resetPoliceState();
    } else {
      // Réinitialiser immédiatement l'état d'erreur dès que l'utilisateur modifie le champ
      this.policeStatus = 'idle';
    }
    this.policeInput$.next(value);
  }

  private resetPoliceState(): void {
    this.policeStatus = 'idle'; this.showPoliceDialog = false;
    this.foundProductions = []; this.selectedIds = new Set(); this.viewMode = 'form';
  }

  // ════════════════════════════════════════════════════════════
  // PRÉ-REMPLISSAGE
  // ════════════════════════════════════════════════════════════
  private prefillForm(data: ProductionRequest): void {
    this.productionForm.patchValue({
      certificate_variant_code: data.certificate_variant_code || '',
      rc: data.rc || '',
      police_number: data.police_number || '',
      starts_at: data.starts_at || '',
      ends_at: data.ends_at || '',
      customer_name: data.customer_name || '',
      customer_phone: data.customer_phone || '',
      customer_email: data.customer_email || '',
      customer_postal_code: data.customer_postal_code || '',
      customer_type: data.customer_type || 'TSPM',
      taxpayer_number: data.taxpayer_number || '',
      insured_name: data.insured_name || '',
      insured_phone: data.insured_phone || '',
      insured_email: data.insured_email || '',
      insured_postal_code: data.insured_postal_code || '',
      insured_birthdate: data.insured_birthdate || '',
      licence_plate: data.licence_plate || '',
      vehicle_chassis: data.vehicle_chassis || '',
      vehicle_brand: data.vehicle_brand || '',
      vehicle_model: data.vehicle_model || '',
      vehicle_category: data.vehicle_category || '',
      vehicle_genre: data.vehicle_genre || 'GV04',
      vehicle_type: data.vehicle_type || 'TV10',
      vehicule_usage: data.vehicule_usage || 'UV01',
      vehicle_energy: data.vehicle_energy || 'SEES',
      vehicle_gross_weight: data.vehicle_gross_weight || '',
      nb_of_seats: data.nb_of_seats ?? null,
      fiscal_power: data.fiscal_power ?? null,
      circulation_zone: data.circulation_zone || 'A',
      vehicle_has_trailer: data.vehicle_has_trailer || false,
      trailer_licence_plate: data.trailer_licence_plate || '',
      driver_name: data.driver_name || '',
      driver_birthdate: data.driver_birthdate || '',
      driver_licence_issued_at: data.driver_licence_issued_at || '',
      vehicle_first_registration_date: data.vehicle_first_registration_date || '',
      driver_permis_categorie: data.driver_permis_categorie || '',
      driver_permis: data.driver_permis || '',
      insured_code: data.insured_code || '',
      insured_profession: data.insured_profession || '',
      insured_city: data.insured_city || '',
    });
  }

  // ════════════════════════════════════════════════════════════
  // SÉLECTION FLOTTE
  // ════════════════════════════════════════════════════════════
  toggleSelection(index: number): void {
    this.selectedIds.has(index) ? this.selectedIds.delete(index) : this.selectedIds.add(index);
  }
  toggleAll(event: Event): void {
    const checked = (event.target as HTMLInputElement).checked;
    this.selectedIds = checked ? new Set(this.foundProductions.map((_, i) => i)) : new Set();
  }
  isSelected(index: number): boolean { return this.selectedIds.has(index); }
  get allSelected(): boolean {
    return this.foundProductions.length > 0 && this.selectedIds.size === this.foundProductions.length;
  }
  get selectedCount(): number { return this.selectedIds.size; }
  backToForm(): void { this.viewMode = 'form'; this.initForm(); }

  // ════════════════════════════════════════════════════════════
  // SOUMISSION
  // ════════════════════════════════════════════════════════════
  onSubmit(): void {
    if (this.viewMode === 'fleet') {
      if (this.selectedIds.size === 0) { alert('Veuillez sélectionner au moins un véhicule.'); return; }
      this.prepareFleetConfirm(); return;
    }
    this.productionForm.markAllAsTouched();
    if (this.productionForm.invalid) return;
    this.prepareSingleConfirm();
  }

  private prepareSingleConfirm(): void {
    const session = this.authService.currentUserValue;
    // getRawValue() inclut les champs désactivés (cas canEdit=false)
    const form = this.productionForm.getRawValue();
    const request: InsuranceCertificateRequest = {
      office_code: session?.agencyCode || '0000',
      organization_code: session?.companyName || 'ZENITHE INSURANCE',
      certificate_type: 'cima',
      productions: [{
        ...form,
        insured_name: form.insured_name || form.customer_name,
        insured_phone: form.insured_phone || form.customer_phone,
        insured_email: form.insured_email || form.customer_email,
        insured_postal_code: form.insured_postal_code || form.customer_postal_code,
      }]
    };
    this.pendingRequest = request;
    this.confirmJsonString = JSON.stringify(request, null, 2);
    this.showConfirmDialog = true;
  }

  private prepareFleetConfirm(): void {
    const session = this.authService.currentUserValue;
    const productions: ProductionRequest[] = Array.from(this.selectedIds).map(i => ({
      ...this.foundProductions[i],
      certificate_variant_code: this.foundProductions[i].certificate_variant_code || this.fleetVariantCode,
      // ── Champs assuré : fallback sur client si vides ───────
      insured_name: this.foundProductions[i].insured_name || this.foundProductions[i].customer_name || '',
      insured_phone: this.foundProductions[i].insured_phone || this.foundProductions[i].customer_phone || '',
      insured_email: this.foundProductions[i].insured_email || this.foundProductions[i].customer_email || '',
      insured_postal_code: this.foundProductions[i].insured_postal_code || this.foundProductions[i].customer_postal_code || '',

      // ── Champs avec valeur par défaut si absents ───────────
      taxpayer_number: this.foundProductions[i].taxpayer_number || '',
      vehicle_gross_weight: this.foundProductions[i].vehicle_gross_weight || 0,
      vehicle_genre: this.foundProductions[i].vehicle_genre || 'GV04',
      vehicle_type: this.foundProductions[i].vehicle_type || 'TV10',
      vehicule_usage: this.foundProductions[i].vehicule_usage || 'UV01',
      vehicle_energy: this.foundProductions[i].vehicle_energy || 'SEES',
      circulation_zone: this.foundProductions[i].circulation_zone || 'A',
      customer_type: this.foundProductions[i].customer_type || 'TSPM',
    }));
    const request: InsuranceCertificateRequest = {
      office_code: session?.agencyCode || '0000',
      organization_code: session?.companyName || 'ZENITHE INSURANCE',
      certificate_type: 'cima',
      productions
    };
    this.pendingRequest = request;
    this.confirmJsonString = JSON.stringify(request, null, 2);
    this.showConfirmDialog = true;
  }

  confirmSubmit(): void {
    this.showConfirmDialog = false;
    if (this.pendingRequest) { this.sendRequest(this.pendingRequest); this.pendingRequest = null; }
  }

  cancelConfirm(): void {
    this.showConfirmDialog = false; this.pendingRequest = null; this.confirmJsonString = '';
  }

 private sendRequest(request: InsuranceCertificateRequest): void {
  this.isSubmitting = true;
  this.clearMessages();

  this.certificateService.createCertificate(request).subscribe({
    next: (response: any) => {
      console.log('[COMPOSANT] next() appelé, réponse :', response); // ← ajouter
      this.isSubmitting = false;
      this.resetForm();               // ← reset en premier (efface successResponse)
      this.successResponse = response; // ← assigner APRÈS (sinon clearMessages l'écrase)
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },
    error: (err: any) => {
      console.log('[COMPOSANT] error() appelé :', err);
      this.isSubmitting = false;
      this.errorResponse = err;

      // Clé i18n dans err.message quand le backend retourne {"status":4xx,"message":"STOCK.*","data":null}
      const backendKey: string | undefined = err?.message;
      if (backendKey && backendKey.startsWith('STOCK.')) {
        const translated = this.translate.instant(backendKey);
        this.parsedErrors = [{ field: 'stock', message: translated, icon: 'fa-box-open' }];
        this.fieldErrors = {};
      } else {
        this.fieldErrors = err?.errors && !Array.isArray(err.errors) ? err.errors : {};
        this.parsedErrors = this.parseApiErrors(err);
      }

      console.error('[CertificateForm] Erreur brute :', JSON.stringify(err, null, 2));
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  });
}

  // ════════════════════════════════════════════════════════════
  // DIALOGS
  // ════════════════════════════════════════════════════════════
  closePoliceDialog(): void {
    this.showPoliceDialog = false; this.policeStatus = 'idle';
    this.productionForm.patchValue({ police_number: '' });
    setTimeout(() => document.getElementById('police_number')?.focus(), 150);
  }
  continueWithNewPolice(): void { this.showPoliceDialog = false; this.policeStatus = 'idle'; this.viewMode = 'form'; }

  // ════════════════════════════════════════════════════════════
  // UTILITAIRES
  // ════════════════════════════════════════════════════════════
  resetForm(): void { this.initForm(); this.applyEditPermissions(); this.clearMessages(); this.resetPoliceState(); }

  private clearMessages(): void {
    this.errorResponse = null; this.successResponse = null;
    this.fieldErrors = {}; this.parsedErrors = [];
  }

  dismissErrors(): void { this.errorResponse = null; this.parsedErrors = []; }

  isPhysique(): boolean { return this.productionForm.get('customer_type')?.value === 'TSPP'; }

  isInvalid(field: string): boolean {
    const c = this.productionForm.get(field);
    return (!!c && c.invalid && c.touched) || !!this.fieldErrors[`productions.0.${field}`];
  }

  getFieldError(field: string): string {
    return this.fieldErrors[`productions.0.${field}`]?.[0] || '';
  }

  hasFieldErrors(): boolean { return Object.keys(this.fieldErrors).length > 0; }
  getErrorKeys(): string[] { return Object.keys(this.fieldErrors); }
  getFirstError(k: string): string { return this.fieldErrors[k]?.[0] || ''; }

  getTotalRc(): number {
    return Array.from(this.selectedIds).reduce((s, i) => s + (this.foundProductions[i]?.rc || 0), 0);
  }

  toggleDetailPanel(index: number): void {
    this.expandedIndex = this.expandedIndex === index ? null : index;
  }

  getMissingCount(p: ProductionRequest): number {
    const req: (keyof ProductionRequest)[] = [
      'customer_name', 'customer_phone', 'customer_email', 'customer_type',
      'insured_birthdate', 'licence_plate', 'vehicle_chassis', 'vehicle_category',
      'vehicle_brand', 'vehicle_model', 'vehicle_first_registration_date',
      'nb_of_seats', 'fiscal_power'
    ];
    return req.filter(f => !p[f]).length;
  }

  isComplete(p: ProductionRequest): boolean { return this.getMissingCount(p) === 0; }

  updateFleetField(index: number, field: keyof ProductionRequest, event: Event): void {
    const value = (event.target as HTMLInputElement | HTMLSelectElement).value;
    this.foundProductions[index] = { ...this.foundProductions[index], [field]: value };
  }

  get completeCount(): number { return this.foundProductions.filter(p => this.isComplete(p)).length; }

  copyJsonToClipboard(): void { navigator.clipboard?.writeText(this.confirmJsonString).catch(() => { }); }
}