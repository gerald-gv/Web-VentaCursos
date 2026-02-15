package com.EFSRTIII.ventacursos.repositories;

import com.EFSRTIII.ventacursos.models.CursoContenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoContenidoRepository extends JpaRepository<CursoContenido, Integer> {

    // Obtener contenidos de un curso ordenados
    List<CursoContenido> findByIdCursoAndActivoTrueOrderByOrden(Integer idCurso);

    // Contar contenidos de un curso
    long countByIdCursoAndActivoTrue(Integer idCurso);
}
