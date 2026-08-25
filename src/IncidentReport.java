import java.sql.Timestamp;

public class IncidentReport {
    private int reportId;
    private String description;
    private Timestamp reportTime;
    private String reportType;
    private String status; // 'Open', 'In Progress', 'Resolved', 'Closed'
    private int disasterId;
    private int volunteerId;

    // Joined fields
    private String disasterType;
    private String disasterLocation;
    private String volunteerName;

    public IncidentReport() {}

    public IncidentReport(int reportId, String description, Timestamp reportTime, String reportType, String status, int disasterId, int volunteerId) {
        this.reportId = reportId;
        this.description = description;
        this.reportTime = reportTime;
        this.reportType = reportType;
        this.status = status;
        this.disasterId = disasterId;
        this.volunteerId = volunteerId;
    }

    public IncidentReport(String description, Timestamp reportTime, String reportType, String status, int disasterId, int volunteerId) {
        this.description = description;
        this.reportTime = reportTime;
        this.reportType = reportType;
        this.status = status;
        this.disasterId = disasterId;
        this.volunteerId = volunteerId;
    }

    public int getReportId() { return reportId; }
    public void setReportId(int reportId) { this.reportId = reportId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getReportTime() { return reportTime; }
    public void setReportTime(Timestamp reportTime) { this.reportTime = reportTime; }

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getDisasterId() { return disasterId; }
    public void setDisasterId(int disasterId) { this.disasterId = disasterId; }

    public int getVolunteerId() { return volunteerId; }
    public void setVolunteerId(int volunteerId) { this.volunteerId = volunteerId; }

    public String getDisasterType() { return disasterType; }
    public void setDisasterType(String disasterType) { this.disasterType = disasterType; }

    public String getDisasterLocation() { return disasterLocation; }
    public void setDisasterLocation(String disasterLocation) { this.disasterLocation = disasterLocation; }

    public String getVolunteerName() { return volunteerName; }
    public void setVolunteerName(String volunteerName) { this.volunteerName = volunteerName; }

    @Override
    public String toString() {
        return "Incident #" + reportId + " (" + reportType + " - " + status + ")";
    }
}
