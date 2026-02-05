package com.EFSRTIII.ventacursos.repositories;

import com.EFSRTIII.ventacursos.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByNombreRol(String nombre);
}
