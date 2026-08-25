public class VolunteerAssignment {
    private int volunteerId;
    private int disasterId;
    private String role;
    private String shiftTiming;

    // Joined fields for easy display
    private String volunteerName;
    private String disasterType;
    private String disasterLocation;

    public VolunteerAssignment() {}

    public VolunteerAssignment(int volunteerId, int disasterId, String role, String shiftTiming) {
        this.volunteerId = volunteerId;
        this.disasterId = disasterId;
        this.role = role;
        this.shiftTiming = shiftTiming;
    }

    public VolunteerAssignment(int volunteerId, int disasterId, String role, String shiftTiming, String volunteerName, String disasterType, String disasterLocation) {
        this.volunteerId = volunteerId;
        this.disasterId = disasterId;
        this.role = role;
        this.shiftTiming = shiftTiming;
        this.volunteerName = volunteerName;
        this.disasterType = disasterType;
        this.disasterLocation = disasterLocation;
    }

    public int getVolunteerId() { return volunteerId; }
    public void setVolunteerId(int volunteerId) { this.volunteerId = volunteerId; }

    public int getDisasterId() { return disasterId; }
    public void setDisasterId(int disasterId) { this.disasterId = disasterId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getShiftTiming() { return shiftTiming; }
    public void setShiftTiming(String shiftTiming) { this.shiftTiming = shiftTiming; }

    public String getVolunteerName() { return volunteerName; }
    public void setVolunteerName(String volunteerName) { this.volunteerName = volunteerName; }

    public String getDisasterType() { return disasterType; }
    public void setDisasterType(String disasterType) { this.disasterType = disasterType; }

    public String getDisasterLocation() { return disasterLocation; }
    public void setDisasterLocation(String disasterLocation) { this.disasterLocation = disasterLocation; }

    @Override
    public String toString() {
        return "Assignment [Volunteer " + volunteerId + ", Disaster " + disasterId + ": " + role + "]";
    }
}
