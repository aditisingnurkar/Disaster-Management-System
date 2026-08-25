import java.sql.Date;

public class Disaster {
    private int disasterId;
    private String disasterType;
    private String location;
    private Date startDate;
    private Date endDate;
    private String severity;

    public Disaster() {}

    public Disaster(int disasterId, String disasterType, String location, Date startDate, Date endDate, String severity) {
        this.disasterId = disasterId;
        this.disasterType = disasterType;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.severity = severity;
    }

    public Disaster(String disasterType, String location, Date startDate, Date endDate, String severity) {
        this.disasterType = disasterType;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.severity = severity;
    }

    public int getDisasterId() { return disasterId; }
    public void setDisasterId(int disasterId) { this.disasterId = disasterId; }

    public String getDisasterType() { return disasterType; }
    public void setDisasterType(String disasterType) { this.disasterType = disasterType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    @Override
    public String toString() {
        return "Disaster #" + disasterId + " (" + disasterType + " - " + location + ")";
    }
}
