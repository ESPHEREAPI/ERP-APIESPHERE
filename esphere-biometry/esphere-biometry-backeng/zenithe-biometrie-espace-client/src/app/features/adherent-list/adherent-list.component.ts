import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';

import { Adherent } from '../../core/models/Adherent';
import { AdherentFilter } from '../../core/models/AdherentFilter';
import { AdherentService } from '../../core/services/adherent.service';
import { PageResponse } from '../../core/models/PageResponse';
import { SessionContext } from '../../core/auth/SessionContext';


@Component({
  selector: 'app-adherent-list',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './adherent-list.component.html',
  styleUrl: './adherent-list.component.css'
})
export class AdherentListComponent implements OnInit, OnDestroy {

  adherents: Adherent[] = [];
  totalElements = 0;
  totalPages = 0;
  currentPage = 0;
  pageSize = 20;
  loading = false;
  error: string | null = null;

  filter: AdherentFilter = {
    page: 0,
    size: 20,
    sortBy: 'assurePrincipal',
    sortDirection: 'ASC'
  };

  statutOptions = [
    { label: 'ACTIF',    value: '1'  },
    { label: 'SUSPENDU', value: '-1' }
  ];
  sexeOptions = ['M', 'F'];
  enroleOptions = [
    { value: '1',  label: 'Enrôlé'     },
    { value: '-1', label: 'Non enrôlé' }
  ];

  private readonly destroy$ = new Subject<void>();

  constructor(
    private readonly adherentService: AdherentService,
    private readonly router: Router,
    private readonly session: SessionContext,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // On s'abonne à la session et on fait le premier chargement
    // DANS le callback — garantit que les filtres souscripteur/police
    // sont renseignés avant l'appel HTTP (évite la race condition)
    this.session.session$
      .pipe(takeUntil(this.destroy$))
      .subscribe(s => {
        const dto = s?.usersDTO;
        this.filter.souscripteur = dto?.lastname   ?? '';
        this.filter.police       = dto?.userName   ?? '';
        this.cdr.markForCheck();
        this.loadAdherents();
      });
  }

  ngOnDestroy(): void {
    // Complète le Subject → désabonne automatiquement tous les takeUntil
    this.destroy$.next();
    this.destroy$.complete();
  }

  // ── Chargement ─────────────────────────────────────────────────────────────

  loadAdherents(): void {
    this.loading = true;
    this.error = null;

    this.adherentService.searchAdherents(this.filter).subscribe({
      next: (response: PageResponse<Adherent>) => {
        this.adherents      = response.content;
        this.totalElements  = response.totalElements;
        this.totalPages     = response.totalPages;
        this.currentPage    = response.number;
        this.loading        = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('[AdherentList] Erreur chargement', err);
        this.error   = 'Erreur lors du chargement des adhérents.';
        this.loading = false;
        this.showError(this.error);
        this.cdr.markForCheck();
      }
    });
  }

  // ── Filtres / pagination / tri ─────────────────────────────────────────────

  onSearch(): void {
    this.filter.page = 0;
    this.loadAdherents();
  }

  resetFilters(): void {
    this.filter = {
      page: 0,
      size: 20,
      sortBy: 'assurePrincipal',
      sortDirection: 'ASC',
      souscripteur: this.filter.souscripteur,
      police:       this.filter.police
    };
    this.loadAdherents();
  }

  onPageChange(page: number): void {
    this.filter.page = page;
    this.loadAdherents();
  }

  onPageSizeChange(size: number): void {
    this.filter.size = size;
    this.filter.page = 0;
    this.loadAdherents();
  }

  onSort(column: string): void {
    if (this.filter.sortBy === column) {
      this.filter.sortDirection =
        this.filter.sortDirection === 'ASC' ? 'DESC' : 'ASC';
    } else {
      this.filter.sortBy        = column;
      this.filter.sortDirection = 'ASC';
    }
    this.loadAdherents();
  }

  // ── Navigation ─────────────────────────────────────────────────────────────

  viewProfile(codeAdherent: string): void {
    this.router.navigate(['/adherents', codeAdherent]);
  }

  editAdherent(codeAdherent: string): void {
    this.router.navigate(['/adherents', codeAdherent, 'edit']);
  }

  createAdherent(): void {
    this.router.navigate(['/adherents', 'create']);
  }

  // ── Actions sur adhérent ───────────────────────────────────────────────────

  suspendAdherent(adherent: Adherent): void {
    Swal.fire({
      title: 'Suspendre l\'adhérent ?',
      text: `Voulez-vous vraiment suspendre ${adherent.assurePrincipal} ?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#ffc107',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Oui, suspendre',
      cancelButtonText: 'Annuler'
    }).then(result => {
      if (result.isConfirmed) {
        this.changeStatut(adherent, 'SUSPENDU', 'Adhérent suspendu avec succès');
      }
    });
  }

  activateAdherent(adherent: Adherent): void {
    this.changeStatut(adherent, 'ACTIF', 'Adhérent activé avec succès');
  }

  deleteAdherent(adherent: Adherent): void {
    Swal.fire({
      title: 'Supprimer l\'adhérent ?',
      text: `Cette action supprimera définitivement ${adherent.assurePrincipal}`,
      icon: 'error',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Annuler'
    }).then(result => {
      if (result.isConfirmed) {
        this.adherentService.deleteAdherent(adherent.codeAdherent!).subscribe({
          next: () => { this.showSuccess('Adhérent supprimé avec succès'); this.loadAdherents(); },
          error: err => { console.error('[AdherentList] Suppression', err); this.showError('Erreur lors de la suppression.'); }
        });
      }
    });
  }

  // ── Exports ────────────────────────────────────────────────────────────────

  exportExcel(): void {
    this.adherentService.exportToExcel(this.filter).subscribe({
      next: blob => this.downloadBlob(blob, `adherents_${Date.now()}.xlsx`),
      error: err => { console.error('[AdherentList] Export Excel', err); this.showError('Erreur lors de l\'export Excel.'); }
    });
  }

  exportPdf(): void {
    this.adherentService.exportToPdf(this.filter).subscribe({
      next: blob => this.downloadBlob(blob, `adherents_${Date.now()}.pdf`),
      error: err => { console.error('[AdherentList] Export PDF', err); this.showError('Erreur lors de l\'export PDF.'); }
    });
  }

  // ── Helpers template ───────────────────────────────────────────────────────

  isEnrole(valeur: string): boolean {
    return ['1', '2', '3', '4', '5'].includes(valeur);
  }

  getStatutBadgeClass(statut: string): string {
    const map: Record<string, string> = {
      'ACTIF':    'badge-success',
      'SUSPENDU': 'badge-warning',
      'RESILIE':  'badge-danger'
    };
    return map[statut] ?? 'badge-secondary';
  }

  calculateAge(naissance: Date): number {
    const birth = new Date(naissance);
    const today = new Date();
    let age = today.getFullYear() - birth.getFullYear();
    const m = today.getMonth() - birth.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birth.getDate())) age--;
    return age;
  }

  // ── Privés ─────────────────────────────────────────────────────────────────

  private changeStatut(adherent: Adherent, statut: string, successMsg: string): void {
    this.adherentService.changeStatut(adherent.codeAdherent!, statut).subscribe({
      next: () => { this.showSuccess(successMsg); this.loadAdherents(); },
      error: err => { console.error(`[AdherentList] changeStatut ${statut}`, err); this.showError(`Erreur lors du changement de statut.`); }
    });
  }

  private downloadBlob(blob: Blob, filename: string): void {
    const url  = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href  = url;
    link.download = filename;
    link.click();
    window.URL.revokeObjectURL(url);
  }

  private showSuccess(message: string): void {
    Swal.fire({ icon: 'success', title: 'Succès', text: message, timer: 3000, showConfirmButton: false });
  }

  private showError(message: string): void {
    Swal.fire({ icon: 'error', title: 'Erreur', text: message });
  }
}