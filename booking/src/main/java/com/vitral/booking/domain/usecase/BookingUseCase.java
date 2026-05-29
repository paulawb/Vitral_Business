package com.vitral.booking.domain.usecase;

import com.vitral.booking.domain.model.AvailabilitySlot;
import com.vitral.booking.domain.model.Booking;
import com.vitral.booking.domain.model.gateway.BookingGateway;
import com.vitral.booking.domain.model.gateway.SoftLockGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class BookingUseCase {

    private final BookingGateway bookingGateway;
    private final SoftLockGateway softLockGateway;

    public Booking create(Booking booking) {
        validarHorario(booking.getStartTime(), booking.getEndTime());
        validarDisponibilidad(booking.getTenantId(), booking.getBookingDate(), booking.getStartTime(), booking.getEndTime());
        booking.setBookingId(booking.getBookingId() == null ? UUID.randomUUID().toString() : booking.getBookingId());
        booking.setStatus(booking.getStatus() == null ? "PENDING" : booking.getStatus());
        booking.setCreatedAt(LocalDateTime.now());
        return bookingGateway.save(booking);
    }

    public Booking update(String bookingId, Booking booking) {
        validarHorario(booking.getStartTime(), booking.getEndTime());
        booking.setBookingId(bookingId);
        return bookingGateway.update(booking);
    }

    public Booking cancel(String bookingId) {
        Booking booking = bookingGateway.findById(bookingId);
        booking.setStatus("CANCELLED");
        return bookingGateway.update(booking);
    }

    public List<AvailabilitySlot> availability(String tenantId, LocalDate date) {
        List<Booking> bookings = bookingGateway.findByTenantAndDate(tenantId, date);
        List<AvailabilitySlot> slots = new ArrayList<>();
        LocalTime current = LocalTime.of(8, 0);
        while (current.isBefore(LocalTime.of(20, 0))) {
            LocalTime end = current.plusMinutes(30);
            boolean available = true;
            for (Booking booking : bookings) {
                if (solapa(current, end, booking.getStartTime(), booking.getEndTime()) && !"CANCELLED".equalsIgnoreCase(booking.getStatus())) {
                    available = false;
                    break;
                }
            }
            if (softLockGateway.isHeld(tenantId, date, current, end)) {
                available = false;
            }
            slots.add(new AvailabilitySlot(current.toString(), end.toString(), available));
            current = end;
        }
        return slots;
    }

    public void hold(String tenantId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        validarHorario(startTime, endTime);
        validarDisponibilidad(tenantId, date, startTime, endTime);
        softLockGateway.hold(tenantId, date, startTime, endTime);
    }

    public List<Booking> findByTenant(String tenantId) {
        return bookingGateway.findByTenant(tenantId);
    }

    private void validarHorario(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null || !startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Rango horario invalido");
        }
        if (startTime.isBefore(LocalTime.of(8, 0)) || endTime.isAfter(LocalTime.of(20, 0))) {
            throw new IllegalArgumentException("El horario esta fuera de la jornada permitida");
        }
    }

    private void validarDisponibilidad(String tenantId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        for (Booking booking : bookingGateway.findByTenantAndDate(tenantId, date)) {
            if (!"CANCELLED".equalsIgnoreCase(booking.getStatus()) && solapa(startTime, endTime, booking.getStartTime(), booking.getEndTime())) {
                throw new IllegalStateException("El slot ya esta reservado");
            }
        }
        if (softLockGateway.isHeld(tenantId, date, startTime, endTime)) {
            throw new IllegalStateException("El slot esta bloqueado temporalmente");
        }
    }

    private boolean solapa(LocalTime startA, LocalTime endA, LocalTime startB, LocalTime endB) {
        return startA.isBefore(endB) && endA.isAfter(startB);
    }
}
