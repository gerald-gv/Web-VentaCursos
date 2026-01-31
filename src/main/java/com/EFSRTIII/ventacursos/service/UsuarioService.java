package com.EFSRTIII.ventacursos.service;

import com.EFSRTIII.ventacursos.models.Usuario;

import java.util.List;

public interface UsuarioService {
    public List<Usuario> listarUsuarios();

    public Usuario guardarUsuario(Usuario usuario);

    public void eliminarUsuario(Integer id);

}
