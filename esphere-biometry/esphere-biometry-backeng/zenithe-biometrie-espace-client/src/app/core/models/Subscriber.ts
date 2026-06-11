export interface Subscriber {
  id?: number;
  policyNumber: string;
  fullName: string;
  phoneNumber: string;
  email: string;
  username: string;
  active: boolean;
  passwordMode: 'manual' | 'activation_link';
  password?: string;
  activationToken?: string;
  activationExpiry?: Date;
  activationSentAt?: Date;
  activationDuration?: number; // in hours
  createdAt?: Date;
  updatedAt?: Date;
  effet?:Date;
  echeance?:Date;
}