// Production — actif lors du build ng build --configuration production
export const environment = {
  production: true,

  // IMPORTANT : laisser vide — les requêtes passent par Apache (:443)
  // Apache proxifie /gateway-proxy/ → https://localhost:4141 (gateway interne)
  // URL finale : /gateway-proxy/api/... → https://77.68.94.193/gateway-proxy/api/...
  // NE PAS mettre
   apiUrl: 'https://77.68.94.193:4141',
  //apiUrl: '',

  //adminServiceUrl: '',

  //gatewayPrefix: '/gateway-proxy',

  // Secret JWT
  token_key: '3cfa76ef890d4aed2d3981a7c93bd1a13c8796dafcb4f94fa578234a0df56b321',

  tokenRefreshWarningMs: 300000,

  enableLogging: false,
};
