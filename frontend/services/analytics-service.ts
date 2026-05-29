"use client";

import { analyticsClient } from "@/services/http-client";
import { AnalyticsEvent, AnalyticsSummary } from "@/types/analytics";

export async function getAnalyticsSummary(tenantId: string) {
  const response = await analyticsClient.get<AnalyticsSummary>(`/api/v1/analytics/dashboard/${tenantId}`);
  return response.data;
}

export async function trackEvent(payload: AnalyticsEvent) {
  await analyticsClient.post("/api/v1/analytics/events", payload);
}
