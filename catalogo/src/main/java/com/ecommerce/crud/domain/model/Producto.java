package com.ecommerce.crud.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    private Long productoId;
    private String uuid;
    private String tenantId;
    private String slug;
    private String nombre;
    private String descripcion;
    private double precio;
    private Double precioDescuento;
    private Integer stock;
    private String categoria;
    private String subCategoria;
    private String thumbnail;
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

    public Producto(Long productoId, String nombre, String descripcion, double precio, Integer stock) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }
}
