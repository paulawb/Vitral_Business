package com.ecommerce.crud.domain.usecase;

import com.ecommerce.crud.domain.model.Producto;
import com.ecommerce.crud.domain.model.gateway.Productogateway;
import com.ecommerce.crud.infraestructure.exception.ValidacionException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ProductoUseCase {
    private final Productogateway productogateway;

    public Producto guardarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new ValidacionException("nombre es requerido - guardarProducto");
        }
        if (producto.getDescripcion() == null) {
            throw new ValidacionException("descripcion es requerida - guardarProducto");
        }
        producto.setUuid(producto.getUuid() == null ? UUID.randomUUID().toString() : producto.getUuid());
        producto.setSlug(producto.getSlug() == null ? slugify(producto.getNombre()) : slugify(producto.getSlug()));
        producto.setTenantId(vacio(producto.getTenantId()) ? "default" : producto.getTenantId());
        producto.setDisponible(producto.getDisponible() == null ? Boolean.TRUE : producto.getDisponible());
        producto.setDestacado(producto.getDestacado() == null ? Boolean.FALSE : producto.getDestacado());
        producto.setRating(producto.getRating() == null ? 0D : producto.getRating());
        producto.setVecesAgendado(producto.getVecesAgendado() == null ? 0L : producto.getVecesAgendado());
        producto.setVecesVisto(producto.getVecesVisto() == null ? 0L : producto.getVecesVisto());
        producto.setFechaActualizacion(LocalDateTime.now());
        return productogateway.guardarProducto(producto);
    }

    public Producto buscarProductoPorId(Long id) {
        return productogateway.buscarProductoPorId(id);
    }

    public Producto buscarProductoPorNombre(String nombre) {
        return productogateway.buscarProductoPorNombre(nombre);
    }

    public Producto buscarProductoPorDescripcion(String descripcion) {
        return productogateway.buscarProductoPorDescripcion(descripcion);
    }

    public Producto buscarProductoPorPrecio(Double precio) {
        return productogateway.buscarProductoPorPrecio(precio);
    }

    public Producto buscarProductoPorStock(Integer stock) {
        return productogateway.buscarProductoPorStock(stock);
    }

    public Producto buscarProductoPorSlug(String slug) {
        return productogateway.buscarProductoPorSlug(slug);
    }

    public List<Producto> listarPorTenant(String tenantId) {
        return productogateway.listarPorTenant(tenantId);
    }

    public Producto actualizarProducto(Producto producto) {
        if (producto.getProductoId() == null) {
            throw new ValidacionException("id del producto es incorrecto - actualizarProducto");
        }
        producto.setSlug(producto.getSlug() == null ? slugify(producto.getNombre()) : slugify(producto.getSlug()));
        producto.setFechaActualizacion(LocalDateTime.now());
        return productogateway.actualizarProducto(producto);
    }

    public void eliminarProductoPorId(Long id) {
        productogateway.eliminarProductoPorId(id);
    }

    private String slugify(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().toLowerCase().replace(" ", "-");
    }

    private boolean vacio(String value) {
        return value == null || value.trim().isEmpty();
    }
}
