import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { ParametreService } from '../../services/parametre.service';

interface ParamRow {
  cle:              string;
  valeur:           string;
  description:      string;
  dateModification: string;
  editing:          boolean;
  valeurEdit:       string;
  saving:           boolean;
  saved:            boolean;
  error:            string;
}

interface Categorie {
  titre:    string;
  icone:    string;
  couleur:  string;
  prefixes: string[];
  params:   ParamRow[];
}

@Component({
  selector: 'app-parametre',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule],
  templateUrl: './parametre.component.html',
  styleUrl: './parametre.component.css'
})
export class ParametreComponent implements OnInit, OnDestroy {

  params:           ParamRow[] = [];
  categories:       Categorie[] = [];
  isLoading         = true;
  erreurChargement  = '';

  private destroy$ = new Subject<void>();

  constructor(private parametreService: ParametreService) {}

  ngOnInit(): void { this.charger(); }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  charger(): void {
    this.isLoading       = true;
    this.erreurChargement = '';
    this.parametreService.getAll()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data) => {
          this.params = data.map(p => ({
            cle:              p.cle,
            valeur:           p.valeur,
            description:      p.description || '',
            dateModification: p.dateModification || '',
            editing:    false,
            valeurEdit: p.valeur,
            saving:     false,
            saved:      false,
            error:      ''
          }));
          this.construireCategories();
          this.isLoading = false;
        },
        error: () => {
          this.erreurChargement = 'Impossible de charger les paramètres.';
          this.isLoading = false;
        }
      });
  }

  private construireCategories(): void {
    const defs: Omit<Categorie, 'params'>[] = [
      { titre: 'param_cat_documents',      icone: 'fa-file-photo-o', couleur: '#3c8dbc', prefixes: ['DOCUMENT_'] },
      { titre: 'param_cat_notif_assure',   icone: 'fa-user',         couleur: '#00a65a', prefixes: ['NOTIF_ASSURE_'] },
      { titre: 'param_cat_notif_ss',       icone: 'fa-heartbeat',    couleur: '#f39c12', prefixes: ['NOTIF_SS_'] },
      { titre: 'param_cat_alertes_stats',  icone: 'fa-bar-chart',    couleur: '#dd4b39', prefixes: ['NOTIF_STATS_', 'NOTIF_ALERTE_', 'SEUIL_'] },
      { titre: 'param_cat_autres',         icone: 'fa-cog',          couleur: '#6c757d', prefixes: ['SESSION_', '__autres__'] },
    ];

    const assignes = new Set<string>();

    this.categories = defs.map(def => {
      const lignes = this.params.filter(p => {
        if (def.prefixes.includes('__autres__')) return !assignes.has(p.cle);
        return def.prefixes.some(pr => p.cle.startsWith(pr));
      });
      lignes.forEach(p => assignes.add(p.cle));
      return { ...def, params: lignes };
    }).filter(c => c.params.length > 0);
  }

  editer(p: ParamRow): void {
    this.params.forEach(x => { if (x !== p) { x.editing = false; x.error = ''; } });
    p.valeurEdit = p.valeur;
    p.editing = true;
    p.saved   = false;
    p.error   = '';
  }

  annuler(p: ParamRow): void { p.editing = false; p.error = ''; }

  toggleBoolean(p: ParamRow): void {
    const nouvelleValeur = p.valeur === 'true' ? 'false' : 'true';
    p.saving = true;
    this.parametreService.set(p.cle, nouvelleValeur)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (updated: any) => {
          p.valeur           = updated.valeur ?? nouvelleValeur;
          p.dateModification = updated.dateModification || p.dateModification;
          p.saving           = false;
          p.saved            = true;
          setTimeout(() => p.saved = false, 3000);
        },
        error: () => { p.saving = false; }
      });
  }

  sauvegarder(p: ParamRow): void {
    if (!p.valeurEdit.trim()) { p.error = 'La valeur ne peut pas être vide.'; return; }
    p.saving = true;
    p.error  = '';
    this.parametreService.set(p.cle, p.valeurEdit.trim())
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (updated: any) => {
          p.valeur           = updated.valeur;
          p.dateModification = updated.dateModification || p.dateModification;
          p.editing          = false;
          p.saving           = false;
          p.saved            = true;
          setTimeout(() => p.saved = false, 3000);
        },
        error: () => { p.error = 'Erreur lors de la sauvegarde.'; p.saving = false; }
      });
  }

  formatDate(d: string): string {
    if (!d) return '—';
    return new Date(d).toLocaleDateString('fr-FR', {
      day: '2-digit', month: '2-digit', year: 'numeric',
      hour: '2-digit', minute: '2-digit'
    });
  }

  isBoolean(valeur: string): boolean {
    const v = valeur?.toLowerCase().trim();
    return v === 'true' || v === 'false';
  }

  isNumber(valeur: string): boolean {
    const v = valeur?.trim();
    return !!v && !isNaN(Number(v));
  }
}
