package com.EFSRTIII.ventacursos.service;

import com.EFSRTIII.ventacursos.models.Rol;

import java.util.List;

public interface RolService {
    public List<Rol> listarRoles();

    public Rol buscarRolPorId(Integer id);

    public Rol guardarRol(Rol rol);

    public void eliminarRoles(Integer id);

}
