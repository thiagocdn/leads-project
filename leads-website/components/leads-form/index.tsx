"use client";

import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Input } from "@/components/ui/input";
import { useEffect } from "react";
import {
  Card,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";

const formSchema = z
  .object({
    name: z
      .string()
      .min(2, "Nome deve ter mais de 2 caracteres")
      .max(50, "Nome deve ter menos de 50 caracteres"),
    email: z.string().email("Email inválido"),
    phone: z.string().min(10, "Insira um telefone válido"),
    referralSource: z.enum(["INTERNET", "REFERRAL", "EVENTS", "OTHERS"], {
      required_error: "Selecione uma opção",
    }),
    referralComment: z.string().optional(),
  })
  .refine(
    (data) => {
      if (data.referralSource === "OTHERS") {
        return (
          !!data.referralComment?.trim() && data.referralComment.length > 1
        );
      }
      return true;
    },
    {
      message: "Nos conte como nos conheceu.",
      path: ["referralComment"],
    }
  );

export default function LeadsForm() {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      name: "",
      email: "",
      phone: "",
    },
  });
  const referralSource = form.watch("referralSource");

  useEffect(() => {
    if (referralSource !== "OTHERS") {
      form.unregister("referralComment");
    }
  }, [referralSource, form]);

  function onSubmit(values: z.infer<typeof formSchema>) {
    console.log(values);
  }

  return (
    <Card className="w-full max-w-lg p-6">
      <CardHeader className="space-y-1 p-0">
        <CardTitle className="text-2xl">Formulário de Contato</CardTitle>
        <CardDescription>
          Por favor, preencha o formulário abaixo e entraremos em contato assim
          que possível.
        </CardDescription>
      </CardHeader>
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="w-full space-y-6"
        >
          <FormField
            control={form.control}
            name="name"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Nome</FormLabel>
                <FormControl>
                  <Input placeholder="Nome completo" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="email"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Email</FormLabel>
                <FormControl>
                  <Input placeholder="email" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="phone"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Telefone</FormLabel>
                <FormControl>
                  <Input placeholder="12 3456-7890" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="referralSource"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Como nos conheceu?</FormLabel>
                <Select
                  onValueChange={field.onChange}
                  defaultValue={field.value}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Selecione uma opção" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    <SelectItem value="INTERNET">Internet</SelectItem>
                    <SelectItem value="REFERRAL">Indicação</SelectItem>
                    <SelectItem value="EVENTS">Eventos</SelectItem>
                    <SelectItem value="OTHERS">Outros</SelectItem>
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            )}
          />
          {referralSource === "OTHERS" && (
            <FormField
              control={form.control}
              name="referralComment"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Por favor, nos conte como nos conheceu:</FormLabel>
                  <FormControl>
                    <Input {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          )}
          <Button type="submit">Enviar</Button>
        </form>
      </Form>
    </Card>
  );
}
