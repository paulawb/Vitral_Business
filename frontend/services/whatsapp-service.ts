"use client";

import { whatsappClient } from "@/services/http-client";

export async function getPrefilledLink(phone: string, text: string) {
  const response = await whatsappClient.post<{ url: string }>("/api/v1/whatsapp/prefilled-link", {
    phone,
    text
  });
  return response.data.url;
}
