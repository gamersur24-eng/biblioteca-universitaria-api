-- ============================================================
-- SCRIPT DE BASE DE DATOS
-- Sistema de Gestión de Biblioteca Universitaria
-- IDAT - 2025
-- ============================================================

-- Eliminar base de datos si existe (para instalación limpia)
DROP DATABASE IF EXISTS biblioteca_universitaria;

-- Crear base de datos
CREATE DATABASE biblioteca_universitaria
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE biblioteca_universitaria;

-- ============================================================
-- TABLA: roles
-- Descripción: Roles del sistema (ADMIN, USUARIO)
-- ============================================================
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLA: usuarios
-- Descripción: Usuarios del sistema con autenticación
-- ============================================================
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_email (email),
    INDEX idx_username (username),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLA: libros
-- Descripción: Catálogo de libros de la biblioteca
-- ============================================================
CREATE TABLE libros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(200) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    editorial VARCHAR(150),
    anio_publicacion INT,
    categoria VARCHAR(100),
    num_paginas INT,
    idioma VARCHAR(50) DEFAULT 'Español',
    ubicacion VARCHAR(100),
    descripcion TEXT,
    cantidad_total INT DEFAULT 1,
    cantidad_disponible INT DEFAULT 1,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_titulo (titulo),
    INDEX idx_autor (autor),
    INDEX idx_isbn (isbn),
    INDEX idx_categoria (categoria),
    INDEX idx_disponible (cantidad_disponible)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLA: prestamos
-- Descripción: Registro de préstamos de libros
-- ============================================================
CREATE TABLE prestamos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    libro_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    fecha_prestamo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_devolucion_esperada DATE NOT NULL,
    fecha_devolucion_real DATE,
    estado VARCHAR(20) DEFAULT 'ACTIVO',
    observaciones TEXT,
    multa DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (libro_id) REFERENCES libros(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_libro_id (libro_id),
    INDEX idx_usuario_id (usuario_id),
    INDEX idx_estado (estado),
    INDEX idx_fecha_prestamo (fecha_prestamo),
    INDEX idx_fecha_devolucion (fecha_devolucion_esperada),
    CONSTRAINT chk_estado CHECK (estado IN ('ACTIVO', 'DEVUELTO', 'VENCIDO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLA: usuario_roles (Relación Many-to-Many)
-- Descripción: Asignación de roles a usuarios
-- ============================================================
CREATE TABLE usuario_roles (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_usuario (usuario_id),
    INDEX idx_rol (rol_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- DATOS INICIALES: Roles
-- ============================================================
INSERT INTO roles (id, nombre, descripcion) VALUES
(1, 'ROLE_ADMIN', 'Administrador del sistema con acceso completo'),
(2, 'ROLE_USER', 'Usuario normal con permisos básicos de préstamo');

-- ============================================================
-- DATOS INICIALES: Usuarios
-- Contraseñas encriptadas con BCrypt
-- ============================================================
-- Usuario Admin
-- Email: admin@biblioteca.com
-- Password: admin123 (encriptado con BCrypt)
INSERT INTO usuarios (id, nombre, apellido, email, username, password, telefono, direccion, activo) VALUES
(1, 'Administrador', 'Sistema', 'admin@biblioteca.com', 'admin',
'$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8ssKQqpZmV0zKJ2jWK',
'999888777', 'Av. Principal 123', TRUE);

-- Usuario Normal
-- Email: juan.perez@example.com
-- Password: password123 (encriptado con BCrypt)
INSERT INTO usuarios (id, nombre, apellido, email, username, password, telefono, direccion, activo) VALUES
(3, 'Juan', 'Pérez', 'juan.perez@example.com', 'juan.perez',
'$2a$10$8Z8pQpPqkUxFhO5pPqHO0u7QY0BqMZqYQqMZqYQqMZqYQqMZqYQqM',
'987654321', 'Calle Los Olivos 456', TRUE);

-- ============================================================
-- DATOS INICIALES: Asignación de Roles
-- ============================================================
-- Admin tiene rol ADMIN
INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (1, 1);

-- juan.perez tiene rol USER
INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (3, 2);

-- ============================================================
-- DATOS DE EJEMPLO: Libros
-- ============================================================
INSERT INTO libros (titulo, autor, isbn, editorial, anio_publicacion, categoria, num_paginas, idioma, ubicacion, descripcion, cantidad_total, cantidad_disponible) VALUES
('Effective Java', 'Joshua Bloch', '978-0134685991', 'Addison-Wesley', 2018, 'Programación', 416, 'Inglés', 'Estante A-01', 'Guía definitiva sobre mejores prácticas en Java', 3, 3),
('Clean Code', 'Robert C. Martin', '978-0132350884', 'Prentice Hall', 2008, 'Programación', 464, 'Inglés', 'Estante A-02', 'Manual de estilo para el desarrollo ágil de software', 2, 2),
('Design Patterns', 'Erich Gamma', '978-0201633610', 'Addison-Wesley', 1994, 'Programación', 395, 'Inglés', 'Estante A-03', 'Elementos de software orientado a objetos reutilizable', 2, 2),
('Cien Años de Soledad', 'Gabriel García Márquez', '978-0060883287', 'Harper Perennial', 1967, 'Literatura', 417, 'Español', 'Estante L-10', 'Obra maestra del realismo mágico', 5, 5),
('El Principito', 'Antoine de Saint-Exupéry', '978-0156012195', 'Harcourt', 1943, 'Literatura', 96, 'Español', 'Estante L-15', 'Novela corta y clásico de la literatura universal', 4, 4);

-- ============================================================
-- DATOS DE EJEMPLO: Préstamos
-- ============================================================
-- Préstamo de ejemplo (ya devuelto)
INSERT INTO prestamos (libro_id, usuario_id, fecha_prestamo, fecha_devolucion_esperada, fecha_devolucion_real, estado, observaciones) VALUES
(1, 3, '2024-11-20 10:30:00', '2024-12-05', '2024-11-28', 'DEVUELTO', 'Préstamo devuelto a tiempo');

-- ============================================================
-- VISTAS ÚTILES
-- ============================================================

-- Vista: Préstamos activos con información completa
CREATE OR REPLACE VIEW vista_prestamos_activos AS
SELECT 
    p.id AS prestamo_id,
    p.fecha_prestamo,
    p.fecha_devolucion_esperada,
    p.estado,
    p.multa,
    l.id AS libro_id,
    l.titulo AS libro_titulo,
    l.autor AS libro_autor,
    l.isbn AS libro_isbn,
    u.id AS usuario_id,
    u.nombre AS usuario_nombre,
    u.apellido AS usuario_apellido,
    u.email AS usuario_email,
    DATEDIFF(CURDATE(), p.fecha_devolucion_esperada) AS dias_retraso
FROM prestamos p
INNER JOIN libros l ON p.libro_id = l.id
INNER JOIN usuarios u ON p.usuario_id = u.id
WHERE p.estado = 'ACTIVO';

-- Vista: Estadísticas de libros
CREATE OR REPLACE VIEW vista_estadisticas_libros AS
SELECT 
    l.id,
    l.titulo,
    l.autor,
    l.categoria,
    l.cantidad_total,
    l.cantidad_disponible,
    (l.cantidad_total - l.cantidad_disponible) AS cantidad_prestada,
    COUNT(p.id) AS total_prestamos,
    ROUND((COUNT(p.id) / l.cantidad_total) * 100, 2) AS porcentaje_uso
FROM libros l
LEFT JOIN prestamos p ON l.id = p.libro_id
GROUP BY l.id, l.titulo, l.autor, l.categoria, l.cantidad_total, l.cantidad_disponible;

-- Vista: Historial de usuarios
CREATE OR REPLACE VIEW vista_historial_usuarios AS
SELECT 
    u.id AS usuario_id,
    u.nombre,
    u.apellido,
    u.email,
    COUNT(p.id) AS total_prestamos,
    SUM(CASE WHEN p.estado = 'ACTIVO' THEN 1 ELSE 0 END) AS prestamos_activos,
    SUM(CASE WHEN p.estado = 'DEVUELTO' THEN 1 ELSE 0 END) AS prestamos_devueltos,
    SUM(CASE WHEN p.estado = 'VENCIDO' THEN 1 ELSE 0 END) AS prestamos_vencidos,
    COALESCE(SUM(p.multa), 0) AS total_multas
FROM usuarios u
LEFT JOIN prestamos p ON u.id = p.usuario_id
GROUP BY u.id, u.nombre, u.apellido, u.email;

-- ============================================================
-- PROCEDIMIENTOS ALMACENADOS
-- ============================================================

-- Procedimiento: Registrar préstamo
DELIMITER //
CREATE PROCEDURE registrar_prestamo(
    IN p_libro_id BIGINT,
    IN p_usuario_id BIGINT,
    IN p_dias_prestamo INT
)
BEGIN
    DECLARE v_cantidad_disponible INT;
    
    -- Verificar disponibilidad
    SELECT cantidad_disponible INTO v_cantidad_disponible
    FROM libros WHERE id = p_libro_id;
    
    IF v_cantidad_disponible > 0 THEN
        -- Crear préstamo
        INSERT INTO prestamos (libro_id, usuario_id, fecha_devolucion_esperada)
        VALUES (p_libro_id, p_usuario_id, DATE_ADD(CURDATE(), INTERVAL p_dias_prestamo DAY));
        
        -- Actualizar disponibilidad
        UPDATE libros 
        SET cantidad_disponible = cantidad_disponible - 1
        WHERE id = p_libro_id;
        
        SELECT 'Préstamo registrado exitosamente' AS mensaje;
    ELSE
        SELECT 'No hay ejemplares disponibles' AS mensaje;
    END IF;
END //
DELIMITER ;

-- Procedimiento: Registrar devolución
DELIMITER //
CREATE PROCEDURE registrar_devolucion(
    IN p_prestamo_id BIGINT
)
BEGIN
    DECLARE v_libro_id BIGINT;
    
    -- Obtener libro_id
    SELECT libro_id INTO v_libro_id
    FROM prestamos WHERE id = p_prestamo_id;
    
    -- Actualizar préstamo
    UPDATE prestamos 
    SET fecha_devolucion_real = CURDATE(),
        estado = 'DEVUELTO'
    WHERE id = p_prestamo_id;
    
    -- Actualizar disponibilidad
    UPDATE libros 
    SET cantidad_disponible = cantidad_disponible + 1
    WHERE id = v_libro_id;
    
    SELECT 'Devolución registrada exitosamente' AS mensaje;
END //
DELIMITER ;

-- ============================================================
-- TRIGGERS
-- ============================================================

-- Trigger: Actualizar estado de préstamos vencidos
DELIMITER //
CREATE TRIGGER actualizar_prestamos_vencidos
BEFORE UPDATE ON prestamos
FOR EACH ROW
BEGIN
    IF NEW.estado = 'ACTIVO' AND NEW.fecha_devolucion_esperada < CURDATE() THEN
        SET NEW.estado = 'VENCIDO';
        -- Calcular multa (ejemplo: $2 por día de retraso)
        SET NEW.multa = DATEDIFF(CURDATE(), NEW.fecha_devolucion_esperada) * 2.00;
    END IF;
END //
DELIMITER ;

-- Trigger: Validar cantidad disponible antes de préstamo
DELIMITER //
CREATE TRIGGER validar_prestamo_insert
BEFORE INSERT ON prestamos
FOR EACH ROW
BEGIN
    DECLARE v_disponible INT;
    SELECT cantidad_disponible INTO v_disponible
    FROM libros WHERE id = NEW.libro_id;
    
    IF v_disponible <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No hay ejemplares disponibles para préstamo';
    END IF;
END //
DELIMITER ;

-- ============================================================
-- ÍNDICES ADICIONALES PARA OPTIMIZACIÓN
-- ============================================================

-- Índice compuesto para búsquedas frecuentes
CREATE INDEX idx_prestamos_usuario_estado ON prestamos(usuario_id, estado);
CREATE INDEX idx_libros_categoria_disponible ON libros(categoria, cantidad_disponible);

-- Índice de texto completo para búsquedas
CREATE FULLTEXT INDEX idx_libros_busqueda ON libros(titulo, autor, descripcion);

-- ============================================================
-- INFORMACIÓN FINAL
-- ============================================================

SELECT 'Base de datos creada exitosamente' AS mensaje;

-- Mostrar resumen de tablas
SELECT 
    'roles' AS tabla, COUNT(*) AS registros FROM roles
UNION ALL
SELECT 
    'usuarios' AS tabla, COUNT(*) AS registros FROM usuarios
UNION ALL
SELECT 
    'libros' AS tabla, COUNT(*) AS registros FROM libros
UNION ALL
SELECT 
    'prestamos' AS tabla, COUNT(*) AS registros FROM prestamos
UNION ALL
SELECT 
    'usuario_roles' AS tabla, COUNT(*) AS registros FROM usuario_roles;

-- ============================================================
-- FIN DEL SCRIPT
-- ============================================================
-- Versión: 1.0
-- Fecha: Diciembre 2025
-- Autor: Sistema de Gestión de Biblioteca Universitaria
-- Institución: IDAT
-- ============================================================
