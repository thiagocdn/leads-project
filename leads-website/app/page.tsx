import { Button } from "@/components/ui/button";
import Link from "next/link";

export default function Home() {
  return (
    <div className="flex flex-col items-center min-h-screen justify-center">
      <h1 className="text-2xl font-bold">Bem-vindo ao Leads Website</h1>
      <p className="mt-4 text-lg pb-6">
        Clique no botão abaixo para acessar o formulário.
      </p>
      <Button asChild size="lg" className="text-xl font-bold bg-blue-800">
        <Link href="/form">Acessar Formulário</Link>
      </Button>
    </div>
  );
}
