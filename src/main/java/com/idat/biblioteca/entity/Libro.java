package com.idat.biblioteca.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "libros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El ISBN es obligatorio")
    @Size(min = 10, max = 20, message = "El ISBN debe tener entre 10 y 20 caracteres")
    @Column(nullable = false, unique = true, length = 20)
    private String isbn;
    
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no debe exceder 200 caracteres")
    @Column(nullable = false, length = 200)
    private String titulo;
    
    @NotBlank(message = "El autor es obligatorio")
    @Size(max = 100, message = "El autor no debe exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String autor;
    
    @Column(length = 100)
    private String editorial;
    
    @Column(length = 50)
    private String categoria;
    
    @NotNull(message = "El año de publicación es obligatorio")
    @Min(value = 1900, message = "El año debe ser mayor a 1900")
    @Column(nullable = false)
    private Integer anioPublicacion;
    
    @NotNull(message = "La cantidad disponible es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @Column(nullable = false)
    private Integer cantidadDisponible;
    
    @NotNull(message = "La cantidad total es obligatoria")
    @Min(value = 1, message = "Debe haber al menos 1 ejemplar")
    @Column(nullable = false)
    private Integer cantidadTotal;
    
    @Column(length = 500)
    private String descripcion;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    
    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL)
    private Set<Prestamo> prestamos = new HashSet<>();
}
