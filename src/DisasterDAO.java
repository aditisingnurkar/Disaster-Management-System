import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisasterDAO {

    public static List<Disaster> getAll() throws SQLException {
        List<Disaster> list = new ArrayList<>();
        String sql = "SELECT disaster_id, disaster_type, location, start_date, end_date, severity FROM DISASTER ORDER BY disaster_id DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Disaster(
                        rs.getInt("disaster_id"),
                        rs.getString("disaster_type"),
                        rs.getString("location"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("severity")
                ));
            }
        }
        return list;
    }

    public static Disaster getById(int id) throws SQLException {
        String sql = "SELECT disaster_id, disaster_type, location, start_date, end_date, severity FROM DISASTER WHERE disaster_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Disaster(
                            rs.getInt("disaster_id"),
                            rs.getString("disaster_type"),
                            rs.getString("location"),
                            rs.getDate("start_date"),
                            rs.getDate("end_date"),
                            rs.getString("severity")
                    );
                }
            }
        }
        return null;
    }

    public static boolean insert(Disaster d) throws SQLException {
        String sql = "INSERT INTO DISASTER (disaster_type, location, start_date, end_date, severity) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getDisasterType());
            ps.setString(2, d.getLocation());
            ps.setDate(3, d.getStartDate());
            if (d.getEndDate() != null) {
                ps.setDate(4, d.getEndDate());
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setString(5, d.getSeverity());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        d.setDisasterId(keys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean update(Disaster d) throws SQLException {
        String sql = "UPDATE DISASTER SET disaster_type = ?, location = ?, start_date = ?, end_date = ?, severity = ? WHERE disaster_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getDisasterType());
            ps.setString(2, d.getLocation());
            ps.setDate(3, d.getStartDate());
            if (d.getEndDate() != null) {
                ps.setDate(4, d.getEndDate());
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setString(5, d.getSeverity());
            ps.setInt(6, d.getDisasterId());
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM DISASTER WHERE disaster_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public static List<Disaster> search(String keyword) throws SQLException {
        List<Disaster> list = new ArrayList<>();
        String sql = "SELECT disaster_id, disaster_type, location, start_date, end_date, severity FROM DISASTER " +
                     "WHERE disaster_type LIKE ? OR location LIKE ? OR severity LIKE ? ORDER BY disaster_id DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String term = "%" + keyword + "%";
            ps.setString(1, term);
            ps.setString(2, term);
            ps.setString(3, term);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Disaster(
                            rs.getInt("disaster_id"),
                            rs.getString("disaster_type"),
                            rs.getString("location"),
                            rs.getDate("start_date"),
                            rs.getDate("end_date"),
                            rs.getString("severity")
                    ));
                }
            }
        }
        return list;
    }

    public static List<ComboItem> getComboItems() throws SQLException {
        List<ComboItem> list = new ArrayList<>();
        String sql = "SELECT disaster_id, disaster_type, location FROM DISASTER ORDER BY disaster_id ASC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("disaster_id");
                String label = "ID " + id + ": " + rs.getString("disaster_type") + " (" + rs.getString("location") + ")";
                list.add(new ComboItem(id, label));
            }
        }
        return list;
    }
}
