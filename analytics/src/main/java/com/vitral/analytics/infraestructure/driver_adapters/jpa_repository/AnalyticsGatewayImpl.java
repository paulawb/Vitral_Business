package com.vitral.analytics.infraestructure.driver_adapters.jpa_repository;

import com.vitral.analytics.domain.model.AnalyticsEvent;
import com.vitral.analytics.domain.model.gateway.AnalyticsGateway;
import com.vitral.analytics.infraestructure.mapper.AnalyticsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AnalyticsGatewayImpl implements AnalyticsGateway {

    private final AnalyticsEventDataJpaRepository repository;
    private final AnalyticsMapper mapper;

    @Override
    public AnalyticsEvent save(AnalyticsEvent event) {
        return mapper.toEvent(repository.save(mapper.toData(event)));
    }

    @Override
    public List<AnalyticsEvent> findByTenant(String tenantId) {
        return repository.findAllByTenantId(tenantId)
                .stream()
                .map(mapper::toEvent)
                .collect(Collectors.toList());
    }
}
