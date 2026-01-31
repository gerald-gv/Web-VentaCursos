package com.EFSRTIII.ventacursos.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="roles")
public class Rol {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_rol")
    private Integer idRol;

    @Column(name="nombre_rol")
    private String nombreRol;

    private String descripcion;

    @ManyToMany(mappedBy="roles")
    private Set<Usuario> usuarios = new HashSet<>();

    public Rol(){ }
    public Rol(String nombreRol, String descripcion) {
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
    }
    public Rol(Integer idRol, String nombreRol, String descripcion) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rol)) return false;
        Rol rol = (Rol) o;
        return Objects.equals(idRol, rol.idRol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRol);
    }



    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
