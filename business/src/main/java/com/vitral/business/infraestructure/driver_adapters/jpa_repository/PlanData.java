package com.vitral.business.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanData {
    @Id
    private String code;
    private String name;
    private Integer maxProducts;
    private Integer maxBookingsPerMonth;
    private Boolean whatsappEnabled;
    private Boolean analyticsEnabled;
}
