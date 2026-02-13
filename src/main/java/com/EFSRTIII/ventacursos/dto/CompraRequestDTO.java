package com.EFSRTIII.ventacursos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraRequestDTO {

    private Integer idUsuario;

    private Integer idCurso;

    private String metodoPago;
}