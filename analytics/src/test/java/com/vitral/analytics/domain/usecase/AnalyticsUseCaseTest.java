package com.vitral.analytics.domain.usecase;

import com.vitral.analytics.domain.model.AnalyticsEvent;
import com.vitral.analytics.domain.model.DashboardSummary;
import com.vitral.analytics.domain.model.gateway.AnalyticsGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsUseCaseTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String EVENT_ID = UUID.randomUUID().toString();

    @Mock
    private AnalyticsGateway analyticsGateway;

    @InjectMocks
    private AnalyticsUseCase analyticsUseCase;

    private AnalyticsEvent event;

    @BeforeEach
    void setUp() {
        event = AnalyticsEvent.builder()
                .eventId(EVENT_ID)
                .tenantId(TENANT_ID)
                .eventType("VISIT")
                .resourceName("home")
                .occurredAt(LocalDateTime.now())
                .build();
    }

    @Test
    void track_debeGenerarEventIdSiEsNulo() {
        event.setEventId(null);
        when(analyticsGateway.save(any(AnalyticsEvent.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AnalyticsEvent resultado = analyticsUseCase.track(event);

        assertNotNull(resultado.getEventId());
        verify(analyticsGateway).save(any(AnalyticsEvent.class));
    }

    @Test
    void track_debeGenerarOccurredAtSiEsNulo() {
        event.setOccurredAt(null);
        when(analyticsGateway.save(any(AnalyticsEvent.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AnalyticsEvent resultado = analyticsUseCase.track(event);

        assertNotNull(resultado.getOccurredAt());
        verify(analyticsGateway).save(any(AnalyticsEvent.class));
    }

    @Test
    void track_debeUsarEventIdExistente() {
        when(analyticsGateway.save(any(AnalyticsEvent.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AnalyticsEvent resultado = analyticsUseCase.track(event);

        assertEquals(EVENT_ID, resultado.getEventId());
    }

    @Test
    void dashboard_debeRetornarResumenConVisitas() {
        AnalyticsEvent visitEvent = AnalyticsEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tenantId(TENANT_ID)
                .eventType("VISIT")
                .occurredAt(LocalDateTime.now())
                .build();

        when(analyticsGateway.findByTenant(TENANT_ID)).thenReturn(List.of(visitEvent));

        DashboardSummary resultado = analyticsUseCase.dashboard(TENANT_ID);

        assertNotNull(resultado);
        assertEquals(1, resultado.getVisits());
        assertEquals(0, resultado.getConversions());
        assertEquals(0, resultado.getWhatsappClicks());
    }

    @Test
    void dashboard_debeRetornarResumenConConversiones() {
        AnalyticsEvent conversionEvent = AnalyticsEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tenantId(TENANT_ID)
                .eventType("CONVERSION")
                .occurredAt(LocalDateTime.now())
                .build();

        when(analyticsGateway.findByTenant(TENANT_ID)).thenReturn(List.of(conversionEvent));

        DashboardSummary resultado = analyticsUseCase.dashboard(TENANT_ID);

        assertNotNull(resultado);
        assertEquals(0, resultado.getVisits());
        assertEquals(1, resultado.getConversions());
    }

    @Test
    void dashboard_debeRetornarResumenConWhatsappClicks() {
        AnalyticsEvent whatsappEvent = AnalyticsEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tenantId(TENANT_ID)
                .eventType("WHATSAPP_CLICK")
                .occurredAt(LocalDateTime.now())
                .build();

        when(analyticsGateway.findByTenant(TENANT_ID)).thenReturn(List.of(whatsappEvent));

        DashboardSummary resultado = analyticsUseCase.dashboard(TENANT_ID);

        assertNotNull(resultado);
        assertEquals(1, resultado.getWhatsappClicks());
    }

    @Test
    void dashboard_debeRetornarResumenConTopProducts() {
        AnalyticsEvent productView1 = AnalyticsEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tenantId(TENANT_ID)
                .eventType("PRODUCT_VIEW")
                .resourceName("Producto A")
                .occurredAt(LocalDateTime.now())
                .build();

        AnalyticsEvent productView2 = AnalyticsEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tenantId(TENANT_ID)
                .eventType("PRODUCT_VIEW")
                .resourceName("Producto A")
                .occurredAt(LocalDateTime.now())
                .build();

        when(analyticsGateway.findByTenant(TENANT_ID)).thenReturn(List.of(productView1, productView2));

        DashboardSummary resultado = analyticsUseCase.dashboard(TENANT_ID);

        assertNotNull(resultado);
        assertNotNull(resultado.getTopProducts());
        assertEquals(1, resultado.getTopProducts().size());
    }

    @Test
    void dashboard_debeRetornarResumenConPeakHours() {
        AnalyticsEvent event1 = AnalyticsEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .tenantId(TENANT_ID)
                .eventType("VISIT")
                .occurredAt(LocalDateTime.of(2024, 1, 1, 10, 0))
                .build();

        when(analyticsGateway.findByTenant(TENANT_ID)).thenReturn(List.of(event1));

        DashboardSummary resultado = analyticsUseCase.dashboard(TENANT_ID);

        assertNotNull(resultado);
        assertNotNull(resultado.getPeakHours());
    }

    @Test
    void dashboard_debeRetornarResumenVacioConListaVacia() {
        when(analyticsGateway.findByTenant(TENANT_ID)).thenReturn(List.of());

        DashboardSummary resultado = analyticsUseCase.dashboard(TENANT_ID);

        assertNotNull(resultado);
        assertEquals(0, resultado.getVisits());
        assertEquals(0, resultado.getConversions());
        assertEquals(0, resultado.getWhatsappClicks());
    }
}