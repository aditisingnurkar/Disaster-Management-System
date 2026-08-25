import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardDAO {

    public static class SystemStats {
        public int totalDisasters;
        public int totalVictims;
        public int totalShelters;
        public int totalVolunteers;
        public int totalInventoryUnits;
        public int openIncidents;
    }

    public static SystemStats getSystemStats() {
        SystemStats stats = new SystemStats();
        try (Connection con = DatabaseConnection.getConnection()) {
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM DISASTER")) {
                if (rs.next()) stats.totalDisasters = rs.getInt(1);
            }
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM VICTIM")) {
                if (rs.next()) stats.totalVictims = rs.getInt(1);
            }
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM SHELTER")) {
                if (rs.next()) stats.totalShelters = rs.getInt(1);
            }
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM VOLUNTEER")) {
                if (rs.next()) stats.totalVolunteers = rs.getInt(1);
            }
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COALESCE(SUM(quantity), 0) FROM SHELTER_INVENTORY")) {
                if (rs.next()) stats.totalInventoryUnits = rs.getInt(1);
            }
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM INCIDENT_REPORT WHERE status = 'Open'")) {
                if (rs.next()) stats.openIncidents = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching dashboard stats: " + e.getMessage());
        }
        return stats;
    }

    public static List<Object[]> getVictimsByDisaster() throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT d.disaster_type, d.location, d.severity, COUNT(v.victim_id) AS victim_count " +
                     "FROM DISASTER d " +
                     "LEFT JOIN VICTIM v ON d.disaster_id = v.disaster_id " +
                     "GROUP BY d.disaster_id, d.disaster_type, d.location, d.severity " +
                     "ORDER BY victim_count DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getString("disaster_type"),
                        rs.getString("location"),
                        rs.getString("severity"),
                        rs.getInt("victim_count")
                });
            }
        }
        return rows;
    }

    public static List<Object[]> getShelterOccupancy() throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT s.shelter_name, d.disaster_type, s.capacity, " +
                     "COUNT(v.victim_id) AS current_occupancy, " +
                     "(s.capacity - COUNT(v.victim_id)) AS available_space " +
                     "FROM SHELTER s " +
                     "JOIN DISASTER d ON s.disaster_id = d.disaster_id " +
                     "LEFT JOIN VICTIM v ON s.shelter_id = v.shelter_id " +
                     "GROUP BY s.shelter_id, s.shelter_name, d.disaster_type, s.capacity " +
                     "ORDER BY current_occupancy DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getString("shelter_name"),
                        rs.getString("disaster_type"),
                        rs.getInt("capacity"),
                        rs.getInt("current_occupancy"),
                        rs.getInt("available_space")
                });
            }
        }
        return rows;
    }

    public static List<Object[]> getRecentIncidents(int limit) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT ir.report_id, ir.report_type, ir.status, d.disaster_type, v.name AS volunteer_name, ir.report_time " +
                     "FROM INCIDENT_REPORT ir " +
                     "JOIN DISASTER d ON ir.disaster_id = d.disaster_id " +
                     "JOIN VOLUNTEER v ON ir.volunteer_id = v.volunteer_id " +
                     "ORDER BY ir.report_id DESC LIMIT ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{
                            rs.getInt("report_id"),
                            rs.getString("report_type"),
                            rs.getString("status"),
                            rs.getString("disaster_type"),
                            rs.getString("volunteer_name"),
                            rs.getTimestamp("report_time")
                    });
                }
            }
        }
        return rows;
    }
}
