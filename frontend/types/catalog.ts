export type Product = {
  productoId: number;
  uuid?: string;
  tenantId?: string;
  slug?: string;
  nombre: string;
  descripcion?: string | null;
  precio: number;
  precioDescuento?: number | null;
  stock?: number | null;
  categoria?: string | null;
  subCategoria?: string | null;
  thumbnail?: string | null;
  tags?: string | null;
  disponible?: boolean | null;
  destacado?: boolean | null;
  tipoProducto?: string | null;
  vertical?: string | null;
  duracion?: number | null;
  marca?: string | null;
  colores?: string | null;
  rating?: number | null;
  vecesVisto?: number | null;
  vecesAgendado?: number | null;
};
 
export type ProductInput = Partial<Product> & {
  nombre: string;
  precio: number;
  descripcion?: string;
};
