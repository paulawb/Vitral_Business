package com.vitral.analytics.infraestructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalyticsEventDataJpaRepository extends JpaRepository<AnalyticsEventData, String> {
    List<AnalyticsEventData> findAllByTenantId(String tenantId);
}
