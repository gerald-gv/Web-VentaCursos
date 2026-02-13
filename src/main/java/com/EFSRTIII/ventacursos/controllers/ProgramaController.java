package com.EFSRTIII.ventacursos.controllers;

import com.EFSRTIII.ventacursos.dto.CompraRequestDTO;
import com.EFSRTIII.ventacursos.models.Curso;
import com.EFSRTIII.ventacursos.models.Usuario;
import com.EFSRTIII.ventacursos.repositories.CursoRepository;
import com.EFSRTIII.ventacursos.service.CompraService;
import com.EFSRTIII.ventacursos.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProgramaController {

    private final CursoRepository cursoRepository;
    private final CompraService compraService;
    private final UsuarioService usuarioService;

    public ProgramaController(CursoRepository cursoRepository, CompraService compraService, UsuarioService usuarioService){
        this.cursoRepository = cursoRepository;
        this.compraService = compraService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/programas")
    public String programas(Model model){
        // Traer los cursos desde la BD
        List<Curso> cursos = cursoRepository.findAll();

        //Establecer atributo
        model.addAttribute("programas", cursos);

        return "pages/programas";
    }
    // Ver detalle de un programa/curso específico, te sirve para ver el contenido del curso
    @GetMapping("/programas/{idCurso}")
    public String verDetallePrograma(@PathVariable Integer idCurso,
                                     Model model,
                                     Authentication authentication) {
        try {
            Curso curso = cursoRepository.findById(idCurso)
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

            model.addAttribute("curso", curso);

            // Verificar si el usuario ya compró el curso
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                Usuario usuario = usuarioService.buscarPorEmail(email).orElse(null);

                if (usuario != null) {
                    boolean yaComprado = compraService.usuarioTieneCurso(
                            usuario.getIdUsuario(), idCurso);
                    model.addAttribute("yaComprado", yaComprado);
                    model.addAttribute("idUsuario", usuario.getIdUsuario());
                }
            }

            return "pages/detalle-programa";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "pages/error";
        }
    }

    // Procesar compra
    @PostMapping("/comprar-programa")
    public String comprarPrograma(@RequestParam Integer idCurso,
                                  @RequestParam(required = false) String metodoPago,
                                  Authentication authentication,
                                  Model model) {
        try {
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            CompraRequestDTO request = new CompraRequestDTO();
            request.setIdUsuario(usuario.getIdUsuario());
            request.setIdCurso(idCurso);
            request.setMetodoPago(metodoPago != null ? metodoPago : "gratis");

            compraService.realizarCompra(request);

            return "redirect:/mis-cursos?compraExitosa=true";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "pages/error";
        }
    }
}
