package com.EFSRTIII.ventacursos.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="usuarios")
public class Usuario {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_usuario")
    private Integer idUsuario;

    @Setter
    @Getter
    private String nombre;
    @Setter
    @Getter
    @Column(unique = true, nullable = false)
    private String email;
    @Setter
    @Getter
    @Column(name="contraseña")
    private String contrasenia;

    @Getter
    @Setter
    private boolean activo;
    @Getter
    @Column(name="fecha_creacion", insertable = true, updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Getter
    @ManyToMany(fetch = FetchType.EAGER)
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

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Compra> compras = new ArrayList<>();

}
