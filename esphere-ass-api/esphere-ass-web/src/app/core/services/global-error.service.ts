import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import {
  BackendError,
  FrontendError,
  isValidationError,
  ValidationErrorResponse,
} from '../model/api-error.model';

/**
 * Service centralisé de gestion des erreurs HTTP backend.
 * Traduit les réponses d'erreur backend en messages utilisateur cohérents.
 * Utilisé par l'ErrorInterceptor et les composants.
 */
@Injectable({ providedIn: 'root' })
export class GlobalErrorService {
  constructor(private toastr: ToastrService) {}

  /**
   * Normalise une erreur backend vers un FrontendError affichable.
   */
  normalize(httpStatus: number, body: BackendError | null): FrontendError {
    const timestamp = Date.now();

    if (!body) {
      return {
        status: httpStatus,
        title: this.titleFor(httpStatus),
        message: this.defaultMessageFor(httpStatus),
        timestamp,
      };
    }

    if (isValidationError(body)) {
      return {
        status: body.status,
        title: 'Erreur de validation',
        message: body.message,
        fieldErrors: body.errors,
        errorType: body.errorType,
        timestamp,
      };
    }

    return {
      status: httpStatus,
      title: this.titleFor(httpStatus),
      message: body.message || this.defaultMessageFor(httpStatus),
      errorCode: body.errorCode,
      timestamp,
    };
  }

  /**
   * Affiche un toast selon le type d'erreur et retourne le FrontendError normalisé.
   */
  handleAndNotify(httpStatus: number, body: BackendError | null): FrontendError {
    const error = this.normalize(httpStatus, body);

    switch (httpStatus) {
      case 401:
        this.toastr.warning(error.message, 'Session expirée', { timeOut: 5000 });
        break;
      case 403:
        this.toastr.error(error.message, 'Accès refusé');
        break;
      case 422:
        this.toastr.warning(error.message, 'Données invalides');
        break;
      case 404:
        this.toastr.info(error.message, 'Introuvable');
        break;
      case 409:
        this.toastr.warning(error.message, 'Conflit');
        break;
      case 0:
        this.toastr.error(
          'Le serveur est inaccessible. Vérifiez votre connexion.',
          'Erreur réseau',
          { timeOut: 6000 }
        );
        break;
      default:
        this.toastr.error(error.message, error.title);
    }

    return error;
  }

  /**
   * Retourne tous les messages d'erreur de champ aplatis en tableau.
   */
  flatFieldErrors(
    fieldErrors: Record<string, string[]> | undefined
  ): string[] {
    if (!fieldErrors) return [];
    return Object.values(fieldErrors).flat();
  }

  /**
   * Retourne le premier message d'erreur d'un champ donné.
   */
  getFieldError(
    fieldErrors: Record<string, string[]> | undefined,
    field: string
  ): string | null {
    return fieldErrors?.[field]?.[0] ?? null;
  }

  private titleFor(status: number): string {
    const titles: Record<number, string> = {
      400: 'Requête invalide',
      401: 'Non authentifié',
      403: 'Accès refusé',
      404: 'Introuvable',
      409: 'Conflit',
      422: 'Données invalides',
      500: 'Erreur serveur',
      502: 'Passerelle invalide',
      503: 'Service indisponible',
    };
    return titles[status] ?? 'Erreur';
  }

  private defaultMessageFor(status: number): string {
    const messages: Record<number, string> = {
      400: 'La requête envoyée est invalide.',
      401: 'Votre session a expiré. Veuillez vous reconnecter.',
      403: "Vous n'avez pas les permissions pour effectuer cette action.",
      404: "La ressource demandée n'existe pas.",
      409: 'Cette ressource existe déjà.',
      422: 'Les données soumises sont incorrectes.',
      500: 'Une erreur interne est survenue. Veuillez réessayer.',
      502: 'Le service est temporairement indisponible.',
      503: 'Service en maintenance. Veuillez patienter.',
    };
    return messages[status] ?? 'Une erreur inattendue est survenue.';
  }
}
