package src.modelo;

import src.seguridad.PermisosManager;

public class Usuario {
    private int id;
    private String nombre;
    private String username;
    private String password;
    private int rol; // 1=Admin, 2=Dependiente, 3=Contador, etc.
    private boolean activo;
    private Almacen almacen;

    // Constructores
    public Usuario() {
        this.activo = true;

    }

    public Usuario(String nombre, String username, String password, int rol) {
        this.nombre = nombre;
        this.username = username;
        this.password = password;
        this.rol = rol;

    }

    // SOLO getters y setters
    // Método para verificar credenciales
    public boolean verificarCredenciales(String username, String password) {

        // Verifica que no sean nulos
        if (username == null || password == null)
            return false;

        // Compara username y password (case-sensitive para username)
        boolean usuarioCoincide = this.username.equals(username.trim());
        boolean passwordCoincide = this.password.equals(password.trim());

        return usuarioCoincide && passwordCoincide && this.activo;
    }

    // Método para obtener nombre del rol
    public String getNombreRol() {
        switch (this.rol) {
            case 1:
                return "Administrador";
            case 2:
                return "Dependiente";
            case 3:
                return "Contador";
            case 4:
                return "Inventarista";
            default:
                return "Sin rol";
        }
    }

    // Getters y Setters
    public Almacen getAlmacen() {
        return this.almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s (%s) | Rol: %s | Estado: %s",
                id, nombre, username, getNombreRol(), activo ? "Activo" : "Inactivo");
    }
}