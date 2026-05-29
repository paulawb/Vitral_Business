"use client";

import { authClient } from "@/services/http-client";
import { AuthResponse, LoginInput, RegisterInput, User } from "@/types/auth";
import { readRefreshToken } from "@/utils/session";

export async function login(payload: LoginInput) {
  const response = await authClient.post<AuthResponse>("/api/v1/auth/login", payload);
  return response.data;
}

export async function register(payload: RegisterInput) {
  const response = await authClient.post<User>("/api/v1/auth/register", {
    ...payload,
    tenantId: "",
    tipoNegocio: "",
    rol: "TENANT_ADMIN"
  });
  return response.data;
}

export async function assignTenant(cedula: string, tenantId: string) {
  const response = await authClient.post<User>(`/api/v1/auth/users/${cedula}/tenant`, { tenantId });
  return response.data;
}

export async function logout() {
  const refreshToken = readRefreshToken();
  if (refreshToken) {
    try {
      await authClient.post("/api/v1/auth/logout", { refreshToken });
    } catch {
      // Ignore errors on logout
    }
  }
}
