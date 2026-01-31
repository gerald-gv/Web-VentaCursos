package com.EFSRTIII.ventacursos.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import java.time.LocalDateTime;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_usuario")
    private Integer idUsuario;

    private String nombre;
    private String email;
    @Column(name="contraseña")
    private String contrasenia;

    private boolean activo;
    @Column(name="fecha_creacion", insertable = true, updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToMany
    @JoinTable(
            name="usuario_rol",
            joinColumns=@JoinColumn(name="id_usuario"),
            inverseJoinColumns=@JoinColumn(name="id_rol")
    )
    private Set<Rol> roles = new HashSet<>();

    public Usuario(){ }
    public Usuario(String nombre, String email, String contrasenia, boolean activo, Set<Rol> roles) {
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
        this.activo = activo;
        this.roles = roles;
    }
    public Usuario(String nombre, String email, String contrasenia, boolean activo, LocalDateTime fechaCreacion) {
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }
    public Usuario(Integer idUsuario, String nombre, String email, String contrasenia, boolean activo, LocalDateTime fechaCreacion) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
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

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
}
