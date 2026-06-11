import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { AuthService } from '../auth/auth.service';
import { ProfilType } from '../../shared/enums/ProfilType';
import { UserSession } from '../models/user-session';

/**
 * SessionContext — source de vérité sur l'utilisateur connecté.
 *
 * Centralise la lecture de la session pour éviter que chaque composant
 * aille lui-même piocher dans localStorage ou dans usersDTO.
 *
 * Utilisation :
 *   constructor(private session: SessionContext) {}
 *
 *   this.session.codeSouscripteur$   // Observable<string>
 *   this.session.profilType$         // Observable<ProfilType | null>
 *   this.session.isSouscripteur()    // boolean
 *   this.session.isDII()             // boolean
 */
@Injectable({ providedIn: 'root' })
export class SessionContext {

  constructor() {}

  // ── Observables dérivés de currentUser$ ───────────────────────────────────

  /** Session complète (null si non connecté) */
  //readonly session$: Observable<UserSession | null> =
    ///this.authService.currentUser$;
    private authService = inject(AuthService);

readonly session$: Observable<UserSession | null> =
    this.authService.currentUser$;

  /** ProfilType de l'utilisateur connecté */
  readonly profilType$: Observable<ProfilType | null> = this.session$.pipe(
    map(s => s?.usersDTO?.profilType ?? null)
  );

  /**
   * Code souscripteur résolu selon le profil :
   * - SOUSCRIPTEUR → userName (numéro de police = identifiant unique)
   * - Autres profils → chaîne vide (ils passent le code en paramètre de route)
   */
  readonly codeSouscripteur$: Observable<string> = this.session$.pipe(
    map(s => {
      if (!s?.usersDTO) return '';
      const profil = s.usersDTO.profilType;
      if (profil === ProfilType.SOUSCRIPTEUR) {
        return s.usersDTO.userName ?? '';
      }
      return '';
    })
  );

  /** Nom d'affichage de l'utilisateur connecté */
  readonly displayName$: Observable<string> = this.session$.pipe(
    map(s => {
      const u = s?.usersDTO;
      if (!u) return '';
      return [u.firstName, u.lastname ?? u.lastName]
        .filter(Boolean)
        .join(' ') || u.userName || '';
    })
  );

  // ── Accesseurs synchrones (pour les gardes de route) ─────────────────────

  get currentSession(): UserSession | null {
    return this.authService.currentUserValue;
  }

  get profilType(): ProfilType | null {
    return this.currentSession?.usersDTO?.profilType ?? null;
  }

  get codeSouscripteur(): string {
    const s = this.currentSession;
    if (!s?.usersDTO) return '';
    if (s.usersDTO.profilType === ProfilType.SOUSCRIPTEUR) {
      return s.usersDTO.userName ?? '';
    }
    return '';
  }

  get userName(): string {
    return this.currentSession?.usersDTO?.userName ?? '';
  }

  get codeAdherent(): string {
    return this.currentSession?.usersDTO?.codeAdherent ?? '';
  }

  // ── Vérifications de profil ───────────────────────────────────────────────

  isSouscripteur(): boolean {
    return this.profilType === ProfilType.SOUSCRIPTEUR;
  }

  isAdherent(): boolean {
    return this.profilType === ProfilType.ADHERENT;
  }

  isServiceSante(): boolean {
    return this.profilType === ProfilType.SERVICE_SANTE;
  }

  isDII(): boolean {
    return this.profilType === ProfilType.DII;
  }

  /** Retourne true si le profil fait partie de la liste fournie */
  hasProfile(...profils: ProfilType[]): boolean {
    return profils.includes(this.profilType as ProfilType);
  }

  /** Peut accéder aux données d'un souscripteur donné */
  canAccessSouscripteur(codeSouscripteur: string): boolean {
    if (this.isDII() || this.isServiceSante()) return true;
    if (this.isSouscripteur()) return this.codeSouscripteur === codeSouscripteur;
    return false;
  }
}