package com.EFSRTIII.ventacursos.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="cursos")
public class Curso {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_curso")
    private Integer idCurso;

    @Getter
    private String titulo;
    @Getter
    private String  descripcion;
    @Getter
    private BigDecimal precio;
    @Getter
    @Setter
    private boolean activo;
    @Getter
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

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Compra> compras = new ArrayList<>();

    // Método helper
    public void agregarCompra(Compra compra) {
        compras.add(compra);
        compra.setCurso(this);
    }

}
