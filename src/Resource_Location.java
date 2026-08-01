import java.sql.*;
import java.util.*;

class Resource_Location {

    public void insertResource_Location(Connection con, Scanner sc) {
        try {
            System.out.print("Resource_ID: ");
            int resource_id = sc.nextInt();

            System.out.print("Disaster_ID: ");
            int disaster_id = sc.nextInt();

            System.out.print("Shelter_ID: ");
            int shelter_id = sc.nextInt();

            System.out.print("Quantity: ");
            int quantity = sc.nextInt();

            String query = "INSERT INTO Resource_Location VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, resource_id);
            pst.setInt(2, disaster_id);
            pst.setInt(3, shelter_id);
            pst.setInt(4, quantity);
            pst.executeUpdate();

            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayResource_Location(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Resource_Location");

            System.out.println("\nResource_ID | Disaster_ID | Shelter_ID | Quantity");
            while (rs.next()) {
                System.out.println(
                        rs.getInt(1) + " | " +
                                rs.getInt(2) + " | " +
                                rs.getInt(3) + " | " +
                                rs.getInt(4));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Individual update methods ----
    public void updateQuantity(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Resource_ID: ");
            int resource_id = sc.nextInt();
            System.out.print("Enter Disaster_ID: ");
            int disaster_id = sc.nextInt();
            System.out.print("Enter Shelter_ID: ");
            int shelter_id = sc.nextInt();
            System.out.print("Enter new Quantity: ");
            int quantity = sc.nextInt();

            String query = "UPDATE Resource_Location SET Quantity = ? WHERE Resource_ID = ? AND Disaster_ID = ? AND Shelter_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, quantity);
            pst.setInt(2, resource_id);
            pst.setInt(3, disaster_id);
            pst.setInt(4, shelter_id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Quantity updated successfully!");
            else
                System.out.println("Resource_Location record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDisasterID(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Resource_ID: ");
            int resource_id = sc.nextInt();
            System.out.print("Enter current Disaster_ID: ");
            int old_disaster_id = sc.nextInt();
            System.out.print("Enter Shelter_ID: ");
            int shelter_id = sc.nextInt();
            System.out.print("Enter new Disaster_ID: ");
            int new_disaster_id = sc.nextInt();

            String query = "UPDATE Resource_Location SET Disaster_ID = ? WHERE Resource_ID = ? AND Disaster_ID = ? AND Shelter_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, new_disaster_id);
            pst.setInt(2, resource_id);
            pst.setInt(3, old_disaster_id);
            pst.setInt(4, shelter_id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Disaster_ID updated successfully!");
            else
                System.out.println("Resource_Location record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateShelterID(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Resource_ID: ");
            int resource_id = sc.nextInt();
            System.out.print("Enter Disaster_ID: ");
            int disaster_id = sc.nextInt();
            System.out.print("Enter current Shelter_ID: ");
            int old_shelter_id = sc.nextInt();
            System.out.print("Enter new Shelter_ID: ");
            int new_shelter_id = sc.nextInt();

            String query = "UPDATE Resource_Location SET Shelter_ID = ? WHERE Resource_ID = ? AND Disaster_ID = ? AND Shelter_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, new_shelter_id);
            pst.setInt(2, resource_id);
            pst.setInt(3, disaster_id);
            pst.setInt(4, old_shelter_id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Shelter_ID updated successfully!");
            else
                System.out.println("Resource_Location record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Delete ----
    public void delete(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Resource_ID to delete: ");
            int resource_id = sc.nextInt();
            System.out.print("Enter Disaster_ID to delete: ");
            int disaster_id = sc.nextInt();
            System.out.print("Enter Shelter_ID to delete: ");
            int shelter_id = sc.nextInt();

            String query = "DELETE FROM Resource_Location WHERE Resource_ID = ? AND Disaster_ID = ? AND Shelter_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, resource_id);
            pst.setInt(2, disaster_id);
            pst.setInt(3, shelter_id);
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

