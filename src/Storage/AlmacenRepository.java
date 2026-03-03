package src.Storage;

import src.modelo.Almacen;
import java.util.ArrayList;
import java.util.List;

public class AlmacenRepository {
    private List<Almacen> almacenes;
    private static AlmacenRepository instancia;
    private int nextId;

    private AlmacenRepository() {
        this.almacenes = new ArrayList<>();
        this.nextId = 1;
        inicializarAlmacenesDemo();
    }

    public static AlmacenRepository getInstance() {
        if (instancia == null) {
            instancia = new AlmacenRepository();
        }
        return instancia;
    }

    private void inicializarAlmacenesDemo() {
        // Solo inicializar si está vacío
        if (!almacenes.isEmpty()) {
            return;
        }

        // Almacén central
        Almacen central = new Almacen();
        central.setId(nextId++);
        central.setCodigo("ALM-001");
        central.setNombre("Almacén Central");
        central.setUbicacion("Sede Principal, Calle Falsa 123");
        central.setResponsable("Administrador");
        central.setEsCentral(true);
        central.setActivo(true);
        almacenes.add(central);

        // Almacén Norte
        Almacen norte = new Almacen();
        norte.setId(nextId++);
        norte.setCodigo("ALM-002");
        norte.setNombre("Almacén Norte");
        norte.setUbicacion("Zona Norte, Av. Siempre Viva 456");
        norte.setResponsable("Juan Pérez");
        norte.setEsCentral(false);
        norte.setActivo(true);
        almacenes.add(norte);

        // Almacén Sur
        Almacen sur = new Almacen();
        sur.setId(nextId++);
        sur.setCodigo("ALM-003");
        sur.setNombre("Almacén Sur");
        sur.setUbicacion("Zona Sur, Blvd. Los Olivos 789");
        sur.setResponsable("María García");
        sur.setEsCentral(false);
        sur.setActivo(true);
        almacenes.add(sur);

        System.out.println("✅ " + almacenes.size() + " almacenes de demostración inicializados.");
    }

    // CRUD
    public boolean agregarAlmacen(Almacen almacen) {
        if (almacen == null) {
            System.err.println("Error: Almacén nulo.");
            return false;
        }

        // Validar campos
        if (almacen.getCodigo() == null || almacen.getCodigo().trim().isEmpty()) {
            System.err.println("Error: El código del almacén es obligatorio.");
            return false;
        }

        if (almacen.getNombre() == null || almacen.getNombre().trim().isEmpty()) {
            System.err.println("Error: El nombre del almacén es obligatorio.");
            return false;
        }

        // Verificar que el código no exista
        for (Almacen a : almacenes) {
            if (a.getCodigo().equalsIgnoreCase(almacen.getCodigo().trim())) {
                System.err.println("Error: Ya existe un almacén con el código " + almacen.getCodigo());
                return false;
            }
        }

        // Asignar ID si no tiene
        if (almacen.getId() <= 0) {
            almacen.setId(nextId++);
        }

        // Asegurar que esté activo
        if (!almacen.isActivo()) {
            almacen.setActivo(true);
        }

        try {
            almacenes.add(almacen);
            System.out.println("✅ Almacén agregado: " + almacen.getNombre() + " (ID: " + almacen.getId() + ")");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error al agregar almacén: " + e.getMessage());
            return false;
        }
    }

    public List<Almacen> obtenerTodosAlmacenes() {
        return new ArrayList<>(almacenes); // Copia para evitar modificación externa
    }

    public Almacen buscarPorId(int id) {
        for (Almacen almacen : almacenes) {
            if (almacen.getId() == id) {
                return almacen;
            }
        }
        return null;
    }

    public Almacen buscarPorCodigo(String codigo) {
        for (Almacen almacen : almacenes) {
            if (almacen.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return almacen;
            }
        }
        return null;
    }

    public boolean actualizarAlmacen(int id, Almacen almacenActualizado) {
        Almacen almacenExistente = buscarPorId(id);
        if (almacenExistente == null) {
            System.err.println("Error: No se encontró almacén con ID " + id);
            return false;
        }

        // Validar que el nuevo código no exista en otro almacén
        if (almacenActualizado.getCodigo() != null && !almacenActualizado.getCodigo().trim().isEmpty()) {
            String nuevoCodigo = almacenActualizado.getCodigo().trim();
            if (!almacenExistente.getCodigo().equalsIgnoreCase(nuevoCodigo)) {
                for (Almacen a : almacenes) {
                    if (a.getId() != id && a.getCodigo().equalsIgnoreCase(nuevoCodigo)) {
                        System.err.println("Error: Ya existe otro almacén con el código " + nuevoCodigo);
                        return false;
                    }
                }
            }
        }

        // Actualizar campos si no son nulos o vacíos
        if (almacenActualizado.getCodigo() != null && !almacenActualizado.getCodigo().trim().isEmpty()) {
            almacenExistente.setCodigo(almacenActualizado.getCodigo().trim());
        }

        if (almacenActualizado.getNombre() != null && !almacenActualizado.getNombre().trim().isEmpty()) {
            almacenExistente.setNombre(almacenActualizado.getNombre().trim());
        }

        if (almacenActualizado.getUbicacion() != null) {
            almacenExistente.setUbicacion(almacenActualizado.getUbicacion());
        }

        if (almacenActualizado.getResponsable() != null) {
            almacenExistente.setResponsable(almacenActualizado.getResponsable());
        }

        // El atributo esCentral no se puede cambiar arbitrariamente (solo un almacén
        // central)
        // Se podría implementar lógica adicional si se desea permitir el cambio

        System.out.println("✅ Almacén actualizado: " + almacenExistente.getNombre());
        return true;
    }

    public boolean eliminarAlmacen(int id) {
        Almacen almacen = buscarPorId(id);
        if (almacen == null) {
            System.err.println("Error: No se encontró almacén con ID " + id);
            return false;
        }

        // No permitir eliminar el almacén central
        if (almacen.isEsCentral()) {
            System.err.println("Error: No se puede eliminar el almacén central.");
            return false;
        }

        try {
            // En lugar de eliminar, marcar como inactivo (baja lógica)
            almacen.setActivo(false);
            System.out.println("✅ Almacén marcado como inactivo: " + almacen.getNombre());
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error al eliminar almacén: " + e.getMessage());
            return false;
        }
    }

    // Método para obtener el almacén central
    public Almacen obtenerAlmacenCentral() {
        for (Almacen almacen : almacenes) {
            if (almacen.isEsCentral()) {
                return almacen;
            }
        }
        return null;
    }

    public List<Almacen> obtenerTodos() {

        return almacenes;
    }

    // Método para obtener almacenes activos (excluyendo inactivos)
    public List<Almacen> obtenerAlmacenesActivos() {
        List<Almacen> activos = new ArrayList<>();
        for (Almacen almacen : almacenes) {
            if (almacen.isActivo()) {
                activos.add(almacen);
            }
        }
        return activos;
    }
}