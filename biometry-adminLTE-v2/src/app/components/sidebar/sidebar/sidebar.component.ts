import { Component, OnInit, OnDestroy, HostListener, Renderer2 } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, NavigationEnd } from '@angular/router';
import { filter, takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { TranslateModule } from '@ngx-translate/core';
import { AuthService } from '../../../auth/auth.service';
import { User } from '../../../models/user';

interface MenuItem {
  title: string;
  icon: string;
  route?: string;
  children?: MenuItem[];
  badgeCount?: number;
  badgeBlink?: boolean;
  isOpen?: boolean;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule, TranslateModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit, OnDestroy {

  user: User | null = null;
  currentRoute = '';
  isMobile = false;
  menuItems: MenuItem[] = [];

  consultationsEnAttente = 0;
  ordonnancesEnAttente = 0;
  examensEnAttente = 0;
  // Ajoutez ces propriétés
consultationsValides  = 0;
ordonnancesValidees   = 0;
examensValides        = 0;
private prestataireId = '';

  private destroy$ = new Subject<void>();
  private refreshInterval: any;
  private soundInterval: any = null;
  private alertAudio: HTMLAudioElement | null = null;
  private previousTotal = 0;

  constructor(
    private router: Router,
    private renderer: Renderer2,
    private authService: AuthService,
    private http: HttpClient
  ) { }

  ngOnInit(): void {
    this.user = this.authService.getStoredUser();
    this.prestataireId = this.user?.prestataireId || '';
    this.buildMenu();
    this.checkScreenSize();
    this.initAudio();

    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      takeUntil(this.destroy$)
    ).subscribe((event: any) => {
      this.currentRoute = event.url;
      this.openParentOfActiveRoute();
      if (this.isMobile) this.closeSidebarOnMobile();
    });

    

    // Charger les compteurs immédiatement puis toutes les 60 secondes
    this.loadBadgeCounts();
    this.refreshInterval = setInterval(() => this.loadBadgeCounts(), 120000);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    if (this.refreshInterval) clearInterval(this.refreshInterval);
    this.stopAlertLoop();
    if (this.alertAudio) {
      this.alertAudio.pause();
      this.alertAudio.src = '';
      this.alertAudio = null;
    }
  }

  // ══════════════════════════════════════════════════════════
  // AUDIO — lecture de public/assets/sounds/alert.mp3
  // ══════════════════════════════════════════════════════════

  private initAudio(): void {
    // Récupère le baseHref défini dans angular.json (ex: /biometry/)
    // pour construire le chemin complet vers le fichier MP3
    const base = document.querySelector('base')?.getAttribute('href') || '/';
    this.alertAudio = new Audio(`${base}assets/sounds/alert.mp3`);
    this.alertAudio.preload = 'auto';
    this.alertAudio.volume = 1.0;

    // Déverrouillage obligatoire sur Chrome/Edge/iOS :
    // les navigateurs bloquent l'audio sans interaction utilisateur préalable.
    // On joue 0ms puis on met en pause pour débloquer le contexte audio.
    const unlock = () => {
      if (!this.alertAudio) return;
      this.alertAudio.play()
        .then(() => {
          this.alertAudio!.pause();
          this.alertAudio!.currentTime = 0;
        })
        .catch(() => { /* déjà déverrouillé */ });
    };
    document.addEventListener('click', unlock, { once: true });
  }

  private playAlertSound(): void {
    if (!this.alertAudio) return;
    this.alertAudio.currentTime = 0;
    this.alertAudio.play().catch(err => {
      console.warn('⚠️ Lecture audio bloquée :', err);
    });
  }

  private startAlertLoop(): void {
    if (this.soundInterval) return;
    this.playAlertSound();
    this.soundInterval = setInterval(() => {
      this.playAlertSound();
    }, 120000);
  }

  private stopAlertLoop(): void {
    if (this.soundInterval) {
      clearInterval(this.soundInterval);
      this.soundInterval = null;
    }
    if (this.alertAudio && !this.alertAudio.paused) {
      this.alertAudio.pause();
      this.alertAudio.currentTime = 0;
    }
  }

  // ══════════════════════════════════════════════════════════
  // BADGES ET COMPTEURS
  // ══════════════════════════════════════════════════════════

 /**  private loadBadgeCounts(): void {
    const profilCode = this.user?.profilCode || '';
    const isSS = ['SERVICE_SANTE', 'SUP_ADMIN'].includes(profilCode);
    if (!isSS) return;

    const employeId = this.user?.utilisateurId;
    const annee = new Date().getFullYear();

    this.http.get<any>(`/reporting/dashboard/ss/${employeId}?annee=${annee}`)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: data => this.updateBadges(data),
        error: () => { }
      });
  }*/

  // Remplacez loadBadgeCounts()
private loadBadgeCounts(): void {
    const profilCode = this.user?.profilCode || '';
    const isSS = ['SERVICE_SANTE',
                  'SUP_ADMIN'].includes(profilCode);
    const isPrestataire = !isSS;

    const bgHeaders = { 'X-Background-Poll': 'true' };

    // ── Service Santé → prestations en attente ──────────
    if (isSS) {
        const employeId = this.user?.utilisateurId;
        const annee     = new Date().getFullYear();
        this.http.get<any>(
            `/reporting/dashboard/ss/${employeId}?annee=${annee}`,
            { headers: bgHeaders })
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next:  d => this.updateBadgesSS(d),
                error: () => {}
            });
    }

    // ── Prestataire → prestations validées à encaisser ──
    if (isPrestataire && this.prestataireId) {
        this.http.get<any>(
            `/validations/dashboard/prestataire/${this.prestataireId}/a-encaisser`,
            { headers: bgHeaders })
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next:  d => this.updateBadgesPrestataire(d),
                error: () => {}
            });
    }
}

// Renommez updateBadges en updateBadgesSS
private updateBadgesSS(data: any): void {
    this.consultationsEnAttente =
        data.consultationsEnAttente || 0;
    this.ordonnancesEnAttente   =
        data.ordonnancesEnAttente   || 0;
    this.examensEnAttente       =
        data.examensEnAttente       || 0;

    const total = this.consultationsEnAttente
                + this.ordonnancesEnAttente
                + this.examensEnAttente;

    if (total > 0) this.startAlertLoop();
    else           this.stopAlertLoop();

    this.menuItems.forEach(item => {
        if (item.title === 'prestation'
                && item.children) {
            item.badgeCount = total > 0
                ? total : undefined;
            item.badgeBlink = total > 0;

            item.children.forEach(child => {
                if (child.title === 'consultation') {
                    child.badgeCount =
                        this.consultationsEnAttente > 0
                        ? this.consultationsEnAttente
                        : undefined;
                    child.badgeBlink =
                        this.consultationsEnAttente > 0;
                }
                if (child.title === 'ordonnance') {
                    child.badgeCount =
                        this.ordonnancesEnAttente > 0
                        ? this.ordonnancesEnAttente
                        : undefined;
                    child.badgeBlink =
                        this.ordonnancesEnAttente > 0;
                }
                if (child.title === 'examen') {
                    child.badgeCount =
                        this.examensEnAttente > 0
                        ? this.examensEnAttente
                        : undefined;
                    child.badgeBlink =
                        this.examensEnAttente > 0;
                }
            });
        }
    });
}

// Ajoutez cette nouvelle méthode
private updateBadgesPrestataire(data: any): void {
  console.log("data in sidebar", data);
    this.consultationsValides =
        data.consultationsValides  || 0;
    this.ordonnancesValidees  =
        data.ordonnancesValidees   || 0;
    this.examensValides       =
        data.examensValides        || 0;

    const total = this.consultationsValides
                + this.ordonnancesValidees
                + this.examensValides;

    // Alerte sonore si nouvelles prestations validées
    if (total > 0) this.startAlertLoop();
    else           this.stopAlertLoop();

    // Mise à jour badges menu prestataire
    this.menuItems.forEach(item => {
        if (item.title === 'consultation') {
            item.badgeCount =
                this.consultationsValides > 0
                ? this.consultationsValides : undefined;
            item.badgeBlink =
                this.consultationsValides > 0;
        }
        if (item.title === 'ordonnance') {
            item.badgeCount =
                this.ordonnancesValidees > 0
                ? this.ordonnancesValidees : undefined;
            item.badgeBlink =
                this.ordonnancesValidees > 0;
        }
        if (item.title === 'examen') {
            item.badgeCount =
                this.examensValides > 0
                ? this.examensValides : undefined;
            item.badgeBlink =
                this.examensValides > 0;
        }
    });
}

 /**  private updateBadges(data: any): void {
    this.consultationsEnAttente = data.consultationsEnAttente || 0;
    this.ordonnancesEnAttente = data.ordonnancesEnAttente || 0;
    this.examensEnAttente = data.examensEnAttente || 0;

    const total = this.consultationsEnAttente
      + this.ordonnancesEnAttente
      + this.examensEnAttente;

    this.previousTotal = total;

    if (total > 0) {
      this.startAlertLoop();
    } else {
      this.stopAlertLoop();
    }

    // Mise à jour des badges dans le menu
    this.menuItems.forEach(item => {
      if (item.title === 'prestation' && item.children) {
        item.badgeCount = total > 0 ? total : undefined;
        item.badgeBlink = total > 0;

        item.children.forEach(child => {
          if (child.title === 'consultation') {
            child.badgeCount = this.consultationsEnAttente > 0
              ? this.consultationsEnAttente : undefined;
            child.badgeBlink = this.consultationsEnAttente > 0;
          }
          if (child.title === 'ordonnance') {
            child.badgeCount = this.ordonnancesEnAttente > 0
              ? this.ordonnancesEnAttente : undefined;
            child.badgeBlink = this.ordonnancesEnAttente > 0;
          }
          if (child.title === 'examen') {
            child.badgeCount = this.examensEnAttente > 0
              ? this.examensEnAttente : undefined;
            child.badgeBlink = this.examensEnAttente > 0;
          }
        });
      }
    });
  } */

  // ══════════════════════════════════════════════════════════
  // MENU
  // ══════════════════════════════════════════════════════════

  private buildMenu(): void {
    const profilCode = this.user?.profilCode || '';
    const isPrestataire = !['SERVICE_SANTE', 'SUP_ADMIN'].includes(profilCode);

    const base: MenuItem[] = [
      { title: 'accueil', icon: 'fa fa-home', route: '/public/admin/accueil' }
    ];

    if (profilCode === 'SUP_ADMIN') {
      base.push(
        {
          title: 'administration',
          icon: 'fa fa-users',
          isOpen: false,
          children: [
            { title: 'employe', icon: 'fa fa-circle-o', route: '/public/admin/administration/employe' },
            { title: 'profil', icon: 'fa fa-circle-o', route: '/public/admin/administration/profil' },
            { title: 'permission', icon: 'fa fa-circle-o', route: '/public/admin/administration/permission' },
            { title: 'prestataire', icon: 'fa fa-circle-o', route: '/public/admin/administration/prestataire' }
          ]
        },
        { title: 'menu', icon: 'fa fa-list', route: '/public/admin/menu' },
        { title: 'parametre', icon: 'fa fa-cog', route: '/public/admin/parametre' },
        {
          title: 'regionalisation',
          icon: 'fa fa-map-marker',
          isOpen: false,
          children: [
            { title: 'ville', icon: 'fa fa-circle-o', route: '/public/admin/regionalisation/ville' }
          ]
        }
      );
    }
    if (isPrestataire) {
      if (profilCode === 'LABORATOIRE') {
        base.push(
          { title: 'examen', icon: 'fa fa-flask', route: '/public/admin/examen' }
        );
      } else if (profilCode === 'PHARMACIE') {
        base.push(
          { title: 'ordonnance', icon: 'fa fa-file-text-o', route: '/public/admin/ordonnance' }
        );
      } else {
        base.push(
          { title: 'consultation', icon: 'fa fa-heartbeat', route: '/public/admin/consultation' },
          { title: 'ordonnance', icon: 'fa fa-file-text-o', route: '/public/admin/ordonnance' },
          { title: 'examen', icon: 'fa fa-flask', route: '/public/admin/examen' }
        );
      }

      base.push({
        title: 'menu_reporting',
        icon: 'fa fa-bar-chart',
        isOpen: false,
        children: [
          { title: 'menu_etat_prestations', icon: 'fa fa-list-alt', route: '/public/admin/reporting/mes-prestations' }
        ]
      });

    } else {
      base.push({
        title: 'prestation',
        icon: 'fa fa-check-circle',
        isOpen: true,
        children: [
          { title: 'consultation', icon: 'fa fa-circle-o', route: '/public/admin/consultation' },
          { title: 'ordonnance', icon: 'fa fa-circle-o', route: '/public/admin/ordonnance' },
          { title: 'examen', icon: 'fa fa-circle-o', route: '/public/admin/examen' }
        ]
      });
    }



    this.menuItems = base;
  }

  // ══════════════════════════════════════════════════════════
  // UTILITAIRES
  // ══════════════════════════════════════════════════════════

  getInitiales(): string {
    if (!this.user) return '?';
    const p = this.user.prenom?.charAt(0) || '';
    const n = this.user.nom?.charAt(0) || '';
    return (p + n).toUpperCase() || '?';
  }

  @HostListener('window:resize')
  onResize(): void { this.checkScreenSize(); }

  checkScreenSize(): void { this.isMobile = window.innerWidth < 768; }

  isActive(route: string): boolean { return this.currentRoute === route; }

  hasActiveChild(item: MenuItem): boolean {
    return item.children?.some(c => c.route === this.currentRoute) || false;
  }

  openParentOfActiveRoute(): void {
    this.menuItems.forEach(item => {
      if (item.children?.some(c => c.route === this.currentRoute)) {
        item.isOpen = true;
      }
    });
  }

  toggleSubmenu(item: MenuItem, event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    if (item.children) item.isOpen = !item.isOpen;
  }

  closeSidebarOnMobile(): void {
    if (this.isMobile) this.renderer.removeClass(document.body, 'sidebar-open');
  }
  isPrestataire(): boolean {
    const profilCode = this.user?.profilCode || '';
    //console.log(profilCode)
    return !['SERVICE_SANTE',
             'SUP_ADMIN'].includes(profilCode);
}
}