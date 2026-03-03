package src.ui;

import src.seguridad.AuthService;
import java.util.Scanner;

public class LoginForm {
    private AuthService authService;
    private Scanner scanner;

    public LoginForm(Scanner scanner) {
        this.authService = new AuthService();
        this.scanner = scanner;
    }

    // Mostrar formulario de login
    public boolean mostrarLogin() {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("           🔐 SISTEMA DE INVENTARIOS - LOGIN");
        System.out.println("═".repeat(50));

        int intentos = 0;
        int maxIntentos = 3;

        while (intentos < maxIntentos) {
            System.out.println("\nIngrese sus credenciales:");
            System.out.println("(Intentos restantes: " + (maxIntentos - intentos) + ")");

            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            if (authService.autenticar(username, password)) {
                System.out.println("\n✅ ¡Autenticación exitosa!");
                mostrarBienvenida();
                return true;
            } else {
                intentos++;
                System.out.println("\n❌ Credenciales incorrectas");

                if (intentos < maxIntentos) {
                    System.out.println("Por favor, intente nuevamente.");
                } else {
                    System.out.println("\n⚠️  Demasiados intentos fallidos.");
                    System.out.println("El sistema se cerrará por seguridad.");
                }
            }
        }

        return false;
    }

    // Mostrar menú de cambio de contraseña
    public void mostrarCambioPassword() {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("           🔐 CAMBIO DE CONTRASEÑA");
        System.out.println("═".repeat(50));

        System.out.print("\nContraseña actual: ");
        String actual = scanner.nextLine().trim();

        System.out.print("Nueva contraseña: ");
        String nueva = scanner.nextLine().trim();

        System.out.print("Confirmar nueva contraseña: ");
        String confirmacion = scanner.nextLine().trim();

        if (!nueva.equals(confirmacion)) {
            System.out.println("\n❌ Las contraseñas no coinciden");
            return;
        }

        if (authService.cambiarPassword(actual, nueva)) {
            System.out.println("\n✅ Contraseña cambiada exitosamente");
        } else {
            System.out.println("\n❌ No se pudo cambiar la contraseña");
        }
    }

    private void mostrarBienvenida() {
        System.out.println("\n" + "⭐".repeat(50));
        System.out.println("        BIENVENIDO/A AL SISTEMA DE INVENTARIOS");
        System.out.println("⭐".repeat(50));
        System.out.println("\nUsuario: " + authService.getInfoUsuario());
        System.out.println("Hora de acceso: " + java.time.LocalDateTime.now());
        System.out.println("\n" + "═".repeat(50));
    }

    // Cerrar sesión
    public void cerrarSesion() {
        authService.cerrarSesion();
        System.out.println("\n✅ Sesión cerrada exitosamente");
    }
}
