export class ErrorHelper {
 
  // Extrait les erreurs d'une production par index
  // "productions.0.vehicle_chassis" → { "vehicle_chassis": ["..."] }
  static getProductionErrors(
    errors: Record<string, string[]>,
    index = 0
  ): Record<string, string[]> {
    const prefix = `productions.${index}.`;
    const result: Record<string, string[]> = {};
    Object.keys(errors)
      .filter(k => k.startsWith(prefix))
      .forEach(k => { result[k.replace(prefix, '')] = errors[k]; });
    return result;
  }
 
  // Erreurs globales (pas liées à un champ)
  static getGlobalErrors(errors: Record<string, string[]>): string[] {
    return errors['productions'] || [];
  }
 
  // Premier message d'erreur d'un champ
  static getError(
    errors: Record<string, string[]>,
    index: number,
    field: string
  ): string | null {
    return errors[`productions.${index}.${field}`]?.[0] ?? null;
  }
}
 