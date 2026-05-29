package com.vitral.business.domain.model.gateway;

import com.vitral.business.domain.model.Business;

public interface BusinessGateway {
    Business save(Business business);

    Business update(Business business);

    Business findBySlug(String slug);

    Business findByTenantId(String tenantId);

    void deleteBySlug(String slug);
}
