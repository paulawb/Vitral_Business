"use client";

import { bookingClient } from "@/services/http-client";
import { AvailabilitySlot, Booking } from "@/types/booking";

export async function getBookingsByTenant(tenantId: string) {
  try {
    const response = await bookingClient.get<Booking[]>(`/api/v1/bookings/tenant/${tenantId}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching bookings:", error);
    throw error;
  }
}

export async function getAvailability(tenantId: string, date: string) {
  try {
    const response = await bookingClient.get<AvailabilitySlot[]>("/api/v1/bookings/availability", {
      params: { tenantId, date }
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching availability:", error);
    throw error;
  }
}

export async function createBooking(payload: Omit<Booking, "bookingId" | "status">) {
  try {
    const response = await bookingClient.post<Booking>("/api/v1/bookings", payload);
    return response.data;
  } catch (error) {
    console.error("Error creating booking:", error);
    throw error;
  }
}

export async function cancelBooking(bookingId: string) {
  try {
    const response = await bookingClient.post<Booking>(`/api/v1/bookings/${bookingId}/cancel`);
    return response.data;
  } catch (error) {
    console.error("Error canceling booking:", error);
    throw error;
  }
}
