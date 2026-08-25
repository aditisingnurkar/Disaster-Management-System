import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VolunteerDAO {

    public static List<Volunteer> getAll() throws SQLException {
        List<Volunteer> list = new ArrayList<>();
        String sql = "SELECT volunteer_id, name, age, gender, contact_number, skills, availability_status FROM VOLUNTEER ORDER BY volunteer_id DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Volunteer(
                        rs.getInt("volunteer_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("contact_number"),
                        rs.getString("skills"),
                        rs.getString("availability_status")
                ));
            }
        }
        return list;
    }

    public static Volunteer getById(int id) throws SQLException {
        String sql = "SELECT volunteer_id, name, age, gender, contact_number, skills, availability_status FROM VOLUNTEER WHERE volunteer_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Volunteer(
                            rs.getInt("volunteer_id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getString("contact_number"),
                            rs.getString("skills"),
                            rs.getString("availability_status")
                    );
                }
            }
        }
        return null;
    }

    public static boolean insert(Volunteer v) throws SQLException {
        String sql = "INSERT INTO VOLUNTEER (name, age, gender, contact_number, skills, availability_status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, v.getName());
            ps.setInt(2, v.getAge());
            ps.setString(3, v.getGender());
            ps.setString(4, v.getContactNumber());
            ps.setString(5, v.getSkills());
            ps.setString(6, v.getAvailabilityStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        v.setVolunteerId(keys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean update(Volunteer v) throws SQLException {
        String sql = "UPDATE VOLUNTEER SET name = ?, age = ?, gender = ?, contact_number = ?, skills = ?, availability_status = ? WHERE volunteer_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getName());
            ps.setInt(2, v.getAge());
            ps.setString(3, v.getGender());
            ps.setString(4, v.getContactNumber());
            ps.setString(5, v.getSkills());
            ps.setString(6, v.getAvailabilityStatus());
            ps.setInt(7, v.getVolunteerId());
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM VOLUNTEER WHERE volunteer_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public static List<Volunteer> search(String keyword) throws SQLException {
        List<Volunteer> list = new ArrayList<>();
        String sql = "SELECT volunteer_id, name, age, gender, contact_number, skills, availability_status FROM VOLUNTEER " +
                     "WHERE name LIKE ? OR skills LIKE ? OR availability_status LIKE ? ORDER BY volunteer_id DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String term = "%" + keyword + "%";
            ps.setString(1, term);
            ps.setString(2, term);
            ps.setString(3, term);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Volunteer(
                            rs.getInt("volunteer_id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getString("contact_number"),
                            rs.getString("skills"),
                            rs.getString("availability_status")
                    ));
                }
            }
        }
        return list;
    }

    public static List<ComboItem> getComboItems() throws SQLException {
        List<ComboItem> list = new ArrayList<>();
        String sql = "SELECT volunteer_id, name, skills FROM VOLUNTEER ORDER BY volunteer_id ASC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("volunteer_id");
                String label = "ID " + id + ": " + rs.getString("name") + " (" + rs.getString("skills") + ")";
                list.add(new ComboItem(id, label));
            }
        }
        return list;
    }

    public static List<ComboItem> getAssignedVolunteersForDisaster(int disasterId) throws SQLException {
        List<ComboItem> list = new ArrayList<>();
        String sql = "SELECT v.volunteer_id, v.name, va.role " +
                     "FROM VOLUNTEER v " +
                     "JOIN VOLUNTEER_ASSIGNMENT va ON v.volunteer_id = va.volunteer_id " +
                     "WHERE va.disaster_id = ? " +
                     "ORDER BY v.name ASC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, disasterId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("volunteer_id");
                    String label = "ID " + id + ": " + rs.getString("name") + " [" + rs.getString("role") + "]";
                    list.add(new ComboItem(id, label));
                }
            }
        }
        return list;
    }
}
