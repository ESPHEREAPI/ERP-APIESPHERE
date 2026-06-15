import { ProductionRequest } from "./ProductionRequest";

export interface InsuranceCertificateRequest {
  office_code:       string;
  organization_code: string;
  certificate_type:  string;
  //channel:           string;
  productions:       ProductionRequest[];
}