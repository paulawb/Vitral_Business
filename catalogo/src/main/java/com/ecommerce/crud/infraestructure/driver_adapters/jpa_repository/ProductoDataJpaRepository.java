package com.ecommerce.crud.infraestructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoDataJpaRepository extends JpaRepository<ProductoData, Long> {

    Optional<ProductoData> findByNombre(String nombre);

    Optional<ProductoData> findByDescripcion(String descripcion);

    Optional<ProductoData> findByPrecio(Double precio);

    Optional<ProductoData> findByStock(Integer stock);

    Optional<ProductoData> findBySlug(String slug);

    List<ProductoData> findAllByTenantId(String tenantId);
}
