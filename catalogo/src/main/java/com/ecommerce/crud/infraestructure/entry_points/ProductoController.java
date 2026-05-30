package com.ecommerce.crud.infraestructure.entry_points;

import com.ecommerce.crud.domain.model.Producto;
import com.ecommerce.crud.domain.usecase.ProductoUseCase;
import com.ecommerce.crud.infraestructure.driver_adapters.jpa_repository.ProductoData;
import com.ecommerce.crud.infraestructure.mapper.ProductoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoUseCase productoUseCase;
    private final ProductoMapper productoMapper;

    @PostMapping("/guardar")
    public ResponseEntity<Producto> guardarProducto(@Valid @RequestBody ProductoData productoData) {
        Producto productoValidadoGuardado = productoUseCase.guardarProducto(productoMapper.toProducto(productoData));
        return new ResponseEntity<>(productoValidadoGuardado, HttpStatus.OK);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<Producto> actualizarProducto(@Valid @RequestBody ProductoData productoData) {
        Producto productoActualizado = productoUseCase.actualizarProducto(productoMapper.toProducto(productoData));
        return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
    }

    @GetMapping("/buscar/id/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        Producto producto = productoUseCase.buscarProductoPorId(id);
        if (producto == null || producto.getProductoId() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(producto, HttpStatus.OK);
    }

    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<Producto> obtenerProductoPorNombre(@PathVariable String nombre) {
        Producto producto = productoUseCase.buscarProductoPorNombre(nombre);
        if (producto == null || producto.getProductoId() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(producto, HttpStatus.OK);
    }

    @GetMapping("/buscar/descripcion/{descripcion}")
    public ResponseEntity<Producto> obtenerProductoPorDescripcion(@PathVariable String descripcion) {
        Producto producto = productoUseCase.buscarProductoPorDescripcion(descripcion);
        if (producto == null || producto.getProductoId() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(producto, HttpStatus.OK);
    }

    @GetMapping("/buscar/precio/{precio}")
    public ResponseEntity<Producto> obtenerProductoPorPrecio(@PathVariable Double precio) {
        Producto producto = productoUseCase.buscarProductoPorPrecio(precio);
        if (producto == null || producto.getProductoId() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(producto, HttpStatus.OK);
    }

    @GetMapping("/buscar/stock/{stock}")
    public ResponseEntity<Producto> obtenerProductoPorStock(@PathVariable Integer stock) {
        Producto producto = productoUseCase.buscarProductoPorStock(stock);
        if (producto == null || producto.getProductoId() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(producto, HttpStatus.OK);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Producto> obtenerProductoPorSlug(@PathVariable String slug) {
        return new ResponseEntity<>(productoUseCase.buscarProductoPorSlug(slug), HttpStatus.OK);
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<Producto>> listarPorTenant(@PathVariable String tenantId) {
        return new ResponseEntity<>(productoUseCase.listarPorTenant(tenantId), HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Long id) {
        Producto producto = productoUseCase.buscarProductoPorId(id);
        if (producto == null || producto.getProductoId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }
        productoUseCase.eliminarProductoPorId(id);
        return ResponseEntity.ok("Producto eliminado correctamente");
    }
}
