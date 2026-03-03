
package src.modelo;

import src.modelo.Cliente;
import src.Storage.ClienteRepository;
import java.util.List;
import java.util.Scanner;

public class ClienteMenu {
    private Scanner scanner;
    private ClienteRepository clienteRepo;
    private boolean salirMenu;

    public ClienteMenu(Scanner scanner) {
        this.scanner = scanner;
        this.clienteRepo = ClienteRepository.getInstance();
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
        System.out.println("            👥 GESTIÓN DE CLIENTES");
        System.out.println("═".repeat(60));
        System.out.println("Total clientes: " + clienteRepo.obtenerTodos().size());
    }

    private void mostrarOpciones() {
        System.out.println("\n1. 📝 Agregar cliente");
        System.out.println("2. 📋 Listar clientes");
        System.out.println("3. 🔍 Buscar cliente");
        System.out.println("4. ✏️  Editar cliente");
        System.out.println("5. 🗑️  Eliminar cliente");
        System.out.println("0. ↩️ Volver");
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
                agregarCliente();
                break;
            case 2:
                listarClientes();
                break;
            case 3:
                buscarCliente();
                break;
            case 4:
                editarCliente();
                break;
            case 5:
                eliminarCliente();
                break;
            case 0:
                salirMenu = true;
                break;
            default:
                System.out.println("❌ Opción inválida");
                esperarEnter();
        }
    }

    private void agregarCliente() {
        System.out.println("\n📝 AGREGAR CLIENTE");
        System.out.println("-".repeat(40));

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Documento: ");
        String documento = scanner.nextLine().trim();

        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine().trim();

        System.out.print("Dirección: ");
        String direccion = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        Cliente cliente = new Cliente();
        cliente.setCodigo("CLI-" + System.currentTimeMillis() % 10000);
        cliente.setNombre(nombre);
        cliente.setDocumento(documento);
        cliente.setTelefono(telefono);
        cliente.setDireccion(direccion);
        cliente.setEmail(email);

        if (clienteRepo.agregarCliente(cliente)) {
            System.out.println("✅ Cliente agregado.");
        }
        esperarEnter();
    }

    private void listarClientes() {
        System.out.println("\n📋 LISTA DE CLIENTES");
        System.out.println("-".repeat(80));

        List<Cliente> clientes = clienteRepo.obtenerTodos();

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            System.out.printf("%-5s %-15s %-25s %-15s%n",
                    "ID", "Documento", "Nombre", "Teléfono");
            System.out.println("-".repeat(80));

            for (Cliente c : clientes) {
                System.out.printf("%-5d %-15s %-25s %-15s%n",
                        c.getId(),
                        c.getDocumento(),
                        truncar(c.getNombre(), 24),
                        c.getTelefono());
            }
        }
        esperarEnter();
    }

    private void buscarCliente() {
        System.out.println("\n🔍 BUSCAR CLIENTE");
        System.out.println("-".repeat(40));

        System.out.print("Nombre a buscar: ");
        String criterio = scanner.nextLine().trim();

        List<Cliente> resultados = clienteRepo.buscarPorNombre(criterio);

        if (resultados.isEmpty()) {
            System.out.println("No se encontraron clientes.");
        } else {
            System.out.println("\nResultados:");
            for (Cliente c : resultados) {
                System.out.printf("• %s - %s (Tel: %s)%n",
                        c.getNombre(), c.getDocumento(), c.getTelefono());
            }
        }
        esperarEnter();
    }

    private void editarCliente() {
        System.out.println("\n✏️ EDITAR CLIENTE");
        System.out.println("-".repeat(40));

        System.out.print("ID del cliente: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Cliente cliente = clienteRepo.buscarPorId(id);

            if (cliente == null) {
                System.out.println("❌ Cliente no encontrado.");
                esperarEnter();
                return;
            }

            System.out.println("Editando: " + cliente.getNombre());

            System.out.print("Nuevo nombre [" + cliente.getNombre() + "]: ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty())
                cliente.setNombre(nombre);

            System.out.print("Nuevo teléfono [" + cliente.getTelefono() + "]: ");
            String telefono = scanner.nextLine().trim();
            if (!telefono.isEmpty())
                cliente.setTelefono(telefono);

            System.out.println("✅ Cliente actualizado.");

        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido.");
        }
        esperarEnter();
    }

    private void eliminarCliente() {
        System.out.println("\n🗑️ ELIMINAR CLIENTE");
        System.out.println("-".repeat(40));

        System.out.print("ID del cliente: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());

            Cliente cliente = clienteRepo.buscarPorId(id);
            if (cliente == null) {
                System.out.println("❌ Cliente no encontrado.");
                esperarEnter();
                return;
            }

            System.out.println("Cliente: " + cliente.getNombre());
            System.out.print("¿Confirmar? (s/n): ");

            if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                cliente.setActivo(false);
                System.out.println("✅ Cliente eliminado.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido.");
        }
        esperarEnter();
    }

    private String truncar(String texto, int longitud) {
        if (texto == null || texto.length() <= longitud)
            return texto;
        return texto.substring(0, longitud - 3) + "...";
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
