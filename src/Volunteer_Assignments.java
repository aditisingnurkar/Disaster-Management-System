import java.sql.*;
import java.util.*;

class Volunteer_Assignments {

    public void insertVolunteerAssignments(Connection con, Scanner sc) {
        try {
            System.out.print("Volunteer_ID: ");
            int volunteer_id = sc.nextInt();

            System.out.print("Disaster_ID: ");
            int disaster_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Role: ");
            String role = sc.nextLine();

            System.out.print("Shift_Timing: ");
            String shift_timing = sc.nextLine();

            String query1 = "INSERT INTO Volunteer_Assignments VALUES (?, ?, ?, ?)";
            PreparedStatement pst1 = con.prepareStatement(query1);
            pst1.setInt(1, volunteer_id);
            pst1.setInt(2, disaster_id);
            pst1.setString(3, role);
            pst1.setString(4, shift_timing);
            pst1.executeUpdate();
            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayVolunteerAssignments(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Volunteer_Assignments");

            System.out.println("\nVolunteer_ID | Disaster_ID | Role | Shift_Timing");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + rs.getInt(2) + " | " +
                        rs.getString(3) + " | " + rs.getString(4));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAssignment(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Volunteer_ID: ");
            int volunteer_id = sc.nextInt();

            System.out.print("Enter Disaster_ID: ");
            int disaster_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter new Role: ");
            String role = sc.nextLine();

            System.out.print("Enter new Shift_Timing: ");
            String shift_timing = sc.nextLine();

            String query3 = "UPDATE Volunteer_Assignments SET role = ?, shift_timing = ? WHERE volunteer_id = ? AND disaster_id = ?";
            PreparedStatement pst3 = con.prepareStatement(query3);
            pst3.setString(1, role);
            pst3.setString(2, shift_timing);
            pst3.setInt(3, volunteer_id);
            pst3.setInt(4, disaster_id);
            int rows = pst3.executeUpdate();

            if (rows > 0)
                System.out.println("Assignment updated successfully!");
            else
                System.out.println("Record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Volunteer_ID: ");
            int volunteer_id = sc.nextInt();

            System.out.print("Enter Disaster_ID: ");
            int disaster_id = sc.nextInt();

            String query4 = "DELETE FROM Volunteer_Assignments WHERE volunteer_id = ? AND disaster_id = ?";
            PreparedStatement pst4 = con.prepareStatement(query4);
            pst4.setInt(1, volunteer_id);
            pst4.setInt(2, disaster_id);
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

