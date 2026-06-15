import { CertificateInfo } from "./CertificateInfo";
import { OfficeInfo } from "./OfficeInfo";
import { OrganizationInfo } from "./OrganizationInfo";
import { UserInfo } from "./UserInfo";

export interface ProductionData {
  id:                   string;
  reference:            string;
  channel:              string;
  quantity:             number;
  sent_to_storage:      boolean;
  download_link:        string;
  user:                 UserInfo;
  organization:         OrganizationInfo;
  office:               OfficeInfo;
  certificates:         CertificateInfo[];
  created_at:           string;
  formatted_created_at: string;
  updated_at:           string;
}