import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShelterDAO {

    public static List<Shelter> getAll() throws SQLException {
        List<Shelter> list = new ArrayList<>();
        String sql = "SELECT shelter_id, shelter_name, address, capacity, coordinator_name, contact_number, disaster_id FROM SHELTER ORDER BY shelter_id DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Shelter(
                        rs.getInt("shelter_id"),
                        rs.getString("shelter_name"),
                        rs.getString("address"),
                        rs.getInt("capacity"),
                        rs.getString("coordinator_name"),
                        rs.getString("contact_number"),
                        rs.getInt("disaster_id")
                ));
            }
        }
        return list;
    }

    public static Shelter getById(int id) throws SQLException {
        String sql = "SELECT shelter_id, shelter_name, address, capacity, coordinator_name, contact_number, disaster_id FROM SHELTER WHERE shelter_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Shelter(
                            rs.getInt("shelter_id"),
                            rs.getString("shelter_name"),
                            rs.getString("address"),
                            rs.getInt("capacity"),
                            rs.getString("coordinator_name"),
                            rs.getString("contact_number"),
                            rs.getInt("disaster_id")
                    );
                }
            }
        }
        return null;
    }

    public static boolean insert(Shelter s) throws SQLException {
        String sql = "INSERT INTO SHELTER (shelter_name, address, capacity, coordinator_name, contact_number, disaster_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getShelterName());
            ps.setString(2, s.getAddress());
            ps.setInt(3, s.getCapacity());
            ps.setString(4, s.getCoordinatorName());
            ps.setString(5, s.getContactNumber());
            ps.setInt(6, s.getDisasterId());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        s.setShelterId(keys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean update(Shelter s) throws SQLException {
        String sql = "UPDATE SHELTER SET shelter_name = ?, address = ?, capacity = ?, coordinator_name = ?, contact_number = ?, disaster_id = ? WHERE shelter_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getShelterName());
            ps.setString(2, s.getAddress());
            ps.setInt(3, s.getCapacity());
            ps.setString(4, s.getCoordinatorName());
            ps.setString(5, s.getContactNumber());
            ps.setInt(6, s.getDisasterId());
            ps.setInt(7, s.getShelterId());
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM SHELTER WHERE shelter_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public static List<Shelter> search(String keyword) throws SQLException {
        List<Shelter> list = new ArrayList<>();
        String sql = "SELECT shelter_id, shelter_name, address, capacity, coordinator_name, contact_number, disaster_id FROM SHELTER " +
                     "WHERE shelter_name LIKE ? OR address LIKE ? OR coordinator_name LIKE ? ORDER BY shelter_id DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String term = "%" + keyword + "%";
            ps.setString(1, term);
            ps.setString(2, term);
            ps.setString(3, term);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Shelter(
                            rs.getInt("shelter_id"),
                            rs.getString("shelter_name"),
                            rs.getString("address"),
                            rs.getInt("capacity"),
                            rs.getString("coordinator_name"),
                            rs.getString("contact_number"),
                            rs.getInt("disaster_id")
                    ));
                }
            }
        }
        return list;
    }

    public static List<ComboItem> getComboItems() throws SQLException {
        List<ComboItem> list = new ArrayList<>();
        String sql = "SELECT shelter_id, shelter_name, capacity FROM SHELTER ORDER BY shelter_id ASC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("shelter_id");
                String label = "ID " + id + ": " + rs.getString("shelter_name") + " (Cap: " + rs.getInt("capacity") + ")";
                list.add(new ComboItem(id, label));
            }
        }
        return list;
    }
}
