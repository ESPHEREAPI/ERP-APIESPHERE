import { ErrorType } from "../error/ErrorType";

export interface ValidationErrorResponse {
  httpStatus: number;
  message:    string;
  errors:     Record<string, string[]>;
  errorType:  ErrorType;
}