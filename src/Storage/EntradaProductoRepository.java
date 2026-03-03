
package src.Storage;

import src.modelo.*;
import java.util.ArrayList;
import java.util.List;

public class EntradaProductoRepository {
    private List<EntradaProducto> entradas;
    private static EntradaProductoRepository instancia;
    private int nextId;

    private EntradaProductoRepository() {
        this.entradas = new ArrayList<>();
        this.nextId = 1;
    }

    public static EntradaProductoRepository getInstance() {
        if (instancia == null) {
            instancia = new EntradaProductoRepository();
        }
        return instancia;
    }

    public boolean agregarEntrada(EntradaProducto entrada) {
        if (entrada == null)
            return false;

        if (entrada.getId() <= 0) {
            entrada.setId(nextId++);
        }

        if (entrada.getNumeroEntrada() == null) {
            entrada.setNumeroEntrada(generarNumeroEntrada());
        }

        entradas.add(entrada);
        System.out.println("✅ Entrada registrada: " + entrada.getNumeroEntrada());
        return true;
    }

    public List<EntradaProducto> obtenerTodas() {
        return new ArrayList<>(entradas);
    }

    public EntradaProducto buscarPorId(int id) {
        for (EntradaProducto e : entradas) {
            if (e.getId() == id)
                return e;
        }
        return null;
    }

    public EntradaProducto buscarPorNumero(String numero) {
        for (EntradaProducto e : entradas) {
            if (e.getNumeroEntrada().equalsIgnoreCase(numero))
                return e;
        }
        return null;
    }

    public List<EntradaProducto> buscarPorProveedor(int proveedorId) {
        List<EntradaProducto> resultados = new ArrayList<>();
        for (EntradaProducto e : entradas) {
            if (e.getProveedor() != null && e.getProveedor().getId() == proveedorId) {
                resultados.add(e);
            }
        }
        return resultados;
    }

    public List<EntradaProducto> buscarPorFecha(java.util.Date fechaInicio, java.util.Date fechaFin) {
        List<EntradaProducto> resultados = new ArrayList<>();
        for (EntradaProducto e : entradas) {
            if (e.getFechaEntrada().compareTo(fechaInicio) >= 0 &&
                    e.getFechaEntrada().compareTo(fechaFin) <= 0) {
                resultados.add(e);
            }
        }
        return resultados;
    }

    private String generarNumeroEntrada() {
        return "ENT-" + System.currentTimeMillis() % 10000 + "-" +
                new java.util.Date().toString().substring(0, 3).toUpperCase();
    }

    public int generarNuevoId() {
        return nextId++;
    }
}
