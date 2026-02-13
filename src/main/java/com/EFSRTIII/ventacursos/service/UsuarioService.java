package com.EFSRTIII.ventacursos.service;

import com.EFSRTIII.ventacursos.models.Usuario;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    public List<Usuario> listarUsuarios();

    public Usuario registrarUsuario(Usuario usuario);

    public void eliminarUsuario(Integer id);

    public Optional<Usuario> buscarPorEmail(String email);

    public Usuario actualizarUsuario(Usuario usuario, String nuevoNombre, String nuevoEmail, String nuevaContrasenia);
}
