import java.sql.Timestamp;

public class Incident_Report extends IncidentReport {
    public Incident_Report() { super(); }
    public Incident_Report(int reportId, String description, Timestamp reportTime, String reportType, String status, int disasterId, int volunteerId) {
        super(reportId, description, reportTime, reportType, status, disasterId, volunteerId);
    }
    public Incident_Report(String description, Timestamp reportTime, String reportType, String status, int disasterId, int volunteerId) {
        super(description, reportTime, reportType, status, disasterId, volunteerId);
    }
}
