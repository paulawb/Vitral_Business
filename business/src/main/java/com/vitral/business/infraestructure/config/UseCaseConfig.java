package com.vitral.business.infraestructure.config;

import com.vitral.business.domain.model.gateway.BusinessGateway;
import com.vitral.business.domain.usecase.BusinessUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
    @Bean
    public BusinessUseCase businessUseCase(BusinessGateway businessGateway) {
        return new BusinessUseCase(businessGateway);
    }
}