import { api } from "./api";
import { CreateLeadRequest, Lead } from "./data/leads";
import { ApiPaginatedResponse, ApiResponse } from "./data/api-response";
import { AxiosResponse } from "axios";

export const routes = {
  searchLeads: (
    email: string = "",
    phone: string = "",
    contacted: boolean = false,
    page: number = 0
  ): Promise<AxiosResponse<ApiPaginatedResponse<Lead[]>>> =>
    api.get(
      `/leads?size=5&page=${page}&contacted=${contacted}${
        email != "" ? `&email=${email}` : ""
      }${phone != "" ? `&phone=${phone}` : ""}`
    ),
  setLeadContacted: (id: string): Promise<ApiResponse<string>> =>
    api.post(`/leads/${id}/contacted`),
  setLeadsContactedByEmail: (email: string): Promise<ApiResponse<string>> =>
    api.post(`/leads/email-contacted/${email}`),
  setLeadsContactedByPhone: (phone: string): Promise<ApiResponse<string>> =>
    api.post(`/leads/phone-contacted/${phone}`),
  createLead: (data: CreateLeadRequest): Promise<ApiResponse<string>> =>
    api.post("/leads", data),
};
