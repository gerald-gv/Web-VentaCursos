package com.EFSRTIII.ventacursos.controllers;

import com.EFSRTIII.ventacursos.models.Curso;
import com.EFSRTIII.ventacursos.repositories.CursoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProgramaController {

    private final CursoRepository cursoRepository;

    public ProgramaController(CursoRepository cursoRepository){
        this.cursoRepository = cursoRepository;
    }

    @GetMapping("/programas")
    public String programas(Model model){
        // Traer los cursos desde la BD
        List<Curso> cursos = cursoRepository.findAll();

        //Establecer atributo
        model.addAttribute("programas", cursos);

        return "pages/programas";
    }

}
