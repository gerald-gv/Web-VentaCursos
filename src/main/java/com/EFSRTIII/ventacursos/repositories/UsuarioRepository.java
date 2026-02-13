package com.EFSRTIII.ventacursos.repositories;

import com.EFSRTIII.ventacursos.models.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @EntityGraph(attributePaths = {"roles"})
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
