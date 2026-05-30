package com.vitral.analytics.application.config;

import com.vitral.analytics.domain.model.gateway.AnalyticsGateway;
import com.vitral.analytics.domain.usecase.AnalyticsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public AnalyticsUseCase analyticsUseCase(AnalyticsGateway analyticsGateway) {
        return new AnalyticsUseCase(analyticsGateway);
    }
}
