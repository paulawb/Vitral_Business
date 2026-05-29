"use client";

import { useSearchParams } from "next/navigation";
import { BookingForm } from "@/components/forms/booking-form";

export default function BookingPage() {
  const searchParams = useSearchParams();
  const tenant = searchParams.get("tenant") || "";
  const slug = searchParams.get("slug") || "";
  const service = searchParams.get("service") || "";

  return (
    <main className="container-app py-8">
      <BookingForm defaults={{ tenantId: tenant, businessSlug: slug, serviceName: service }} />
    </main>
  );
}
