/**
 * header.component.ts
 * ─────────────────────────────────────────────────────────────────────────────
 * Composant header AdminLTE 3 / Angular 18.
 *
 * Alertes police :
 *  - Clignotement CSS du badge (expired = rapide, warning = lent)
 *  - Halo lumineux pulsant autour de l'avatar
 *  - Vibration initiale (shake) au premier rendu si statut dégradé
 *  - Bannière fixe sous le header (dismissible)
 *  - Son d'alerte généré via Web Audio API (aucun fichier externe)
 *    → 3 bips descendants pour "expired", 1 bip doux pour "warning"
 *    → Joué UNE SEULE FOIS au chargement, puis peut être rejoué
 *      manuellement depuis la bannière
 * ─────────────────────────────────────────────────────────────────────────────
 */
import {
  Component, OnInit, OnDestroy,
  ChangeDetectionStrategy, ChangeDetectorRef,
} from '@angular/core';
import { Router, RouterLink }   from '@angular/router';
import { CommonModule }          from '@angular/common';
import { Subject, takeUntil }    from 'rxjs';
import { AuthService }           from '../../core/auth/auth.service';
import { UserSession } from '../../core/models/user-session';
import { User } from '../../core/models/user';


const EXPIRY_WARN_DAYS = 30;

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HeaderComponent implements OnInit, OnDestroy {

  private destroy$ = new Subject<void>();

  // ── Session ───────────────────────────────────────────────────────────────
  currentSession: UserSession | null = null;
  loggingOut = false;

  // ── Avatar ────────────────────────────────────────────────────────────────
  initiales   = '';
  avatarColor = '#1a56db';

  // ── Statut police ─────────────────────────────────────────────────────────
  policeStatus: 'valid' | 'warning' | 'expired' = 'valid';
  joursRestants = 0;

  // ── Dropdowns ─────────────────────────────────────────────────────────────
  showUserDropdown  = false;
  showNotifDropdown = false;
  showMsgDropdown   = false;

  // ── Alertes visuelles & sonores ───────────────────────────────────────────
  /**
   * showBanner : true tant que la bannière n'a pas été fermée par l'utilisateur.
   * Remise à true si la session change et que le statut reste dégradé.
   */
  showBanner = false;

  /**
   * shakeActive : déclenche la classe .shake-once sur l'avatar.
   * Mis à true pendant 700ms puis remis à false pour rejouer si besoin.
   */
  shakeActive = false;

  /**
   * audioPlayed : garde en mémoire si le son a déjà été joué pour cette session.
   * Évite de rejouer à chaque détection de changement OnPush.
   */
  private audioPlayed = false;

  /** Contexte Web Audio API — créé à la demande (lazy) */
  private audioCtx: AudioContext | null = null;

  private readonly AVATAR_PALETTE = [
    '#1a56db', '#0e9f6e', '#d61f69', '#ff5a1f',
    '#6875f5', '#057a55', '#9061f9', '#c27803',
  ];

  constructor(
    private authService: AuthService,
    private router:      Router,
    private cdr:         ChangeDetectorRef,
  ) {}

  // ════════════════════════════════════════════════════════════════════════════
  // Cycle de vie
  // ════════════════════════════════════════════════════════════════════════════

  ngOnInit(): void {
    this.authService.currentUser$
      .pipe(takeUntil(this.destroy$))
      .subscribe((session: UserSession | null) => {
        this.currentSession = session;
        const dto = session?.usersDTO ?? null;

        if (dto) {
          this.initiales   = this.buildInitiales(dto.lastname);
          this.avatarColor = this.buildAvatarColor(dto.lastname);
          this.computePoliceStatus(dto.echeance ?? '');

          // ── Déclencher alertes si statut dégradé ──────────────────────────
          if (this.policeStatus !== 'valid') {
            this.triggerAlerts();
          }
        } else {
          this.resetState();
        }

        this.cdr.markForCheck();
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    // Fermer le contexte audio pour libérer les ressources
    this.audioCtx?.close();
  }

  // ════════════════════════════════════════════════════════════════════════════
  // Getter de commodité vers usersDTO
  // ════════════════════════════════════════════════════════════════════════════

  /** Raccourci vers usersDTO — utilisé dans tout le template */
  get user(): User | null {
    return this.currentSession?.usersDTO ?? null;
  }

  // ════════════════════════════════════════════════════════════════════════════
  // Alertes : visuelle + sonore
  // ════════════════════════════════════════════════════════════════════════════

  /**
   * triggerAlerts — orchestrateur des alertes.
   * Appelé une fois à la détection d'un statut dégradé.
   * Les CSS (clignotement, halo) sont appliquées en continu via les classes
   * dynamiques dans le template — triggerAlerts gère uniquement
   * les effets ONE-SHOT (son + shake + bannière).
   */
  private triggerAlerts(): void {
    // Bannière : toujours affichée jusqu'à fermeture manuelle
    this.showBanner = true;

    // Vibration initiale — rejouée à chaque nouvelle session dégradée
    this.playShake();

    // Son — joué une seule fois par session pour ne pas agacer
    if (!this.audioPlayed) {
      this.audioPlayed = true;
      // Délai de 800ms : laisse Angular finir le rendu avant de jouer le son
      setTimeout(() => this.playAlertSound(), 800);
    }
  }

  /**
   * playShake — active l'animation de vibration sur l'avatar pendant 700ms.
   * La classe .shake-once est retirée après l'animation pour pouvoir
   * être réappliquée si nécessaire.
   */
  private playShake(): void {
    this.shakeActive = false;
    this.cdr.markForCheck();

    // Re-trigger l'animation au prochain cycle
    setTimeout(() => {
      this.shakeActive = true;
      this.cdr.markForCheck();

      // Retire la classe après la durée de l'animation (600ms)
      setTimeout(() => {
        this.shakeActive = false;
        this.cdr.markForCheck();
      }, 700);
    }, 50);
  }

  /**
   * playAlertSound — génère un son d'alerte via Web Audio API.
   * Aucun fichier audio externe requis.
   *
   * Police EXPIRÉE  → 3 bips descendants urgents (do-si-la, 880→660→440 Hz)
   * Police WARNING  → 1 bip doux montant puis descendant (beep court)
   *
   * Les navigateurs modernes bloquent l'AudioContext sans interaction
   * utilisateur préalable. On crée le contexte ici (lazy) pour maximiser
   * les chances qu'une interaction ait déjà eu lieu (login = clic).
   */
  playAlertSound(): void {
    try {
      // Créer le contexte si absent ou suspendu
      if (!this.audioCtx || this.audioCtx.state === 'closed') {
        this.audioCtx = new AudioContext();
      }
      if (this.audioCtx.state === 'suspended') {
        this.audioCtx.resume();
      }

      if (this.policeStatus === 'expired') {
        this.playExpiredSound(this.audioCtx);
      } else if (this.policeStatus === 'warning') {
        this.playWarningSound(this.audioCtx);
      }
    } catch (e) {
      // Web Audio API non disponible (très ancien navigateur) — silencieux
      console.warn('[HeaderComponent] Web Audio API non disponible :', e);
    }
  }

  /**
   * playExpiredSound — 3 bips descendants de plus en plus graves.
   * Évoque un compte à rebours terminé / alarme urgente.
   * Fréquences : 880 Hz → 660 Hz → 440 Hz (intervalles de 0.35s)
   */
  private playExpiredSound(ctx: AudioContext): void {
    const notes = [
      { freq: 880, start: 0.0,  dur: 0.18 },   // DO aigu
      { freq: 660, start: 0.35, dur: 0.18 },   // SOL
      { freq: 440, start: 0.70, dur: 0.35 },   // LA (plus long, final)
    ];

    notes.forEach(note => {
      const osc    = ctx.createOscillator();
      const gainNode = ctx.createGain();

      osc.connect(gainNode);
      gainNode.connect(ctx.destination);

      osc.type      = 'sine';
      osc.frequency.setValueAtTime(note.freq, ctx.currentTime + note.start);

      // Enveloppe : attaque rapide, déclin progressif
      gainNode.gain.setValueAtTime(0, ctx.currentTime + note.start);
      gainNode.gain.linearRampToValueAtTime(0.55, ctx.currentTime + note.start + 0.02);
      gainNode.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + note.start + note.dur);

      osc.start(ctx.currentTime + note.start);
      osc.stop(ctx.currentTime  + note.start + note.dur + 0.05);
    });
  }

  /**
   * playWarningSound — un double bip doux montant.
   * Moins agressif que expired, évoque une notification.
   * Fréquences : 523 Hz → 659 Hz (DO → MI)
   */
  private playWarningSound(ctx: AudioContext): void {
    const notes = [
      { freq: 523, start: 0.0,  dur: 0.15 },   // DO moyen
      { freq: 659, start: 0.22, dur: 0.20 },   // MI (montée douce)
    ];

    notes.forEach(note => {
      const osc      = ctx.createOscillator();
      const gainNode = ctx.createGain();

      osc.connect(gainNode);
      gainNode.connect(ctx.destination);

      osc.type = 'sine';
      osc.frequency.setValueAtTime(note.freq, ctx.currentTime + note.start);

      gainNode.gain.setValueAtTime(0, ctx.currentTime + note.start);
      gainNode.gain.linearRampToValueAtTime(0.3, ctx.currentTime + note.start + 0.02);
      gainNode.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + note.start + note.dur);

      osc.start(ctx.currentTime + note.start);
      osc.stop(ctx.currentTime  + note.start + note.dur + 0.05);
    });
  }

  /** Ferme la bannière d'alerte (action utilisateur) */
  dismissBanner(event: Event): void {
    event.stopPropagation();
    this.showBanner = false;
    this.cdr.markForCheck();
  }

  /** Rejoue le son manuellement (bouton dans la bannière) */
  replaySound(event: Event): void {
    event.stopPropagation();
    this.audioPlayed = false; // réautoriser la lecture
    this.playAlertSound();
    this.audioPlayed = true;
  }

  // ════════════════════════════════════════════════════════════════════════════
  // Calculs internes
  // ════════════════════════════════════════════════════════════════════════════

  private resetState(): void {
    this.initiales     = '?';
    this.avatarColor   = '#1a56db';
    this.policeStatus  = 'valid';
    this.joursRestants = 0;
    this.showBanner    = false;
    this.audioPlayed   = false;
  }

  private buildInitiales(fullName?: string): string {
    if (!fullName?.trim()) return '?';
    const parts = fullName.trim().split(/[\s\-]+/).filter(p => p.length > 0);
    if (parts.length === 1) return parts[0][0].toUpperCase();
    return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
  }

  private buildAvatarColor(name?: string): string {
    if (!name) return this.AVATAR_PALETTE[0];
    const hash = name.split('').reduce((acc, ch) => acc + ch.charCodeAt(0), 0);
    return this.AVATAR_PALETTE[hash % this.AVATAR_PALETTE.length];
  }

  private computePoliceStatus(echeanceStr: string): void {
    if (!echeanceStr) { this.policeStatus = 'valid'; return; }
    const echeance = this.parseDate(echeanceStr);
    if (!echeance)   { this.policeStatus = 'valid'; return; }

    const today = new Date();
    today.setHours(0, 0, 0, 0);
    echeance.setHours(0, 0, 0, 0);

    const diffDays = Math.ceil(
      (echeance.getTime() - today.getTime()) / (1000 * 60 * 60 * 24)
    );
    this.joursRestants = diffDays;

    if (diffDays < 0)                      this.policeStatus = 'expired';
    else if (diffDays <= EXPIRY_WARN_DAYS) this.policeStatus = 'warning';
    else                                   this.policeStatus = 'valid';
  }

  private parseDate(dateStr: string): Date | null {
    if (!dateStr) return null;
    if (dateStr.includes('/')) {
      const [day, month, year] = dateStr.split('/').map(Number);
      const d = new Date(year, month - 1, day);
      return isNaN(d.getTime()) ? null : d;
    }
    const d = new Date(dateStr);
    return isNaN(d.getTime()) ? null : d;
  }

  // ════════════════════════════════════════════════════════════════════════════
  // Getters template
  // ════════════════════════════════════════════════════════════════════════════

  get statusLabel(): string {
    if (this.policeStatus === 'expired')
      return `Police expirée depuis ${Math.abs(this.joursRestants)} jour(s)`;
    if (this.policeStatus === 'warning')
      return `Expire dans ${this.joursRestants} jour(s)`;
    return 'Police valide';
  }

  get statusBadgeClass(): string {
    return { valid: 'badge-success', warning: 'badge-warning', expired: 'badge-danger' }
      [this.policeStatus];
  }

  get statusIcon(): string {
    return {
      valid:   'fas fa-shield-alt',
      warning: 'fas fa-exclamation-triangle',
      expired: 'fas fa-times-circle',
    }[this.policeStatus];
  }

  get truncatedName(): string {
    const name = this.user?.lastname ?? '';
    return name.length > 22 ? name.slice(0, 22) + '…' : name;
  }

  // ════════════════════════════════════════════════════════════════════════════
  // Actions
  // ════════════════════════════════════════════════════════════════════════════

  closeAllDropdowns(): void {
    this.showUserDropdown = this.showNotifDropdown = this.showMsgDropdown = false;
    this.cdr.markForCheck();
  }

  logout(event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    if (this.loggingOut) return;
    if (!confirm('Êtes-vous sûr de vouloir vous déconnecter ?')) return;

    this.loggingOut = true;
    this.closeAllDropdowns();
    this.cdr.markForCheck();

    this.authService.logout()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next:     (r) => { console.log('[Header] logout OK', r); this.router.navigate(['/login'], { replaceUrl: true }); },
        error:    (e) => { console.error('[Header] logout error', e); this.router.navigate(['/login'], { replaceUrl: true }); },
        complete: ()  => { this.loggingOut = false; this.cdr.markForCheck(); },
      });
  }
}