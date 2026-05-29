package com.vitral.business.infraestructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessDataJpaRepository extends JpaRepository<BusinessData, String> {
    Optional<BusinessData> findBySlug(String slug);
    Optional<BusinessData> findByTenantId(String tenantId);
    void deleteBySlug(String slug);
}
