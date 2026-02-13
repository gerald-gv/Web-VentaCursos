package com.EFSRTIII.ventacursos.service;

import com.EFSRTIII.ventacursos.models.Curso;

import java.util.List;

public interface CursoService {

    public List<Curso> listarCursos();

    public Curso guardarCurso(Curso curso);

    public void eliminarCurso(Integer id);

    List<Curso> obtenerCursosComprados(String email);

}
