package com.idat.biblioteca.service;

import com.idat.biblioteca.entity.Libro;
import com.idat.biblioteca.entity.Prestamo;
import com.idat.biblioteca.entity.Prestamo.EstadoPrestamo;
import com.idat.biblioteca.entity.Usuario;
import com.idat.biblioteca.repository.LibroRepository;
import com.idat.biblioteca.repository.PrestamoRepository;
import com.idat.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PrestamoService {
    
    @Autowired
    private PrestamoRepository prestamoRepository;
    
    @Autowired
    private LibroRepository libroRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public List<Prestamo> listarTodos() {
        return prestamoRepository.findAll();
    }
    
    public Prestamo obtenerPorId(Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con id: " + id));
    }
    
    public Prestamo crearPrestamo(Prestamo prestamo) {
        Usuario usuario = usuarioRepository.findById(prestamo.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Libro libro = libroRepository.findById(prestamo.getLibro().getId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        
        if (libro.getCantidadDisponible() <= 0) {
            throw new RuntimeException("No hay ejemplares disponibles del libro: " + libro.getTitulo());
        }
        
        // Reducir cantidad disponible
        libro.setCantidadDisponible(libro.getCantidadDisponible() - 1);
        libroRepository.save(libro);
        
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setEstado(EstadoPrestamo.ACTIVO);
        
        return prestamoRepository.save(prestamo);
    }
    
    public Prestamo registrarDevolucion(Long id) {
        Prestamo prestamo = obtenerPorId(id);
        
        if (prestamo.getEstado() != EstadoPrestamo.ACTIVO) {
            throw new RuntimeException("El préstamo no está activo");
        }
        
        // Aumentar cantidad disponible
        Libro libro = prestamo.getLibro();
        libro.setCantidadDisponible(libro.getCantidadDisponible() + 1);
        libroRepository.save(libro);
        
        prestamo.setFechaDevolucionReal(LocalDate.now());
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        
        return prestamoRepository.save(prestamo);
    }
    
    public List<Prestamo> listarPorUsuario(Long usuarioId) {
        return prestamoRepository.findByUsuarioId(usuarioId);
    }
    
    public List<Prestamo> listarPorEstado(EstadoPrestamo estado) {
        return prestamoRepository.findByEstado(estado);
    }
    
    public void eliminarPrestamo(Long id) {
        Prestamo prestamo = obtenerPorId(id);
        
        // Si el préstamo está activo, devolver el libro
        if (prestamo.getEstado() == EstadoPrestamo.ACTIVO) {
            Libro libro = prestamo.getLibro();
            libro.setCantidadDisponible(libro.getCantidadDisponible() + 1);
            libroRepository.save(libro);
        }
        
        prestamoRepository.delete(prestamo);
    }
}
