package com.idat.biblioteca.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prestamos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @NotNull(message = "El libro es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;
    
    @NotNull(message = "La fecha de préstamo es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaPrestamo;
    
    @NotNull(message = "La fecha de devolución prevista es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaDevolucionPrevista;
    
    @Column
    private LocalDate fechaDevolucionReal;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPrestamo estado = EstadoPrestamo.ACTIVO;
    
    @Column(length = 200)
    private String observaciones;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    
    public enum EstadoPrestamo {
        ACTIVO,
        DEVUELTO,
        VENCIDO,
        CANCELADO
    }
}
