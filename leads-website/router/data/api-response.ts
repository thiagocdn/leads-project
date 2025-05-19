export type ApiResponse<T> = {
  message: string;
  errors: ApiError[];
  response: T;
};

export type ApiPaginatedResponse<T> = {
  message: string;
  errors: ApiError[];
  response: PaginatedResponse<T>;
};

export type PaginatedResponse<T> = {
  content: T;
  totalElements: number;
  page: number;
  size: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
};

export type ApiError = {
  field: string;
  message: string;
};
