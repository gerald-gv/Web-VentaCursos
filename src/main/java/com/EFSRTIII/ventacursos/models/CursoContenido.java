package com.EFSRTIII.ventacursos.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "curso_contenido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoContenido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contenido")
    private Integer idContenido;

    @Column(name = "id_curso", nullable = false)
    private Integer idCurso;

    @Builder.Default
    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo = "video";

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "url", length = 500)
    private String url;

    @Builder.Default
    @Column(name = "orden", nullable = false)
    private Integer orden = 1;

    @Builder.Default
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Builder.Default
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}