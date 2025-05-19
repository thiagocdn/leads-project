export type Lead = {
  id: string;
  name: string;
  email: string;
  phone: string;
  quantityRequested: number;
  contacted: boolean;
};

export type CreateLeadRequest = {
  name: string;
  email: string;
  phone: string;
  referralSource: string;
  referralComment?: string;
};
