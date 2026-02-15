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
        Curso curso = Curso.builder()
                .titulo("Curso de testeo con SpringBoot")
                .descripcion("Aprender a hacer tests de manera profesional")
                .precio(BigDecimal.ZERO)
                .activo(true)
                .build();
        Curso saved = cursoService.guardarCurso(curso);
        assertEquals("Curso de testeo con SpringBoot", saved.getTitulo());
    }
}
