package com.idat.biblioteca.controller;

import com.idat.biblioteca.entity.Prestamo;
import com.idat.biblioteca.entity.Prestamo.EstadoPrestamo;
import com.idat.biblioteca.security.UserDetailsImpl;
import com.idat.biblioteca.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {
    
    @Autowired
    private PrestamoService prestamoService;
    
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Prestamo>> listarTodos() {
        return ResponseEntity.ok(prestamoService.listarTodos());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Prestamo> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.obtenerPorId(id));
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Prestamo> crearPrestamo(@Valid @RequestBody Prestamo prestamo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoService.crearPrestamo(prestamo));
    }
    
    @PutMapping("/{id}/devolucion")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Prestamo> registrarDevolucion(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.registrarDevolucion(id));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> eliminarPrestamo(@PathVariable Long id) {
        prestamoService.eliminarPrestamo(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Prestamo>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(prestamoService.listarPorUsuario(usuarioId));
    }
    
    @GetMapping("/mis-prestamos")
    public ResponseEntity<List<Prestamo>> misPrestamos(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(prestamoService.listarPorUsuario(userDetails.getId()));
    }
    
    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Prestamo>> listarPorEstado(@PathVariable EstadoPrestamo estado) {
        return ResponseEntity.ok(prestamoService.listarPorEstado(estado));
    }
}
