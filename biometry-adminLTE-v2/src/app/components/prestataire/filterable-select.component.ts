import { Component, Input, Output, EventEmitter, ElementRef, HostListener, OnChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

export interface SelectOption {
  id: any;
  label: string;
}

@Component({
  selector: 'app-filterable-select',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="fs-container" [class.fs-open]="open">
      <div class="fs-display form-control" (click)="toggle()" [class.fs-placeholder]="!selectedLabel">
        {{ selectedLabel || placeholder }}
        <i class="fa fa-caret-down fs-arrow"></i>
      </div>
      <div class="fs-dropdown" *ngIf="open">
        <input type="text" class="form-control input-sm fs-search"
          [(ngModel)]="search" (input)="filtrer()"
          placeholder="Rechercher..." autofocus
          (click)="$event.stopPropagation()">
        <ul class="fs-list">
          <li *ngIf="nullable" class="fs-item" [class.fs-selected]="value == null"
            (click)="select(null, placeholderOption)">
            {{ placeholderOption }}
          </li>
          <li *ngFor="let opt of filtered" class="fs-item"
            [class.fs-selected]="opt.id === value"
            (click)="select(opt.id, opt.label)">
            {{ opt.label }}
          </li>
          <li *ngIf="filtered.length === 0" class="fs-empty">Aucun résultat</li>
        </ul>
      </div>
    </div>
  `,
  styles: [`
    .fs-container { position: relative; }
    .fs-display {
      cursor: pointer; display: flex; align-items: center;
      justify-content: space-between; min-height: 34px;
      background: #fff; overflow: hidden; text-overflow: ellipsis;
      white-space: nowrap;
    }
    .fs-placeholder { color: #999; }
    .fs-arrow { margin-left: 8px; color: #999; }
    .fs-dropdown {
      position: absolute; top: 100%; left: 0; right: 0;
      z-index: 1050; background: #fff;
      border: 1px solid #ccc; border-top: none;
      border-radius: 0 0 3px 3px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }
    .fs-search { border: none; border-bottom: 1px solid #eee; border-radius: 0; }
    .fs-list {
      list-style: none; margin: 0; padding: 0;
      max-height: 200px; overflow-y: auto;
    }
    .fs-item {
      padding: 6px 12px; cursor: pointer; font-size: 13px;
    }
    .fs-item:hover { background: #f0f7ff; }
    .fs-selected { background: #e8f0fe; font-weight: 600; }
    .fs-empty { padding: 8px 12px; color: #aaa; font-size: 12px; }
  `]
})
export class FilterableSelectComponent implements OnChanges {
  @Input() options: SelectOption[] = [];
  @Input() value: any = null;
  @Input() placeholder = 'Sélectionnez...';
  @Input() placeholderOption = '— Aucun —';
  @Input() nullable = true;
  @Output() valueChange = new EventEmitter<any>();

  open = false;
  search = '';
  filtered: SelectOption[] = [];
  selectedLabel = '';

  ngOnChanges(): void {
    this.filtrer();
    this.updateLabel();
  }

  toggle(): void {
    this.open = !this.open;
    if (this.open) { this.search = ''; this.filtrer(); }
  }

  filtrer(): void {
    const s = this.search.toLowerCase();
    this.filtered = s
      ? this.options.filter(o => o.label.toLowerCase().includes(s))
      : [...this.options];
  }

  select(id: any, label: string): void {
    this.value = id;
    this.selectedLabel = id != null ? label : '';
    this.open = false;
    this.valueChange.emit(id);
  }

  private updateLabel(): void {
    const found = this.options.find(o => o.id === this.value);
    this.selectedLabel = found ? found.label : '';
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent): void {
    if (this.open && !this.el.nativeElement.contains(event.target)) {
      this.open = false;
    }
  }

  constructor(private el: ElementRef) {}
}
