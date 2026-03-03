
package src.modelo;

public class Categoria {
    private int id;
    private String nombre;
    private String descripcion;
    private boolean activo;

    public Categoria() {
        this.activo = true;
    }

    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = true;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return String.format("Categoria [id=%d, nombre=%s, descripcion=%s, activo=%s]",
                id, nombre, descripcion, activo ? "Sí" : "No");
    }
}