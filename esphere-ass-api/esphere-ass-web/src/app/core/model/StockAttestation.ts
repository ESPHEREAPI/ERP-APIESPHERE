export interface StockAttestation {
  id:                           number;
  officeCode:                   string;
  officeName:                   string;
  orgCode:                      string;
  certTypeCode:                 string | null;
  certTypeName:                 string | null;
  certVariantCode:              string | null;
  certVariantName:              string | null;
  quantiteDisponible:           number;
  quantiteReservee:             number;
  quantiteTotaleApprovisionnee: number;
  quantiteTotalConsommee:       number;
  seuilAlerte:                  number;
  seuilCritique:                number;
  statut:                       'NORMAL' | 'ALERTE' | 'CRITIQUE' | 'RUPTURE';
  createdAt:                    string;
  updatedAt:                    string;
  createdBy:                    string;
}

export interface MouvementStock {
  id:              number;
  stockId:         number;
  officeCode:      string;
  certTypeCode:    string | null;
  certVariantCode: string | null;
  typeMouvement:   'APPROVISIONNEMENT' | 'DESTOCKAGE' | 'AJUSTEMENT_PLUS' | 'AJUSTEMENT_MOINS' | 'ANNULATION';
  quantite:        number;
  quantiteAvant:   number;
  quantiteApres:   number;
  referenceSource: string | null;
  motif:           string | null;
  createdAt:       string;
  createdBy:       string;
}

export interface InitierStockRequest {
  officeCode:       string;
  officeName?:      string;
  orgCode?:         string;
  certTypeCode?:    string | null;
  certTypeName?:    string | null;
  certVariantCode?: string | null;
  certVariantName?: string | null;
  quantiteInitiale?: number;
  seuilAlerte?:     number;
  seuilCritique?:   number;
  motif?:           string;
}

export interface ApprovisionnerRequest {
  certTypeCode?:    string | null;
  certVariantCode?: string | null;
  quantite:         number;
  referenceSource?: string;
  motif?:           string;
}

export interface AjustementRequest {
  certTypeCode?:    string | null;
  certVariantCode?: string | null;
  delta:            number;
  motif?:           string;
}
