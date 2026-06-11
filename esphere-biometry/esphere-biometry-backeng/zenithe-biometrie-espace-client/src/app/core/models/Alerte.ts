export interface Alerte {
  type: string;
  niveau: 'INFO' | 'WARNING' | 'CRITICAL';
  message: string;
  codeAssure?: string;
  nomAssure?: string;
  dateDetection: string;
  details?: string;
}