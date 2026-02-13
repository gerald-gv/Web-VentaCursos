package com.EFSRTIII.ventacursos.service;

import com.EFSRTIII.ventacursos.dto.CompraDTO;
import com.EFSRTIII.ventacursos.dto.CompraRequestDTO;
import com.EFSRTIII.ventacursos.models.Compra;
import com.EFSRTIII.ventacursos.models.Curso;
import com.EFSRTIII.ventacursos.models.Usuario;
import com.EFSRTIII.ventacursos.repositories.CompraRepository;
import com.EFSRTIII.ventacursos.repositories.CursoRepository;
import com.EFSRTIII.ventacursos.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    public CompraDTO realizarCompra(CompraRequestDTO request) {
        // Verificar si ya compró el curso
        if (compraRepository.existsByUsuarioIdUsuarioAndCursoIdCurso(
                request.getIdUsuario(), request.getIdCurso())) {
            throw new RuntimeException("El usuario ya ha comprado este curso");
        }

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException(
                        "Usuario no encontrado con ID: " + request.getIdUsuario()));

        if (!usuario.isActivo()) {
            throw new RuntimeException("El usuario no está activo");
        }

        // Buscar curso
        Curso curso = cursoRepository.findById(request.getIdCurso())
                .orElseThrow(() -> new RuntimeException(
                        "Curso no encontrado con ID: " + request.getIdCurso()));

        if (!curso.isActivo()) {
            throw new RuntimeException("El curso no está disponible");
        }

        // Crear compra
        Compra compra = new Compra();
        compra.setUsuario(usuario);
        compra.setCurso(curso);
        compra.setPrecioPagado(curso.getPrecio());
        compra.setMetodoPago(request.getMetodoPago());
        compra.setEstado(Compra.EstadoCompra.COMPLETADA);

        return new CompraDTO(compraRepository.save(compra));
    }

    @Transactional
    public List<CompraDTO> obtenerCursosComprados(Integer idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }

        return compraRepository.findByUsuarioIdUsuario(idUsuario)
                .stream()
                .map(CompraDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CompraDTO> obtenerTodasLasCompras() {
        return compraRepository.findAll()
                .stream()
                .map(CompraDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean usuarioTieneCurso(Integer idUsuario, Integer idCurso) {
        return compraRepository.existsByUsuarioIdUsuarioAndCursoIdCurso(
                idUsuario, idCurso);
    }

    @Transactional
    public CompraDTO obtenerCompraPorId(Integer idCompra) {
        Compra compra = compraRepository.findById(idCompra)
                .orElseThrow(() -> new RuntimeException(
                        "Compra no encontrada con ID: " + idCompra));

        return new CompraDTO(compra);
    }
}