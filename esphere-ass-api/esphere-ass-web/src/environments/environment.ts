// Développement — remplacé par environment.prod.ts lors du build --configuration production
export const environment = {
  production: false,

  // URL de la gateway — vide en dev : le proxy Angular (proxy.conf.json) forward
  // /gateway-proxy/** → http://localhost:4141 (gateway Spring Cloud)
  //apiUrl: 'https://77.68.94.193:4141',
apiUrl:'',
  // URL directe du microservice admin (si connexion sans gateway)
  //adminServiceUrl: 'http://localhost:8083',

  // Préfixe de routage gateway utilisé par le proxy Angular
  //gatewayPrefix: '/gateway-proxy',

  // Clé de chiffrement localStorage (à ne jamais commit en prod)
  token_key: '3cfa76ef890d4aed2d3981a7c93bd1a13c8796dafcb4f94fa578234a0df56b321',

  // Durée d'avertissement avant expiration token (ms)
  tokenRefreshWarningMs: 300000, // 5 min

  // Logging activé en dev
  enableLogging: true,
};
