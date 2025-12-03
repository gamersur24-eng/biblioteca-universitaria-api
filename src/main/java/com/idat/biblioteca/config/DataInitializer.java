package com.idat.biblioteca.config;

import com.idat.biblioteca.entity.Rol;
import com.idat.biblioteca.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private RolRepository rolRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        if (!rolRepository.existsByNombre("ADMIN")) {
            Rol adminRole = new Rol();
            adminRole.setNombre("ADMIN");
            adminRole.setDescripcion("Administrador del sistema");
            rolRepository.save(adminRole);
        }
        
        if (!rolRepository.existsByNombre("USUARIO")) {
            Rol userRole = new Rol();
            userRole.setNombre("USUARIO");
            userRole.setDescripcion("Usuario estándar");
            rolRepository.save(userRole);
        }
        
        System.out.println("Roles inicializados correctamente");
    }
}
