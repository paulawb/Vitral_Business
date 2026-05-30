"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { AxiosError } from "axios";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { register as registerUser } from "@/services/auth-service";
import { useToastStore } from "@/store/toast-store";
import { APP_ROUTES } from "@/styles/tokens";

const schema = z.object({
  cedula: z.string().min(5, "Documento obligatorio"),
  nombres: z.string().min(2, "Nombre obligatorio"),
  apellidos: z.string().min(2, "Apellido obligatorio"),
  correo: z.string().email("Correo invalido"),
  telefono: z.string().min(7, "Telefono obligatorio"),
  password: z.string().min(6, "Minimo 6 caracteres"),
});

type FormData = z.infer<typeof schema>;

export function RegisterForm() {
  const router = useRouter();
  const [serverError, setServerError] = useState<string | null>(null);
  const pushToast = useToastStore((state) => state.push);
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<FormData>({
    resolver: zodResolver(schema),
  });

  const onSubmit = async (data: FormData) => {
    setServerError(null);
    try {
      await registerUser(data);
      pushToast({
        tone: "success",
        title: "Cuenta creada",
        description:
          "Ahora puedes iniciar sesion y configurar tu negocio.",
      });
      router.push(APP_ROUTES.login);
    } catch (error) {
      const axiosError = error as AxiosError<any>;

      const backendMessage =
        axiosError.response?.data?.message ??
        axiosError.response?.data?.error ??
        (typeof axiosError.response?.data === "string"
          ? axiosError.response.data
          : undefined);

      const message =
        backendMessage ?? "No fue posible crear la cuenta.";

      setServerError(message);
      pushToast({
        tone: "error",
        title: "Registro fallido",
        description: message,
      });
    }
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="panel w-full max-w-2xl space-y-4 p-6"
    >
      <div className="space-y-1">
        <h1 className="text-2xl font-semibold text-ink">
          Registro inicial
        </h1>
        <p className="text-sm text-slate">
          Crea tu acceso primero. El negocio se configura despues.
        </p>
      </div>

      <div className="grid gap-4 sm:grid-cols-2">
        <Input
          label="Documento"
          error={errors.cedula?.message}
          {...register("cedula")}
        />
        <Input
          label="Telefono"
          error={errors.telefono?.message}
          {...register("telefono")}
        />
        <Input
          label="Nombres"
          error={errors.nombres?.message}
          {...register("nombres")}
        />
        <Input
          label="Apellidos"
          error={errors.apellidos?.message}
          {...register("apellidos")}
        />
        <Input
          label="Correo"
          error={errors.correo?.message}
          {...register("correo")}
        />
        <Input
          label="Contrasena"
          type="password"
          error={errors.password?.message}
          {...register("password")}
        />
      </div>

      {serverError ? (
        <p className="rounded-md bg-coral/10 px-3 py-2 text-sm text-coral">
          {serverError}
        </p>
      ) : null}

      <Button type="submit" busy={isSubmitting}>
        Crear cuenta
      </Button>
    </form>
  );
}
