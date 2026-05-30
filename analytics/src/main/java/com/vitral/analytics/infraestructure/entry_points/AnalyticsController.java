package com.vitral.analytics.infraestructure.entry_points;

import com.vitral.analytics.domain.model.AnalyticsEvent;
import com.vitral.analytics.domain.model.DashboardSummary;
import com.vitral.analytics.domain.usecase.AnalyticsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsUseCase analyticsUseCase;

    @PostMapping("/events")
    public ResponseEntity<AnalyticsEvent> track(@RequestBody AnalyticsEvent event) {
        return ResponseEntity.ok(analyticsUseCase.track(event));
    }

    @GetMapping("/dashboard/{tenantId}")
    public ResponseEntity<DashboardSummary> dashboard(@PathVariable String tenantId) {
        return ResponseEntity.ok(analyticsUseCase.dashboard(tenantId));
    }
}
