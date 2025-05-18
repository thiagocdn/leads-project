export type CreateLeadRequest = {
  name: string;
  email: string;
  phone: string;
  referralSource: string;
  referralComment?: string;
};
