package com.EFSRTIII.ventacursos.controllers;

import com.EFSRTIII.ventacursos.dto.CompraDTO;
import com.EFSRTIII.ventacursos.dto.CompraRequestDTO;
import com.EFSRTIII.ventacursos.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
public class CompraController {

    @Autowired
    private CompraService compraService;

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