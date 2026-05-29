"use client";

import { create } from "zustand";
import { Business } from "@/types/business";

type TenantStore = {
  business: Business | null;
  setBusiness: (business: Business | null) => void;
};

export const useTenantStore = create<TenantStore>((set) => ({
  business: null,
  setBusiness: (business) => set({ business })
}));
