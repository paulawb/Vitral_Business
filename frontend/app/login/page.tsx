import Link from "next/link";
import { LoginForm } from "@/components/forms/login-form";
import { APP_ROUTES } from "@/styles/tokens";
 
export default function LoginPage() {
  return (
    <main className="flex min-h-screen items-center justify-center bg-sand px-4 py-8">
      <div className="grid w-full max-w-5xl gap-6 lg:grid-cols-[1fr_440px]">
        <section className="hidden rounded-2xl bg-night p-8 text-white lg:flex lg:flex-col lg:justify-between">
          <div className="space-y-4">
            <div className="text-sm uppercase tracking-[0.22em] text-white/65">Vitral Access</div>
            <h1 className="text-4xl font-semibold leading-tight">Controla citas, catalogo y operaciones desde el primer ingreso.</h1>
            <p className="max-w-xl text-base leading-7 text-white/75">La sesion persiste, las rutas se protegen y el flujo de onboarding separa el registro del negocio.</p>
          </div>
          <Link href={APP_ROUTES.register} className="text-sm text-white/82 underline-offset-4 hover:underline">
            Crear cuenta nueva
          </Link>
        </section>
        <LoginForm />
      </div>
    </main>
  );
}
