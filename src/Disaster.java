import java.sql.*;
import java.util.*;

class Disaster {

    public void insertDisaster(Connection con, Scanner sc) {
        try {
            System.out.print("Disaster_ID: ");
            int disaster_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Disaster_Type: ");
            String disaster_type = sc.nextLine();

            System.out.print("Location: ");
            String location = sc.nextLine();

            System.out.print("Start_Date(YYYY-MM-DD): ");
            String start_date = sc.nextLine();

            System.out.print("End_Date(YYYY-MM-DD): ");
            String end_date = sc.nextLine();

            System.out.print("Severity_Level: ");
            String severity_level = sc.nextLine();

            try {
                java.sql.Date.valueOf(start_date);
                java.sql.Date.valueOf(end_date);
            } catch (IllegalArgumentException ex) {
                System.out.println("Invalid date format! Use YYYY-MM-DD.");
                return;
            }

            String query1 = "INSERT INTO Disaster VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst1 = con.prepareStatement(query1);
            pst1.setInt(1, disaster_id);
            pst1.setString(2, disaster_type);
            pst1.setString(3, location);
            pst1.setDate(4, java.sql.Date.valueOf(start_date));
            pst1.setDate(5, java.sql.Date.valueOf(end_date));
            pst1.setString(6, severity_level);
            pst1.executeUpdate();
            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayDisaster(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Disaster");

            System.out.println("\nDisaster_ID | Disaster_Type | Location | Start_Date | End_Date | Severity_Level");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + rs.getString(2) + " | " +
                        rs.getString(3) + " | " + rs.getDate(4) + " | " +
                        rs.getDate(5) + " | " + rs.getString(6));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateEndDate(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Disaster_ID to update: ");
            int IDUpdate = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter end_date: ");
            String endDate = sc.nextLine();

            String query3 = "UPDATE Disaster SET end_date = ? WHERE disaster_id = ?";
            PreparedStatement pst3 = con.prepareStatement(query3);
            pst3.setDate(1, java.sql.Date.valueOf(endDate));
            pst3.setInt(2, IDUpdate);
            int rows = pst3.executeUpdate();

            if (rows > 0)
                System.out.println("End Date updated successfully!");
            else
                System.out.println("Disaster record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Disaster ID to delete: ");
            int IDDelete = sc.nextInt();

            String query4 = "DELETE FROM Disaster WHERE disaster_id = ?";
            PreparedStatement pst4 = con.prepareStatement(query4);
            pst4.setInt(1, IDDelete);
            int deleted = pst4.executeUpdate();

            if (deleted > 0)
                System.out.println("Record deleted successfully!");
            else
                System.out.println("Record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


