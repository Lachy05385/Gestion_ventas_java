
package src.Storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/sistema_inventarios";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123"; // Cambia por tu contraseña

    private static Connection connection = null;

    private DatabaseConnection() {
    } // Constructor privado

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Cargar el driver (opcional desde Java 6+ pero recomendado)
                Class.forName("org.postgresql.Driver");

                // Establecer conexión
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Conexión a PostgreSQL establecida.");

            } catch (ClassNotFoundException e) {
                System.err.println("❌ Driver PostgreSQL no encontrado: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("❌ Error al conectar a PostgreSQL: " + e.getMessage());
                System.err.println("   URL: " + URL);
                System.err.println("   Usuario: " + USER);
                System.err.println("   Verifica que PostgreSQL esté corriendo");
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("✅ Conexión a PostgreSQL cerrada.");
            } catch (SQLException e) {
                System.err.println("❌ Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    // Método para probar la conexión
    public static void testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                System.out.println("✅ Conexión exitosa a PostgreSQL");
                System.out.println("   Base de datos: " + conn.getCatalog());
                System.out.println("   Driver: " + conn.getMetaData().getDriverName());
                closeConnection();
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en test de conexión: " + e.getMessage());
        }
    }
}