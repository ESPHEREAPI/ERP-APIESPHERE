import { CommonModule }   from '@angular/common';
import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { Router, NavigationEnd, RouterModule } from '@angular/router';

import { MenuItem }      from 'primeng/api';
import { AuthService }   from '../../core/auth/auth.service';
import { StockService }  from '../../core/services/stock.service';
import { User }          from '../../core/model/user';
import { UserSession }   from '../../core/model/user-session';
import { Subscription, interval, distinctUntilChanged, filter } from 'rxjs';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterModule, CommonModule, TranslatePipe],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit, OnDestroy {

  @Input() user: User | null = null;
  imagePath    = 'img/user2-160x160.jpg';
  userSession: UserSession | null = null;
  profilType:  string | null = '';

  /** Clé du menu parent actuellement ouvert (null = tous fermés) */
  expandedMenu: string | null = null;

  toggleMenu(menuKey: string): void {
    this.expandedMenu = this.expandedMenu === menuKey ? null : menuKey;
  }

  isExpanded(menuKey: string): boolean {
    return this.expandedMenu === menuKey;
  }

  /** Retourne true si le menu doit être visible pour le profilAgent courant */
  canSeeMenu(menuKey: string): boolean {
    const p = this.userSession?.profilAgent;

    // Pas de profilAgent → montrer tout (enrichissement pas encore reçu ou user non-PAYLOAD)
    if (!p) return true;

    const rules: Record<string, string[]> = {
      'dashboard':         ['CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'],
      'dashboard-payload': ['CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'],
      'attestations':      ['PRODUCTEUR', 'SUPER_PRODUCTEUR', 'CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'],
      'historique':        ['PRODUCTEUR', 'SUPER_PRODUCTEUR', 'CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'],
      'stock':             ['CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'],
      'admin-agences':     ['ADMINISTRATEUR'],
      'utilisateurs':      ['ADMINISTRATEUR'],
      'parametres':        ['ADMINISTRATEUR'],
    };
    // Clé non répertoriée → toujours visible
    return rules[menuKey]?.includes(p) ?? true;
  }

  // ── Alerte stock ─────────────────────────────────────────────
  alerteStock: 'NON_INITIALISE' | 'CRITIQUE' | 'RUPTURE' | null = null;
  private stockSub?: Subscription;
  private sessionSub?: Subscription;

  constructor(
    private authService: AuthService,
    private stockService: StockService,
    private router: Router
  ) {
    // Ferme le menu ouvert lors d'une navigation (clic sur un item enfant)
    this.router.events.pipe(filter(e => e instanceof NavigationEnd))
      .subscribe(() => {
        // On garde le menu parent ouvert pour que l'item actif reste visible
        // (l'utilisateur voit toujours dans quel module il est)
      });
  }

  ngOnInit(): void {
    if (this.user) {
      this.imagePath = this.user.profileImageUrl ?? 'img/user2-160x160.jpg';
    } else {
      this.imagePath = 'img/user2-160x160.jpg';
    }

    // Souscription réactive — se met à jour quand enrichProfileAfterLogin() complète
    this.sessionSub = this.authService.currentUser$
      .pipe(distinctUntilChanged((a, b) =>
        a?.profilAgent === b?.profilAgent && a?.profilType === b?.profilType
      ))
      .subscribe(session => {
        this.userSession = session;
        this.profilType  = session?.profilType ?? '';

        // Alerte stock — (re)démarre seulement pour PAYLOAD
        this.stockSub?.unsubscribe();
        if (this.profilType === 'PAYLOAD') {
          this.verifierStockBureau();
          this.stockSub = interval(120_000).subscribe(() => this.verifierStockBureau());
        }
      });
  }

  ngOnDestroy(): void {
    this.stockSub?.unsubscribe();
    this.sessionSub?.unsubscribe();
  }

  private verifierStockBureau(): void {
    const officeCode = this.userSession?.agencyCode;
    if (!officeCode) return;
    this.stockService.getStocksParBureau(officeCode).subscribe({
      next: r => {
        const stocks = r.data ?? [];
        if (stocks.length === 0) {
          this.alerteStock = 'NON_INITIALISE';
        } else if (stocks.some(s => s.statut === 'RUPTURE')) {
          this.alerteStock = 'RUPTURE';
        } else if (stocks.some(s => s.statut === 'CRITIQUE')) {
          this.alerteStock = 'CRITIQUE';
        } else {
          this.alerteStock = null;
        }
      },
      error: () => { this.alerteStock = null; }
    });
  }

  /** Contrôle la visibilité d'un sous-menu par childKey */
  canSeeChild(childKey: string | undefined): boolean {
    if (!childKey) return true;
    const p = this.userSession?.profilAgent;
    if (!p) return true; // profil non encore chargé → montrer tout

    const rules: Record<string, string[]> = {
      'stock-dashboard':       ['CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'],
      'stock-liste':           ['ADMINISTRATEUR'],
      'stock-historique':      ['CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'],
      'stock-audit':           ['CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'],
      'cert-suspension':       ['PRODUCTEUR', 'SUPER_PRODUCTEUR', 'CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'],
      'cert-annulation':       ['SUPER_PRODUCTEUR', 'CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'],
      'cert-resiliation':      ['SUPER_PRODUCTEUR', 'CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'],
      'hist-suspensions':      ['PRODUCTEUR', 'SUPER_PRODUCTEUR', 'CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'],
      'hist-annulations':      ['SUPER_PRODUCTEUR', 'CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'],
      'hist-resiliations':     ['SUPER_PRODUCTEUR', 'CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'],
    };
    return rules[childKey]?.includes(p) ?? true;
  }

  hasPermission(item: MenuItem): boolean  { return true; }
  hasChildPermissions(item: MenuItem): boolean { return true; }
  toggleExpanded(item: MenuItem): void { item.expanded = !item.expanded; }

  menuItems: Array<{
    menuKey: string;
    titleKey: string;
    icon: string;
    route: string;
    profilType: string;
    children: Array<{ titleKey: string; icon: string; route: string; childKey?: string }>;
  }> = [
    {
      menuKey: 'dashboard',
      titleKey: 'SIDEBAR.DASHBOARD',
      icon: 'fas fa-tachometer-alt',
      route: '/dashboard',
      children: [],
      profilType: ''
    },
    {
      menuKey: 'dashboard-payload',
      titleKey: 'SIDEBAR.DASHBOARD_PAYLOAD',
      icon: 'fas fa-tachometer-alt',
      route: '/dashboard-payload',
      children: [],
      profilType: 'PAYLOAD'
    },
    {
      menuKey: 'attestations',
      titleKey: 'SIDEBAR.ATTESTATIONS',
      icon: 'fas fa-file-contract',
      route: '',
      children: [
        { titleKey: 'SIDEBAR.VALIDATION',  icon: 'pi pi-verified',      route: '/certificates/validation' },
        { titleKey: 'SIDEBAR.PRODUCTIONS', icon: 'fas fa-certificate',  route: '/certificates' },
        { titleKey: 'SIDEBAR.DOWNLOADS',   icon: 'fas fa-file-pdf',     route: '/certificates/downloads' },
        { titleKey: 'SIDEBAR.SUSPENSION',  icon: 'fas fa-pause-circle',    route: '/certificates/suspension',  childKey: 'cert-suspension' },
        { titleKey: 'SIDEBAR.ANNULATION',  icon: 'fas fa-times-circle',    route: '/certificates/annulation',  childKey: 'cert-annulation' },
        { titleKey: 'SIDEBAR.RESILIATION', icon: 'fas fa-file-contract',   route: '/certificates/resiliation', childKey: 'cert-resiliation' },
      ],
      profilType: 'PAYLOAD'
    },
    {
      menuKey: 'historique',
      titleKey: 'SIDEBAR.HISTORIQUE',
      icon: 'fas fa-history',
      route: '',
      children: [
        { titleKey: 'SIDEBAR.HIST_SUSPENSIONS',  icon: 'fas fa-pause-circle',  route: '/historique/suspensions',  childKey: 'hist-suspensions' },
        { titleKey: 'SIDEBAR.HIST_ANNULATIONS',  icon: 'fas fa-times-circle',  route: '/historique/annulations',  childKey: 'hist-annulations' },
        { titleKey: 'SIDEBAR.HIST_RESILIATIONS', icon: 'fas fa-file-contract', route: '/historique/resiliations', childKey: 'hist-resiliations' },
      ],
      profilType: 'PAYLOAD'
    },
    {
      menuKey: 'stock',
      titleKey: 'SIDEBAR.STOCK',
      icon: 'fas fa-boxes',
      route: '',
      children: [
        { titleKey: 'SIDEBAR.STOCK_DASHBOARD', icon: 'fas fa-chart-pie',      route: '/stock',           childKey: 'stock-dashboard' },
        { titleKey: 'SIDEBAR.STOCK_LIST',      icon: 'fas fa-warehouse',     route: '/stock/liste',      childKey: 'stock-liste' },
        { titleKey: 'SIDEBAR.STOCK_HISTORY',   icon: 'fas fa-history',       route: '/stock/historique', childKey: 'stock-historique' },
        { titleKey: 'SIDEBAR.STOCK_AUDIT',     icon: 'fas fa-balance-scale', route: '/stock/audit',      childKey: 'stock-audit' }
      ],
      profilType: 'PAYLOAD'
    },
    {
      menuKey: 'admin-agences',
      titleKey: 'SIDEBAR.AGENCIES',
      icon: 'fas fa-building',
      route: '/admin-agences',
      children: [],
      profilType: 'PAYLOAD'
    },
    {
      menuKey: 'utilisateurs',
      titleKey: 'SIDEBAR.USERS',
      icon: 'fas fa-users',
      route: '',
      children: [
        { titleKey: 'SIDEBAR.USER_LIST',   icon: 'far fa-circle', route: '/users' },
        { titleKey: 'SIDEBAR.ROLES',       icon: 'far fa-circle', route: '/role' },
        { titleKey: 'SIDEBAR.PERMISSIONS', icon: 'far fa-circle', route: '/permission' }
      ],
      profilType: ''
    },
    {
      menuKey: 'parametres',
      titleKey: 'SIDEBAR.SETTINGS',
      icon: 'fas fa-cogs',
      route: '',
      children: [
        { titleKey: 'SIDEBAR.GENERAL',  icon: 'far fa-circle', route: '/general' },
        { titleKey: 'SIDEBAR.SECURITY', icon: 'far fa-circle', route: '/security' }
      ],
      profilType: ''
    }
  ];
}
