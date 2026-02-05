package com.EFSRTIII.ventacursos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminController {
    //Ruta de prueba
    @GetMapping("/rutaQueDeberiaSerLaDelAdmin")
    @ResponseBody
    public String rutaQueDeberiaSerLaDelAdmin(){
        return "Full Show!";
    }
}
