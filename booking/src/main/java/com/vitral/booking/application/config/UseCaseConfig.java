package com.vitral.booking.application.config;

import com.vitral.booking.domain.model.gateway.BookingGateway;
import com.vitral.booking.domain.model.gateway.SoftLockGateway;
import com.vitral.booking.domain.usecase.BookingUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public BookingUseCase bookingUseCase(BookingGateway bookingGateway, SoftLockGateway softLockGateway) {
        return new BookingUseCase(bookingGateway, softLockGateway);
    }
}
