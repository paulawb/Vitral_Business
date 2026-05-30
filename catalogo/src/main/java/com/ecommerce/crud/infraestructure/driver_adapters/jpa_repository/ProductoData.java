package com.ecommerce.crud.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "productos")
@Data
public class ProductoData {

    @Id
    @Column(name = "producto_id")
    private Long productoId;

    @Column(length = 36, nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(length = 100, nullable = false)
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @PositiveOrZero(message = "El precio debe ser un valor positivo o cero")
    private Double precio;
    private Double precioDescuento;
    private Integer stock;
    private String categoria;
    private String subCategoria;
    private String thumbnail;

    @Column(length = 600)
    private String tags;

    private Boolean disponible;
    private Boolean destacado;
    private String tipoProducto;
    private String vertical;
    private Integer duracion;
    private String marca;
    private String colores;
    private Double rating;
    private Long vecesVisto;
    private Long vecesAgendado;
    private LocalDateTime fechaActualizacion;
    private LocalDateTime deletedAt;

    public ProductoData(Long productoId, String nombre, String descripcion, Double precio, Integer stock) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    @PrePersist
    void onCreate() {
        if (uuid == null || uuid.trim().isEmpty()) {
            uuid = UUID.randomUUID().toString();
        }
        if (fechaActualizacion == null) {
            fechaActualizacion = LocalDateTime.now();
        }
    }

    @PreUpdate
    void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
