import { api } from "./api";
import { CreateLeadRequest, Lead } from "./data/leads";
import { ApiPaginatedResponse, ApiResponse } from "./data/api-response";
import { AxiosResponse } from "axios";

export const routes = {
  getUncontactedLeads: (): Promise<
    AxiosResponse<ApiPaginatedResponse<Lead[]>>
  > => api.get(`/leads/not-contacted?`),
  setLeadContacted: (id: string): Promise<ApiResponse<string>> =>
    api.post(`/leads/${id}/contacted`),
  setLeadsContactedByEmail: (email: string): Promise<ApiResponse<string>> =>
    api.post(`/leads/email-contacted/${email}`),
  setLeadsContactedByPhone: (phone: string): Promise<ApiResponse<string>> =>
    api.post(`/leads/phone-contacted/${phone}`),
  createLead: (data: CreateLeadRequest): Promise<ApiResponse<string>> =>
    api.post("/leads", data),
};
