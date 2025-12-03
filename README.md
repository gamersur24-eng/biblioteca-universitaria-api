# 📚 Sistema de Gestión de Biblioteca Universitaria - API REST

## 📋 Descripción del Proyecto

Sistema de gestión de biblioteca universitaria desarrollado con Spring Boot que permite administrar libros, usuarios y préstamos. Implementa autenticación JWT y control de acceso basado en roles (ADMIN y USUARIO).

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation
- **MySQL 8.0** - Base de datos relacional
- **JWT (JSON Web Tokens)** - Autenticación y autorización
- **Lombok** - Reducción de código boilerplate
- **Maven** - Gestión de dependencias
- **Jackson Hibernate Module** - Serialización JSON de entidades JPA

## ✅ Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- **Java JDK 17** o superior
- **MySQL Server 8.0** o superior
- **Maven 3.6+** (opcional, el proyecto incluye Maven Wrapper)
- **IDE:** IntelliJ IDEA, Eclipse, o VS Code
- **Postman** (para pruebas de API)

## 🗄️ Configuración de Base de Datos

### 1. Instalar MySQL 8.0

Descarga e instala MySQL desde: https://dev.mysql.com/downloads/installer/

### 2. Crear la Base de Datos

Abre MySQL Workbench o la consola de MySQL y ejecuta:

```sql
CREATE DATABASE biblioteca_universitaria;
```

### 3. Configurar Credenciales

El archivo `application.properties` está configurado con:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca_universitaria
spring.datasource.username=root
spring.datasource.password=root
```

**Importante:** Si tu MySQL usa credenciales diferentes, modifica el archivo `src/main/resources/application.properties`

## 🚀 Instalación y Ejecución

### Opción 1: Usando IntelliJ IDEA (Recomendado)

1. **Clonar o descargar el proyecto**
   ```bash
   git clone <url-del-repositorio>
   cd biblioteca-universitaria-api
   ```

2. **Abrir en IntelliJ IDEA**
   - File → Open → Seleccionar la carpeta del proyecto
   - Esperar a que Maven descargue las dependencias

3. **Configurar MySQL**
   - Asegurarse de que el servicio MySQL esté corriendo
   - Verificar que la base de datos `biblioteca_universitaria` exista

4. **Ejecutar la aplicación**
   - Abrir: `src/main/java/com/idat/biblioteca/BibliotecaUniversitariaApplication.java`
   - Clic derecho → Run 'BibliotecaUniversitariaApplication'
   - O presionar el botón verde ▶️

5. **Verificar que arrancó correctamente**
   ```
   Started BibliotecaUniversitariaApplication in X.XXX seconds
   Tomcat started on port 9000
   Roles inicializados correctamente
   ```

### Opción 2: Usando Maven desde Terminal

```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

### Opción 3: Generar JAR ejecutable

```bash
# Generar el JAR
mvn clean package

# Ejecutar el JAR
java -jar target/biblioteca-universitaria-api-1.0.0.jar
```

## 📡 API Endpoints

La API está disponible en: `http://localhost:9000`

### 🔐 Autenticación

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Registrar nuevo usuario | No |
| POST | `/api/auth/login` | Iniciar sesión y obtener token JWT | No |

### 📚 Gestión de Libros

| Método | Endpoint | Descripción | Rol Requerido |
|--------|----------|-------------|---------------|
| POST | `/api/libros` | Crear nuevo libro | ADMIN |
| GET | `/api/libros` | Listar todos los libros | Cualquiera |
| GET | `/api/libros/{id}` | Obtener libro por ID | Cualquiera |
| PUT | `/api/libros/{id}` | Actualizar libro | ADMIN |
| DELETE | `/api/libros/{id}` | Eliminar libro | ADMIN |
| GET | `/api/libros/buscar/titulo` | Buscar libros por título | Cualquiera |
| GET | `/api/libros/buscar/autor` | Buscar libros por autor | Cualquiera |
| GET | `/api/libros/categoria/{categoria}` | Filtrar por categoría | Cualquiera |

### 📖 Gestión de Préstamos

| Método | Endpoint | Descripción | Rol Requerido |
|--------|----------|-------------|---------------|
| POST | `/api/prestamos` | Crear nuevo préstamo | ADMIN |
| GET | `/api/prestamos` | Listar todos los préstamos | ADMIN |
| GET | `/api/prestamos/{id}` | Obtener préstamo por ID | ADMIN |
| GET | `/api/prestamos/mis-prestamos` | Listar préstamos del usuario autenticado | ADMIN, USUARIO |
| PUT | `/api/prestamos/{id}/devolucion` | Registrar devolución de libro | ADMIN |
| GET | `/api/prestamos/estado/{estado}` | Filtrar préstamos por estado | ADMIN |

## 🔑 Autenticación con JWT

### 1. Registrar un usuario ADMIN

```bash
POST http://localhost:9000/api/auth/register
Content-Type: application/json

{
  "username": "admin",
  "email": "admin@biblioteca.com",
  "password": "admin123",
  "nombreCompleto": "Administrador Sistema",
  "telefono": "999888777",
  "roles": ["ADMIN"]
}
```

### 2. Iniciar sesión

```bash
POST http://localhost:9000/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@biblioteca.com",
  "roles": ["ADMIN"]
}
```

### 3. Usar el token en peticiones protegidas

Agregar el header en todas las peticiones protegidas:

```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

**Nota:** El token expira después de 24 horas.

## 📊 Modelo de Datos

### Entidades Principales

#### 👤 Usuario
- id (Long, PK)
- username (String, único)
- email (String, único)
- password (String, encriptado)
- nombreCompleto (String)
- telefono (String)
- activo (Boolean)
- fechaRegistro (LocalDateTime)
- roles (Set<Rol>, ManyToMany)

#### 🎭 Rol
- id (Long, PK)
- nombre (String, único): ADMIN, USUARIO
- descripcion (String)

#### 📕 Libro
- id (Long, PK)
- isbn (String, único)
- titulo (String)
- autor (String)
- editorial (String)
- categoria (String)
- anioPublicacion (Integer)
- cantidadDisponible (Integer)
- cantidadTotal (Integer)
- descripcion (String)
- fechaRegistro (LocalDateTime)

#### 📋 Prestamo
- id (Long, PK)
- usuario (Usuario, ManyToOne)
- libro (Libro, ManyToOne)
- fechaPrestamo (LocalDate)
- fechaDevolucionPrevista (LocalDate)
- fechaDevolucionReal (LocalDate)
- estado (EstadoPrestamo): ACTIVO, DEVUELTO, VENCIDO, CANCELADO
- observaciones (String)

### Relaciones

- Usuario ↔ Rol: ManyToMany
- Prestamo → Usuario: ManyToOne
- Prestamo → Libro: ManyToOne

## 🧪 Pruebas con Postman

### Importar Colección

1. Abrir Postman
2. Import → Upload Files
3. Seleccionar `Biblioteca-Postman-Collection.json`
4. La colección incluye 15 peticiones pre-configuradas

### Variables de Colección

- `baseUrl`: http://localhost:9000
- `token`: (Se actualiza después del login)

### Secuencia de Pruebas Recomendada

1. **Registrar Admin** → Crear usuario administrador
2. **Login Admin** → Obtener token JWT
3. **Crear Libro** → Agregar libro al sistema
4. **Listar Libros** → Verificar que el libro fue creado
5. **Registrar Usuario Normal** → Crear usuario estándar
6. **Crear Préstamo** → Registrar préstamo de libro
7. **Listar Préstamos** → Ver todos los préstamos
8. **Registrar Devolución** → Marcar libro como devuelto
9. **Buscar por Título** → Probar búsqueda
10. **Error Sin Token** → Verificar seguridad (debe dar 401)

## 🔒 Seguridad

### Características Implementadas

- **Autenticación JWT:** Tokens seguros con expiración de 24 horas
- **Encriptación de Contraseñas:** BCrypt para hash de passwords
- **Control de Acceso:** Basado en roles (ADMIN, USUARIO)
- **CORS:** Configurado para desarrollo
- **Validaciones:** Bean Validation en todos los DTOs

### Configuración de Seguridad

- **Endpoints públicos:** `/api/auth/**`
- **Endpoints protegidos:** `/api/libros/**`, `/api/prestamos/**`
- **Filtro JWT:** Valida token en cada petición protegida

## 📝 Configuración Adicional

### Cambiar Puerto del Servidor

Editar `application.properties`:

```properties
server.port=9000
```

### Configurar JWT Secret

```properties
jwt.secret=tu-clave-secreta-muy-larga-y-segura
jwt.expiration=86400000
```

### Nivel de Logs

```properties
logging.level.org.springframework.security=DEBUG
logging.level.com.idat.biblioteca=DEBUG
```

## 🐛 Solución de Problemas Comunes

### Error: "Port 9000 was already in use"

**Solución:** Cambiar el puerto en `application.properties` o detener el proceso que usa el puerto 9000.

### Error: "Access denied for user 'root'@'localhost'"

**Solución:** Verificar credenciales de MySQL en `application.properties`.

### Error: "Unable to connect to MySQL at localhost:3306"

**Solución:** 
1. Verificar que MySQL esté corriendo: `services.msc` → MySQL80
2. Verificar que la base de datos existe
3. Probar conexión en MySQL Workbench

### Error 401 Unauthorized en Postman

**Solución:**
1. Verificar que el token esté en el header Authorization
2. Formato correcto: `Bearer <token>`
3. Token no debe estar vencido (24 horas)

### Error: "Type definition error: ByteBuddyInterceptor"

**Solución:** Este error fue solucionado agregando la dependencia `jackson-datatype-hibernate5-jakarta` y creando `JacksonConfig.java`

## 👥 Roles y Permisos

### Rol ADMIN
- ✅ Crear, editar y eliminar libros
- ✅ Crear y gestionar préstamos
- ✅ Ver todos los préstamos del sistema
- ✅ Registrar devoluciones
- ✅ Acceso completo a todos los endpoints

### Rol USUARIO
- ✅ Ver listado de libros
- ✅ Buscar libros
- ✅ Ver sus propios préstamos
- ✅  crear/editar libros
- ✅ crear préstamos (solo ADMIN)
- ✅  ver préstamos de otros usuarios

## 📦 Estructura del Proyecto

```
src/main/java/com/idat/biblioteca/
├── config/
│   ├── JacksonConfig.java          # Configuración Jackson/Hibernate
│   └── WebSecurityConfig.java      # Configuración de seguridad
├── controller/
│   ├── AuthController.java         # Autenticación
│   ├── LibroController.java        # Gestión de libros
│   └── PrestamoController.java     # Gestión de préstamos
├── dto/
│   ├── AuthRequest.java
│   ├── AuthResponse.java
│   ├── LibroRequest.java
│   └── PrestamoRequest.java
├── model/
│   ├── Usuario.java
│   ├── Rol.java
│   ├── Libro.java
│   └── Prestamo.java
├── repository/
│   ├── UsuarioRepository.java
│   ├── RolRepository.java
│   ├── LibroRepository.java
│   └── PrestamoRepository.java
├── security/
│   ├── AuthEntryPointJwt.java
│   ├── AuthTokenFilter.java
│   ├── JwtUtils.java
│   └── UserDetailsServiceImpl.java
├── service/
│   ├── LibroService.java
│   ├── PrestamoService.java
│   └── DataInitializer.java
└── BibliotecaUniversitariaApplication.java
```

## 📚 Documentación Adicional

- **Colección Postman:** `Biblioteca-Postman-Collection.json`
- **Informe Técnico:** Incluye capturas de pruebas y diagramas
- **Diagrama ER:** Modelo de base de datos en MySQL Workbench


