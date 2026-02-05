package com.EFSRTIII.ventacursos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UsuarioController {

    //Ruta de prueba
    @GetMapping("/rutaQueDeberiaSerLaDelUsuario")
    @ResponseBody
    public String rutaQueDeberiaSerLaDelUsuario(){
        return "Pasame la G!";
    }
}
