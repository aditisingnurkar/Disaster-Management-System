import java.sql.*;
import java.util.*;

class Resource {

    public void insertResource(Connection con, Scanner sc) {
        try {
            System.out.print("Resource_ID: ");
            int resource_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Resource_Name: ");
            String resource_name = sc.nextLine();

            System.out.print("Description: ");
            String description = sc.nextLine();

            String query = "INSERT INTO Resource VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, resource_id);
            pst.setString(2, resource_name);
            pst.setString(3, description);
            pst.executeUpdate();

            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayResource(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Resource");

            System.out.println("\nResource_ID | Resource_Name | Description");
            while (rs.next()) {
                System.out.println(
                        rs.getInt(1) + " | " +
                                rs.getString(2) + " | " +
                                rs.getString(3));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Individual update methods ----
    public void updateResourceName(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Resource_ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter new Resource_Name: ");
            String name = sc.nextLine();

            String query = "UPDATE Resource SET Resource_Name = ? WHERE Resource_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, name);
            pst.setInt(2, id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Resource_Name updated successfully!");
            else
                System.out.println("Resource record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDescription(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Resource_ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter new Description: ");
            String desc = sc.nextLine();

            String query = "UPDATE Resource SET Description = ? WHERE Resource_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, desc);
            pst.setInt(2, id);
            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Description updated successfully!");
            else
                System.out.println("Resource record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Delete ----
    public void delete(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Resource_ID to delete: ");
            int id = sc.nextInt();

            String query = "DELETE FROM Resource WHERE Resource_ID = ?";
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


