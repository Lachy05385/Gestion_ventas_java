package src.modelo;
//import src.ui.AlmacenMenu;

//import src.ui.CategoriaMenu;
//package src.seguridad;
//import src.modelo.EntradaProducto;
//import src.seguridad.AuthService;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuPrincipal {
    private String titulo;
    private String nombreEmpresa;
    private String version;
    private List<String> opciones;

    private Scanner scanner; // Recibirá el Scanner de Main

    public MenuPrincipal(Scanner scanner) {
        this.titulo = "SISTEMA DE CONTROL DE INVENTARIOS";
        this.nombreEmpresa = "Mi Empresa S.A.";
        this.version = "v1.0.0";
        this.opciones = new ArrayList<>();
        this.scanner = scanner;
        inicializarOpciones();
    }

    private void inicializarOpciones() {
        opciones.add("1. 📊 Dashboard");
        opciones.add("2. 👥 Gestión de Usuarios");

        // MÓDULO DE INVENTARIO
        opciones.add("3. 🏪 Gestión de Almacenes");
        opciones.add("4. 📂 Gestión de Categorías");
        opciones.add("5. 📦 Gestión de Productos");
        opciones.add("6. 📊 Consultar Stock");
        opciones.add("7. 🔄 Transferencias entre Almacenes");

        // MÓDULO DE COMPRAS
        opciones.add("8. 🏢 Gestión de Proveedores");
        opciones.add("9. 📦 Gestión de Entradas (Compras)");

        // MÓDULO DE VENTAS
        opciones.add("10. 🛒 Punto de Venta");
        opciones.add("11. 👥 Gestión de Clientes");
        opciones.add("12. 💰 Módulo de Ventas");

        // REPORTES Y CONFIGURACIÓN
        opciones.add("13. 📈 Reportes y Estadísticas");
        opciones.add("14. ⚙️ Configuración");
        opciones.add("15. ❓ Ayuda");
        opciones.add("16. 👤 Mi Perfil");
        opciones.add("17. 🚪 Salir / Cerrar Sesión");
    }

    /**
     * Muestra la pantalla de bienvenida completa
     * 
     * @return La opción seleccionada por el usuario (1-8)
     */
    public int mostrarPantallaCompleta() {
        limpiarConsola();
        mostrarEncabezadoArtistico();
        mostrarInformacionSistema();
        mostrarOpcionesNumeradas();
        return obtenerSeleccionValida();
    }

    /**
     * Muestra solo el encabezado (para pantallas más rápidas)
     */
    public void mostrarEncabezadoArtistico() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                          ║");
        System.out.println("║           " + titulo + "              ║");
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * Muestra información del sistema
     */
    private void mostrarInformacionSistema() {
        System.out.println("   " + nombreEmpresa);
        System.out.println("   " + obtenerFechaActual());
        System.out.println("   " + version);
        System.out.println("   Usuario: Administrador");
        System.out.println();
        System.out.println("   Estado: Sistema listo |  Base de datos: Conectada");
        System.out.println();
    }

    /**
     * Muestra las opciones con formato numerado
     */
    private void mostrarOpcionesNumeradas() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                    🎯 MENÚ PRINCIPAL                     ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");

        for (int i = 0; i < opciones.size(); i++) {
            String numero = String.format("%2d", i + 1); // Formato de 2 dígitos
            System.out.printf("║   %s. %-50s ║%n", numero, opciones.get(i));
        }

        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * Obtiene y valida la selección del usuario
     */
    private int obtenerSeleccionValida() {
        int intentos = 0;
        int maxIntentos = 3;

        while (intentos < maxIntentos) {
            System.out.print("👉 Ingrese el número de su opción (1-" + opciones.size() + "): ");
            String entrada = scanner.nextLine().trim();

            try {
                int opcion = Integer.parseInt(entrada);

                if (opcion >= 1 && opcion <= opciones.size()) {
                    System.out.println();
                    System.out.println("✅ Seleccionó: " + opciones.get(opcion - 1));
                    pausaBreve(500); // Pequeña pausa para feedback
                    return opcion;
                } else {
                    System.out.println("❌ Error: Debe ingresar un número entre 1 y " + opciones.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Error: '" + entrada + "' no es un número válido");
            }

            intentos++;
            if (intentos < maxIntentos) {
                System.out.println("⚠️  Intentos restantes: " + (maxIntentos - intentos));
            }
        }

        System.out.println("\n🚫 Demasiados intentos fallidos. Volviendo al menú...");
        pausaBreve(2000);
        return -1; // Código de error
    }

    /**
     * Método auxiliar para pausas
     */
    private void pausaBreve(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            // No hacer nada si se interrumpe
        }
    }

    /**
     * Método para limpiar la consola (funciona en Windows y Unix)
     */
    private void limpiarConsola() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Si falla, al menos imprimir líneas en blanco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Obtiene la fecha actual formateada
     */
    private String obtenerFechaActual() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(new java.util.Date());
    }

    /**
     * Muestra un mensaje de despedida
     */
    public void mostrarDespedida() {
        limpiarConsola();
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                          ║");
        System.out.println("║                👋 ¡HASTA PRONTO! 👋                ║");
        System.out.println("║                                                          ║");
        System.out.println("║    Gracias por usar el Sistema de Control de Inventarios   ║");
        System.out.println("║                                                          ║");
        System.out.println("║              " + nombreEmpresa + "              ║");
        System.out.println("║              " + version + "                            ║");
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
        pausaBreve(2000);
    }

    /**
     * Getter para el texto de una opción
     */
    public String getTextoOpcion(int numeroOpcion) {
        if (numeroOpcion >= 1 && numeroOpcion <= opciones.size()) {
            return opciones.get(numeroOpcion - 1);
        }
        return "Opción no disponible";
    }

    /**
     * Verifica si la opción es para salir
     */
    public boolean esSalir(int opcion) {
        return opcion == opciones.size();
    }

    /**
     * Cierra recursos
     */
    public void cerrar() {
        if (scanner != null) {
            scanner.close();
        }
    }
}