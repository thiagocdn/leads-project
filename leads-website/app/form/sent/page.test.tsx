import { render, screen } from "@testing-library/react";
import FormSentPage from "@/app/form/sent/page";

describe("FormSentPage", () => {
  test("shows title, message and link to another form correctly", () => {
    render(<FormSentPage />);

    expect(screen.getByTestId("sent-form-title")).toBeInTheDocument();
    expect(screen.getByTestId("sent-form-message")).toBeInTheDocument();
    const link = screen.getByTestId("new-form-link");
    expect(link).toBeInTheDocument();
    expect(link).toHaveAttribute("href", "/form");
  });
});
