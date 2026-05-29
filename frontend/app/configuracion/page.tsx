"use client";

import { useEffect, useState } from "react";
import { AxiosError } from "axios";
import { ProtectedShell } from "@/components/layout/protected-shell";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { useAuthGuard } from "@/hooks/use-auth-guard";
import { getBusinessByTenant, updateBusiness } from "@/services/business-service";
import { useTenantStore } from "@/store/tenant-store";
import { useToastStore } from "@/store/toast-store";

export default function SettingsPage() {
  const { session } = useAuthGuard();
  const business = useTenantStore((state) => state.business);
  const setBusiness = useTenantStore((state) => state.setBusiness);
  const pushToast = useToastStore((state) => state.push);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [form, setForm] = useState({
    nombre: "",
    descripcion: "",
    primaryColor: "#101828",
    secondaryColor: "#12B76A",
    instagramUrl: "",
    whatsappNumber: ""
  });

  useEffect(() => {
    const run = async () => {
      if (!session?.tenantId) {
        return;
      }
      try {
        const data = await getBusinessByTenant(session.tenantId);
        setBusiness(data);
        setForm({
          nombre: data.nombre || "",
          descripcion: data.descripcion || "",
          primaryColor: data.primaryColor || "#101828",
          secondaryColor: data.secondaryColor || "#12B76A",
          instagramUrl: data.instagramUrl || "",
          whatsappNumber: data.whatsappNumber || ""
        });
      } finally {
        setLoading(false);
      }
    };
    void run();
  }, [session?.tenantId, setBusiness]);

  const save = async () => {
    if (!business) {
      return;
    }
    setSaving(true);
    try {
      const updated = await updateBusiness(business.slug, {
        ...business,
        ...form
      });
      setBusiness(updated);
      pushToast({ tone: "success", title: "Negocio actualizado", description: "La configuracion quedo guardada." });
    } catch (error) {
      const message = (error as AxiosError<{ message?: string }>).response?.data?.message ?? "No fue posible actualizar el negocio.";
      pushToast({ tone: "error", title: "Error al guardar", description: message });
    } finally {
      setSaving(false);
    }
  };

  return (
    <ProtectedShell>
      <section className="panel p-6">
        <h1 className="text-2xl font-semibold text-ink">Configuracion del negocio</h1>
        {loading ? (
          <p className="mt-4 text-sm text-slate">Cargando configuracion...</p>
        ) : (
          <div className="mt-6 grid gap-4">
            <div className="grid gap-4 sm:grid-cols-2">
              <Input label="Nombre comercial" value={form.nombre} onChange={(event) => setForm((prev) => ({ ...prev, nombre: event.target.value }))} />
              <Input label="Instagram" value={form.instagramUrl} onChange={(event) => setForm((prev) => ({ ...prev, instagramUrl: event.target.value }))} />
              <Input label="Color primario" value={form.primaryColor} onChange={(event) => setForm((prev) => ({ ...prev, primaryColor: event.target.value }))} />
              <Input label="Color secundario" value={form.secondaryColor} onChange={(event) => setForm((prev) => ({ ...prev, secondaryColor: event.target.value }))} />
              <Input label="WhatsApp" value={form.whatsappNumber} onChange={(event) => setForm((prev) => ({ ...prev, whatsappNumber: event.target.value }))} />
            </div>
            <Textarea label="Descripcion" value={form.descripcion} onChange={(event) => setForm((prev) => ({ ...prev, descripcion: event.target.value }))} />
            <Button busy={saving} onClick={save}>Guardar cambios</Button>
          </div>
        )}
      </section>
    </ProtectedShell>
  );
}
