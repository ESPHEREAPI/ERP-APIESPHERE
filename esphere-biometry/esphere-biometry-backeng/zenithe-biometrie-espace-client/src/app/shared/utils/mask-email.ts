/**
 * email-mask.util.ts
 * Masque partiellement un email pour aider l'utilisateur à s'en souvenir
 * sans l'afficher en clair.
 *
 * Exemples :
 *   jean.dupont@gmail.com  →  je***.du***@gm***.com
 *   ab@xy.fr               →  a*@x*.fr
 */
export function maskEmail(email: string): string {
  if (!email || !email.includes('@')) return email;

  const [local, domain] = email.split('@');
  const [domainName, ...tldParts] = domain.split('.');
  const tld = tldParts.join('.');

  return `${maskPart(local)}@${maskPart(domainName)}.${tld}`;
}

/**
 * Masque une partie d'une chaîne :
 *  - Garde les 2 premiers caractères visibles (1 si longueur ≤ 2)
 *  - Remplace le reste par ***
 */
function maskPart(value: string): string {
  if (!value) return '';
  const visible = Math.min(2, Math.max(1, Math.floor(value.length / 3)));
  return value.slice(0, visible) + '***';
}