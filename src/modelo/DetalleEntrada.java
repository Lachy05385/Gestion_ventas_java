package src.modelo;

import src.modelo.Producto;
import src.modelo.EntradaProducto;

import java.util.Date;

public class DetalleEntrada {
    private int id;
    private EntradaProducto entrada;
    private Producto producto;
    private int cantidad;
    private double precioCompraUnitario;
    private String lote; // Opcional
    private Date fechaVencimiento; // Para productos perecederos

    public DetalleEntrada() {
    }

    public DetalleEntrada(Producto producto, int cantidad, double precioCompraUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioCompraUnitario = precioCompraUnitario;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EntradaProducto getEntrada() {
        return entrada;
    }

    public void setEntrada(EntradaProducto entrada) {
        this.entrada = entrada;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioCompraUnitario() {
        return precioCompraUnitario;
    }

    public void setPrecioCompraUnitario(double precioCompraUnitario) {
        this.precioCompraUnitario = precioCompraUnitario;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public double getSubtotal() {
        return cantidad * precioCompraUnitario;
    }
}
