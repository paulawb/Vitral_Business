import { ProtectedShell } from "@/components/layout/protected-shell";

const plans = [
  { name: "Starter", detail: "Hasta 30 productos, 100 reservas y branding base." },
  { name: "Growth", detail: "Catalogo ampliado, analytics, conversiones y WhatsApp." },
  { name: "Scale", detail: "Mayor capacidad operativa, multiples equipos y seguimiento." }
];

export default function PlansPage() {
  return (
    <ProtectedShell>
      <section className="grid gap-4 lg:grid-cols-3">
        {plans.map((plan) => (
          <article key={plan.name} className="panel p-6">
            <div className="chip">Plan</div>
            <h1 className="mt-4 text-2xl font-semibold text-ink">{plan.name}</h1>
            <p className="mt-2 text-sm leading-6 text-slate">{plan.detail}</p>
          </article>
        ))}
      </section>
    </ProtectedShell>
  );
}
