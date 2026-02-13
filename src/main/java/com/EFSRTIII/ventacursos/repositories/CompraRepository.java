package com.EFSRTIII.ventacursos.repositories;

import com.EFSRTIII.ventacursos.models.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompraRepository extends JpaRepository<Compra, Integer> {
    // Buscar todas las compras de un usuario
    List<Compra> findByUsuarioIdUsuario(Integer idUsuario);

    // Buscar todas las compras de un curso
    List<Compra> findByCursoIdCurso(Integer idCurso);

    // Verificar si un usuario ya compró un curso
    boolean existsByUsuarioIdUsuarioAndCursoIdCurso(
            Integer idUsuario, Integer idCurso
    );

    // Buscar compra específica
    Optional<Compra> findByUsuarioIdUsuarioAndCursoIdCurso(
            Integer idUsuario, Integer idCurso
    );

    // Contar cursos comprados por un usuario
    @Query("SELECT COUNT(c) FROM Compra c WHERE c.usuario.idUsuario = :idUsuario")
    Long countCursosByUsuario(Integer idUsuario);
}
