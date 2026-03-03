package src.modelo;

import src.modelo.*;
import src.Storage.*;
import src.seguridad.SessionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PuntoVentaMenu {
    private Scanner scanner;
    private SessionManager sessionManager;
    private ProductoRepository productRepo;
    private StockAlmacenRepository stockRepo;
    private ClienteRepository clienteRepo;
    private VentaRepository ventaRepo;
    private AlmacenRepository almacenRepo;
    private TurnoRepository turnoRepo;
    private boolean salirMenu;

    // Carrito de compras temporal
    private List<DetalleVenta> carrito;
    private Venta ventaActual;
    private Turno turnoActual;

    public PuntoVentaMenu(Scanner scanner) {
        this.scanner = scanner;
        this.sessionManager = SessionManager.getInstance();
        this.productRepo = ProductoRepository.getInstance();
        this.stockRepo = StockAlmacenRepository.getInstance();
        this.clienteRepo = ClienteRepository.getInstance();
        this.ventaRepo = VentaRepository.getInstance();
        this.almacenRepo = AlmacenRepository.getInstance();
        this.turnoRepo = TurnoRepository.getInstance();
        this.salirMenu = false;
        this.carrito = new ArrayList<>();
        this.turnoActual = null;
    }

    public void mostrarMenu() {
        while (!salirMenu) {
            limpiarPantalla();

            // Verificar si hay turno activo
            Usuario usuario = sessionManager.getUsuarioActual();
            turnoActual = turnoRepo.obtenerTurnoActivoPorUsuario(usuario.getId());

            if (turnoActual == null) {
                mostrarMenuSinTurno();
                int opcion = obtenerOpcion();
                procesarOpcionSinTurno(opcion);
            } else {
                mostrarMenuConTurno();
                int opcion = obtenerOpcion();

                if (carrito.isEmpty()) {
                    procesarOpcionConTurno(opcion);
                } else {
                    procesarOpcionCarrito(opcion);
                }
            }
        }
    }

    private void mostrarMenuSinTurno() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("              🛒 PUNTO DE VENTA");
        System.out.println("═".repeat(60));

        Usuario usuario = sessionManager.getUsuarioActual();
        System.out.println("Vendedor: " + usuario.getNombre());
        System.out.println("\n⚠️  No tiene un turno activo.");
        System.out.println("\n1. ➕ Iniciar turno");
        System.out.println("2. 📊 Ver mis turnos anteriores");
        System.out.println("0. ↩️ Volver");
    }

    private void mostrarMenuConTurno() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("              🛒 PUNTO DE VENTA");
        System.out.println("═".repeat(70));

        System.out.printf("Vendedor: %-30s Almacén: %s%n",
                turnoActual.getUsuario().getNombre(),
                turnoActual.getAlmacen().getNombre());
        System.out.printf("Turno iniciado: %-20s Efectivo inicial: $%.2f%n",
                turnoActual.getFechaInicio().toString().substring(0, 19),
                turnoActual.getEfectivoInicial());
        System.out.printf("Ventas turno: %d | Total: $%.2f (Efectivo: $%.2f / Transf: $%.2f)%n",
                ventaRepo.buscarPorVendedor(turnoActual.getUsuario().getId()).size(),
                turnoActual.getTotalVentas(),
                turnoActual.getVentasEfectivo(),
                turnoActual.getVentasTransferencia());

        if (!carrito.isEmpty()) {
            System.out.println("-".repeat(70));
            System.out.printf("🛒 CARRITO: %d productos | Total: $%.2f%n",
                    carrito.size(), calcularTotalCarrito());
        }

        System.out.println("\n" + "-".repeat(70));

        if (carrito.isEmpty()) {
            System.out.println("1. ➕ Nueva venta");
            System.out.println("2. 👥 Gestión de clientes");
            System.out.println("3. 📋 Ventas del turno");
            System.out.println("4. 🔒 Cerrar turno");
            System.out.println("0. ↩️ Volver");
        } else {
            System.out.println("🛒 PRODUCTOS EN CARRITO:");
            System.out.println("-".repeat(70));
            System.out.printf("%-5s %-25s %-10s %-12s %-12s%n",
                    "ID", "Producto", "Cantidad", "Precio", "Subtotal");
            System.out.println("-".repeat(70));

            for (DetalleVenta d : carrito) {
                System.out.printf("%-5d %-25s %-10d $%-11.2f $%-11.2f%n",
                        d.getProducto().getId(),
                        truncar(d.getProducto().getNombre(), 24),
                        d.getCantidad(),
                        d.getPrecioUnitario(),
                        d.getSubtotal());
            }
            System.out.println("-".repeat(70));
            System.out.printf("TOTAL: $%.2f%n", calcularTotalCarrito());

            System.out.println("\n1. ➕ Agregar producto");
            System.out.println("2. ➖ Quitar producto");
            System.out.println("3. 👤 Seleccionar cliente");
            System.out.println("4. 💰 Procesar pago");
            System.out.println("5. ❌ Cancelar venta");
        }
    }

    private int obtenerOpcion() {
        System.out.print("\n👉 Seleccione: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void procesarOpcionSinTurno(int opcion) {
        switch (opcion) {
            case 1:
                iniciarTurno();
                break;
            case 2:
                verMisTurnos();
                break;
            case 0:
                salirMenu = true;
                break;
            default:
                System.out.println("❌ Opción inválida");
                esperarEnter();
        }
    }

    private void procesarOpcionConTurno(int opcion) {
        switch (opcion) {
            case 1:
                nuevaVenta();
                break;
            case 2:
                gestionarClientes();
                break;
            case 3:
                verVentasTurno();
                break;
            case 4:
                cerrarTurno();
                break;
            case 0:
                salirMenu = true;
                break;
            default:
                System.out.println("❌ Opción inválida");
                esperarEnter();
        }
    }

    private void procesarOpcionCarrito(int opcion) {
        switch (opcion) {
            case 1:
                agregarProducto();
                break;
            case 2:
                quitarProducto();
                break;
            case 3:
                seleccionarCliente();
                break;
            case 4:
                procesarPago();
                break;
            case 5:
                cancelarVenta();
                break;
            default:
                System.out.println("❌ Opción inválida");
                esperarEnter();
        }
    }

    private void iniciarTurno() {
        System.out.println("\n➕ INICIAR TURNO");
        System.out.println("-".repeat(40));

        Usuario usuario = sessionManager.getUsuarioActual();
        System.out.println("DEBUG - Usuario actual: " + usuario.getNombre() + " (ID: " + usuario.getId() + ")");

        // Seleccionar almacén donde trabajará
        List<Almacen> almacenes = almacenRepo.obtenerAlmacenesActivos();
        System.out.println("DEBUG - Almacenes activos encontrados: " + almacenes.size());

        if (almacenes.isEmpty()) {
            System.out.println("❌ No hay almacenes disponibles.");
            esperarEnter();
            return;
        }

        System.out.println("\nAlmacenes disponibles:");
        for (Almacen a : almacenes) {
            System.out.printf("   %d. %s%n", a.getId(), a.getNombre());
        }

        System.out.print("\nSeleccione almacén: ");
        try {
            int almacenId = Integer.parseInt(scanner.nextLine().trim());
            System.out.println("DEBUG - Almacén seleccionado ID: " + almacenId);

            Almacen almacen = almacenRepo.buscarPorId(almacenId);

            if (almacen == null) {
                System.out.println("❌ Almacén no válido.");
                esperarEnter();
                return;
            }

            System.out.println("DEBUG - Almacén encontrado: " + almacen.getNombre());

            System.out.print("Efectivo inicial en caja: $");
            double efectivoInicial = Double.parseDouble(scanner.nextLine().trim());
            System.out.println("DEBUG - Efectivo inicial: $" + efectivoInicial);

            Turno turno = new Turno(usuario, almacen, efectivoInicial);
            System.out.println("DEBUG - Turno creado. Estado: " + turno.getEstado());

            if (turnoRepo.agregarTurno(turno)) {
                turnoActual = turno;
                System.out.println("✅ Turno iniciado exitosamente.");
                System.out.println("DEBUG - turnoActual asignado. ID: " + turnoActual.getId());
            } else {
                System.out.println("❌ Error al guardar el turno.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
            e.printStackTrace();
        }
        esperarEnter();
    }

    private void cerrarTurno() {
        System.out.println("\n🔒 CERRAR TURNO");
        System.out.println("-".repeat(40));

        if (!carrito.isEmpty()) {
            System.out.println("⚠️  Hay una venta en proceso. Debe completarla o cancelarla.");
            esperarEnter();
            return;
        }

        System.out.printf("Resumen del turno:%n");
        System.out.printf("   Ventas efectivo: $%.2f%n", turnoActual.getVentasEfectivo());
        System.out.printf("   Ventas transfer: $%.2f%n", turnoActual.getVentasTransferencia());
        System.out.printf("   Total ventas: $%.2f%n", turnoActual.getTotalVentas());
        System.out.printf("   Efectivo inicial: $%.2f%n", turnoActual.getEfectivoInicial());

        System.out.print("\nEfectivo final en caja: $");
        try {
            double efectivoFinal = Double.parseDouble(scanner.nextLine().trim());

            String resultado = turnoActual.cerrarTurno(efectivoFinal);
            System.out.println("\n" + resultado);

            turnoActual = null;

        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
        }
        esperarEnter();
    }

    private void verMisTurnos() {
        System.out.println("\n📋 MIS TURNOS");
        System.out.println("-".repeat(60));

        Usuario usuario = sessionManager.getUsuarioActual();
        List<Turno> turnos = turnoRepo.obtenerTurnosPorUsuario(usuario.getId());

        if (turnos.isEmpty()) {
            System.out.println("No tiene turnos registrados.");
        } else {
            System.out.printf("%-5s %-15s %-15s %-10s %-12s%n",
                    "ID", "Fecha", "Almacén", "Ventas", "Estado");
            System.out.println("-".repeat(60));

            for (Turno t : turnos) {
                System.out.printf("%-5d %-15s %-15s $%-9.2f %-10s%n",
                        t.getId(),
                        t.getFechaInicio().toString().substring(0, 10),
                        t.getAlmacen().getNombre(),
                        t.getTotalVentas(),
                        t.getEstado());
            }
        }
        esperarEnter();
    }

    private void verVentasTurno() {
        System.out.println("\n📋 VENTAS DEL TURNO");
        System.out.println("-".repeat(70));

        List<Venta> ventas = ventaRepo.buscarPorVendedor(turnoActual.getUsuario().getId());

        if (ventas.isEmpty()) {
            System.out.println("No hay ventas en este turno.");
        } else {
            System.out.printf("%-15s %-15s %-20s %-12s%n",
                    "Número", "Hora", "Cliente", "Total");
            System.out.println("-".repeat(70));

            for (Venta v : ventas) {
                System.out.printf("%-15s %-15s %-20s $%-11.2f%n",
                        v.getNumeroVenta(),
                        v.getFechaVenta().toString().substring(11, 19),
                        truncar(v.getCliente().getNombre(), 19),
                        v.getTotal());
            }
            System.out.println("-".repeat(70));
            System.out.printf("TOTAL VENTAS TURNO: $%.2f%n", turnoActual.getTotalVentas());
        }
        esperarEnter();
    }

    private void nuevaVenta() {
        ventaActual = new Venta();
        ventaActual.setVendedor(turnoActual.getUsuario());
        ventaActual.setAlmacen(turnoActual.getAlmacen());

        Cliente clienteDefecto = clienteRepo.buscarPorDocumento("000-0000000-0");
        ventaActual.setCliente(clienteDefecto);

        carrito.clear();
        System.out.println("\n✅ Nueva venta iniciada.");

        // 👇 NUEVA LÍNEA: Ir directamente a agregar productos
        agregarProducto(); // ← Esto muestra los productos inmediatamente
    }

    private void agregarProducto() {
        System.out.println("\n➕ AGREGAR PRODUCTO");
        System.out.println("-".repeat(40));
        System.out.println("Ejecutando Ventas...");
        esperarEnter();
        try {
            List<StockAlmacen> stock = stockRepo.obtenerStockPorAlmacen(turnoActual.getAlmacen().getId());

            if (stock.isEmpty()) {
                System.out.println("❌ No hay productos disponibles.");
                esperarEnter();
                return;
            }

            System.out.println("\nProductos disponibles:");
            System.out.printf("%-5s %-25s %-10s %-12s%n",
                    "ID", "Producto", "Stock", "Precio");
            System.out.println("-".repeat(60));

            for (StockAlmacen s : stock) {
                if (s.getCantidad() > 0) {
                    System.out.printf("%-5d %-25s %-10d $%-11.2f%n",
                            s.getProducto().getId(),
                            truncar(s.getProducto().getNombre(), 24),
                            s.getCantidad(),
                            s.getProducto().getPrecioVenta());
                }
            }

            System.out.print("\nID del producto: ");
            int productoId = Integer.parseInt(scanner.nextLine().trim());

            Producto producto = productRepo.buscarPorId(productoId);
            if (producto == null) {
                System.out.println("❌ Producto no encontrado.");
                esperarEnter();
                return;
            }

            StockAlmacen stockProd = stockRepo.obtenerStockPorProductoYAlmacen(
                    productoId, turnoActual.getAlmacen().getId());

            if (stockProd == null || stockProd.getCantidad() == 0) {
                System.out.println("❌ Producto sin stock.");
                esperarEnter();
                return;
            }

            System.out.print("Cantidad: ");
            int cantidad = Integer.parseInt(scanner.nextLine().trim());

            if (cantidad <= 0 || cantidad > stockProd.getCantidad()) {
                System.out.println("❌ Cantidad inválida o insuficiente stock.");
                esperarEnter();
                return;
            }

            // Verificar si ya está en carrito
            for (DetalleVenta d : carrito) {
                if (d.getProducto().getId() == productoId) {
                    System.out.println("❌ Producto ya está en el carrito.");
                    esperarEnter();
                    return;
                }
            }

            DetalleVenta detalle = new DetalleVenta(producto, cantidad, producto.getPrecioVenta());
            carrito.add(detalle);
            System.out.printf("✅ Producto agregado: %s (%d unidades)%n",
                    producto.getNombre(), cantidad);

        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
        }
        esperarEnter();
    }

    private void quitarProducto() {
        System.out.println("\n➖ QUITAR PRODUCTO");
        System.out.println("-".repeat(40));

        if (carrito.isEmpty()) {
            System.out.println("El carrito está vacío.");
            esperarEnter();
            return;
        }

        System.out.println("Productos en carrito:");
        for (int i = 0; i < carrito.size(); i++) {
            DetalleVenta d = carrito.get(i);
            System.out.printf("   %d. %s (%d unidades)%n",
                    i + 1, d.getProducto().getNombre(), d.getCantidad());
        }

        System.out.print("\nNúmero del producto a quitar: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;

            if (idx >= 0 && idx < carrito.size()) {
                DetalleVenta removido = carrito.remove(idx);
                System.out.printf("✅ Producto removido: %s%n",
                        removido.getProducto().getNombre());
            } else {
                System.out.println("❌ Número inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
        }
        esperarEnter();
    }

    private void seleccionarCliente() {
        System.out.println("\n👤 SELECCIONAR CLIENTE");
        System.out.println("-".repeat(40));

        System.out.println("1. Consumidor Final");
        System.out.println("2. Buscar por documento");
        System.out.println("3. Buscar por nombre");
        System.out.print("Seleccione: ");

        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());

            switch (opcion) {
                case 1:
                    Cliente finalCliente = clienteRepo.buscarPorDocumento("000-0000000-0");
                    ventaActual.setCliente(finalCliente);
                    System.out.println("✅ Cliente: Consumidor Final");
                    break;

                case 2:
                    System.out.print("Documento: ");
                    String doc = scanner.nextLine().trim();
                    Cliente cliente = clienteRepo.buscarPorDocumento(doc);
                    if (cliente != null) {
                        ventaActual.setCliente(cliente);
                        System.out.println("✅ Cliente: " + cliente.getNombre());
                    } else {
                        System.out.println("❌ Cliente no encontrado.");
                    }
                    break;

                case 3:
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine().trim();
                    List<Cliente> resultados = clienteRepo.buscarPorNombre(nombre);
                    if (resultados.isEmpty()) {
                        System.out.println("No se encontraron clientes.");
                    } else {
                        System.out.println("\nResultados:");
                        for (int i = 0; i < resultados.size(); i++) {
                            Cliente c = resultados.get(i);
                            System.out.printf("   %d. %s (%s)%n",
                                    i + 1, c.getNombre(), c.getDocumento());
                        }
                        System.out.print("Seleccione: ");
                        int sel = Integer.parseInt(scanner.nextLine().trim()) - 1;
                        if (sel >= 0 && sel < resultados.size()) {
                            ventaActual.setCliente(resultados.get(sel));
                            System.out.println("✅ Cliente seleccionado.");
                        }
                    }
                    break;

                default:
                    System.out.println("❌ Opción inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
        }
        esperarEnter();
    }

    private void procesarPago() {
        if (carrito.isEmpty()) {
            System.out.println("❌ No hay productos en el carrito.");
            esperarEnter();
            return;
        }

        ventaActual.setDetalles(new ArrayList<>(carrito));
        ventaActual.calcularTotales();

        System.out.println("\n💰 PROCESAR PAGO");
        System.out.println("═".repeat(50));
        System.out.printf("TOTAL A PAGAR: $%.2f%n", ventaActual.getTotal());
        System.out.println("═".repeat(50));

        boolean pagoCompleto = false;

        while (!pagoCompleto) {
            try {
                System.out.printf("Efectivo recibido: $");
                double efectivo = Double.parseDouble(scanner.nextLine().trim());

                System.out.printf("Transferencia recibida: $");
                double transferencia = Double.parseDouble(scanner.nextLine().trim());

                ventaActual.setEfectivo(efectivo);
                ventaActual.setTransferencia(transferencia);

                String resultado = ventaActual.procesarPago();
                System.out.println(resultado);

                if (ventaActual.pagoCompleto()) {
                    pagoCompleto = true;

                    // Actualizar stock
                    for (DetalleVenta detalle : carrito) {
                        stockRepo.disminuirStock(
                                detalle.getProducto().getId(),
                                turnoActual.getAlmacen().getId(),
                                detalle.getCantidad());
                    }

                    // Actualizar turno
                    turnoActual.agregarVenta(efectivo, transferencia);

                    // Guardar venta
                    ventaRepo.agregarVenta(ventaActual);

                    // Mostrar ticket
                    mostrarTicket(ventaActual);

                    // Limpiar carrito
                    carrito.clear();
                    ventaActual = null;

                } else {
                    System.out.println("\n⚠️ Intente nuevamente con otro monto.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Monto inválido.");
            }
        }

        esperarEnter();
    }

    private void cancelarVenta() {
        System.out.print("\n❌ ¿Cancelar venta? (s/n): ");
        if (scanner.nextLine().trim().toLowerCase().equals("s")) {
            carrito.clear();
            ventaActual = null;
            System.out.println("✅ Venta cancelada.");
        }
        esperarEnter();
    }

    private void gestionarClientes() {
        ClienteMenu clienteMenu = new ClienteMenu(scanner);
        clienteMenu.mostrarMenu();
    }

    private void mostrarTicket(Venta venta) {
        System.out.println("\n" + "═".repeat(40));
        System.out.println("           🧾 TICKET DE VENTA");
        System.out.println("═".repeat(40));
        System.out.println("Número: " + venta.getNumeroVenta());
        System.out.println("Fecha: " + venta.getFechaVenta());
        System.out.println("Vendedor: " + venta.getVendedor().getNombre());
        System.out.println("Cliente: " + venta.getCliente().getNombre());
        System.out.println("-".repeat(40));

        for (DetalleVenta d : venta.getDetalles()) {
            System.out.printf("%-20s %d x $%.2f = $%.2f%n",
                    truncar(d.getProducto().getNombre(), 19),
                    d.getCantidad(),
                    d.getPrecioUnitario(),
                    d.getSubtotal());
        }
        System.out.println("-".repeat(40));
        System.out.printf("TOTAL: $%.2f%n", venta.getTotal());
        System.out.println("═".repeat(40));
        System.out.printf("EFECTIVO: $%.2f%n", venta.getEfectivo());
        System.out.printf("TRANSFERENCIA: $%.2f%n", venta.getTransferencia());
        System.out.println("═".repeat(40));
        System.out.println("     ¡GRACIAS POR SU COMPRA!");
        System.out.println("═".repeat(40));
    }

    private double calcularTotalCarrito() {
        double total = 0;
        for (DetalleVenta d : carrito) {
            total += d.getSubtotal();
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