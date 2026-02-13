package com.EFSRTIII.ventacursos.controllers;

import com.EFSRTIII.ventacursos.dto.CompraDTO;
import com.EFSRTIII.ventacursos.models.Usuario;
import com.EFSRTIII.ventacursos.service.CompraService;
import com.EFSRTIII.ventacursos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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

    // Método sin parámetro - obtiene el usuario autenticado
    @GetMapping("/mis-cursos")
    public String verMisCursos(Authentication authentication, Model model) {
        try {
            String email = authentication.getName();

            // Usar el método existente buscarPorEmail
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            List<CompraDTO> compras = compraService.obtenerCursosComprados(usuario.getIdUsuario());

            model.addAttribute("compras", compras);
            model.addAttribute("idUsuario", usuario.getIdUsuario());
            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("cantidadCursos", compras.size());

            double totalGastado = compras.stream()
                    .mapToDouble(c -> c.getPrecioPagado().doubleValue())
                    .sum();
            model.addAttribute("totalGastado", totalGastado);

            return "pages/mis-cursos";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "pages/error";
        }
    }


    // Método con parámetro - para admin
    @GetMapping("/mis-cursos/{idUsuario}")
    public String verMisCursosPorId(@PathVariable Integer idUsuario, Model model) {
        try {
            List<CompraDTO> compras = compraService.obtenerCursosComprados(idUsuario);

            model.addAttribute("compras", compras);
            model.addAttribute("idUsuario", idUsuario);
            model.addAttribute("cantidadCursos", compras.size());

            double totalGastado = compras.stream()
                    .mapToDouble(c -> c.getPrecioPagado().doubleValue())
                    .sum();
            model.addAttribute("totalGastado", totalGastado);

            return "pages/mis-cursos";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "pages/error";
        }
    }

    @GetMapping("/admin/compras")
    public String verTodasLasCompras(Model model) {
        List<CompraDTO> compras = compraService.obtenerTodasLasCompras();

        model.addAttribute("compras", compras);
        model.addAttribute("totalCompras", compras.size());

        return "pages/todas-compras";
    }
}