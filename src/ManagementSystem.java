import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class ManagementSystem {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("   DISASTER MANAGEMENT SYSTEM - CONSOLE COMMAND CLI       ");
        System.out.println("   (3NF Relational Architecture - MySQL 8.0 Integration)  ");
        System.out.println("==========================================================");

        boolean running = true;
        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1.  Disaster Events Management");
            System.out.println("2.  Relief Shelter Management");
            System.out.println("3.  Victim Registration & Tracking");
            System.out.println("4.  Resource Catalog Management");
            System.out.println("5.  Shelter Inventory Allocation (Shelter ── Resource)");
            System.out.println("6.  Volunteer Personnel Registry");
            System.out.println("7.  Volunteer Field Assignments (Volunteer ── Disaster)");
            System.out.println("8.  Incident Reports & Emergency Logs");
            System.out.println("9.  Live Database Dashboard & Analytics Summary");
            System.out.println("10. Launch Graphical User Interface (GUI)");
            System.out.println("0.  Exit");
            System.out.print("Enter choice (0-10): ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": disasterMenu(); break;
                case "2": shelterMenu(); break;
                case "3": victimMenu(); break;
                case "4": resourceMenu(); break;
                case "5": inventoryMenu(); break;
                case "6": volunteerMenu(); break;
                case "7": assignmentMenu(); break;
                case "8": incidentMenu(); break;
                case "9": showAnalytics(); break;
                case "10":
                    System.out.println("Launching Swing GUI Dashboard...");
                    javax.swing.SwingUtilities.invokeLater(() -> new DashboardUI().setVisible(true));
                    break;
                case "0":
                    running = false;
                    System.out.println("Exiting Disaster Management System. Stay safe!");
                    break;
                default:
                    System.out.println("Invalid selection! Please enter a number between 0 and 10.");
            }
        }
    }

    // 1. DISASTER MENU
    private static void disasterMenu() {
        System.out.println("\n=== DISASTER EVENTS MENU ===");
        System.out.println("1. View All Disasters");
        System.out.println("2. Add New Disaster");
        System.out.println("3. Search Disasters");
        System.out.println("4. Delete Disaster");
        System.out.print("Enter choice: ");
        String c = sc.nextLine().trim();

        try {
            switch (c) {
                case "1":
                    List<Disaster> list = DisasterDAO.getAll();
                    System.out.println("\nID | Type | Location | Start Date | End Date | Severity");
                    System.out.println("---------------------------------------------------------------");
                    for (Disaster d : list) {
                        System.out.printf("%-3d| %-12s| %-20s| %-10s| %-10s| %-8s\n",
                                d.getDisasterId(), d.getDisasterType(), d.getLocation(),
                                d.getStartDate(), (d.getEndDate() != null ? d.getEndDate() : "-"), d.getSeverity());
                    }
                    break;
                case "2":
                    System.out.print("Disaster Type (e.g. Flood, Earthquake): ");
                    String type = sc.nextLine().trim();
                    System.out.print("Location: ");
                    String loc = sc.nextLine().trim();
                    System.out.print("Start Date (YYYY-MM-DD): ");
                    Date start = Date.valueOf(sc.nextLine().trim());
                    System.out.print("End Date (YYYY-MM-DD or leave blank): ");
                    String endStr = sc.nextLine().trim();
                    Date end = endStr.isEmpty() ? null : Date.valueOf(endStr);
                    System.out.print("Severity (Low / Medium / High / Critical): ");
                    String sev = sc.nextLine().trim();

                    Disaster d = new Disaster(type, loc, start, end, sev);
                    if (DisasterDAO.insert(d)) {
                        System.out.println("✓ Disaster added successfully with ID: " + d.getDisasterId());
                    }
                    break;
                case "3":
                    System.out.print("Enter search keyword: ");
                    String kw = sc.nextLine().trim();
                    List<Disaster> results = DisasterDAO.search(kw);
                    System.out.println("\nSearch Results (" + results.size() + " matches):");
                    for (Disaster dis : results) {
                        System.out.println("• #" + dis.getDisasterId() + ": " + dis.getDisasterType() + " at " + dis.getLocation() + " [" + dis.getSeverity() + "]");
                    }
                    break;
                case "4":
                    System.out.print("Enter Disaster ID to delete: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    if (DisasterDAO.delete(id)) {
                        System.out.println("✓ Disaster deleted.");
                    } else {
                        System.out.println("✗ Record not found.");
                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // 2. SHELTER MENU
    private static void shelterMenu() {
        System.out.println("\n=== RELIEF SHELTERS MENU ===");
        System.out.println("1. View All Shelters");
        System.out.println("2. Add New Shelter");
        System.out.println("3. Delete Shelter");
        System.out.print("Enter choice: ");
        String c = sc.nextLine().trim();

        try {
            switch (c) {
                case "1":
                    List<Shelter> list = ShelterDAO.getAll();
                    System.out.println("\nID | Shelter Name | Capacity | Coordinator | Contact | Disaster ID");
                    System.out.println("-------------------------------------------------------------------------");
                    for (Shelter s : list) {
                        System.out.printf("%-3d| %-25s| %-8d| %-18s| %-14s| %-5d\n",
                                s.getShelterId(), s.getShelterName(), s.getCapacity(),
                                s.getCoordinatorName(), s.getContactNumber(), s.getDisasterId());
                    }
                    break;
                case "2":
                    System.out.print("Shelter Name: ");
                    String name = sc.nextLine().trim();
                    System.out.print("Address / Location: ");
                    String addr = sc.nextLine().trim();
                    System.out.print("Capacity (bed count): ");
                    int cap = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Coordinator Name: ");
                    String coord = sc.nextLine().trim();
                    System.out.print("Contact Number: ");
                    String phone = sc.nextLine().trim();
                    System.out.print("Associated Disaster ID: ");
                    int disId = Integer.parseInt(sc.nextLine().trim());

                    Shelter s = new Shelter(name, addr, cap, coord, phone, disId);
                    if (ShelterDAO.insert(s)) {
                        System.out.println("✓ Shelter registered successfully with ID: " + s.getShelterId());
                    }
                    break;
                case "3":
                    System.out.print("Enter Shelter ID to delete: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    if (ShelterDAO.delete(id)) {
                        System.out.println("✓ Shelter deleted.");
                    } else {
                        System.out.println("✗ Record not found.");
                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // 3. VICTIM MENU
    private static void victimMenu() {
        System.out.println("\n=== VICTIM REGISTRATION MENU ===");
        System.out.println("1. View All Victims");
        System.out.println("2. Register New Victim");
        System.out.println("3. Delete Victim");
        System.out.print("Enter choice: ");
        String c = sc.nextLine().trim();

        try {
            switch (c) {
                case "1":
                    List<Victim> list = VictimDAO.getAll();
                    System.out.println("\nID | Name | Age | Gender | Medical Condition | Disaster ID | Shelter ID");
                    System.out.println("-----------------------------------------------------------------------------");
                    for (Victim v : list) {
                        System.out.printf("%-3d| %-18s| %-3d| %-6s| %-25s| %-11d| %-10s\n",
                                v.getVictimId(), v.getName(), v.getAge(), v.getGender(),
                                v.getMedicalCondition(), v.getDisasterId(), (v.getShelterId() != null ? v.getShelterId() : "None"));
                    }
                    break;
                case "2":
                    System.out.print("Victim Full Name: ");
                    String name = sc.nextLine().trim();
                    System.out.print("Age: ");
                    int age = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Gender (Male/Female/Other): ");
                    String gender = sc.nextLine().trim();
                    System.out.print("Contact Number (or leave blank): ");
                    String phone = sc.nextLine().trim();
                    System.out.print("Medical Condition: ");
                    String med = sc.nextLine().trim();
                    System.out.print("Disaster ID: ");
                    int disId = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Shelter ID (0 for None/Field): ");
                    int shId = Integer.parseInt(sc.nextLine().trim());
                    Integer shelterId = (shId > 0) ? shId : null;

                    Victim v = new Victim(name, age, gender, phone.isEmpty() ? null : phone, med, disId, shelterId);
                    if (VictimDAO.insert(v)) {
                        System.out.println("✓ Victim registered with ID: " + v.getVictimId());
                    }
                    break;
                case "3":
                    System.out.print("Enter Victim ID to delete: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    if (VictimDAO.delete(id)) {
                        System.out.println("✓ Victim record deleted.");
                    } else {
                        System.out.println("✗ Record not found.");
                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // 4. RESOURCE MENU
    private static void resourceMenu() {
        System.out.println("\n=== RESOURCE CATALOG MENU ===");
        System.out.println("1. View All Resource Items");
        System.out.println("2. Add Resource Item");
        System.out.println("3. Delete Resource Item");
        System.out.print("Enter choice: ");
        String c = sc.nextLine().trim();

        try {
            switch (c) {
                case "1":
                    List<Resource> list = ResourceDAO.getAll();
                    System.out.println("\nID | Resource Name | Unit | Description");
                    System.out.println("-------------------------------------------------------------------");
                    for (Resource r : list) {
                        System.out.printf("%-3d| %-25s| %-10s| %s\n",
                                r.getResourceId(), r.getResourceName(), r.getUnit(), (r.getDescription() != null ? r.getDescription() : "-"));
                    }
                    break;
                case "2":
                    System.out.print("Resource Name (e.g. Water Bottles 1L): ");
                    String name = sc.nextLine().trim();
                    System.out.print("Packaging Unit (e.g. Bottles, Kits, Packets): ");
                    String unit = sc.nextLine().trim();
                    System.out.print("Description: ");
                    String desc = sc.nextLine().trim();

                    Resource r = new Resource(name, desc, unit);
                    if (ResourceDAO.insert(r)) {
                        System.out.println("✓ Resource cataloged with ID: " + r.getResourceId());
                    }
                    break;
                case "3":
                    System.out.print("Enter Resource ID to delete: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    if (ResourceDAO.delete(id)) {
                        System.out.println("✓ Resource deleted.");
                    } else {
                        System.out.println("✗ Record not found.");
                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // 5. INVENTORY MENU
    private static void inventoryMenu() {
        System.out.println("\n=== SHELTER INVENTORY MENU (Shelter ── Resource) ===");
        System.out.println("1. View Shelter Inventory Stock Levels");
        System.out.println("2. Allocate / Update Resource Stock at Shelter");
        System.out.println("3. Remove Stock Item from Shelter");
        System.out.print("Enter choice: ");
        String c = sc.nextLine().trim();

        try {
            switch (c) {
                case "1":
                    List<ShelterInventory> list = ShelterInventoryDAO.getAll();
                    System.out.println("\nShelter ID | Shelter Name | Resource ID | Resource Item | Quantity | Unit");
                    System.out.println("---------------------------------------------------------------------------------");
                    for (ShelterInventory si : list) {
                        System.out.printf("%-10d| %-22s| %-11d| %-20s| %-8d| %s\n",
                                si.getShelterId(), si.getShelterName(), si.getResourceId(), si.getResourceName(), si.getQuantity(), si.getUnit());
                    }
                    break;
                case "2":
                    System.out.print("Enter Shelter ID: ");
                    int sId = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Enter Resource ID: ");
                    int rId = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Enter Quantity Stocked: ");
                    int qty = Integer.parseInt(sc.nextLine().trim());

                    if (ShelterInventoryDAO.saveOrUpdate(sId, rId, qty)) {
                        System.out.println("✓ Inventory stock updated successfully.");
                    }
                    break;
                case "3":
                    System.out.print("Enter Shelter ID: ");
                    int dsId = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Enter Resource ID: ");
                    int drId = Integer.parseInt(sc.nextLine().trim());

                    if (ShelterInventoryDAO.delete(dsId, drId)) {
                        System.out.println("✓ Inventory record removed.");
                    } else {
                        System.out.println("✗ Record not found.");
                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // 6. VOLUNTEER MENU
    private static void volunteerMenu() {
        System.out.println("\n=== VOLUNTEER REGISTRY MENU ===");
        System.out.println("1. View All Volunteers");
        System.out.println("2. Register New Volunteer");
        System.out.println("3. Delete Volunteer");
        System.out.print("Enter choice: ");
        String c = sc.nextLine().trim();

        try {
            switch (c) {
                case "1":
                    List<Volunteer> list = VolunteerDAO.getAll();
                    System.out.println("\nID | Name | Age | Gender | Contact | Skills | Status");
                    System.out.println("-----------------------------------------------------------------------------");
                    for (Volunteer v : list) {
                        System.out.printf("%-3d| %-18s| %-3d| %-6s| %-14s| %-25s| %s\n",
                                v.getVolunteerId(), v.getName(), v.getAge(), v.getGender(), v.getContactNumber(), v.getSkills(), v.getAvailabilityStatus());
                    }
                    break;
                case "2":
                    System.out.print("Volunteer Name: ");
                    String name = sc.nextLine().trim();
                    System.out.print("Age (Min 18): ");
                    int age = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Gender (Male/Female/Other): ");
                    String gender = sc.nextLine().trim();
                    System.out.print("Contact Number: ");
                    String phone = sc.nextLine().trim();
                    System.out.print("Skills (e.g. First Aid, Rescue, Driving): ");
                    String skills = sc.nextLine().trim();
                    System.out.print("Availability (Available / Assigned / On Leave): ");
                    String status = sc.nextLine().trim();

                    Volunteer v = new Volunteer(name, age, gender, phone, skills, status);
                    if (VolunteerDAO.insert(v)) {
                        System.out.println("✓ Volunteer registered with ID: " + v.getVolunteerId());
                    }
                    break;
                case "3":
                    System.out.print("Enter Volunteer ID to delete: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    if (VolunteerDAO.delete(id)) {
                        System.out.println("✓ Volunteer deleted.");
                    } else {
                        System.out.println("✗ Record not found.");
                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // 7. ASSIGNMENT MENU
    private static void assignmentMenu() {
        System.out.println("\n=== VOLUNTEER ASSIGNMENTS MENU (Volunteer ── Disaster) ===");
        System.out.println("1. View All Volunteer Deployments");
        System.out.println("2. Deploy Volunteer to Disaster");
        System.out.println("3. Remove Volunteer Deployment");
        System.out.print("Enter choice: ");
        String c = sc.nextLine().trim();

        try {
            switch (c) {
                case "1":
                    List<VolunteerAssignment> list = VolunteerAssignmentDAO.getAll();
                    System.out.println("\nVol ID | Volunteer Name | Dis ID | Disaster Event | Operational Role | Shift");
                    System.out.println("---------------------------------------------------------------------------------");
                    for (VolunteerAssignment va : list) {
                        System.out.printf("%-6d| %-18s| %-6d| %-15s| %-20s| %s\n",
                                va.getVolunteerId(), va.getVolunteerName(), va.getDisasterId(), va.getDisasterType(), va.getRole(), va.getShiftTiming());
                    }
                    break;
                case "2":
                    System.out.print("Enter Volunteer ID: ");
                    int vId = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Enter Disaster ID: ");
                    int dId = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Enter Operational Role: ");
                    String role = sc.nextLine().trim();
                    System.out.print("Enter Shift Timing: ");
                    String shift = sc.nextLine().trim();

                    if (VolunteerAssignmentDAO.saveOrUpdate(vId, dId, role, shift)) {
                        System.out.println("✓ Volunteer deployment recorded successfully.");
                    }
                    break;
                case "3":
                    System.out.print("Enter Volunteer ID: ");
                    int dvId = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Enter Disaster ID: ");
                    int ddId = Integer.parseInt(sc.nextLine().trim());

                    if (VolunteerAssignmentDAO.delete(dvId, ddId)) {
                        System.out.println("✓ Deployment record removed.");
                    } else {
                        System.out.println("✗ Record not found.");
                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // 8. INCIDENT MENU
    private static void incidentMenu() {
        System.out.println("\n=== INCIDENT REPORTS MENU ===");
        System.out.println("1. View All Incident Reports");
        System.out.println("2. Log New Incident Report");
        System.out.println("3. Update Incident Status");
        System.out.println("4. Delete Incident Report");
        System.out.print("Enter choice: ");
        String c = sc.nextLine().trim();

        try {
            switch (c) {
                case "1":
                    List<IncidentReport> list = IncidentReportDAO.getAll();
                    System.out.println("\nID | Type | Status | Disaster Event | Volunteer | Logged Time | Description");
                    System.out.println("------------------------------------------------------------------------------------------");
                    for (IncidentReport ir : list) {
                        System.out.printf("%-3d| %-15s| %-10s| %-15s| %-15s| %-19s| %s\n",
                                ir.getReportId(), ir.getReportType(), ir.getStatus(),
                                ir.getDisasterType(), ir.getVolunteerName(), ir.getReportTime(), ir.getDescription());
                    }
                    break;
                case "2":
                    System.out.print("Disaster ID: ");
                    int dId = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Reporting Volunteer ID: ");
                    int vId = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Incident Type (e.g. Road Blockage, Medical Emergency): ");
                    String type = sc.nextLine().trim();
                    System.out.print("Status (Open / In Progress / Resolved): ");
                    String status = sc.nextLine().trim();
                    System.out.print("Description & Details: ");
                    String desc = sc.nextLine().trim();

                    IncidentReport report = new IncidentReport(desc, null, type, status, dId, vId);
                    if (IncidentReportDAO.insert(report)) {
                        System.out.println("✓ Incident report logged with ID: " + report.getReportId());
                    }
                    break;
                case "3":
                    System.out.print("Enter Incident Report ID: ");
                    int rId = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Enter New Status (Open / In Progress / Resolved / Closed): ");
                    String newStatus = sc.nextLine().trim();

                    if (IncidentReportDAO.updateStatus(rId, newStatus)) {
                        System.out.println("✓ Status updated.");
                    } else {
                        System.out.println("✗ Report not found.");
                    }
                    break;
                case "4":
                    System.out.print("Enter Incident Report ID to delete: ");
                    int delId = Integer.parseInt(sc.nextLine().trim());
                    if (IncidentReportDAO.delete(delId)) {
                        System.out.println("✓ Incident report deleted.");
                    } else {
                        System.out.println("✗ Record not found.");
                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // 9. ANALYTICS SUMMARY
    private static void showAnalytics() {
        System.out.println("\n==========================================================");
        System.out.println("       LIVE DATABASE ANALYTICS & METRICS SUMMARY          ");
        System.out.println("==========================================================");

        DashboardDAO.SystemStats stats = DashboardDAO.getSystemStats();
        System.out.println("• Active Disasters Recorded   : " + stats.totalDisasters);
        System.out.println("• Registered Affected Victims : " + stats.totalVictims);
        System.out.println("• Relief Shelters Established : " + stats.totalShelters);
        System.out.println("• Registered Relief Volunteers: " + stats.totalVolunteers);
        System.out.println("• Total Supply Units Stocked  : " + stats.totalInventoryUnits);
        System.out.println("• Open Emergency Incidents    : " + stats.openIncidents);

        System.out.println("\n[SQL Breakdown: Victims by Disaster Event]");
        try {
            List<Object[]> rows = DashboardDAO.getVictimsByDisaster();
            for (Object[] r : rows) {
                System.out.printf("  - %s (%s, Severity: %s): %d victims\n", r[0], r[1], r[2], r[3]);
            }
        } catch (Exception e) {
            System.err.println("  Error: " + e.getMessage());
        }

        System.out.println("\n[SQL Breakdown: Shelter Occupancy vs Capacity]");
        try {
            List<Object[]> rows = DashboardDAO.getShelterOccupancy();
            for (Object[] r : rows) {
                System.out.printf("  - %s (%s): Capacity %d | Occupancy %d | Available %d\n", r[0], r[1], r[2], r[3], r[4]);
            }
        } catch (Exception e) {
            System.err.println("  Error: " + e.getMessage());
        }
    }
}