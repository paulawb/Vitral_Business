"use client";

import { useCallback } from "react";
import { getAnalyticsSummary } from "@/services/analytics-service";
import { getBookingsByTenant } from "@/services/booking-service";
import { getBusinessByTenant } from "@/services/business-service";
import { getProductsByTenant } from "@/services/catalog-service";
import { useDashboardStore } from "@/store/dashboard-store";

export function useDashboardData() {
  const setLoading = useDashboardStore((state) => state.setLoading);
  const setData = useDashboardStore((state) => state.setData);

  const load = useCallback(async (tenantId: string) => {
    setLoading(true);
    try {
      const [analytics, bookings, products, business] = await Promise.all([
        getAnalyticsSummary(tenantId).catch(() => null),
        getBookingsByTenant(tenantId).catch(() => []),
        getProductsByTenant(tenantId).catch(() => []),
        getBusinessByTenant(tenantId).catch(() => null)
      ]);

      setData({ analytics, bookings, products, business, loading: false });
    } finally {
      setLoading(false);
    }
  }, [setData, setLoading]);

  return { load };
}
