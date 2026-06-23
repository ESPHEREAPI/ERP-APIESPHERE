import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AdminProfilService, ProfilItem, ProfilRequest } from '../../services/admin-profil.service';

type Mode = 'liste' | 'form';

@Component({
  selector: 'app-profil',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule],
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.css']
})
export class ProfilComponent implements OnInit, OnDestroy {

  mode: Mode = 'liste';
  items: ProfilItem[] = [];
  total = 0; totalPages = 0; currentPage = 0; pageSize = 10;
  tailles = [10, 25, 50];
  filtreSearch = '';
  isLoading = false;
  selected: Set<number> = new Set();

  isEdit = false; isSaving = false; editId = 0;
  form: ProfilRequest = this.emptyForm();
  formError = '';

  confirmItem: ProfilItem | null = null;
  private destroy$ = new Subject<void>();

  constructor(private svc: AdminProfilService, private translate: TranslateService) {}

  ngOnInit(): void { this.charger(); }
  ngOnDestroy(): void { this.destroy$.next(); this.destroy$.complete(); }

  charger(page = 0): void {
    this.currentPage = page; this.isLoading = true; this.selected.clear();
    this.svc.lister({ page, size: this.pageSize, search: this.filtreSearch || undefined })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: r => { this.items = r.data; this.total = r.total; this.totalPages = r.totalPages; this.isLoading = false; },
        error: () => { this.isLoading = false; }
      });
  }

  appliquerFiltres(): void { this.charger(0); }
  reinitFiltres(): void { this.filtreSearch = ''; this.charger(0); }

  get pages(): number[] { return Array.from({ length: this.totalPages }, (_, i) => i); }
  get finPage(): number { return Math.min(this.total, (this.currentPage + 1) * this.pageSize); }

  toggleSelect(id: number): void { this.selected.has(id) ? this.selected.delete(id) : this.selected.add(id); }
  toggleAll(checked: boolean): void {
    this.selected.clear();
    if (checked) this.items.forEach(i => this.selected.add(i.id));
  }
  get allSelected(): boolean { return this.items.length > 0 && this.items.every(i => this.selected.has(i.id)); }

  ouvrirAjouter(): void {
    this.isEdit = false; this.editId = 0; this.form = this.emptyForm();
    this.formError = ''; this.mode = 'form';
  }

  ouvrirModifier(item: ProfilItem): void {
    this.isEdit = true; this.editId = item.id;
    this.form = { typeProfil: item.typeProfil, typeSousProfil: item.typeSousProfil ?? '', code: item.code };
    this.formError = ''; this.mode = 'form';
  }

  annuler(): void { this.mode = 'liste'; }

  enregistrer(): void {
    if (!this.form.typeProfil?.trim()) { this.formError = this.translate.instant('prof_type_obligatoire'); return; }
    if (!this.form.code?.trim()) { this.formError = this.translate.instant('prof_code_obligatoire'); return; }

    this.isSaving = true; this.formError = '';
    const obs = this.isEdit ? this.svc.modifier(this.editId, this.form) : this.svc.creer(this.form);
    obs.pipe(takeUntil(this.destroy$)).subscribe({
      next: () => { this.isSaving = false; this.mode = 'liste'; this.charger(this.isEdit ? this.currentPage : 0); },
      error: (err) => { this.isSaving = false; this.formError = err?.error?.erreur ?? 'Erreur'; }
    });
  }

  demanderSuppression(item: ProfilItem): void { this.confirmItem = item; }
  confirmerSuppression(): void {
    if (!this.confirmItem) return;
    this.svc.supprimer(this.confirmItem.id).pipe(takeUntil(this.destroy$))
      .subscribe({ next: () => { this.confirmItem = null; this.charger(this.currentPage); } });
  }
  annulerSuppression(): void { this.confirmItem = null; }

  private emptyForm(): ProfilRequest { return { typeProfil: '', typeSousProfil: '', code: '' }; }

  statutClass(s: string): string { return s === '1' ? 'label label-success' : 'label label-default'; }
}
