package src.modelo;

//import src.modelo.Almacen;
import src.Storage.AlmacenRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AlmacenMenu {
    private AlmacenRepository almacenRepository;
    private Scanner scanner;

    public AlmacenMenu(Scanner scanner) {
        this.almacenRepository = AlmacenRepository.getInstance();
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n═══════════════════════════════════════");
            System.out.println("           GESTIÓN DE ALMACENES");
            System.out.println("═══════════════════════════════════════");
            System.out.println("1. Listar almacenes");
            System.out.println("2. Agregar almacén");
            System.out.println("3. Editar almacén");
            System.out.println("4. Eliminar almacén");
            System.out.println("5. Buscar almacén");
            System.out.println("6. Ver almacén central");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nSeleccione una opción: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    listarAlmacenes();
                    break;
                case "2":
                    agregarAlmacen();
                    break;
                case "3":
                    editarAlmacen();
                    break;
                case "4":
                    eliminarAlmacen();
                    break;
                case "5":
                    buscarAlmacen();
                    break;
                case "6":
                    verAlmacenCentral();
                    break;
                case "0":
                    salir = true;
                    break;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }

    private void listarAlmacenes() {
        List<Almacen> almacenes = almacenRepository.obtenerTodos();

        if (almacenes.isEmpty()) {
            System.out.println("\nNo hay almacenes registrados.");
            return;
        }

        System.out.println("\n══════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                                   LISTA DE ALMACENES");
        System.out.println("══════════════════════════════════════════════════════════════════════════════════");
        System.out.printf("%-4s %-10s %-25s %-30s %-12s %-15s %-10s%n",
                "ID", "Código", "Nombre", "Dirección", "Teléfono", "Responsable", "Tipo");
        System.out.println(
                "──────────────────────────────────────────────────────────────────────────────────────────────────────");

        for (Almacen a : almacenes) {
            String tipo = a.isEsCentral() ? "CENTRAL" : "SUCURSAL";
            System.out.printf("%-4d %-10s %-25s %-30s %-12s %-15s %-10s%n",
                    a.getId(), a.getCodigo(), a.getNombre(),
                    a.getUbicacion(), a.getTelefono(), a.getResponsable(), tipo);
        }

        System.out.println("══════════════════════════════════════════════════════════════════════════════════");
        System.out.println("Total: " + almacenes.size() + " almacenes");
    }

    private void agregarAlmacen() {
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("           NUEVO ALMACÉN");
        System.out.println("═══════════════════════════════════════");

        System.out.print("Código: ");
        String codigo = scanner.nextLine().trim();

        if (almacenRepository.buscarPorCodigo(codigo) != null) {
            System.out.println("❌ Ya existe un almacén con ese código");
            return;
        }

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Dirección: ");
        String direccion = scanner.nextLine().trim();

        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine().trim();

        System.out.print("Responsable: ");
        String responsable = scanner.nextLine().trim();

        System.out.print("¿Es almacén central? (s/n): ");
        String esCentralStr = scanner.nextLine().trim();
        boolean esCentral = esCentralStr.equalsIgnoreCase("s");

        Almacen almacen = new Almacen(0, codigo, nombre, direccion, responsable, esCentral, telefono);
        // public Almacen(int id, String codigo, String nombre, String ubicacion, String
        // responsable, boolean esCentral,String telefono)
        if (almacenRepository.agregarAlmacen(almacen)) {
            System.out.println("\n✅ Almacén agregado exitosamente");
        } else {
            System.out.println("\n❌ Error al agregar el almacén");
        }
    }

    private void editarAlmacen() {
        System.out.print("\nIngrese el ID del almacén a editar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Almacen almacen = almacenRepository.buscarPorId(id);

            if (almacen == null) {
                System.out.println("❌ No se encontró el almacén con ID " + id);
                return;
            }

            System.out.println("\nEditando almacén: " + almacen.getNombre());
            System.out.println("(Deje en blanco para mantener el valor actual)");

            System.out.print("Nuevo nombre [" + almacen.getNombre() + "]: ");
            String nuevoNombre = scanner.nextLine().trim();
            if (!nuevoNombre.isEmpty())
                almacen.setNombre(nuevoNombre);

            System.out.print("Nueva dirección [" + almacen.getUbicacion() + "]: ");
            String nuevaDireccion = scanner.nextLine().trim();
            if (!nuevaDireccion.isEmpty())
                almacen.setUbicacion(nuevaDireccion);

            System.out.print("Nuevo teléfono [" + almacen.getTelefono() + "]: ");
            String nuevoTelefono = scanner.nextLine().trim();
            if (!nuevoTelefono.isEmpty())
                almacen.setTelefono(nuevoTelefono);

            System.out.print("Nuevo responsable [" + almacen.getResponsable() + "]: ");
            String nuevoResponsable = scanner.nextLine().trim();
            if (!nuevoResponsable.isEmpty())
                almacen.setResponsable(nuevoResponsable);

            System.out.print("¿Es central? (s/n) [" + (almacen.isEsCentral() ? "s" : "n") + "]: ");
            String esCentralStr = scanner.nextLine().trim();
            if (!esCentralStr.isEmpty()) {
                almacen.setEsCentral(esCentralStr.equalsIgnoreCase("s"));
            }

            if (almacenRepository.actualizarAlmacen(id, almacen)) {
                System.out.println("\n✅ Almacén actualizado exitosamente");
            } else {
                System.out.println("\n❌ Error al actualizar el almacén");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido");
        }
    }

    private void eliminarAlmacen() {
        System.out.print("\nIngrese el ID del almacén a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Almacen almacen = almacenRepository.buscarPorId(id);

            if (almacen == null) {
                System.out.println("❌ No se encontró el almacén con ID " + id);
                return;
            }

            System.out.println("¿Está seguro de eliminar el almacén: " + almacen.getNombre() + "? (s/n): ");
            String confirmar = scanner.nextLine().trim();

            if (confirmar.equalsIgnoreCase("s")) {
                if (almacenRepository.eliminarAlmacen(id)) {
                    System.out.println("✅ Almacén eliminado exitosamente");
                } else {
                    System.out.println("❌ Error al eliminar el almacén");
                }
            } else {
                System.out.println("⚠️ Operación cancelada");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido");
        }
    }

    private void buscarAlmacen() {
        System.out.print("\nIngrese código o nombre a buscar: ");
        String criterio = scanner.nextLine().trim().toLowerCase();

        List<Almacen> almacenes = almacenRepository.obtenerTodos();
        List<Almacen> resultados = new ArrayList<>();

        for (Almacen a : almacenes) {
            if (a.getCodigo().toLowerCase().contains(criterio) ||
                    a.getNombre().toLowerCase().contains(criterio)) {
                resultados.add(a);
            }
        }

        if (resultados.isEmpty()) {
            System.out.println("\nNo se encontraron almacenes con: " + criterio);
            return;
        }

        System.out.println("\nResultados encontrados: " + resultados.size());
        for (Almacen a : resultados) {
            System.out.println("  • " + a.getNombre() + " (" + a.getCodigo() + ") - " +
                    (a.isEsCentral() ? "CENTRAL" : "SUCURSAL"));
        }
    }

    private void verAlmacenCentral() {
        Almacen central = almacenRepository.obtenerAlmacenCentral();

        if (central == null) {
            System.out.println("\n❌ No hay almacén central configurado");
            return;
        }

        System.out.println("\n═══════════════════════════════════════");
        System.out.println("           ALMACÉN CENTRAL");
        System.out.println("═══════════════════════════════════════");
        System.out.println("ID: " + central.getId());
        System.out.println("Código: " + central.getCodigo());
        System.out.println("Nombre: " + central.getNombre());
        System.out.println("Dirección: " + central.getUbicacion());
        System.out.println("Teléfono: " + central.getTelefono());
        System.out.println("Responsable: " + central.getResponsable());
        System.out.println("═══════════════════════════════════════");
    }
}