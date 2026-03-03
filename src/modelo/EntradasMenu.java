
package src.modelo;

import src.modelo.Proveedor;
import src.modelo.Producto;
import src.modelo.EntradaProducto;
import src.modelo.DetalleEntrada;
import src.Storage.EntradaProductoRepository;
import src.Storage.ProductoRepository;
import src.Storage.ProveedorRepository1;
import src.Storage.AlmacenRepository;
import src.Storage.EntradaProductoRepository;
import src.Storage.StockAlmacenRepository;
import src.seguridad.SessionManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class EntradasMenu {
    private Scanner scanner;
    private EntradaProductoRepository entradaRepo;
    private ProveedorRepository1 proveedorRepo;
    private ProductoRepository productRepo;
    private AlmacenRepository almacenRepo;
    private StockAlmacenRepository stockRepo;
    private SessionManager sessionManager;
    private boolean salirMenu;

    public EntradasMenu(Scanner scanner) {
        this.scanner = scanner;
        this.entradaRepo = EntradaProductoRepository.getInstance();
        this.proveedorRepo = ProveedorRepository1.getInstance();
        this.productRepo = ProductoRepository.getInstance();
        this.almacenRepo = AlmacenRepository.getInstance();
        this.stockRepo = StockAlmacenRepository.getInstance();
        this.sessionManager = SessionManager.getInstance();
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
        System.out.println("            📦 ENTRADAS DE PRODUCTOS (COMPRAS)");
        System.out.println("═".repeat(60));
        System.out.println("Total entradas: " + entradaRepo.obtenerTodas().size());
    }

    private void mostrarOpciones() {
        System.out.println("\n1. 📝 Registrar nueva entrada");
        System.out.println("2. 📋 Ver todas las entradas");
        System.out.println("3. 🔍 Buscar entrada por número");
        System.out.println("4. 🏢 Ver entradas por proveedor");
        System.out.println("5. 📅 Ver entradas por fecha");
        System.out.println("6. 🧾 Ver detalle de entrada");
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
                registrarEntrada();
                break;
            case 2:
                verTodasEntradas();
                break;
            case 3:
                buscarPorNumero();
                break;
            case 4:
                verPorProveedor();
                break;
            case 5:
                verPorFecha();
                break;
            case 6:
                verDetalleEntrada();
                break;
            case 0:
                salirMenu = true;
                break;
            default:
                System.out.println("❌ Opción inválida");
                esperarEnter();
        }
    }

    private void registrarEntrada() {
        System.out.println("\n📝 REGISTRAR NUEVA ENTRADA DE PRODUCTOS");
        System.out.println("-".repeat(50));

        try {
            // Seleccionar proveedor
            List<Proveedor> proveedores = proveedorRepo.obtenerTodos();
            if (proveedores.isEmpty()) {
                System.out.println("❌ No hay proveedores registrados.");
                esperarEnter();
                return;
            }

            System.out.println("\nProveedores disponibles:");
            for (Proveedor p : proveedores) {
                System.out.printf("   %d. %s - %s%n", p.getId(), p.getNombre(), p.getEmpresa());
            }

            System.out.print("\nID del proveedor: ");
            int proveedorId = Integer.parseInt(scanner.nextLine().trim());
            Proveedor proveedor = proveedorRepo.buscarPorId(proveedorId);

            if (proveedor == null) {
                System.out.println("❌ Proveedor no encontrado.");
                esperarEnter();
                return;
            }

            // Número de factura
            System.out.print("Número de factura: ");
            String numeroFactura = scanner.nextLine().trim();

            // Seleccionar almacén destino (normalmente central)
            Almacen central = almacenRepo.obtenerAlmacenCentral();
            if (central == null) {
                System.out.println("❌ No hay almacén central configurado.");
                esperarEnter();
                return;
            }

            System.out.println("Almacén destino: " + central.getNombre() + " [CENTRAL]");

            // Crear entrada
            EntradaProducto entrada = new EntradaProducto();
            entrada.setProveedor(proveedor);
            entrada.setNumeroFactura(numeroFactura);
            entrada.setAlmacenDestino(central);
            entrada.setResponsable(sessionManager.getUsuarioActual());

            List<DetalleEntrada> detalles = new ArrayList<>();

            boolean agregarProductos = true;
            while (agregarProductos) {
                System.out.println("\n" + "-".repeat(40));

                // Mostrar productos
                List<Producto> productos = productRepo.obtenerTodos();
                System.out.println("\nProductos disponibles:");
                for (Producto p : productos) {
                    System.out.printf("   %d. %s (%s)%n", p.getId(), p.getNombre(), p.getCodigo());
                }

                System.out.print("\nID del producto (0 para terminar): ");
                int productoId = Integer.parseInt(scanner.nextLine().trim());

                if (productoId == 0) {
                    agregarProductos = false;
                    break;
                }

                Producto producto = productRepo.buscarPorId(productoId);
                if (producto == null) {
                    System.out.println("❌ Producto no encontrado.");
                    continue;
                }

                System.out.print("Cantidad recibida: ");
                int cantidad = Integer.parseInt(scanner.nextLine().trim());

                if (cantidad <= 0) {
                    System.out.println("❌ Cantidad inválida.");
                    continue;
                }

                System.out.print("Precio de compra unitario: ");
                double precioCompra = Double.parseDouble(scanner.nextLine().trim());

                if (precioCompra <= 0) {
                    System.out.println("❌ Precio inválido.");
                    continue;
                }

                System.out.print("Lote (opcional, Enter para omitir): ");
                String lote = scanner.nextLine().trim();

                DetalleEntrada detalle = new DetalleEntrada(producto, cantidad, precioCompra);
                if (!lote.isEmpty())
                    detalle.setLote(lote);
                detalles.add(detalle);

                System.out.printf("✅ Producto agregado: %s (%d unidades) a $%.2f c/u%n",
                        producto.getNombre(), cantidad, precioCompra);
            }

            if (detalles.isEmpty()) {
                System.out.println("❌ No se agregaron productos. Entrada cancelada.");
                esperarEnter();
                return;
            }

            entrada.setDetalles(detalles);

            System.out.print("\nObservaciones: ");
            String obs = scanner.nextLine().trim();
            entrada.setObservaciones(obs);

            // Resumen
            System.out.println("\n📋 RESUMEN DE ENTRADA:");
            System.out.println("   Proveedor: " + proveedor.getNombre() + " - " + proveedor.getEmpresa());
            System.out.println("   Factura: " + numeroFactura);
            System.out.println("   Productos: " + detalles.size() + " items");
            System.out.println("   Total unidades: " + detalles.stream().mapToInt(DetalleEntrada::getCantidad).sum());
            System.out.printf("   Total compra: $%.2f%n", entrada.getTotalEntrada());

            System.out.print("\n¿Confirmar entrada? (s/n): ");
            if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                // Registrar entrada
                if (entradaRepo.agregarEntrada(entrada)) {
                    // Actualizar stock en almacén central
                    for (DetalleEntrada detalle : detalles) {
                        stockRepo.agregarStock(
                                detalle.getProducto().getId(),
                                central.getId(),
                                detalle.getCantidad());

                        // Opcional: actualizar precio de costo del producto si es mejor que el actual
                        Producto producto = detalle.getProducto();
                        if (detalle.getPrecioCompraUnitario() < producto.getPrecioCosto()) {
                            producto.setPrecioCosto(detalle.getPrecioCompraUnitario());
                        }
                    }
                    System.out.println("✅ Entrada registrada exitosamente. Stock actualizado.");
                }
            } else {
                System.out.println("⚠️ Entrada cancelada.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
        }
        esperarEnter();
    }

    private void verTodasEntradas() {
        System.out.println("\n📋 TODAS LAS ENTRADAS");
        System.out.println("-".repeat(80));

        List<EntradaProducto> entradas = entradaRepo.obtenerTodas();

        if (entradas.isEmpty()) {
            System.out.println("No hay entradas registradas.");
        } else {
            System.out.printf("%-15s %-15s %-20s %-12s %-10s%n",
                    "Número", "Fecha", "Proveedor", "Factura", "Items");
            System.out.println("-".repeat(80));

            for (EntradaProducto e : entradas) {
                System.out.printf("%-15s %-15s %-20s %-12s %-10d%n",
                        e.getNumeroEntrada(),
                        e.getFechaEntrada().toString().substring(0, 10),
                        truncar(e.getProveedor().getNombre(), 19),
                        e.getNumeroFactura(),
                        e.getDetalles().size());
            }
        }
        esperarEnter();
    }

    private void buscarPorNumero() {
        System.out.println("\n🔍 BUSCAR ENTRADA POR NÚMERO");
        System.out.println("-".repeat(40));

        System.out.print("Número de entrada: ");
        String numero = scanner.nextLine().trim();

        EntradaProducto entrada = entradaRepo.buscarPorNumero(numero);

        if (entrada == null) {
            System.out.println("❌ Entrada no encontrada.");
        } else {
            mostrarDetalleEntrada(entrada);
        }
        esperarEnter();
    }

    private void verPorProveedor() {
        System.out.println("\n🏢 ENTRADAS POR PROVEEDOR");
        System.out.println("-".repeat(40));

        try {
            List<Proveedor> proveedores = proveedorRepo.obtenerTodos();

            System.out.println("\nProveedores:");
            for (Proveedor p : proveedores) {
                System.out.printf("   %d. %s%n", p.getId(), p.getNombre());
            }

            System.out.print("\nID del proveedor: ");
            int proveedorId = Integer.parseInt(scanner.nextLine().trim());

            List<EntradaProducto> entradas = entradaRepo.buscarPorProveedor(proveedorId);

            if (entradas.isEmpty()) {
                System.out.println("No hay entradas de este proveedor.");
            } else {
                System.out.println("\nEntradas encontradas: " + entradas.size());
                for (EntradaProducto e : entradas) {
                    System.out.printf("   • %s - %s (%d items)%n",
                            e.getNumeroEntrada(),
                            e.getFechaEntrada().toString().substring(0, 10),
                            e.getDetalles().size());
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido.");
        }
        esperarEnter();
    }

    private void verPorFecha() {
        System.out.println("\n📅 ENTRADAS POR FECHA");
        System.out.println("-".repeat(40));

        System.out.print("Fecha inicio (YYYY-MM-DD): ");
        String inicioStr = scanner.nextLine().trim();
        System.out.print("Fecha fin (YYYY-MM-DD): ");
        String finStr = scanner.nextLine().trim();

        try {
            // Parseo simple de fechas
            Date fechaInicio = java.sql.Date.valueOf(inicioStr);
            Date fechaFin = java.sql.Date.valueOf(finStr);

            List<EntradaProducto> entradas = entradaRepo.buscarPorFecha(fechaInicio, fechaFin);

            if (entradas.isEmpty()) {
                System.out.println("No hay entradas en ese rango de fechas.");
            } else {
                System.out.println("\nEntradas entre " + inicioStr + " y " + finStr + ":");
                double totalCompras = 0;
                for (EntradaProducto e : entradas) {
                    System.out.printf("   • %s - %s (Total: $%.2f)%n",
                            e.getNumeroEntrada(),
                            e.getProveedor().getNombre(),
                            e.getTotalEntrada());
                    totalCompras += e.getTotalEntrada();
                }
                System.out.printf("\n💰 Total compras en período: $%.2f%n", totalCompras);
            }

        } catch (Exception e) {
            System.out.println("❌ Formato de fecha inválido. Use YYYY-MM-DD");
        }
        esperarEnter();
    }

    private void verDetalleEntrada() {
        System.out.println("\n🔍 VER DETALLE DE ENTRADA");
        System.out.println("-".repeat(40));

        System.out.print("Número de entrada: ");
        String numero = scanner.nextLine().trim();

        EntradaProducto entrada = entradaRepo.buscarPorNumero(numero);

        if (entrada == null) {
            System.out.println("❌ Entrada no encontrada.");
        } else {
            mostrarDetalleEntrada(entrada);
        }
        esperarEnter();
    }

    private void mostrarDetalleEntrada(EntradaProducto entrada) {
        System.out.println("\n📋 DETALLE DE ENTRADA");
        System.out.println("═".repeat(70));
        System.out.println("Número: " + entrada.getNumeroEntrada());
        System.out.println("Fecha: " + entrada.getFechaEntrada());
        System.out.println(
                "Proveedor: " + entrada.getProveedor().getNombre() + " - " + entrada.getProveedor().getEmpresa());
        System.out.println("Factura: " + entrada.getNumeroFactura());
        System.out.println("Almacén: " + entrada.getAlmacenDestino().getNombre());
        System.out.println("Responsable: " + entrada.getResponsable().getNombre());

        if (entrada.getObservaciones() != null && !entrada.getObservaciones().isEmpty()) {
            System.out.println("Observaciones: " + entrada.getObservaciones());
        }

        System.out.println("\n📦 PRODUCTOS:");
        System.out.println("-".repeat(70));
        System.out.printf("%-5s %-30s %-10s %-12s %-12s%n",
                "ID", "Producto", "Cantidad", "P. Compra", "Subtotal");
        System.out.println("-".repeat(70));

        for (DetalleEntrada d : entrada.getDetalles()) {
            System.out.printf("%-5d %-30s %-10d $%-11.2f $%-11.2f%n",
                    d.getProducto().getId(),
                    truncar(d.getProducto().getNombre(), 29),
                    d.getCantidad(),
                    d.getPrecioCompraUnitario(),
                    d.getSubtotal());
        }

        System.out.println("-".repeat(70));
        System.out.printf("TOTAL ENTRADA: $%.2f%n", entrada.getTotalEntrada());
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
