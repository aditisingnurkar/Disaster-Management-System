import java.sql.*;
import java.util.*;

class Volunteer_Assignment_Disaster_Types {

    public void insertVolunteerAssignmentDisasterTypes(Connection con, Scanner sc) {
        try {
            System.out.print("Disaster_ID: ");
            int disaster_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Disaster_Type: ");
            String disaster_type = sc.nextLine();

            String query1 = "INSERT INTO Volunteer_Assignment_Disaster_Types VALUES (?, ?)";
            PreparedStatement pst1 = con.prepareStatement(query1);
            pst1.setInt(1, disaster_id);
            pst1.setString(2, disaster_type);
            pst1.executeUpdate();
            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayVolunteerAssignmentDisasterTypes(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Volunteer_Assignment_Disaster_Types");

            System.out.println("\nDisaster_ID | Disaster_Type");
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
            System.out.print("Enter Disaster_ID to delete: ");
            int IDDelete = sc.nextInt();

            String query4 = "DELETE FROM Volunteer_Assignment_Disaster_Types WHERE disaster_id = ?";
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


