package com.EFSRTIII.ventacursos.dto;

import com.EFSRTIII.ventacursos.models.Compra;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraDTO {

    private Integer idCompra;
    private Integer idUsuario;
    private String nombreUsuario;
    private Integer idCurso;
    private String tituloCurso;
    private String descripcionCurso;
    private BigDecimal precioPagado;
    private LocalDateTime fechaCompra;
    private String metodoPago;
    private String estado;

    private String emailUsuario;

    // Constructor desde entidad
    public CompraDTO(Compra compra) {
        this.idCompra = compra.getIdCompra();
        this.idUsuario = compra.getUsuario().getIdUsuario();
        this.nombreUsuario = compra.getUsuario().getNombre();
        this.idCurso = compra.getCurso().getIdCurso();
        this.tituloCurso = compra.getCurso().getTitulo();
        this.descripcionCurso = compra.getCurso().getDescripcion();
        this.precioPagado = compra.getPrecioPagado();
        this.fechaCompra = compra.getFechaCompra();
        this.metodoPago = compra.getMetodoPago();
        this.estado = Optional.ofNullable(compra.getEstado())
                .map(Enum::name).orElse(null);
    }
}