
package src.modelo;

import src.modelo.Proveedor;
//import src.modelo.;
import src.Storage.ProveedorRepository1;
import java.util.List;
import java.util.Scanner;

public class ProveedorMenu {
    private Scanner scanner;
    private ProveedorRepository1 proveedorRepo;
    private boolean salirMenu;

    public ProveedorMenu(Scanner scanner) {
        this.scanner = scanner;
        this.proveedorRepo = ProveedorRepository1.getInstance();
        this.salirMenu = false;
    }

    public void mostrarMenu() {
        while (!salirMenu) {
            limpiarPantalla();
            mostrarEncabezado();
            mostrarOpciones();

            int opcion = obtenerOpcion();
            procesarOpcion(opcion);
        }
    }

    private void mostrarEncabezado() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("            🏢 GESTIÓN DE PROVEEDORES");
        System.out.println("═".repeat(60));
        System.out.println("Total proveedores: " + proveedorRepo.obtenerTodos().size());
    }

    private void mostrarOpciones() {
        System.out.println("\n1. 📝 Agregar proveedor");
        System.out.println("2. 📋 Listar proveedores");
        System.out.println("3. 🔍 Buscar proveedor");
        System.out.println("4. ✏️  Editar proveedor");
        System.out.println("5. 🗑️  Eliminar proveedor");
        System.out.println("0. ↩️ Volver al menú principal");
        System.out.println("\n" + "-".repeat(60));
    }

    private int obtenerOpcion() {
        System.out.print("👉 Seleccione: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                agregarProveedor();
                break;
            case 2:
                listarProveedores();
                break;
            case 3:
                buscarProveedor();
                break;
            case 4:
                editarProveedor();
                break;
            case 5:
                eliminarProveedor();
                break;
            case 0:
                salirMenu = true;
                break;
            default:
                System.out.println("❌ Opción inválida");
                esperarEnter();
        }
    }

    private void agregarProveedor() {
        System.out.println("\n📝 AGREGAR PROVEEDOR");
        System.out.println("-".repeat(40));

        System.out.print("Código: ");
        String codigo = scanner.nextLine().trim();

        System.out.print("Nombre del contacto: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Empresa: ");
        String empresa = scanner.nextLine().trim();

        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine().trim();

        System.out.print("Dirección: ");
        String direccion = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        Proveedor proveedor = new Proveedor(codigo, nombre, empresa, telefono, direccion, email);

        if (proveedorRepo.agregarProveedor(proveedor)) {
            System.out.println("✅ Proveedor agregado exitosamente.");
        }
        esperarEnter();
    }

    private void listarProveedores() {
        System.out.println("\n📋 LISTA DE PROVEEDORES");
        System.out.println("-".repeat(80));

        List<Proveedor> proveedores = proveedorRepo.obtenerTodos();

        if (proveedores.isEmpty()) {
            System.out.println("No hay proveedores registrados.");
        } else {
            System.out.printf("%-5s %-15s %-25s %-15s %-15s%n",
                    "ID", "Código", "Nombre/Empresa", "Teléfono", "Email");
            System.out.println("-".repeat(80));

            for (Proveedor p : proveedores) {
                System.out.printf("%-5d %-15s %-25s %-15s %-15s%n",
                        p.getId(),
                        p.getCodigo(),
                        p.getNombre() + " (" + p.getEmpresa() + ")",
                        p.getTelefono(),
                        p.getEmail());
            }
        }
        esperarEnter();
    }

    private void buscarProveedor() {
        System.out.println("\n🔍 BUSCAR PROVEEDOR");
        System.out.println("-".repeat(40));

        System.out.print("Nombre o empresa a buscar: ");
        String criterio = scanner.nextLine().trim();

        List<Proveedor> resultados = proveedorRepo.buscarPorNombre(criterio);

        if (resultados.isEmpty()) {
            System.out.println("No se encontraron proveedores.");
        } else {
            System.out.println("\nResultados:");
            for (Proveedor p : resultados) {
                System.out.printf("• %s - %s (Tel: %s)%n",
                        p.getNombre(), p.getEmpresa(), p.getTelefono());
            }
        }
        esperarEnter();
    }

    private void editarProveedor() {
        System.out.println("\n✏️ EDITAR PROVEEDOR");
        System.out.println("-".repeat(40));

        System.out.print("ID del proveedor a editar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Proveedor proveedor = proveedorRepo.buscarPorId(id);

            if (proveedor == null) {
                System.out.println("❌ Proveedor no encontrado.");
                esperarEnter();
                return;
            }

            System.out.println("\nEditando: " + proveedor.getNombre());
            System.out.println("(Enter para mantener valor actual)");

            System.out.print("Nuevo nombre [" + proveedor.getNombre() + "]: ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty())
                proveedor.setNombre(nombre);

            System.out.print("Nueva empresa [" + proveedor.getEmpresa() + "]: ");
            String empresa = scanner.nextLine().trim();
            if (!empresa.isEmpty())
                proveedor.setEmpresa(empresa);

            System.out.print("Nuevo teléfono [" + proveedor.getTelefono() + "]: ");
            String telefono = scanner.nextLine().trim();
            if (!telefono.isEmpty())
                proveedor.setTelefono(telefono);

            System.out.print("Nueva dirección [" + proveedor.getDireccion() + "]: ");
            String direccion = scanner.nextLine().trim();
            if (!direccion.isEmpty())
                proveedor.setDireccion(direccion);

            System.out.print("Nuevo email [" + proveedor.getEmail() + "]: ");
            String email = scanner.nextLine().trim();
            if (!email.isEmpty())
                proveedor.setEmail(email);

            System.out.println("✅ Proveedor actualizado.");

        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido.");
        }
        esperarEnter();
    }

    private void eliminarProveedor() {
        System.out.println("\n🗑️ ELIMINAR PROVEEDOR");
        System.out.println("-".repeat(40));

        System.out.print("ID del proveedor a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());

            Proveedor proveedor = proveedorRepo.buscarPorId(id);
            if (proveedor == null) {
                System.out.println("❌ Proveedor no encontrado.");
                esperarEnter();
                return;
            }

            System.out.println("Proveedor: " + proveedor.getNombre() + " - " + proveedor.getEmpresa());
            System.out.print("¿Confirmar eliminación? (s/n): ");

            if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                if (proveedorRepo.eliminarProveedor(id)) {
                    System.out.println("✅ Proveedor eliminado.");
                }
            } else {
                System.out.println("⚠️ Operación cancelada.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido.");
        }
        esperarEnter();
    }

    private void limpiarPantalla() {
        for (int i = 0; i < 30; i++)
            System.out.println();
    }

    private void esperarEnter() {
        System.out.print("\n👆 Presione Enter para continuar...");
        scanner.nextLine();
    }
}