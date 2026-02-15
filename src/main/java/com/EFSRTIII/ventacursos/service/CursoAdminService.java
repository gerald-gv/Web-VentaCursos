package com.EFSRTIII.ventacursos.service;

import com.EFSRTIII.ventacursos.dto.CursoFormDTO;
import com.EFSRTIII.ventacursos.models.Curso;
import com.EFSRTIII.ventacursos.models.CursoContenido;
import com.EFSRTIII.ventacursos.repositories.CursoContenidoRepository;
import com.EFSRTIII.ventacursos.repositories.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CursoAdminService {

    private final CursoRepository cursoRepository;
    private final CursoContenidoRepository cursoContenidoRepository;

    public List<Curso> obtenerTodosLosCursos() {
        return cursoRepository.findAll();
    }

    public CursoFormDTO obtenerCursoParaEditar(Integer idCurso) {

        Curso curso = obtenerCursoPorId(idCurso);

        CursoFormDTO dto = new CursoFormDTO(curso);

        dto.setModulos(
                cursoContenidoRepository
                        .findByIdCursoAndActivoTrueOrderByOrden(idCurso)
                        .stream()
                        .map(this::mapToModuloDTO)
                        .toList()
        );
        return dto;
    }

    @Transactional
    public Curso guardarCurso(CursoFormDTO dto) {

        Curso curso = (dto.getIdCurso() != null)
                ? cursoRepository.findById(dto.getIdCurso())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"))
                : new Curso();

        dto.aplicarACurso(curso);

        curso = cursoRepository.save(curso);

        if (dto.getModulos() != null && !dto.getModulos().isEmpty()) {
            guardarModulos(curso.getIdCurso(), dto.getModulos());
        }

        return curso;
    }

    public void guardarModulos(Integer idCurso, List<CursoFormDTO.ModuloDTO> modulos) {

        if (modulos == null || modulos.isEmpty()) return;

        cursoContenidoRepository.deleteAll(
                cursoContenidoRepository.findByIdCursoAndActivoTrueOrderByOrden(idCurso)
        );

        cursoContenidoRepository.saveAll(
                modulos.stream()
                        .map(m -> mapToContenidoEntity(idCurso, m))
                        .toList()
        );
    }

    @Transactional
    public void cambiarEstadoCurso(Integer idCurso, Boolean nuevoEstado) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        curso.setActivo(nuevoEstado);
        cursoRepository.save(curso);
    }

    public long contarCursosActivos() {
        return cursoRepository.countByActivoTrue();
    }

    // MÉTODOS PRIVADOS

    private Curso obtenerCursoPorId(Integer id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
    }

    private CursoFormDTO.ModuloDTO mapToModuloDTO(CursoContenido c) {
        return new CursoFormDTO.ModuloDTO(
                c.getIdContenido(),
                c.getTitulo(),
                c.getDescripcion(),
                c.getUrl(),
                c.getOrden()
        );
    }

    private CursoContenido mapToContenidoEntity(Integer idCurso, CursoFormDTO.ModuloDTO m) {

        CursoContenido contenido = new CursoContenido();

        contenido.setIdCurso(idCurso);
        contenido.setTitulo(m.getTitulo());
        contenido.setDescripcion(m.getDescripcion());
        contenido.setUrl(m.getUrl());
        contenido.setOrden(m.getOrden());
        contenido.setTipo("video");
        contenido.setActivo(true);

        return contenido;
    }
}
