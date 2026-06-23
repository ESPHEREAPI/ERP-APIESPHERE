import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import {
  AdminEmployeService, EmployeItem, CreateEmployeRequest, ProfilOption, EmployePageResponse
} from '../../services/admin-employe.service';
import {
  AdminPrestataireService, PrestataireAdminItem
} from '../../services/admin-prestataire.service';
import { FilterableSelectComponent, SelectOption } from '../prestataire/filterable-select.component';

type Mode = 'liste' | 'form';

@Component({
  selector: 'app-employe',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, FilterableSelectComponent],
  templateUrl: './employe.component.html',
  styleUrls: ['./employe.component.css']
})
export class EmployeComponent implements OnInit, OnDestroy {

  mode: Mode = 'liste';
  items: EmployeItem[] = [];
  total = 0; totalPages = 0; currentPage = 0; pageSize = 10;
  tailles = [10, 25, 50];
  filtreSearch = '';
  filtreStatut = '';
  filtreGenre = '';
  filtreProfilId: number | null = null;
  filtrePrestataireId = '';

  profils: ProfilOption[] = [];
  prestataires: PrestataireAdminItem[] = [];
  isLoading = false;
  selected: Set<number> = new Set();

  isEdit = false; isSaving = false; editId = 0;
  form: CreateEmployeRequest & { confirmMotPasse?: string } = this.emptyForm();
  formError = ''; formSuccess = '';

  confirmItem: EmployeItem | null = null;
  resetResult: { login: string; password: string } | null = null;

  private destroy$ = new Subject<void>();

  constructor(
    private svc: AdminEmployeService,
    private prestSvc: AdminPrestataireService,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.chargerProfils();
    this.chargerPrestataires();
    this.charger();
  }

  ngOnDestroy(): void { this.destroy$.next(); this.destroy$.complete(); }

  charger(page = 0): void {
    this.currentPage = page; this.isLoading = true; this.selected.clear();
    this.svc.lister({ page, size: this.pageSize, search: this.filtreSearch || undefined })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: r => {
          this.items = r.data; this.total = r.total;
          this.totalPages = r.totalPages; this.isLoading = false;
        },
        error: () => { this.isLoading = false; }
      });
  }

  chargerProfils(): void {
    this.svc.profils().pipe(takeUntil(this.destroy$))
      .subscribe({ next: r => this.profils = r });
  }

  chargerPrestataires(): void {
    this.prestSvc.lister({ page: 0, size: 500 }).pipe(takeUntil(this.destroy$))
      .subscribe({ next: r => this.prestataires = r.data });
  }

  appliquerFiltres(): void { this.charger(0); }
  reinitFiltres(): void {
    this.filtreSearch = ''; this.filtreStatut = '';
    this.filtreGenre = ''; this.filtreProfilId = null;
    this.filtrePrestataireId = '';
    this.charger(0);
  }

  get filteredItems(): EmployeItem[] {
    return this.items.filter(i => {
      if (this.filtreStatut && i.statut !== this.filtreStatut) return false;
      if (this.filtreGenre && i.genre !== this.filtreGenre) return false;
      if (this.filtreProfilId && i.profilId !== this.filtreProfilId) return false;
      if (this.filtrePrestataireId && i.prestataireId !== this.filtrePrestataireId) return false;
      return true;
    });
  }

  get pages(): number[] { return Array.from({ length: this.totalPages }, (_, i) => i); }
  get finPage(): number { return Math.min(this.total, (this.currentPage + 1) * this.pageSize); }

  toggleSelect(id: number): void { this.selected.has(id) ? this.selected.delete(id) : this.selected.add(id); }
  toggleAll(checked: boolean): void {
    this.selected.clear();
    if (checked) this.filteredItems.forEach(i => this.selected.add(i.id));
  }
  get allSelected(): boolean { return this.filteredItems.length > 0 && this.filteredItems.every(i => this.selected.has(i.id)); }

  // ── Form ──────────────────────────────────────────────────────

  ouvrirAjouter(): void {
    this.isEdit = false; this.editId = 0;
    this.form = this.emptyForm();
    this.formError = ''; this.mode = 'form';
  }

  ouvrirModifier(item: EmployeItem): void {
    this.isEdit = true; this.editId = item.id;
    this.form = {
      nom: item.nom, prenom: item.prenom ?? '', genre: item.genre ?? '',
      email: item.email, login: item.login, motPasse: '', confirmMotPasse: '',
      telephone: item.telephone ?? '', profilId: item.profilId,
      prestataireId: item.prestataireId ?? '', langueDefaut: item.langueDefaut ?? 2,
      connexionAppli: item.connexionAppli ?? '1', serialBiometrie: item.serialBiometrie ?? ''
    };
    this.formError = ''; this.mode = 'form';
  }

  annuler(): void { this.mode = 'liste'; }

  enregistrer(): void {
    if (!this.form.nom?.trim()) { this.formError = this.translate.instant('emp_nom_obligatoire'); return; }
    if (!this.form.email?.trim()) { this.formError = this.translate.instant('emp_email_obligatoire'); return; }
    if (!this.form.login?.trim()) { this.formError = this.translate.instant('emp_login_obligatoire'); return; }
    if (!this.isEdit && !this.form.motPasse?.trim()) { this.formError = this.translate.instant('emp_mdp_obligatoire'); return; }
    if (this.form.motPasse && this.form.motPasse !== this.form.confirmMotPasse) {
      this.formError = this.translate.instant('emp_mdp_mismatch'); return;
    }
    if (!this.form.profilId) { this.formError = this.translate.instant('emp_profil_obligatoire'); return; }
    if (this.isPrestataire && !this.form.serialBiometrie?.trim()) {
      this.formError = this.translate.instant('emp_serial_obligatoire'); return;
    }

    this.isSaving = true; this.formError = '';
    const req: CreateEmployeRequest = { ...this.form };
    if (this.isEdit && !req.motPasse) delete (req as any).motPasse;

    const obs = this.isEdit ? this.svc.modifier(this.editId, req) : this.svc.creer(req);
    obs.pipe(takeUntil(this.destroy$)).subscribe({
      next: () => { this.isSaving = false; this.mode = 'liste'; this.charger(this.isEdit ? this.currentPage : 0); },
      error: (err) => { this.isSaving = false; this.formError = err?.error?.erreur ?? 'Erreur'; }
    });
  }

  // ── Actions ───────────────────────────────────────────────────

  activer(item: EmployeItem): void {
    this.svc.activer(item.id).pipe(takeUntil(this.destroy$)).subscribe({ next: () => this.charger(this.currentPage) });
  }
  desactiver(item: EmployeItem): void {
    this.svc.desactiver(item.id).pipe(takeUntil(this.destroy$)).subscribe({ next: () => this.charger(this.currentPage) });
  }
  demanderSuppression(item: EmployeItem): void { this.confirmItem = item; }
  confirmerSuppression(): void {
    if (!this.confirmItem) return;
    this.svc.supprimer(this.confirmItem.id).pipe(takeUntil(this.destroy$))
      .subscribe({ next: () => { this.confirmItem = null; this.charger(this.currentPage); } });
  }
  annulerSuppression(): void { this.confirmItem = null; }

  resetPassword(item: EmployeItem): void {
    this.svc.resetPassword(item.id).pipe(takeUntil(this.destroy$))
      .subscribe({ next: r => this.resetResult = { login: item.login, password: r.temporaryPassword } });
  }
  fermerReset(): void { this.resetResult = null; }

  // ── Helpers ───────────────────────────────────────────────────

  private emptyForm(): CreateEmployeRequest & { confirmMotPasse?: string } {
    return { nom: '', prenom: '', genre: '', email: '', login: '', motPasse: '', confirmMotPasse: '',
      telephone: '', profilId: 0, prestataireId: '', langueDefaut: 2, connexionAppli: '1', serialBiometrie: '' };
  }

  get profilOptions(): SelectOption[] {
    return this.profils.map(p => ({
      id: p.id,
      label: p.typeProfil + (p.typeSousProfil ? ' - ' + p.typeSousProfil : '')
    }));
  }

  get prestataireOptions(): SelectOption[] {
    return this.prestataires.map(p => ({ id: p.id, label: p.nom }));
  }

  get isPrestataire(): boolean {
    const pid = this.form.prestataireId;
    return !!pid && pid !== '' && pid !== 'BACK_OFFICE';
  }

  statutClass(s: string): string { return s === '1' ? 'label label-success' : 'label label-default'; }

  genreLib(g: string): string {
    if (g === 'M' || g === 'Masculin') return 'Masculin';
    if (g === 'F' || g === 'Féminin' || g === 'Feminin') return 'Féminin';
    return g ?? '—';
  }
}
