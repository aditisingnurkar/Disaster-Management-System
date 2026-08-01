import java.sql.*;
import java.util.*;

class Incident_Report {

    public void insertIncident_Report(Connection con, Scanner sc) {
        try {
            System.out.print("Report_ID: ");
            int report_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Description: ");
            String description = sc.nextLine();

            System.out.print("Timestamp (YYYY-MM-DD HH:MM:SS): ");
            String timestamp = sc.nextLine();

            System.out.print("Volunteer_ID: ");
            int volunteer_id = sc.nextInt();

            System.out.print("Disaster_ID: ");
            int disaster_id = sc.nextInt();

            String query = "INSERT INTO Incident_Report VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, report_id);
            pst.setString(2, description);
            pst.setTimestamp(3, java.sql.Timestamp.valueOf(timestamp));
            pst.setInt(4, volunteer_id);
            pst.setInt(5, disaster_id);
            pst.executeUpdate();

            System.out.println("Record inserted successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid timestamp format! Use YYYY-MM-DD HH:MM:SS.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayIncident_Report(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Incident_Report");

            System.out.println("\nReport_ID | Description | Timestamp | Volunteer_ID | Disaster_ID");
            while (rs.next()) {
                System.out.println(
                        rs.getInt(1) + " | " +
                                rs.getString(2) + " | " +
                                rs.getTimestamp(3) + " | " +
                                rs.getInt(4) + " | " +
                                rs.getInt(5));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Individual update methods ----
    public void updateDescription(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Report_ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter new Description: ");
            String desc = sc.nextLine();

            String query = "UPDATE Incident_Report SET Description = ? WHERE Report_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, desc);
            pst.setInt(2, id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Description updated successfully!");
            else
                System.out.println("Incident report not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTimestamp(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Report_ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter new Timestamp (YYYY-MM-DD HH:MM:SS): ");
            String ts = sc.nextLine();

            String query = "UPDATE Incident_Report SET Timestamp = ? WHERE Report_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setTimestamp(1, java.sql.Timestamp.valueOf(ts));
            pst.setInt(2, id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Timestamp updated successfully!");
            else
                System.out.println("Incident report not found!");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid timestamp format! Use YYYY-MM-DD HH:MM:SS.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateVolunteerID(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Report_ID: ");
            int id = sc.nextInt();
            System.out.print("Enter new Volunteer_ID: ");
            int volunteer_id = sc.nextInt();

            String query = "UPDATE Incident_Report SET Volunteer_ID = ? WHERE Report_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, volunteer_id);
            pst.setInt(2, id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Volunteer_ID updated successfully!");
            else
                System.out.println("Incident report not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDisasterID(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Report_ID: ");
            int id = sc.nextInt();
            System.out.print("Enter new Disaster_ID: ");
            int disaster_id = sc.nextInt();

            String query = "UPDATE Incident_Report SET Disaster_ID = ? WHERE Report_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, disaster_id);
            pst.setInt(2, id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Disaster_ID updated successfully!");
            else
                System.out.println("Incident report not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Delete ----
    public void delete(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Report_ID to delete: ");
            int id = sc.nextInt();

            String query = "DELETE FROM Incident_Report WHERE Report_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id);
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


