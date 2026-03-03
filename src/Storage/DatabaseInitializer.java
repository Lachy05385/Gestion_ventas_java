
package src.Storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void ejecutarScript() {
        String scriptPath = "sql/crear_bd.sql";

        try (Connection conn = DatabaseConnection.getConnection();
                BufferedReader reader = new BufferedReader(new FileReader(scriptPath));
                Statement stmt = conn.createStatement()) {

            StringBuilder script = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                script.append(linea).append("\n");
            }

            // Ejecutar el script completo
            stmt.execute(script.toString());
            System.out.println("✅ Base de datos creada/verificada correctamente");

        } catch (Exception e) {
            System.err.println("❌ Error al ejecutar script: " + e.getMessage());
        }
    }
}