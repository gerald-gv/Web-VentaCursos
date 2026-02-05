package com.EFSRTIII.ventacursos.dto;

public class RegisterDTO {
    private String nombre;
    private String email;
    private String contrasenia;
    private String confirmarContrasenia;

    public RegisterDTO(){}
    public RegisterDTO(String nombre, String email, String contrasenia, String confirmarContrasenia) {
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
        this.confirmarContrasenia = confirmarContrasenia;
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

    public String getConfirmarContrasenia() {
        return confirmarContrasenia;
    }

    public void setConfirmarContrasenia(String confirmarContrasenia) {
        this.confirmarContrasenia = confirmarContrasenia;
    }
}
