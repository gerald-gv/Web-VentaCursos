package com.EFSRTIII.ventacursos.controllers;

import com.EFSRTIII.ventacursos.models.Curso;
import com.EFSRTIII.ventacursos.models.Rol;
import com.EFSRTIII.ventacursos.models.Usuario;
import com.EFSRTIII.ventacursos.service.CursoService;
import com.EFSRTIII.ventacursos.service.RolService;
import com.EFSRTIII.ventacursos.service.UsuarioService;
import com.EFSRTIII.ventacursos.service.impl.RolServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class TestController {
    @Autowired
    CursoService cursoService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    RolService rolService;

    @GetMapping("/test/curso/{titulo}/{desc}/{precio}/{estado}")
    public String mixPathCurso(@PathVariable String titulo,
                             @PathVariable String desc,
                             @PathVariable BigDecimal precio,
                             @PathVariable boolean estado
                             ){

        Curso curso = new Curso(titulo, desc, precio, estado);

        cursoService.guardarCurso(curso);

        return "pages/home";
    }
    @GetMapping("/test/usuario/{nombre}/{email}/{contra}/{estado}/{rol}")
    public String mixPathUsuario(@PathVariable String nombre,
                             @PathVariable String email,
                             @PathVariable String contra,
                             @PathVariable boolean estado,
                             @PathVariable Integer rol
    ){
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.buscarRolPorId(rol));
        Usuario usuario = new Usuario(nombre, email, contra, estado, roles);

        usuarioService.guardarUsuario(usuario);

        return "pages/home";
    }
    @GetMapping("/test/rol/{nombre}/{desc}")
    public String mixPathRol(@PathVariable String nombre,
                                 @PathVariable String desc
    ){
        Rol rol = new Rol(nombre, desc);

        rolService.guardarRol(rol);

        return "pages/home";
    }
}
