import java.sql.*;
import java.util.*;

class Victim {

    public void insertVictim(Connection con, Scanner sc) {
        try {
            System.out.print("Victim_ID: ");
            int victim_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Name: ");
            String name = sc.nextLine();

            System.out.print("Age: ");
            int age = sc.nextInt();
            sc.nextLine();

            System.out.print("Contact: ");
            String contact = sc.nextLine();

            System.out.print("Disaster_ID: ");
            int disaster_id = sc.nextInt();

            System.out.print("Shelter_ID: ");
            int shelter_id = sc.nextInt();

            System.out.print("Medical Needs: ");
            String needs = sc.nextLine();

            String query1 = "INSERT INTO Victim VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst1 = con.prepareStatement(query1);
            pst1.setInt(1, victim_id);
            pst1.setString(2, name);
            pst1.setInt(3, age);
            pst1.setString(4, contact);
            pst1.setInt(5, disaster_id);
            pst1.setInt(6, shelter_id);
            pst1.setString(7, needs);
            pst1.executeUpdate();
            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayVictim(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Victim");

            System.out.println("\nVictim_ID | Name | Age | Contact | Disaster_ID | Shelter_ID | Medical_Needs");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + rs.getString(2) + " | " +
                        rs.getInt(3) + " | " + rs.getString(4) + " | " +
                        rs.getInt(5) + " | " + rs.getInt(6) + rs.getString(7));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAge(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Victim_ID to update age: ");
            int IDUpdate = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter new Age: ");
            int newAge = sc.nextInt();

            String query3 = "UPDATE Victim SET age = ? WHERE victim_id = ?";
            PreparedStatement pst3 = con.prepareStatement(query3);
            pst3.setInt(1, newAge);
            pst3.setInt(2, IDUpdate);
            int rows = pst3.executeUpdate();

            if (rows > 0)
                System.out.println("Age updated successfully!");
            else
                System.out.println("Victim record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateContact(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Victim_ID to update contact: ");
            int IDUpdate = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter new Contact: ");
            String newContact = sc.nextLine();

            String query4 = "UPDATE Victim SET contact = ? WHERE victim_id = ?";
            PreparedStatement pst4 = con.prepareStatement(query4);
            pst4.setString(1, newContact);
            pst4.setInt(2, IDUpdate);
            int rows = pst4.executeUpdate();

            if (rows > 0)
                System.out.println("Contact updated successfully!");
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

            String query5 = "DELETE FROM Victim WHERE victim_id = ?";
            PreparedStatement pst5 = con.prepareStatement(query5);
            pst5.setInt(1, IDDelete);
            int deleted = pst5.executeUpdate();

            if (deleted > 0)
                System.out.println("Record deleted successfully!");
            else
                System.out.println("Record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
