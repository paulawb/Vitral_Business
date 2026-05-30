"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { EmptyState } from "@/components/feedback/empty-state";
import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";
import { trackEvent } from "@/services/analytics-service";
import { getProductBySlug } from "@/services/catalog-service";
import { Product } from "@/types/catalog";
import { formatCurrency } from "@/utils/format";

type Props = {
  params: { slug: string };
};

export default function ProductDetailPage({ params }: Props) {
  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const run = async () => {
      try {
        const item = await getProductBySlug(params.slug);
        setProduct(item);
        if (item.tenantId) {
          await trackEvent({
            tenantId: item.tenantId,
            eventType: "PRODUCT_VIEW",
            resourceId: String(item.productoId),
            resourceName: item.nombre,
            channel: "web"
          });
        }
      } finally {
        setLoading(false);
      }
    };
    void run();
  }, [params.slug]);

  if (loading) {
    return <main className="container-app py-8"><Skeleton className="h-[540px]" /></main>;
  }

  if (!product) {
    return <main className="container-app py-8"><EmptyState title="Producto no encontrado" description="No existe un item con ese slug." /></main>;
  }

  return (
    <main className="container-app py-8">
      <div className="grid gap-6 lg:grid-cols-[1.1fr_.9fr]">
        <div className="panel aspect-square bg-cover bg-center" style={{ backgroundImage: `url('${product.thumbnail || "https://images.unsplash.com/photo-1523293182086-7651a899d37f?auto=format&fit=crop&w=1200&q=80"}')` }} />
        <section className="panel p-6">
          <span className="chip">Detalle</span>
          <h1 className="mt-4 text-3xl font-semibold capitalize text-ink">{product.nombre}</h1>
          <p className="mt-3 text-sm leading-6 text-slate">{product.descripcion}</p>
          <div className="mt-6 grid gap-3 sm:grid-cols-2">
            <div className="rounded-md bg-sand p-4">
              <div className="text-sm text-slate">Precio</div>
              <div className="mt-1 text-xl font-semibold text-ink">{formatCurrency(product.precio)}</div>
            </div>
            <div className="rounded-md bg-sand p-4">
              <div className="text-sm text-slate">Duracion</div>
              <div className="mt-1 text-xl font-semibold text-ink">{product.duracion ? `${product.duracion} min` : "No aplica"}</div>
            </div>
          </div>
          {product.tenantId && product.slug ? (
            <div className="mt-6">
              <Link href={`/citas?tenant=${product.tenantId}&slug=${product.slug}&service=${encodeURIComponent(product.nombre)}`}>
                <Button>Reservar este servicio</Button>
              </Link>
            </div>
          ) : null}
        </section>
      </div>
    </main>
  );
}
