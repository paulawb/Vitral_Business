package com.ecommerce.crud.domain.usecase;

import com.ecommerce.crud.domain.model.Producto;
import com.ecommerce.crud.domain.model.gateway.Productogateway;
import com.ecommerce.crud.infraestructure.exception.ValidacionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoUseCaseTest {

    private static final String NOMBRE = "Producto Test";
    private static final String DESCRIPCION = "Descripcion del producto";
    private static final String TENANT_ID = "tenant-123";
    private static final Long PRODUCTO_ID = 1L;

    @Mock
    private Productogateway productogateway;

    @InjectMocks
    private ProductoUseCase productoUseCase;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .nombre(NOMBRE)
                .descripcion(DESCRIPCION)
                .tenantId(TENANT_ID)
                .precio(100.0)
                .stock(10)
                .build();
    }

    @Test
    void guardarProducto_debeGuardarProductoConValoresPorDefecto() {
        when(productogateway.guardarProducto(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = productoUseCase.guardarProducto(producto);

        assertNotNull(resultado.getUuid());
        assertEquals(NOMBRE.toLowerCase().replace(" ", "-"), resultado.getSlug());
        assertEquals(TENANT_ID, resultado.getTenantId());
        assertTrue(resultado.getDisponible());
        assertFalse(resultado.getDestacado());
        assertEquals(0D, resultado.getRating());
        assertEquals(0L, resultado.getVecesAgendado());
        assertEquals(0L, resultado.getVecesVisto());
        assertNotNull(resultado.getFechaActualizacion());
        verify(productogateway).guardarProducto(any(Producto.class));
    }

    @Test
    void guardarProducto_debeFallarSiNombreEsNull() {
        producto.setNombre(null);

        assertThrows(
                ValidacionException.class,
                () -> productoUseCase.guardarProducto(producto)
        );
        verify(productogateway, never()).guardarProducto(any());
    }

    @Test
    void guardarProducto_debeFallarSiDescripcionEsNull() {
        producto.setDescripcion(null);

        assertThrows(
                ValidacionException.class,
                () -> productoUseCase.guardarProducto(producto)
        );
        verify(productogateway, never()).guardarProducto(any());
    }

    @Test
    void guardarProducto_debeUsarUuidExistente() {
        producto.setUuid("uuid-existente");
        when(productogateway.guardarProducto(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = productoUseCase.guardarProducto(producto);

        assertEquals("uuid-existente", resultado.getUuid());
    }

    @Test
    void guardarProducto_debeUsarSlugExistente() {
        producto.setSlug("slug-personalizado");
        when(productogateway.guardarProducto(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = productoUseCase.guardarProducto(producto);

        assertEquals("slug-personalizado", resultado.getSlug());
    }

    @Test
    void guardarProducto_debeUsarTenantIdExistente() {
        producto.setTenantId("otro-tenant");
        when(productogateway.guardarProducto(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = productoUseCase.guardarProducto(producto);

        assertEquals("otro-tenant", resultado.getTenantId());
    }

    @Test
    void buscarProductoPorId_debeRetornarProducto() {
        when(productogateway.buscarProductoPorId(PRODUCTO_ID)).thenReturn(producto);

        Producto resultado = productoUseCase.buscarProductoPorId(PRODUCTO_ID);

        assertNotNull(resultado);
        verify(productogateway).buscarProductoPorId(PRODUCTO_ID);
    }

    @Test
    void buscarProductoPorNombre_debeRetornarProducto() {
        when(productogateway.buscarProductoPorNombre(NOMBRE)).thenReturn(producto);

        Producto resultado = productoUseCase.buscarProductoPorNombre(NOMBRE);

        assertNotNull(resultado);
        verify(productogateway).buscarProductoPorNombre(NOMBRE);
    }

    @Test
    void buscarProductoPorDescripcion_debeRetornarProducto() {
        when(productogateway.buscarProductoPorDescripcion(DESCRIPCION)).thenReturn(producto);

        Producto resultado = productoUseCase.buscarProductoPorDescripcion(DESCRIPCION);

        assertNotNull(resultado);
        verify(productogateway).buscarProductoPorDescripcion(DESCRIPCION);
    }

    @Test
    void buscarProductoPorPrecio_debeRetornarProducto() {
        when(productogateway.buscarProductoPorPrecio(100.0)).thenReturn(producto);

        Producto resultado = productoUseCase.buscarProductoPorPrecio(100.0);

        assertNotNull(resultado);
        verify(productogateway).buscarProductoPorPrecio(100.0);
    }

    @Test
    void buscarProductoPorStock_debeRetornarProducto() {
        when(productogateway.buscarProductoPorStock(10)).thenReturn(producto);

        Producto resultado = productoUseCase.buscarProductoPorStock(10);

        assertNotNull(resultado);
        verify(productogateway).buscarProductoPorStock(10);
    }

    @Test
    void buscarProductoPorSlug_debeRetornarProducto() {
        when(productogateway.buscarProductoPorSlug("producto-test")).thenReturn(producto);

        Producto resultado = productoUseCase.buscarProductoPorSlug("producto-test");

        assertNotNull(resultado);
        verify(productogateway).buscarProductoPorSlug("producto-test");
    }

    @Test
    void listarPorTenant_debeRetornarLista() {
        when(productogateway.listarPorTenant(TENANT_ID)).thenReturn(List.of(producto));

        List<Producto> resultado = productoUseCase.listarPorTenant(TENANT_ID);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(productogateway).listarPorTenant(TENANT_ID);
    }

    @Test
    void actualizarProducto_debeActualizarProducto() {
        producto.setProductoId(PRODUCTO_ID);
        when(productogateway.actualizarProducto(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = productoUseCase.actualizarProducto(producto);

        assertNotNull(resultado);
        assertNotNull(resultado.getFechaActualizacion());
        verify(productogateway).actualizarProducto(any(Producto.class));
    }

    @Test
    void actualizarProducto_debeFallarSiIdEsNull() {
        producto.setProductoId(null);

        assertThrows(
                ValidacionException.class,
                () -> productoUseCase.actualizarProducto(producto)
        );
        verify(productogateway, never()).actualizarProducto(any());
    }

    @Test
    void eliminarProductoPorId_debeEliminarProducto() {
        assertDoesNotThrow(() -> productoUseCase.eliminarProductoPorId(PRODUCTO_ID));

        verify(productogateway).eliminarProductoPorId(PRODUCTO_ID);
    }
}
