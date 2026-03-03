package src.Storage;

import src.modelo.Producto;
import src.modelo.Categoria;

import java.util.ArrayList;
import java.util.List;

public class ProductoRepository {
    private List<Producto> productos;
    private static ProductoRepository instancia;

    private ProductoRepository() {
        this.productos = new ArrayList<>();
        // No inicializamos productos demo aquí porque dependen de categorías
    }

    public static ProductoRepository getInstance() {
        if (instancia == null) {
            instancia = new ProductoRepository();
            // ← Asegurar que esto se ejecuta
        }
        return instancia;
    }

    public void inicializarProductosDemo() {
        if (!productos.isEmpty()) {
            System.out.println("⚠️ Productos ya existen: " + productos.size());
            return;
        }

        CategoriaRepository catRepo = CategoriaRepository.getInstance();
        List<Categoria> categorias = catRepo.obtenerTodasCategorias();

        if (categorias.isEmpty())
            return;

        // Crear algunos productos de ejemplo
        Producto p1 = new Producto(0, "PROD-001", "Laptop HP", categorias.get(0), "Unidad", 4500.00, 5500.00, 5, "img");
        p1.setId(1);
        productos.add(p1);

        Producto p2 = new Producto(0, "PROD-002", "Camisa Casual", categorias.get(1), "Unidad", 120.00, 180.00, 20,
                "img");
        p2.setId(2);
        productos.add(p2);

        Producto p3 = new Producto(0, "PROD-003", "Arroz 5kg", categorias.get(2), "Bolsa", 25.00, 32.00, 50, "img");
        p3.setId(3);
        productos.add(p3);

        Producto p4 = new Producto(0, "PROD-004", "Refresco 2L", categorias.get(3), "Botella", 8.00, 12.00, 30, "img");
        p4.setId(4);
        productos.add(p4);

        Producto p5 = new Producto(0, "PROD-005", "Detergente 1kg", categorias.get(4), "Bolsa", 15.00, 22.00, 40,
                "img");
        p5.setId(5);
        productos.add(p5);

        System.out.println("✅ " + productos.size() + " productos inicializados");
    }

    // CRUD methods
    public boolean agregarProducto(Producto producto) {
        if (producto == null)
            return false;

        // Validar que el código no exista
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                System.out.println("❌ Ya existe un producto con ese código");
                return false;
            }
        }

        if (producto.getId() <= 0) {
            int nuevoId = 1;
            for (Producto p : productos) {
                if (p.getId() >= nuevoId)
                    nuevoId = p.getId() + 1;
            }
            producto.setId(nuevoId);
        }

        productos.add(producto);
        System.out.println("✅ Producto agregado: " + producto.getNombre() + " (" + producto.getCodigo() + ")");
        return true;
    }

    public List<Producto> obtenerTodos() {
        return productos;
    }

    public List<Producto> obtenerProductosActivos(){
        List<Producto>activos;
        for (Producto prod : productos){
            if (prod.isActivo()){
                activos.add(prod)
            }
        }
        return activos; 
    }

    public Producto buscarPorId(int id) {
        for (Producto p : productos) {
            if (p.getId() == id)
                return p;
        }
        return null;
    }

    public Producto buscarPorCodigo(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo))
                return p;
        }
        return null;
    }

    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> resultados = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    public List<Producto> buscarPorCategoria(int categoriaId) {
        List<Producto> resultados = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getCategoria().getId() == categoriaId) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    public boolean actualizarProducto(Producto productoActualizado) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId() == productoActualizado.getId()) {
                productos.set(i, productoActualizado);
                return true;
            }
        }
        return false;
    }

    public boolean eliminarProducto(int id) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId() == id) {
                productos.remove(i);
                return true;
            }
        }
        return false;
    }

    public int contarProductos() {
        return productos.size();
    }
}