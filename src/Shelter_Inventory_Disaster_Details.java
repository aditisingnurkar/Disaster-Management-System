import java.sql.*;
import java.util.*;

class Shelter_Inventory_Disaster_Details {

    public void insertShelter_Inventory_Disaster_Details(Connection con, Scanner sc) {
        try {
            System.out.print("Shelter_ID: ");
            int shelter_id = sc.nextInt();

            System.out.print("Disaster_ID: ");
            int disaster_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Disaster_Type: ");
            String disaster_type = sc.nextLine();

            String query = "INSERT INTO Shelter_Inventory_Disaster_Details VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, shelter_id);
            pst.setInt(2, disaster_id);
            pst.setString(3, disaster_type);
            pst.executeUpdate();

            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayShelter_Inventory_Disaster_Details(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Shelter_Inventory_Disaster_Details");

            System.out.println("\nShelter_ID | Disaster_ID | Disaster_Type");
            while (rs.next()) {
                System.out.println(
                        rs.getInt(1) + " | " +
                                rs.getInt(2) + " | " +
                                rs.getString(3));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Update Methods ----
    public void updateDisasterType(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Shelter_ID: ");
            int shelter_id = sc.nextInt();
            System.out.print("Enter Disaster_ID: ");
            int disaster_id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter new Disaster_Type: ");
            String disaster_type = sc.nextLine();

            String query = "UPDATE Shelter_Inventory_Disaster_Details SET Disaster_Type = ? WHERE Shelter_ID = ? AND Disaster_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, disaster_type);
            pst.setInt(2, shelter_id);
            pst.setInt(3, disaster_id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Disaster_Type updated successfully!");
            else
                System.out.println("Record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDisasterID(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Shelter_ID: ");
            int shelter_id = sc.nextInt();
            System.out.print("Enter current Disaster_ID: ");
            int old_disaster_id = sc.nextInt();
            System.out.print("Enter new Disaster_ID: ");
            int new_disaster_id = sc.nextInt();

            String query = "UPDATE Shelter_Inventory_Disaster_Details SET Disaster_ID = ? WHERE Shelter_ID = ? AND Disaster_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, new_disaster_id);
            pst.setInt(2, shelter_id);
            pst.setInt(3, old_disaster_id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Disaster_ID updated successfully!");
            else
                System.out.println("Record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateShelterID(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Disaster_ID: ");
            int disaster_id = sc.nextInt();
            System.out.print("Enter current Shelter_ID: ");
            int old_shelter_id = sc.nextInt();
            System.out.print("Enter new Shelter_ID: ");
            int new_shelter_id = sc.nextInt();

            String query = "UPDATE Shelter_Inventory_Disaster_Details SET Shelter_ID = ? WHERE Shelter_ID = ? AND Disaster_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, new_shelter_id);
            pst.setInt(2, old_shelter_id);
            pst.setInt(3, disaster_id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Shelter_ID updated successfully!");
            else
                System.out.println("Record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Delete ----
    public void delete(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Shelter_ID to delete: ");
            int shelter_id = sc.nextInt();
            System.out.print("Enter Disaster_ID to delete: ");
            int disaster_id = sc.nextInt();

            String query = "DELETE FROM Shelter_Inventory_Disaster_Details WHERE Shelter_ID = ? AND Disaster_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, shelter_id);
            pst.setInt(2, disaster_id);
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
