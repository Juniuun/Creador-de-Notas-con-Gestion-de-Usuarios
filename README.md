# README - Sistema de Gestión de Notas Personales

## Descripción del Proyecto
Sistema de notas personales con autenticación segura que permite crear, editar, buscar y eliminar notas. Cada usuario tiene su propio espacio de almacenamiento.

## Características Principales
- ✅ Registro/login con validación de email y contraseña segura (SHA-256)
- 📝 CRUD completo de notas (Crear, Leer, Actualizar, Eliminar)
- 🔍 Búsqueda instantánea por título
- 📂 Almacenamiento persistente en archivos por usuario

## Requisitos
- Java JDK 8+
- Cualquier SO compatible con Java Swing

## Instrucciones
1. **Ejecución**:  
   `java Main`

2. **Registro**:  
   - Email válido (ej: usuario@dominio.com)  
   - Contraseña (6+ caracteres)  
   - Botón "Registrarse"

3. **Uso**:  
   - **Guardar**: Título + contenido → "Guardar"  
   - **Editar**: Seleccionar nota → Modificar → "Editar"  
   - **Buscar**: Escribir término → "Buscar"  
   - **Doble clic** en lista para cargar notas

## Estructura de Archivos

- **src/**: Contiene el código fuente
  - **jbl/**: Paquete principal
    - `CreadorNotas.java`: Interfaz de gestión de notas
    - `LoginManager.java`: Sistema de autenticación
  - `Main.java`: Punto de entrada
- **Usuarios/**: Directorio de usuarios
  - **email_usuario/** (ej: ejemplo1@gmail.com)
    - Archivos `.txt`: Notas del usuario
- `users.txt`: Base de datos de usuarios (email:contraseña_hasheada)


## Autor
Sara Alonso Perdomo  
28/03/2025