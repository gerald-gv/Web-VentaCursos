package com.EFSRTIII.ventacursos.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_curso")
    private Integer idCurso;

    private String titulo;
    private String  descripcion;
    private BigDecimal precio;
    private boolean activo;
    @Column(name="fecha_creacion")
    @CreationTimestamp
    private LocalDateTime fechaCreacion = LocalDateTime.now();;
    
    public Curso(){ }
    public Curso(String titulo, String descripcion, BigDecimal precio, boolean activo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.activo = activo;
    }
    public Curso(String titulo, String descripcion, BigDecimal precio, boolean activo, LocalDateTime fechaCreacion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }
    public Curso(Integer idCurso, String titulo, String descripcion, BigDecimal precio, boolean activo, LocalDateTime fechaCreacion) {
        this.idCurso = idCurso;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
