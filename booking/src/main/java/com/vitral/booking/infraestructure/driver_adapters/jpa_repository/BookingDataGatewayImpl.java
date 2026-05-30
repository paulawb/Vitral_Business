package com.vitral.booking.infraestructure.driver_adapters.jpa_repository;

import com.vitral.booking.domain.model.Booking;
import com.vitral.booking.domain.model.gateway.BookingGateway;
import com.vitral.booking.infraestructure.exception.BookingNotFoundException;
import com.vitral.booking.infraestructure.mapper.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookingDataGatewayImpl implements BookingGateway {

    private final BookingDataJpaRepository repository;
    private final BookingMapper mapper;

    @Override
    public Booking save(Booking booking) {
        return mapper.toBooking(repository.save(mapper.toData(booking)));
    }

    @Override
    public Booking update(Booking booking) {
        repository.findById(booking.getBookingId())
                .orElseThrow(() -> new BookingNotFoundException("Reserva no encontrada: " + booking.getBookingId()));
        return mapper.toBooking(repository.save(mapper.toData(booking)));
    }

    @Override
    public Booking findById(String bookingId) {
        return repository.findById(bookingId)
                .map(mapper::toBooking)
                .orElseThrow(() -> new BookingNotFoundException("Reserva no encontrada: " + bookingId));
    }

    @Override
    public List<Booking> findByTenantAndDate(String tenantId, LocalDate bookingDate) {
        return repository.findAllByTenantIdAndBookingDate(tenantId, bookingDate)
                .stream()
                .map(mapper::toBooking)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByTenant(String tenantId) {
        return repository.findAllByTenantId(tenantId)
                .stream()
                .map(mapper::toBooking)
                .collect(Collectors.toList());
    }
}
