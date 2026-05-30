package com.vitral.analytics.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class DashboardSummary {
    private long visits;
    private long conversions;
    private long whatsappClicks;
    private List<Map<String, Object>> topProducts;
    private List<Map<String, Object>> peakHours;
}
