package com.vitral.business.domain.usecase;

import com.vitral.business.domain.model.Business;
import com.vitral.business.domain.model.gateway.BusinessGateway;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class BusinessUseCase {

    private final BusinessGateway businessGateway;

    public Business save(Business business) {
        business.setTenantId(vacio(business.getTenantId()) ? UUID.randomUUID().toString() : business.getTenantId());
        business.setSlug(slugify(business.getSlug() == null ? business.getNombre() : business.getSlug()));
        business.setActive(business.getActive() == null ? Boolean.TRUE : business.getActive());
        business.setTimezone(vacio(business.getTimezone()) ? "America/Bogota" : business.getTimezone());
        return businessGateway.save(business);
    }

    public Business update(Business business) {
        business.setSlug(slugify(business.getSlug() == null ? business.getNombre() : business.getSlug()));
        return businessGateway.update(business);
    }

    public Business findBySlug(String slug) {
        return businessGateway.findBySlug(slug);
    }

    public Business findByTenantId(String tenantId) {
        return businessGateway.findByTenantId(tenantId);
    }

    public void deleteBySlug(String slug) {
        businessGateway.deleteBySlug(slug);
    }

    private String slugify(String value) {
        return value == null ? null : value.trim().toLowerCase().replace(" ", "-");
    }

    private boolean vacio(String value) {
        return value == null || value.trim().isEmpty();
    }
}
