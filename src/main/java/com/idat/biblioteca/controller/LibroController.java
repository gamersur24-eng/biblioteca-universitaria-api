package com.idat.biblioteca.controller;

import com.idat.biblioteca.entity.Libro;
import com.idat.biblioteca.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/libros")
public class LibroController {
    
    @Autowired
    private LibroService libroService;
    
    @GetMapping
    public ResponseEntity<List<Libro>> listarTodos() {
        return ResponseEntity.ok(libroService.listarTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.obtenerPorId(id));
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Libro> crearLibro(@Valid @RequestBody Libro libro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(libroService.crearLibro(libro));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable Long id, @Valid @RequestBody Libro libro) {
        return ResponseEntity.ok(libroService.actualizarLibro(id, libro));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> eliminarLibro(@PathVariable Long id) {
        libroService.eliminarLibro(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<Libro>> buscarPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(libroService.buscarPorTitulo(titulo));
    }
    
    @GetMapping("/buscar/autor")
    public ResponseEntity<List<Libro>> buscarPorAutor(@RequestParam String autor) {
        return ResponseEntity.ok(libroService.buscarPorAutor(autor));
    }
    
    @GetMapping("/buscar/categoria")
    public ResponseEntity<List<Libro>> buscarPorCategoria(@RequestParam String categoria) {
        return ResponseEntity.ok(libroService.buscarPorCategoria(categoria));
    }
}
