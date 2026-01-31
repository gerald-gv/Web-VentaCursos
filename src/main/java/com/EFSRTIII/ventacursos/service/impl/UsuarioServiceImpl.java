package com.EFSRTIII.ventacursos.service.impl;

import com.EFSRTIII.ventacursos.models.Usuario;
import com.EFSRTIII.ventacursos.repositories.UsuarioRepository;
import com.EFSRTIII.ventacursos.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    UsuarioRepository usuarioRepo;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepo.findAll();
    }

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        Usuario usuario1 = new Usuario(usuario.getNombre(), usuario.getEmail(), usuario.getContrasenia(), usuario.isActivo(), usuario.getRoles());
        return usuarioRepo.save(usuario1);
    }

    @Override
    public void eliminarUsuario(Integer id) {
        usuarioRepo.deleteById(id);
    }

}
