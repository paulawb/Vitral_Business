"use client";

import axios, { AxiosError, InternalAxiosRequestConfig } from "axios";
import { env } from "@/lib/env";
import { useAuthStore } from "@/store/auth-store";
import { readAccessToken, readRefreshToken } from "@/utils/session";

type RetryConfig = InternalAxiosRequestConfig & { _retry?: boolean };

const refreshClient = axios.create({
  baseURL: env.authApi,
  headers: { "Content-Type": "application/json" }
});

async function refreshSession() {
  const refreshToken = readRefreshToken();
  if (!refreshToken) {
    throw new Error("No refresh token");
  }
  const response = await refreshClient.post("/api/v1/auth/refresh", { refreshToken });
  useAuthStore.getState().setAuth(response.data);
  return response.data.accessToken as string;
}

function attachInterceptors(baseURL: string) {
  const client = axios.create({
    baseURL,
    headers: { "Content-Type": "application/json" }
  });

  client.interceptors.request.use((config) => {
    const token = readAccessToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });

  client.interceptors.response.use(
    (response) => response,
    async (error: AxiosError) => {
      const status = error.response?.status;
      const originalRequest = error.config as RetryConfig | undefined;
      if (status === 401 && originalRequest && !originalRequest._retry) {
        originalRequest._retry = true;
        try {
          const token = await refreshSession();
          originalRequest.headers.Authorization = `Bearer ${token}`;
          return client(originalRequest);
        } catch {
          useAuthStore.getState().logout();
        }
      }
      return Promise.reject(error);
    }
  );

  return client;
}

export const authClient = attachInterceptors(env.authApi);
export const catalogClient = attachInterceptors(env.catalogApi);
export const businessClient = attachInterceptors(env.businessApi);
export const bookingClient = attachInterceptors(env.bookingApi);
export const analyticsClient = attachInterceptors(env.analyticsApi);
export const whatsappClient = attachInterceptors(env.whatsappApi);
