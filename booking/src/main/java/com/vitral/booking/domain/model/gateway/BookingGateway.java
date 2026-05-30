package com.vitral.booking.domain.model.gateway;

import com.vitral.booking.domain.model.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingGateway {
    Booking save(Booking booking);
    Booking update(Booking booking);
    Booking findById(String bookingId);
    List<Booking> findByTenantAndDate(String tenantId, LocalDate bookingDate);
    List<Booking> findByTenant(String tenantId);
}
