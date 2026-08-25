import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VolunteerAssignmentDAO {

    public static List<VolunteerAssignment> getAll() throws SQLException {
        List<VolunteerAssignment> list = new ArrayList<>();
        String sql = "SELECT va.volunteer_id, va.disaster_id, va.role, va.shift_timing, " +
                     "v.name AS volunteer_name, d.disaster_type, d.location AS disaster_location " +
                     "FROM VOLUNTEER_ASSIGNMENT va " +
                     "JOIN VOLUNTEER v ON va.volunteer_id = v.volunteer_id " +
                     "JOIN DISASTER d ON va.disaster_id = d.disaster_id " +
                     "ORDER BY d.disaster_type ASC, v.name ASC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new VolunteerAssignment(
                        rs.getInt("volunteer_id"),
                        rs.getInt("disaster_id"),
                        rs.getString("role"),
                        rs.getString("shift_timing"),
                        rs.getString("volunteer_name"),
                        rs.getString("disaster_type"),
                        rs.getString("disaster_location")
                ));
            }
        }
        return list;
    }

    public static boolean saveOrUpdate(int volunteerId, int disasterId, String role, String shiftTiming) throws SQLException {
        String sql = "INSERT INTO VOLUNTEER_ASSIGNMENT (volunteer_id, disaster_id, role, shift_timing) VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE role = VALUES(role), shift_timing = VALUES(shift_timing)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, volunteerId);
            ps.setInt(2, disasterId);
            ps.setString(3, role);
            ps.setString(4, shiftTiming);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean updateRoleAndShift(int volunteerId, int disasterId, String role, String shiftTiming) throws SQLException {
        String sql = "UPDATE VOLUNTEER_ASSIGNMENT SET role = ?, shift_timing = ? WHERE volunteer_id = ? AND disaster_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.setString(2, shiftTiming);
            ps.setInt(3, volunteerId);
            ps.setInt(4, disasterId);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean delete(int volunteerId, int disasterId) throws SQLException {
        String sql = "DELETE FROM VOLUNTEER_ASSIGNMENT WHERE volunteer_id = ? AND disaster_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, volunteerId);
            ps.setInt(2, disasterId);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean isVolunteerAssigned(int volunteerId, int disasterId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM VOLUNTEER_ASSIGNMENT WHERE volunteer_id = ? AND disaster_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, volunteerId);
            ps.setInt(2, disasterId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public static List<VolunteerAssignment> search(String keyword) throws SQLException {
        List<VolunteerAssignment> list = new ArrayList<>();
        String sql = "SELECT va.volunteer_id, va.disaster_id, va.role, va.shift_timing, " +
                     "v.name AS volunteer_name, d.disaster_type, d.location AS disaster_location " +
                     "FROM VOLUNTEER_ASSIGNMENT va " +
                     "JOIN VOLUNTEER v ON va.volunteer_id = v.volunteer_id " +
                     "JOIN DISASTER d ON va.disaster_id = d.disaster_id " +
                     "WHERE v.name LIKE ? OR d.disaster_type LIKE ? OR va.role LIKE ? " +
                     "ORDER BY d.disaster_type ASC, v.name ASC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String term = "%" + keyword + "%";
            ps.setString(1, term);
            ps.setString(2, term);
            ps.setString(3, term);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new VolunteerAssignment(
                            rs.getInt("volunteer_id"),
                            rs.getInt("disaster_id"),
                            rs.getString("role"),
                            rs.getString("shift_timing"),
                            rs.getString("volunteer_name"),
                            rs.getString("disaster_type"),
                            rs.getString("disaster_location")
                    ));
                }
            }
        }
        return list;
    }
}
