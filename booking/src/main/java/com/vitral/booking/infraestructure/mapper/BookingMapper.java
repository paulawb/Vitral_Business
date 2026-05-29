package com.vitral.booking.infraestructure.mapper;

import com.vitral.booking.domain.model.Booking;
import com.vitral.booking.infraestructure.driver_adapters.jpa_repository.BookingData;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public Booking toBooking(BookingData data) {
        return Booking.builder()
                .bookingId(data.getBookingId())
                .tenantId(data.getTenantId())
                .businessSlug(data.getBusinessSlug())
                .productId(data.getProductId())
                .serviceName(data.getServiceName())
                .customerName(data.getCustomerName())
                .customerEmail(data.getCustomerEmail())
                .customerPhone(data.getCustomerPhone())
                .bookingDate(data.getBookingDate())
                .startTime(data.getStartTime())
                .endTime(data.getEndTime())
                .status(data.getStatus())
                .notes(data.getNotes())
                .createdAt(data.getCreatedAt())
                .build();
    }

    public BookingData toData(Booking booking) {
        return BookingData.builder()
                .bookingId(booking.getBookingId())
                .tenantId(booking.getTenantId())
                .businessSlug(booking.getBusinessSlug())
                .productId(booking.getProductId())
                .serviceName(booking.getServiceName())
                .customerName(booking.getCustomerName())
                .customerEmail(booking.getCustomerEmail())
                .customerPhone(booking.getCustomerPhone())
                .bookingDate(booking.getBookingDate())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .status(booking.getStatus())
                .notes(booking.getNotes())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
