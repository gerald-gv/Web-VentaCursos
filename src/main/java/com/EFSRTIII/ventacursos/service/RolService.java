package com.EFSRTIII.ventacursos.service;

import com.EFSRTIII.ventacursos.models.Rol;

import java.util.Optional;

public interface RolService {

    public Optional<Rol> buscarPorNombre(String nombre);

}
