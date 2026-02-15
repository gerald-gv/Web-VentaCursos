package com.EFSRTIII.ventacursos.service.impl;

import com.EFSRTIII.ventacursos.models.Rol;
import com.EFSRTIII.ventacursos.repositories.RolRepository;
import com.EFSRTIII.ventacursos.service.RolService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    RolRepository rolRepo;

    public RolServiceImpl(RolRepository rolRepo){
        this.rolRepo = rolRepo;
    }

    @Override
    public Optional<Rol> buscarPorNombre(String nombre) {
        return rolRepo.findByNombreRol(nombre);
    }
}
