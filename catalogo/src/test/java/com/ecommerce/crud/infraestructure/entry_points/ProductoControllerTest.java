package com.ecommerce.crud.infraestructure.entry_points;

import com.ecommerce.crud.domain.model.Producto;
import com.ecommerce.crud.domain.usecase.ProductoUseCase;
import com.ecommerce.crud.infraestructure.driver_adapters.jpa_repository.ProductoData;
import com.ecommerce.crud.infraestructure.mapper.ProductoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoUseCase productoUseCase;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoController controller;

    @Test
    void guardarProducto_debeRetornarOk() {
        ProductoData productoData = crearProductoData();
        Producto producto = crearProducto();
        when(productoMapper.toProducto(productoData)).thenReturn(producto);
        when(productoUseCase.guardarProducto(producto)).thenReturn(producto);

        ResponseEntity<Producto> response = controller.guardarProducto(productoData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(producto, response.getBody());
    }

    @Test
    void actualizarProducto_debeRetornarOk() {
        ProductoData productoData = crearProductoData();
        Producto producto = crearProducto();
        when(productoMapper.toProducto(productoData)).thenReturn(producto);
        when(productoUseCase.actualizarProducto(producto)).thenReturn(producto);

        ResponseEntity<Producto> response = controller.actualizarProducto(productoData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(producto, response.getBody());
    }

    @Test
    void obtenerProductoPorId_debeRetornarNotFoundSiNoExiste() {
        when(productoUseCase.buscarProductoPorId(1L)).thenReturn(null);

        ResponseEntity<Producto> response = controller.obtenerProductoPorId(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void obtenerProductoPorId_debeRetornarNotFoundSiProductoNoTieneId() {
        Producto producto = crearProducto();
        producto.setProductoId(null);
        when(productoUseCase.buscarProductoPorId(1L)).thenReturn(producto);

        ResponseEntity<Producto> response = controller.obtenerProductoPorId(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void obtenerProductoPorId_debeRetornarOkSiExiste() {
        Producto producto = crearProducto();
        when(productoUseCase.buscarProductoPorId(1L)).thenReturn(producto);

        ResponseEntity<Producto> response = controller.obtenerProductoPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(producto, response.getBody());
    }

    @Test
    void obtenerProductoPorNombre_debeRetornarNotFoundSiNoExiste() {
        when(productoUseCase.buscarProductoPorNombre("Laptop")).thenReturn(null);

        ResponseEntity<Producto> response = controller.obtenerProductoPorNombre("Laptop");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void obtenerProductoPorNombre_debeRetornarNotFoundSiProductoNoTieneId() {
        Producto producto = crearProducto();
        producto.setProductoId(null);
        when(productoUseCase.buscarProductoPorNombre("Laptop")).thenReturn(producto);

        ResponseEntity<Producto> response = controller.obtenerProductoPorNombre("Laptop");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void obtenerProductoPorNombre_debeRetornarOkSiExiste() {
        Producto producto = crearProducto();
        when(productoUseCase.buscarProductoPorNombre("Laptop")).thenReturn(producto);

        ResponseEntity<Producto> response = controller.obtenerProductoPorNombre("Laptop");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(producto, response.getBody());
    }

    @Test
    void obtenerProductoPorDescripcion_debeRetornarNotFoundSiNoExiste() {
        when(productoUseCase.buscarProductoPorDescripcion("Portatil")).thenReturn(null);

        ResponseEntity<Producto> response = controller.obtenerProductoPorDescripcion("Portatil");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void obtenerProductoPorDescripcion_debeRetornarNotFoundSiProductoNoTieneId() {
        Producto producto = crearProducto();
        producto.setProductoId(null);
        when(productoUseCase.buscarProductoPorDescripcion("Portatil")).thenReturn(producto);

        ResponseEntity<Producto> response = controller.obtenerProductoPorDescripcion("Portatil");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void obtenerProductoPorDescripcion_debeRetornarOkSiExiste() {
        Producto producto = crearProducto();
        when(productoUseCase.buscarProductoPorDescripcion("Portatil")).thenReturn(producto);

        ResponseEntity<Producto> response = controller.obtenerProductoPorDescripcion("Portatil");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(producto, response.getBody());
    }

    @Test
    void obtenerProductoPorPrecio_debeRetornarNotFoundSiNoExiste() {
        when(productoUseCase.buscarProductoPorPrecio(1000.0)).thenReturn(null);

        ResponseEntity<Producto> response = controller.obtenerProductoPorPrecio(1000.0);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void obtenerProductoPorPrecio_debeRetornarNotFoundSiProductoNoTieneId() {
        Producto producto = crearProducto();
        producto.setProductoId(null);
        when(productoUseCase.buscarProductoPorPrecio(1000.0)).thenReturn(producto);

        ResponseEntity<Producto> response = controller.obtenerProductoPorPrecio(1000.0);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void obtenerProductoPorPrecio_debeRetornarOkSiExiste() {
        Producto producto = crearProducto();
        when(productoUseCase.buscarProductoPorPrecio(1000.0)).thenReturn(producto);

        ResponseEntity<Producto> response = controller.obtenerProductoPorPrecio(1000.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(producto, response.getBody());
    }

    @Test
    void obtenerProductoPorStock_debeRetornarNotFoundSiNoExiste() {
        when(productoUseCase.buscarProductoPorStock(5)).thenReturn(null);

        ResponseEntity<Producto> response = controller.obtenerProductoPorStock(5);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void obtenerProductoPorStock_debeRetornarNotFoundSiProductoNoTieneId() {
        Producto producto = crearProducto();
        producto.setProductoId(null);
        when(productoUseCase.buscarProductoPorStock(5)).thenReturn(producto);

        ResponseEntity<Producto> response = controller.obtenerProductoPorStock(5);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void obtenerProductoPorStock_debeRetornarOkSiExiste() {
        Producto producto = crearProducto();
        when(productoUseCase.buscarProductoPorStock(5)).thenReturn(producto);

        ResponseEntity<Producto> response = controller.obtenerProductoPorStock(5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(producto, response.getBody());
    }

    @Test
    void eliminarProducto_debeRetornarNotFoundSiNoExiste() {
        when(productoUseCase.buscarProductoPorId(1L)).thenReturn(null);

        ResponseEntity<String> response = controller.eliminarProducto(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Producto no encontrado", response.getBody());
        verify(productoUseCase, never()).eliminarProductoPorId(1L);
    }

    @Test
    void eliminarProducto_debeRetornarNotFoundSiProductoNoTieneId() {
        Producto producto = crearProducto();
        producto.setProductoId(null);
        when(productoUseCase.buscarProductoPorId(1L)).thenReturn(producto);

        ResponseEntity<String> response = controller.eliminarProducto(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Producto no encontrado", response.getBody());
        verify(productoUseCase, never()).eliminarProductoPorId(1L);
    }

    @Test
    void eliminarProducto_debeRetornarOkSiExiste() {
        Producto producto = crearProducto();
        when(productoUseCase.buscarProductoPorId(1L)).thenReturn(producto);

        ResponseEntity<String> response = controller.eliminarProducto(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Producto eliminado correctamente", response.getBody());
        verify(productoUseCase).eliminarProductoPorId(1L);
    }

    private Producto crearProducto() {
        return new Producto(1L, "Laptop", "Portatil", 1000.0, 5);
    }

    private ProductoData crearProductoData() {
        return new ProductoData(1L, "Laptop", "Portatil", 1000.0, 5);
    }
}
