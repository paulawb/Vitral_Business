package com.ecommerce.crud.infraestructure.driver_adapters.jpa_repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProductoDataTest {

    @Test
    void constructorVacioYSetters_debenAsignarValores() {
        ProductoData productoData = new ProductoData();

        productoData.setProductoId(1L);
        productoData.setNombre("Laptop");
        productoData.setDescripcion("Portatil");
        productoData.setPrecio(1000.0);
        productoData.setStock(5);

        assertEquals(1L, productoData.getProductoId());
        assertEquals("Laptop", productoData.getNombre());
        assertEquals("Portatil", productoData.getDescripcion());
        assertEquals(1000.0, productoData.getPrecio());
        assertEquals(5, productoData.getStock());
    }

    @Test
    void constructorCompleto_debeAsignarValores() {
        ProductoData productoData = new ProductoData(1L, "Laptop", "Portatil", 1000.0, 5);

        assertEquals(1L, productoData.getProductoId());
        assertEquals("Laptop", productoData.getNombre());
        assertEquals("Portatil", productoData.getDescripcion());
        assertEquals(1000.0, productoData.getPrecio());
        assertEquals(5, productoData.getStock());
    }

    @Test
    void constructorVacio_debeIniciarConNulls() {
        ProductoData productoData = new ProductoData();

        assertNull(productoData.getProductoId());
        assertNull(productoData.getNombre());
        assertNull(productoData.getDescripcion());
        assertNull(productoData.getPrecio());
        assertNull(productoData.getStock());
    }
}
