package com.vitral.booking.infraestructure.entry_points;

import com.vitral.booking.domain.model.AvailabilitySlot;
import com.vitral.booking.domain.model.Booking;
import com.vitral.booking.domain.usecase.BookingUseCase;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingUseCase bookingUseCase;

    @PostMapping
    public ResponseEntity<Booking> create(@RequestBody Booking booking) {
        return ResponseEntity.ok(bookingUseCase.create(booking));
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<Booking> update(@PathVariable String bookingId, @RequestBody Booking booking) {
        return ResponseEntity.ok(bookingUseCase.update(bookingId, booking));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Booking> cancel(@PathVariable String bookingId) {
        return ResponseEntity.ok(bookingUseCase.cancel(bookingId));
    }

    @GetMapping("/availability")
    public ResponseEntity<List<AvailabilitySlot>> availability(@RequestParam String tenantId, @RequestParam String date) {
        return ResponseEntity.ok(bookingUseCase.availability(tenantId, LocalDate.parse(date)));
    }

    @PostMapping("/hold")
    public ResponseEntity<Void> hold(@RequestBody HoldRequest request) {
        bookingUseCase.hold(
                request.getTenantId(),
                LocalDate.parse(request.getDate()),
                LocalTime.parse(request.getStartTime()),
                LocalTime.parse(request.getEndTime())
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<Booking>> listByTenant(@PathVariable String tenantId) {
        return ResponseEntity.ok(bookingUseCase.findByTenant(tenantId));
    }

    @Data
    public static class HoldRequest {
        private String tenantId;
        private String date;
        private String startTime;
        private String endTime;
    }
}
