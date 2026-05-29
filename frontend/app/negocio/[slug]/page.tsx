"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { EmptyState } from "@/components/feedback/empty-state";
import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";
import { trackEvent } from "@/services/analytics-service";
import { getBusinessBySlug } from "@/services/business-service";
import { getProductsByTenant } from "@/services/catalog-service";
import { getPrefilledLink } from "@/services/whatsapp-service";
import { Business } from "@/types/business";
import { Product } from "@/types/catalog";
import { formatCurrency } from "@/utils/format";

type Props = {
  params: { slug: string };
};

export default function BusinessPublicPage({ params }: Props) {
  const [business, setBusiness] = useState<Business | null>(null);
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const run = async () => {
      try {
        const businessData = await getBusinessBySlug(params.slug);
        setBusiness(businessData);
        const productsData = await getProductsByTenant(businessData.tenantId);
        setProducts(productsData);
        await trackEvent({
          tenantId: businessData.tenantId,
          eventType: "VISIT",
          resourceName: businessData.nombre,
          channel: "web"
        });
      } finally {
        setLoading(false);
      }
    };
    void run();
  }, [params.slug]);

  const openWhatsApp = async () => {
    if (!business?.whatsappNumber) {
      return;
    }
    const url = await getPrefilledLink(business.whatsappNumber, `Hola, quiero informacion sobre ${business.nombre}`);
    await trackEvent({
      tenantId: business.tenantId,
      eventType: "WHATSAPP_CLICK",
      resourceName: business.nombre,
      channel: "web"
    });
    window.open(url, "_blank", "noopener,noreferrer");
  };

  if (loading) {
    return <main className="container-app py-8"><Skeleton className="h-[480px]" /></main>;
  }

  if (!business) {
    return <main className="container-app py-8"><EmptyState title="Negocio no encontrado" description="El slug solicitado no existe en backend." /></main>;
  }

  return (
    <main className="container-app py-8">
      <section className="panel overflow-hidden">
        <div
          className="aspect-[5/2] bg-cover bg-center"
          style={{ backgroundImage: `linear-gradient(180deg, rgba(11,18,32,.2), rgba(11,18,32,.58)), url('${business.logoUrl || "https://images.unsplash.com/photo-1521590832167-7bcbfaa6381f?auto=format&fit=crop&w=1400&q=80"}')` }}
        />
        <div className="space-y-5 p-6">
          <span className="chip">Vista publica</span>
          <div className="space-y-2">
            <h1 className="text-3xl font-semibold text-ink">{business.nombre}</h1>
            <p className="max-w-2xl text-sm leading-6 text-slate">{business.descripcion || "Negocio disponible para reservas, catalogo y contacto directo."}</p>
          </div>
          <div className="flex flex-wrap gap-3">
            <Link href={`/citas?tenant=${business.tenantId}&slug=${business.slug}`}>
              <Button>Reservar cita</Button>
            </Link>
            <Button variant="ghost" onClick={openWhatsApp}>WhatsApp</Button>
          </div>
        </div>
      </section>
      <section className="mt-6 grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {products.map((product) => (
          <article key={product.productoId} className="panel overflow-hidden">
            <div className="aspect-[4/3] bg-cover bg-center" style={{ backgroundImage: `url('${product.thumbnail || "https://images.unsplash.com/photo-1523293182086-7651a899d37f?auto=format&fit=crop&w=1200&q=80"}')` }} />
            <div className="space-y-2 p-4">
              <span className="chip">{product.tipoProducto || "Item"}</span>
              <h2 className="text-lg font-semibold text-ink">{product.nombre}</h2>
              <div className="text-sm text-slate">{formatCurrency(product.precio)}</div>
              {product.slug ? <Link href={`/productos/${product.slug}`} className="text-sm font-medium text-ink underline-offset-4 hover:underline">Ver detalle</Link> : null}
            </div>
          </article>
        ))}
      </section>
    </main>
  );
}
