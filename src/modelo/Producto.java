package src.modelo;

public class Producto {
    private int id;
    private String codigo; // Código único del producto
    private String nombre;
    private Categoria categoria; // Objeto Categoria
    private String unidadMedida; // Unidad, Kg, Litro, Caja, etc.
    private double precioCosto;
    private double precioVenta;
    private int stockMinimo; // Stock mínimo para alertas
    private String imagen; // Ruta o nombre del archivo de imagen
    private boolean activo;

    // Constructor por defecto
    public Producto() {
        this.activo = true;
        this.stockMinimo = 10; // Valor por defecto
    }

    // Constructor con parámetros

    public Producto(int id, String codigo, String nombre, Categoria categoria, String unidadMedida,
            double precioCosto, double precioVenta, int stockMinimo, String imagen) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.unidadMedida = unidadMedida;
        this.precioCosto = precioCosto;
        this.precioVenta = precioVenta;
        this.stockMinimo = stockMinimo;
        this.imagen = imagen;
        this.activo = true;
    }

    // Getters y setters
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public double getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(double precioCosto) {
        this.precioCosto = precioCosto;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return String.format(
                "Producto [id=%d, codigo=%s, nombre=%s, categoria=%s, unidad=%s, costo=%.2f, venta=%.2f, stockMin=%d, activo=%s]",
                id, codigo, nombre, categoria.getNombre(), unidadMedida, precioCosto, precioVenta, stockMinimo,
                activo ? "Sí" : "No");
    }
}