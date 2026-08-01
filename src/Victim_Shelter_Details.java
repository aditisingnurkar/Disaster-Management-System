import java.sql.*;
import java.util.*;

class Victim_Shelter_Details {

    public void insertVictimShelterDetails(Connection con, Scanner sc) {
        try {
            System.out.print("Victim_ID: ");
            int victim_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Shelter_Address: ");
            String shelter_address = sc.nextLine();

            String query1 = "INSERT INTO Victim_Shelter_Details VALUES (?, ?)";
            PreparedStatement pst1 = con.prepareStatement(query1);
            pst1.setInt(1, victim_id);
            pst1.setString(2, shelter_address);
            pst1.executeUpdate();
            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayVictimShelterDetails(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Victim_Shelter_Details");

            System.out.println("\nVictim_ID | Shelter_Address");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + rs.getString(2));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateShelterAddress(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Victim_ID to update: ");
            int IDUpdate = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter new Shelter_Address: ");
            String newAddress = sc.nextLine();

            String query3 = "UPDATE Victim_Shelter_Details SET shelter_address = ? WHERE victim_id = ?";
            PreparedStatement pst3 = con.prepareStatement(query3);
            pst3.setString(1, newAddress);
            pst3.setInt(2, IDUpdate);
            int rows = pst3.executeUpdate();

            if (rows > 0)
                System.out.println("Shelter Address updated successfully!");
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

            String query4 = "DELETE FROM Victim_Shelter_Details WHERE victim_id = ?";
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

