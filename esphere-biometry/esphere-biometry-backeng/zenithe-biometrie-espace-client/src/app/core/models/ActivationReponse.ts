export interface ActivationResponse {
  message: string;
  expiry: string; // ISO 8601 string (Java Instant serializes to e.g. "2025-03-03T10:00:00Z")
  durationHours: number;
}