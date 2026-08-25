import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncidentReportDAO {

    public static List<IncidentReport> getAll() throws SQLException {
        List<IncidentReport> list = new ArrayList<>();
        String sql = "SELECT ir.report_id, ir.description, ir.report_time, ir.report_type, ir.status, ir.disaster_id, ir.volunteer_id, " +
                     "d.disaster_type, d.location AS disaster_location, v.name AS volunteer_name " +
                     "FROM INCIDENT_REPORT ir " +
                     "JOIN DISASTER d ON ir.disaster_id = d.disaster_id " +
                     "JOIN VOLUNTEER v ON ir.volunteer_id = v.volunteer_id " +
                     "ORDER BY ir.report_id DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                IncidentReport report = new IncidentReport(
                        rs.getInt("report_id"),
                        rs.getString("description"),
                        rs.getTimestamp("report_time"),
                        rs.getString("report_type"),
                        rs.getString("status"),
                        rs.getInt("disaster_id"),
                        rs.getInt("volunteer_id")
                );
                report.setDisasterType(rs.getString("disaster_type"));
                report.setDisasterLocation(rs.getString("disaster_location"));
                report.setVolunteerName(rs.getString("volunteer_name"));
                list.add(report);
            }
        }
        return list;
    }

    public static IncidentReport getById(int id) throws SQLException {
        String sql = "SELECT ir.report_id, ir.description, ir.report_time, ir.report_type, ir.status, ir.disaster_id, ir.volunteer_id, " +
                     "d.disaster_type, d.location AS disaster_location, v.name AS volunteer_name " +
                     "FROM INCIDENT_REPORT ir " +
                     "JOIN DISASTER d ON ir.disaster_id = d.disaster_id " +
                     "JOIN VOLUNTEER v ON ir.volunteer_id = v.volunteer_id " +
                     "WHERE ir.report_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    IncidentReport report = new IncidentReport(
                            rs.getInt("report_id"),
                            rs.getString("description"),
                            rs.getTimestamp("report_time"),
                            rs.getString("report_type"),
                            rs.getString("status"),
                            rs.getInt("disaster_id"),
                            rs.getInt("volunteer_id")
                    );
                    report.setDisasterType(rs.getString("disaster_type"));
                    report.setDisasterLocation(rs.getString("disaster_location"));
                    report.setVolunteerName(rs.getString("volunteer_name"));
                    return report;
                }
            }
        }
        return null;
    }

    public static boolean insert(IncidentReport report) throws SQLException {
        String sql = "INSERT INTO INCIDENT_REPORT (description, report_type, status, disaster_id, volunteer_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, report.getDescription());
            ps.setString(2, report.getReportType());
            ps.setString(3, report.getStatus());
            ps.setInt(4, report.getDisasterId());
            ps.setInt(5, report.getVolunteerId());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        report.setReportId(keys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean update(IncidentReport report) throws SQLException {
        String sql = "UPDATE INCIDENT_REPORT SET description = ?, report_type = ?, status = ?, disaster_id = ?, volunteer_id = ? WHERE report_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, report.getDescription());
            ps.setString(2, report.getReportType());
            ps.setString(3, report.getStatus());
            ps.setInt(4, report.getDisasterId());
            ps.setInt(5, report.getVolunteerId());
            ps.setInt(6, report.getReportId());
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean updateStatus(int reportId, String newStatus) throws SQLException {
        String sql = "UPDATE INCIDENT_REPORT SET status = ? WHERE report_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, reportId);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM INCIDENT_REPORT WHERE report_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public static List<IncidentReport> search(String keyword) throws SQLException {
        List<IncidentReport> list = new ArrayList<>();
        String sql = "SELECT ir.report_id, ir.description, ir.report_time, ir.report_type, ir.status, ir.disaster_id, ir.volunteer_id, " +
                     "d.disaster_type, d.location AS disaster_location, v.name AS volunteer_name " +
                     "FROM INCIDENT_REPORT ir " +
                     "JOIN DISASTER d ON ir.disaster_id = d.disaster_id " +
                     "JOIN VOLUNTEER v ON ir.volunteer_id = v.volunteer_id " +
                     "WHERE ir.description LIKE ? OR ir.report_type LIKE ? OR ir.status LIKE ? OR v.name LIKE ? " +
                     "ORDER BY ir.report_id DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String term = "%" + keyword + "%";
            ps.setString(1, term);
            ps.setString(2, term);
            ps.setString(3, term);
            ps.setString(4, term);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    IncidentReport report = new IncidentReport(
                            rs.getInt("report_id"),
                            rs.getString("description"),
                            rs.getTimestamp("report_time"),
                            rs.getString("report_type"),
                            rs.getString("status"),
                            rs.getInt("disaster_id"),
                            rs.getInt("volunteer_id")
                    );
                    report.setDisasterType(rs.getString("disaster_type"));
                    report.setDisasterLocation(rs.getString("disaster_location"));
                    report.setVolunteerName(rs.getString("volunteer_name"));
                    list.add(report);
                }
            }
        }
        return list;
    }
}
