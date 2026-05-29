"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { AxiosError } from "axios";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { ColorPicker } from "@/components/ui/color-picker";
import { assignTenant } from "@/services/auth-service";
import { createBusiness } from "@/services/business-service";
import { useAuthStore } from "@/store/auth-store";
import { useTenantStore } from "@/store/tenant-store";
import { useToastStore } from "@/store/toast-store";
import { APP_ROUTES } from "@/styles/tokens";

const schema = z.object({
  nombre: z.string().min(2, "Nombre obligatorio"),
  slug: z.string().min(2, "Slug obligatorio"),
  descripcion: z.string().optional(),
  vertical: z.string().min(2, "Vertical obligatoria"),
  whatsappNumber: z.string().min(7, "WhatsApp obligatorio"),
  phone: z.string().min(7, "Telefono obligatorio"),
  primaryColor: z.string().optional(),
  secondaryColor: z.string().optional(),
  logoUrl: z.string().optional()
});

type FormData = z.infer<typeof schema>;

export function BusinessOnboardingForm() {
  const router = useRouter();
  const session = useAuthStore((state) => state.session);
  const updateTenant = useAuthStore((state) => state.updateTenant);
  const setBusiness = useTenantStore((state) => state.setBusiness);
  const pushToast = useToastStore((state) => state.push);
  const { register, handleSubmit, watch, setValue, formState: { errors, isSubmitting } } = useForm<FormData>({
    resolver: zodResolver(schema),
    defaultValues: { vertical: "barberia", primaryColor: "#3B82F6", secondaryColor: "#10B981" }
  });

  const nombre = watch("nombre");
  const primaryColor = watch("primaryColor");
  const secondaryColor = watch("secondaryColor");

  const handleLogoUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      // For now, store as base64 - in production, upload to storage service
      const reader = new FileReader();
      reader.onload = (event) => {
        const base64 = event.target?.result as string;
        setValue("logoUrl", base64);
      };
      reader.readAsDataURL(file);
    }
  };

  const onSubmit = async (data: FormData) => {
    if (!session) {
      return;
    }
    try {
      const business = await createBusiness({
        tenantId: data.slug,
        slug: data.slug,
        nombre: data.nombre,
        descripcion: data.descripcion,
        vertical: data.vertical,
        businessType: data.vertical,
        whatsappNumber: data.whatsappNumber,
        phone: data.phone,
        email: session.user.correo,
        active: true,
        timezone: "America/Bogota",
        primaryColor: data.primaryColor,
        secondaryColor: data.secondaryColor,
        logoUrl: data.logoUrl
      });
      const user = await assignTenant(session.user.cedula, business.tenantId);
      updateTenant(business.tenantId, user);
      setBusiness(business);
      pushToast({ tone: "success", title: "Negocio creado", description: "Tu tenant ya esta listo para operar." });
      router.replace(APP_ROUTES.dashboard);
    } catch (error) {
      const message = (error as AxiosError<{ message?: string }>).response?.data?.message ?? "No fue posible crear el negocio.";
      pushToast({ tone: "error", title: "Onboarding fallido", description: message });
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="panel w-full max-w-3xl space-y-5 p-6">
      <div className="space-y-1">
        <h1 className="text-2xl font-semibold text-ink">Crea tu negocio</h1>
        <p className="text-sm text-slate">Este paso es posterior al registro. No fue requerido para abrir tu cuenta.</p>
      </div>
      <div className="grid gap-4 sm:grid-cols-2">
        <Input label="Nombre comercial" error={errors.nombre?.message} {...register("nombre")} />
        <Input
          label="Slug publico"
          error={errors.slug?.message}
          placeholder={(nombre || "mi-negocio").toLowerCase().replace(/\s+/g, "-")}
          {...register("slug")}
        />
        <Input label="Vertical" error={errors.vertical?.message} {...register("vertical")} />
        <Input label="WhatsApp" error={errors.whatsappNumber?.message} {...register("whatsappNumber")} />
        <Input label="Telefono" error={errors.phone?.message} {...register("phone")} />
      </div>
      <Textarea label="Descripcion" error={errors.descripcion?.message} {...register("descripcion")} />
      
      <div className="space-y-3">
        <label className="text-sm font-medium text-ink">Logo del negocio</label>
        <Input type="file" accept="image/*" onChange={handleLogoUpload} />
        {watch("logoUrl") && (
          <img src={watch("logoUrl")} alt="Preview" className="h-20 w-20 rounded object-cover" />
        )}
      </div>

      <div className="grid gap-4 sm:grid-cols-2">
        <div>
          <label className="text-sm font-medium text-ink">Color primario</label>
          <ColorPicker value={primaryColor} onChange={(color) => setValue("primaryColor", color)} />
        </div>
        <div>
          <label className="text-sm font-medium text-ink">Color secundario</label>
          <ColorPicker value={secondaryColor} onChange={(color) => setValue("secondaryColor", color)} />
        </div>
      </div>

      <div className="flex flex-wrap gap-3">
        <Button type="submit" busy={isSubmitting}>Crear negocio</Button>
        <Button type="button" variant="ghost" onClick={() => router.replace(APP_ROUTES.dashboard)}>Configurar despues</Button>
      </div>
    </form>
  );
}
