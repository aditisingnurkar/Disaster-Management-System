import java.sql.*;
import java.util.*;

class Volunteer_Skills {

    public void insertVolunteerSkills(Connection con, Scanner sc) {
        try {
            System.out.print("Volunteer_ID: ");
            int volunteer_id = sc.nextInt();
            sc.nextLine();

            System.out.print("Skill: ");
            String skill = sc.nextLine();

            String query1 = "INSERT INTO Volunteer_Skills VALUES (?, ?)";
            PreparedStatement pst1 = con.prepareStatement(query1);
            pst1.setInt(1, volunteer_id);
            pst1.setString(2, skill);
            pst1.executeUpdate();
            System.out.println("Record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayVolunteerSkills(Connection con) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Volunteer_Skills");

            System.out.println("\nVolunteer_ID | Skill");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + rs.getString(2));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSkill(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Volunteer_ID to update: ");
            int IDUpdate = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter new Skill: ");
            String newSkill = sc.nextLine();

            String query3 = "UPDATE Volunteer_Skills SET skill = ? WHERE volunteer_id = ?";
            PreparedStatement pst3 = con.prepareStatement(query3);
            pst3.setString(1, newSkill);
            pst3.setInt(2, IDUpdate);
            int rows = pst3.executeUpdate();

            if (rows > 0)
                System.out.println("Skill updated successfully!");
            else
                System.out.println("Volunteer record not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // No delete method for this table as per requirement
}

