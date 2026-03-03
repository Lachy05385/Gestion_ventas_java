package src.modelo; // O src.ui dependiendo de tu paquete

//import src.modelo.UsuarioService;
import java.util.List;
import java.util.Scanner;

public class UserMenu {
    private UsuarioService usuarioService;
    private Scanner scanner;
    private boolean salirMenu;

    public UserMenu(Scanner scanner) {
        this.usuarioService = new UsuarioService();
        this.scanner = scanner;
        this.salirMenu = false;
    }

    public void mostrarMenuUsuarios() {
        while (!salirMenu) {
            mostrarEncabezado();
            mostrarOpciones();
            int opcion = obtenerOpcion();
            procesarOpcion(opcion);
        }
    }

    private void mostrarEncabezado() {
        System.out.println("\n=== GESTIÓN DE USUARIOS ===");
        System.out.println("Total usuarios: " + usuarioService.obtenerTotalUsuarios()); // ✅ CORREGIDO
    }

    private void mostrarOpciones() {
        System.out.println("\n1. Agregar usuario");
        System.out.println("2. Listar usuarios");
        System.out.println("3. Buscar usuario");
        System.out.println("4. Editar usuario");
        System.out.println("5. Eliminar usuario");
        System.out.println("6. Ver estadísticas");
        System.out.println("0. Volver al menú principal");
    }

    private int obtenerOpcion() {
        System.out.print("\nSeleccione opción: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                agregarUsuario();
                break;
            case 2:
                listarUsuarios();
                break;
            case 3:
                buscarUsuario();
                break;
            case 4:
                editarUsuario();
                break;
            case 5:
                eliminarUsuario();
                break;
            case 6:
                mostrarEstadisticas();
                break;
            case 0:
                salirMenu = true;
                break;
            default:
                System.out.println("Opción inválida");
        }
    }

    private void agregarUsuario() {
        System.out.println("\n--- AGREGAR USUARIO ---");

        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        System.out.println("Roles: 1. Admin General, 2. Dependiente, 3. Contador");
        System.out.print("Rol (1-3): ");

        try {
            int rol = Integer.parseInt(scanner.nextLine().trim());

            // ✅ Usar el método corregido
            Usuario nuevo = usuarioService.crearUsuario(nombre, username, password, rol);
            System.out.println("✅ Usuario creado. ID: " + nuevo.getId());

        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error inesperado: " + e.getMessage());
        }
    }

    private void listarUsuarios() {
        System.out.println("\n--- LISTA DE USUARIOS ---");

        // ✅ Usar el método corregido
        List<Usuario> usuarios = usuarioService.obtenerTodosUsuarios();

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados");
        } else {
            for (Usuario usuario : usuarios) {
                // Asegúrate que Usuario tenga estos métodos
                System.out.printf("%3d. %-20s (%s) - Rol: %d%n",
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getUsername(),
                        usuario.getRol());
            }
        }
    }

    private void buscarUsuario() {
        System.out.print("\nBuscar por nombre: ");
        String nombre = scanner.nextLine().trim();

        // ✅ Usar el método corregido
        List<Usuario> resultados = usuarioService.buscarPorNombre(nombre);

        if (resultados.isEmpty()) {
            System.out.println("No se encontraron usuarios");
        } else {
            System.out.println("Resultados (" + resultados.size() + "):");
            for (Usuario usuario : resultados) {
                System.out.println("• " + usuario.getNombre() + " (ID: " + usuario.getId() + ")");
            }
        }
    }

    private void editarUsuario() {
        System.out.print("\nID del usuario a editar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());

            // ✅ Usar el método corregido
            Usuario usuario = usuarioService.buscarPorId(id);
            if (usuario == null) {
                System.out.println("Usuario no encontrado");
                return;
            }

            System.out.println("Editando: " + usuario.getNombre() + " (" + usuario.getUsername() + ")");

            System.out.print("Nuevo nombre (enter para mantener '" + usuario.getNombre() + "'): ");
            String nuevoNombre = scanner.nextLine().trim();
            if (nuevoNombre.isEmpty()) {
                nuevoNombre = usuario.getNombre();
            }

            System.out.print("Nuevo username (enter para mantener '" + usuario.getUsername() + "'): ");
            String nuevoUsername = scanner.nextLine().trim();
            if (nuevoUsername.isEmpty()) {
                nuevoUsername = usuario.getUsername();
            }

            System.out.print("Nueva contraseña (enter para mantener actual): ");
            String nuevaPassword = scanner.nextLine().trim();
            if (nuevaPassword.isEmpty()) {
                nuevaPassword = usuario.getPassword();
            }

            System.out.println("Nuevo rol: 1. Admin, 2. Dependiente, 3. Contador");
            System.out.print("Rol (enter para mantener " + usuario.getRol() + "): ");
            String rolStr = scanner.nextLine().trim();
            int nuevoRol = usuario.getRol();

            if (!rolStr.isEmpty()) {
                try {
                    nuevoRol = Integer.parseInt(rolStr);
                    if (nuevoRol < 1 || nuevoRol > 3) {
                        System.out.println("❌ Rol inválido. Manteniendo rol actual.");
                        nuevoRol = usuario.getRol();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Formato inválido. Manteniendo rol actual.");
                }
            }

            // ✅ Usar el método corregido
            boolean actualizado = usuarioService.actualizarUsuario(id, nuevoNombre, nuevoUsername, nuevaPassword,
                    nuevoRol);
            if (actualizado) {
                System.out.println("✅ Usuario actualizado exitosamente");
            } else {
                System.out.println("❌ Error al actualizar usuario");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido");
        }
    }

    private void eliminarUsuario() {
        System.out.print("\nID del usuario a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("¿Seguro que desea eliminar este usuario? (s/n): ");
            String confirmar = scanner.nextLine().trim().toLowerCase();

            if (confirmar.equals("s") || confirmar.equals("si")) {
                // ✅ Usar el método corregido
                boolean eliminado = usuarioService.eliminarUsuario(id);
                if (eliminado) {
                    System.out.println("✅ Usuario eliminado exitosamente");
                } else {
                    System.out.println("❌ Error al eliminar usuario");
                }
            } else {
                System.out.println("⚠️ Eliminación cancelada");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido");
        }
    }

    private void mostrarEstadisticas() {
        System.out.println("\n--- ESTADÍSTICAS DE USUARIOS ---");

        // ✅ Usar el método corregido
        String estadisticas = usuarioService.obtenerEstadisticas();
        System.out.println(estadisticas);

        // Total de usuarios
        System.out.println("\nTotal usuarios registrados: " + usuarioService.obtenerTotalUsuarios());
    }

    // public void cerrar() {
    // if (scanner != null) {
    // scanner.close();
    // }
    // }
}