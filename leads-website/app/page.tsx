import { Button } from "@/components/ui/button";
import Link from "next/link";

export default function Home() {
  return (
    <div className="flex flex-col items-center min-h-screen justify-center">
      <h1 data-testid="home-title" className="text-2xl font-bold">
        Bem-vindo ao Leads Website
      </h1>
      <p data-testid="form-button-message" className="mt-4 text-lg pb-6">
        Clique no botão abaixo para acessar o formulário.
      </p>
      <Button asChild size="lg" className="text-xl font-bold bg-blue-800">
        <Link data-testid="form-link" href="/form">
          Acessar Formulário
        </Link>
      </Button>
    </div>
  );
}
