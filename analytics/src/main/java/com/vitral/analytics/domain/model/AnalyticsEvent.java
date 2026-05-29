package com.vitral.analytics.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsEvent {
    private String eventId;
    private String tenantId;
    private String eventType;
    private String resourceId;
    private String resourceName;
    private String channel;
    private LocalDateTime occurredAt;
}
