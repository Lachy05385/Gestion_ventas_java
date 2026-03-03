
package src.Storage;

import src.modelo.Venta;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentaRepository {
    private List<Venta> ventas;
    private static VentaRepository instancia;
    private int nextId;

    private VentaRepository() {
        this.ventas = new ArrayList<>();
        this.nextId = 1;
    }

    public static VentaRepository getInstance() {
        if (instancia == null) {
            instancia = new VentaRepository();
        }
        return instancia;
    }

    public boolean agregarVenta(Venta venta) {
        if (venta == null)
            return false;
        if (venta.getId() <= 0) {
            venta.setId(nextId++);
        }
        if (venta.getNumeroVenta() == null) {
            venta.setNumeroVenta(generarNumeroVenta());
        }
        ventas.add(venta);
        return true;
    }

    public List<Venta> obtenerTodas() {
        return new ArrayList<>(ventas);
    }

    public Venta buscarPorId(int id) {
        for (Venta v : ventas) {
            if (v.getId() == id)
                return v;
        }
        return null;
    }

    public Venta buscarPorNumero(String numero) {
        for (Venta v : ventas) {
            if (v.getNumeroVenta().equalsIgnoreCase(numero))
                return v;
        }
        return null;
    }

    public List<Venta> buscarPorFecha(Date fechaInicio, Date fechaFin) {
        List<Venta> resultados = new ArrayList<>();
        for (Venta v : ventas) {
            if (v.getFechaVenta().compareTo(fechaInicio) >= 0 &&
                    v.getFechaVenta().compareTo(fechaFin) <= 0) {
                resultados.add(v);
            }
        }
        return resultados;
    }

    public List<Venta> buscarPorVendedor(int vendedorId) {
        List<Venta> resultados = new ArrayList<>();
        for (Venta v : ventas) {
            if (v.getVendedor() != null && v.getVendedor().getId() == vendedorId) {
                resultados.add(v);
            }
        }
        return resultados;
    }

    public List<Venta> buscarPorAlmacen(int almacenId) {
        List<Venta> resultados = new ArrayList<>();
        for (Venta v : ventas) {
            if (v.getAlmacen() != null && v.getAlmacen().getId() == almacenId) {
                resultados.add(v);
            }
        }
        return resultados;
    }

    private String generarNumeroVenta() {
        return "VEN-" + System.currentTimeMillis() % 10000 + "-" +
                new Date().toString().substring(0, 3).toUpperCase();
    }
}
