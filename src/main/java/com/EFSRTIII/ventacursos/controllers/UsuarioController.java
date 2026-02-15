package com.EFSRTIII.ventacursos.controllers;

import com.EFSRTIII.ventacursos.models.Usuario;
import com.EFSRTIII.ventacursos.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/mi-cuenta")
    public String verPerfil(Authentication authentication,
                            @RequestParam(required = false) Boolean editar,
                            @RequestParam(required = false) String mensaje,
                            Model model) {

        try {
            Usuario usuario = obtenerUsuarioAutenticado(authentication);

            model.addAttribute("usuario", usuario);
            model.addAttribute("isEditing", Boolean.TRUE.equals(editar));
            model.addAttribute("mensaje", mensaje);

            return "usuario/perfil";

        } catch (RuntimeException e) {
            return "redirect:/";
        }
    }

    @PostMapping("/actualizar")
    public String actualizarPerfil(Authentication authentication,
                                   @RequestParam String nombre,
                                   @RequestParam String email,
                                   @RequestParam(required = false) String contrasenia,
                                   Model model) {

        try {
            Usuario usuario = obtenerUsuarioAutenticado(authentication);

            Optional<Usuario> emailExistente = usuarioService.buscarPorEmail(email);

            if (emailExistente.isPresent() &&
                    !emailExistente.get().getIdUsuario().equals(usuario.getIdUsuario())) {

                model.addAttribute("usuario", usuario);
                model.addAttribute("isEditing", true);
                model.addAttribute("error", "El correo ya está registrado");
                return "usuario/perfil";
            }

            usuarioService.actualizarUsuario(usuario, nombre, email, contrasenia);

            return "redirect:/mi-cuenta?mensaje=Perfil+actualizado+exitosamente";

        } catch (RuntimeException e) {
            return "redirect:/";
        }
    }
    //METODO HELPER
    private Usuario obtenerUsuarioAutenticado(Authentication authentication) {
        return usuarioService.buscarPorEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
