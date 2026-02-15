package com.EFSRTIII.ventacursos.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioFormDTO {

    private Integer idUsuario;
    private String nombre;
    private String email;
    private String contrasenia;

    @Builder.Default
    private Boolean activo = true;

    @Builder.Default
    private List<String> roles = new ArrayList<>();
}