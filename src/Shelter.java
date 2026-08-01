import java.sql.*;
import java.util.*;

class Shelter {

    public void insertShelter(Connection con, Scanner sc) {
        try {
            System.out.print("Shelter_ID: ");
            int shelter_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Address: ");
            String address = sc.nextLine();

            System.out.print("Capacity: ");
            int capacity = sc.nextInt();
            sc.nextLine();

            System.out.print("Coordinator_Name: ");
            String coordinator_name = sc.nextLine();

            System.out.print("Disaster_ID: ");
            int disaster_id = sc.nextInt();

            String query1 = "INSERT INTO Shelter VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst1 = con.prepareStatement(query1);
            pst1.setInt(1, shelter_id);
            pst1.setString(2, address);
            pst1.setInt(3, capacity);
            pst1.setString(4, coordinator_name);
            pst1.setInt(5, disaster_id);
            pst1.executeUpdate();
            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayShelter(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Shelter");

            System.out.println("\nShelter_ID | Address | Capacity | Coordinator_Name | Disaster_ID");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + rs.getString(2) + " | " +
                        rs.getInt(3) + " | " + rs.getString(4) + " | " + rs.getInt(5));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateShelter(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Shelter_ID to update: ");
            int IDUpdate = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter new Address: ");
            String address = sc.nextLine();

            System.out.print("Enter new Capacity: ");
            int capacity = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter new Coordinator_Name: ");
            String coordinator_name = sc.nextLine();

            String query3 = "UPDATE Shelter SET address = ?, capacity = ?, coordinator_name = ? WHERE shelter_id = ?";
            PreparedStatement pst3 = con.prepareStatement(query3);
            pst3.setString(1, address);
            pst3.setInt(2, capacity);
            pst3.setString(3, coordinator_name);
            pst3.setInt(4, IDUpdate);
            int rows = pst3.executeUpdate();

            if (rows > 0)
                System.out.println("Shelter record updated successfully!");
            else
                System.out.println("Shelter not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Shelter_ID to delete: ");
            int IDDelete = sc.nextInt();

            String query4 = "DELETE FROM Shelter WHERE shelter_id = ?";
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

