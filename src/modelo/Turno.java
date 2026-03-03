package src.modelo;

import java.util.Date;

public class Turno {
    private int id;
    private Usuario usuario;
    private Almacen almacen;
    private Date fechaInicio;
    private Date fechaFin;
    private double efectivoInicial;
    private double efectivoFinal;
    private double ventasEfectivo;
    private double ventasTransferencia;
    private double totalVentas;
    private String estado; // ACTIVO, CERRADO

    public Turno() {
        this.fechaInicio = new Date();
        this.estado = "ACTIVO";
    }

    public Turno(Usuario usuario, Almacen almacen, double efectivoInicial) {
        this.usuario = usuario;
        this.almacen = almacen;
        this.efectivoInicial = efectivoInicial;
        this.fechaInicio = new Date();
        this.estado = "ACTIVO";
        this.ventasEfectivo = 0;
        this.ventasTransferencia = 0;
        this.totalVentas = 0;
    }

    // Método para agregar venta al turno
    public void agregarVenta(double efectivo, double transferencia) {
        this.ventasEfectivo += efectivo;
        this.ventasTransferencia += transferencia;
        this.totalVentas += (efectivo + transferencia);
    }

    // Método para cerrar turno
    public String cerrarTurno(double efectivoFinal) {
        this.efectivoFinal = efectivoFinal;
        this.fechaFin = new Date();
        this.estado = "CERRADO";

        double esperado = efectivoInicial + ventasEfectivo;
        double diferencia = efectivoFinal - esperado;

        return String.format(
                "📊 CIERRE DE TURNO\n" +
                        "   Inicio: %s\n" +
                        "   Fin: %s\n" +
                        "   Efectivo inicial: $%.2f\n" +
                        "   Ventas efectivo: $%.2f\n" +
                        "   Ventas transfer: $%.2f\n" +
                        "   Total ventas: $%.2f\n" +
                        "   Efectivo esperado: $%.2f\n" +
                        "   Efectivo final: $%.2f\n" +
                        "   Diferencia: $%.2f (%s)",
                fechaInicio.toString().substring(0, 19),
                fechaFin.toString().substring(0, 19),
                efectivoInicial,
                ventasEfectivo,
                ventasTransferencia,
                totalVentas,
                esperado,
                efectivoFinal,
                diferencia,
                Math.abs(diferencia) < 0.01 ? "OK" : (diferencia > 0 ? "SOBRA" : "FALTA"));
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getEfectivoInicial() {
        return efectivoInicial;
    }

    public void setEfectivoInicial(double efectivoInicial) {
        this.efectivoInicial = efectivoInicial;
    }

    public double getEfectivoFinal() {
        return efectivoFinal;
    }

    public void setEfectivoFinal(double efectivoFinal) {
        this.efectivoFinal = efectivoFinal;
    }

    public double getVentasEfectivo() {
        return ventasEfectivo;
    }

    public void setVentasEfectivo(double ventasEfectivo) {
        this.ventasEfectivo = ventasEfectivo;
    }

    public double getVentasTransferencia() {
        return ventasTransferencia;
    }

    public void setVentasTransferencia(double ventasTransferencia) {
        this.ventasTransferencia = ventasTransferencia;
    }

    public double getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(double totalVentas) {
        this.totalVentas = totalVentas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}