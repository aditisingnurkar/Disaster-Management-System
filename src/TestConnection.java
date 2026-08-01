import java.sql.*;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection con = DatabaseConnection.getConnection();
            System.out.println("Connected Successfully!");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}