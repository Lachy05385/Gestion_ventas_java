package src.modelo;

import java.util.Date;
import java.util.List;

public class Transferencia {
    private static int id;
    private String codigo;
    private Almacen almacenOrigen;
    private Almacen almacenDestino;
    private Date fechaSolicitud;
    private Date fechaAprobacion;
    private Usuario solicitante; // Usuario que solicita (del almacén origen)
    private Usuario aprobador; // Usuario que aprueba (del almacén destino)
    private String estado; // PENDIENTE, APROBADA, RECHAZADA, COMPLETADA
    private List<DetalleTransferencia> detalles;
    private String observaciones;

    public Transferencia() {
        id = generarNuevoId();
        this.fechaSolicitud = new Date();
        this.estado = "PENDIENTE";

    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getObservaciones() {
        return observaciones;
    }

    private static int generarNuevoId() {
        return ++id;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Almacen getAlmacenOrigen() {
        return almacenOrigen;
    }

    public void setAlmacenOrigen(Almacen almacenOrigen) {
        this.almacenOrigen = almacenOrigen;
    }

    public Almacen getAlmacenDestino() {
        return almacenDestino;
    }

    public void setAlmacenDestino(Almacen almacenDestino) {
        this.almacenDestino = almacenDestino;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Usuario solicitante) {
        this.solicitante = solicitante;
    }

    public Usuario getAprobador() {
        return aprobador;
    }

    public void setAprobador(Usuario aprobador) {
        this.aprobador = aprobador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<DetalleTransferencia> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleTransferencia> detalles) {
        this.detalles = detalles;
    }

    public double getTotal() {
        double total = 0;
        if (detalles != null) {
            for (DetalleTransferencia detalle : detalles) {
                total += detalle.getCantidad() * detalle.getProducto().getPrecioCosto();
            }
        }
        return total;
    }
}