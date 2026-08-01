import java.sql.*;

public class DatabaseConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/disaster_management_system?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "YOUR_PASSWORD";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found!", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
