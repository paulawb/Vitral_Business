package com.vitral.business.infraestructure.driver_adapters.jpa_repository;

import com.vitral.business.domain.model.Business;
import com.vitral.business.domain.model.gateway.BusinessGateway;
import com.vitral.business.infraestructure.exception.BusinessNotFoundException;
import com.vitral.business.infraestructure.mapper.BusinessMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BusinessDataGatewayImpl implements BusinessGateway {

    private final BusinessDataJpaRepository repository;
    private final BusinessMapper mapper;

    @Override
    public Business save(Business business) {
        return mapper.toBusiness(repository.save(mapper.toData(business)));
    }

    @Override
    public Business update(Business business) {
        BusinessData existing = repository.findBySlug(business.getSlug())
                .orElseThrow(() -> new BusinessNotFoundException("Negocio no encontrado: " + business.getSlug()));
        BusinessData payload = mapper.toData(business);
        payload.setTenantId(existing.getTenantId());
        return mapper.toBusiness(repository.save(payload));
    }

    @Override
    public Business findBySlug(String slug) {
        return repository.findBySlug(slug)
                .map(mapper::toBusiness)
                .orElseThrow(() -> new BusinessNotFoundException("Negocio no encontrado: " + slug));
    }

    @Override
    public Business findByTenantId(String tenantId) {
        return repository.findByTenantId(tenantId)
                .map(mapper::toBusiness)
                .orElseThrow(() -> new BusinessNotFoundException("Negocio no encontrado para tenant: " + tenantId));
    }

    @Override
    public void deleteBySlug(String slug) {
        repository.findBySlug(slug)
                .orElseThrow(() -> new BusinessNotFoundException("Negocio no encontrado: " + slug));
        repository.deleteBySlug(slug);
    }
}
