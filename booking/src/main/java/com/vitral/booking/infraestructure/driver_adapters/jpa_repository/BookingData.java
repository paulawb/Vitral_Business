package com.vitral.booking.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingData {
    @Id
    @Column(nullable = false, updatable = false, length = 36)
    private String bookingId;

    @Column(nullable = false, length = 36)
    private String tenantId;

    @Column(length = 100)
    private String businessSlug;

    private Long productId;

    @Column(length = 150)
    private String serviceName;

    @Column(length = 100)
    private String customerName;

    @Column(length = 120)
    private String customerEmail;

    @Column(length = 20)
    private String customerPhone;

    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @Column(length = 30)
    private String status;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @PrePersist
    void onCreate() {
        if (bookingId == null || bookingId.isBlank()) {
            bookingId = UUID.randomUUID().toString();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (status == null || status.isBlank()) {
            status = "PENDING";
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
