import { Injectable, computed, signal } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

export type AppLang = 'fr' | 'en';

const STORAGE_KEY = 'esphere_lang';
const SUPPORTED: AppLang[] = ['fr', 'en'];

@Injectable({ providedIn: 'root' })
export class LanguageService {

  /** Signal local pour forcer la détection de changement dans les templates */
  private _lang = signal<AppLang>('fr');
  readonly currentLang = computed(() => this._lang());

  constructor(private translate: TranslateService) {}

  /** Appelé une seule fois au démarrage (app.component.ts) */
  init(): void {
    const lang = this.resolve();
    this._lang.set(lang);
    this.translate.use(lang);
  }

  get current(): AppLang {
    return this._lang();
  }

  use(lang: AppLang): void {
    localStorage.setItem(STORAGE_KEY, lang);
    this._lang.set(lang);
    this.translate.use(lang);
  }

  /** Résout la langue : stockée > navigateur > fr */
  private resolve(): AppLang {
    const stored = localStorage.getItem(STORAGE_KEY) as AppLang | null;
    if (stored && SUPPORTED.includes(stored)) return stored;

    const browser = (navigator.language || '').slice(0, 2).toLowerCase() as AppLang;
    return SUPPORTED.includes(browser) ? browser : 'fr';
  }
}
