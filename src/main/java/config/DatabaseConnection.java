import java.sql.*;
import java.io.*;
import java.util.Properties;

public class DatabaseConnection {

    private static String host = "localhost";
    private static int port = 3306;
    private static String dbName = "disaster_management";
    private static String user = "root";
    private static String password = "<YOUR PASSWORD>";

    private static final String CONFIG_FILE = "db.properties";

    static {
        loadConfig();
    }

    public static synchronized void loadConfig() {
        File file = findConfigFile();
        if (file.exists()) {
            try (InputStream in = new FileInputStream(file)) {
                Properties props = new Properties();
                props.load(in);
                host = props.getProperty("db.host", host);
                port = Integer.parseInt(props.getProperty("db.port", String.valueOf(port)));
                dbName = props.getProperty("db.name", dbName);
                user = props.getProperty("db.user", user);
                password = props.getProperty("db.password", password);
            } catch (Exception e) {
                System.err.println("Warning: Could not load " + CONFIG_FILE + ", using default settings: " + e.getMessage());
            }
        }

        host = System.getenv().getOrDefault("DB_HOST", host);
        port = parsePort(System.getenv("DB_PORT"), port);
        dbName = System.getenv().getOrDefault("DB_NAME", dbName);
        user = System.getenv().getOrDefault("DB_USER", user);
        password = System.getenv().getOrDefault("DB_PASSWORD", password);
    }

    private static File findConfigFile() {
        File currentDirectory = new File(CONFIG_FILE);
        if (currentDirectory.exists()) {
            return currentDirectory;
        }
        return new File("..", CONFIG_FILE);
    }

    private static int parsePort(String value, int defaultPort) {
        if (value == null || value.trim().isEmpty()) {
            return defaultPort;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            System.err.println("Warning: Invalid DB_PORT; using " + defaultPort + ".");
            return defaultPort;
        }
    }

    public static synchronized void saveConfig(String newHost, int newPort, String newDb, String newUser, String newPass) {
        host = newHost;
        port = newPort;
        dbName = newDb;
        user = newUser;
        password = newPass;

        Properties props = new Properties();
        props.setProperty("db.host", host);
        props.setProperty("db.port", String.valueOf(port));
        props.setProperty("db.name", dbName);
        props.setProperty("db.user", user);
        props.setProperty("db.password", password);

        try (OutputStream out = new FileOutputStream(CONFIG_FILE)) {
            props.store(out, "Disaster Management Database Configuration");
        } catch (Exception e) {
            System.err.println("Error saving db.properties: " + e.getMessage());
        }
    }

    public static String getUrl() {
        return "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    }

    public static String getBaseServerUrl() {
        return "jdbc:mysql://" + host + ":" + port + "/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    }

    public static String getHost() { return host; }
    public static int getPort() { return port; }
    public static String getDbName() { return dbName; }
    public static String getUser() { return user; }
    public static String getPassword() { return password; }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver (com.mysql.cj.jdbc.Driver) not found in classpath!", e);
        }
        if (password.isEmpty()) {
            throw new SQLException("Database password is not configured. Set DB_PASSWORD or configure db.properties.");
        }
        return DriverManager.getConnection(getUrl(), user, password);
    }

    public static Connection getServerConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found in classpath!", e);
        }
        if (password.isEmpty()) {
            throw new SQLException("Database password is not configured. Set DB_PASSWORD or configure db.properties.");
        }
        return DriverManager.getConnection(getBaseServerUrl(), user, password);
    }

    public static boolean testConnection() {
        try (Connection con = getConnection()) {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
