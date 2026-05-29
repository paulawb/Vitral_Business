package com.vitral.analytics.domain.usecase;

import com.vitral.analytics.domain.model.AnalyticsEvent;
import com.vitral.analytics.domain.model.DashboardSummary;
import com.vitral.analytics.domain.model.gateway.AnalyticsGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AnalyticsUseCase {

    private final AnalyticsGateway analyticsGateway;

    public AnalyticsEvent track(AnalyticsEvent event) {
        event.setEventId(event.getEventId() == null ? UUID.randomUUID().toString() : event.getEventId());
        event.setOccurredAt(event.getOccurredAt() == null ? LocalDateTime.now() : event.getOccurredAt());
        return analyticsGateway.save(event);
    }

    public DashboardSummary dashboard(String tenantId) {
        List<AnalyticsEvent> events = analyticsGateway.findByTenant(tenantId);
        long visits = events.stream().filter(e -> "VISIT".equalsIgnoreCase(e.getEventType())).count();
        long conversions = events.stream().filter(e -> "CONVERSION".equalsIgnoreCase(e.getEventType())).count();
        long whatsappClicks = events.stream().filter(e -> "WHATSAPP_CLICK".equalsIgnoreCase(e.getEventType())).count();

        Map<String, Long> topProductsMap = events.stream()
                .filter(e -> "PRODUCT_VIEW".equalsIgnoreCase(e.getEventType()))
                .collect(Collectors.groupingBy(AnalyticsEvent::getResourceName, Collectors.counting()));
        List<Map<String, Object>> topProducts = topProductsMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", entry.getKey());
                    item.put("views", entry.getValue());
                    return item;
                })
                .collect(Collectors.toList());

        Map<Integer, Long> peakHourMap = events.stream()
                .collect(Collectors.groupingBy(e -> e.getOccurredAt().getHour(), Collectors.counting()));
        List<Map<String, Object>> peakHours = peakHourMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("hour", entry.getKey());
                    item.put("events", entry.getValue());
                    return item;
                })
                .collect(Collectors.toList());

        return DashboardSummary.builder()
                .visits(visits)
                .conversions(conversions)
                .whatsappClicks(whatsappClicks)
                .topProducts(topProducts)
                .peakHours(peakHours)
                .build();
    }
}
