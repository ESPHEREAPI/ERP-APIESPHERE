export interface ActivationLinkRequest {
  subscriberId: number;
  email: string;
  duration: number; // hours
}