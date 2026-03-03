package src.seguridad;

import java.util.HashMap;
import java.util.Map;

public class PermisosManager {
    // Definición de operaciones (puedes agregar más)
    public static final int PERMISO_DASHBOARD = 1;
    public static final int PERMISO_GESTION_USUARIOS = 2;
    public static final int PERMISO_GESTION_PRODUCTOS = 3;
    public static final int PERMISO_GESTION_VENTAS = 4;
    public static final int PERMISO_GESTION_REPORTES = 5;
    public static final int PERMISO_CONFIGURACION = 6;
    public static final int PERMISO_ELIMINAR = 7;
    public static final int PERMISO_EXPORTAR = 8;
    public static final int PERMISO_AUDITORIA = 9;

    public static final int PERMISO_GESTION_ALMACENES = 10;
    public static final int PERMISO_GESTION_CATEGORIAS = 11;
    // public static final int PERMISO_GESTION_PRODUCTOS = 12;
    public static final int PERMISO_GESTION_STOCK = 13;
    public static final int PERMISO_GESTION_TRANSFERENCIAS = 14;
    public static final int PERMISO_GESTION_PROVEEDORES = 15;
    public static final int PERMISO_GESTION_ENTRADAS = 16;
    public static final int PERMISO_PUNTO_VENTA = 20;
    public static final int PERMISO_GESTION_CLIENTES = 21;
    // Mapa de permisos por rol

    private static Map<Integer, int[]> permisosPorRol;

    static {
        inicializarPermisos();
    }

    private static void inicializarPermisos() {
        permisosPorRol = new HashMap<>();

        // Administrador (rol 1) - Acceso total
        permisosPorRol.put(1, new int[] {
                PERMISO_DASHBOARD,
                PERMISO_GESTION_USUARIOS,
                PERMISO_GESTION_PRODUCTOS,
                PERMISO_GESTION_VENTAS,
                PERMISO_GESTION_REPORTES,
                PERMISO_CONFIGURACION,
                PERMISO_ELIMINAR,
                PERMISO_EXPORTAR,
                PERMISO_AUDITORIA,
                PERMISO_GESTION_ALMACENES, // ← NUEVO
                PERMISO_GESTION_CATEGORIAS, // ← NUEVO
                PERMISO_GESTION_STOCK, // ← NUEVO
                PERMISO_GESTION_TRANSFERENCIAS, // ← NUEVO
                PERMISO_GESTION_PROVEEDORES,
                PERMISO_GESTION_ENTRADAS,
                PERMISO_PUNTO_VENTA,
                PERMISO_GESTION_CLIENTES

        });

        // Dependiente (rol 2) - Solo ventas y consultas
        permisosPorRol.put(2, new int[] {
                // PERMISO_DASHBOARD,
                PERMISO_GESTION_VENTAS,
                PERMISO_GESTION_PRODUCTOS, // Solo para consulta, no modificación
                PERMISO_PUNTO_VENTA,
                PERMISO_GESTION_CLIENTES
        });

        // Contador (rol 3) - Reportes y ventas
        permisosPorRol.put(3, new int[] {
                PERMISO_DASHBOARD,
                PERMISO_GESTION_VENTAS,
                PERMISO_GESTION_REPORTES,
                PERMISO_EXPORTAR
        });

        // Inventarista (rol 4) - Solo productos
        permisosPorRol.put(4, new int[] {
                PERMISO_DASHBOARD,
                PERMISO_GESTION_PRODUCTOS
        });
    }

    // Verificar si un rol tiene un permiso específico
    public static boolean tienePermiso(int rol, int permiso) {
        if (!permisosPorRol.containsKey(rol)) {
            return false;
        }

        int[] permisos = permisosPorRol.get(rol);
        for (int p : permisos) {
            if (p == permiso) {
                return true;
            }
        }
        return false;
    }

    // Obtener descripción del permiso
    public static String getDescripcionPermiso(int permiso) {
        switch (permiso) {
            case PERMISO_DASHBOARD:
                return "Ver Dashboard";
            case PERMISO_GESTION_USUARIOS:
                return "Gestionar Usuarios";
            case PERMISO_GESTION_PRODUCTOS:
                return "Gestionar Productos";
            case PERMISO_GESTION_VENTAS:
                return "Gestionar Ventas";
            case PERMISO_GESTION_REPORTES:
                return "Generar Reportes";
            case PERMISO_CONFIGURACION:
                return "Configurar Sistema";
            case PERMISO_ELIMINAR:
                return "Eliminar Registros";
            case PERMISO_EXPORTAR:
                return "Exportar Datos";
            case PERMISO_AUDITORIA:
                return "Ver Auditoría";
            case PERMISO_GESTION_ALMACENES:
                return "Gestionar Almacenes";
            case PERMISO_GESTION_CATEGORIAS:
                return "Gestionar Categorías";

            case PERMISO_GESTION_STOCK:
                return "Gestionar Stock";
            case PERMISO_GESTION_TRANSFERENCIAS:
                return "Gestionar Transferencias";
            case PERMISO_GESTION_PROVEEDORES:
                return "Gestionar Proveedores";
            case PERMISO_GESTION_ENTRADAS:
                return "Registrar Entradas de Productos";
            case PERMISO_PUNTO_VENTA:
                return "Punto de Venta";
            case PERMISO_GESTION_CLIENTES:
                return "Gestionar Clientes";
            default:
                return "Permiso Desconocido";

        }
    }

    // Mostrar matriz de permisos
    public static void mostrarMatrizPermisos() {
        System.out.println("\n══════════════════════════════════════════════════════════");
        System.out.println("                    MATRIZ DE PERMISOS");
        System.out.println("══════════════════════════════════════════════════════════");

        System.out.printf("%-20s", "Permiso / Rol");
        System.out.printf("%-15s", "Admin");
        System.out.printf("%-15s", "Dependiente");
        System.out.printf("%-15s", "Contador");
        System.out.printf("%-15s%n", "Inventarista");

        System.out.println("-".repeat(80));

        int[] todosPermisos = {
                PERMISO_DASHBOARD, PERMISO_GESTION_USUARIOS, PERMISO_GESTION_PRODUCTOS,
                PERMISO_GESTION_VENTAS, PERMISO_GESTION_REPORTES, PERMISO_CONFIGURACION,
                PERMISO_ELIMINAR, PERMISO_EXPORTAR, PERMISO_AUDITORIA
        };

        for (int permiso : todosPermisos) {
            System.out.printf("%-20s", getDescripcionPermiso(permiso));
            System.out.printf("%-15s", tienePermiso(1, permiso) ? "✓" : "✗");
            System.out.printf("%-15s", tienePermiso(2, permiso) ? "✓" : "✗");
            System.out.printf("%-15s", tienePermiso(3, permiso) ? "✓" : "✗");
            System.out.printf("%-15s%n", tienePermiso(4, permiso) ? "✓" : "✗");
        }
    }
}