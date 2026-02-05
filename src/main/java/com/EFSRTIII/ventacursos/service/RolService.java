package com.EFSRTIII.ventacursos.service;

import com.EFSRTIII.ventacursos.models.Rol;
import com.EFSRTIII.ventacursos.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface RolService {
    public List<Rol> listarRoles();

    public Rol buscarRolPorId(Integer id);

    public Rol guardarRol(Rol rol);

    public void eliminarRoles(Integer id);

    public Optional<Rol> buscarPorNombre(String nombre);

}
