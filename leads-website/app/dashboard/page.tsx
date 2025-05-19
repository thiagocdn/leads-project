"use client";

import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { Lead } from "@/router/data/leads";
import { routes } from "@/router/routes";

export default function LeadsDashboard() {
  const [leads, setLeads] = useState<Lead[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchLeads();
  }, []);

  const fetchLeads = async () => {
    setLoading(true);
    try {
      const { data } = await routes.getUncontactedLeads();
      setLeads(data.response.content);
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

  if (loading) {
    return (
      <div className="grid gap-4 p-4">
        {[...Array(5)].map((_, i) => (
          <Skeleton key={i} className="h-24 w-80 rounded-xl" />
        ))}
      </div>
    );
  }

  return (
    <div className="grid gap-4 p-4">
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
          <Button onClick={() => markAsContacted(lead.id)} variant="default">
            Contacted
          </Button>
        </Card>
      ))}
      {leads.length === 0 && (
        <p className="text-center text-muted-foreground">All leads contacted</p>
      )}
    </div>
  );
}
