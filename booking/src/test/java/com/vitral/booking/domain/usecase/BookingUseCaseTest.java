package com.vitral.booking.domain.usecase;

import com.vitral.booking.domain.model.AvailabilitySlot;
import com.vitral.booking.domain.model.Booking;
import com.vitral.booking.domain.model.gateway.BookingGateway;
import com.vitral.booking.domain.model.gateway.SoftLockGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingUseCaseTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String BOOKING_ID = "booking-123";

    @Mock
    private BookingGateway bookingGateway;

    @Mock
    private SoftLockGateway softLockGateway;

    @InjectMocks
    private BookingUseCase bookingUseCase;

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = Booking.builder()
                .bookingId(BOOKING_ID)
                .tenantId(TENANT_ID)
                .bookingDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .status("PENDING")
                .build();
    }

    @Test
    void create_debeCrearBookingConValoresPorDefecto() {
        booking.setBookingId(null);
        when(bookingGateway.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking resultado = bookingUseCase.create(booking);

        assertNotNull(resultado.getBookingId());
        assertEquals("PENDING", resultado.getStatus());
        assertNotNull(resultado.getCreatedAt());
        verify(bookingGateway).save(any(Booking.class));
    }

    @Test
    void create_debeFallarSiHorarioInvalido() {
        booking.setStartTime(null);
        booking.setEndTime(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> bookingUseCase.create(booking)
        );
        verify(bookingGateway, never()).save(any());
    }

    @Test
    void create_debeFallarSiStartTimeNoEsAntesQueEndTime() {
        booking.setStartTime(LocalTime.of(11, 0));
        booking.setEndTime(LocalTime.of(10, 0));

        assertThrows(
                IllegalArgumentException.class,
                () -> bookingUseCase.create(booking)
        );
    }

    @Test
    void create_debeFallarSiHorarioAntesDe8am() {
        booking.setStartTime(LocalTime.of(7, 0));
        booking.setEndTime(LocalTime.of(8, 0));

        assertThrows(
                IllegalArgumentException.class,
                () -> bookingUseCase.create(booking)
        );
    }

    @Test
    void create_debeFallarSiHorarioDespuesDe8pm() {
        booking.setStartTime(LocalTime.of(19, 0));
        booking.setEndTime(LocalTime.of(21, 0));

        assertThrows(
                IllegalArgumentException.class,
                () -> bookingUseCase.create(booking)
        );
    }

    @Test
    void create_debeFallarSiSlotYaReservado() {
        Booking existingBooking = Booking.builder()
                .bookingId("existing")
                .tenantId(TENANT_ID)
                .bookingDate(booking.getBookingDate())
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(11, 0))
                .status("CONFIRMED")
                .build();

        when(bookingGateway.findByTenantAndDate(TENANT_ID, booking.getBookingDate()))
                .thenReturn(List.of(existingBooking));

        assertThrows(
                IllegalStateException.class,
                () -> bookingUseCase.create(booking)
        );
    }

    @Test
    void create_debeFallarSiSlotBloqueadoTemporalmente() {
        when(bookingGateway.findByTenantAndDate(TENANT_ID, booking.getBookingDate()))
                .thenReturn(List.of());
        when(softLockGateway.isHeld(TENANT_ID, booking.getBookingDate(), booking.getStartTime(), booking.getEndTime()))
                .thenReturn(true);

        assertThrows(
                IllegalStateException.class,
                () -> bookingUseCase.create(booking)
        );
    }

    @Test
    void update_debeActualizarBooking() {
        when(bookingGateway.update(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking resultado = bookingUseCase.update(BOOKING_ID, booking);

        assertEquals(BOOKING_ID, resultado.getBookingId());
        verify(bookingGateway).update(any(Booking.class));
    }

    @Test
    void cancel_debeCancelarBooking() {
        when(bookingGateway.findById(BOOKING_ID)).thenReturn(booking);
        when(bookingGateway.update(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking resultado = bookingUseCase.cancel(BOOKING_ID);

        assertEquals("CANCELLED", resultado.getStatus());
        verify(bookingGateway).update(any(Booking.class));
    }

    @Test
    void availability_debeRetornarSlotsDisponibles() {
        when(bookingGateway.findByTenantAndDate(TENANT_ID, booking.getBookingDate()))
                .thenReturn(List.of());
        when(softLockGateway.isHeld(anyString(), any(), any(), any()))
                .thenReturn(false);

        List<AvailabilitySlot> resultado = bookingUseCase.availability(TENANT_ID, booking.getBookingDate());

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
    }

    @Test
    void availability_debeRetornarSlotsNoDisponiblesParaReservas() {
        Booking existingBooking = Booking.builder()
                .tenantId(TENANT_ID)
                .bookingDate(booking.getBookingDate())
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(8, 30))
                .status("CONFIRMED")
                .build();

        when(bookingGateway.findByTenantAndDate(TENANT_ID, booking.getBookingDate()))
                .thenReturn(List.of(existingBooking));
        when(softLockGateway.isHeld(anyString(), any(), any(), any()))
                .thenReturn(false);

        List<AvailabilitySlot> resultado = bookingUseCase.availability(TENANT_ID, booking.getBookingDate());

        assertNotNull(resultado);
        assertTrue(resultado.stream().anyMatch(slot -> !slot.isAvailable()));
    }

    @Test
    void availability_debeRetornarSlotsNoDisponiblesParaSoftLock() {
        when(bookingGateway.findByTenantAndDate(TENANT_ID, booking.getBookingDate()))
                .thenReturn(List.of());
        when(softLockGateway.isHeld(TENANT_ID, booking.getBookingDate(), LocalTime.of(8, 0), LocalTime.of(8, 30)))
                .thenReturn(true);

        List<AvailabilitySlot> resultado = bookingUseCase.availability(TENANT_ID, booking.getBookingDate());

        assertNotNull(resultado);
        assertTrue(resultado.stream().anyMatch(slot -> !slot.isAvailable()));
    }

    @Test
    void hold_debeCrearSoftLock() {
        when(bookingGateway.findByTenantAndDate(TENANT_ID, booking.getBookingDate()))
                .thenReturn(List.of());
        when(softLockGateway.isHeld(anyString(), any(), any(), any()))
                .thenReturn(false);

        assertDoesNotThrow(() -> bookingUseCase.hold(TENANT_ID, booking.getBookingDate(), booking.getStartTime(), booking.getEndTime()));

        verify(softLockGateway).hold(TENANT_ID, booking.getBookingDate(), booking.getStartTime(), booking.getEndTime());
    }

    @Test
    void hold_debeFallarSiHorarioInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> bookingUseCase.hold(TENANT_ID, booking.getBookingDate(), null, null)
        );
    }

    @Test
    void hold_debeFallarSiSlotYaReservado() {
        Booking existingBooking = Booking.builder()
                .tenantId(TENANT_ID)
                .bookingDate(booking.getBookingDate())
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(8, 30))
                .status("CONFIRMED")
                .build();

        when(bookingGateway.findByTenantAndDate(TENANT_ID, booking.getBookingDate()))
                .thenReturn(List.of(existingBooking));

        assertThrows(
                IllegalStateException.class,
                () -> bookingUseCase.hold(TENANT_ID, booking.getBookingDate(), LocalTime.of(8, 0), LocalTime.of(8, 30))
        );
    }

    @Test
    void hold_debeFallarSiSlotBloqueado() {
        when(bookingGateway.findByTenantAndDate(TENANT_ID, booking.getBookingDate()))
                .thenReturn(List.of());
        when(softLockGateway.isHeld(TENANT_ID, booking.getBookingDate(), booking.getStartTime(), booking.getEndTime()))
                .thenReturn(true);

        assertThrows(
                IllegalStateException.class,
                () -> bookingUseCase.hold(TENANT_ID, booking.getBookingDate(), booking.getStartTime(), booking.getEndTime())
        );
    }

    @Test
    void findByTenant_debeRetornarListaDeBookings() {
        when(bookingGateway.findByTenant(TENANT_ID)).thenReturn(List.of(booking));

        List<Booking> resultado = bookingUseCase.findByTenant(TENANT_ID);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }
}