package com.EFSRTIII.ventacursos.curso.test;

import com.EFSRTIII.ventacursos.models.Curso;
import com.EFSRTIII.ventacursos.service.CursoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CursoServiceTest {

    @Autowired
    private CursoService cursoService;

    @Test
    void guardarCurso(){
        Curso curso = new Curso("Curso de testeo con SpringBoot",
                "Aprender a hacer tests de manera profesional",
                new BigDecimal(0), true);

        Curso saved = cursoService.guardarCurso(curso);

        assertEquals("Curso de testeo con SpringBoot", saved.getTitulo());
    }
}
