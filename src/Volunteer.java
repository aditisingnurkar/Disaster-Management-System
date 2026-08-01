import java.sql.*;
import java.util.*;

class Volunteer {

    public void insertVolunteer(Connection con, Scanner sc) {
        try {
            System.out.print("Volunteer_ID: ");
            int volunteer_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Name: ");
            String name = sc.nextLine();

            System.out.print("Availability: ");
            String availability = sc.nextLine();

            String query1 = "INSERT INTO Volunteer VALUES (?, ?, ?)";
            PreparedStatement pst1 = con.prepareStatement(query1);
            pst1.setInt(1, volunteer_id);
            pst1.setString(2, name);
            pst1.setString(3, availability);
            pst1.executeUpdate();
            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayVolunteer(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Volunteer");

            System.out.println("\nVolunteer_ID | Name | Availability");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + rs.getString(2) + " | " + rs.getString(3));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAvailability(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Volunteer_ID to update: ");
            int IDUpdate = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter new Availability: ");
            String availability = sc.nextLine();

            String query3 = "UPDATE Volunteer SET availability = ? WHERE volunteer_id = ?";
            PreparedStatement pst3 = con.prepareStatement(query3);
            pst3.setString(1, availability);
            pst3.setInt(2, IDUpdate);
            int rows = pst3.executeUpdate();

            if (rows > 0)
                System.out.println("Availability updated successfully!");
            else
                System.out.println("Volunteer not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Volunteer_ID to delete: ");
            int IDDelete = sc.nextInt();

            String query4 = "DELETE FROM Volunteer WHERE volunteer_id = ?";
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

