package src.modelo;

//import src.modelo.Categoria;
//import src.main;
import src.Storage.CategoriaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CategoriaMenu {
    private CategoriaRepository categoriaRepository;
    private Scanner scanner;

    public CategoriaMenu(Scanner scanner) {
        this.categoriaRepository = CategoriaRepository.getInstance();
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n═══════════════════════════════════════");
            System.out.println("         GESTIÓN DE CATEGORÍAS");
            System.out.println("═══════════════════════════════════════");
            System.out.println("1. Listar categorías");
            System.out.println("2. Agregar categoría");
            System.out.println("3. Editar categoría");
            System.out.println("4. Eliminar categoría");
            System.out.println("5. Buscar categoría");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nSeleccione una opción: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    listarCategorias();
                    break;
                case "2":
                    agregarCategoria();
                    break;
                case "3":
                    editarCategoria();
                    break;
                case "4":
                    eliminarCategoria();
                    break;
                case "5":
                    buscarCategoria();
                    break;
                case "0":
                    salir = true;
                    break;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }

    private void listarCategorias() {
        List<Categoria> categorias = categoriaRepository.obtenerTodasCategorias();

        if (categorias.isEmpty()) {
            System.out.println("\nNo hay categorías registradas.");
            return;
        }

        System.out.println("\n══════════════════════════════════════════════════════════════════════");
        System.out.println("                           LISTA DE CATEGORÍAS");
        System.out.println("══════════════════════════════════════════════════════════════════════");
        System.out.printf("%-4s %-15s %-30s %-10s%n", "ID", "Nombre", "Descripción", "Estado");
        System.out.println("──────────────────────────────────────────────────────────────────────");

        for (Categoria c : categorias) {
            String estado = c.isActivo() ? "✅ Activa" : "❌ Inactiva";
            System.out.printf("%-4d %-15s %-30s %-10s%n",
                    c.getId(), c.getNombre(), c.getDescripcion(), estado);
        }

        System.out.println("══════════════════════════════════════════════════════════════════════");
        System.out.println("Total: " + categorias.size() + " categorías");

        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();

    }

    private void agregarCategoria() {
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("           NUEVA CATEGORÍA");
        System.out.println("═══════════════════════════════════════");

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();

        if (categoriaRepository.buscarPorNombre(nombre) != null) {
            System.out.println("❌ Ya existe una categoría con ese nombre");
            return;
        }

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine().trim();

        Categoria categoria = new Categoria(nombre, descripcion);

        if (categoriaRepository.agregarCategoria(categoria)) {
            System.out.println("\n✅ Categoría agregada exitosamente");
        } else {
            System.out.println("\n❌ Error al agregar la categoría");
        }
    }

    private void editarCategoria() {
        System.out.print("\nIngrese el ID de la categoría a editar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Categoria categoria = categoriaRepository.buscarPorId(id);

            if (categoria == null) {
                System.out.println("❌ No se encontró la categoría con ID " + id);
                return;
            }

            System.out.println("\nEditando categoría: " + categoria.getNombre());
            System.out.println("(Deje en blanco para mantener el valor actual)");

            System.out.print("Nuevo nombre [" + categoria.getNombre() + "]: ");
            String nuevoNombre = scanner.nextLine().trim();
            if (!nuevoNombre.isEmpty())
                categoria.setNombre(nuevoNombre);

            System.out.print("Nueva descripción [" + categoria.getDescripcion() + "]: ");
            String nuevaDescripcion = scanner.nextLine().trim();
            if (!nuevaDescripcion.isEmpty())
                categoria.setDescripcion(nuevaDescripcion);

            System.out.print("¿Activa? (s/n) [" + (categoria.isActivo() ? "s" : "n") + "]: ");
            String activaStr = scanner.nextLine().trim();
            if (!activaStr.isEmpty()) {
                categoria.setActivo(activaStr.equalsIgnoreCase("s"));
            }

            if (categoriaRepository.actualizarCategoria(id, categoria)) {
                System.out.println("\n✅ Categoría actualizada exitosamente");
            } else {
                System.out.println("\n❌ Error al actualizar la categoría");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido");
        }
    }

    private void eliminarCategoria() {
        System.out.print("\nIngrese el ID de la categoría a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Categoria categoria = categoriaRepository.buscarPorId(id);

            if (categoria == null) {
                System.out.println("❌ No se encontró la categoría con ID " + id);
                return;
            }

            System.out.println("¿Está seguro de eliminar la categoría: " + categoria.getNombre() + "? (s/n): ");
            String confirmar = scanner.nextLine().trim();

            if (confirmar.equalsIgnoreCase("s")) {
                if (categoriaRepository.eliminarCategoria(id)) {
                    System.out.println("✅ Categoría eliminada exitosamente");
                } else {
                    System.out.println("❌ Error al eliminar la categoría");
                }
            } else {
                System.out.println("⚠️ Operación cancelada");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido");
        }
    }

    private void buscarCategoria() {
        System.out.print("\nIngrese nombre a buscar: ");
        String criterio = scanner.nextLine().trim().toLowerCase();

        List<Categoria> categorias = categoriaRepository.obtenerTodasCategorias();
        List<Categoria> resultados = new ArrayList<>();

        for (Categoria c : categorias) {
            if (c.getNombre().toLowerCase().contains(criterio)) {
                resultados.add(c);
            }
        }

        if (resultados.isEmpty()) {
            System.out.println("\nNo se encontraron categorías con: " + criterio);
            return;
        }

        System.out.println("\nResultados encontrados: " + resultados.size());
        for (Categoria c : resultados) {
            System.out.println("  • " + c.getNombre() + " - " + c.getDescripcion());
        }
    }
}