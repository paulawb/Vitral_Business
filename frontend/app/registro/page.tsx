import Link from "next/link";
import { RegisterForm } from "@/components/forms/register-form";
import { APP_ROUTES } from "@/styles/tokens";

export default function RegisterPage() {
  return (
    <main className="flex min-h-screen items-center justify-center bg-sand px-4 py-8">
      <div className="grid w-full max-w-6xl gap-6 lg:grid-cols-[1fr_620px]">
        <section className="hidden rounded-2xl bg-white p-8 lg:block">
          <div className="chip">Onboarding progresivo</div>
          <h1 className="mt-4 text-4xl font-semibold leading-tight text-ink">Tu cuenta se crea primero. El tenant se configura despues.</h1>
          <p className="mt-4 max-w-xl text-base leading-7 text-slate">Se elimina la friccion inicial: registro basico, login real con JWT y luego creacion del negocio cuando el usuario ya esta autenticado.</p>
          <Link href={APP_ROUTES.login} className="mt-8 inline-block text-sm font-medium text-ink underline-offset-4 hover:underline">
            Ya tengo cuenta
          </Link>
        </section>
        <RegisterForm />
      </div>
    </main>
  );
}
