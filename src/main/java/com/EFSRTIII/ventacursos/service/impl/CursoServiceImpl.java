package com.EFSRTIII.ventacursos.service.impl;

import com.EFSRTIII.ventacursos.models.Curso;
import com.EFSRTIII.ventacursos.repositories.CursoRepository;
import com.EFSRTIII.ventacursos.service.CursoService;
import org.springframework.stereotype.Service;

@Service
public class CursoServiceImpl implements CursoService {

    CursoRepository cursoRepo;

    public CursoServiceImpl(CursoRepository repo){
        this.cursoRepo = repo;
    }

    @Override
    public Curso guardarCurso(Curso curso) {
        return cursoRepo.save(curso);
    }

}
