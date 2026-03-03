package src;

//import src.Storage.UsuarioRepositoryPostgreSQL;
import src.Storage.DatabaseConnection;
import src.seguridad.SessionManager;
import src.seguridad.AuthService;
import src.seguridad.PermisosManager;
//import src.modelo.Almacen;
import src.modelo.StockMenu;
import src.modelo.TransferenciaMenu;
import src.Storage.AlmacenRepository;
import src.Storage.CategoriaRepository;
import src.Storage.DatabaseConnection;
import src.Storage.ProductoRepository;
import src.Storage.StockAlmacenRepository;
import src.Storage.UserRepository;
import src.Storage.UsuarioRepositoryPostgreSQL;
import src.modelo.AlmacenMenu;
//import src.modelo.DetalleTransferencia;
import src.modelo.EntradasMenu;
import src.modelo.PuntoVentaMenu;
import src.modelo.ClienteMenu;
//import src.modelo.Categoria;
import src.modelo.CategoriaMenu;
import src.modelo.ProductoMenu;
import src.modelo.ProveedorMenu;
import src.ui.LoginForm;
import src.modelo.MenuPrincipal;
import src.modelo.UserMenu;
import src.modelo.Usuario;

import java.util.List;
import java.util.Scanner;
//import src.modelo.Proveedor;

public class main {
    private static Scanner scannerCompartido;
    private static SessionManager sessionManager;
    private static AuthService authService;

    public static void main(String[] args) {
        // Probar conexión ANTES de todo
        //System.out.println("\n🔌 Probando conexión a PostgreSQL...");
        //DatabaseConnection.testConnection();

        // Si la conexión es exitosa, continuar
        //UsuarioRepositoryPostgreSQL repo = new UsuarioRepositoryPostgreSQL();
        UserRepository repo = new UserRepository();
        List<Usuario> usuarios = repo.obtenerUsuarios();

        if (usuarios.size() == 0) {
            System.out.println("No hay Usuarios un en la BD");

        } else {
            System.out.println("\n📋 Usuarios en BD:");
            for (Usuario u : usuarios) {
                System.out.println("   • " + u.getNombre() + " (" + u.getUsername() + ")");
            }

        }

        scannerCompartido = new Scanner(System.in);
        sessionManager = SessionManager.getInstance();
        authService = new AuthService();
        /// _________________________________________
        // 1. Primero categorías (si existen)
        CategoriaRepository catRepo = CategoriaRepository.getInstance();

        // 2. Luego productos
        ProductoRepository prodRepo = ProductoRepository.getInstance();
        prodRepo.inicializarProductosDemo(); // Forzar creación de productos

        // 3. Luego almacenes
        AlmacenRepository almRepo = AlmacenRepository.getInstance();

        // 4. Finalmente stock
        StockAlmacenRepository stockRepo = StockAlmacenRepository.getInstance();

        /// _________________________________________
        ProductoRepository productRepo = ProductoRepository.getInstance();
        System.out.println("Productos en sistema: " + productRepo.obtenerTodos().size());

        verificarUsuarios();

        try {
            // 1. Mostrar pantalla de login
            LoginForm loginForm = new LoginForm(scannerCompartido);
            if (!loginForm.mostrarLogin()) {
                System.out.println("\n🚫 Acceso denegado. Saliendo del sistema...");
                return;
            }

            // 2. Inicializar componentes principales
            MenuPrincipal menuPrincipal = new MenuPrincipal(scannerCompartido);

            boolean sistemaActivo = true;

            // 3. Bucle principal del sistema
            while (sistemaActivo && sessionManager.isAutenticado()) {
                mostrarInfoSesion();

                int opcion = menuPrincipal.mostrarPantallaCompleta();

                switch (opcion) {
                    // ============ MÓDULOS PRINCIPALES ============
                    case 1: // 📊 Dashboard
                        if (verificarPermiso(PermisosManager.PERMISO_DASHBOARD)) {
                            mostrarDashboard();
                        }
                        break;

                    case 2: // 👥 Gestión de Usuarios
                        if (verificarPermiso(PermisosManager.PERMISO_GESTION_USUARIOS)) {
                            UserMenu userMenu = new UserMenu(scannerCompartido);
                            userMenu.mostrarMenuUsuarios();
                        }
                        break;

                    // ============ MÓDULO DE INVENTARIO ============
                    case 3: // 🏪 Gestión de Almacenes
                        if (verificarPermiso(PermisosManager.PERMISO_GESTION_ALMACENES)) {
                            AlmacenMenu almacenMenu = new AlmacenMenu(scannerCompartido);
                            almacenMenu.mostrarMenu();
                        }
                        break;

                    case 4: // 📂 Gestión de Categorías
                        if (verificarPermiso(PermisosManager.PERMISO_GESTION_CATEGORIAS)) {
                            CategoriaMenu categoriaMenu = new CategoriaMenu(scannerCompartido);
                            categoriaMenu.mostrarMenu();
                        }
                        break;

                    case 5: // 📦 Gestión de Productos
                        if (verificarPermiso(PermisosManager.PERMISO_GESTION_PRODUCTOS)) {
                            ProductoMenu productoMenu = new ProductoMenu(scannerCompartido);
                            productoMenu.mostrarMenu();
                        }
                        break;

                    case 6: // 📊 Consultar Stock
                        if (verificarPermiso(PermisosManager.PERMISO_GESTION_STOCK)) {
                            StockMenu stockMenu = new StockMenu(scannerCompartido);
                            stockMenu.mostrarMenu();
                        }
                        break;

                    case 7: // 🔄 Transferencias entre Almacenes
                        if (verificarPermiso(PermisosManager.PERMISO_GESTION_TRANSFERENCIAS)) {
                            TransferenciaMenu transferenciaMenu = new TransferenciaMenu(scannerCompartido);
                            transferenciaMenu.mostrarMenu();
                        }
                        break;

                    // ============ MÓDULO DE COMPRAS ============
                    case 8: // 🏢 Gestión de Proveedores
                        if (verificarPermiso(PermisosManager.PERMISO_GESTION_PROVEEDORES)) {
                            ProveedorMenu proveedorMenu = new ProveedorMenu(scannerCompartido);
                            proveedorMenu.mostrarMenu();
                        }
                        break;

                    case 9: // 📦 Gestión de Entradas (Compras)
                        if (verificarPermiso(PermisosManager.PERMISO_GESTION_ENTRADAS)) {
                            EntradasMenu entradasMenu = new EntradasMenu(scannerCompartido);
                            entradasMenu.mostrarMenu();
                        }
                        break;

                    // ============ MÓDULO DE VENTAS ============
                    case 10: // 🛒 Punto de Venta
                        if (verificarPermiso(PermisosManager.PERMISO_PUNTO_VENTA)) {
                            PuntoVentaMenu puntoVentaMenu = new PuntoVentaMenu(scannerCompartido);
                            puntoVentaMenu.mostrarMenu();
                        }
                        break;

                    case 11: // 👥 Gestión de Clientes
                        if (verificarPermiso(PermisosManager.PERMISO_GESTION_CLIENTES)) {
                            ClienteMenu clienteMenu = new ClienteMenu(scannerCompartido);
                            clienteMenu.mostrarMenu();
                        }
                        break;

                    case 12: // 💰 Ventas
                        if (verificarPermiso(PermisosManager.PERMISO_PUNTO_VENTA)) {
                            PuntoVentaMenu puntoVentaMenu = new PuntoVentaMenu(scannerCompartido);
                            puntoVentaMenu.mostrarMenu();
                        }
                        break;

                    // ============ REPORTES Y CONFIGURACIÓN ============
                    case 13: // 📈 Reportes y Estadísticas
                        if (verificarPermiso(PermisosManager.PERMISO_GESTION_REPORTES)) {
                            System.out.println("\n🚧 Módulo de reportes próximamente...");
                            esperarEnter();
                        }
                        break;

                    case 14: // ⚙️ Configuración
                        if (verificarPermiso(PermisosManager.PERMISO_CONFIGURACION)) {
                            mostrarMenuConfiguracion(loginForm);
                        }
                        break;

                    case 15: // ❓ Ayuda
                        mostrarAyuda();
                        break;

                    case 16: // 👤 Mi Perfil
                        mostrarPerfil();
                        break;

                    // ============ SALIR ============
                    case 17: // 🚪 Salir/Cerrar sesión
                        sistemaActivo = manejarSalida(loginForm);
                        break;

                    default:
                        System.out.println("❌ Opción no válida");
                        esperarEnter();
                }
            }

            System.out.println("\n👋 ¡Hasta pronto! Sistema finalizado.");

        } finally {
            if (scannerCompartido != null) {
                scannerCompartido.close();
            }
        }
    }

    // ============ MÉTODOS AUXILIARES (TUS MÉTODOS EXISTENTES) ============

    private static void verificarUsuarios() {
        System.out.println("\n🔍 VERIFICANDO USUARIOS EN EL SISTEMA:");

        // Obtén el repositorio directamente
        src.Storage.UserRepository repo = src.Storage.UserRepository.getInstance();
        java.util.List<src.modelo.Usuario> usuarios = repo.obtenerUsuarios();

        if (usuarios.isEmpty()) {
            System.out.println("❌ NO hay usuarios en el sistema");
            System.out.println("Creando usuario administrador por defecto...");

            // Crea un usuario admin de emergencia
            src.modelo.Usuario adminEmergencia = new src.modelo.Usuario();
            adminEmergencia.setId(1);
            adminEmergencia.setNombre("Administrador Emergencia");
            adminEmergencia.setUsername("admin");
            adminEmergencia.setPassword("admin123");
            adminEmergencia.setRol(1);

            if (repo.agregarUsuario(adminEmergencia)) {
                System.out.println("✅ Usuario admin creado: admin / admin123");
            }
        } else {
            System.out.println("✅ Usuarios encontrados: " + usuarios.size());
            for (src.modelo.Usuario u : usuarios) {
                System.out.println("   • " + u.getNombre() +
                        " | User: " + u.getUsername() +
                        " | Pass: " + u.getPassword() +
                        " | Rol: " + u.getRol());
            }
        }
    }

    private static boolean verificarPermiso(int permiso) {
        if (authService.tienePermiso(permiso)) {
            return true;
        } else {
            System.out.println("\n🚫 Acceso denegado.");
            System.out.println("No tiene permisos para: " + PermisosManager.getDescripcionPermiso(permiso));
            System.out.println("Contacte al administrador del sistema.");
            esperarEnter();
            return false;
        }
    }

    private static void mostrarInfoSesion() {
        sessionManager.mostrarInfoSesion();
    }

    private static void mostrarMenuConfiguracion(LoginForm loginForm) {
        boolean enConfiguracion = true;

        while (enConfiguracion && sessionManager.isAutenticado()) {
            System.out.println("\n═══════════════════════════════════════");
            System.out.println("        ⚙️  CONFIGURACIÓN");
            System.out.println("═══════════════════════════════════════");
            System.out.println("1. Cambiar contraseña");
            System.out.println("2. Ver matriz de permisos");
            System.out.println("3. Ver información de sesión");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nSeleccione: ");

            try {
                int opcion = Integer.parseInt(scannerCompartido.nextLine().trim());

                switch (opcion) {
                    case 1:
                        loginForm.mostrarCambioPassword();
                        break;
                    case 2:
                        PermisosManager.mostrarMatrizPermisos();
                        esperarEnter();
                        break;
                    case 3:
                        mostrarInfoSesion();
                        esperarEnter();
                        break;
                    case 0:
                        enConfiguracion = false;
                        break;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida");
            }
        }
    }

    private static void mostrarDashboard() {
        System.out.println("\n══════════════════════════════════════════════════════");
        System.out.println("                   📊 DASHBOARD");
        System.out.println("══════════════════════════════════════════════════════");
        System.out.println("\nBienvenido al panel principal del sistema.");
        System.out.println("\nUsuario actual: " + authService.getInfoUsuario());
        System.out.println("Hora del sistema: " + java.time.LocalDateTime.now());
        esperarEnter();
    }

    private static void mostrarAyuda() {
        System.out.println("\n══════════════════════════════════════════════════════");
        System.out.println("                    ❓ AYUDA");
        System.out.println("══════════════════════════════════════════════════════");
        System.out.println("\nEste sistema utiliza control de acceso basado en roles.");
        System.out.println("\nRoles disponibles:");
        System.out.println("• Administrador: Acceso completo al sistema");
        System.out.println("• Dependiente: Puede gestionar ventas y consultar productos");
        System.out.println("• Contador: Acceso a reportes y módulo financiero");
        System.out.println("• Inventarista: Gestión de productos e inventario");
        System.out.println("\nPara solicitar permisos adicionales, contacte al administrador.");
        esperarEnter();
    }

    private static void mostrarPerfil() {
        if (!sessionManager.isAutenticado())
            return;

        Usuario usuario = sessionManager.getUsuarioActual();

        System.out.println("\n👤 MI PERFIL");
        System.out.println("═".repeat(40));
        System.out.println("Nombre: " + usuario.getNombre());
        System.out.println("Usuario: " + usuario.getUsername());
        System.out.println("Rol: " + usuario.getNombreRol());
        // System.out.println(
        // "Almacén: " + (usuario.getAlmacen() != null ?
        // usuario.getAlmacen().getNombre() : "No asignado"));
        System.out.println("Estado: " + (usuario.isActivo() ? "✅ Activo" : "❌ Inactivo"));
        esperarEnter();
    }

    private static boolean manejarSalida(LoginForm loginForm) {
        System.out.println("\n¿Qué desea hacer?");
        System.out.println("1. Cerrar sesión (volver al login)");
        System.out.println("2. Salir del sistema");
        System.out.print("Seleccione: ");

        try {
            int subOpcion = Integer.parseInt(scannerCompartido.nextLine().trim());
            if (subOpcion == 1) {
                loginForm.cerrarSesion();
                // Volver a login
                if (!loginForm.mostrarLogin()) {
                    return false; // Salir del sistema
                }
                return true; // Continuar con nuevo usuario
            } else if (subOpcion == 2) {
                loginForm.cerrarSesion();
                return false; // Salir del sistema
            }
        } catch (NumberFormatException e) {
            System.out.println("Opción inválida");
        }
        return true; // Continuar si hay error
    }

    public static void esperarEnter() {
        System.out.print("\nPresione Enter para continuar...");
        scannerCompartido.nextLine();
    }
}