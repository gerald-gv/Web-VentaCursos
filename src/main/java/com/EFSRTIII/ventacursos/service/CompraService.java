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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final CompraRepository compraRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    @Transactional
    public CompraDTO realizarCompra(CompraRequestDTO request) {

        if (compraRepository.existsByUsuarioIdUsuarioAndCursoIdCurso(
                request.getIdUsuario(), request.getIdCurso())) {
            throw new RuntimeException("El usuario ya ha comprado este curso");
        }

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.isActivo()) {
            throw new RuntimeException("El usuario no está activo");
        }

        Curso curso = cursoRepository.findById(request.getIdCurso())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        if (!curso.getActivo()) {
            throw new RuntimeException("El curso no está disponible");
        }

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
            throw new RuntimeException("Usuario no encontrado");
        }

        return compraRepository.findByUsuarioIdUsuario(idUsuario)
                .stream()
                .map(CompraDTO::new)
                .toList();
    }

    public List<CompraDTO> obtenerTodasLasCompras() {
        return compraRepository.findAll()
                .stream()
                .map(CompraDTO::new)
                .toList();
    }

    @Transactional
    public boolean usuarioTieneCurso(Integer idUsuario, Integer idCurso) {
        return compraRepository.existsByUsuarioIdUsuarioAndCursoIdCurso(idUsuario, idCurso);
    }

    @Transactional
    public CompraDTO obtenerCompraPorId(Integer idCompra) {
        return compraRepository.findById(idCompra)
                .map(CompraDTO::new)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
    }

    public List<CompraDTO> buscarComprasConFiltros(
            Integer idCurso,
            Integer idUsuario,
            String metodoPago,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {

        return compraRepository.buscarConFiltros(
                        idCurso,
                        idUsuario,
                        metodoPago,
                        fechaInicio,
                        fechaFin
                ).stream()
                .map(CompraDTO::new)
                .toList();
    }
}
