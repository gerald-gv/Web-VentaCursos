package com.EFSRTIII.ventacursos.controllers;

import com.EFSRTIII.ventacursos.dto.CompraDTO;
import com.EFSRTIII.ventacursos.models.Curso;
import com.EFSRTIII.ventacursos.models.CursoContenido;
import com.EFSRTIII.ventacursos.models.Usuario;
import com.EFSRTIII.ventacursos.repositories.CursoContenidoRepository;
import com.EFSRTIII.ventacursos.repositories.CursoRepository;
import com.EFSRTIII.ventacursos.service.CompraService;
import com.EFSRTIII.ventacursos.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mis-cursos")
@RequiredArgsConstructor
public class MisCursosController {

    private final CursoRepository cursoRepository;
    private final CursoContenidoRepository cursoContenidoRepository;
    private final CompraService compraService;
    private final UsuarioService usuarioService;

    @GetMapping
    public String misCursos(Model model, Authentication authentication) {

        // Verificar autenticación
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            // Obtener usuario
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Obtener compras del usuario
            List<CompraDTO> compras = compraService.obtenerCursosComprados(usuario.getIdUsuario());

            // Obtener detalles completos de los cursos
            List<Curso> cursosComprados = compras.stream()
                    .map(compra -> cursoRepository.findById(compra.getIdCurso()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            // Calcular total gastado
            BigDecimal totalGastado = compras.stream()
                    .map(CompraDTO::getPrecioPagado)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // Pasar datos al modelo
            model.addAttribute("cursos", cursosComprados);
            model.addAttribute("compras", compras);
            model.addAttribute("usuario", usuario);
            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("cantidadCursos", cursosComprados.size());
            model.addAttribute("totalGastado", totalGastado);

            return "pages/mis-cursos";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "pages/error";
        }
    }
//AQUI VERIFICA SI ERES ADMIN O USUARIO, SI ERES ADMIN TE DA ACCESO GRATIS, O ESO SE SUPONE, OJALA FUNCIONE
    @GetMapping("/{idCurso}")
    public String verCursoComprado(@PathVariable Integer idCurso,
                                   Model model,
                                   Authentication authentication) {

        // Verificar autenticación
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            // Obtener usuario
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // VERIFICAR si es ADMIN
            boolean esAdmin = esAdmin(authentication);

            // olo verificar compra si NO es admin
            if (!esAdmin) {
                boolean yaComprado = compraService.usuarioTieneCurso(usuario.getIdUsuario(), idCurso);

                if (!yaComprado) {
                    // Si no compró, redirigir a la página de venta
                    return "redirect:/programas/" + idCurso + "?error=noComprado";
                }
            }

            // Obtener datos del curso
            Curso curso = cursoRepository.findById(idCurso)
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

            // Obtener contenidos del curso (desbloqueados)
            List<CursoContenido> contenidos = cursoContenidoRepository
                    .findByIdCursoAndActivoTrueOrderByOrden(idCurso);

            // Pasar datos al modelo
            model.addAttribute("curso", curso);
            model.addAttribute("contenidos", contenidos);
            model.addAttribute("usuario", usuario);
            model.addAttribute("esAdmin", esAdmin); // ⬅️ NUEVO: para mostrar badge en la vista

            return "pages/curso-estudio";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "pages/error";
        }
    }
    //VERIFICA SI ES ADMIN, SI LO ES MUESTRA LOS CURSOS SIN TENER QUE PAGAR NADA XDXDXD
    private boolean esAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
    }

}