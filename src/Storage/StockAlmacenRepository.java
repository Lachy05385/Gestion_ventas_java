package src.Storage;

import src.modelo.Producto;
import src.modelo.Almacen;
import src.modelo.StockAlmacen;
import java.util.ArrayList;
import java.util.List;

public class StockAlmacenRepository {
    private List<StockAlmacen> stocks;
    private static StockAlmacenRepository instancia;
    private int nextId;

    // ✅ Constructor privado (patrón Singleton)
    private StockAlmacenRepository() {
        this.stocks = new ArrayList<>();
        this.nextId = 1;
        inicializarStockDemo();
    }

    public static StockAlmacenRepository getInstance() {
        if (instancia == null) {
            instancia = new StockAlmacenRepository();
        }
        return instancia;
    }

    private void inicializarStockDemo() {

        System.out.println("\n🔍 DEBUG - INICIANDO INICIALIZACIÓN DE STOCK");

        ProductoRepository productRepo = ProductoRepository.getInstance();
        AlmacenRepository almacenRepo = AlmacenRepository.getInstance();

        List<Producto> productos = productRepo.obtenerTodos();
        List<Almacen> almacenes = almacenRepo.obtenerAlmacenesActivos();

        System.out.println("DEBUG - Productos encontrados: " + (productos != null ? productos.size() : "null"));
        System.out.println("DEBUG - Almacenes encontrados: " + (almacenes != null ? almacenes.size() : "null"));

        if (productos == null || productos.isEmpty()) {
            System.out.println("❌ ERROR: No hay productos. ¿Se inicializaron correctamente?");
            return;
        }

        if (almacenes == null || almacenes.isEmpty()) {
            System.out.println("❌ ERROR: No hay almacenes. ¿Se inicializaron correctamente?");
            return;
        }

        // Mostrar qué productos hay
        System.out.println("\n📋 PRODUCTOS DISPONIBLES:");
        for (Producto p : productos) {
            System.out.println("   - ID: " + p.getId() + " | " + p.getNombre() + " | Código: " + p.getCodigo());
        }

        // Mostrar qué almacenes hay
        System.out.println("\n🏪 ALMACENES DISPONIBLES:");
        for (Almacen a : almacenes) {
            System.out.println("   - ID: " + a.getId() + " | " + a.getNombre() + " | Central: " + a.isEsCentral());
        }

        // Stock para CADA almacén
        for (Almacen almacen : almacenes) {
            System.out.println("\n🔧 Creando stock para almacén: " + almacen.getNombre());
            for (Producto producto : productos) {
                int cantidadInicial = almacen.isEsCentral() ? 100 : 10;
                StockAlmacen stock = new StockAlmacen(producto, almacen, cantidadInicial);
                stock.setId(nextId++);
                stocks.add(stock);
                System.out.println("   + " + producto.getNombre() + ": " + cantidadInicial + " unidades");
            }
        }

        System.out.println("\n✅ TOTAL: " + stocks.size() + " registros de stock creados.");
    }

    // Obtener stock de un producto en todos los almacenes
    public List<StockAlmacen> obtenerStockPorProducto(int productoId) {
        List<StockAlmacen> resultado = new ArrayList<>();
        for (StockAlmacen stock : stocks) {
            if (stock.getProducto().getId() == productoId) {
                resultado.add(stock);
            }
        }
        return resultado;
    }

    // Obtener stock de un almacén específico
    public List<StockAlmacen> obtenerStockPorAlmacen(int almacenId) {
        List<StockAlmacen> resultado = new ArrayList<>();
        for (StockAlmacen stock : stocks) {
            if (stock.getAlmacen().getId() == almacenId) {
                resultado.add(stock);
            }
        }
        return resultado;
    }

    // Obtener stock de un producto en un almacén específico
    public StockAlmacen obtenerStockPorProductoYAlmacen(int productoId, int almacenId) {
        for (StockAlmacen stock : stocks) {
            if (stock.getProducto().getId() == productoId && stock.getAlmacen().getId() == almacenId) {
                return stock;
            }
        }
        return null;
    }

    // Agregar stock a un producto en un almacén
    public boolean agregarStock(int productoId, int almacenId, int cantidad) {
        StockAlmacen stock = obtenerStockPorProductoYAlmacen(productoId, almacenId);
        if (stock != null) {
            stock.aumentarStock(cantidad);
            System.out.println("✅ Stock aumentado: +" + cantidad + " de " + stock.getProducto().getNombre());
            return true;
        }
        System.out.println("❌ No se encontró stock para producto " + productoId + " en almacén " + almacenId);
        return false;
    }

    // Disminuir stock (para transferencias o ventas)
    public boolean disminuirStock(int productoId, int almacenId, int cantidad) {
        StockAlmacen stock = obtenerStockPorProductoYAlmacen(productoId, almacenId);
        if (stock != null) {
            if (stock.tieneStockSuficiente(cantidad)) {
                stock.disminuirStock(cantidad);
                System.out.println("✅ Stock disminuido: -" + cantidad + " de " + stock.getProducto().getNombre());
                return true;
            } else {
                System.out.println("❌ Stock insuficiente. Disponible: " + stock.getCantidad());
            }
        } else {
            System.out.println("❌ No se encontró stock para producto " + productoId + " en almacén " + almacenId);
        }
        return false;
    }

    // Obtener productos con stock bajo (debajo del mínimo)
    public List<StockAlmacen> obtenerStockBajo() {
        List<StockAlmacen> resultado = new ArrayList<>();
        for (StockAlmacen stock : stocks) {
            if (stock.estaBajoStockMinimo()) {
                resultado.add(stock);
            }
        }
        return resultado;
    }

    // Obtener productos sin stock
    public List<StockAlmacen> obtenerProductosAgotados() {
        List<StockAlmacen> resultado = new ArrayList<>();
        for (StockAlmacen stock : stocks) {
            if (stock.getCantidad() == 0) {
                resultado.add(stock);
            }
        }
        return resultado;
    }

    // Obtener estadísticas de stock
    public String obtenerEstadisticas() {
        int totalRegistros = stocks.size();
        int totalUnidades = 0;
        int productosBajoStock = 0;
        int productosAgotados = 0;

        for (StockAlmacen stock : stocks) {
            totalUnidades += stock.getCantidad();
            if (stock.estaBajoStockMinimo()) {
                productosBajoStock++;
            }
            if (stock.getCantidad() == 0) {
                productosAgotados++;
            }
        }

        return String.format(
                "📊 ESTADÍSTICAS DE STOCK:\n" +
                        "   • Total registros: %d\n" +
                        "   • Total unidades: %d\n" +
                        "   • Productos con stock bajo: %d\n" +
                        "   • Productos agotados: %d",
                totalRegistros, totalUnidades, productosBajoStock, productosAgotados);
    }
}