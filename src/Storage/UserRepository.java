package src.Storage;

import java.util.ArrayList;
import java.util.List;

import src.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar operaciones de persistencia de Usuarios
 * Implementa separación clara entre lógica de negocio y almacenamiento
 */
public class UserRepository {
    private List<Usuario> usuarios;
    private static UserRepository instancia;

    /**
     * Constructor privado para implementar patrón Singleton
     */
    public UserRepository() {
        this.usuarios = new ArrayList<>();
        inicializarUsuariosDemo(); // Datos de ejemplo para pruebas
    }

    /**
     * Obtiene la instancia única del repositorio (Singleton)
     */
    public static UserRepository getInstance() {
        if (instancia == null) {
            instancia = new UserRepository();
        }
        return instancia;
    }

    /**
     * Agrega un nuevo usuario al repositorio
     * 
     * @param usuario El usuario a agregar
     * @return true si se agregó exitosamente, false si hubo error
     */
    public boolean agregarUsuario(Usuario usuario) {
        if (usuario == null) {
            System.err.println("Error: Intento de agregar usuario nulo");
            return false;
        }

        // Validar que el usuario tenga datos mínimos
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            System.err.println("Error: Usuario sin nombre válido");
            return false;
        }

        // Verificar si el username ya existe (simulación, necesitarías getUsername en
        // Usuario)
        if (existeUsername(obtenerUsername(usuario))) {
            System.err.println("Error: El username '" + obtenerUsername(usuario) + "' ya está registrado");
            return false;
        }

        // Asignar ID si no tiene
        if (usuario.getId() <= 0) {
            usuario.setId(generarNuevoId());
        }

        try {
            usuarios.add(usuario);
            System.out.println(
                    "Usuario agregado exitosamente: " + usuario.getNombre() + " (ID: " + usuario.getId() + ")");
            return true;
        } catch (Exception e) {
            System.err.println("Error al agregar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los usuarios del repositorio
     * 
     * @return Lista de usuarios (copia para evitar modificación directa)
     */
    public List<Usuario> obtenerUsuarios() {
        return new ArrayList<>(usuarios); // Retorna copia para proteger los datos originales
    }

    /**
     * Busca un usuario por su ID
     * 
     * @param id El ID del usuario a buscar
     * @return El usuario encontrado o null si no existe
     */
    public Usuario buscarPorId(int id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }
        System.out.println("Usuario con ID " + id + " no encontrado");
        return null;
    }

    /**
     * Busca usuarios por nombre (búsqueda parcial case-insensitive)
     * 
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de usuarios que coinciden
     */
    public List<Usuario> buscarPorNombre(String nombre) {
        List<Usuario> resultados = new ArrayList<>();

        if (nombre == null || nombre.trim().isEmpty()) {
            return resultados; // Lista vacía
        }

        String nombreBusqueda = nombre.trim().toLowerCase();

        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().toLowerCase().contains(nombreBusqueda)) {
                resultados.add(usuario);
            }
        }

        return resultados;
    }

    /**
     * Actualiza un usuario existente
     * 
     * @param id                 ID del usuario a actualizar
     * @param usuarioActualizado Usuario con los nuevos datos
     * @return true si se actualizó exitosamente, false si hubo error
     */
    public boolean actualizarUsuario(int id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = buscarPorId(id);

        if (usuarioExistente == null) {
            System.err.println("Error: No se puede actualizar usuario inexistente (ID: " + id + ")");
            return false;
        }

        if (usuarioActualizado == null) {
            System.err.println("Error: Datos de actualización nulos");
            return false;
        }

        // Actualizar campos (manteniendo el ID original)
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setUsername(usuarioActualizado.getUsername());
        usuarioExistente.setPassword(usuarioActualizado.getPassword());
        usuarioExistente.setRol(usuarioActualizado.getRol());

        System.out.println("Usuario actualizado exitosamente: " + usuarioExistente.getNombre());
        return true;
    }

    /**
     * Elimina un usuario por su ID
     * 
     * @param id ID del usuario a eliminar
     * @return true si se eliminó exitosamente, false si hubo error
     */
    public boolean eliminarUsuario(int id) {
        Usuario usuario = buscarPorId(id);

        if (usuario == null) {
            System.err.println("Error: No se puede eliminar usuario inexistente (ID: " + id + ")");
            return false;
        }

        try {
            usuarios.remove(usuario);
            System.out.println("Usuario eliminado exitosamente: " + usuario.getNombre() + " (ID: " + id + ")");
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el total de usuarios registrados
     * 
     * @return Número total de usuarios
     */
    public int obtenerTotalUsuarios() {
        return usuarios.size();
    }

    /**
     * Verifica si existe un usuario con el username especificado
     * 
     * @param username Username a verificar
     * @return true si existe, false si no
     */
    public boolean existeUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        String usernameBusqueda = username.trim().toLowerCase();

        for (Usuario usuario : usuarios) {
            String usuarioUsername = obtenerUsername(usuario);
            if (usuarioUsername != null && usuarioUsername.toLowerCase().equals(usernameBusqueda)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Obtiene estadísticas de usuarios por rol
     * 
     * @return Array donde índice = código de rol, valor = cantidad de usuarios
     */
    public int[] obtenerEstadisticasPorRol() {
        int[] estadisticas = new int[4]; // Índices 0-3 para roles 0-3

        for (Usuario usuario : usuarios) {
            int rol = usuario.getRol();
            if (rol >= 0 && rol < estadisticas.length) {
                estadisticas[rol]++;
            }
        }

        return estadisticas;
    }

    /**
     * Obtiene el siguiente ID disponible
     * 
     * @return Nuevo ID único
     */
    private int generarNuevoId() {
        int maxId = 0;

        for (Usuario usuario : usuarios) {
            if (usuario.getId() > maxId) {
                maxId = usuario.getId();
            }
        }

        return maxId + 1;
    }

    /**
     * Método auxiliar para obtener username de un usuario
     * (Temporal hasta que Usuario tenga getUsername())
     */
    private String obtenerUsername(Usuario usuario) {
        // Intenta obtener username directamente si el método existe
        try {
            // Si Usuario tiene getUsername()
            return usuario.getUsername();
        } catch (Exception e) {
            // Si no, genera uno basado en el nombre
            return "user_" + usuario.getId();
        }
    }

    /**
     * Inicializa algunos usuarios de ejemplo para pruebas
     */
    private void inicializarUsuariosDemo() {

        // Solo inicializa si la lista está vacía
        if (!usuarios.isEmpty()) {
            return;
        }

        System.out.println("Inicializando usuarios de demostración...");

        // Usuario 1
        Usuario admin = new Usuario();
        admin.setId(1);
        admin.setNombre("Admin Principal");
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setRol(1);
        admin.setActivo(true);

        usuarios.add(admin);

        // Usuario 2
        Usuario dependiente = new Usuario();
        dependiente.setId(2);
        dependiente.setNombre("Carlos Martínez");
        dependiente.setUsername("carlos_m");
        dependiente.setPassword("clave123");
        dependiente.setRol(2);
        dependiente.setActivo(true);
        usuarios.add(dependiente);

        // Usuario 3
        Usuario contador = new Usuario();
        contador.setId(3);
        contador.setNombre("Ana López");
        contador.setUsername("ana_l");
        contador.setPassword("contador456");
        contador.setRol(3);
        contador.setActivo(true);
        usuarios.add(contador);

        // Usuario 4
        Usuario dependiente2 = new Usuario();
        dependiente2.setId(4);
        dependiente2.setNombre("Pedro González");
        dependiente2.setUsername("pedro_g");
        dependiente2.setPassword("ventas789");
        dependiente2.setRol(2);
        dependiente2.setActivo(true);
        usuarios.add(dependiente2);

        System.out.println("✓ " + usuarios.size() + " usuarios de demostración inicializados");
    }

    /**
     * Limpia todos los usuarios (solo para pruebas/reset)
     * 
     * @return true si se limpió exitosamente
     */
    public boolean limpiarRepositorio() {
        try {
            int total = usuarios.size();
            usuarios.clear();
            System.out.println("Repositorio limpiado. " + total + " usuarios eliminados.");
            return true;
        } catch (Exception e) {
            System.err.println("Error al limpiar repositorio: " + e.getMessage());
            return false;
        }
    }

    /**
     * Exporta usuarios a formato texto (para reportes)
     * 
     * @return String con todos los usuarios formateados
     */
    public String exportarUsuariosTexto() {
        StringBuilder sb = new StringBuilder();

        sb.append("=== LISTA DE USUARIOS ===\n");
        sb.append("Total: ").append(usuarios.size()).append("\n\n");

        for (Usuario usuario : usuarios) {
            sb.append("ID: ").append(usuario.getId()).append("\n");
            sb.append("Nombre: ").append(usuario.getNombre()).append("\n");
            sb.append("Username: ").append(obtenerUsername(usuario)).append("\n");
            sb.append("Rol: ").append(obtenerNombreRol(usuario.getRol())).append("\n");
            sb.append("----------------------------\n");
        }

        return sb.toString();
    }

    /**
     * Convierte código de rol a nombre descriptivo
     */
    private String obtenerNombreRol(int rol) {
        switch (rol) {
            case 1:
                return "Admin General";
            case 2:
                return "Dependiente";
            case 3:
                return "Contador";
            default:
                return "Sin definir";
        }
    }
}