export interface PolicyCheck {
  policyNumber: string;
  fullName: string;
  exists: boolean;
  statut: boolean;
  effet: Date;
  echeance: Date;
  email?:string;
}