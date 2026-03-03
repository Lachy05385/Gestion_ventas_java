
package src.Storage;

import src.modelo.Almacen;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlmacenRepositoryPostgreSQL {
    private Connection connection;

    public AlmacenRepositoryPostgreSQL() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Obtener todos los almacenes
    public List<Almacen> obtenerTodos() {
        List<Almacen> almacenes = new ArrayList<>();
        String sql = "SELECT id, codigo, nombre, ubicacion, responsable, es_central, activo FROM almacenes";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Almacen almacen = mapearAlmacen(rs);
                almacenes.add(almacen);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener almacenes: " + e.getMessage());
        }
        return almacenes;
    }

    // Buscar almacén por ID
    public Almacen buscarPorId(int id) {
        String sql = "SELECT id, codigo, nombre, ubicacion, responsable, es_central, activo FROM almacenes WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapearAlmacen(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar almacén por ID: " + e.getMessage());
        }
        return null;
    }

    // Obtener almacén central
    public Almacen obtenerAlmacenCentral() {
        String sql = "SELECT id, codigo, nombre, ubicacion, responsable, es_central, activo FROM almacenes WHERE es_central = true LIMIT 1";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return mapearAlmacen(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener almacén central: " + e.getMessage());
        }
        return null;
    }

    // Obtener almacenes activos
    public List<Almacen> obtenerAlmacenesActivos() {
        List<Almacen> almacenes = new ArrayList<>();
        String sql = "SELECT id, codigo, nombre, ubicacion, responsable, es_central, activo FROM almacenes WHERE activo = true";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Almacen almacen = mapearAlmacen(rs);
                almacenes.add(almacen);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener almacenes activos: " + e.getMessage());
        }
        return almacenes;
    }

    // Obtener almacenes no centrales
    public List<Almacen> obtenerAlmacenesNoCentrales() {
        List<Almacen> almacenes = new ArrayList<>();
        String sql = "SELECT id, codigo, nombre, ubicacion, responsable, es_central, activo FROM almacenes WHERE es_central = false AND activo = true";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Almacen almacen = mapearAlmacen(rs);
                almacenes.add(almacen);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener almacenes no centrales: " + e.getMessage());
        }
        return almacenes;
    }

    // Insertar nuevo almacén
    public boolean insertar(Almacen almacen) {
        String sql = "INSERT INTO almacenes (codigo, nombre, ubicacion, responsable, es_central, activo) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, almacen.getCodigo());
            pstmt.setString(2, almacen.getNombre());
            pstmt.setString(3, almacen.getUbicacion());
            pstmt.setString(4, almacen.getResponsable());
            pstmt.setBoolean(5, almacen.isEsCentral());
            pstmt.setBoolean(6, almacen.isActivo());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                almacen.setId(rs.getInt("id"));
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar almacén: " + e.getMessage());
        }
        return false;
    }

    // Actualizar almacén
    public boolean actualizar(Almacen almacen) {
        String sql = "UPDATE almacenes SET codigo = ?, nombre = ?, ubicacion = ?, responsable = ?, es_central = ?, activo = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, almacen.getCodigo());
            pstmt.setString(2, almacen.getNombre());
            pstmt.setString(3, almacen.getUbicacion());
            pstmt.setString(4, almacen.getResponsable());
            pstmt.setBoolean(5, almacen.isEsCentral());
            pstmt.setBoolean(6, almacen.isActivo());
            pstmt.setInt(7, almacen.getId());

            int afectadas = pstmt.executeUpdate();
            return afectadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar almacén: " + e.getMessage());
            return false;
        }
    }

    // Eliminar (baja lógica)
    public boolean eliminar(int id) {
        String sql = "UPDATE almacenes SET activo = false WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int afectadas = pstmt.executeUpdate();
            return afectadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar almacén: " + e.getMessage());
            return false;
        }
    }

    // Mapear ResultSet a objeto Almacen
    private Almacen mapearAlmacen(ResultSet rs) throws SQLException {
        Almacen almacen = new Almacen();
        almacen.setId(rs.getInt("id"));
        almacen.setCodigo(rs.getString("codigo"));
        almacen.setNombre(rs.getString("nombre"));
        almacen.setUbicacion(rs.getString("ubicacion"));
        almacen.setResponsable(rs.getString("responsable"));
        almacen.setEsCentral(rs.getBoolean("es_central"));
        almacen.setActivo(rs.getBoolean("activo"));
        return almacen;
    }
}