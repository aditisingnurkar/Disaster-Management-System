import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("Testing MySQL Database Connection...");
        System.out.println("URL: " + DatabaseConnection.getUrl());
        System.out.println("User: " + DatabaseConnection.getUser());

        try (Connection con = DatabaseConnection.getConnection()) {
            DatabaseMetaData meta = con.getMetaData();
            System.out.println("✓ Connected Successfully to " + meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion());

            System.out.println("\nTables in database '" + DatabaseConnection.getDbName() + "':");
            try (ResultSet rs = meta.getTables(DatabaseConnection.getDbName(), null, "%", new String[]{"TABLE"})) {
                int count = 0;
                while (rs.next()) {
                    System.out.println("  • " + rs.getString("TABLE_NAME"));
                    count++;
                }
                System.out.println("Total Tables Found: " + count);
            }
        } catch (Exception e) {
            System.err.println("✗ Connection Failed: " + e.getMessage());
            System.err.println("Note: Update your password in DatabaseConnection.java or db.properties if necessary.");
        }
    }
}