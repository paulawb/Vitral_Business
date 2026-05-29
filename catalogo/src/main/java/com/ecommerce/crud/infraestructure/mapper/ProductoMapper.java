package com.ecommerce.crud.infraestructure.mapper;

import com.ecommerce.crud.domain.model.Producto;
import com.ecommerce.crud.infraestructure.driver_adapters.jpa_repository.ProductoData;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toProducto(ProductoData productoData) {
        return Producto.builder()
                .productoId(productoData.getProductoId())
                .uuid(productoData.getUuid())
                .tenantId(productoData.getTenantId())
                .slug(productoData.getSlug())
                .nombre(productoData.getNombre())
                .descripcion(productoData.getDescripcion())
                .precio(productoData.getPrecio() == null ? 0D : productoData.getPrecio())
                .precioDescuento(productoData.getPrecioDescuento())
                .stock(productoData.getStock())
                .categoria(productoData.getCategoria())
                .subCategoria(productoData.getSubCategoria())
                .thumbnail(productoData.getThumbnail())
                .tags(productoData.getTags())
                .disponible(productoData.getDisponible())
                .destacado(productoData.getDestacado())
                .tipoProducto(productoData.getTipoProducto())
                .vertical(productoData.getVertical())
                .duracion(productoData.getDuracion())
                .marca(productoData.getMarca())
                .colores(productoData.getColores())
                .rating(productoData.getRating())
                .vecesVisto(productoData.getVecesVisto())
                .vecesAgendado(productoData.getVecesAgendado())
                .fechaActualizacion(productoData.getFechaActualizacion())
                .build();
    }

    public ProductoData toProductoData(Producto producto) {
        return ProductoData.builder()
                .productoId(producto.getProductoId())
                .uuid(producto.getUuid())
                .tenantId(producto.getTenantId())
                .slug(producto.getSlug())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .precioDescuento(producto.getPrecioDescuento())
                .stock(producto.getStock())
                .categoria(producto.getCategoria())
                .subCategoria(producto.getSubCategoria())
                .thumbnail(producto.getThumbnail())
                .tags(producto.getTags())
                .disponible(producto.getDisponible())
                .destacado(producto.getDestacado())
                .tipoProducto(producto.getTipoProducto())
                .vertical(producto.getVertical())
                .duracion(producto.getDuracion())
                .marca(producto.getMarca())
                .colores(producto.getColores())
                .rating(producto.getRating())
                .vecesVisto(producto.getVecesVisto())
                .vecesAgendado(producto.getVecesAgendado())
                .fechaActualizacion(producto.getFechaActualizacion())
                .build();
    }
}
