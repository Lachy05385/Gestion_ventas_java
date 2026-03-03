
package src.modelo;

import java.util.Date;
import java.util.List;

public class Venta {
    private int id;
    private String numeroVenta;
    private Date fechaVenta;
    private Usuario vendedor;
    private Almacen almacen;
    private Cliente cliente;
    private List<DetalleVenta> detalles;
    private double subtotal;
    private double descuento;
    private double total;
    private double efectivo;
    private double transferencia;
    private String estado; // PENDIENTE, COMPLETADA, ANULADA
    private String observaciones;

    public Venta() {
        this.fechaVenta = new Date();
        this.estado = "PENDIENTE";
        this.efectivo = 0;
        this.transferencia = 0;
    }

    // Método para calcular totales
    public void calcularTotales() {
        this.subtotal = 0;
        if (detalles != null) {
            for (DetalleVenta d : detalles) {
                this.subtotal += d.getSubtotal();
            }
        }
        this.total = subtotal - descuento;
    }

    // Verificar si el pago está completo
    public boolean pagoCompleto() {
        double totalPagado = efectivo + transferencia;
        return totalPagado >= total && (totalPagado - total) < 0.01; // Margen de 1 centavo
    }

    // Método para procesar pago
    public String procesarPago() {
        if (pagoCompleto()) {
            this.estado = "COMPLETADA";
            double cambio = (efectivo + transferencia) - total;
            if (cambio > 0 && efectivo > 0) {
                return String.format("✅ Venta completada. Cambio: $%.2f", cambio);
            }
            return "✅ Venta completada exitosamente.";
        }
        double restante = total - (efectivo + transferencia);
        return String.format("⚠️ Pago incompleto. Restan: $%.2f", restante);
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroVenta() {
        return numeroVenta;
    }

    public void setNumeroVenta(String numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(double efectivo) {
        this.efectivo = efectivo;
    }

    public double getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(double transferencia) {
        this.transferencia = transferencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}