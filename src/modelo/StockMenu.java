
package src.modelo;

//import src.modelo.*;
import src.Storage.*;
import java.util.List;
import java.util.Scanner;

public class StockMenu {
    private Scanner scanner;
    private StockAlmacenRepository stockRepo;
    private ProductoRepository productRepo;
    private AlmacenRepository almacenRepo;
    private boolean salirMenu;

    public StockMenu(Scanner scanner) {
        this.scanner = scanner;
        this.stockRepo = StockAlmacenRepository.getInstance();
        this.productRepo = ProductoRepository.getInstance();
        this.almacenRepo = AlmacenRepository.getInstance();
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
        System.out.println("            📊 GESTIÓN DE STOCK E INVENTARIO");
        System.out.println("═".repeat(60));
    }

    private void mostrarOpciones() {
        System.out.println("\n1. 📋 Ver stock de un producto");
        System.out.println("2. 🏪 Ver stock de un almacén");
        System.out.println("3. 📦 Ver productos con stock bajo");
        System.out.println("4. ⚠️  Ver productos agotados");
        System.out.println("5. 📊 Ver estadísticas generales");
        System.out.println("6. ✏️  Ajustar stock manualmente");
        System.out.println("7. 🔄 Registrar entrada de productos (compra)");
        System.out.println("0. ↩️  Volver al menú principal");
        System.out.println("\n" + "-".repeat(60));
    }

    private int obtenerOpcion() {
        System.out.print("👉 Seleccione una opción: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                verStockPorProducto();
                break;
            case 2:
                verStockPorAlmacen();
                break;
            case 3:
                verStockBajo();
                break;
            case 4:
                verProductosAgotados();
                break;
            case 5:
                verEstadisticas();
                break;
            case 6:
                ajustarStockManual();
                break;
            case 7:
                registrarEntradaProductos();
                break;
            case 0:
                salirMenu = true;
                break;
            default:
                System.out.println("❌ Opción inválida");
                esperarEnter();
        }
    }

    private void verStockPorProducto() {
        System.out.println("\n📋 VER STOCK POR PRODUCTO");
        System.out.println("-".repeat(40));

        // Mostrar productos disponibles
        List<Producto> productos = productRepo.obtenerTodos();
        if (productos.isEmpty()) {
            System.out.println("❌ No hay productos registrados.");
            esperarEnter();
            return;
        }

        System.out.println("\nProductos disponibles:");
        for (Producto p : productos) {
            System.out.printf("   %d. %s (%s)%n", p.getId(), p.getNombre(), p.getCodigo());
        }

        System.out.print("\nIngrese ID del producto: ");
        try {
            int productoId = Integer.parseInt(scanner.nextLine().trim());
            Producto producto = productRepo.buscarPorId(productoId);

            if (producto == null) {
                System.out.println("❌ Producto no encontrado.");
                esperarEnter();
                return;
            }

            List<StockAlmacen> stocks = stockRepo.obtenerStockPorProducto(productoId);

            if (stocks.isEmpty()) {
                System.out.println("❌ No hay stock registrado para este producto.");
            } else {
                System.out.println("\n📦 STOCK DE: " + producto.getNombre());
                System.out.println("─".repeat(60));
                System.out.printf("%-20s %-15s %-10s %-10s%n", "Almacén", "Cantidad", "Stock Mín", "Estado");
                System.out.println("─".repeat(60));

                for (StockAlmacen stock : stocks) {
                    String estado = stock.estaBajoStockMinimo() ? (stock.getCantidad() == 0 ? "🔴 AGOTADO" : "🟡 BAJO")
                            : "🟢 OK";

                    System.out.printf("%-20s %-15d %-10d %-10s%n",
                            stock.getAlmacen().getNombre(),
                            stock.getCantidad(),
                            stock.getProducto().getStockMinimo(),
                            estado);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido.");
        }
        esperarEnter();
    }

    private void verStockPorAlmacen() {
        System.out.println("\n🏪 VER STOCK POR ALMACÉN");
        System.out.println("-".repeat(40));

        List<Almacen> almacenes = almacenRepo.obtenerAlmacenesActivos();
        if (almacenes.isEmpty()) {
            System.out.println("❌ No hay almacenes registrados.");
            esperarEnter();
            return;
        }

        System.out.println("\nAlmacenes disponibles:");
        for (Almacen a : almacenes) {
            System.out.printf("   %d. %s (%s)%s%n",
                    a.getId(), a.getNombre(), a.getCodigo(),
                    a.isEsCentral() ? " [CENTRAL]" : "");
        }

        System.out.print("\nIngrese ID del almacén: ");
        try {
            int almacenId = Integer.parseInt(scanner.nextLine().trim());
            Almacen almacen = almacenRepo.buscarPorId(almacenId);

            if (almacen == null) {
                System.out.println("❌ Almacén no encontrado.");
                esperarEnter();
                return;
            }

            List<StockAlmacen> stocks = stockRepo.obtenerStockPorAlmacen(almacenId);

            if (stocks.isEmpty()) {
                System.out.println("❌ No hay productos en este almacén.");
            } else {
                System.out.println("\n📦 STOCK EN: " + almacen.getNombre());
                System.out.println("─".repeat(70));
                System.out.printf("%-5s %-25s %-10s %-10s %-10s%n",
                        "ID", "Producto", "Cantidad", "Mínimo", "Estado");
                System.out.println("─".repeat(70));

                for (StockAlmacen stock : stocks) {
                    String estado = stock.estaBajoStockMinimo() ? (stock.getCantidad() == 0 ? "🔴 AGOTADO" : "🟡 BAJO")
                            : "🟢 OK";

                    System.out.printf("%-5d %-25s %-10d %-10d %-10s%n",
                            stock.getProducto().getId(),
                            truncar(stock.getProducto().getNombre(), 24),
                            stock.getCantidad(),
                            stock.getProducto().getStockMinimo(),
                            estado);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido.");
        }
        esperarEnter();
    }

    private void verStockBajo() {
        System.out.println("\n⚠️ PRODUCTOS CON STOCK BAJO");
        System.out.println("-".repeat(50));

        List<StockAlmacen> stockBajo = stockRepo.obtenerStockBajo();

        if (stockBajo.isEmpty()) {
            System.out.println("✅ No hay productos con stock bajo. ¡Todo bien!");
        } else {
            System.out.printf("%-25s %-20s %-10s %-10s %-10s%n",
                    "Producto", "Almacén", "Actual", "Mínimo", "Faltan");
            System.out.println("-".repeat(70));

            for (StockAlmacen stock : stockBajo) {
                int faltan = stock.getProducto().getStockMinimo() - stock.getCantidad();
                System.out.printf("%-25s %-20s %-10d %-10d %-10d%n",
                        truncar(stock.getProducto().getNombre(), 24),
                        truncar(stock.getAlmacen().getNombre(), 19),
                        stock.getCantidad(),
                        stock.getProducto().getStockMinimo(),
                        faltan > 0 ? faltan : 0);
            }

            System.out.println("\n🔴 " + stockBajo.size() + " productos requieren atención inmediata.");
        }
        esperarEnter();
    }

    private void verProductosAgotados() {
        System.out.println("\n🚫 PRODUCTOS AGOTADOS (STOCK CERO)");
        System.out.println("-".repeat(50));

        List<StockAlmacen> agotados = stockRepo.obtenerProductosAgotados();

        if (agotados.isEmpty()) {
            System.out.println("✅ No hay productos agotados.");
        } else {
            System.out.printf("%-25s %-20s %-10s%n",
                    "Producto", "Almacén", "Stock Mínimo");
            System.out.println("-".repeat(60));

            for (StockAlmacen stock : agotados) {
                System.out.printf("%-25s %-20s %-10d%n",
                        truncar(stock.getProducto().getNombre(), 24),
                        truncar(stock.getAlmacen().getNombre(), 19),
                        stock.getProducto().getStockMinimo());
            }

            System.out.println("\n🔴 " + agotados.size() + " productos deben ser reabastecidos urgentemente.");
        }
        esperarEnter();
    }

    private void verEstadisticas() {
        System.out.println("\n📊 ESTADÍSTICAS DE INVENTARIO");
        System.out.println("-".repeat(40));

        System.out.println(stockRepo.obtenerEstadisticas());

        List<Producto> productos = productRepo.obtenerTodos();
        List<Almacen> almacenes = almacenRepo.obtenerAlmacenesActivos();

        System.out.printf("\n📦 Productos activos: %d%n", productos.size());
        System.out.printf("🏪 Almacenes activos: %d%n", almacenes.size());
        System.out.printf("💰 Valor total del inventario: $%.2f%n", calcularValorInventario());

        esperarEnter();
    }

    private void ajustarStockManual() {
        System.out.println("\n✏️ AJUSTE MANUAL DE STOCK");
        System.out.println("-".repeat(40));

        try {
            // Seleccionar producto
            List<Producto> productos = productRepo.obtenerTodos(); // estos eran obtener productos activos. si se
                                                                   // necesita se cambia
            if (productos.isEmpty()) {
                System.out.println("❌ No hay productos registrados.");
                esperarEnter();
                return;
            }

            System.out.println("\nProductos disponibles:");
            for (Producto p : productos) {
                System.out.printf("   %d. %s%n", p.getId(), p.getNombre());
            }

            System.out.print("\nID del producto: ");
            int productoId = Integer.parseInt(scanner.nextLine().trim());

            // Seleccionar almacén
            List<Almacen> almacenes = almacenRepo.obtenerAlmacenesActivos();
            System.out.println("\nAlmacenes disponibles:");
            for (Almacen a : almacenes) {
                System.out.printf("   %d. %s%n", a.getId(), a.getNombre());
            }

            System.out.print("ID del almacén: ");
            int almacenId = Integer.parseInt(scanner.nextLine().trim());

            // Verificar stock actual
            StockAlmacen stock = stockRepo.obtenerStockPorProductoYAlmacen(productoId, almacenId);
            if (stock == null) {
                System.out.println("❌ No hay registro de stock para ese producto/almacén.");
                esperarEnter();
                return;
            }

            System.out.println("\nStock actual: " + stock.getCantidad());
            System.out.println("1. Aumentar stock");
            System.out.println("2. Disminuir stock");
            System.out.println("3. Establecer valor exacto");
            System.out.print("Seleccione tipo de ajuste: ");

            int tipo = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Cantidad: ");
            int cantidad = Integer.parseInt(scanner.nextLine().trim());

            switch (tipo) {
                case 1:
                    stockRepo.agregarStock(productoId, almacenId, cantidad);
                    break;
                case 2:
                    if (stockRepo.disminuirStock(productoId, almacenId, cantidad)) {
                        System.out.println("✅ Stock disminuido exitosamente.");
                    } else {
                        System.out.println("❌ Stock insuficiente para disminuir.");
                    }
                    break;
                case 3:
                    stock.setCantidad(cantidad);
                    System.out.println("✅ Stock establecido en " + cantidad);
                    break;
                default:
                    System.out.println("❌ Opción inválida");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
        }
        esperarEnter();
    }

    private void registrarEntradaProductos() {
        System.out.println("\n📦 REGISTRAR ENTRADA DE PRODUCTOS (COMPRA)");
        System.out.println("-".repeat(40));

        try {
            Almacen central = almacenRepo.obtenerAlmacenCentral();
            if (central == null) {
                System.out.println("❌ No hay almacén central configurado.");
                esperarEnter();
                return;
            }

            System.out.println("🏪 Almacén de entrada: " + central.getNombre() + " [CENTRAL]");
            System.out.println("📅 Fecha: " + new java.util.Date());

            boolean continuar = true;

            while (continuar) {
                System.out.println("\n" + "-".repeat(40));
                System.out.print("Código del producto (o 0 para terminar): ");
                String codigo = scanner.nextLine().trim();

                if (codigo.equals("0")) {
                    continuar = false;
                    break;
                }

                Producto producto = productRepo.buscarPorCodigo(codigo);
                if (producto == null) {
                    System.out.println("❌ Producto no encontrado.");
                    continue;
                }

                System.out.print("Cantidad recibida: ");
                int cantidad = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Precio de compra unitario: ");
                double precioCompra = Double.parseDouble(scanner.nextLine().trim());

                // Actualizar stock en almacén central
                stockRepo.agregarStock(producto.getId(), central.getId(), cantidad);

                // Registrar en un historial de compras (pendiente)
                System.out.printf("✅ Entrada registrada: %d unidades de %s a $%.2f c/u%n",
                        cantidad, producto.getNombre(), precioCompra);

                System.out.print("\n¿Registrar otro producto? (s/n): ");
                continuar = scanner.nextLine().trim().toLowerCase().equals("s");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
        }
        esperarEnter();
    }

    private double calcularValorInventario() {
        double total = 0;
        List<Producto> productos = productRepo.obtenerTodos();

        for (Producto producto : productos) {
            List<StockAlmacen> stocks = stockRepo.obtenerStockPorProducto(producto.getId());
            for (StockAlmacen stock : stocks) {
                total += stock.getCantidad() * producto.getPrecioCosto();
            }
        }
        return total;
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
