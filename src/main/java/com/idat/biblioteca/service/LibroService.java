package com.idat.biblioteca.service;

import com.idat.biblioteca.entity.Libro;
import com.idat.biblioteca.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LibroService {
    
    @Autowired
    private LibroRepository libroRepository;
    
    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }
    
    public Libro obtenerPorId(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));
    }
    
    public Libro crearLibro(Libro libro) {
        if (libroRepository.existsByIsbn(libro.getIsbn())) {
            throw new RuntimeException("Ya existe un libro con el ISBN: " + libro.getIsbn());
        }
        return libroRepository.save(libro);
    }
    
    public Libro actualizarLibro(Long id, Libro libroActualizado) {
        Libro libro = obtenerPorId(id);
        
        if (!libro.getIsbn().equals(libroActualizado.getIsbn()) && 
            libroRepository.existsByIsbn(libroActualizado.getIsbn())) {
            throw new RuntimeException("Ya existe un libro con el ISBN: " + libroActualizado.getIsbn());
        }
        
        libro.setIsbn(libroActualizado.getIsbn());
        libro.setTitulo(libroActualizado.getTitulo());
        libro.setAutor(libroActualizado.getAutor());
        libro.setEditorial(libroActualizado.getEditorial());
        libro.setCategoria(libroActualizado.getCategoria());
        libro.setAnioPublicacion(libroActualizado.getAnioPublicacion());
        libro.setCantidadDisponible(libroActualizado.getCantidadDisponible());
        libro.setCantidadTotal(libroActualizado.getCantidadTotal());
        libro.setDescripcion(libroActualizado.getDescripcion());
        
        return libroRepository.save(libro);
    }
    
    public void eliminarLibro(Long id) {
        Libro libro = obtenerPorId(id);
        libroRepository.delete(libro);
    }
    
    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }
    
    public List<Libro> buscarPorAutor(String autor) {
        return libroRepository.findByAutorContainingIgnoreCase(autor);
    }
    
    public List<Libro> buscarPorCategoria(String categoria) {
        return libroRepository.findByCategoria(categoria);
    }
}
