import java.sql.*;
import java.util.*;

class Volunteer_Assignment_Names {

    public void insertVolunteerAssignmentNames(Connection con, Scanner sc) {
        try {
            System.out.print("Volunteer_ID: ");
            int volunteer_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Name: ");
            String name = sc.nextLine();

            String query1 = "INSERT INTO Volunteer_Assignment_Names VALUES (?, ?)";
            PreparedStatement pst1 = con.prepareStatement(query1);
            pst1.setInt(1, volunteer_id);
            pst1.setString(2, name);
            pst1.executeUpdate();
            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayVolunteerAssignmentNames(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Volunteer_Assignment_Names");

            System.out.println("\nVolunteer_ID | Name");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + rs.getString(2));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // No update method as per requirement

    public void delete(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Volunteer_ID to delete: ");
            int IDDelete = sc.nextInt();

            String query4 = "DELETE FROM Volunteer_Assignment_Names WHERE volunteer_id = ?";
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

