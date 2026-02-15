package com.EFSRTIII.ventacursos.controllers;

import com.EFSRTIII.ventacursos.dto.CompraDTO;
import com.EFSRTIII.ventacursos.dto.CompraRequestDTO;
import com.EFSRTIII.ventacursos.models.Usuario;
import com.EFSRTIII.ventacursos.service.CompraService;
import com.EFSRTIII.ventacursos.service.PaypalService;
import com.EFSRTIII.ventacursos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PaypalService payPalService;

    @PostMapping("/finalizar-paypal")
    public ResponseEntity<?> finalizarCompraPorPayPal(
            @RequestBody Map<String, Object> payload,
            Authentication authentication) {

        try {
            String orderId = (String) payload.get("orderId");
            Integer idCurso = Integer.parseInt(payload.get("idCurso").toString());

            // Verificar que el pago fue completado en PayPal
            boolean pagoVerificado = payPalService.verificarPago(orderId);

            if (!pagoVerificado) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El pago no pudo ser verificado en PayPal");
                return ResponseEntity.badRequest().body(error);
            }
            // Obtener usuario autenticado
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Verificar si ya compró el curso
            if (compraService.usuarioTieneCurso(usuario.getIdUsuario(), idCurso)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Ya has comprado este curso anteriormente");
                return ResponseEntity.badRequest().body(error);
            }
            // Registrar la compra
            CompraRequestDTO request = new CompraRequestDTO();
            request.setIdUsuario(usuario.getIdUsuario());
            request.setIdCurso(idCurso);
            request.setMetodoPago("paypal");

            CompraDTO compra = compraService.realizarCompra(request);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "¡Compra realizada con éxito!");
            response.put("compra", compra);
            response.put("redirectUrl", "/mis-cursos/" + idCurso); // ⬅️ NUEVO: URL de redirección

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    @PostMapping
    public ResponseEntity<?> realizarCompra(
            @RequestBody CompraRequestDTO request) {
        try {
            CompraDTO compra = compraService.realizarCompra(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(compra);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<CompraDTO>> obtenerTodasLasCompras() {
        return ResponseEntity.ok(compraService.obtenerTodasLasCompras());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCompraPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(compraService.obtenerCompraPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> obtenerCursosComprados(
            @PathVariable Integer idUsuario) {
        try {
            List<CompraDTO> cursos =
                    compraService.obtenerCursosComprados(idUsuario);
            return ResponseEntity.ok(cursos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/verificar")
    public ResponseEntity<Map<String, Boolean>> verificarCompra(
            @RequestParam Integer idUsuario,
            @RequestParam Integer idCurso) {

        boolean tieneCurso = compraService.usuarioTieneCurso(idUsuario, idCurso);
        return ResponseEntity.ok(Map.of("tieneCurso", tieneCurso));
    }
}