import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VictimDAO {

    public static List<Victim> getAll() throws SQLException {
        List<Victim> list = new ArrayList<>();
        String sql = "SELECT victim_id, name, age, gender, contact_number, medical_condition, disaster_id, shelter_id FROM VICTIM ORDER BY victim_id DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int shelterIdVal = rs.getInt("shelter_id");
                Integer shelterId = rs.wasNull() ? null : shelterIdVal;
                list.add(new Victim(
                        rs.getInt("victim_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("contact_number"),
                        rs.getString("medical_condition"),
                        rs.getInt("disaster_id"),
                        shelterId
                ));
            }
        }
        return list;
    }

    public static Victim getById(int id) throws SQLException {
        String sql = "SELECT victim_id, name, age, gender, contact_number, medical_condition, disaster_id, shelter_id FROM VICTIM WHERE victim_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int shelterIdVal = rs.getInt("shelter_id");
                    Integer shelterId = rs.wasNull() ? null : shelterIdVal;
                    return new Victim(
                            rs.getInt("victim_id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getString("contact_number"),
                            rs.getString("medical_condition"),
                            rs.getInt("disaster_id"),
                            shelterId
                    );
                }
            }
        }
        return null;
    }

    public static boolean insert(Victim v) throws SQLException {
        String sql = "INSERT INTO VICTIM (name, age, gender, contact_number, medical_condition, disaster_id, shelter_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, v.getName());
            ps.setInt(2, v.getAge());
            ps.setString(3, v.getGender());
            ps.setString(4, v.getContactNumber());
            ps.setString(5, v.getMedicalCondition());
            ps.setInt(6, v.getDisasterId());
            if (v.getShelterId() != null && v.getShelterId() > 0) {
                ps.setInt(7, v.getShelterId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        v.setVictimId(keys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean update(Victim v) throws SQLException {
        String sql = "UPDATE VICTIM SET name = ?, age = ?, gender = ?, contact_number = ?, medical_condition = ?, disaster_id = ?, shelter_id = ? WHERE victim_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getName());
            ps.setInt(2, v.getAge());
            ps.setString(3, v.getGender());
            ps.setString(4, v.getContactNumber());
            ps.setString(5, v.getMedicalCondition());
            ps.setInt(6, v.getDisasterId());
            if (v.getShelterId() != null && v.getShelterId() > 0) {
                ps.setInt(7, v.getShelterId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.setInt(8, v.getVictimId());
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM VICTIM WHERE victim_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public static List<Victim> search(String keyword) throws SQLException {
        List<Victim> list = new ArrayList<>();
        String sql = "SELECT victim_id, name, age, gender, contact_number, medical_condition, disaster_id, shelter_id FROM VICTIM " +
                     "WHERE name LIKE ? OR medical_condition LIKE ? OR contact_number LIKE ? ORDER BY victim_id DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String term = "%" + keyword + "%";
            ps.setString(1, term);
            ps.setString(2, term);
            ps.setString(3, term);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int shelterIdVal = rs.getInt("shelter_id");
                    Integer shelterId = rs.wasNull() ? null : shelterIdVal;
                    list.add(new Victim(
                            rs.getInt("victim_id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getString("contact_number"),
                            rs.getString("medical_condition"),
                            rs.getInt("disaster_id"),
                            shelterId
                    ));
                }
            }
        }
        return list;
    }
}
