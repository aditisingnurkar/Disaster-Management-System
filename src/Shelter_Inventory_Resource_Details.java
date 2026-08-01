import java.sql.*;
import java.util.*;

class Shelter_Inventory_Resource_Details {

    public void insertShelter_Inventory_Resource_Details(Connection con, Scanner sc) {
        try {
            System.out.print("Resource_ID: ");
            int resource_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Resource_Name: ");
            String resource_name = sc.nextLine();

            String query = "INSERT INTO Shelter_Inventory_Resource_Details VALUES (?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, resource_id);
            pst.setString(2, resource_name);
            pst.executeUpdate();

            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayShelter_Inventory_Resource_Details(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Shelter_Inventory_Resource_Details");

            System.out.println("\nResource_ID | Resource_Name");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + rs.getString(2));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Update Methods ----
    public void updateResourceName(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Resource_ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter new Resource_Name: ");
            String name = sc.nextLine();

            String query = "UPDATE Shelter_Inventory_Resource_Details SET Resource_Name = ? WHERE Resource_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, name);
            pst.setInt(2, id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Resource_Name updated successfully!");
            else
                System.out.println("Record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Delete ----
    public void delete(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Resource_ID to delete: ");
            int id = sc.nextInt();

            String query = "DELETE FROM Shelter_Inventory_Resource_Details WHERE Resource_ID = ?";
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


