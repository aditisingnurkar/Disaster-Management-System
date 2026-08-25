import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAO {

    public static List<Resource> getAll() throws SQLException {
        List<Resource> list = new ArrayList<>();
        String sql = "SELECT resource_id, resource_name, description, unit FROM RESOURCE ORDER BY resource_id DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Resource(
                        rs.getInt("resource_id"),
                        rs.getString("resource_name"),
                        rs.getString("description"),
                        rs.getString("unit")
                ));
            }
        }
        return list;
    }

    public static Resource getById(int id) throws SQLException {
        String sql = "SELECT resource_id, resource_name, description, unit FROM RESOURCE WHERE resource_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Resource(
                            rs.getInt("resource_id"),
                            rs.getString("resource_name"),
                            rs.getString("description"),
                            rs.getString("unit")
                    );
                }
            }
        }
        return null;
    }

    public static boolean insert(Resource r) throws SQLException {
        String sql = "INSERT INTO RESOURCE (resource_name, description, unit) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getResourceName());
            ps.setString(2, r.getDescription());
            ps.setString(3, r.getUnit());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        r.setResourceId(keys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean update(Resource r) throws SQLException {
        String sql = "UPDATE RESOURCE SET resource_name = ?, description = ?, unit = ? WHERE resource_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getResourceName());
            ps.setString(2, r.getDescription());
            ps.setString(3, r.getUnit());
            ps.setInt(4, r.getResourceId());
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM RESOURCE WHERE resource_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public static List<Resource> search(String keyword) throws SQLException {
        List<Resource> list = new ArrayList<>();
        String sql = "SELECT resource_id, resource_name, description, unit FROM RESOURCE " +
                     "WHERE resource_name LIKE ? OR description LIKE ? OR unit LIKE ? ORDER BY resource_id DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String term = "%" + keyword + "%";
            ps.setString(1, term);
            ps.setString(2, term);
            ps.setString(3, term);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Resource(
                            rs.getInt("resource_id"),
                            rs.getString("resource_name"),
                            rs.getString("description"),
                            rs.getString("unit")
                    ));
                }
            }
        }
        return list;
    }

    public static List<ComboItem> getComboItems() throws SQLException {
        List<ComboItem> list = new ArrayList<>();
        String sql = "SELECT resource_id, resource_name, unit FROM RESOURCE ORDER BY resource_id ASC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("resource_id");
                String label = "ID " + id + ": " + rs.getString("resource_name") + " (" + rs.getString("unit") + ")";
                list.add(new ComboItem(id, label));
            }
        }
        return list;
    }
}
