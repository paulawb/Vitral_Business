"use client";

import { catalogClient } from "@/services/http-client";
import { Product, ProductInput } from "@/types/catalog";

export async function getProductsByTenant(tenantId: string) {
  const response = await catalogClient.get<Product[]>(`/api/ecommerce/productos/tenant/${tenantId}`);
  return response.data;
}

export async function getProductBySlug(slug: string) {
  const response = await catalogClient.get<Product>(`/api/ecommerce/productos/slug/${slug}`);
  return response.data;
}

export async function createProduct(payload: ProductInput) {
  const response = await catalogClient.post<Product>("/api/ecommerce/productos/guardar", payload);
  return response.data;
}
