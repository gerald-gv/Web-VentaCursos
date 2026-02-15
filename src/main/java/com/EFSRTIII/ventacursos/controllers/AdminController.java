package com.EFSRTIII.ventacursos.controllers;

import com.EFSRTIII.ventacursos.dto.CompraDTO;
import com.EFSRTIII.ventacursos.dto.CursoFormDTO;
import com.EFSRTIII.ventacursos.dto.UsuarioFormDTO;
import com.EFSRTIII.ventacursos.models.Compra;
import com.EFSRTIII.ventacursos.models.Curso;
import com.EFSRTIII.ventacursos.models.Usuario;
import com.EFSRTIII.ventacursos.repositories.CompraRepository;
import com.EFSRTIII.ventacursos.repositories.UsuarioRepository;
import com.EFSRTIII.ventacursos.service.CompraService;
import com.EFSRTIII.ventacursos.service.CursoAdminService;
import com.EFSRTIII.ventacursos.service.UsuarioAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final CursoAdminService cursoAdminService;
    private final CompraService compraService;
    private final CompraRepository compraRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioAdminService usuarioAdminService;

    /**Dashboard principal**/
    @GetMapping
    public String dashboard(Model model) {
        // Estadísticas
        long totalCursos = cursoAdminService.contarCursosActivos();
        long totalUsuarios = usuarioRepository.count();
        long totalCompras = compraRepository.count();

        // Calcular ingresos totales
        BigDecimal totalIngresos = compraRepository.findAll().stream()
                .map(Compra::getPrecioPagado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("totalCursos", totalCursos);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalCompras", totalCompras);
        model.addAttribute("totalIngresos", totalIngresos);

        return "admin/dashboard";
    }

    /**Lista de cursos**/
    @GetMapping("/cursos")
    public String listarCursos(Model model) {
        List<Curso> cursos = cursoAdminService.obtenerTodosLosCursos();
        model.addAttribute("cursos", cursos);
        return "admin/cursos";
    }

    /**Formulario para crear curso**/
    @GetMapping("/cursos/nuevo")
    public String nuevoCursoForm(Model model) {
        model.addAttribute("curso", new CursoFormDTO());
        model.addAttribute("esNuevo", true);
        return "admin/curso-form";
    }

    /**Formulario para editar curso**/
    @GetMapping("/cursos/editar/{id}")
    public String editarCursoForm(@PathVariable Integer id, Model model) {
        CursoFormDTO curso = cursoAdminService.obtenerCursoParaEditar(id);
        model.addAttribute("curso", curso);
        model.addAttribute("esNuevo", false);
        return "admin/curso-form";
    }

    /**Guardar curso (crear o actualizar**/
    @PostMapping("/cursos/guardar")
    public String guardarCurso(@ModelAttribute CursoFormDTO cursoDTO,
                               @RequestParam(required = false) String[] modulosTitulo,
                               @RequestParam(required = false) String[] modulosDescripcion,
                               @RequestParam(required = false) String[] modulosUrl,
                               @RequestParam(required = false) Integer[] modulosOrden,
                               RedirectAttributes redirectAttributes) {

        try {

            cursoDTO.getModulos().addAll(
                    construirModulos(modulosTitulo, modulosDescripcion, modulosUrl, modulosOrden)
            );

            cursoAdminService.guardarCurso(cursoDTO);

            redirectAttributes.addFlashAttribute("mensaje", "Curso guardado");
            return "redirect:/admin/cursos";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/cursos";
        }
    }

    /**Desactivar/activar curso**/
    @PostMapping("/cursos/cambiar-estado/{id}")
    public String cambiarEstadoCurso(@PathVariable Integer id,
                                     @RequestParam Boolean nuevoEstado,
                                     RedirectAttributes redirectAttributes) {
        try {
            cursoAdminService.cambiarEstadoCurso(id, nuevoEstado);
            String mensaje = nuevoEstado ? "✅ Curso activado exitosamente" : "✅ Curso desactivado exitosamente";
            redirectAttributes.addFlashAttribute("mensaje", mensaje);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/admin/cursos";
    }

    /**Lista de compras*/
    @GetMapping("/compras")
    public String listarCompras(
            @RequestParam(required = false) Integer cursoId,
            @RequestParam(required = false) Integer usuarioId,
            @RequestParam(required = false) String metodoPago,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model model) {

        List<CompraDTO> compras;

        if (metodoPago != null && metodoPago.trim().isEmpty()) {
            metodoPago = null;
        }

        // Convertir fechas si existen
        LocalDateTime fechaInicioDateTime = fechaInicio != null ? fechaInicio.atStartOfDay() : null;
        LocalDateTime fechaFinDateTime = fechaFin != null ? fechaFin.atTime(23, 59, 59) : null;

        boolean hayFiltros = cursoId != null ||
                usuarioId != null ||
                metodoPago != null ||
                fechaInicio != null ||
                fechaFin != null;

        // Buscar con filtros o sin filtros
        if (hayFiltros) {
            compras = compraService.buscarComprasConFiltros(
                    cursoId,
                    usuarioId,
                    metodoPago,
                    fechaInicioDateTime,
                    fechaFinDateTime
            );
        } else {
            compras = compraService.obtenerTodasLasCompras();
        }
        // Calcular total
        BigDecimal totalIngresos = compras.stream()
                .map(CompraDTO::getPrecioPagado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Pasar datos al modelo
        model.addAttribute("compras", compras);
        model.addAttribute("totalIngresos", totalIngresos);

        // Pasar cursos y usuarios para los filtros
        model.addAttribute("cursos", cursoAdminService.obtenerTodosLosCursos());
        model.addAttribute("usuarios", usuarioAdminService.obtenerTodosLosUsuarios());

        // Mantener valores de los filtros
        model.addAttribute("cursoIdSeleccionado", cursoId);
        model.addAttribute("usuarioIdSeleccionado", usuarioId);
        model.addAttribute("metodoPagoSeleccionado", metodoPago);
        model.addAttribute("fechaInicioSeleccionada", fechaInicio);
        model.addAttribute("fechaFinSeleccionada", fechaFin);

        return "admin/compras";
    }
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model, Authentication authentication) {
        List<Usuario> usuarios = usuarioAdminService.obtenerTodosLosUsuarios();
        String emailActual = authentication.getName();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("emailAdminActual", emailActual);  // ⬅️ NUEVO
        return "admin/usuarios";
    }
    /**Ver detalle de un usuario (info + compras)**/
    @GetMapping("/usuarios/detalle/{id}")
    public String verDetalleUsuario(@PathVariable Integer id, Model model) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Obtener compras del usuario
            List<CompraDTO> compras = compraService.obtenerCursosComprados(id);

            // Calcular total gastado
            BigDecimal totalGastado = compras.stream()
                    .map(CompraDTO::getPrecioPagado)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            model.addAttribute("usuario", usuario);
            model.addAttribute("compras", compras);
            model.addAttribute("totalGastado", totalGastado);

            return "admin/usuario-detalle";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/usuarios";
        }
    }
    @GetMapping("/usuarios/nuevo")
    public String nuevoUsuarioForm(Model model) {
        UsuarioFormDTO usuario = new UsuarioFormDTO();
        usuario.getRoles().add("CLIENTE"); // Rol por defecto

        model.addAttribute("usuario", usuario);
        model.addAttribute("esNuevo", true);
        return "admin/usuario-form";
    }

    /**Formulario para editar usuario**/
    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuarioForm(@PathVariable Integer id, Model model) {
        try {
            UsuarioFormDTO usuario = usuarioAdminService.obtenerUsuarioParaEditar(id);
            model.addAttribute("usuario", usuario);
            model.addAttribute("esNuevo", false);
            return "admin/usuario-form";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Usuario no encontrado: " + e.getMessage());
            return "redirect:/admin/usuarios";
        }
    }

    /**Guardar usuario (crear o actualizar)**/
    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute UsuarioFormDTO usuarioDTO,
                                 RedirectAttributes redirectAttributes) {
        try {
            usuarioAdminService.guardarUsuario(usuarioDTO);
            redirectAttributes.addFlashAttribute("mensaje", "✅ Usuario guardado exitosamente");
            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Error al guardar: " + e.getMessage());
            return "redirect:/admin/usuarios";
        }
    }

    /**Cambiar estado de usuario (activar/desactivar)**/
    @PostMapping("/usuarios/cambiar-estado/{id}")
    public String cambiarEstadoUsuario(@PathVariable Integer id,
                                       @RequestParam Boolean nuevoEstado,
                                       RedirectAttributes redirectAttributes) {
        try {
            usuarioAdminService.cambiarEstado(id, nuevoEstado);
            redirectAttributes.addFlashAttribute("mensaje", "Estado actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    //METODO HELPER:
    private List<CursoFormDTO.ModuloDTO> construirModulos(
            String[] titulos,
            String[] descripciones,
            String[] urls,
            Integer[] ordenes) {

        List<CursoFormDTO.ModuloDTO> modulos = new ArrayList<>();

        if (titulos == null) return modulos;

        for (int i = 0; i < titulos.length; i++) {

            modulos.add(new CursoFormDTO.ModuloDTO(
                    null,
                    titulos[i],
                    descripciones != null && i < descripciones.length ? descripciones[i] : null,
                    urls != null && i < urls.length ? urls[i] : null,
                    ordenes != null && i < ordenes.length ? ordenes[i] : i + 1
            ));
        }
        return modulos;
    }
}