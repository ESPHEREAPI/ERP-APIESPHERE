import { Component, OnInit, OnDestroy } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators
} from '@angular/forms';
import { Subject, debounceTime, distinctUntilChanged, takeUntil } from 'rxjs';
import { InfosAdminAgence, PROFILS_AGENT } from '../../core/model/infos-admin-agence.model';
import { InfosAdminAgenceService } from '../../core/services/infos-admin-agence.service';


@Component({
  selector: 'app-admin-agence',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TranslatePipe],
  templateUrl: './admin-agence.component.html'
})
export class AdminAgenceComponent implements OnInit, OnDestroy {

  // ── State ─────────────────────────────────────────────────────────────────
  items: InfosAdminAgence[] = [];
  totalElements = 0;
  totalPages = 0;
  currentPage = 0;
  pageSize = 10;
  searchTerm = '';
  sortBy = 'id';
  sortDir = 'desc';

  isLoading = false;
  isSaving = false;
  isModalOpen = false;
  isDeleteModalOpen = false;
  isEditMode = false;
  selectedId: number | null = null;
  toastMessage = '';
  toastType: 'success' | 'danger' | 'warning' = 'success';
  showToast = false;

  form!: FormGroup;
  searchSubject = new Subject<string>();
  private destroy$ = new Subject<void>();

  readonly profils = PROFILS_AGENT;

  constructor(
    private fb: FormBuilder,
    private service: InfosAdminAgenceService
  ) {}

  ngOnInit(): void {
    this.buildForm();
    this.loadData();

    this.searchSubject.pipe(
      debounceTime(400),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(term => {
      this.searchTerm = term;
      this.currentPage = 0;
      this.loadData();
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // ── Form ──────────────────────────────────────────────────────────────────

  buildForm(): void {
    this.form = this.fb.group({
      codeAgence:    [null, [Validators.required, Validators.min(1)]],
      libelleAgence: ['',   [Validators.required, Validators.minLength(2)]],
      email:         ['',   [Validators.required, Validators.email]],
      login:         ['',   [Validators.required, Validators.minLength(3)]],
      clientName:    [''],
      expiresAt:     [24,   [Validators.required, Validators.min(1)]],
      officeCode:    [''],
      username:      ['',   [Validators.required, Validators.minLength(3)]],
      profilAgent:   ['PRODUCTEUR', [Validators.required]],
      canEdit:       [false]
    });
  }

  // ── Data ──────────────────────────────────────────────────────────────────

  loadData(): void {
    this.isLoading = true;
    this.service
      .getAll(this.searchTerm, this.currentPage, this.pageSize, this.sortBy, this.sortDir)
      .subscribe({
        next: res => {
          if (res.success) {
            this.items        = res.data.content;
            this.totalElements = res.data.totalElements;
            this.totalPages   = res.data.totalPages;
          }
          this.isLoading = false;
        },
        error: () => {
          this.showNotification('Erreur lors du chargement des données', 'danger');
          this.isLoading = false;
        }
      });
  }

  // ── CRUD ──────────────────────────────────────────────────────────────────

  openCreate(): void {
    this.isEditMode = false;
    this.selectedId = null;
    this.form.reset({ expiresAt: 24, profilAgent: 'PRODUCTEUR', canEdit: false });
    this.isModalOpen = true;
  }

  openEdit(item: InfosAdminAgence): void {
    this.isEditMode = true;
    this.selectedId = item.id!;
    this.form.patchValue({
      codeAgence:    item.codeAgence,
      libelleAgence: item.libelleAgence,
      email:         item.email,
      login:         item.login,
      clientName:    item.clientName,
      expiresAt:     item.expiresAt,
      officeCode:    item.officeCode,
      username:      item.username,
      profilAgent:   item.profilAgent ?? 'PRODUCTEUR',
      canEdit:       item.canEdit ?? false
    });
    this.isModalOpen = true;
  }

  saveForm(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSaving = true;
    const payload: InfosAdminAgence = this.form.value;

    const obs = this.isEditMode
      ? this.service.update(this.selectedId!, payload)
      : this.service.create(payload);

    obs.subscribe({
      next: res => {
        if (res.success) {
          this.isModalOpen = false;
          this.showNotification(res.message, 'success');
          this.loadData();
        }
        this.isSaving = false;
      },
      error: err => {
        const msg = err?.error?.message || 'Erreur lors de la sauvegarde';
        this.showNotification(msg, 'danger');
        this.isSaving = false;
      }
    });
  }

  confirmDelete(id: number): void {
    this.selectedId = id;
    this.isDeleteModalOpen = true;
  }

  doDelete(): void {
    if (!this.selectedId) return;
    this.service.delete(this.selectedId).subscribe({
      next: res => {
        this.isDeleteModalOpen = false;
        this.showNotification(res.message, 'success');
        if (this.items.length === 1 && this.currentPage > 0) {
          this.currentPage--;
        }
        this.loadData();
      },
      error: () => {
        this.showNotification('Erreur lors de la suppression', 'danger');
        this.isDeleteModalOpen = false;
      }
    });
  }

  // ── Pagination & Tri ──────────────────────────────────────────────────────

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages) return;
    this.currentPage = page;
    this.loadData();
  }

  changeSort(col: string): void {
    if (this.sortBy === col) {
      this.sortDir = this.sortDir === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortBy = col;
      this.sortDir = 'asc';
    }
    this.currentPage = 0;
    this.loadData();
  }

  onSearch(event: Event): void {
    this.searchSubject.next((event.target as HTMLInputElement).value);
  }

  // ── Helpers ───────────────────────────────────────────────────────────────

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }

  isInvalid(field: string): boolean {
    const c = this.form.get(field);
    return !!(c && c.invalid && (c.dirty || c.touched));
  }

  errorMsg(field: string): string {
    const c = this.form.get(field);
    if (!c || !c.errors) return '';
    if (c.errors['required'])   return 'Ce champ est obligatoire';
    if (c.errors['email'])      return 'Email invalide';
    if (c.errors['minlength'])  return `Minimum ${c.errors['minlength'].requiredLength} caractères`;
    if (c.errors['min'])        return `Valeur minimale : ${c.errors['min'].min}`;
    return 'Valeur invalide';
  }

  showNotification(msg: string, type: 'success' | 'danger' | 'warning'): void {
    this.toastMessage = msg;
    this.toastType = type;
    this.showToast = true;
    setTimeout(() => this.showToast = false, 4000);
  }
}
