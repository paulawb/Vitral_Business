package com.vitral.business.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "businesses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessData {
    @Id
    @Column(nullable = false, updatable = false, length = 36)
    private String tenantId;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    private String logoUrl;
    private String primaryColor;
    private String secondaryColor;

    @Column(length = 50)
    private String businessType;

    @Column(length = 50)
    private String vertical;

    @Column(length = 20)
    private String phone;

    @Column(length = 120)
    private String email;

    @Column(length = 20)
    private String whatsappNumber;

    private String instagramUrl;
    private String facebookUrl;
    private String tiktokUrl;

    @Column(length = 50)
    private String timezone;

    private Boolean active;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Builder.Default
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleData> schedules = new ArrayList<>();

    @PrePersist
    void onCreate() {
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = UUID.randomUUID().toString();
        }
        if (slug == null || slug.isBlank()) {
            slug = tenantId;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (active == null) {
            active = true;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
