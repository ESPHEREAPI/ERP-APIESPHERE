import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { Subscriber } from '../../../core/models/Subscriber';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, finalize, Subject, takeUntil } from 'rxjs';
import { SouscripteurService } from '../../../core/services/souscripteur.service';
import { Router } from '@angular/router';
import { SubscriberFilter } from '../../../core/models/SubscriberFilter';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-subscriber-list',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule,FormsModule],
  templateUrl: './subscriber-list.component.html',
  styleUrl: './subscriber-list.component.css'
})
export class SubscriberListComponent implements OnInit, OnDestroy {
  subscribers: Subscriber[] = [];
  total = 0;
  currentPage = 1;
  limit = 10;
  isLoading = false;
  deletingId?: number;
  togglingId?: number;
  confirmDeleteId?: number;

  searchCtrl = new FormControl('');
  statusFilter: boolean | undefined = undefined;

  private destroy$ = new Subject<void>();

  constructor(
    private subscriberService: SouscripteurService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadData();

    this.searchCtrl.valueChanges
      .pipe(
        debounceTime(400),
        distinctUntilChanged(),
        takeUntil(this.destroy$)
      )
      .subscribe(() => {
        this.currentPage = 1;
        this.loadData();
      });
  }

  loadData(): void {
    this.isLoading = true;
    this.cdr.markForCheck();

    const filter: SubscriberFilter = {
      search: this.searchCtrl.value || undefined,
      active: this.statusFilter,
      page: this.currentPage,
      limit: this.limit,
    };

    this.subscriberService
      .getAll(filter)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => {
          this.isLoading = false;
          this.cdr.markForCheck();
        })
      )
      .subscribe((res) => {
        this.subscribers = res.data;
        this.total = res.total;
        this.cdr.markForCheck();
      });
  }

  get totalPages(): number {
    return Math.ceil(this.total / this.limit);
  }

  get pages(): number[] {
    const pages: number[] = [];
    for (let i = 1; i <= this.totalPages; i++) {
      pages.push(i);
    }
    return pages;
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    this.loadData();
  }

  setStatusFilter(status: boolean | undefined): void {
    this.statusFilter = status;
    this.currentPage = 1;
    this.loadData();
  }

  onCreateNew(): void {
    this.router.navigate(['/subscribers/new']);
  }

  onEdit(id: number): void {
    this.router.navigate(['/subscribers/edit', id]);
  }

  onView(id: number): void {
    this.router.navigate(['/subscribers', id]);
  }

  onConfirmDelete(id: number): void {
    this.confirmDeleteId = id;
  }

  onCancelDelete(): void {
    this.confirmDeleteId = undefined;
  }

  onDelete(id: number): void {
    this.deletingId = id;
    this.cdr.markForCheck();

    this.subscriberService
      .delete(id)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => {
          this.deletingId = undefined;
          this.confirmDeleteId = undefined;
          this.cdr.markForCheck();
        })
      )
      .subscribe(() => {
        this.loadData();
      });
  }

  onToggleStatus(sub: Subscriber): void {
    this.togglingId = sub.id;
    this.cdr.markForCheck();

    this.subscriberService
      .toggleStatus(sub.id!, !sub.active)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => {
          this.togglingId = undefined;
          this.cdr.markForCheck();
        })
      )
      .subscribe(() => {
        this.loadData();
      });
  }

  getAvatarColor(id: number): string {
    const colors = ["#3a7afe","#28a745","#fd7e14","#6f42c1","#20c997","#e83e8c","#17a2b8","#dc3545"];
    return colors[id % colors.length];
  }

  trackById(_: number, sub: Subscriber): number {
    return sub.id!;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  getEndIndex(): number {
  return Math.min(this.currentPage * this.limit, this.total);
}
}
