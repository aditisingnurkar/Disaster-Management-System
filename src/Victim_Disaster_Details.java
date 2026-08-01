import java.sql.*;
import java.util.*;

class Victim_Disaster_Details {

    public void insertVictimDisasterDetails(Connection con, Scanner sc) {
        try {
            System.out.print("Victim_ID: ");
            int victim_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Disaster_Type: ");
            String disaster_type = sc.nextLine();

            String query1 = "INSERT INTO Victim_Disaster_Details VALUES (?, ?)";
            PreparedStatement pst1 = con.prepareStatement(query1);
            pst1.setInt(1, victim_id);
            pst1.setString(2, disaster_type);
            pst1.executeUpdate();
            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayVictimDisasterDetails(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Victim_Disaster_Details");

            System.out.println("\nVictim_ID | Disaster_Type");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + rs.getString(2));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDisasterType(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Victim_ID to update: ");
            int IDUpdate = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter new Disaster_Type: ");
            String newType = sc.nextLine();

            String query3 = "UPDATE Victim_Disaster_Details SET disaster_type = ? WHERE victim_id = ?";
            PreparedStatement pst3 = con.prepareStatement(query3);
            pst3.setString(1, newType);
            pst3.setInt(2, IDUpdate);
            int rows = pst3.executeUpdate();

            if (rows > 0)
                System.out.println("Disaster Type updated successfully!");
            else
                System.out.println("Victim record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Victim_ID to delete: ");
            int IDDelete = sc.nextInt();

            String query4 = "DELETE FROM Victim_Disaster_Details WHERE victim_id = ?";
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
