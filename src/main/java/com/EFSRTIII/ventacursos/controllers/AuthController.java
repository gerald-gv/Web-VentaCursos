package com.EFSRTIII.ventacursos.controllers;

import com.EFSRTIII.ventacursos.dto.RegisterDTO;
import com.EFSRTIII.ventacursos.models.Rol;
import com.EFSRTIII.ventacursos.models.Usuario;
import com.EFSRTIII.ventacursos.service.RolService;
import com.EFSRTIII.ventacursos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RolService rolService;

    @PostMapping("/register")
    public String registrarUsuario(@ModelAttribute RegisterDTO dto) {
        if (!dto.getContrasenia().equals(dto.getConfirmarContrasenia()))
            return "redirect:/?passwordError";

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setContrasenia(dto.getContrasenia());

        Rol rolUsuario = rolService.buscarPorNombre("ROLE_CLIENTE").orElseThrow();
        usuario.getRoles().add(rolUsuario);

        usuarioService.registrarUsuario(usuario);
        return "redirect:/?registroExitoso";
    }
}
