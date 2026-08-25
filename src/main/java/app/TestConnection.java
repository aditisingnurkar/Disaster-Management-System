import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestConnection {
    private static final Set<String> REQUIRED_TABLES = new HashSet<>(Arrays.asList(
            "DISASTER", "SHELTER", "VICTIM", "RESOURCE", "SHELTER_INVENTORY",
            "VOLUNTEER", "VOLUNTEER_ASSIGNMENT", "INCIDENT_REPORT"));

    public static void main(String[] args) {
        System.out.println("Testing MySQL Database Connection...");
        System.out.println("URL: " + DatabaseConnection.getUrl());
        System.out.println("User: " + DatabaseConnection.getUser());

        try (Connection con = DatabaseConnection.getConnection()) {
            DatabaseMetaData meta = con.getMetaData();
            System.out.println("✓ Connected Successfully to " + meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion());

            System.out.println("\nTables in database '" + DatabaseConnection.getDbName() + "':");
            Set<String> foundTables = new HashSet<>();
            try (ResultSet rs = meta.getTables(DatabaseConnection.getDbName(), null, "%", new String[]{"TABLE"})) {
                int count = 0;
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME").toUpperCase();
                    System.out.println("  " + tableName);
                    foundTables.add(tableName);
                    count++;
                }
                System.out.println("Total Tables Found: " + count);
            }
            Set<String> missingTables = new HashSet<>(REQUIRED_TABLES);
            missingTables.removeAll(foundTables);
            if (!missingTables.isEmpty()) {
                throw new IllegalStateException("Schema is incomplete; missing tables: " + missingTables);
            }
            System.out.println("Schema check passed: all 8 required tables are present.");
        } catch (Exception e) {
            System.err.println("✗ Connection Failed: " + e.getMessage());
            System.err.println("Set DB_PASSWORD or configure db.properties, and ensure MySQL is running with the schema imported.");
            System.exit(1);
        }
    }
}