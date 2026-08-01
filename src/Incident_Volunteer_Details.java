import java.sql.*;
import java.util.*;

class Incident_Volunteer_Details {

    public void insertIncident_Volunteer_Details(Connection con, Scanner sc) {
        try {
            System.out.print("Report_ID: ");
            int report_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Vol_Name: ");
            String vol_name = sc.nextLine();

            String query = "INSERT INTO Incident_Volunteer_Details VALUES (?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, report_id);
            pst.setString(2, vol_name);
            pst.executeUpdate();

            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayIncident_Volunteer_Details(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Incident_Volunteer_Details");

            System.out.println("\nReport_ID | Vol_Name");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + rs.getString(2));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Individual update methods ----
    public void updateVolName(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Report_ID: ");
            int report_id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter new Vol_Name: ");
            String vol_name = sc.nextLine();

            String query = "UPDATE Incident_Volunteer_Details SET Vol_Name = ? WHERE Report_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, vol_name);
            pst.setInt(2, report_id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Vol_Name updated successfully!");
            else
                System.out.println("Incident_Volunteer_Details record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Delete ----
    public void delete(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Report_ID to delete: ");
            int report_id = sc.nextInt();

            String query = "DELETE FROM Incident_Volunteer_Details WHERE Report_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, report_id);
            int deleted = pst.executeUpdate();

            if (deleted > 0)
                System.out.println("Record deleted successfully!");
            else
                System.out.println("Record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
