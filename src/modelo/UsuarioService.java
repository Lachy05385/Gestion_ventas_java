
package src.modelo;

import src.modelo.Usuario;
import src.Storage.UserRepository;
import java.util.List;

public class UsuarioService {
    private UserRepository userRepository;

    public UsuarioService() {
        this.userRepository = UserRepository.getInstance(); // Usa Singleton
    }

    /**
     * Crea y registra un nuevo usuario
     */
    public Usuario crearUsuario(String nombre, String username, String password, int rol) {
        // Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El username no puede estar vacío");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        if (rol < 1 || rol > 3) {
            throw new IllegalArgumentException("Rol inválido. Debe ser 1, 2 o 3");
        }

        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre.trim());
        usuario.setUsername(username.trim());
        usuario.setPassword(password);
        usuario.setRol(rol);

        // Agregar al repositorio
        if (userRepository.agregarUsuario(usuario)) {
            return usuario;
        } else {
            throw new RuntimeException("No se pudo registrar el usuario. Posible username duplicado.");
        }
    }

    /**
     * Obtiene todos los usuarios
     */
    public List<Usuario> obtenerTodosUsuarios() {
        return userRepository.obtenerUsuarios();
    }

    /**
     * Busca usuario por ID
     */
    public Usuario buscarPorId(int id) {
        return userRepository.buscarPorId(id);
    }

    /**
     * Busca usuarios por nombre
     */
    public List<Usuario> buscarPorNombre(String nombre) {
        return userRepository.buscarPorNombre(nombre);
    }

    /**
     * Actualiza un usuario existente
     */
    public boolean actualizarUsuario(int id, String nombre, String username, String password, int rol) {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre(nombre);
        usuarioActualizado.setUsername(username);
        usuarioActualizado.setPassword(password);
        usuarioActualizado.setRol(rol);

        return userRepository.actualizarUsuario(id, usuarioActualizado);
    }

    /**
     * Elimina un usuario
     */
    public boolean eliminarUsuario(int id) {
        return userRepository.eliminarUsuario(id);
    }

    /**
     * Verifica si un username existe
     */
    public boolean existeUsername(String username) {
        return userRepository.existeUsername(username);
    }

    /**
     * Obtiene el total de usuarios
     */
    public int obtenerTotalUsuarios() {
        return userRepository.obtenerTotalUsuarios();
    }

    /**
     * Obtiene estadísticas por rol
     */
    public String obtenerEstadisticas() {
        int[] estadisticas = userRepository.obtenerEstadisticasPorRol();

        return String.format(
                "Total usuarios: %d\n" +
                        "• Admin General: %d\n" +
                        "• Dependiente: %d\n" +
                        "• Contador: %d\n" +
                        "• Sin definir: %d",
                estadisticas[1] + estadisticas[2] + estadisticas[3] + estadisticas[0],
                estadisticas[1], estadisticas[2], estadisticas[3], estadisticas[0]);
    }
}