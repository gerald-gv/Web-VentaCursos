package com.EFSRTIII.ventacursos.repositories;

import com.EFSRTIII.ventacursos.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Integer> {
    List<Curso> findByActivoTrue();

    Long countByActivoTrue();
}
