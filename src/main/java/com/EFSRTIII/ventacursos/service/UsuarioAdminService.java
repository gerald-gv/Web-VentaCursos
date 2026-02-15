package com.EFSRTIII.ventacursos.service;

import com.EFSRTIII.ventacursos.dto.UsuarioFormDTO;
import com.EFSRTIII.ventacursos.models.Rol;
import com.EFSRTIII.ventacursos.models.Usuario;
import com.EFSRTIII.ventacursos.repositories.RolRepository;
import com.EFSRTIII.ventacursos.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioAdminService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    // =========================
    // Obtener usuarios
    // =========================

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public UsuarioFormDTO obtenerUsuarioParaEditar(Integer idUsuario) {
        Usuario usuario = obtenerUsuario(idUsuario);

        return UsuarioFormDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .activo(usuario.isActivo())
                .roles(usuario.getRoles()
                        .stream()
                        .map(Rol::getNombreRol)
                        .toList())
                .build();
    }

    // Guardar usuario


    @Transactional
    public Usuario guardarUsuario(UsuarioFormDTO dto) {

        Usuario usuario = (dto.getIdUsuario() != null)
                ? obtenerUsuario(dto.getIdUsuario())
                : crearUsuario(dto);

        mapearDatosBasicos(usuario, dto);
        actualizarContrasenia(usuario, dto.getContrasenia());
        actualizarRolesSiExisten(usuario, dto.getRoles());

        return usuarioRepository.save(usuario);
    }

    private Usuario crearUsuario(UsuarioFormDTO dto) {

        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        return new Usuario();
    }

    private void mapearDatosBasicos(Usuario usuario, UsuarioFormDTO dto) {
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setActivo(dto.getActivo());
    }

    private void actualizarContrasenia(Usuario usuario, String contrasenia) {
        if (contrasenia != null && !contrasenia.isBlank()) {
            usuario.setContrasenia(passwordEncoder.encode(contrasenia));
        }
    }

    private void actualizarRolesSiExisten(Usuario usuario, List<String> roles) {
        if (roles == null || roles.isEmpty()) return;

        Set<Rol> rolesEntity = roles.stream()
                .map(nombre -> rolRepository.findByNombreRol(nombre)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombre)))
                .collect(Collectors.toSet());

        usuario.setRoles(rolesEntity);
    }

    // Estado

    @Transactional
    public void cambiarEstado(Integer idUsuario, Boolean nuevoEstado) {
        Usuario usuario = obtenerUsuario(idUsuario);
        usuario.setActivo(nuevoEstado);
        usuarioRepository.save(usuario);
    }

    // =========================
    // Eliminaciones
    // =========================

    @Transactional
    public void eliminarUsuario(Integer idUsuario) {
        cambiarEstado(idUsuario, false);
    }

    @Transactional
    public void eliminarUsuarioPermanentemente(Integer idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }

    // Utilidades

    public long contarUsuariosActivos() {
        return usuarioRepository.countByActivoTrue();
    }

    public boolean tieneRol(Usuario usuario, String nombreRol) {
        return usuario.getRoles().stream()
                .anyMatch(r -> r.getNombreRol().equals(nombreRol));
    }

    private Usuario obtenerUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}

