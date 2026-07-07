import { MouvementStock } from './StockAttestation';

export type AuditStatutGlobal = 'SYNCHRONISE' | 'ATTENTION' | 'DIVERGENCE' | 'SANS_DONNEE_EXTERNE';
export type AuditStatutItem   = 'SYNCHRONISE' | 'ATTENTION' | 'DIVERGENCE' | 'LOCAL_ONLY' | 'EXTERNE_ONLY';

export interface StockAuditItem {
  certTypeCode:         string | null;
  localApprovisionnement: number;
  localDisponible:      number;
  localConsommation:    number;
  externalAttributed:   number | null;
  externalAvailable:    number | null;
  externalUsed:         number | null;
  deltaDisponible:      number | null;
  deltaConsommation:    number | null;
  statut:               AuditStatutItem;
}

export interface StockAuditResponse {
  officeCode:              string;
  officeName:              string;
  auditDate:               string;
  statutGlobal:            AuditStatutGlobal;
  localTotalDisponible:    number;
  localTotalConsommation:  number;
  localTotalAppro:         number;
  externalTotalAttributed: number | null;
  externalTotalUsed:       number | null;
  externalTotalAvailable:  number | null;
  deltaDisponible:         number | null;
  deltaConsommation:       number | null;
  items:                   StockAuditItem[];
  mouvementsRecents:       MouvementStock[];
  externalApiError:        string | null;
}
