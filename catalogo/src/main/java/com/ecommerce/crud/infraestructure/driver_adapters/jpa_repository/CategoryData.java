package com.ecommerce.crud.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryData {
    @Id
    private String slug;
    private String tenantId;
    private String name;
    private String parentSlug;
}
