package com.EFSRTIII.ventacursos.controllers;

import com.EFSRTIII.ventacursos.models.Curso;
import com.EFSRTIII.ventacursos.models.Usuario;
import com.EFSRTIII.ventacursos.service.CursoService;
import com.EFSRTIII.ventacursos.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final CursoService cursoService;

    public UsuarioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder, CursoService cursoService){
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.cursoService = cursoService;
    }

    @GetMapping("/mi-cuenta")
    public String verPerfil(Authentication authentication,
                            @RequestParam(value = "editar", required = false) Boolean editar,
                            @RequestParam(value = "mensaje", required = false) String mensaje,
                            Model model) {
        String email = authentication.getName();
        Optional<Usuario> optionalUsuario = usuarioService.buscarPorEmail(email);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            model.addAttribute("usuario", usuario);
            model.addAttribute("isEditing", editar != null && editar);
            if (mensaje != null) {
                model.addAttribute("mensaje", mensaje);
            }
            return "usuario/perfil";
        } else {
            return "redirect:/";
        }
    }


    @PostMapping("/actualizar")
    public String actualizarPerfil(Authentication authentication, @RequestParam String nombre,
                                   @RequestParam String email,
                                   @RequestParam(required = false) String contrasenia,
                                   Model model) {

        String userEmail = authentication.getName();
        Optional<Usuario> optionalUsuario = usuarioService.buscarPorEmail(userEmail);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            // Validar si el email ya existe en otro usuario
            Optional<Usuario> emailExistente = usuarioService.buscarPorEmail(email);
            if (emailExistente.isPresent() && !emailExistente.get().getIdUsuario().equals(usuario.getIdUsuario())) {
                model.addAttribute("usuario", usuario);
                model.addAttribute("isEditing", true);
                model.addAttribute("error", "El correo ya está registrado");
                return "usuario/perfil";
            }

            usuarioService.actualizarUsuario(usuario, nombre, email, contrasenia);

            return "redirect:/mi-cuenta?mensaje=Perfil+actualizado+exitosamente";

        } else {
            return "redirect:/";
        }
    }
}
