package com.ecommerce.crud.infraestructure.mapper;

import com.ecommerce.crud.domain.model.Producto;
import com.ecommerce.crud.infraestructure.driver_adapters.jpa_repository.ProductoData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductoMapperTest {

    private final ProductoMapper mapper = new ProductoMapper();

    @Test
    void toProducto_debeMapearTodosLosCampos() {
        ProductoData productoData = new ProductoData(1L, "Laptop", "Portatil", 1000.0, 5);

        Producto producto = mapper.toProducto(productoData);

        assertEquals(1L, producto.getProductoId());
        assertEquals("Laptop", producto.getNombre());
        assertEquals("Portatil", producto.getDescripcion());
        assertEquals(1000.0, producto.getPrecio());
        assertEquals(5, producto.getStock());
    }

    @Test
    void toProductoData_debeMapearTodosLosCampos() {
        Producto producto = new Producto(1L, "Laptop", "Portatil", 1000.0, 5);

        ProductoData productoData = mapper.toProductoData(producto);

        assertEquals(1L, productoData.getProductoId());
        assertEquals("Laptop", productoData.getNombre());
        assertEquals("Portatil", productoData.getDescripcion());
        assertEquals(1000.0, productoData.getPrecio());
        assertEquals(5, productoData.getStock());
    }
}
