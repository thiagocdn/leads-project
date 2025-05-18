import {
  Card,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";

export default function FormLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <Card className="w-full max-w-lg p-6">
      <CardHeader className="space-y-1 p-0">
        <CardTitle className="text-2xl">Formulário de Contato</CardTitle>
        <CardDescription>
          Por favor, preencha o formulário abaixo e entraremos em contato assim
          que possível.
        </CardDescription>
      </CardHeader>
      {children}
    </Card>
  );
}
