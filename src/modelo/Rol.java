package src.modelo;

public class Rol {
    private int id;
    private String rol;

    public Rol() {

    }

    public void setRol(int id, String rol) {
        this.id = id;
        this.rol = rol;

    }

    public int getId() {
        return id;
    }

    public String getRol() {
        return rol;
    }

    @Override
    public String toString() {
        return "Rol [id :" + id + "Rol: " + rol + "]";
    }

}
