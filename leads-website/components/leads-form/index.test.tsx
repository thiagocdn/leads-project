import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import LeadsForm from "./index";
import { ReferralSource } from "@/app/form/data";

jest.mock("next/navigation", () => ({
  useRouter: () => ({
    push: jest.fn(),
    replace: jest.fn(),
    prefetch: jest.fn(),
  }),
}));

describe("LeadsForm", () => {
  test("render form fields correctly", () => {
    render(<LeadsForm />);

    expect(screen.getByTestId("full-name-input")).toBeInTheDocument();
    expect(screen.getByTestId("email-input")).toBeInTheDocument();
    expect(screen.getByTestId("phone-input")).toBeInTheDocument();
    expect(screen.getByTestId("referral-choice")).toBeInTheDocument();

    const inputItem = screen.getByTestId("referral-others-input-item");
    expect(inputItem).toHaveClass("hidden");

    expect(screen.getByTestId("send-form-button")).toBeInTheDocument();
  });

  test("when choose 'OTHERS' in referralSource, show referralOthers input", async () => {
    render(
      <LeadsForm defaultValues={{ referralSource: ReferralSource.OTHERS }} />
    );

    const inputItem = screen.getByTestId("referral-others-input-item");
    expect(inputItem).not.toHaveClass("hidden");
  });

  test("show validation errors when try to send with errors", async () => {
    const user = userEvent.setup();
    render(<LeadsForm />);
    const submit = screen.getByRole("button", { name: /enviar/i });
    await user.click(submit);

    expect(
      await screen.findAllByText(/Nome deve ter mais de 2 caracteres/i)
    ).toHaveLength(1);
    expect(await screen.findAllByText(/Email inválido/i)).toHaveLength(1);
    expect(
      await screen.findAllByText(/Insira um telefone válido/i)
    ).toHaveLength(1);
  });

  test("show 'other' error when try to send with OTHERS without referralOthers", async () => {
    const user = userEvent.setup();
    render(
      <LeadsForm
        defaultValues={{
          name: "John Doe",
          email: "john@doe.com",
          phone: "1234567890",
          referralSource: ReferralSource.OTHERS,
        }}
      />
    );
    const submit = screen.getByRole("button", { name: /enviar/i });
    await user.click(submit);

    expect(
      await screen.findAllByText(/Nos conte como nos conheceu./i)
    ).toHaveLength(1);
  });
});
