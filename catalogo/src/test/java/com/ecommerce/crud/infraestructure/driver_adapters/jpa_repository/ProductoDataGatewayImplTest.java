package com.ecommerce.crud.infraestructure.driver_adapters.jpa_repository;

import com.ecommerce.crud.domain.model.Producto;
import com.ecommerce.crud.infraestructure.exception.ProductoNoEncontradoException;
import com.ecommerce.crud.infraestructure.mapper.ProductoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoDataGatewayImplTest {

    @Mock
    private ProductoDataJpaRepository repository;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoDataGatewayImpl gateway;

    @Test
    void guardarProducto_debeMapearYGuardar() {
        Producto producto = crearProducto();
        ProductoData productoData = crearProductoData();
        when(productoMapper.toProductoData(producto)).thenReturn(productoData);
        when(repository.save(productoData)).thenReturn(productoData);
        when(productoMapper.toProducto(productoData)).thenReturn(producto);

        Producto resultado = gateway.guardarProducto(producto);

        assertEquals(producto, resultado);
    }

    @Test
    void buscarProductoPorId_debeRetornarProductoSiExiste() {
        ProductoData productoData = crearProductoData();
        Producto producto = crearProducto();
        when(repository.findById(1L)).thenReturn(Optional.of(productoData));
        when(productoMapper.toProducto(productoData)).thenReturn(producto);

        Producto resultado = gateway.buscarProductoPorId(1L);

        assertEquals(producto, resultado);
    }

    @Test
    void buscarProductoPorId_debeLanzarExcepcionSiNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ProductoNoEncontradoException error = assertThrows(
                ProductoNoEncontradoException.class,
                () -> gateway.buscarProductoPorId(1L)
        );

        assertEquals("Producto no encontrado con id: 1", error.getMessage());
    }

    @Test
    void buscarProductoPorNombre_debeRetornarProductoSiExiste() {
        ProductoData productoData = crearProductoData();
        Producto producto = crearProducto();
        when(repository.findByNombre("Laptop")).thenReturn(Optional.of(productoData));
        when(productoMapper.toProducto(productoData)).thenReturn(producto);

        Producto resultado = gateway.buscarProductoPorNombre("Laptop");

        assertEquals(producto, resultado);
    }

    @Test
    void buscarProductoPorNombre_debeLanzarExcepcionSiNoExiste() {
        when(repository.findByNombre("Laptop")).thenReturn(Optional.empty());

        ProductoNoEncontradoException error = assertThrows(
                ProductoNoEncontradoException.class,
                () -> gateway.buscarProductoPorNombre("Laptop")
        );

        assertEquals("Producto no encontrado con nombre: Laptop", error.getMessage());
    }

    @Test
    void buscarProductoPorDescripcion_debeRetornarProductoSiExiste() {
        ProductoData productoData = crearProductoData();
        Producto producto = crearProducto();
        when(repository.findByDescripcion("Portatil")).thenReturn(Optional.of(productoData));
        when(productoMapper.toProducto(productoData)).thenReturn(producto);

        Producto resultado = gateway.buscarProductoPorDescripcion("Portatil");

        assertEquals(producto, resultado);
    }

    @Test
    void buscarProductoPorDescripcion_debeLanzarExcepcionSiNoExiste() {
        when(repository.findByDescripcion("Portatil")).thenReturn(Optional.empty());

        ProductoNoEncontradoException error = assertThrows(
                ProductoNoEncontradoException.class,
                () -> gateway.buscarProductoPorDescripcion("Portatil")
        );

        assertEquals("Producto no encontrado con descripcion: Portatil", error.getMessage());
    }

    @Test
    void buscarProductoPorPrecio_debeRetornarProductoSiExiste() {
        ProductoData productoData = crearProductoData();
        Producto producto = crearProducto();
        when(repository.findByPrecio(1000.0)).thenReturn(Optional.of(productoData));
        when(productoMapper.toProducto(productoData)).thenReturn(producto);

        Producto resultado = gateway.buscarProductoPorPrecio(1000.0);

        assertEquals(producto, resultado);
    }

    @Test
    void buscarProductoPorPrecio_debeLanzarExcepcionSiNoExiste() {
        when(repository.findByPrecio(1000.0)).thenReturn(Optional.empty());

        ProductoNoEncontradoException error = assertThrows(
                ProductoNoEncontradoException.class,
                () -> gateway.buscarProductoPorPrecio(1000.0)
        );

        assertEquals("Producto no encontrado con precio: 1000.0", error.getMessage());
    }

    @Test
    void buscarProductoPorStock_debeRetornarProductoSiExiste() {
        ProductoData productoData = crearProductoData();
        Producto producto = crearProducto();
        when(repository.findByStock(5)).thenReturn(Optional.of(productoData));
        when(productoMapper.toProducto(productoData)).thenReturn(producto);

        Producto resultado = gateway.buscarProductoPorStock(5);

        assertEquals(producto, resultado);
    }

    @Test
    void buscarProductoPorStock_debeLanzarExcepcionSiNoExiste() {
        when(repository.findByStock(5)).thenReturn(Optional.empty());

        ProductoNoEncontradoException error = assertThrows(
                ProductoNoEncontradoException.class,
                () -> gateway.buscarProductoPorStock(5)
        );

        assertEquals("Producto no encontrado con stock: 5", error.getMessage());
    }

    @Test
    void actualizarProducto_debeGuardarSiExiste() {
        Producto producto = crearProducto();
        ProductoData productoData = crearProductoData();
        when(repository.existsById(1L)).thenReturn(true);
        when(productoMapper.toProductoData(producto)).thenReturn(productoData);
        when(repository.save(productoData)).thenReturn(productoData);
        when(productoMapper.toProducto(productoData)).thenReturn(producto);

        Producto resultado = gateway.actualizarProducto(producto);

        assertEquals(producto, resultado);
    }

    @Test
    void actualizarProducto_debeLanzarExcepcionSiNoExiste() {
        Producto producto = crearProducto();
        when(repository.existsById(1L)).thenReturn(false);

        ProductoNoEncontradoException error = assertThrows(
                ProductoNoEncontradoException.class,
                () -> gateway.actualizarProducto(producto)
        );

        assertEquals("Producto no encontrado con id: 1", error.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void eliminarProductoPorId_debeEliminarSiExiste() {
        when(repository.existsById(1L)).thenReturn(true);

        gateway.eliminarProductoPorId(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminarProductoPorId_debeLanzarExcepcionSiNoExiste() {
        when(repository.existsById(1L)).thenReturn(false);

        ProductoNoEncontradoException error = assertThrows(
                ProductoNoEncontradoException.class,
                () -> gateway.eliminarProductoPorId(1L)
        );

        assertEquals("Producto no encontrado con id: 1", error.getMessage());
        verify(repository, never()).deleteById(any());
    }

    private Producto crearProducto() {
        return new Producto(1L, "Laptop", "Portatil", 1000.0, 5);
    }

    private ProductoData crearProductoData() {
        return new ProductoData(1L, "Laptop", "Portatil", 1000.0, 5);
    }
}
