"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { AxiosError } from "axios";
import { useEffect, useMemo, useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { createBooking, getAvailability } from "@/services/booking-service";
import { useToastStore } from "@/store/toast-store";
import { AvailabilitySlot } from "@/types/booking";

const schema = z.object({
  tenantId: z.string().min(2),
  businessSlug: z.string().min(2),
  serviceName: z.string().min(2),
  customerName: z.string().min(2),
  customerEmail: z.string().email(),
  customerPhone: z.string().min(7),
  bookingDate: z.string().min(1)
});

type FormData = z.infer<typeof schema>;

type Props = {
  defaults?: Partial<FormData>;
};

export function BookingForm({ defaults }: Props) {
  const pushToast = useToastStore((state) => state.push);
  const [slots, setSlots] = useState<AvailabilitySlot[]>([]);
  const [selectedSlot, setSelectedSlot] = useState<AvailabilitySlot | null>(null);
  const [loadingSlots, setLoadingSlots] = useState(false);
  const { register, handleSubmit, watch, formState: { errors, isSubmitting } } = useForm<FormData>({
    resolver: zodResolver(schema),
    defaultValues: defaults
  });

  const tenantId = watch("tenantId");
  const bookingDate = watch("bookingDate");

  useEffect(() => {
    const run = async () => {
      if (!tenantId || !bookingDate) {
        setSlots([]);
        return;
      }
      setLoadingSlots(true);
      try {
        const response = await getAvailability(tenantId, bookingDate);
        setSlots(response);
      } catch {
        setSlots([]);
      } finally {
        setLoadingSlots(false);
      }
    };
    void run();
  }, [bookingDate, tenantId]);

  const availableSlots = useMemo(() => slots.filter((slot) => slot.available), [slots]);

  const onSubmit = async (data: FormData) => {
    if (!selectedSlot) {
      pushToast({ tone: "error", title: "Selecciona un horario", description: "Debes escoger un slot disponible." });
      return;
    }
    try {
      await createBooking({
        ...data,
        startTime: selectedSlot.startTime,
        endTime: selectedSlot.endTime
      });
      pushToast({ tone: "success", title: "Reserva creada", description: "La cita quedo registrada correctamente." });
    } catch (error) {
      const message = (error as AxiosError<{ message?: string }>).response?.data?.message ?? "No fue posible crear la reserva.";
      pushToast({ tone: "error", title: "Reserva fallida", description: message });
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="panel space-y-4 p-6">
      <div className="space-y-1">
        <h2 className="text-2xl font-semibold text-ink">Agendar cita</h2>
        <p className="text-sm text-slate">Selecciona fecha y horario disponibles.</p>
      </div>
      <div className="grid gap-4 sm:grid-cols-2">
        <Input label="Tenant" error={errors.tenantId?.message} {...register("tenantId")} />
        <Input label="Slug negocio" error={errors.businessSlug?.message} {...register("businessSlug")} />
        <Input label="Servicio" error={errors.serviceName?.message} {...register("serviceName")} />
        <Input label="Nombre" error={errors.customerName?.message} {...register("customerName")} />
        <Input label="Correo" error={errors.customerEmail?.message} {...register("customerEmail")} />
        <Input label="Telefono" error={errors.customerPhone?.message} {...register("customerPhone")} />
        <Input label="Fecha" type="date" error={errors.bookingDate?.message} {...register("bookingDate")} />
      </div>
      <div className="space-y-2">
        <div className="text-sm font-medium text-ink">Horarios disponibles</div>
        {loadingSlots ? <div className="text-sm text-slate">Consultando disponibilidad...</div> : null}
        <div className="grid gap-2 sm:grid-cols-3">
          {availableSlots.map((slot) => (
            <button
              key={`${slot.startTime}-${slot.endTime}`}
              type="button"
              onClick={() => setSelectedSlot(slot)}
              className={`rounded-md border px-3 py-3 text-sm transition ${selectedSlot?.startTime === slot.startTime ? "border-ink bg-ink text-white" : "border-slate-200 bg-white text-ink hover:bg-slate-50"}`}
            >
              {slot.startTime} - {slot.endTime}
            </button>
          ))}
        </div>
      </div>
      <Button type="submit" busy={isSubmitting}>Confirmar cita</Button>
    </form>
  );
}
