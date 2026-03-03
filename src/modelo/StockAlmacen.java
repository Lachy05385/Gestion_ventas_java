package src.modelo;

public class StockAlmacen {
    private int id;
    private Producto producto;
    private Almacen almacen;
    private int cantidad;
    private java.util.Date fechaUltimaActualizacion;

    // Constructor
    public StockAlmacen(Producto producto, Almacen almacen, int cantidad) {
        this.producto = producto;
        this.almacen = almacen;
        this.cantidad = cantidad;
        this.fechaUltimaActualizacion = new java.util.Date();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.fechaUltimaActualizacion = new java.util.Date();
    }

    public java.util.Date getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion;
    }

    public void setFechaUltimaActualizacion(java.util.Date fecha) {
        this.fechaUltimaActualizacion = fecha;
    }

    // Método para aumentar stock
    public void aumentarStock(int cantidad) {
        this.cantidad += cantidad;
        this.fechaUltimaActualizacion = new java.util.Date();
    }

    // Método para disminuir stock (validando que no sea negativo)
    public boolean disminuirStock(int cantidad) {
        if (this.cantidad >= cantidad) {
            this.cantidad -= cantidad;
            this.fechaUltimaActualizacion = new java.util.Date();
            return true;
        }
        return false;
    }

    // Verificar si hay stock suficiente
    public boolean tieneStockSuficiente(int cantidadRequerida) {
        return this.cantidad >= cantidadRequerida;
    }

    // Verificar si está por debajo del stock mínimo del producto
    public boolean estaBajoStockMinimo() {
        return this.cantidad < producto.getStockMinimo();
    }

    @Override
    public String toString() {
        return String.format("Stock [Producto: %s, Almacen: %s, Cantidad: %d, Minimo: %d, Bajo: %s]",
                producto.getNombre(), almacen.getNombre(), cantidad,
                producto.getStockMinimo(), estaBajoStockMinimo() ? "SI" : "NO");
    }
}