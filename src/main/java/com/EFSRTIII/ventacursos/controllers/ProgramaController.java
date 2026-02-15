package com.EFSRTIII.ventacursos.controllers;

import com.EFSRTIII.ventacursos.dto.CompraDTO;
import com.EFSRTIII.ventacursos.dto.CompraRequestDTO;
import com.EFSRTIII.ventacursos.models.Curso;
import com.EFSRTIII.ventacursos.models.CursoContenido;
import com.EFSRTIII.ventacursos.models.Usuario;
import com.EFSRTIII.ventacursos.repositories.CursoContenidoRepository;
import com.EFSRTIII.ventacursos.repositories.CursoRepository;
import com.EFSRTIII.ventacursos.service.CompraService;
import com.EFSRTIII.ventacursos.service.PaypalService;
import com.EFSRTIII.ventacursos.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProgramaController {

    private final CursoRepository cursoRepository;
    private final CursoContenidoRepository cursoContenidoRepository;
    private final CompraService compraService;
    private final UsuarioService usuarioService;
    private PaypalService payPalService;

    @GetMapping("/programas")
    public String programas(Model model, Authentication authentication) {

        model.addAttribute("programas", cursoRepository.findByActivoTrue());

        Usuario usuario = obtenerUsuarioAutenticado(authentication);

        List<Integer> cursosComprados = new ArrayList<>();

        if (usuario != null) {

            model.addAttribute("idUsuario", usuario.getIdUsuario());

            cursosComprados = compraService.obtenerCursosComprados(usuario.getIdUsuario())
                    .stream()
                    .map(CompraDTO::getIdCurso)
                    .toList();
        }

        model.addAttribute("cursosComprados", cursosComprados);

        return "pages/programas";
    }

    @GetMapping("/programas/{idCurso}")
    public String verDetallePrograma(@PathVariable Integer idCurso,
                                     Model model,
                                     Authentication authentication) {

        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        model.addAttribute("curso", curso);
        model.addAttribute("objetivos", convertirTextoLista(curso.getObjetivos()));
        model.addAttribute("requisitos", convertirTextoLista(curso.getRequisitos()));

        List<CursoContenido> contenidos =
                cursoContenidoRepository.findByIdCursoAndActivoTrueOrderByOrden(idCurso);

        model.addAttribute("contenidos", contenidos);
        model.addAttribute("cantidadModulos",
                cursoContenidoRepository.countByIdCursoAndActivoTrue(idCurso));

        Usuario usuario = obtenerUsuarioAutenticado(authentication);

        boolean yaComprado = usuario != null &&
                compraService.usuarioTieneCurso(usuario.getIdUsuario(), idCurso);

        if (usuario != null) {
            model.addAttribute("idUsuario", usuario.getIdUsuario());
        }

        model.addAttribute("yaComprado", yaComprado);

        return "pages/detalle-programa";
    }

    @GetMapping("/pago-exitoso")
    public String pagoExitoso(@RequestParam String token,
                              @RequestParam Integer idCurso,
                              Authentication authentication,
                              Model model) {

        if (!payPalService.capturarPago(token)) {
            model.addAttribute("error", "No se pudo confirmar el pago");
            return "pages/error";
        }

        Usuario usuario = obtenerUsuarioAutenticado(authentication);

        compraService.realizarCompra(
                crearCompraRequest(usuario.getIdUsuario(), idCurso, "paypal")
        );

        return "redirect:/mis-cursos?compraExitosa=true";
    }

    // Callback cuando el pago es cancelado
    @GetMapping("/pago-cancelado/{idCurso}")
    public String pagoCancelado(@PathVariable Integer idCurso) {
        return "redirect:/programas/" + idCurso + "?pagoCancelado=true";
    }

    @PostMapping("/inscribirse-gratis")
    public String inscribirseGratis(@RequestParam Integer idCurso,
                                    Authentication authentication,
                                    Model model) {

        Usuario usuario = obtenerUsuarioAutenticado(authentication);

        compraService.realizarCompra(
                crearCompraRequest(usuario.getIdUsuario(), idCurso, "gratis")
        );

        return "redirect:/mis-cursos?compraExitosa=true";
    }

    //METODOS HELPER:
    private Usuario obtenerUsuarioAutenticado(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return usuarioService.buscarPorEmail(authentication.getName()).orElse(null);
    }
    private List<String> convertirTextoLista(String texto) {
        if (texto == null || texto.isBlank()) {
            return new ArrayList<>();
        }
        return Arrays.asList(texto.split("\n"));
    }
    private CompraRequestDTO crearCompraRequest(Integer idUsuario, Integer idCurso, String metodo) {
        CompraRequestDTO request = new CompraRequestDTO();
        request.setIdUsuario(idUsuario);
        request.setIdCurso(idCurso);
        request.setMetodoPago(metodo);
        return request;
    }
}
