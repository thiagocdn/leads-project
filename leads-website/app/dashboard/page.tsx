"use client";

import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { Lead } from "@/router/data/leads";
import { routes } from "@/router/routes";
import { Input } from "@/components/ui/input";
import {
  Pagination,
  PaginationContent,
  PaginationItem,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination";

export default function LeadsDashboard() {
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [leads, setLeads] = useState<Lead[]>([]);
  const [loading, setLoading] = useState(true);
  const [emailSearch, setEmailSearch] = useState("");
  const [phoneSearch, setPhoneSearch] = useState("");
  const [contacted, setContacted] = useState(false);

  useEffect(() => {
    fetchLeads();
  }, [contacted, page]);

  const fetchLeads = async () => {
    setLoading(true);
    try {
      const { data } = await routes.searchLeads(
        emailSearch,
        phoneSearch,
        contacted,
        page
      );
      setLeads(data.response.content);
      setTotalPages(data.response.totalPages);
    } catch (error) {
      console.error("Failed to fetch leads", error);
    } finally {
      setLoading(false);
    }
  };

  const markAsContacted = async (id: string) => {
    try {
      await routes.setLeadContacted(id);
      fetchLeads();
    } catch (error) {
      console.error("Failed to mark as contacted", error);
    }
  };

  const markAsContactedByEmail = async (email: string) => {
    try {
      await routes.setLeadsContactedByEmail(email);
      fetchLeads();
    } catch (error) {
      console.error("Failed to mark as contacted by email", error);
    }
  };

  const markAsContactedByPhone = async (phone: string) => {
    try {
      await routes.setLeadsContactedByPhone(phone);
      fetchLeads();
    } catch (error) {
      console.error("Failed to mark as contacted by phone", error);
    }
  };

  if (loading) {
    return (
      <div className="grid gap-4 p-4">
        {[...Array(5)].map((_, i) => (
          <Skeleton key={i} className="h-24 w-80 rounded-xl" />
        ))}
      </div>
    );
  }

  const handlePageChange = (newPage: number) => {
    if (newPage < 0 || newPage >= totalPages) return;
    setPage(newPage);
  };

  return (
    <div className="h-screen flex flex-col items-center w-full">
      <div className="flex justify-center p-6 shadow-sm w-full sticky top-0 bg-white z-10">
        <div className="flex gap-4 max-w-180">
          <Input
            type="text"
            placeholder="Pesquisar por email"
            value={emailSearch}
            onChange={(e) => setEmailSearch(e.target.value)}
            className="input"
          />
          <Input
            type="text"
            placeholder="Pesquisar por telefone"
            value={phoneSearch}
            onChange={(e) => setPhoneSearch(e.target.value)}
            className="input"
          />
          <Button onClick={fetchLeads}>Search</Button>
          <Button
            onClick={() => setContacted(!contacted)}
            variant={contacted ? "outline" : "destructive"}
          >
            {contacted ? "Mostrar Não Contatados" : "Mostrar Contatados"}
          </Button>
        </div>
      </div>
      <div className="grid gap-4 p-4 max-w-140">
        {leads.map((lead) => (
          <Card
            key={lead.id}
            className="flex flex-row items-center justify-between p-4"
          >
            <CardContent className="flex-1 space-y-1">
              <p className="text-lg font-semibold">{lead.name}</p>
              <p className="text-sm text-muted-foreground">{lead.email}</p>
              <p className="text-sm text-muted-foreground">{lead.phone}</p>
            </CardContent>
            <div className="flex flex-col items-center gap-1">
              <Button
                onClick={() => markAsContacted(lead.id)}
                variant="default"
                size="sm"
              >
                Contatado Lead
              </Button>
              <Button
                onClick={() => markAsContactedByEmail(lead.email)}
                variant="default"
                size="sm"
              >
                Contatado por E-mail
              </Button>
              <Button
                onClick={() => markAsContactedByPhone(lead.phone)}
                variant="default"
                size="sm"
              >
                Contatado por Telefone
              </Button>
            </div>
          </Card>
        ))}
        {leads.length === 0 && (
          <p className="text-center text-muted-foreground">
            All leads contacted
          </p>
        )}
        <Pagination className="my-4">
          <PaginationContent>
            <PaginationItem>
              <PaginationPrevious
                onClick={() => handlePageChange(page - 1)}
                className="cursor-pointer"
              />
            </PaginationItem>
            <PaginationItem>
              <span className="text-sm px-4 py-2">
                Página {page + 1} de {totalPages}
              </span>
            </PaginationItem>
            <PaginationItem>
              <PaginationNext
                onClick={() => handlePageChange(page + 1)}
                className="cursor-pointer"
              />
            </PaginationItem>
          </PaginationContent>
        </Pagination>
      </div>
    </div>
  );
}
