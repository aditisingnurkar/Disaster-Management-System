public class Volunteer_Assignments extends VolunteerAssignment {
    public Volunteer_Assignments() { super(); }
    public Volunteer_Assignments(int volunteerId, int disasterId, String role, String shiftTiming) {
        super(volunteerId, disasterId, role, shiftTiming);
    }
    public Volunteer_Assignments(int volunteerId, int disasterId, String role, String shiftTiming, String volunteerName, String disasterType, String disasterLocation) {
        super(volunteerId, disasterId, role, shiftTiming, volunteerName, disasterType, disasterLocation);
    }
}
