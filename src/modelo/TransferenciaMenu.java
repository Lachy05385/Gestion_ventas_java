
package src.modelo;

//import src.modelo.*;
import src.Storage.*;
import src.seguridad.SessionManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
//import src.modelo.Transferencia;

public class TransferenciaMenu {
    private Scanner scanner;
    private TransferenciaRepository transferenciaRepo;
    private StockAlmacenRepository stockRepo;
    private ProductoRepository productRepo;
    private AlmacenRepository almacenRepo;
    private SessionManager sessionManager;
    private boolean salirMenu;

    public TransferenciaMenu(Scanner scanner) {
        this.scanner = scanner;
        this.transferenciaRepo = TransferenciaRepository.getInstance();
        this.stockRepo = StockAlmacenRepository.getInstance();
        this.productRepo = ProductoRepository.getInstance();
        this.almacenRepo = AlmacenRepository.getInstance();
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
        System.out.println("            🔄 TRANSFERENCIAS ENTRE ALMACENES");
        System.out.println("═".repeat(60));
    }

    private void mostrarOpciones() {
        System.out.println("\n1. 📝 Solicitar nueva transferencia");
        System.out.println("2. 📋 Ver transferencias pendientes de aprobación");
        System.out.println("3. ✅ Aprobar/Rechazar transferencia");
        System.out.println("4. 📊 Ver todas las transferencias");
        System.out.println("5. 🔍 Buscar transferencia por número");
        System.out.println("6. 📦 Ver transferencias por almacén");
        System.out.println("0. ↩️ Volver al menú principal");
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
                solicitarTransferencia();
                break;
            case 2:
                verPendientes();
                break;
            case 3:
                aprobarRechazar();
                break;
            case 4:
                verTodas();
                break;
            case 5:
                buscarTransferencia();
                break;
            case 6:
                verPorAlmacen();
                break;
            case 0:
                salirMenu = true;
                break;
            default:
                System.out.println("❌ Opción inválida");
                esperarEnter();
        }
    }

    private void solicitarTransferencia() {
        System.out.println("\n📝 SOLICITAR NUEVA TRANSFERENCIA");
        System.out.println("-".repeat(50));

        try {
            // Seleccionar almacén origen (debe ser el central)
            Almacen central = almacenRepo.obtenerAlmacenCentral();
            if (central == null) {
                System.out.println("❌ No hay almacén central configurado.");
                esperarEnter();
                return;
            }

            System.out.println("🏪 Almacén origen: " + central.getNombre() + " [CENTRAL]");

            // Seleccionar almacén destino
            List<Almacen> destinos = almacenRepo.obtenerAlmacenesActivos();
            if (destinos.isEmpty()) {
                System.out.println("❌ No hay almacenes destino disponibles.");
                esperarEnter();
                return;
            }

            System.out.println("\nAlmacenes destino disponibles:");
            for (Almacen d : destinos) {
                System.out.printf("   %d. %s (%s)%n", d.getId(), d.getNombre(), d.getCodigo());
            }

            System.out.print("Seleccione ID del almacén destino: ");
            int destinoId = Integer.parseInt(scanner.nextLine().trim());
            Almacen destino = almacenRepo.buscarPorId(destinoId);

            if (destino == null || !destino.isActivo()) {
                System.out.println("❌ Almacén destino no válido.");
                esperarEnter();
                return;
            }

            // Crear nueva transferencia
            Transferencia transferencia = new Transferencia();
            transferencia.setId(transferencia.getId());
            transferencia.setCodigo(generarNumeroTransferencia());
            transferencia.setAlmacenOrigen(central);
            transferencia.setAlmacenDestino(destino);
            transferencia.setFechaSolicitud(new Date());
            transferencia.setSolicitante(sessionManager.getUsuarioActual());
            transferencia.setEstado("PENDIENTE");

            List<DetalleTransferencia> detalles = new ArrayList<>();

            boolean agregarProductos = true;
            while (agregarProductos) {
                System.out.println("\n" + "-".repeat(40));

                // Mostrar productos disponibles en almacén central
                List<StockAlmacen> stockCentral = stockRepo.obtenerStockPorAlmacen(central.getId());
                System.out.println("\nProductos disponibles en almacén central:");
                System.out.printf("%-5s %-25s %-10s%n", "ID", "Producto", "Stock");
                System.out.println("-".repeat(45));

                for (StockAlmacen s : stockCentral) {
                    if (s.getCantidad() > 0) {
                        System.out.printf("%-5d %-25s %-10d%n",
                                s.getProducto().getId(),
                                truncar(s.getProducto().getNombre(), 24),
                                s.getCantidad());
                    }
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

                StockAlmacen stockCentralProd = stockRepo.obtenerStockPorProductoYAlmacen(
                        productoId, central.getId());

                if (stockCentralProd == null || stockCentralProd.getCantidad() == 0) {
                    System.out.println("❌ Producto sin stock en almacén central.");
                    continue;
                }

                System.out.print("Cantidad a solicitar: ");
                int cantidad = Integer.parseInt(scanner.nextLine().trim());

                if (cantidad <= 0) {
                    System.out.println("❌ Cantidad inválida.");
                    continue;
                }

                if (cantidad > stockCentralProd.getCantidad()) {
                    System.out.println("❌ Stock insuficiente. Stock disponible: " + stockCentralProd.getCantidad());
                    continue;
                }

                DetalleTransferencia detalle = new DetalleTransferencia();
                detalle.setId(detalles.size() + 1);
                detalle.setProducto(producto);
                detalle.setCantidad(cantidad);
                // detalle.setCantidadAprobada(0);
                detalle.setPrecioUnitario(producto.getPrecioCosto());
                detalles.add(detalle);

                System.out.printf("✅ Producto agregado: %s (%d unidades)%n", producto.getNombre(), cantidad);
            }

            if (detalles.isEmpty()) {
                System.out.println("❌ No se agregaron productos. Transferencia cancelada.");
                esperarEnter();
                return;
            }

            transferencia.setDetalles(detalles);

            System.out.print("\nObservaciones para la transferencia: ");
            String observaciones = scanner.nextLine().trim();
            transferencia.setObservaciones(observaciones);

            System.out.println("\n📋 RESUMEN DE TRANSFERENCIA:");
            System.out.println("   Número: " + transferencia.getCodigo());
            System.out.println("   Origen: " + central.getNombre());
            System.out.println("   Destino: " + destino.getNombre());
            System.out.println("   Productos: " + detalles.size() + " items");
            System.out.println("   Total unidades: "
                    + detalles.stream().mapToInt(DetalleTransferencia::getCantidad).sum());

            System.out.print("\n¿Confirmar solicitud? (s/n): ");
            if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                if (transferenciaRepo.agregarTransferencia(transferencia)) {
                    System.out.println("✅ Transferencia solicitada exitosamente.");
                } else {
                    System.out.println("❌ Error al crear la transferencia.");
                }
            } else {
                System.out.println("⚠️ Solicitud cancelada.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
        }
        esperarEnter();
    }

    private void verPendientes() {
        System.out.println("\n📋 TRANSFERENCIAS PENDIENTES DE APROBACIÓN");
        System.out.println("-".repeat(60));

        List<Transferencia> pendientes = transferenciaRepo.buscarPorEstado("PENDIENTE");

        if (pendientes.isEmpty()) {
            System.out.println("✅ No hay transferencias pendientes.");
        } else {
            System.out.printf("%-15s %-20s %-20s %-15s %-10s%n",
                    "Número", "Origen", "Destino", "Fecha", "Items");
            System.out.println("-".repeat(80));

            for (Transferencia t : pendientes) {
                System.out.printf("%-15s %-20s %-20s %-15s %-10d%n",
                        t.getCodigo(),
                        truncar(t.getAlmacenOrigen().getNombre(), 19),
                        truncar(t.getAlmacenDestino().getNombre(), 19),
                        t.getFechaSolicitud().toString().substring(0, 10),
                        t.getDetalles().size());
            }
        }
        esperarEnter();
    }

    private void aprobarRechazar() {
        System.out.println("\n✅ APROBAR/RECHAZAR TRANSFERENCIA");
        System.out.println("-".repeat(50));

        try {
            Usuario usuarioActual = sessionManager.getUsuarioActual();

            // Mostrar solo transferencias donde el usuario es el aprobador
            List<Transferencia> pendientes = transferenciaRepo.buscarPorEstado("PENDIENTE");
            // usuarioActual.getId() != null ? usuarioActual.getAlmacen().getId() : 0);

            if (pendientes.isEmpty()) {
                System.out.println("✅ No hay transferencias pendientes para su aprobación.");
                esperarEnter();
                return;
            }

            System.out.println("\nTransferencias pendientes:");
            for (int i = 0; i < pendientes.size(); i++) {
                Transferencia t = pendientes.get(i);
                System.out.printf("   %d. %s - %s → %s%n",
                        i + 1,
                        t.getCodigo(),
                        t.getAlmacenOrigen().getNombre(),
                        t.getAlmacenDestino().getNombre());
            }

            System.out.print("\nSeleccione número de transferencia: ");
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;

            if (idx < 0 || idx >= pendientes.size()) {
                System.out.println("❌ Selección inválida.");
                esperarEnter();
                return;
            }

            Transferencia transferencia = pendientes.get(idx);

            // Mostrar detalles
            System.out.println("\n📦 DETALLES DE TRANSFERENCIA");
            System.out.println("   Número: " + transferencia.getCodigo());
            System.out.println("   Solicitante: " + transferencia.getSolicitante().getNombre());
            System.out.println("   Fecha: " + transferencia.getFechaSolicitud());
            System.out.println("   Productos solicitados:");

            for (DetalleTransferencia d : transferencia.getDetalles()) {
                System.out.printf("      • %s: %d unidades%n",
                        d.getProducto().getNombre(),
                        d.getCantidad());
            }

            System.out.println("\n1. ✅ Aprobar");
            System.out.println("2. ❌ Rechazar");
            System.out.print("Seleccione: ");

            int accion = Integer.parseInt(scanner.nextLine().trim());

            if (accion == 1) {
                transferencia.setEstado("APROBADA");
                transferencia.setFechaAprobacion(new Date());
                transferencia.setAprobador(usuarioActual);

                // Actualizar stock
                for (DetalleTransferencia detalle : transferencia.getDetalles()) {
                    detalle.setCantidad(detalle.getCantidad());

                    // Disminuir stock en origen
                    stockRepo.disminuirStock(
                            detalle.getProducto().getId(),
                            transferencia.getAlmacenOrigen().getId(),
                            detalle.getCantidad());

                    // Aumentar stock en destino
                    stockRepo.agregarStock(
                            detalle.getProducto().getId(),
                            transferencia.getAlmacenDestino().getId(),
                            detalle.getCantidad());
                }

                System.out.println("✅ Transferencia APROBADA. Stock actualizado.");

            } else if (accion == 2) {
                transferencia.setEstado("RECHAZADA");
                System.out.print("Motivo del rechazo: ");
                String motivo = scanner.nextLine().trim();
                transferencia.setObservaciones(
                        (transferencia.getObservaciones() != null ? transferencia.getObservaciones() + "\n" : "") +
                                "RECHAZADA: " + motivo);

                System.out.println("⚠️ Transferencia RECHAZADA.");
            } else {
                System.out.println("❌ Opción inválida.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
        }
        esperarEnter();
    }

    private void verTodas() {
        System.out.println("\n📊 TODAS LAS TRANSFERENCIAS");
        System.out.println("-".repeat(80));

        List<Transferencia> todas = transferenciaRepo.obtenerTodas();

        if (todas.isEmpty()) {
            System.out.println("✅ No hay transferencias registradas.");
        } else {
            System.out.printf("%-15s %-20s %-20s %-15s %-10s %-10s%n",
                    "Número", "Origen", "Destino", "Fecha", "Estado", "Items");
            System.out.println("-".repeat(90));

            for (Transferencia t : todas) {
                System.out.printf("%-15s %-20s %-20s %-15s %-10s %-10d%n",
                        t.getCodigo(),
                        truncar(t.getAlmacenOrigen().getNombre(), 19),
                        truncar(t.getAlmacenDestino().getNombre(), 19),
                        t.getFechaSolicitud().toString().substring(0, 10),
                        t.getEstado(),
                        t.getDetalles().size());
            }
        }
        esperarEnter();
    }

    private void buscarTransferencia() {
        System.out.println("\n🔍 BUSCAR TRANSFERENCIA");
        System.out.println("-".repeat(40));

        System.out.print("Ingrese número de transferencia: ");
        String numero = scanner.nextLine().trim();

        Transferencia transferencia = transferenciaRepo.buscarPorCodigo(numero);

        if (transferencia == null) {
            System.out.println("❌ Transferencia no encontrada.");
        } else {
            mostrarDetalleTransferencia(transferencia);
        }
        esperarEnter();
    }

    private void verPorAlmacen() {
        System.out.println("\n📦 TRANSFERENCIAS POR ALMACÉN");
        System.out.println("-".repeat(40));

        try {
            List<Almacen> almacenes = almacenRepo.obtenerAlmacenesActivos();

            System.out.println("\nAlmacenes:");
            for (Almacen a : almacenes) {
                System.out.printf("   %d. %s%n", a.getId(), a.getNombre());
            }

            System.out.print("\nSeleccione ID del almacén: ");
            int almacenId = Integer.parseInt(scanner.nextLine().trim());

            Almacen almacen = almacenRepo.buscarPorId(almacenId);
            if (almacen == null) {
                System.out.println("❌ Almacén no encontrado.");
                esperarEnter();
                return;
            }

            List<Transferencia> comoOrigen = transferenciaRepo.buscarPorAlmacenOrigen(almacenId);
            List<Transferencia> comoDestino = transferenciaRepo.buscarPorAlmacenDestino(almacenId);

            System.out.println("\n📤 Transferencias como ORIGEN (" + comoOrigen.size() + "):");
            for (Transferencia t : comoOrigen) {
                System.out.printf("   • %s → %s (%s)%n",
                        t.getCodigo(),
                        t.getAlmacenDestino().getNombre(),
                        t.getEstado());
            }

            System.out.println("\n📥 Transferencias como DESTINO (" + comoDestino.size() + "):");
            for (Transferencia t : comoDestino) {
                System.out.printf("   • %s ← %s (%s)%n",
                        t.getCodigo(),
                        t.getAlmacenOrigen().getNombre(),
                        t.getEstado());
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
        }
        esperarEnter();
    }

    private void mostrarDetalleTransferencia(Transferencia t) {
        System.out.println("\n📋 DETALLE DE TRANSFERENCIA");
        System.out.println("═".repeat(60));
        System.out.println("Número: " + t.getCodigo());
        System.out.println("Estado: " + t.getEstado());
        System.out.println("Origen: " + t.getAlmacenOrigen().getNombre());
        System.out.println("Destino: " + t.getAlmacenDestino().getNombre());
        System.out.println("Solicitante: " + t.getSolicitante().getNombre());
        System.out.println("Fecha solicitud: " + t.getFechaSolicitud());

        if (t.getAprobador() != null) {
            System.out.println("Aprobador: " + t.getAprobador().getNombre());
            System.out.println("Fecha aprobación: " + t.getFechaAprobacion());
        }

        if (t.getObservaciones() != null && !t.getObservaciones().isEmpty()) {
            System.out.println("Observaciones: " + t.getObservaciones());
        }

        System.out.println("\n📦 PRODUCTOS:");
        System.out.println("-".repeat(50));
        System.out.printf("%-30s %-15s %-15s%n", "Producto", "Solicitado", "Aprobado");
        System.out.println("-".repeat(60));

        for (DetalleTransferencia d : t.getDetalles()) {
            System.out.printf("%-30s %-15d %-15d%n",
                    truncar(d.getProducto().getNombre(), 29),
                    d.getCantidad(),
                    d.getCantidad());
        }
    }

    private String generarNumeroTransferencia() {
        return "TRF-" + System.currentTimeMillis() % 10000 + "-" +
                new Date().toString().substring(0, 3).toUpperCase();
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
