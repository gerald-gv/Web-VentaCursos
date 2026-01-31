package com.EFSRTIII.ventacursos.repositories;

import com.EFSRTIII.ventacursos.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}
