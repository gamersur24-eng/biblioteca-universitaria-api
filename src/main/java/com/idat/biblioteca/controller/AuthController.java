package com.idat.biblioteca.controller;

import com.idat.biblioteca.dto.JwtResponse;
import com.idat.biblioteca.dto.LoginRequest;
import com.idat.biblioteca.dto.MessageResponse;
import com.idat.biblioteca.dto.RegisterRequest;
import com.idat.biblioteca.entity.Rol;
import com.idat.biblioteca.entity.Usuario;
import com.idat.biblioteca.repository.RolRepository;
import com.idat.biblioteca.repository.UsuarioRepository;
import com.idat.biblioteca.security.JwtUtils;
import com.idat.biblioteca.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        if (usuarioRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: El username ya está en uso"));
        }
        
        if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: El email ya está en uso"));
        }
        
        Usuario usuario = new Usuario();
        usuario.setUsername(signUpRequest.getUsername());
        usuario.setEmail(signUpRequest.getEmail());
        usuario.setPassword(encoder.encode(signUpRequest.getPassword()));
        usuario.setNombreCompleto(signUpRequest.getNombreCompleto());
        usuario.setTelefono(signUpRequest.getTelefono());
        
        Set<String> strRoles = signUpRequest.getRoles();
        Set<Rol> roles = new HashSet<>();
        
        if (strRoles == null || strRoles.isEmpty()) {
            Rol userRole = rolRepository.findByNombre("USUARIO")
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Rol rol = rolRepository.findByNombre(role)
                        .orElseThrow(() -> new RuntimeException("Error: Rol " + role + " no encontrado"));
                roles.add(rol);
            });
        }
        
        usuario.setRoles(roles);
        usuarioRepository.save(usuario);
        
        return ResponseEntity.ok(new MessageResponse("Usuario registrado exitosamente"));
    }
}
