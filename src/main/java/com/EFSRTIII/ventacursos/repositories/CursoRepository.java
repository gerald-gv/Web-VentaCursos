package com.EFSRTIII.ventacursos.repositories;

import com.EFSRTIII.ventacursos.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Integer> {
}
