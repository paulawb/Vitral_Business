package com.vitral.booking.infraestructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingDataJpaRepository extends JpaRepository<BookingData, String> {
    List<BookingData> findAllByTenantId(String tenantId);
    List<BookingData> findAllByTenantIdAndBookingDate(String tenantId, LocalDate bookingDate);
}
