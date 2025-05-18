import { Button } from "@/components/ui/button";
import Link from "next/link";

export default function FormSentPage() {
  return (
    <div className="flex flex-col text-center items-center justify-center bg-emerald-600 p-6 rounded-lg shadow-md">
      <h1 className="text-2xl font-bold mb-4 text-white">
        Seus dados foram enviados!
      </h1>
      <p className="text-md text-red-100 pb-4">
        Muito obrigado, recebemos os seus dados e entraremos em contato o mais
        breve possível.
      </p>
      <Button asChild size="lg" className="text-xl font-bold bg-blue-800">
        <Link href="/form">Enviar outro formulário</Link>
      </Button>
    </div>
  );
}
