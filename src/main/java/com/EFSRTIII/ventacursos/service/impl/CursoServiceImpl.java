package com.EFSRTIII.ventacursos.service.impl;

import com.EFSRTIII.ventacursos.models.Curso;
import com.EFSRTIII.ventacursos.repositories.CursoRepository;
import com.EFSRTIII.ventacursos.service.CursoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoServiceImpl implements CursoService {

    CursoRepository cursoRepo;

    public CursoServiceImpl(CursoRepository repo){
        this.cursoRepo = repo;
    }

    @Override
    public List<Curso> listarCursos() {
        return cursoRepo.findAll();
    }

    @Override
    public Curso guardarCurso(Curso curso) {
        Curso curso1 = new Curso(curso.getIdCurso(), curso.getTitulo(), curso.getDescripcion(),
                curso.getPrecio(), curso.isActivo(), curso.getFechaCreacion());
        return cursoRepo.save(curso1);
    }

    @Override
    public void eliminarCurso(Integer id) {
        cursoRepo.deleteById(id);
    }

    @Override
    public List<Curso> obtenerCursosComprados(String email) {
        // lógica aquí
        return null;
    }

}
