package src.Storage;

import src.modelo.Categoria;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepository {
    private List<Categoria> categorias;
    private static CategoriaRepository instancia;
    private int nextId;

    private CategoriaRepository() {
        this.categorias = new ArrayList<>();
        this.nextId = 1;
        inicializarCategoriasDemo();
    }

    public static CategoriaRepository getInstance() {
        if (instancia == null) {
            instancia = new CategoriaRepository();
        }
        return instancia;
    }

    private void inicializarCategoriasDemo() {
        if (!categorias.isEmpty()) {
            return;
        }

        // Crear algunas categorías de ejemplo
        String[][] categoriasDemo = {
                { "ELECT", "Electrónica", "Productos electrónicos y gadgets" },
                { "ROPA", "Ropa y Accesorios", "Prendas de vestir y complementos" },
                { "ALIM", "Alimentos", "Productos alimenticios y bebidas" },
                { "HOGAR", "Hogar y Jardín", "Artículos para el hogar y jardinería" },
                { "OFIC", "Oficina", "Material de oficina y papelería" }
        };

        for (String[] cat : categoriasDemo) {
            Categoria categoria = new Categoria();
            categoria.setId(nextId++);
            categoria.setNombre(cat[1]);
            categoria.setDescripcion(cat[2]);
            categoria.setActivo(true);
            categorias.add(categoria);
        }

        System.out.println("✅ " + categorias.size() + " categorías de demostración inicializadas.");
    }

    // CRUD methods (similar a AlmacenRepository)
    public boolean agregarCategoria(Categoria categoria) {
        if (categoria == null) {
            System.err.println("Error: Categoría nula.");
            return false;
        }

        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            System.err.println("Error: El nombre de la categoría es obligatorio.");
            return false;
        }

        // Verificar que no exista una categoría con el mismo nombre (case insensitive)
        for (Categoria c : categorias) {
            if (c.getNombre().equalsIgnoreCase(categoria.getNombre().trim())) {
                System.err.println("Error: Ya existe una categoría con el nombre " + categoria.getNombre());
                return false;
            }
        }

        if (categoria.getId() <= 0) {
            categoria.setId(nextId++);
        }

        if (!categoria.isActivo()) {
            categoria.setActivo(true);
        }

        try {
            categorias.add(categoria);
            System.out.println("✅ Categoría agregada: " + categoria.getNombre() + " (ID: " + categoria.getId() + ")");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error al agregar categoría: " + e.getMessage());
            return false;
        }
    }

    public List<Categoria> obtenerTodasCategorias() {
        return new ArrayList<>(categorias);
    }

    public Categoria buscarPorId(int id) {
        for (Categoria categoria : categorias) {
            if (categoria.getId() == id) {
                return categoria;
            }
        }
        return null;
    }

    public Categoria buscarPorNombre(String nombre) {
        for (Categoria categoria : categorias) {
            if (categoria.getNombre().equalsIgnoreCase(nombre.trim())) {
                return categoria;
            }
        }
        return null;
    }

    public boolean actualizarCategoria(int id, Categoria categoriaActualizada) {
        Categoria categoriaExistente = buscarPorId(id);
        if (categoriaExistente == null) {
            System.err.println("Error: No se encontró categoría con ID " + id);
            return false;
        }

        // Validar que el nuevo nombre no exista en otra categoría
        if (categoriaActualizada.getNombre() != null && !categoriaActualizada.getNombre().trim().isEmpty()) {
            String nuevoNombre = categoriaActualizada.getNombre().trim();
            if (!categoriaExistente.getNombre().equalsIgnoreCase(nuevoNombre)) {
                for (Categoria c : categorias) {
                    if (c.getId() != id && c.getNombre().equalsIgnoreCase(nuevoNombre)) {
                        System.err.println("Error: Ya existe otra categoría con el nombre " + nuevoNombre);
                        return false;
                    }
                }
            }
        }

        if (categoriaActualizada.getNombre() != null && !categoriaActualizada.getNombre().trim().isEmpty()) {
            categoriaExistente.setNombre(categoriaActualizada.getNombre().trim());
        }

        if (categoriaActualizada.getDescripcion() != null) {
            categoriaExistente.setDescripcion(categoriaActualizada.getDescripcion());
        }

        System.out.println("✅ Categoría actualizada: " + categoriaExistente.getNombre());
        return true;
    }

    public boolean eliminarCategoria(int id) {
        Categoria categoria = buscarPorId(id);
        if (categoria == null) {
            System.err.println("Error: No se encontró categoría con ID " + id);
            return false;
        }

        // Baja lógica
        categoria.setActivo(false);
        System.out.println("✅ Categoría marcada como inactiva: " + categoria.getNombre());
        return true;
    }

    // Obtener solo categorías activas
    public List<Categoria> obtenerCategoriasActivas() {
        List<Categoria> activas = new ArrayList<>();
        for (Categoria categoria : categorias) {
            if (categoria.isActivo()) {
                activas.add(categoria);
            }
        }
        return activas;
    }
}