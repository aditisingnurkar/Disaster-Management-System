import java.sql.*;
import java.util.*;

class Shelter_Inventory_Shelter_Details {

    public void insertShelter_Inventory_Shelter_Details(Connection con, Scanner sc) {
        try {
            System.out.print("Shelter_ID: ");
            int shelter_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Shelter_Address: ");
            String shelter_address = sc.nextLine();

            System.out.print("Coord_Name: ");
            String coord_name = sc.nextLine();

            System.out.print("Disaster_ID: ");
            int disaster_id = sc.nextInt();

            String query = "INSERT INTO Shelter_Inventory_Shelter_Details VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, shelter_id);
            pst.setString(2, shelter_address);
            pst.setString(3, coord_name);
            pst.setInt(4, disaster_id);
            pst.executeUpdate();

            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayShelter_Inventory_Shelter_Details(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Shelter_Inventory_Shelter_Details");

            System.out.println("\nShelter_ID | Shelter_Address | Coord_Name | Disaster_ID");
            while (rs.next()) {
                System.out.println(
                        rs.getInt(1) + " | " +
                                rs.getString(2) + " | " +
                                rs.getString(3) + " | " +
                                rs.getInt(4));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Individual update methods ----
    public void updateShelterAddress(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Shelter_ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter new Shelter_Address: ");
            String address = sc.nextLine();

            String query = "UPDATE Shelter_Inventory_Shelter_Details SET Shelter_Address = ? WHERE Shelter_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, address);
            pst.setInt(2, id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Shelter_Address updated successfully!");
            else
                System.out.println("Record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCoordName(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Shelter_ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter new Coord_Name: ");
            String coord = sc.nextLine();

            String query = "UPDATE Shelter_Inventory_Shelter_Details SET Coord_Name = ? WHERE Shelter_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, coord);
            pst.setInt(2, id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Coord_Name updated successfully!");
            else
                System.out.println("Record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDisasterID(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Shelter_ID: ");
            int id = sc.nextInt();
            System.out.print("Enter new Disaster_ID: ");
            int disaster_id = sc.nextInt();

            String query = "UPDATE Shelter_Inventory_Shelter_Details SET Disaster_ID = ? WHERE Shelter_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, disaster_id);
            pst.setInt(2, id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Disaster_ID updated successfully!");
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
            int id = sc.nextInt();

            String query = "DELETE FROM Shelter_Inventory_Shelter_Details WHERE Shelter_ID = ?";
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
