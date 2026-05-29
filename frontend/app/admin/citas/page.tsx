"use client";

import { useEffect, useState } from "react";
import { AxiosError } from "axios";
import { ProtectedShell } from "@/components/layout/protected-shell";
import { Button } from "@/components/ui/button";
import { useAuthGuard } from "@/hooks/use-auth-guard";
import { cancelBooking, getBookingsByTenant } from "@/services/booking-service";
import { useToastStore } from "@/store/toast-store";
import { Booking } from "@/types/booking";

export default function AdminBookingsPage() {
  const { session } = useAuthGuard();
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [loading, setLoading] = useState(true);
  const pushToast = useToastStore((state) => state.push);

  useEffect(() => {
    const run = async () => {
      if (!session?.tenantId) {
        return;
      }
      try {
        setBookings(await getBookingsByTenant(session.tenantId));
      } finally {
        setLoading(false);
      }
    };
    void run();
  }, [session?.tenantId]);

  const cancel = async (bookingId: string) => {
    try {
      const updated = await cancelBooking(bookingId);
      setBookings((current) => current.map((item) => item.bookingId === bookingId ? updated : item));
      pushToast({ tone: "success", title: "Cita cancelada", description: "La reserva fue actualizada." });
    } catch (error) {
      const message = (error as AxiosError<{ message?: string }>).response?.data?.message ?? "No fue posible cancelar.";
      pushToast({ tone: "error", title: "Error en citas", description: message });
    }
  };

  return (
    <ProtectedShell>
      <section className="panel p-6">
        <h1 className="text-2xl font-semibold text-ink">Administracion de citas</h1>
        <div className="mt-6 space-y-3">
          {loading ? <p className="text-sm text-slate">Cargando reservas...</p> : null}
          {!loading && !bookings.length ? <p className="text-sm text-slate">No hay citas registradas.</p> : null}
          {bookings.map((booking) => (
            <div key={booking.bookingId} className="rounded-md border border-slate-200 p-4">
              <div className="flex flex-wrap items-center justify-between gap-3">
                <div>
                  <div className="font-medium text-ink">{booking.customerName}</div>
                  <div className="text-sm text-slate">{booking.serviceName}</div>
                </div>
                <div className="text-sm text-slate">{booking.bookingDate} · {booking.startTime}</div>
                <div className="flex items-center gap-3">
                  <span className="chip">{booking.status}</span>
                  {booking.status !== "CANCELLED" ? (
                    <Button variant="ghost" onClick={() => cancel(booking.bookingId)}>Cancelar</Button>
                  ) : null}
                </div>
              </div>
            </div>
          ))}
        </div>
      </section>
    </ProtectedShell>
  );
}
 
