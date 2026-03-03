package src.modelo;

import src.modelo.Producto;
import src.modelo.Categoria;
import src.Storage.ProductoRepository;
import src.Storage.CategoriaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductoMenu {
    private ProductoRepository productoRepository;
    private CategoriaRepository categoriaRepository;
    private Scanner scanner;

    public ProductoMenu(Scanner scanner) {
        this.productoRepository = ProductoRepository.getInstance();
        this.categoriaRepository = CategoriaRepository.getInstance();
        this.scanner = scanner;

        // Inicializar productos demo si no existen
        productoRepository.inicializarProductosDemo();
    }

    public void mostrarMenu() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n═══════════════════════════════════════");
            System.out.println("          GESTIÓN DE PRODUCTOS");
            System.out.println("═══════════════════════════════════════");
            System.out.println("1. Listar productos");
            System.out.println("2. Agregar producto");
            System.out.println("3. Editar producto");
            System.out.println("4. Eliminar producto");
            System.out.println("5. Buscar producto");
            System.out.println("6. Productos por categoría");
            System.out.println("7. Ver estadísticas");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nSeleccione una opción: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    listarProductos();
                    break;
                case "2":
                    agregarProducto();
                    break;
                case "3":
                    editarProducto();
                    break;
                case "4":
                    eliminarProducto();
                    break;
                case "5":
                    buscarProducto();
                    break;
                case "6":
                    productosPorCategoria();
                    break;
                case "7":
                    mostrarEstadisticas();
                    break;
                case "0":
                    salir = true;
                    break;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }

    private void listarProductos() {
        List<Producto> productos = productoRepository.obtenerTodos();

        if (productos.isEmpty()) {
            System.out.println("\nNo hay productos registrados.");
            return;
        }

        System.out.println(
                "\n═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                                                                   LISTA DE PRODUCTOS");
        System.out.println(
                "═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════");
        System.out.printf("%-4s %-12s %-25s %-15s %-10s %-12s %-12s %-10s %-10s%n",
                "ID", "Código", "Nombre", "Categoría", "Unidad", "Costo", "Venta", "Stock Mín", "Estado");
        System.out.println(
                "───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");

        for (Producto p : productos) {
            String estado = p.isActivo() ? "✅" : "❌";
            System.out.printf("%-4d %-12s %-25s %-15s %-10s %-12.2f %-12.2f %-10d %-10s%n",
                    p.getId(), p.getCodigo(), p.getNombre(),
                    p.getCategoria().getNombre(), p.getUnidadMedida(),
                    p.getPrecioCosto(), p.getPrecioVenta(),
                    p.getStockMinimo(), estado);
        }

        System.out.println(
                "═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════");
        System.out.println("Total: " + productos.size() + " productos");
    }

    private void agregarProducto() {
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("           NUEVO PRODUCTO");
        System.out.println("═══════════════════════════════════════");

        // Obtener categorías disponibles
        List<Categoria> categorias = categoriaRepository.obtenerTodasCategorias();
        if (categorias.isEmpty()) {
            System.out.println("❌ No hay categorías registradas. Debe crear al menos una categoría primero.");
            return;
        }

        System.out.print("Código: ");
        String codigo = scanner.nextLine().trim();

        if (productoRepository.buscarPorCodigo(codigo) != null) {
            System.out.println("❌ Ya existe un producto con ese código");
            return;
        }

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();

        // Mostrar categorías disponibles
        System.out.println("\nCategorías disponibles:");
        for (Categoria c : categorias) {
            System.out.println("  " + c.getId() + ". " + c.getNombre());
        }

        System.out.print("Seleccione ID de categoría: ");
        int categoriaId;
        try {
            categoriaId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido");
            return;
        }

        Categoria categoria = categoriaRepository.buscarPorId(categoriaId);
        if (categoria == null) {
            System.out.println("❌ Categoría no encontrada");
            return;
        }

        System.out.print("Unidad de medida (kg, litro, unidad, etc.): ");
        String unidadMedida = scanner.nextLine().trim();

        System.out.print("Precio de costo: ");
        double precioCosto;
        try {
            precioCosto = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Precio inválido");
            return;
        }

        System.out.print("Precio de venta: ");
        double precioVenta;
        try {
            precioVenta = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Precio inválido");
            return;
        }

        System.out.print("Stock mínimo: ");
        int stockMinimo;
        try {
            stockMinimo = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Stock mínimo inválido");
            return;
        }

        System.out.print("Ruta de imagen (opcional): ");
        String imagen = scanner.nextLine().trim();

        Producto producto = new Producto(0, codigo, nombre, categoria, unidadMedida, precioCosto, precioVenta,
                stockMinimo, imagen);
        if (!imagen.isEmpty()) {
            producto.setImagen(imagen);
        }

        if (productoRepository.agregarProducto(producto)) {
            System.out.println("\n✅ Producto agregado exitosamente");
        } else {
            System.out.println("\n❌ Error al agregar el producto");
        }
    }

    private void editarProducto() {
        System.out.print("\nIngrese el ID del producto a editar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Producto producto = productoRepository.buscarPorId(id);

            if (producto == null) {
                System.out.println("❌ No se encontró el producto con ID " + id);
                return;
            }

            System.out.println("\nEditando producto: " + producto.getNombre());
            System.out.println("(Deje en blanco para mantener el valor actual)");

            System.out.print("Nuevo nombre [" + producto.getNombre() + "]: ");
            String nuevoNombre = scanner.nextLine().trim();
            if (!nuevoNombre.isEmpty())
                producto.setNombre(nuevoNombre);

            // Mostrar categorías para editar
            List<Categoria> categorias = categoriaRepository.obtenerTodasCategorias();
            System.out.println("\nCategorías disponibles:");
            for (Categoria c : categorias) {
                System.out.println("  " + c.getId() + ". " + c.getNombre());
            }
            System.out.print("Nuevo ID de categoría [" + producto.getCategoria().getId() + "]: ");
            String nuevaCategoriaStr = scanner.nextLine().trim();
            if (!nuevaCategoriaStr.isEmpty()) {
                try {
                    int nuevaCategoriaId = Integer.parseInt(nuevaCategoriaStr);
                    Categoria nuevaCategoria = categoriaRepository.buscarPorId(nuevaCategoriaId);
                    if (nuevaCategoria != null) {
                        producto.setCategoria(nuevaCategoria);
                    } else {
                        System.out.println("⚠️ Categoría no encontrada, manteniendo la actual");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ ID inválido, manteniendo categoría actual");
                }
            }

            System.out.print("Nueva unidad de medida [" + producto.getUnidadMedida() + "]: ");
            String nuevaUnidad = scanner.nextLine().trim();
            if (!nuevaUnidad.isEmpty())
                producto.setUnidadMedida(nuevaUnidad);

            System.out.print("Nuevo precio de costo [" + producto.getPrecioCosto() + "]: ");
            String nuevoCostoStr = scanner.nextLine().trim();
            if (!nuevoCostoStr.isEmpty()) {
                try {
                    producto.setPrecioCosto(Double.parseDouble(nuevoCostoStr));
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Precio inválido, manteniendo el actual");
                }
            }

            System.out.print("Nuevo precio de venta [" + producto.getPrecioVenta() + "]: ");
            String nuevaVentaStr = scanner.nextLine().trim();
            if (!nuevaVentaStr.isEmpty()) {
                try {
                    producto.setPrecioVenta(Double.parseDouble(nuevaVentaStr));
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Precio inválido, manteniendo el actual");
                }
            }

            System.out.print("Nuevo stock mínimo [" + producto.getStockMinimo() + "]: ");
            String nuevoStockStr = scanner.nextLine().trim();
            if (!nuevoStockStr.isEmpty()) {
                try {
                    producto.setStockMinimo(Integer.parseInt(nuevoStockStr));
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Stock inválido, manteniendo el actual");
                }
            }

            System.out.print("¿Activo? (s/n) [" + (producto.isActivo() ? "s" : "n") + "]: ");
            String activoStr = scanner.nextLine().trim();
            if (!activoStr.isEmpty()) {
                producto.setActivo(activoStr.equalsIgnoreCase("s"));
            }

            if (productoRepository.actualizarProducto(producto)) {
                System.out.println("\n✅ Producto actualizado exitosamente");
            } else {
                System.out.println("\n❌ Error al actualizar el producto");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido");
        }
    }

    private void eliminarProducto() {
        System.out.print("\nIngrese el ID del producto a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Producto producto = productoRepository.buscarPorId(id);

            if (producto == null) {
                System.out.println("❌ No se encontró el producto con ID " + id);
                return;
            }

            System.out.println("¿Está seguro de eliminar el producto: " + producto.getNombre() + "? (s/n): ");
            String confirmar = scanner.nextLine().trim();

            if (confirmar.equalsIgnoreCase("s")) {
                if (productoRepository.eliminarProducto(id)) {
                    System.out.println("✅ Producto eliminado exitosamente");
                } else {
                    System.out.println("❌ Error al eliminar el producto");
                }
            } else {
                System.out.println("⚠️ Operación cancelada");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido");
        }
    }

    private void buscarProducto() {
        System.out.print("\nIngrese nombre o código a buscar: ");
        String criterio = scanner.nextLine().trim().toLowerCase();

        List<Producto> productos = productoRepository.obtenerTodos();
        List<Producto> resultados = new ArrayList<>();

        for (Producto p : productos) {
            if (p.getNombre().toLowerCase().contains(criterio) ||
                    p.getCodigo().toLowerCase().contains(criterio)) {
                resultados.add(p);
            }
        }

        if (resultados.isEmpty()) {
            System.out.println("\nNo se encontraron productos con: " + criterio);
            return;
        }

        System.out.println("\nResultados encontrados: " + resultados.size());
        for (Producto p : resultados) {
            System.out.printf("  • %-25s (Cód: %-10s) - %-15s - $%.2f%n",
                    p.getNombre(), p.getCodigo(), p.getCategoria().getNombre(), p.getPrecioVenta());
        }
    }

    private void productosPorCategoria() {
        List<Categoria> categorias = categoriaRepository.obtenerTodasCategorias();

        if (categorias.isEmpty()) {
            System.out.println("\nNo hay categorías registradas.");
            return;
        }

        System.out.println("\nSeleccione categoría:");
        for (Categoria c : categorias) {
            System.out.println("  " + c.getId() + ". " + c.getNombre());
        }

        System.out.print("ID de categoría: ");
        try {
            int categoriaId = Integer.parseInt(scanner.nextLine().trim());
            Categoria categoria = categoriaRepository.buscarPorId(categoriaId);

            if (categoria == null) {
                System.out.println("❌ Categoría no encontrada");
                return;
            }

            List<Producto> productos = productoRepository.buscarPorCategoria(categoriaId);

            if (productos.isEmpty()) {
                System.out.println("\nNo hay productos en la categoría: " + categoria.getNombre());
                return;
            }

            System.out.println("\n══════════════════════════════════════════════════════════════════════");
            System.out.println("     PRODUCTOS EN CATEGORÍA: " + categoria.getNombre().toUpperCase());
            System.out.println("══════════════════════════════════════════════════════════════════════");
            System.out.printf("%-4s %-12s %-25s %-10s %-12s %-12s%n",
                    "ID", "Código", "Nombre", "Unidad", "Costo", "Venta");
            System.out.println("──────────────────────────────────────────────────────────────────────");

            for (Producto p : productos) {
                System.out.printf("%-4d %-12s %-25s %-10s %-12.2f %-12.2f%n",
                        p.getId(), p.getCodigo(), p.getNombre(),
                        p.getUnidadMedida(), p.getPrecioCosto(), p.getPrecioVenta());
            }

            System.out.println("══════════════════════════════════════════════════════════════════════");
            System.out.println("Total: " + productos.size() + " productos");

        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido");
        }
    }

    private void mostrarEstadisticas() {
        List<Producto> productos = productoRepository.obtenerTodos();

        int totalProductos = productos.size();
        int productosActivos = 0;
        int productosInactivos = 0;
        double valorTotalCosto = 0;
        double valorTotalVenta = 0;

        for (Producto p : productos) {
            if (p.isActivo()) {
                productosActivos++;
            } else {
                productosInactivos++;
            }
            // Aquí podríamos multiplicar por stock si tuviéramos stock por almacén
            valorTotalCosto += p.getPrecioCosto();
            valorTotalVenta += p.getPrecioVenta();
        }

        System.out.println("\n═══════════════════════════════════════");
        System.out.println("      ESTADÍSTICAS DE PRODUCTOS");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Total de productos: " + totalProductos);
        System.out.println("Productos activos: " + productosActivos);
        System.out.println("Productos inactivos: " + productosInactivos);
        System.out.printf("Valor total a costo: $%.2f%n", valorTotalCosto);
        System.out.printf("Valor total a venta: $%.2f%n", valorTotalVenta);
        System.out.println("═══════════════════════════════════════");
    }
}