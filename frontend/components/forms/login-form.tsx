"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { AxiosError } from "axios";
import { useRouter, useSearchParams } from "next/navigation";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { login } from "@/services/auth-service";
import { useAuthStore } from "@/store/auth-store";
import { useToastStore } from "@/store/toast-store";
import { APP_ROUTES } from "@/styles/tokens";

const schema = z.object({
  correo: z.string().email("Correo invalido"),
  password: z.string().min(6, "Minimo 6 caracteres")
});

type FormData = z.infer<typeof schema>;

export function LoginForm() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const redirect = searchParams.get("redirect");
  const [serverError, setServerError] = useState<string | null>(null);
  const setAuth = useAuthStore((state) => state.setAuth);
  const pushToast = useToastStore((state) => state.push);
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<FormData>({
    resolver: zodResolver(schema)
  });

  const onSubmit = async (data: FormData) => {
    setServerError(null);
    try {
      const response = await login(data);
      setAuth(response);
      pushToast({ tone: "success", title: "Sesion iniciada", description: "Tu acceso fue validado correctamente." });
      const destination = response.tenantId === "UNASSIGNED" ? APP_ROUTES.onboarding : redirect || APP_ROUTES.dashboard;
      router.replace(destination);
    } catch (error) {
      const message = (error as AxiosError<{ message?: string }>).response?.data?.message ?? "No fue posible iniciar sesion.";
      setServerError(message);
      pushToast({ tone: "error", title: "Error de acceso", description: message });
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="panel w-full max-w-md space-y-4 p-6">
      <div className="space-y-1">
        <h1 className="text-2xl font-semibold text-ink">Iniciar sesion</h1>
        <p className="text-sm text-slate">Accede al panel de tu negocio.</p>
      </div>
      <Input label="Correo" placeholder="negocio@correo.com" error={errors.correo?.message} {...register("correo")} />
      <Input label="Contrasena" type="password" placeholder="••••••••" error={errors.password?.message} {...register("password")} />
      {serverError ? <p className="rounded-md bg-coral/10 px-3 py-2 text-sm text-coral">{serverError}</p> : null}
      <Button type="submit" busy={isSubmitting} block>
        Entrar al panel
      </Button>
    </form>
  );
}
