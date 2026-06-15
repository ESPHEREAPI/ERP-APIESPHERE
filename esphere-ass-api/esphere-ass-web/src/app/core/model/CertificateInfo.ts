import { CodeName } from "./CodeName";
import { OfficeInfo } from "./OfficeInfo";
import { OrganizationInfo } from "./OrganizationInfo";
import { ProductionRef } from "./ProductionRef";

export interface CertificateInfo {
  production:          ProductionRef;
  reference:           string;
  organization:        OrganizationInfo;
  office:              OfficeInfo;
  certificate_type?:    CodeName | null;
  certificate_variant?: CodeName | null;
  state:               string;
  download_link:       string;
  licence_plate:       string;
  chassis_number:      string;
  police_number:       string;
  insured_name:        string;
  insured_phone:       string;
  insured_email:       string;
  starts_at:           string;
  ends_at:             string;
  printed_at:          string | null;
}