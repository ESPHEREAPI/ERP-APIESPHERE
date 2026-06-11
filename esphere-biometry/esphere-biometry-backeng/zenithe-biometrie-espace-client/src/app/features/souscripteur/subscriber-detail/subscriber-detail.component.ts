import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { Subscriber } from '../../../core/models/Subscriber';
import { finalize, Subject, takeUntil } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { SouscripteurService } from '../../../core/services/souscripteur.service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-subscriber-detail',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './subscriber-detail.component.html',
  styleUrl: './subscriber-detail.component.css'
})
export class SubscriberDetailComponent implements OnInit, OnDestroy {
  subscriber?: Subscriber;
  isLoading = true;
  isDeleting = false;
  showDeleteConfirm = false;

  private destroy$ = new Subject<void>();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private subscriberService: SouscripteurService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadSubscriber(id);
  }

  private loadSubscriber(id: number): void {
    this.subscriberService
      .getById(id)
      .pipe(takeUntil(this.destroy$), finalize(() => { this.isLoading = false; this.cdr.markForCheck(); }))
      .subscribe((sub) => { this.subscriber = sub; this.cdr.markForCheck(); });
  }

  onEdit(): void {
    this.router.navigate(['/subscribers/edit', this.subscriber!.id]);
  }

  onDelete(): void {
    this.isDeleting = true;
    this.cdr.markForCheck();
    this.subscriberService
      .delete(this.subscriber!.id!)
      .pipe(takeUntil(this.destroy$), finalize(() => { this.isDeleting = false; this.cdr.markForCheck(); }))
      .subscribe(() => this.router.navigate(['/subscribers']));
  }

  onBack(): void {
    this.router.navigate(['/subscribers']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

}
