"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import { EmptyState } from "@/components/feedback/empty-state";
import { Skeleton } from "@/components/ui/skeleton";
import { getProductsByTenant } from "@/services/catalog-service";
import { useAuthStore } from "@/store/auth-store";
import { Product } from "@/types/catalog";
import { formatCurrency } from "@/utils/format";

export default function CatalogPage() {
  const searchParams = useSearchParams();
  const session = useAuthStore((state) => state.session);
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const tenantId = searchParams.get("tenant") || session?.tenantId || "";

  useEffect(() => {
    const run = async () => {
      if (!tenantId || tenantId === "UNASSIGNED") {
        setLoading(false);
        return;
      }
      try {
        setProducts(await getProductsByTenant(tenantId));
      } finally {
        setLoading(false);
      }
    };
    void run();
  }, [tenantId]);

  return (
    <main className="container-app py-8">
      <div className="mb-6">
        <h1 className="text-2xl font-semibold text-ink">Catalogo publico</h1>
        <p className="text-sm text-slate">Datos reales desde el backend del catalogo.</p>
      </div>
      {loading ? (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {[1, 2, 3].map((item) => <Skeleton key={item} className="h-80" />)}
        </div>
      ) : !products.length ? (
        <EmptyState title="Catalogo vacio" description="Indica un tenant con ?tenant=... o entra por la vista publica del negocio." />
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {products.map((item) => (
            <article key={item.productoId} className="panel overflow-hidden">
              <div
                className="aspect-[4/3] bg-cover bg-center"
                style={{ backgroundImage: `url('${item.thumbnail || "https://images.unsplash.com/photo-1512496015851-a90fb38ba796?auto=format&fit=crop&w=900&q=80"}')` }}
              />
              <div className="space-y-2 p-4">
                <span className="chip">{item.tipoProducto || "Producto"}</span>
                <h2 className="text-lg font-semibold text-ink">{item.nombre}</h2>
                <div className="text-sm text-slate">{formatCurrency(item.precio)}</div>
                {item.slug ? <Link href={`/productos/${item.slug}`} className="text-sm font-medium text-ink underline-offset-4 hover:underline">Ver detalle</Link> : null}
              </div>
            </article>
          ))}
        </div>
      )}
    </main>
  );
}
