package src.modelo;

import src.modelo.Proveedor;
import src.Storage.ProveedorRepository1;
import src.modelo.DetalleEntrada;
import java.util.Date;
import java.util.List;
//import java.util.ArrayList;

public class EntradaProducto {
    private int id;
    private String numeroEntrada;
    private Date fechaEntrada;
    private Proveedor proveedor;
    private String numeroFactura;
    private Almacen almacenDestino; // Normalmente el almacén central
    private Usuario responsable;
    private String observaciones;
    private List<DetalleEntrada> detalles;

    public EntradaProducto() {
        this.fechaEntrada = new Date();
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroEntrada() {
        return numeroEntrada;
    }

    public void setNumeroEntrada(String numeroEntrada) {
        this.numeroEntrada = numeroEntrada;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Almacen getAlmacenDestino() {
        return almacenDestino;
    }

    public void setAlmacenDestino(Almacen almacenDestino) {
        this.almacenDestino = almacenDestino;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<DetalleEntrada> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleEntrada> detalles) {
        this.detalles = detalles;
    }

    public double getTotalEntrada() {
        double total = 0;
        if (detalles != null) {
            for (DetalleEntrada d : detalles) {
                total += d.getSubtotal();
            }
        }
        return total;
    }
}