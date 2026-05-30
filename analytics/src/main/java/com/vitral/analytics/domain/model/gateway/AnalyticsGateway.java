package com.vitral.analytics.domain.model.gateway;

import com.vitral.analytics.domain.model.AnalyticsEvent;

import java.util.List;

public interface AnalyticsGateway {
    AnalyticsEvent save(AnalyticsEvent event);
    List<AnalyticsEvent> findByTenant(String tenantId);
}
