import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import {
  AdminPrestataireService,
  PrestataireAdminItem,
  PrestataireRequest,
  CategorieOption,
  VilleOption
} from '../../services/admin-prestataire.service';
import { FilterableSelectComponent, SelectOption } from './filterable-select.component';

type Mode = 'liste' | 'form';

@Component({
  selector: 'app-prestataire',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, FilterableSelectComponent],
  templateUrl: './prestataire.component.html',
  styleUrls: ['./prestataire.component.css']
})
export class PrestataireComponent implements OnInit, OnDestroy {

  // ── état liste ────────────────────────────────────────────────────
  mode: Mode = 'liste';
  items: PrestataireAdminItem[] = [];
  total       = 0;
  totalPages  = 0;
  currentPage = 0;
  pageSize    = 10;
  tailles     = [10, 25, 50];

  filtreSearch      = '';
  filtreStatut      = '';
  filtreCategorieId = '';
  filtreVilleId: number | null = null;

  categories: CategorieOption[] = [];
  villes: VilleOption[] = [];
  isLoading = false;

  // ── sélection (checkbox) ──────────────────────────────────────────
  selected: Set<string> = new Set();

  // ── formulaire ────────────────────────────────────────────────────
  isEdit       = false;
  isSaving     = false;
  editId       = '';
  form: PrestataireRequest = this.emptyForm();
  logoFile: File | null = null;
  logoPreview: string | null = null;
  formError    = '';
  formSuccess  = '';

  // ── confirm suppression ───────────────────────────────────────────
  confirmItem: PrestataireAdminItem | null = null;

  private destroy$ = new Subject<void>();

  constructor(private svc: AdminPrestataireService) {}

  ngOnInit(): void {
    this.chargerCategories();
    this.chargerVilles();
    this.charger();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // ── Liste ─────────────────────────────────────────────────────────

  charger(page = 0): void {
    this.currentPage = page;
    this.isLoading   = true;
    this.selected.clear();
    this.svc.lister({
      page,
      size:        this.pageSize,
      statut:      this.filtreStatut,
      categorieId: this.filtreCategorieId,
      villeId:     this.filtreVilleId ?? undefined,
      search:      this.filtreSearch
    }).pipe(takeUntil(this.destroy$))
      .subscribe({
        next: r => {
          this.items      = r.data;
          this.total      = r.total;
          this.totalPages = r.totalPages;
          this.isLoading  = false;
        },
        error: () => { this.isLoading = false; }
      });
  }

  chargerCategories(): void {
    this.svc.categories()
      .pipe(takeUntil(this.destroy$))
      .subscribe({ next: r => this.categories = r });
  }

  chargerVilles(): void {
    this.svc.villes()
      .pipe(takeUntil(this.destroy$))
      .subscribe({ next: r => this.villes = r });
  }

  appliquerFiltres(): void  { this.charger(0); }
  reinitFiltres(): void {
    this.filtreSearch = ''; this.filtreStatut = ''; this.filtreCategorieId = ''; this.filtreVilleId = null;
    this.charger(0);
  }

  get pages(): number[] { return Array.from({ length: this.totalPages }, (_, i) => i); }
  get finPage(): number { return Math.min(this.total, (this.currentPage + 1) * this.pageSize); }

  // ── Sélection ─────────────────────────────────────────────────────

  toggleSelect(id: string): void {
    this.selected.has(id) ? this.selected.delete(id) : this.selected.add(id);
  }

  toggleAll(checked: boolean): void {
    this.selected.clear();
    if (checked) this.items.forEach(i => this.selected.add(i.id));
  }

  get allSelected(): boolean {
    return this.items.length > 0 && this.items.every(i => this.selected.has(i.id));
  }

  // ── Formulaire ────────────────────────────────────────────────────

  ouvrirAjouter(): void {
    this.isEdit      = false;
    this.editId      = '';
    this.form        = this.emptyForm();
    this.logoFile    = null;
    this.logoPreview = null;
    this.formError   = '';
    this.formSuccess = '';
    this.mode        = 'form';
  }

  ouvrirModifier(item: PrestataireAdminItem): void {
    this.isEdit      = true;
    this.editId      = item.id;
    this.form = {
      id:          item.id,
      categorieId: item.categorieId,
      villeId:     item.villeId,
      nom:         item.nom,
      adresse:     item.adresse ?? '',
      email:       item.email   ?? '',
      telephone:   item.telephone ?? '',
      registre:    item.registre  ?? ''
    };
    this.logoFile    = null;
    this.logoPreview = item.logo;
    this.formError   = '';
    this.formSuccess = '';
    this.mode        = 'form';
  }

  annuler(): void { this.mode = 'liste'; }

  onLogoChange(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) return;
    this.logoFile = file;
    const reader  = new FileReader();
    reader.onload = e => this.logoPreview = e.target?.result as string;
    reader.readAsDataURL(file);
  }

  enregistrer(): void {
    if (!this.form.nom?.trim()) { this.formError = 'Le nom est obligatoire.'; return; }
    if (!this.form.categorieId)  { this.formError = 'La catégorie est obligatoire.'; return; }
    if (!this.isEdit && !this.form.id?.trim()) { this.formError = "L'identifiant est obligatoire."; return; }

    this.isSaving  = true;
    this.formError = '';

    const obs = this.isEdit
      ? this.svc.modifier(this.editId, this.form)
      : this.svc.creer(this.form);

    obs.pipe(takeUntil(this.destroy$)).subscribe({
      next: saved => {
        if (this.logoFile) {
          this.svc.uploadLogo(saved.id, this.logoFile)
            .pipe(takeUntil(this.destroy$))
            .subscribe({ complete: () => this.apresEnregistrement() });
        } else {
          this.apresEnregistrement();
        }
      },
      error: (err) => {
        this.isSaving  = false;
        this.formError = err?.error?.erreur ?? 'Une erreur est survenue.';
      }
    });
  }

  private apresEnregistrement(): void {
    this.isSaving = false;
    this.mode     = 'liste';
    this.charger(this.isEdit ? this.currentPage : 0);
  }

  // ── Actions rapides ───────────────────────────────────────────────

  activer(item: PrestataireAdminItem): void {
    this.svc.activer(item.id).pipe(takeUntil(this.destroy$))
      .subscribe({ next: () => this.charger(this.currentPage) });
  }

  desactiver(item: PrestataireAdminItem): void {
    this.svc.desactiver(item.id).pipe(takeUntil(this.destroy$))
      .subscribe({ next: () => this.charger(this.currentPage) });
  }

  demanderSuppression(item: PrestataireAdminItem): void {
    this.confirmItem = item;
  }

  confirmerSuppression(): void {
    if (!this.confirmItem) return;
    this.svc.supprimer(this.confirmItem.id).pipe(takeUntil(this.destroy$))
      .subscribe({ next: () => { this.confirmItem = null; this.charger(this.currentPage); } });
  }

  annulerSuppression(): void { this.confirmItem = null; }

  // ── Helpers ───────────────────────────────────────────────────────

  private emptyForm(): PrestataireRequest {
    return { id: '', categorieId: '', villeId: null, nom: '', adresse: '', email: '', telephone: '', registre: '' };
  }

  statutClass(statut: string): string {
    return statut === '1' ? 'label label-success' : 'label label-default';
  }

  statutLib(statut: string): string {
    return statut === '1' ? 'Actif' : 'Inactif';
  }

  categorieNom(id: string): string {
    return this.categories.find(c => c.id === id)?.nom ?? id;
  }

  get categorieOptions(): SelectOption[] {
    return this.categories.map(c => ({ id: c.id, label: c.nom }));
  }

  get villeOptions(): SelectOption[] {
    return this.villes.map(v => ({ id: v.id, label: this.decodeHtml(v.nom) }));
  }

  hasLogo(item: PrestataireAdminItem): boolean {
    return !!item.logo && item.logo.trim().length > 0;
  }

  decodeHtml(s: string | null): string {
    if (!s) return '';
    const el = document.createElement('textarea');
    el.innerHTML = s;
    return el.value;
  }
}
