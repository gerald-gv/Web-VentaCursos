package com.EFSRTIII.ventacursos.repositories;

import com.EFSRTIII.ventacursos.models.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Integer> {
    // Buscar todas las compras de un usuario
    List<Compra> findByUsuarioIdUsuario(Integer idUsuario);

    // Verificar si un usuario ya compró un curso
    boolean existsByUsuarioIdUsuarioAndCursoIdCurso(
            Integer idUsuario, Integer idCurso
    );

    // Query personalizada con TODOS los filtros opcionales, esto sirve para la pagina de admin
    @Query("SELECT c FROM Compra c WHERE " +
            "(:idCurso IS NULL OR c.curso.idCurso = :idCurso) AND " +
            "(:idUsuario IS NULL OR c.usuario.idUsuario = :idUsuario) AND " +
            "(:metodoPago IS NULL OR c.metodoPago = :metodoPago) AND " +
            "(:fechaInicio IS NULL OR c.fechaCompra >= :fechaInicio) AND " +
            "(:fechaFin IS NULL OR c.fechaCompra <= :fechaFin) " +
            "ORDER BY c.fechaCompra DESC")
    List<Compra> buscarConFiltros(
            @Param("idCurso") Integer idCurso,
            @Param("idUsuario") Integer idUsuario,
            @Param("metodoPago") String metodoPago,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

}
