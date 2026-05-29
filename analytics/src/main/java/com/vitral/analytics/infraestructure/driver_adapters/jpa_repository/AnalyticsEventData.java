package com.vitral.analytics.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "analytics_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsEventData {
    @Id
    @Column(nullable = false, updatable = false, length = 36)
    private String eventId;

    @Column(nullable = false, length = 36)
    private String tenantId;

    @Column(nullable = false, length = 50)
    private String eventType;

    @Column(length = 36)
    private String resourceId;

    @Column(length = 150)
    private String resourceName;

    @Column(length = 30)
    private String channel;

    @Column(nullable = false, updatable = false)
    private LocalDateTime occurredAt;

    @PrePersist
    void onCreate() {
        if (eventId == null || eventId.isBlank()) {
            eventId = UUID.randomUUID().toString();
        }
        if (occurredAt == null) {
            occurredAt = LocalDateTime.now();
        }
    }
}
