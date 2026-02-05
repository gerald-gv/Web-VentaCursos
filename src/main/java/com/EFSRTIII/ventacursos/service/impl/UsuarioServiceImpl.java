package com.EFSRTIII.ventacursos.service.impl;

import com.EFSRTIII.ventacursos.models.Rol;
import com.EFSRTIII.ventacursos.models.Usuario;
import com.EFSRTIII.ventacursos.repositories.RolRepository;
import com.EFSRTIII.ventacursos.repositories.UsuarioRepository;
import com.EFSRTIII.ventacursos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }



    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepo.findAll();
    }

    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepo.existsByEmail(usuario.getEmail()))
            throw new RuntimeException("El correo ya está registrado");

        usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));

        return usuarioRepo.save(usuario);
    }

    @Override
    public void eliminarUsuario(Integer id) {
        usuarioRepo.deleteById(id);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepo.findByEmail(email);
    }
}
