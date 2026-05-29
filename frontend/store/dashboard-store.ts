"use client";

import { create } from "zustand";
import { AnalyticsSummary } from "@/types/analytics";
import { Booking } from "@/types/booking";
import { Business } from "@/types/business";
import { Product } from "@/types/catalog";

type DashboardState = {
  loading: boolean;
  analytics: AnalyticsSummary | null;
  bookings: Booking[];
  products: Product[];
  business: Business | null;
  setLoading: (loading: boolean) => void;
  setData: (payload: Partial<Omit<DashboardState, "setLoading" | "setData">>) => void;
};

export const useDashboardStore = create<DashboardState>((set) => ({
  loading: true,
  analytics: null,
  bookings: [],
  products: [],
  business: null,
  setLoading: (loading) => set({ loading }),
  setData: (payload) => set((state) => ({ ...state, ...payload }))
}));
