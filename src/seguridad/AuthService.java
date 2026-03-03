package src.seguridad;

import src.modelo.Usuario;
import src.Storage.UserRepository;
import java.util.List;

public class AuthService {
    private UserRepository userRepository;
    private SessionManager sessionManager;

    public AuthService() {
        this.userRepository = UserRepository.getInstance();
        this.sessionManager = SessionManager.getInstance();
    }

    // Autenticar usuario
    public boolean autenticar(String username, String password) {
        // revisado ok

        List<Usuario> usuarios = userRepository.obtenerUsuarios();

        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
            if (usuario.verificarCredenciales(username, password)) {
                return sessionManager.iniciarSesion(usuario);
            }
        }

        registrarIntentoFallido(username);
        return false;
    }

    // Cerrar sesión
    public void cerrarSesion() {
        sessionManager.cerrarSesion();
    }

    // Verificar permisos para una operación
    public boolean tienePermiso(int operacion) {
        if (!sessionManager.isAutenticado()) {
            return false;
        }

        int rol = sessionManager.getRolUsuarioActual();
        return PermisosManager.tienePermiso(rol, operacion);
    }

    // Verificar permisos para múltiples operaciones
    public boolean tienePermisos(int[] operaciones) {
        for (int operacion : operaciones) {
            if (!tienePermiso(operacion)) {
                return false;
            }
        }
        return true;
    }

    // Cambiar contraseña
    public boolean cambiarPassword(String passwordActual, String nuevoPassword) {
        if (!sessionManager.isAutenticado()) {
            return false;
        }

        Usuario usuario = sessionManager.getUsuarioActual();
        if (usuario.getPassword().equals(passwordActual)) {
            usuario.setPassword(nuevoPassword);
            // En un sistema real, aquí actualizarías en la base de datos
            System.out.println("✅ Contraseña actualizada exitosamente");
            return true;
        }

        System.out.println("❌ Contraseña actual incorrecta");
        return false;
    }

    private void registrarIntentoFallido(String username) {
        String log = String.format("[%s] Intento fallido de inicio de sesión para: %s",
                java.time.LocalDateTime.now().toString(), username);
        System.out.println("⚠️  " + log);
    }

    // Obtener información del usuario autenticado
    public String getInfoUsuario() {
        if (sessionManager.isAutenticado()) {
            Usuario usuario = sessionManager.getUsuarioActual();
            return String.format("%s (%s) - %s",
                    usuario.getNombre(),
                    usuario.getUsername(),
                    usuario.getNombreRol());
        }
        return "No autenticado";
    }
}