package src.seguridad;

import src.modelo.Usuario;

public class SessionManager {
    private static SessionManager instancia;
    private Usuario usuarioActual;
    private boolean autenticado;

    private SessionManager() {
        this.usuarioActual = null;
        this.autenticado = false;
    }

    // Patrón Singleton
    public static SessionManager getInstance() {
        if (instancia == null) {
            instancia = new SessionManager();
        }
        return instancia;
    }

    // Iniciar sesión
    public boolean iniciarSesion(Usuario usuario) {
        if (usuario != null && usuario.isActivo()) {
            this.usuarioActual = usuario;
            this.autenticado = true;
            registrarLog("Inicio de sesión exitoso: " + usuario.getUsername());
            return true;
        }
        return false;
    }

    // Cerrar sesión
    public void cerrarSesion() {
        if (usuarioActual != null) {
            registrarLog("Cierre de sesión: " + usuarioActual.getUsername());
        }
        this.usuarioActual = null;
        this.autenticado = false;
    }

    // Verificar si hay sesión activa
    public boolean isAutenticado() {
        return autenticado;
    }

    // Obtener usuario actual
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    // Obtener rol del usuario actual
    public int getRolUsuarioActual() {
        return (usuarioActual != null) ? usuarioActual.getRol() : 0;
    }

    // Verificar si el usuario tiene un rol específico
    public boolean tieneRol(int rol) {
        return autenticado && usuarioActual != null && usuarioActual.getRol() == rol;
    }

    // Verificar si el usuario tiene al menos uno de los roles permitidos
    public boolean tieneAlgunRol(int[] rolesPermitidos) {
        if (!autenticado || usuarioActual == null)
            return false;

        for (int rol : rolesPermitidos) {
            if (usuarioActual.getRol() == rol) {
                return true;
            }
        }
        return false;
    }

    // Registrar logs de auditoría
    private void registrarLog(String mensaje) {
        String log = String.format("[%s] %s - %s",
                java.time.LocalDateTime.now().toString(),
                (usuarioActual != null) ? usuarioActual.getUsername() : "Sistema",
                mensaje);

        // Aquí podrías guardar en archivo o base de datos
        System.out.println("🔐 " + log);
    }

    // Mostrar información de sesión
    public void mostrarInfoSesion() {
        if (autenticado && usuarioActual != null) {
            System.out.println("\n═══════════════════════════════════════");
            System.out.println("        🧑‍💼 SESIÓN ACTIVA");
            System.out.println("═══════════════════════════════════════");
            System.out.println(" Usuario: " + usuarioActual.getNombre());
            System.out.println(" Rol: " + usuarioActual.getNombreRol());
            System.out.println(" Username: " + usuarioActual.getUsername());
            System.out.println("═══════════════════════════════════════");
        } else {
            System.out.println("\n⚠️  No hay sesión activa");
        }
    }
}
