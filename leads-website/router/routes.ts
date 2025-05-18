import { AxiosResponse } from "axios";
import { api } from "./api";
import { CreateLeadRequest } from "./data/leads";
import { ApiResponse } from "./data/api-response";

export const routes = {
  getLeads: () => api.get("/leads"),
  getLeadById: (id: string) => api.get<AxiosResponse<string>>(`/leads/${id}`),
  createLead: (data: CreateLeadRequest): Promise<ApiResponse<string>> =>
    api.post("/leads", data),
};
