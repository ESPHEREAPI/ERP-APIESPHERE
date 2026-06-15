/**
 * Modèles d'erreur alignés sur les réponses du backend Spring Boot.
 * Tout ce que le GlobalExceptionHandler peut retourner est typé ici.
 */

/** Réponse d'erreur générique — ApiResponse.error(...) du backend */
export interface ApiErrorResponse {
  success: false;
  status?: number;
  errorCode?: string;
  message: string;
  timestamp?: number;
}

/** Réponse d'erreur de validation — ValidationErrorPayloadResponse */
export interface ValidationErrorResponse {
  status: number;
  message: string;
  errors: Record<string, string[]>;
  errorType: BackendErrorType;
}

export type BackendErrorType =
  | 'VALIDATION'
  | 'UNAUTHORIZED'
  | 'FORBIDDEN'
  | 'STOCK_INSUFFICIENT'
  | 'UNKNOWN';

/** Union des deux formes que peut prendre une erreur backend */
export type BackendError = ApiErrorResponse | ValidationErrorResponse;

/** Erreur normalisée et affichable dans le frontend */
export interface FrontendError {
  status: number;
  title: string;
  message: string;
  errorCode?: string;
  fieldErrors?: Record<string, string[]>;
  errorType?: BackendErrorType;
  timestamp: number;
}

/** Détecte si la réponse est une ValidationErrorResponse */
export function isValidationError(err: BackendError): err is ValidationErrorResponse {
  return 'errorType' in err;
}
