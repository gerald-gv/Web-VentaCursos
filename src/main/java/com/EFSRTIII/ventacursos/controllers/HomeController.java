package com.EFSRTIII.ventacursos.controllers;

import com.EFSRTIII.ventacursos.service.CompraService;
import com.EFSRTIII.ventacursos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private CompraService compraService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String home() {
        return "pages/home";
    }

    @GetMapping("/nosotros")
    public String about(){
        return "pages/nosotros";
    }

    @GetMapping("/soporte")
    public String soporte() { return "pages/soporte";}

}