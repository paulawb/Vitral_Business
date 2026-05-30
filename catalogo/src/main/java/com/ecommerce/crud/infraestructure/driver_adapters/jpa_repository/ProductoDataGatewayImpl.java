package com.ecommerce.crud.infraestructure.driver_adapters.jpa_repository;

import com.ecommerce.crud.domain.model.Producto;
import com.ecommerce.crud.domain.model.gateway.Productogateway;
import com.ecommerce.crud.infraestructure.exception.ProductoNoEncontradoException;
import com.ecommerce.crud.infraestructure.mapper.ProductoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductoDataGatewayImpl implements Productogateway {

    private final ProductoDataJpaRepository repository;
    private final ProductoMapper productoMapper;

    @Override
    public Producto guardarProducto(Producto producto) {
        ProductoData productoData = productoMapper.toProductoData(producto);
        return productoMapper.toProducto(repository.save(productoData));
    }
    @Override
    public Producto buscarProductoPorId(Long productoId) {
        return repository.findById(productoId)
                .map(productoMapper::toProducto)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con id: " + productoId));
    }

    @Override
    public Producto buscarProductoPorNombre(String nombre) {
        return repository.findByNombre(nombre)
                .map(productoMapper::toProducto)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con nombre: " + nombre));
    }

    @Override
    public Producto buscarProductoPorDescripcion(String descripcion) {
        return repository.findByDescripcion(descripcion)
                .map(productoMapper::toProducto)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con descripcion: " + descripcion));
    }
    @Override
    public Producto buscarProductoPorPrecio(Double precio) {
        return repository.findByPrecio(precio)
                .map(productoMapper::toProducto)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con precio: " + precio));
    }

    @Override
    public Producto buscarProductoPorStock(Integer stock) {
        return repository.findByStock(stock)
                .map(productoMapper::toProducto)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con stock: " + stock));
    }

    @Override
    public Producto buscarProductoPorSlug(String slug) {
        return repository.findBySlug(slug)
                .map(productoMapper::toProducto)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con slug: " + slug));
    }

    @Override
    public List<Producto> listarPorTenant(String tenantId) {
        return repository.findAllByTenantId(tenantId)
                .stream()
                .map(productoMapper::toProducto)
                .toList();
    }

    @Override
    public Producto actualizarProducto(Producto producto) {
        if (!repository.existsById(producto.getProductoId())) {
            throw new ProductoNoEncontradoException("Producto no encontrado con id: " + producto.getProductoId());
        }
        ProductoData productoData = productoMapper.toProductoData(producto);
        return productoMapper.toProducto(repository.save(productoData));
    }

    @Override
    public void eliminarProductoPorId(Long id) {
        if (!repository.existsById(id)) {
            throw new ProductoNoEncontradoException("Producto no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }
}
