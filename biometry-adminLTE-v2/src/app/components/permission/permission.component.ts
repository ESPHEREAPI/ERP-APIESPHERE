import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AdminPermissionService, MenuTree } from '../../services/admin-permission.service';
import { AdminEmployeService, ProfilOption } from '../../services/admin-employe.service';
import { FilterableSelectComponent, SelectOption } from '../prestataire/filterable-select.component';

@Component({
  selector: 'app-permission',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, FilterableSelectComponent],
  templateUrl: './permission.component.html',
  styleUrls: ['./permission.component.css']
})
export class PermissionComponent implements OnInit, OnDestroy {

  profils: ProfilOption[] = [];
  selectedProfilId: number | null = null;
  menuTree: MenuTree[] = [];
  checkedMenuIds: Set<number> = new Set();
  isSaving = false;
  isLoading = false;
  saveSuccess = '';
  saveError = '';

  private destroy$ = new Subject<void>();

  constructor(
    private permSvc: AdminPermissionService,
    private empSvc: AdminEmployeService,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.empSvc.profils().pipe(takeUntil(this.destroy$))
      .subscribe({ next: r => this.profils = r });
    this.permSvc.getAllMenus().pipe(takeUntil(this.destroy$))
      .subscribe({ next: menus => this.menuTree = this.permSvc.buildTree(menus) });
  }

  ngOnDestroy(): void { this.destroy$.next(); this.destroy$.complete(); }

  get profilOptions(): SelectOption[] {
    return this.profils.map(p => ({ id: p.id, label: p.typeProfil + ' (' + p.code + ')' }));
  }

  onProfilChange(profilId: number): void {
    this.selectedProfilId = profilId;
    this.saveSuccess = ''; this.saveError = '';
    if (!profilId) { this.checkedMenuIds.clear(); return; }
    this.isLoading = true;
    this.permSvc.getPermissionsByProfil(profilId).pipe(takeUntil(this.destroy$))
      .subscribe({
        next: ids => { this.checkedMenuIds = new Set(ids); this.isLoading = false; },
        error: () => { this.isLoading = false; }
      });
  }

  isChecked(id: number): boolean { return this.checkedMenuIds.has(id); }

  toggle(node: MenuTree): void {
    const id = node.menu.id;
    if (this.checkedMenuIds.has(id)) {
      this.checkedMenuIds.delete(id);
      this.uncheckChildren(node);
    } else {
      this.checkedMenuIds.add(id);
      this.checkParent(node.menu.pereId);
    }
  }

  private uncheckChildren(node: MenuTree): void {
    node.children.forEach(c => {
      this.checkedMenuIds.delete(c.menu.id);
      this.uncheckChildren(c);
    });
  }

  private checkParent(pereId: number): void {
    if (!pereId || pereId === 0) return;
    this.checkedMenuIds.add(pereId);
    const parent = this.findNode(this.menuTree, pereId);
    if (parent) this.checkParent(parent.menu.pereId);
  }

  private findNode(tree: MenuTree[], id: number): MenuTree | null {
    for (const n of tree) {
      if (n.menu.id === id) return n;
      const found = this.findNode(n.children, id);
      if (found) return found;
    }
    return null;
  }

  toggleAll(node: MenuTree, checked: boolean): void {
    if (checked) {
      this.checkedMenuIds.add(node.menu.id);
      node.children.forEach(c => this.toggleAll(c, true));
    } else {
      this.checkedMenuIds.delete(node.menu.id);
      node.children.forEach(c => this.toggleAll(c, false));
    }
  }

  enregistrer(): void {
    if (!this.selectedProfilId) return;
    this.isSaving = true; this.saveSuccess = ''; this.saveError = '';
    this.permSvc.savePermissions(this.selectedProfilId, [...this.checkedMenuIds])
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.isSaving = false;
          this.saveSuccess = this.translate.instant('perm_save_success');
        },
        error: (err) => {
          this.isSaving = false;
          this.saveError = err?.error?.erreur ?? 'Erreur';
        }
      });
  }

  get totalChecked(): number { return this.checkedMenuIds.size; }
}
