package com.EFSRTIII.ventacursos.service.impl;

import com.EFSRTIII.ventacursos.models.Rol;
import com.EFSRTIII.ventacursos.repositories.RolRepository;
import com.EFSRTIII.ventacursos.service.RolService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolServiceImpl implements RolService {

    RolRepository rolRepo;

    public RolServiceImpl(RolRepository rolRepo){
        this.rolRepo = rolRepo;
    }

    @Override
    public List<Rol> listarRoles() {
        return rolRepo.findAll();
    }

    @Override
    public Rol buscarRolPorId(Integer id) {
        return rolRepo.findById(id).orElseThrow(() -> new RuntimeException("Curso no encontrado con id: "+ id));
    }

    @Override
    public Rol guardarRol(Rol rol) {
        Rol rol1 = new Rol(rol.getNombreRol(), rol.getDescripcion());
        return rolRepo.save(rol);
    }

    @Override
    public void eliminarRoles(Integer id) {
        rolRepo.deleteById(id);
    }
}
