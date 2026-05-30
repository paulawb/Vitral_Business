"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { SiteHeader } from "@/components/layout/site-header";
import { Button } from "@/components/ui/button";
import { useAuthStore } from "@/store/auth-store";
import { APP_ROUTES } from "@/styles/tokens";

const features = [
  {
    title: "Reservas en tiempo real",
    description: "Controla disponibilidad, citas y estados desde una sola interfaz."
  },
  {
    title: "Catalogo con servicios y productos",
    description: "Publica verticales distintas sin cambiar la base operativa."
  },
  {
    title: "Analytics y conversiones",
    description: "Mide visitas, clics de WhatsApp y comportamiento de reserva."
  }
];

export default function HomePage() {
  const router = useRouter();
  const session = useAuthStore((state) => state.session);
  const authenticated = !!session?.accessToken;

  const handleDashboardClick = () => {
    if (authenticated) {
      router.push(APP_ROUTES.dashboard);
    } else {
      router.push(APP_ROUTES.login);
    }
  };

  return (
    <main className="min-h-screen bg-night text-white">
      <div
        className="min-h-screen bg-cover bg-center"
        style={{
          backgroundImage:
            "linear-gradient(180deg, rgba(11,18,32,0.82) 0%, rgba(11,18,32,0.68) 35%, rgba(11,18,32,0.88) 100%), url('https://images.unsplash.com/photo-1521590832167-7bcbfaa6381f?auto=format&fit=crop&w=1800&q=80')"
        }}
      >
        <SiteHeader />
        <section className="container-app grid min-h-[calc(100vh-80px)] gap-10 py-10 lg:grid-cols-[1.1fr_.9fr] lg:items-center">
          <div className="space-y-6">
            <span className="inline-flex items-center px-4 py-2 text-xs font-semibold uppercase tracking-[0.22em] text-ink bg-white/90 rounded-lg">
              Plataforma SaaS multi-tenant
            </span>
            <div className="space-y-4">
              <h1 className="hero-text-shadow max-w-3xl text-4xl font-semibold leading-tight sm:text-5xl lg:text-6xl">
                Opera reservas, catalogo y conversiones con una interfaz limpia y lista para demo.
              </h1>
              <p className="hero-text-shadow max-w-2xl text-base leading-7 text-white/88 sm:text-lg">
                Vitral conecta autenticacion, catalogo, negocio, citas, WhatsApp y analitica con un frontend responsive y listo para mostrar en universidad o evolucionar a producto.
              </p>
            </div>
            <div className="flex flex-wrap gap-3">
              <Link href={APP_ROUTES.login}>
                <Button>Login</Button>
              </Link>
              <Link href={APP_ROUTES.register}>
                <Button variant="secondary">Registro</Button>
              </Link>
              <Button variant="secondary" onClick={handleDashboardClick}>
                Entrar al panel
              </Button>
            </div>
            <div className="grid gap-3 pt-4 sm:grid-cols-3">
              {features.map((feature) => (
                <article key={feature.title} className="glass-panel p-4">
                  <h2 className="text-sm font-semibold text-ink">{feature.title}</h2>
                  <p className="mt-2 text-sm leading-6 text-slate">{feature.description}</p>
                </article>
              ))}
            </div>
          </div>
          <div className="glass-panel p-5">
            <div className="grid gap-4">
              <div className="rounded-lg bg-white/90 p-5">
                <div className="text-sm text-ink">Conversion semanal</div>
                <div className="mt-2 text-2xl font-semibold text-ink">18.4%</div>
              </div>
              <div className="grid gap-4 sm:grid-cols-2">
                <div className="rounded-lg bg-white/90 p-5">
                  <div className="text-sm text-ink">Reservas</div>
                  <div className="mt-2 text-2xl font-semibold text-ink">148</div>
                </div>
                <div className="rounded-lg bg-white/90 p-5">
                  <div className="text-sm text-ink">Clicks WhatsApp</div>
                  <div className="mt-2 text-2xl font-semibold text-ink">312</div>
                </div>
              </div>
              <div className="rounded-lg border border-slate-200 bg-white/90 p-5">
                <div className="text-sm text-ink">Negocios objetivo</div>
                <div className="mt-3 flex flex-wrap gap-2">
                  {["Barberias", "Opticas", "Tatuadores", "Restaurantes"].map((item) => (
                    <span key={item} className="rounded-full bg-slate-100 px-3 py-2 text-sm text-ink">
                      {item}
                    </span>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </section>
      </div>
    </main>
  );
}
