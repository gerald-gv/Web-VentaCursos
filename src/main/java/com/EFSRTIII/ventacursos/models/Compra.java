package com.EFSRTIII.ventacursos.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "compras",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_usuario_curso",
                columnNames = {"id_usuario", "id_curso"}
        ))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Integer idCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false,
            foreignKey = @ForeignKey(name = "fk_compra_usuario"))
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", nullable = false,
            foreignKey = @ForeignKey(name = "fk_compra_curso"))
    private Curso curso;

    @Column(name = "precio_pagado", nullable = false,
            precision = 38, scale = 2)
    private BigDecimal precioPagado;

    @Column(name = "fecha_compra", nullable = false,
            updatable = false)
    private LocalDateTime fechaCompra;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private EstadoCompra estado;

    @PrePersist
    protected void onCreate() {
        if (fechaCompra == null) {
            fechaCompra = LocalDateTime.now();
        }
        if (estado == null) {
            estado = EstadoCompra.COMPLETADA;
        }
    }
    // Enum para el estado
    public enum EstadoCompra {
        COMPLETADA,
        PENDIENTE,
        CANCELADA
    }
}