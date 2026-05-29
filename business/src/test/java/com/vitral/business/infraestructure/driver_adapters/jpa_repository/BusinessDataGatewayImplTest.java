package com.vitral.business.infraestructure.driver_adapters.jpa_repository;

import com.vitral.business.domain.model.Business;
import com.vitral.business.infraestructure.exception.BusinessNotFoundException;
import com.vitral.business.infraestructure.mapper.BusinessMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessDataGatewayImplTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String SLUG = "mi-negocio";

    @Mock
    private BusinessDataJpaRepository repository;

    @Mock
    private BusinessMapper mapper;

    @InjectMocks
    private BusinessDataGatewayImpl gateway;

    private Business business;
    private BusinessData businessData;

    @BeforeEach
    void setUp() {
        business = Business.builder()
                .tenantId(TENANT_ID)
                .slug(SLUG)
                .nombre("Mi Negocio")
                .active(true)
                .build();

        businessData = BusinessData.builder()
                .tenantId(TENANT_ID)
                .slug(SLUG)
                .nombre("Mi Negocio")
                .active(true)
                .build();
    }

    @Test
    void save_debeGuardarBusiness() {
        when(mapper.toData(business)).thenReturn(businessData);
        when(repository.save(businessData)).thenReturn(businessData);
        when(mapper.toBusiness(businessData)).thenReturn(business);

        Business resultado = gateway.save(business);

        assertNotNull(resultado);
        assertEquals(TENANT_ID, resultado.getTenantId());
        verify(repository).save(businessData);
    }

    @Test
    void update_debeActualizarBusiness() {
        when(mapper.toData(business)).thenReturn(businessData);
        when(repository.findBySlug(SLUG)).thenReturn(Optional.of(businessData));
        when(repository.save(any(BusinessData.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toBusiness(any(BusinessData.class))).thenAnswer(invocation -> {
            BusinessData data = invocation.getArgument(0);
            return Business.builder()
                    .tenantId(data.getTenantId())
                    .slug(data.getSlug())
                    .nombre(data.getNombre())
                    .active(data.getActive())
                    .build();
        });

        Business resultado = gateway.update(business);

        assertNotNull(resultado);
        assertEquals(TENANT_ID, resultado.getTenantId());
    }

    @Test
    void update_debeFallarSiBusinessNoExiste() {
        when(repository.findBySlug(SLUG)).thenReturn(Optional.empty());

        assertThrows(
                BusinessNotFoundException.class,
                () -> gateway.update(business)
        );
    }

    @Test
    void findBySlug_debeRetornarBusiness() {
        when(repository.findBySlug(SLUG)).thenReturn(Optional.of(businessData));
        when(mapper.toBusiness(businessData)).thenReturn(business);

        Business resultado = gateway.findBySlug(SLUG);

        assertNotNull(resultado);
        assertEquals(SLUG, resultado.getSlug());
    }

    @Test
    void findBySlug_debeFallarSiNoExiste() {
        when(repository.findBySlug(SLUG)).thenReturn(Optional.empty());

        assertThrows(
                BusinessNotFoundException.class,
                () -> gateway.findBySlug(SLUG)
        );
    }

    @Test
    void findByTenantId_debeRetornarBusiness() {
        when(repository.findByTenantId(TENANT_ID)).thenReturn(Optional.of(businessData));
        when(mapper.toBusiness(businessData)).thenReturn(business);

        Business resultado = gateway.findByTenantId(TENANT_ID);

        assertNotNull(resultado);
        assertEquals(TENANT_ID, resultado.getTenantId());
    }

    @Test
    void findByTenantId_debeFallarSiNoExiste() {
        when(repository.findByTenantId(TENANT_ID)).thenReturn(Optional.empty());

        assertThrows(
                BusinessNotFoundException.class,
                () -> gateway.findByTenantId(TENANT_ID)
        );
    }

    @Test
    void deleteBySlug_debeEliminarBusiness() {
        when(repository.findBySlug(SLUG)).thenReturn(Optional.of(businessData));

        assertDoesNotThrow(() -> gateway.deleteBySlug(SLUG));

        verify(repository).deleteBySlug(SLUG);
    }

    @Test
    void deleteBySlug_debeFallarSiNoExiste() {
        when(repository.findBySlug(SLUG)).thenReturn(Optional.empty());

        assertThrows(
                BusinessNotFoundException.class,
                () -> gateway.deleteBySlug(SLUG)
        );
    }
}