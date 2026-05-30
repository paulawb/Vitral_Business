package com.vitral.analytics.infraestructure.mapper;

import com.vitral.analytics.domain.model.AnalyticsEvent;
import com.vitral.analytics.infraestructure.driver_adapters.jpa_repository.AnalyticsEventData;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsMapper {

    public AnalyticsEvent toEvent(AnalyticsEventData data) {
        return AnalyticsEvent.builder()
                .eventId(data.getEventId())
                .tenantId(data.getTenantId())
                .eventType(data.getEventType())
                .resourceId(data.getResourceId())
                .resourceName(data.getResourceName())
                .channel(data.getChannel())
                .occurredAt(data.getOccurredAt())
                .build();
    }

    public AnalyticsEventData toData(AnalyticsEvent event) {
        return AnalyticsEventData.builder()
                .eventId(event.getEventId())
                .tenantId(event.getTenantId())
                .eventType(event.getEventType())
                .resourceId(event.getResourceId())
                .resourceName(event.getResourceName())
                .channel(event.getChannel())
                .occurredAt(event.getOccurredAt())
                .build();
    }
}
