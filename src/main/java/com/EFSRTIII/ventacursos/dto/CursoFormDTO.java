package com.EFSRTIII.ventacursos.dto;

import com.EFSRTIII.ventacursos.models.Curso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoFormDTO {

    private Integer idCurso;
    private String titulo;
    private String descripcion;
    private Double precio;
    private String instructor;
    private String duracion;
    private String objetivos;
    private String requisitos;
    private Boolean activo = true;
    private List<ModuloDTO> modulos = new ArrayList<>();

    public void aplicarACurso(Curso curso) {
        curso.setTitulo(this.titulo);
        curso.setDescripcion(this.descripcion);
        curso.setPrecio(BigDecimal.valueOf(this.precio));
        curso.setInstructor(this.instructor);
        curso.setDuracion(this.duracion);
        curso.setObjetivos(this.objetivos);
        curso.setRequisitos(this.requisitos);
        curso.setActivo(this.activo);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModuloDTO {
        private Integer idContenido;
        private String titulo;
        private String descripcion;
        private String url;
        private Integer orden;
    }
    public CursoFormDTO(Curso curso) {
        this.idCurso = curso.getIdCurso();
        this.titulo = curso.getTitulo();
        this.descripcion = curso.getDescripcion();
        this.precio = curso.getPrecio().doubleValue();
        this.instructor = curso.getInstructor();
        this.duracion = curso.getDuracion();
        this.objetivos = curso.getObjetivos();
        this.requisitos = curso.getRequisitos();
        this.activo = curso.getActivo();
    }
}
