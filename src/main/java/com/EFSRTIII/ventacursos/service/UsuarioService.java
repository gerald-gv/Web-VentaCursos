package com.EFSRTIII.ventacursos.service;

import com.EFSRTIII.ventacursos.models.Usuario;

import java.util.Optional;

public interface UsuarioService {

    public Usuario registrarUsuario(Usuario usuario);

    public Optional<Usuario> buscarPorEmail(String email);

    public Usuario actualizarUsuario(Usuario usuario, String nuevoNombre, String nuevoEmail, String nuevaContrasenia);
}
