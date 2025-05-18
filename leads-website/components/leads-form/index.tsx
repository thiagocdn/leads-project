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
import { useEffect, useState } from "react";
import { routes } from "@/router/routes";
import { useRouter } from "next/navigation";
import { AxiosError } from "axios";
import { ApiResponse } from "@/router/data/api-response";

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
    referralOthers: z.string().optional(),
  })
  .refine(
    (data) => {
      if (data.referralSource === "OTHERS") {
        return !!data.referralOthers?.trim() && data.referralOthers.length > 1;
      }
      return true;
    },
    {
      message: "Nos conte como nos conheceu.",
      path: ["referralOthers"],
    }
  );

export default function LeadsForm() {
  const [loading, setLoading] = useState(false);
  const router = useRouter();
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      name: "",
      email: "",
      phone: "",
      referralSource: undefined,
      referralOthers: "",
    },
  });
  const referralSource = form.watch("referralSource");

  useEffect(() => {
    if (referralSource !== "OTHERS") {
      form.unregister("referralOthers");
    }
  }, [referralSource, form]);

  function onSubmit(values: z.infer<typeof formSchema>) {
    if (loading) return;
    setLoading(true);
    routes
      .createLead(values)
      .then(() => {
        form.reset();
        router.push("/form/sent");
      })
      .catch((error: AxiosError<ApiResponse<null>>) => {
        if (error.response?.status === 400) {
          const { errors } = error.response.data;
          errors.forEach(({ field, message }) => {
            if (field in form.getValues()) {
              form.setError(field as keyof z.infer<typeof formSchema>, {
                type: "manual",
                message,
              });
            }
          });
          return;
        }
      })
      .finally(() => {
        setLoading(false);
      });
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="w-full space-y-6">
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
              <Select onValueChange={field.onChange} defaultValue={field.value}>
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
        <FormField
          control={form.control}
          name="referralOthers"
          render={({ field }) => (
            <FormItem className={referralSource !== "OTHERS" ? "hidden" : ""}>
              <FormLabel>Por favor, nos conte como nos conheceu:</FormLabel>
              <FormControl>
                <Input {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <Button type="submit" disabled={loading}>
          Enviar
        </Button>
      </form>
    </Form>
  );
}
