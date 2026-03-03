package src.modelo;

public class Almacen {
    private int id;
    private String codigo;
    private String nombre;
    private String ubicacion;
    private String responsable;
    private boolean esCentral; // true si es el almacén central
    private boolean activo;
    private String telefono;

    // Constructor por defecto
    public Almacen() {
        this.activo = true;
    }

    // Constructor con parámetros
    public Almacen(int id, String codigo, String nombre, String ubicacion, String responsable, boolean esCentral,
            String telefono) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.responsable = responsable;
        this.esCentral = esCentral;
        this.activo = true;
        this.telefono = telefono;
    }

    // Getters y setters
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public boolean isEsCentral() {
        return esCentral;
    }

    public void setEsCentral(boolean esCentral) {
        this.esCentral = esCentral;
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
                "Almacen [id=%d, codigo=%s, nombre=%s, ubicacion=%s, responsable=%s, esCentral=%s, activo=%s]",
                id, codigo, nombre, ubicacion, responsable, esCentral ? "Sí" : "No", activo ? "Sí" : "No");
    }
}