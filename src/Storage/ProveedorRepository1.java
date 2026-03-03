
package src.Storage;

import src.modelo.Proveedor;
import java.util.ArrayList;
import java.util.List;

public class ProveedorRepository1 {
    private List<Proveedor> proveedores;
    private static ProveedorRepository1 instancia;
    private int nextId;

    private ProveedorRepository1() {
        this.proveedores = new ArrayList<>();
        this.nextId = 1;
        inicializarProveedoresDemo();
    }

    public static ProveedorRepository1 getInstance() {
        if (instancia == null) {
            instancia = new ProveedorRepository1();
        }
        return instancia;
    }

    private void inicializarProveedoresDemo() {
        if (!proveedores.isEmpty())
            return;

        Proveedor[] proveedoresDemo = {
                new Proveedor("PROV-001", "Carlos Distribuciones", "Distribuidora López", "809-555-1234",
                        "Av. Central 123", "ventas@lopez.com"),
                new Proveedor("PROV-002", "Importadora del Sur", "ImportSur SRL", "809-555-5678", "Calle Principal 456",
                        "info@importsur.com"),
                new Proveedor("PROV-003", "Comercial Rodríguez", "Comercial Rdz", "809-555-9012",
                        "Av. Independencia 789", "pedidos@rodriguez.com"),
                new Proveedor("PROV-004", "Distribuidora Pérez", "Pérez Hermanos", "809-555-3456", "Calle Duarte 321",
                        "ventas@perez.com"),
                new Proveedor("PROV-005", "Proveedores Unidos", "P Unidos SA", "809-555-7890", "Av. 27 de Febrero 654",
                        "info@punidos.com")
        };

        for (Proveedor p : proveedoresDemo) {
            p.setId(nextId++);
            proveedores.add(p);
        }

        System.out.println("✅ " + proveedores.size() + " proveedores de demostración inicializados.");
    }

    // CRUD básico
    public boolean agregarProveedor(Proveedor proveedor) {
        if (proveedor == null)
            return false;

        // Validar código único
        for (Proveedor p : proveedores) {
            if (p.getCodigo().equalsIgnoreCase(proveedor.getCodigo())) {
                System.err.println("❌ Código de proveedor ya existe");
                return false;
            }
        }

        if (proveedor.getId() <= 0) {
            proveedor.setId(nextId++);
        }

        proveedores.add(proveedor);
        System.out.println("✅ Proveedor agregado: " + proveedor.getNombre());
        return true;
    }

    public List<Proveedor> obtenerTodos() {
        return new ArrayList<>(proveedores);
    }

    public Proveedor buscarPorId(int id) {
        for (Proveedor p : proveedores) {
            if (p.getId() == id)
                return p;
        }
        return null;
    }

    public Proveedor buscarPorCodigo(String codigo) {
        for (Proveedor p : proveedores) {
            if (p.getCodigo().equalsIgnoreCase(codigo))
                return p;
        }
        return null;
    }

    public List<Proveedor> buscarPorNombre(String nombre) {
        List<Proveedor> resultados = new ArrayList<>();
        for (Proveedor p : proveedores) {
            if (p.getNombre().toLowerCase().contains(nombre.toLowerCase()) ||
                    p.getEmpresa().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    public boolean actualizarProveedor(int id, Proveedor proveedorActualizado) {
        Proveedor existente = buscarPorId(id);
        if (existente == null)
            return false;

        if (proveedorActualizado.getNombre() != null)
            existente.setNombre(proveedorActualizado.getNombre());
        if (proveedorActualizado.getEmpresa() != null)
            existente.setEmpresa(proveedorActualizado.getEmpresa());
        if (proveedorActualizado.getTelefono() != null)
            existente.setTelefono(proveedorActualizado.getTelefono());
        if (proveedorActualizado.getDireccion() != null)
            existente.setDireccion(proveedorActualizado.getDireccion());
        if (proveedorActualizado.getEmail() != null)
            existente.setEmail(proveedorActualizado.getEmail());

        System.out.println("✅ Proveedor actualizado: " + existente.getNombre());
        return true;
    }

    public boolean eliminarProveedor(int id) {
        Proveedor proveedor = buscarPorId(id);
        if (proveedor == null)
            return false;

        proveedor.setActivo(false);
        System.out.println("✅ Proveedor marcado como inactivo: " + proveedor.getNombre());
        return true;
    }
}
