import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShelterInventoryDAO {

    public static List<ShelterInventory> getAll() throws SQLException {
        List<ShelterInventory> list = new ArrayList<>();
        String sql = "SELECT si.shelter_id, si.resource_id, si.quantity, s.shelter_name, r.resource_name, r.unit " +
                     "FROM SHELTER_INVENTORY si " +
                     "JOIN SHELTER s ON si.shelter_id = s.shelter_id " +
                     "JOIN RESOURCE r ON si.resource_id = r.resource_id " +
                     "ORDER BY s.shelter_name ASC, r.resource_name ASC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ShelterInventory(
                        rs.getInt("shelter_id"),
                        rs.getInt("resource_id"),
                        rs.getInt("quantity"),
                        rs.getString("shelter_name"),
                        rs.getString("resource_name"),
                        rs.getString("unit")
                ));
            }
        }
        return list;
    }

    public static boolean saveOrUpdate(int shelterId, int resourceId, int quantity) throws SQLException {
        String sql = "INSERT INTO SHELTER_INVENTORY (shelter_id, resource_id, quantity) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE quantity = VALUES(quantity)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, shelterId);
            ps.setInt(2, resourceId);
            ps.setInt(3, quantity);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean updateQuantity(int shelterId, int resourceId, int newQuantity) throws SQLException {
        String sql = "UPDATE SHELTER_INVENTORY SET quantity = ? WHERE shelter_id = ? AND resource_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, shelterId);
            ps.setInt(3, resourceId);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean delete(int shelterId, int resourceId) throws SQLException {
        String sql = "DELETE FROM SHELTER_INVENTORY WHERE shelter_id = ? AND resource_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, shelterId);
            ps.setInt(2, resourceId);
            return ps.executeUpdate() > 0;
        }
    }

    public static List<ShelterInventory> search(String keyword) throws SQLException {
        List<ShelterInventory> list = new ArrayList<>();
        String sql = "SELECT si.shelter_id, si.resource_id, si.quantity, s.shelter_name, r.resource_name, r.unit " +
                     "FROM SHELTER_INVENTORY si " +
                     "JOIN SHELTER s ON si.shelter_id = s.shelter_id " +
                     "JOIN RESOURCE r ON si.resource_id = r.resource_id " +
                     "WHERE s.shelter_name LIKE ? OR r.resource_name LIKE ? " +
                     "ORDER BY s.shelter_name ASC, r.resource_name ASC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String term = "%" + keyword + "%";
            ps.setString(1, term);
            ps.setString(2, term);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ShelterInventory(
                            rs.getInt("shelter_id"),
                            rs.getInt("resource_id"),
                            rs.getInt("quantity"),
                            rs.getString("shelter_name"),
                            rs.getString("resource_name"),
                            rs.getString("unit")
                    ));
                }
            }
        }
        return list;
    }
}
