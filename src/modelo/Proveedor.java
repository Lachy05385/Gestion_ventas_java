package src.modelo;

public class Proveedor {
    private int id;
    private String codigo;
    private String nombre;
    private String empresa;
    private String telefono;
    private String direccion;
    private String email;
    private boolean activo;

    public Proveedor() {
        this.activo = true;
    }

    public Proveedor(String codigo, String nombre, String empresa, String telefono, String direccion, String email) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.empresa = empresa;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
        this.activo = true;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return String.format("Proveedor [%s] %s - %s", codigo, nombre, empresa);
    }
}
