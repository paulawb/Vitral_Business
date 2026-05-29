package com.ecommerce.crud.domain.model.gateway;

import com.ecommerce.crud.domain.model.Producto;

import java.util.List;

public interface Productogateway {

    Producto guardarProducto(Producto producto);

    Producto buscarProductoPorId(Long productoId);

    Producto buscarProductoPorNombre(String nombre);

    Producto buscarProductoPorDescripcion(String descripcion);

    Producto buscarProductoPorPrecio(Double precio);

    Producto buscarProductoPorStock(Integer stock);

    Producto buscarProductoPorSlug(String slug);

    List<Producto> listarPorTenant(String tenantId);

    Producto actualizarProducto(Producto producto);

    void eliminarProductoPorId(Long id);
}
