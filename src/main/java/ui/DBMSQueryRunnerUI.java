import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class DBMSQueryRunnerUI extends JFrame {

    private JComboBox<String> queryPresetCombo;
    private JTextArea sqlEditorArea;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JLabel countLabel;

    private static final String[][] PRESET_QUERIES = {
            {
                "1. [Aggregates] System Wide Statistics (COUNT, SUM)",
                "SELECT \n" +
                "    (SELECT COUNT(*) FROM DISASTER) AS total_disasters,\n" +
                "    (SELECT COUNT(*) FROM VICTIM) AS total_victims,\n" +
                "    (SELECT COUNT(*) FROM SHELTER) AS total_shelters,\n" +
                "    (SELECT COUNT(*) FROM VOLUNTEER) AS total_volunteers,\n" +
                "    (SELECT SUM(quantity) FROM SHELTER_INVENTORY) AS total_inventory_units,\n" +
                "    (SELECT COUNT(*) FROM INCIDENT_REPORT WHERE status = 'Open') AS open_incidents;"
            },
            {
                "2. [3NF Multi-Table JOIN] Victims with Disaster & Shelter Details",
                "SELECT \n" +
                "    v.victim_id, v.name AS victim_name, v.age, v.gender, v.medical_condition,\n" +
                "    d.disaster_type, d.location AS disaster_location,\n" +
                "    COALESCE(s.shelter_name, 'Unassigned / Field') AS shelter_name\n" +
                "FROM VICTIM v\n" +
                "INNER JOIN DISASTER d ON v.disaster_id = d.disaster_id\n" +
                "LEFT JOIN SHELTER s ON v.shelter_id = s.shelter_id\n" +
                "ORDER BY v.victim_id;"
            },
            {
                "3. [JOIN + GROUP BY] Shelter Occupancy vs Max Capacity Analysis",
                "SELECT \n" +
                "    s.shelter_id, s.shelter_name, d.disaster_type, s.capacity AS max_capacity,\n" +
                "    COUNT(v.victim_id) AS current_occupancy,\n" +
                "    (s.capacity - COUNT(v.victim_id)) AS available_space,\n" +
                "    ROUND((COUNT(v.victim_id) / s.capacity) * 100, 2) AS occupancy_pct\n" +
                "FROM SHELTER s\n" +
                "JOIN DISASTER d ON s.disaster_id = d.disaster_id\n" +
                "LEFT JOIN VICTIM v ON s.shelter_id = v.shelter_id\n" +
                "GROUP BY s.shelter_id, s.shelter_name, d.disaster_type, s.capacity\n" +
                "ORDER BY occupancy_pct DESC;"
            },
            {
                "4. [M:N Junction JOIN] Volunteer Assignments (Volunteer ── Disaster)",
                "SELECT \n" +
                "    vol.volunteer_id, vol.name AS volunteer_name, vol.contact_number,\n" +
                "    d.disaster_type, d.location AS disaster_location,\n" +
                "    va.role, va.shift_timing\n" +
                "FROM VOLUNTEER_ASSIGNMENT va\n" +
                "INNER JOIN VOLUNTEER vol ON va.volunteer_id = vol.volunteer_id\n" +
                "INNER JOIN DISASTER d ON va.disaster_id = d.disaster_id\n" +
                "ORDER BY d.disaster_type, vol.name;"
            },
            {
                "5. [M:N Junction JOIN] Shelter Inventory Logistics (Shelter ── Resource)",
                "SELECT \n" +
                "    s.shelter_id, s.shelter_name, r.resource_name, si.quantity, r.unit\n" +
                "FROM SHELTER_INVENTORY si\n" +
                "INNER JOIN SHELTER s ON si.shelter_id = s.shelter_id\n" +
                "INNER JOIN RESOURCE r ON si.resource_id = r.resource_id\n" +
                "ORDER BY s.shelter_name, r.resource_name;"
            },
            {
                "6. [GROUP BY + HAVING] Shelters with Total Stock > 1,000 Units",
                "SELECT \n" +
                "    s.shelter_id, s.shelter_name, SUM(si.quantity) AS total_items_stocked\n" +
                "FROM SHELTER s\n" +
                "JOIN SHELTER_INVENTORY si ON s.shelter_id = si.shelter_id\n" +
                "GROUP BY s.shelter_id, s.shelter_name\n" +
                "HAVING SUM(si.quantity) > 1000\n" +
                "ORDER BY total_items_stocked DESC;"
            },
            {
                "7. [Subquery NOT IN] Available Volunteers NOT Assigned to Any Disaster",
                "SELECT volunteer_id, name, contact_number, skills, availability_status\n" +
                "FROM VOLUNTEER\n" +
                "WHERE volunteer_id NOT IN (\n" +
                "    SELECT DISTINCT volunteer_id FROM VOLUNTEER_ASSIGNMENT\n" +
                ");"
            },
            {
                "8. [Correlated Subquery] Disasters with More than 2 Registered Victims",
                "SELECT d.disaster_id, d.disaster_type, d.location, d.severity\n" +
                "FROM DISASTER d\n" +
                "WHERE (\n" +
                "    SELECT COUNT(*) FROM VICTIM v WHERE v.disaster_id = d.disaster_id\n" +
                ") >= 2;"
            },
            {
                "9. [JOIN + ORDER BY] Incident Reports with Reporter and Disaster",
                "SELECT \n" +
                "    ir.report_id, ir.report_type, ir.status, ir.description,\n" +
                "    d.disaster_type, d.location,\n" +
                "    v.name AS reported_by, v.contact_number AS reporter_phone,\n" +
                "    ir.report_time\n" +
                "FROM INCIDENT_REPORT ir\n" +
                "INNER JOIN DISASTER d ON ir.disaster_id = d.disaster_id\n" +
                "INNER JOIN VOLUNTEER v ON ir.volunteer_id = v.volunteer_id\n" +
                "ORDER BY ir.report_time DESC;"
            },
            {
                "10. [Aggregations AVG, MIN, MAX] Victim Age Statistics",
                "SELECT \n" +
                "    COUNT(*) AS total_victims,\n" +
                "    ROUND(AVG(age), 1) AS average_age,\n" +
                "    MIN(age) AS youngest_age,\n" +
                "    MAX(age) AS oldest_age\n" +
                "FROM VICTIM;"
            }
    };

    public DBMSQueryRunnerUI() {
        setTitle("DBMS SQL Query Demonstrator & Analytics");
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.PRIMARY_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("⚡ DBMS Live SQL Query Demonstrator (Viva Mode)");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Execute 3NF JOINs, aggregations, groupings, and custom SQL live against MySQL");
        subtitleLabel.setFont(UITheme.FONT_BODY);
        subtitleLabel.setForeground(new Color(203, 213, 225));

        JPanel titleContainer = new JPanel(new GridLayout(2, 1, 0, 4));
        titleContainer.setOpaque(false);
        titleContainer.add(titleLabel);
        titleContainer.add(subtitleLabel);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Center Split Panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        centerPanel.setBackground(UITheme.BG_LIGHT);

        // Top Control / SQL Editor Card
        JPanel topCard = UITheme.createCard("Select Preset Viva Query or Write Custom SQL", UITheme.PRIMARY_DARK);

        queryPresetCombo = new JComboBox<>();
        for (String[] preset : PRESET_QUERIES) {
            queryPresetCombo.addItem(preset[0]);
        }
        queryPresetCombo.setFont(UITheme.FONT_BODY);
        queryPresetCombo.setBackground(Color.WHITE);
        queryPresetCombo.addActionListener(e -> {
            int idx = queryPresetCombo.getSelectedIndex();
            if (idx >= 0 && idx < PRESET_QUERIES.length) {
                sqlEditorArea.setText(PRESET_QUERIES[idx][1]);
            }
        });

        sqlEditorArea = new JTextArea(6, 60);
        sqlEditorArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        sqlEditorArea.setText(PRESET_QUERIES[0][1]);
        sqlEditorArea.setLineWrap(false);
        JScrollPane editorScroll = new JScrollPane(sqlEditorArea);
        editorScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_GRAY));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton executeBtn = UITheme.createPrimaryButton("▶ Execute Query", UITheme.SUCCESS_GREEN, new Color(5, 150, 105));
        executeBtn.addActionListener(e -> executeSQL());

        JButton clearBtn = UITheme.createPrimaryButton("Clear", UITheme.WARNING_ORANGE, new Color(217, 119, 6));
        clearBtn.addActionListener(e -> sqlEditorArea.setText(""));

        btnPanel.add(clearBtn);
        btnPanel.add(executeBtn);

        JPanel queryControls = new JPanel(new BorderLayout(0, 8));
        queryControls.setOpaque(false);
        queryControls.add(queryPresetCombo, BorderLayout.NORTH);
        queryControls.add(editorScroll, BorderLayout.CENTER);
        queryControls.add(btnPanel, BorderLayout.SOUTH);

        topCard.add(queryControls, BorderLayout.CENTER);
        centerPanel.add(topCard, BorderLayout.NORTH);

        // Bottom Result Table Card
        JPanel tableCard = UITheme.createCard("Query Execution Results", UITheme.PRIMARY_DARK);

        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        UITheme.styleTable(resultTable);

        JScrollPane tableScroll = new JScrollPane(resultTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_GRAY));

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setOpaque(false);
        statusLabel = new JLabel("Ready. Click 'Execute Query' to run.");
        statusLabel.setFont(UITheme.FONT_BODY);
        statusLabel.setForeground(UITheme.TEXT_MUTED);

        countLabel = new JLabel("Rows: 0");
        countLabel.setFont(UITheme.FONT_BOLD);
        countLabel.setForeground(UITheme.PRIMARY_DARK);

        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(countLabel, BorderLayout.EAST);

        tableCard.add(tableScroll, BorderLayout.CENTER);
        tableCard.add(statusPanel, BorderLayout.SOUTH);

        centerPanel.add(tableCard, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Run initial query
        executeSQL();
    }

    private void executeSQL() {
        String sql = sqlEditorArea.getText().trim();
        if (sql.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a SQL query to execute.", "Empty Query", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement()) {

            long start = System.currentTimeMillis();
            boolean isResultSet = st.execute(sql);
            long elapsed = System.currentTimeMillis() - start;

            if (isResultSet) {
                try (ResultSet rs = st.getResultSet()) {
                    ResultSetMetaData md = rs.getMetaData();
                    int columnCount = md.getColumnCount();

                    Vector<String> columnNames = new Vector<>();
                    for (int i = 1; i <= columnCount; i++) {
                        columnNames.add(md.getColumnLabel(i));
                    }

                    Vector<Vector<Object>> data = new Vector<>();
                    int rowCount = 0;
                    while (rs.next()) {
                        Vector<Object> row = new Vector<>();
                        for (int i = 1; i <= columnCount; i++) {
                            row.add(rs.getObject(i));
                        }
                        data.add(row);
                        rowCount++;
                    }

                    tableModel.setDataVector(data, columnNames);
                    UITheme.styleTable(resultTable);

                    statusLabel.setText("Query executed successfully in " + elapsed + " ms.");
                    statusLabel.setForeground(UITheme.SUCCESS_GREEN);
                    countLabel.setText("Rows returned: " + rowCount);
                }
            } else {
                int updateCount = st.getUpdateCount();
                tableModel.setDataVector(new Vector<>(), new Vector<>());
                statusLabel.setText("Query OK, " + updateCount + " row(s) affected in " + elapsed + " ms.");
                statusLabel.setForeground(UITheme.SUCCESS_GREEN);
                countLabel.setText("Affected Rows: " + updateCount);
            }

        } catch (SQLException e) {
            statusLabel.setText("SQL Error: " + e.getMessage());
            statusLabel.setForeground(UITheme.DANGER_RED);
            countLabel.setText("Error");
            JOptionPane.showMessageDialog(this, "Error executing SQL:\n" + e.getMessage(), "SQL Execution Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DBMSQueryRunnerUI().setVisible(true));
    }
}
