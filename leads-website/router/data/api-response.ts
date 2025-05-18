export type ApiResponse<T> = {
  message: string;
  errors: ApiError[];
  response: T;
};

export type ApiError = {
  field: string;
  message: string;
};
