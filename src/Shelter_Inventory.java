import java.sql.*;
import java.util.*;

class Shelter_Inventory {

    public void insertShelter_Inventory(Connection con, Scanner sc) {
        try {
            System.out.print("Shelter_ID: ");
            int shelter_id = sc.nextInt();

            System.out.print("Resource_ID: ");
            int resource_id = sc.nextInt();

            System.out.print("Quantity: ");
            int quantity = sc.nextInt();

            String query = "INSERT INTO Shelter_Inventory VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, shelter_id);
            pst.setInt(2, resource_id);
            pst.setInt(3, quantity);
            pst.executeUpdate();

            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayShelter_Inventory(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Shelter_Inventory");

            System.out.println("\nShelter_ID | Resource_ID | Quantity");
            while (rs.next()) {
                System.out.println(
                        rs.getInt(1) + " | " +
                                rs.getInt(2) + " | " +
                                rs.getInt(3));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Individual update methods ----
    public void updateQuantity(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Shelter_ID: ");
            int shelter_id = sc.nextInt();
            System.out.print("Enter Resource_ID: ");
            int resource_id = sc.nextInt();
            System.out.print("Enter new Quantity: ");
            int quantity = sc.nextInt();

            String query = "UPDATE Shelter_Inventory SET Quantity = ? WHERE Shelter_ID = ? AND Resource_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, quantity);
            pst.setInt(2, shelter_id);
            pst.setInt(3, resource_id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Quantity updated successfully!");
            else
                System.out.println("Shelter_Inventory record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateResourceID(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Shelter_ID: ");
            int shelter_id = sc.nextInt();
            System.out.print("Enter current Resource_ID: ");
            int old_resource_id = sc.nextInt();
            System.out.print("Enter new Resource_ID: ");
            int new_resource_id = sc.nextInt();

            String query = "UPDATE Shelter_Inventory SET Resource_ID = ? WHERE Shelter_ID = ? AND Resource_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, new_resource_id);
            pst.setInt(2, shelter_id);
            pst.setInt(3, old_resource_id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Resource_ID updated successfully!");
            else
                System.out.println("Shelter_Inventory record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateShelterID(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Resource_ID: ");
            int resource_id = sc.nextInt();
            System.out.print("Enter current Shelter_ID: ");
            int old_shelter_id = sc.nextInt();
            System.out.print("Enter new Shelter_ID: ");
            int new_shelter_id = sc.nextInt();

            String query = "UPDATE Shelter_Inventory SET Shelter_ID = ? WHERE Shelter_ID = ? AND Resource_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, new_shelter_id);
            pst.setInt(2, old_shelter_id);
            pst.setInt(3, resource_id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Shelter_ID updated successfully!");
            else
                System.out.println("Shelter_Inventory record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Delete ----
    public void delete(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Shelter_ID to delete: ");
            int shelter_id = sc.nextInt();
            System.out.print("Enter Resource_ID to delete: ");
            int resource_id = sc.nextInt();

            String query = "DELETE FROM Shelter_Inventory WHERE Shelter_ID = ? AND Resource_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, shelter_id);
            pst.setInt(2, resource_id);
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
