export type ErrorResponse = {
  timestamp?: string;
  status?: number;
  error?: string;
  message?: string;
  code?: string;
  path?: string;
  validationErrors?: Record<string, string>;
};