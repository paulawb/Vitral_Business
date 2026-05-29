package com.ecommerce.crud.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProductoTest {

    @Test
    void constructorVacioYSetters_debenAsignarValores() {
        Producto producto = new Producto();

        producto.setProductoId(1L);
        producto.setNombre("Laptop");
        producto.setDescripcion("Portatil");
        producto.setPrecio(1000.0);
        producto.setStock(5);

        assertEquals(1L, producto.getProductoId());
        assertEquals("Laptop", producto.getNombre());
        assertEquals("Portatil", producto.getDescripcion());
        assertEquals(1000.0, producto.getPrecio());
        assertEquals(5, producto.getStock());
    }

    @Test
    void constructorCompleto_debeAsignarValores() {
        Producto producto = new Producto(1L, "Laptop", "Portatil", 1000.0, 5);

        assertEquals(1L, producto.getProductoId());
        assertEquals("Laptop", producto.getNombre());
        assertEquals("Portatil", producto.getDescripcion());
        assertEquals(1000.0, producto.getPrecio());
        assertEquals(5, producto.getStock());
    }

    @Test
    void constructorVacio_debeIniciarConNullsYCeroEnPrecio() {
        Producto producto = new Producto();

        assertNull(producto.getProductoId());
        assertNull(producto.getNombre());
        assertNull(producto.getDescripcion());
        assertEquals(0.0, producto.getPrecio());
        assertNull(producto.getStock());
    }
}
