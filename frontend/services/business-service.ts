"use client";

import { businessClient } from "@/services/http-client";
import { Business, BusinessInput } from "@/types/business";

export async function createBusiness(payload: Business) {
  const response = await businessClient.post<Business>("/api/v1/businesses", payload);
  return response.data;
}

export async function updateBusiness(slug: string, payload: BusinessInput) {
  const response = await businessClient.put<Business>(`/api/v1/businesses/${slug}`, payload);
  return response.data;
}

export async function getBusinessBySlug(slug: string) {
  const response = await businessClient.get<Business>(`/api/v1/businesses/${slug}`);
  return response.data;
}

export async function getBusinessByTenant(tenantId: string) {
  const response = await businessClient.get<Business>(`/api/v1/businesses/tenant/${tenantId}`);
  return response.data;
}
