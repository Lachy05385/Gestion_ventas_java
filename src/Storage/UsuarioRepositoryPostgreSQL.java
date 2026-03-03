package src.Storage;

import src.modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositoryPostgreSQL {
    private Connection connection;

    public UsuarioRepositoryPostgreSQL() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Obtener todos los usuarios
    public List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, nombre, username, password, rol_id, almacen_id, activo FROM usuarios";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                usuario.setRol(rs.getInt("rol_id"));
                usuario.setActivo(rs.getBoolean("activo"));

                // Cargar almacén (opcional)
                int almacenId = rs.getInt("almacen_id");
                if (!rs.wasNull()) {
                    AlmacenRepositoryPostgreSQL almacenRepo = new AlmacenRepositoryPostgreSQL();
                    usuario.setAlmacen(almacenRepo.buscarPorId(almacenId));
                }

                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    // Buscar usuario por username
    public Usuario buscarPorUsername(String username) {
        String sql = "SELECT id, nombre, username, password, rol_id, almacen_id, activo FROM usuarios WHERE username = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                usuario.setRol(rs.getInt("rol_id"));
                usuario.setActivo(rs.getBoolean("activo"));

                int almacenId = rs.getInt("almacen_id");
                if (!rs.wasNull()) {
                    AlmacenRepositoryPostgreSQL almacenRepo = new AlmacenRepositoryPostgreSQL();
                    usuario.setAlmacen(almacenRepo.buscarPorId(almacenId));
                }

                return usuario;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar usuario: " + e.getMessage());
        }

        return null;
    }

    // Insertar usuario
    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, username, password, rol_id, almacen_id, activo) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getUsername());
            pstmt.setString(3, usuario.getPassword());
            pstmt.setInt(4, usuario.getRol());

            if (usuario.getAlmacen() != null) {
                pstmt.setInt(5, usuario.getAlmacen().getId());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }

            pstmt.setBoolean(6, usuario.isActivo());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getInt(1));
                }
                System.out.println("✅ Usuario insertado: " + usuario.getNombre());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar usuario: " + e.getMessage());
        }

        return false;
    }

    // Actualizar usuario
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, username = ?, password = ?, rol_id = ?, almacen_id = ?, activo = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getUsername());
            pstmt.setString(3, usuario.getPassword());
            pstmt.setInt(4, usuario.getRol());

            if (usuario.getAlmacen() != null) {
                pstmt.setInt(5, usuario.getAlmacen().getId());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }

            pstmt.setBoolean(6, usuario.isActivo());
            pstmt.setInt(7, usuario.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    // Eliminar usuario (baja lógica)
    public boolean eliminar(int id) {
        String sql = "UPDATE usuarios SET activo = false WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}