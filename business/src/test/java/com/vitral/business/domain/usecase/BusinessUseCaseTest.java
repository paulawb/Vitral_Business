package com.vitral.business.domain.usecase;

import com.vitral.business.domain.model.Business;
import com.vitral.business.domain.model.gateway.BusinessGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessUseCaseTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String SLUG = "mi-negocio";
    private static final String NOMBRE = "Mi Negocio";

    @Mock
    private BusinessGateway businessGateway;

    @InjectMocks
    private BusinessUseCase businessUseCase;

    private Business business;

    @BeforeEach
    void setUp() {
        business = Business.builder()
                .tenantId(TENANT_ID)
                .slug(SLUG)
                .nombre(NOMBRE)
                .active(true)
                .build();
    }

    @Test
    void save_debeGenerarTenantIdSiEsNuloOVacio() {
        business.setTenantId(null);
        when(businessGateway.save(any(Business.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Business resultado = businessUseCase.save(business);

        assertNotNull(resultado.getTenantId());
        verify(businessGateway).save(any(Business.class));
    }

    @Test
    void save_debeGenerarSlugDesdeNombreSiSlugEsNulo() {
        business.setSlug(null);
        when(businessGateway.save(any(Business.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Business resultado = businessUseCase.save(business);

        assertEquals(NOMBRE.toLowerCase().replace(" ", "-"), resultado.getSlug());
        verify(businessGateway).save(any(Business.class));
    }

    @Test
    void save_debeEstablecerActiveTrueSiEsNulo() {
        business.setActive(null);
        when(businessGateway.save(any(Business.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Business resultado = businessUseCase.save(business);

        assertTrue(resultado.getActive());
        verify(businessGateway).save(any(Business.class));
    }

    @Test
    void save_debeEstablecerTimezonePorDefecto() {
        business.setTimezone(null);
        when(businessGateway.save(any(Business.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Business resultado = businessUseCase.save(business);

        assertEquals("America/Bogota", resultado.getTimezone());
        verify(businessGateway).save(any(Business.class));
    }

    @Test
    void update_debeSlugificarElSlug() {
        business.setSlug("Mi Slug Con Espacios");
        when(businessGateway.update(any(Business.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Business resultado = businessUseCase.update(business);

        assertEquals("mi-slug-con-espacios", resultado.getSlug());
        verify(businessGateway).update(any(Business.class));
    }

    @Test
    void update_debeGenerarSlugDesdeNombreSiSlugEsNulo() {
        business.setSlug(null);
        when(businessGateway.update(any(Business.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Business resultado = businessUseCase.update(business);

        assertEquals(NOMBRE.toLowerCase().replace(" ", "-"), resultado.getSlug());
        verify(businessGateway).update(any(Business.class));
    }

    @Test
    void findBySlug_debeRetornarBusiness() {
        when(businessGateway.findBySlug(SLUG)).thenReturn(business);

        Business resultado = businessUseCase.findBySlug(SLUG);

        assertNotNull(resultado);
        assertEquals(SLUG, resultado.getSlug());
        verify(businessGateway).findBySlug(SLUG);
    }

    @Test
    void findByTenantId_debeRetornarBusiness() {
        when(businessGateway.findByTenantId(TENANT_ID)).thenReturn(business);

        Business resultado = businessUseCase.findByTenantId(TENANT_ID);

        assertNotNull(resultado);
        assertEquals(TENANT_ID, resultado.getTenantId());
        verify(businessGateway).findByTenantId(TENANT_ID);
    }

    @Test
    void deleteBySlug_debeEliminarBusiness() {
        doNothing().when(businessGateway).deleteBySlug(SLUG);

        assertDoesNotThrow(() -> businessUseCase.deleteBySlug(SLUG));

        verify(businessGateway).deleteBySlug(SLUG);
    }
}