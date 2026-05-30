"use client";

import { useEffect, useState } from "react"; 
import { AxiosError } from "axios";
import { ProtectedShell } from "@/components/layout/protected-shell";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { createProduct, getProductsByTenant } from "@/services/catalog-service";
import { useAuthGuard } from "@/hooks/use-auth-guard";
import { Product } from "@/types/catalog";
import { useToastStore } from "@/store/toast-store";

export default function AdminProductsPage() {
  const { session } = useAuthGuard();
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [form, setForm] = useState({ nombre: "", descripcion: "", precio: 0, stock: 0 });
  const pushToast = useToastStore((state) => state.push);

  useEffect(() => {
    const run = async () => {
      if (!session?.tenantId) {
        return;
      }
      try {
        setProducts(await getProductsByTenant(session.tenantId));
      } finally {
        setLoading(false);
      }
    };
    void run();
  }, [session?.tenantId]);

  const save = async () => {
    if (!session?.tenantId) {
      return;
    }
    setSaving(true);
    try {
      const created = await createProduct({
        ...form,
        tenantId: session.tenantId,
        tipoProducto: "PRODUCTO",
        disponible: true
      });
      setProducts((current) => [created, ...current]);
      setForm({ nombre: "", descripcion: "", precio: 0, stock: 0 });
      pushToast({ tone: "success", title: "Producto creado", description: "El catalogo se actualizo correctamente." });
    } catch (error) {
       const axiosError = error as AxiosError<{ message?: string; errores?: Record<string, string> }>;
       const responseData = axiosError.response?.data;
       const errorMessages = responseData?.errores
         ? Object.values(responseData.errores).join(", ")
         : responseData?.message ?? "No fue posible crear el producto.";
       pushToast({ tone: "error", title: "Error de catalogo", description: errorMessages });
     } finally {
      setSaving(false);
    }
  };

  return (
    <ProtectedShell>
      <section className="panel p-6">
        <div className="flex items-center justify-between">
          <h1 className="text-2xl font-semibold text-ink">Administracion de productos</h1>
        </div>
        <div className="mt-6 grid gap-4 sm:grid-cols-2">
           <Input label="Nombre" value={form.nombre} onChange={(event) => setForm((prev) => ({ ...prev, nombre: event.target.value }))} />
           <Textarea label="Descripcion (opcional)" value={form.descripcion ?? ""} onChange={(event) => setForm((prev) => ({ ...prev, descripcion: event.target.value }))} />
           <Input label="Precio" type="number" value={String(form.precio)} onChange={(event) => setForm((prev) => ({ ...prev, precio: Number(event.target.value) || 0 }))} />
           <Input label="Stock" type="number" value={String(form.stock)} onChange={(event) => setForm((prev) => ({ ...prev, stock: Number(event.target.value) || 0 }))} />
         </div>
        <div className="mt-4">
          <Button busy={saving} onClick={save}>Guardar producto</Button>
        </div>
        <div className="mt-6 overflow-x-auto">
          {loading ? (
            <p className="text-sm text-slate">Cargando productos...</p>
          ) : (
            <table className="min-w-full text-left text-sm">
              <thead>
                <tr className="text-slate">
                  <th className="pb-3">Nombre</th>
                  <th className="pb-3">Categoria</th>
                  <th className="pb-3">Precio</th>
                </tr>
              </thead>
              <tbody>
                {products.map((product) => (
                  <tr key={product.productoId} className="border-t border-slate-200">
                    <td className="py-4">{product.nombre}</td>
                    <td className="py-4">{product.categoria || product.tipoProducto || "General"}</td>
                    <td className="py-4">{product.precio}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </section>
    </ProtectedShell>
  );
}
