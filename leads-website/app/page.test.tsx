import { render, screen } from "@testing-library/react";
import Home from "./page";

describe("Home Page", () => {
  test("render title, message and button/link to form correctly", () => {
    render(<Home />);

    expect(screen.getByTestId("home-title")).toBeInTheDocument();
    expect(screen.getByTestId("form-button-message")).toBeInTheDocument();

    const button = screen.getByTestId("form-link");
    expect(button).toBeInTheDocument();
    expect(button).toHaveAttribute("href", "/form");
  });
});
