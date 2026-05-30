package com.ecommerce.crud.application.config;

import com.ecommerce.crud.domain.model.gateway.Productogateway;
import com.ecommerce.crud.domain.usecase.ProductoUseCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class UseCaseConfigTest {

    @Test
    void productoUseCase_debeCrearBean() {
        UseCaseConfig config = new UseCaseConfig();
        Productogateway gateway = mock(Productogateway.class);

        ProductoUseCase useCase = config.productoUseCase(gateway);

        assertNotNull(useCase);
    }
}
