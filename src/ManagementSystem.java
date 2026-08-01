import java.sql.*;
import java.util.*;

public class ManagementSystem {

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/disaster_management_system";
        String user = "root";
        String password = "juju1234juju@2006"; // Change this to your MySQL password
        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Connection con = null;

        try {
            con = getConnection();
            System.out.println("✓ Connected to Database Successfully!\n");

            // Create instances of all classes
            Disaster disaster = new Disaster();
            Victim victim = new Victim();
            Victim_Shelter_Details vsd = new Victim_Shelter_Details();
            Victim_Disaster_Details vdd = new Victim_Disaster_Details();
            Shelter shelter = new Shelter();
            Volunteer volunteer = new Volunteer();
            Volunteer_Skills vs = new Volunteer_Skills();
            Volunteer_Assignments va = new Volunteer_Assignments();
            Volunteer_Assignment_Names van = new Volunteer_Assignment_Names();
            Volunteer_Assignment_Disaster_Types vadt = new Volunteer_Assignment_Disaster_Types();
            Resource resource = new Resource();
            Resource_Location rl = new Resource_Location();
            Incident_Report ir = new Incident_Report();
            Incident_Volunteer_Details ivd = new Incident_Volunteer_Details();
            Incident_Disaster_Details idd = new Incident_Disaster_Details();
            Shelter_Inventory si = new Shelter_Inventory();
            Shelter_Inventory_Shelter_Details sisd = new Shelter_Inventory_Shelter_Details();
            Shelter_Inventory_Resource_Details sird = new Shelter_Inventory_Resource_Details();
            Shelter_Inventory_Disaster_Details sidd = new Shelter_Inventory_Disaster_Details();

            boolean exit = false;

            while (!exit) {
                System.out.println("\n╔════════════════════════════════════════════════╗");
                System.out.println("║    DISASTER MANAGEMENT SYSTEM - MAIN MENU      ║");
                System.out.println("╚════════════════════════════════════════════════╝");
                System.out.println("1.  Disaster Management");
                System.out.println("2.  Victim Management");
                System.out.println("3.  Shelter Management");
                System.out.println("4.  Volunteer Management");
                System.out.println("5.  Resource Management");
                System.out.println("6.  Incident Report Management");
                System.out.println("7.  Inventory Management");
                System.out.println("0.  Exit");
                System.out.println("════════════════════════════════════════════════");
                System.out.print("Enter your choice: ");

                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        disasterMenu(con, sc, disaster);
                        break;
                    case 2:
                        victimMenu(con, sc, victim, vsd, vdd);
                        break;
                    case 3:
                        shelterMenu(con, sc, shelter);
                        break;
                    case 4:
                        volunteerMenu(con, sc, volunteer, vs, va, van, vadt);
                        break;
                    case 5:
                        resourceMenu(con, sc, resource, rl);
                        break;
                    case 6:
                        incidentMenu(con, sc, ir, ivd, idd);
                        break;
                    case 7:
                        inventoryMenu(con, sc, si, sisd, sird, sidd);
                        break;
                    case 0:
                        exit = true;
                        System.out.println("\n✓ Thank you for using Disaster Management System!");
                        break;
                    default:
                        System.out.println("\n✗ Invalid choice! Please try again.");
                }
            }

        } catch (SQLException e) {
            System.out.println("\n✗ Database Connection Error!");
            e.printStackTrace();
        } finally {
            try {
                if (con != null) con.close();
                sc.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ==================== DISASTER MENU ====================
    private static void disasterMenu(Connection con, Scanner sc, Disaster disaster) {
        boolean back = false;
        while (!back) {
            System.out.println("\n┌─ DISASTER MANAGEMENT ─────────────────────┐");
            System.out.println("│ 1. Insert Disaster Record               │");
            System.out.println("│ 2. Display All Disasters                │");
            System.out.println("│ 3. Update End Date                      │");
            System.out.println("│ 4. Delete Disaster                      │");
            System.out.println("│ 0. Back to Main Menu                    │");
            System.out.println("└───────────────────────────────────────────┘");
            System.out.print("Choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1: disaster.insertDisaster(con, sc); break;
                case 2: disaster.displayDisaster(con); break;
                case 3: disaster.updateEndDate(con, sc); break;
                case 4: disaster.delete(con, sc); break;
                case 0: back = true; break;
                default: System.out.println("✗ Invalid choice!");
            }
        }
    }

    // ==================== VICTIM MENU ====================
    private static void victimMenu(Connection con, Scanner sc, Victim victim,
                                   Victim_Shelter_Details vsd, Victim_Disaster_Details vdd) {
        boolean back = false;
        while (!back) {
            System.out.println("\n┌─ VICTIM MANAGEMENT ───────────────────────┐");
            System.out.println("│ 1. Insert Victim Record                 │");
            System.out.println("│ 2. Display All Victims                  │");
            System.out.println("│ 3. Update Victim Age                    │");
            System.out.println("│ 4. Update Victim Contact                │");
            System.out.println("│ 5. Delete Victim                        │");
            System.out.println("│ 6. Victim-Shelter Details               │");
            System.out.println("│ 7. Victim-Disaster Details              │");
            System.out.println("│ 0. Back to Main Menu                    │");
            System.out.println("└───────────────────────────────────────────┘");
            System.out.print("Choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1: victim.insertVictim(con, sc); break;
                case 2: victim.displayVictim(con); break;
                case 3: victim.updateAge(con, sc); break;
                case 4: victim.updateContact(con, sc); break;
                case 5: victim.delete(con, sc); break;
                case 6: victimShelterDetailsMenu(con, sc, vsd); break;
                case 7: victimDisasterDetailsMenu(con, sc, vdd); break;
                case 0: back = true; break;
                default: System.out.println("✗ Invalid choice!");
            }
        }
    }

    private static void victimShelterDetailsMenu(Connection con, Scanner sc, Victim_Shelter_Details vsd) {
        System.out.println("\n1. Insert  2. Display  3. Update  4. Delete  0. Back");
        System.out.print("Choice: ");
        int c = sc.nextInt();
        switch (c) {
            case 1: vsd.insertVictimShelterDetails(con, sc); break;
            case 2: vsd.displayVictimShelterDetails(con); break;
            case 3: vsd.updateShelterAddress(con, sc); break;
            case 4: vsd.delete(con, sc); break;
        }
    }

    private static void victimDisasterDetailsMenu(Connection con, Scanner sc, Victim_Disaster_Details vdd) {
        System.out.println("\n1. Insert  2. Display  3. Update  4. Delete  0. Back");
        System.out.print("Choice: ");
        int c = sc.nextInt();
        switch (c) {
            case 1: vdd.insertVictimDisasterDetails(con, sc); break;
            case 2: vdd.displayVictimDisasterDetails(con); break;
            case 3: vdd.updateDisasterType(con, sc); break;
            case 4: vdd.delete(con, sc); break;
        }
    }

    // ==================== SHELTER MENU ====================
    private static void shelterMenu(Connection con, Scanner sc, Shelter shelter) {
        boolean back = false;
        while (!back) {
            System.out.println("\n┌─ SHELTER MANAGEMENT ──────────────────────┐");
            System.out.println("│ 1. Insert Shelter Record                │");
            System.out.println("│ 2. Display All Shelters                 │");
            System.out.println("│ 3. Update Shelter Details               │");
            System.out.println("│ 4. Delete Shelter                       │");
            System.out.println("│ 0. Back to Main Menu                    │");
            System.out.println("└───────────────────────────────────────────┘");
            System.out.print("Choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1: shelter.insertShelter(con, sc); break;
                case 2: shelter.displayShelter(con); break;
                case 3: shelter.updateShelter(con, sc); break;
                case 4: shelter.delete(con, sc); break;
                case 0: back = true; break;
                default: System.out.println("✗ Invalid choice!");
            }
        }
    }

    // ==================== VOLUNTEER MENU ====================
    private static void volunteerMenu(Connection con, Scanner sc, Volunteer volunteer,
                                      Volunteer_Skills vs, Volunteer_Assignments va,
                                      Volunteer_Assignment_Names van,
                                      Volunteer_Assignment_Disaster_Types vadt) {
        boolean back = false;
        while (!back) {
            System.out.println("\n┌─ VOLUNTEER MANAGEMENT ────────────────────┐");
            System.out.println("│ 1. Insert Volunteer Record              │");
            System.out.println("│ 2. Display All Volunteers               │");
            System.out.println("│ 3. Update Volunteer Availability        │");
            System.out.println("│ 4. Delete Volunteer                     │");
            System.out.println("│ 5. Volunteer Skills Management          │");
            System.out.println("│ 6. Volunteer Assignments Management     │");
            System.out.println("│ 7. Assignment Names Management          │");
            System.out.println("│ 8. Assignment Disaster Types            │");
            System.out.println("│ 0. Back to Main Menu                    │");
            System.out.println("└───────────────────────────────────────────┘");
            System.out.print("Choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1: volunteer.insertVolunteer(con, sc); break;
                case 2: volunteer.displayVolunteer(con); break;
                case 3: volunteer.updateAvailability(con, sc); break;
                case 4: volunteer.delete(con, sc); break;
                case 5: volunteerSkillsMenu(con, sc, vs); break;
                case 6: volunteerAssignmentsMenu(con, sc, va); break;
                case 7: volunteerAssignmentNamesMenu(con, sc, van); break;
                case 8: volunteerAssignmentDisasterTypesMenu(con, sc, vadt); break;
                case 0: back = true; break;
                default: System.out.println("✗ Invalid choice!");
            }
        }
    }

    private static void volunteerSkillsMenu(Connection con, Scanner sc, Volunteer_Skills vs) {
        System.out.println("\n1. Insert  2. Display  3. Update  0. Back");
        System.out.print("Choice: ");
        int c = sc.nextInt();
        switch (c) {
            case 1: vs.insertVolunteerSkills(con, sc); break;
            case 2: vs.displayVolunteerSkills(con); break;
            case 3: vs.updateSkill(con, sc); break;
        }
    }

    private static void volunteerAssignmentsMenu(Connection con, Scanner sc, Volunteer_Assignments va) {
        System.out.println("\n1. Insert  2. Display  3. Update  4. Delete  0. Back");
        System.out.print("Choice: ");
        int c = sc.nextInt();
        switch (c) {
            case 1: va.insertVolunteerAssignments(con, sc); break;
            case 2: va.displayVolunteerAssignments(con); break;
            case 3: va.updateAssignment(con, sc); break;
            case 4: va.delete(con, sc); break;
        }
    }

    private static void volunteerAssignmentNamesMenu(Connection con, Scanner sc, Volunteer_Assignment_Names van) {
        System.out.println("\n1. Insert  2. Display  3. Delete  0. Back");
        System.out.print("Choice: ");
        int c = sc.nextInt();
        switch (c) {
            case 1: van.insertVolunteerAssignmentNames(con, sc); break;
            case 2: van.displayVolunteerAssignmentNames(con); break;
            case 3: van.delete(con, sc); break;
        }
    }

    private static void volunteerAssignmentDisasterTypesMenu(Connection con, Scanner sc,
                                                             Volunteer_Assignment_Disaster_Types vadt) {
        System.out.println("\n1. Insert  2. Display  3. Delete  0. Back");
        System.out.print("Choice: ");
        int c = sc.nextInt();
        switch (c) {
            case 1: vadt.insertVolunteerAssignmentDisasterTypes(con, sc); break;
            case 2: vadt.displayVolunteerAssignmentDisasterTypes(con); break;
            case 3: vadt.delete(con, sc); break;
        }
    }

    // ==================== RESOURCE MENU ====================
    private static void resourceMenu(Connection con, Scanner sc, Resource resource, Resource_Location rl) {
        boolean back = false;
        while (!back) {
            System.out.println("\n┌─ RESOURCE MANAGEMENT ─────────────────────┐");
            System.out.println("│ 1. Insert Resource Record               │");
            System.out.println("│ 2. Display All Resources                │");
            System.out.println("│ 3. Update Resource Name                 │");
            System.out.println("│ 4. Update Resource Description          │");
            System.out.println("│ 5. Delete Resource                      │");
            System.out.println("│ 6. Resource Location Management         │");
            System.out.println("│ 0. Back to Main Menu                    │");
            System.out.println("└───────────────────────────────────────────┘");
            System.out.print("Choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1: resource.insertResource(con, sc); break;
                case 2: resource.displayResource(con); break;
                case 3: resource.updateResourceName(con, sc); break;
                case 4: resource.updateDescription(con, sc); break;
                case 5: resource.delete(con, sc); break;
                case 6: resourceLocationMenu(con, sc, rl); break;
                case 0: back = true; break;
                default: System.out.println("✗ Invalid choice!");
            }
        }
    }

    private static void resourceLocationMenu(Connection con, Scanner sc, Resource_Location rl) {
        System.out.println("\n┌─ Resource Location ───────────────────────┐");
        System.out.println("│ 1. Insert  2. Display  3. Update Quantity │");
        System.out.println("│ 4. Update Disaster ID  5. Update Shelter  │");
        System.out.println("│ 6. Delete  0. Back                        │");
        System.out.println("└───────────────────────────────────────────┘");
        System.out.print("Choice: ");
        int c = sc.nextInt();
        switch (c) {
            case 1: rl.insertResource_Location(con, sc); break;
            case 2: rl.displayResource_Location(con); break;
            case 3: rl.updateQuantity(con, sc); break;
            case 4: rl.updateDisasterID(con, sc); break;
            case 5: rl.updateShelterID(con, sc); break;
            case 6: rl.delete(con, sc); break;
        }
    }

    // ==================== INCIDENT MENU ====================
    private static void incidentMenu(Connection con, Scanner sc, Incident_Report ir,
                                     Incident_Volunteer_Details ivd, Incident_Disaster_Details idd) {
        boolean back = false;
        while (!back) {
            System.out.println("\n┌─ INCIDENT REPORT MANAGEMENT ──────────────┐");
            System.out.println("│ 1. Insert Incident Report               │");
            System.out.println("│ 2. Display All Reports                  │");
            System.out.println("│ 3. Update Description                   │");
            System.out.println("│ 4. Update Timestamp                     │");
            System.out.println("│ 5. Update Volunteer ID                  │");
            System.out.println("│ 6. Update Disaster ID                   │");
            System.out.println("│ 7. Delete Report                        │");
            System.out.println("│ 8. Incident Volunteer Details           │");
            System.out.println("│ 9. Incident Disaster Details            │");
            System.out.println("│ 0. Back to Main Menu                    │");
            System.out.println("└───────────────────────────────────────────┘");
            System.out.print("Choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1: ir.insertIncident_Report(con, sc); break;
                case 2: ir.displayIncident_Report(con); break;
                case 3: ir.updateDescription(con, sc); break;
                case 4: ir.updateTimestamp(con, sc); break;
                case 5: ir.updateVolunteerID(con, sc); break;
                case 6: ir.updateDisasterID(con, sc); break;
                case 7: ir.delete(con, sc); break;
                case 8: incidentVolunteerDetailsMenu(con, sc, ivd); break;
                case 9: incidentDisasterDetailsMenu(con, sc, idd); break;
                case 0: back = true; break;
                default: System.out.println("✗ Invalid choice!");
            }
        }
    }

    private static void incidentVolunteerDetailsMenu(Connection con, Scanner sc, Incident_Volunteer_Details ivd) {
        System.out.println("\n1. Insert  2. Display  3. Update  4. Delete  0. Back");
        System.out.print("Choice: ");
        int c = sc.nextInt();
        switch (c) {
            case 1: ivd.insertIncident_Volunteer_Details(con, sc); break;
            case 2: ivd.displayIncident_Volunteer_Details(con); break;
            case 3: ivd.updateVolName(con, sc); break;
            case 4: ivd.delete(con, sc); break;
        }
    }

    private static void incidentDisasterDetailsMenu(Connection con, Scanner sc, Incident_Disaster_Details idd) {
        System.out.println("\n1. Insert  2. Display  3. Update  4. Delete  0. Back");
        System.out.print("Choice: ");
        int c = sc.nextInt();
        switch (c) {
            case 1: idd.insertIncident_Disaster_Details(con, sc); break;
            case 2: idd.displayIncident_Disaster_Details(con); break;
            case 3: idd.updateDisasterType(con, sc); break;
            case 4: idd.delete(con, sc); break;
        }
    }

    // ==================== INVENTORY MENU ====================
    private static void inventoryMenu(Connection con, Scanner sc, Shelter_Inventory si,
                                      Shelter_Inventory_Shelter_Details sisd,
                                      Shelter_Inventory_Resource_Details sird,
                                      Shelter_Inventory_Disaster_Details sidd) {
        boolean back = false;
        while (!back) {
            System.out.println("\n┌─ SHELTER INVENTORY MANAGEMENT ────────────┐");
            System.out.println("│ 1. Insert Inventory Record              │");
            System.out.println("│ 2. Display All Inventory                │");
            System.out.println("│ 3. Update Quantity                      │");
            System.out.println("│ 4. Update Resource ID                   │");
            System.out.println("│ 5. Update Shelter ID                    │");
            System.out.println("│ 6. Delete Inventory Record              │");
            System.out.println("│ 7. Shelter Details Management           │");
            System.out.println("│ 8. Resource Details Management          │");
            System.out.println("│ 9. Disaster Details Management          │");
            System.out.println("│ 0. Back to Main Menu                    │");
            System.out.println("└───────────────────────────────────────────┘");
            System.out.print("Choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1: si.insertShelter_Inventory(con, sc); break;
                case 2: si.displayShelter_Inventory(con); break;
                case 3: si.updateQuantity(con, sc); break;
                case 4: si.updateResourceID(con, sc); break;
                case 5: si.updateShelterID(con, sc); break;
                case 6: si.delete(con, sc); break;
                case 7: shelterInventoryShelterDetailsMenu(con, sc, sisd); break;
                case 8: shelterInventoryResourceDetailsMenu(con, sc, sird); break;
                case 9: shelterInventoryDisasterDetailsMenu(con, sc, sidd); break;
                case 0: back = true; break;
                default: System.out.println("✗ Invalid choice!");
            }
        }
    }

    private static void shelterInventoryShelterDetailsMenu(Connection con, Scanner sc,
                                                           Shelter_Inventory_Shelter_Details sisd) {
        System.out.println("\n1. Insert  2. Display  3. Update Address  4. Update Coord");
        System.out.println("5. Update Disaster  6. Delete  0. Back");
        System.out.print("Choice: ");
        int c = sc.nextInt();
        switch (c) {
            case 1: sisd.insertShelter_Inventory_Shelter_Details(con, sc); break;
            case 2: sisd.displayShelter_Inventory_Shelter_Details(con); break;
            case 3: sisd.updateShelterAddress(con, sc); break;
            case 4: sisd.updateCoordName(con, sc); break;
            case 5: sisd.updateDisasterID(con, sc); break;
            case 6: sisd.delete(con, sc); break;
        }
    }

    private static void shelterInventoryResourceDetailsMenu(Connection con, Scanner sc,
                                                            Shelter_Inventory_Resource_Details sird) {
        System.out.println("\n1. Insert  2. Display  3. Update  4. Delete  0. Back");
        System.out.print("Choice: ");
        int c = sc.nextInt();
        switch (c) {
            case 1: sird.insertShelter_Inventory_Resource_Details(con, sc); break;
            case 2: sird.displayShelter_Inventory_Resource_Details(con); break;
            case 3: sird.updateResourceName(con, sc); break;
            case 4: sird.delete(con, sc); break;
        }
    }

    private static void shelterInventoryDisasterDetailsMenu(Connection con, Scanner sc,
                                                            Shelter_Inventory_Disaster_Details sidd) {
        System.out.println("\n1. Insert  2. Display  3. Update Type  4. Update Disaster ID");
        System.out.println("5. Update Shelter ID  6. Delete  0. Back");
        System.out.print("Choice: ");
        int c = sc.nextInt();
        switch (c) {
            case 1: sidd.insertShelter_Inventory_Disaster_Details(con, sc); break;
            case 2: sidd.displayShelter_Inventory_Disaster_Details(con); break;
            case 3: sidd.updateDisasterType(con, sc); break;
            case 4: sidd.updateDisasterID(con, sc); break;
            case 5: sidd.updateShelterID(con, sc); break;
            case 6: sidd.delete(con, sc); break;
        }
    }
}