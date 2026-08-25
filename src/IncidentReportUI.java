import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.List;

public class IncidentReportUI extends JFrame {

    private JTextField idField, timeField, searchField;
    private JTextArea descArea;
    private JComboBox<String> typeCombo;
    private JComboBox<String> statusCombo;
    private JComboBox<ComboItem> disasterCombo;
    private JComboBox<ComboItem> volunteerCombo;
    private JTable table;
    private DefaultTableModel tableModel;

    public IncidentReportUI() {
        setTitle("Incident Command - Emergency Logs & Field Reports");
        setSize(1250, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.PRIMARY_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel titleLabel = new JLabel("🚨 Emergency Incident Reports");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Log real-time emergency observations, blockages, resource needs, and track response resolution");
        subtitleLabel.setFont(UITheme.FONT_BODY);
        subtitleLabel.setForeground(new Color(203, 213, 225));

        JPanel titleBox = new JPanel(new GridLayout(2, 1, 0, 4));
        titleBox.setOpaque(false);
        titleBox.add(titleLabel);
        titleBox.add(subtitleLabel);
        headerPanel.add(titleBox, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Center Content
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(UITheme.BG_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Left Form Card
        JPanel formCard = UITheme.createCard("Incident Log Entry", UITheme.PRIMARY_DARK);
        formCard.setPreferredSize(new Dimension(420, 0));

        JPanel formGrid = new JPanel(new GridLayout(6, 2, 8, 10));
        formGrid.setOpaque(false);

        idField = UITheme.createTextField();
        idField.setEditable(false);
        idField.setBackground(new Color(241, 245, 249));

        timeField = UITheme.createTextField();
        timeField.setEditable(false);
        timeField.setBackground(new Color(241, 245, 249));

        typeCombo = new JComboBox<>(new String[]{
                "Road Blockage", "Building Collapse", "Medical Emergency", "Resource Shortage",
                "Flooding", "Missing Person", "Fire Hazard", "Power Outage", "Other"
        });
        typeCombo.setFont(UITheme.FONT_BODY);
        typeCombo.setBackground(Color.WHITE);

        statusCombo = new JComboBox<>(new String[]{"Open", "In Progress", "Resolved", "Closed"});
        statusCombo.setFont(UITheme.FONT_BODY);
        statusCombo.setBackground(Color.WHITE);

        disasterCombo = UITheme.createComboBox();
        volunteerCombo = UITheme.createComboBox();

        // When disaster changes, load volunteers assigned to that disaster (Business Rule)
        disasterCombo.addActionListener(e -> updateVolunteersForSelectedDisaster());

        formGrid.add(new JLabel("Report ID:"));
        formGrid.add(idField);
        formGrid.add(new JLabel("Disaster Event [FK]:"));
        formGrid.add(disasterCombo);
        formGrid.add(new JLabel("Assigned Volunteer [FK]:"));
        formGrid.add(volunteerCombo);
        formGrid.add(new JLabel("Incident Type:"));
        formGrid.add(typeCombo);
        formGrid.add(new JLabel("Resolution Status:"));
        formGrid.add(statusCombo);
        formGrid.add(new JLabel("Logged Timestamp:"));
        formGrid.add(timeField);

        // Description Text Area
        descArea = new JTextArea(4, 20);
        descArea.setFont(UITheme.FONT_BODY);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Incident Description & Observations"),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        buttonPanel.setOpaque(false);

        JButton addBtn = UITheme.createPrimaryButton("➕ Submit Report", UITheme.ACCENT_BLUE, UITheme.ACCENT_HOVER);
        JButton updateBtn = UITheme.createPrimaryButton("✏ Update Status", UITheme.WARNING_ORANGE, new Color(217, 119, 6));
        JButton deleteBtn = UITheme.createPrimaryButton("🗑 Delete", UITheme.DANGER_RED, UITheme.DANGER_HOVER);
        JButton clearBtn = UITheme.createPrimaryButton("🔄 Clear Form", new Color(100, 116, 139), new Color(71, 85, 105));

        addBtn.addActionListener(e -> addRecord());
        updateBtn.addActionListener(e -> updateRecord());
        deleteBtn.addActionListener(e -> deleteRecord());
        clearBtn.addActionListener(e -> clearForm());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        JPanel leftCenter = new JPanel(new BorderLayout(0, 10));
        leftCenter.setOpaque(false);
        leftCenter.add(formGrid, BorderLayout.NORTH);
        leftCenter.add(descScroll, BorderLayout.CENTER);
        leftCenter.add(buttonPanel, BorderLayout.SOUTH);

        formCard.add(leftCenter, BorderLayout.CENTER);
        contentPanel.add(formCard, BorderLayout.WEST);

        // Right Table Card
        JPanel tableCard = UITheme.createCard("Logged Incident Reports", UITheme.PRIMARY_DARK);

        // Search Bar
        JPanel searchBar = new JPanel(new BorderLayout(8, 0));
        searchBar.setOpaque(false);
        searchField = UITheme.createTextField();
        JButton searchBtn = UITheme.createPrimaryButton("🔍 Search", UITheme.PRIMARY_DARK, UITheme.PRIMARY_NAVY);
        JButton resetBtn = UITheme.createPrimaryButton("Show All", new Color(100, 116, 139), new Color(71, 85, 105));

        searchBtn.addActionListener(e -> searchRecords());
        resetBtn.addActionListener(e -> { searchField.setText(""); loadData(); });

        JPanel searchBtnBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchBtnBox.setOpaque(false);
        searchBtnBox.add(searchBtn);
        searchBtnBox.add(resetBtn);

        searchBar.add(new JLabel("Search Reports: "), BorderLayout.WEST);
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchBtnBox, BorderLayout.EAST);

        // Table
        String[] columns = {"ID", "Type", "Status", "Disaster", "Reported By", "Report Time", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    loadSelectedReportToForm(id);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_GRAY));

        JPanel tableContainer = new JPanel(new BorderLayout(0, 10));
        tableContainer.setOpaque(false);
        tableContainer.add(searchBar, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        tableCard.add(tableContainer, BorderLayout.CENTER);
        contentPanel.add(tableCard, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        loadDisasterCombo();
        loadData();
    }

    private void loadDisasterCombo() {
        disasterCombo.removeAllItems();
        try {
            List<ComboItem> items = DisasterDAO.getComboItems();
            for (ComboItem item : items) {
                disasterCombo.addItem(item);
            }
            updateVolunteersForSelectedDisaster();
        } catch (Exception e) {
            System.err.println("Error loading disasters: " + e.getMessage());
        }
    }

    private void updateVolunteersForSelectedDisaster() {
        volunteerCombo.removeAllItems();
        ComboItem selectedDisaster = (ComboItem) disasterCombo.getSelectedItem();
        if (selectedDisaster == null) return;

        try {
            // First try to load assigned volunteers for this disaster (Enforcing business rule)
            List<ComboItem> assigned = VolunteerDAO.getAssignedVolunteersForDisaster(selectedDisaster.getId());
            if (!assigned.isEmpty()) {
                for (ComboItem v : assigned) volunteerCombo.addItem(v);
            } else {
                // If none assigned yet, load all volunteers with an indicator
                List<ComboItem> all = VolunteerDAO.getComboItems();
                for (ComboItem v : all) volunteerCombo.addItem(v);
            }
        } catch (Exception e) {
            System.err.println("Error loading volunteers for disaster: " + e.getMessage());
        }
    }

    private void loadSelectedReportToForm(int reportId) {
        try {
            IncidentReport r = IncidentReportDAO.getById(reportId);
            if (r != null) {
                idField.setText(String.valueOf(r.getReportId()));
                typeCombo.setSelectedItem(r.getReportType());
                statusCombo.setSelectedItem(r.getStatus());
                timeField.setText(r.getReportTime() != null ? r.getReportTime().toString() : "");
                descArea.setText(r.getDescription());

                selectDisasterInCombo(r.getDisasterId());
                selectVolunteerInCombo(r.getVolunteerId());
            }
        } catch (Exception e) {
            System.err.println("Error loading report details: " + e.getMessage());
        }
    }

    private void selectDisasterInCombo(int disasterId) {
        for (int i = 0; i < disasterCombo.getItemCount(); i++) {
            ComboItem item = disasterCombo.getItemAt(i);
            if (item != null && item.getId() == disasterId) {
                disasterCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void selectVolunteerInCombo(int volunteerId) {
        for (int i = 0; i < volunteerCombo.getItemCount(); i++) {
            ComboItem item = volunteerCombo.getItemAt(i);
            if (item != null && item.getId() == volunteerId) {
                volunteerCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<IncidentReport> list = IncidentReportDAO.getAll();
            for (IncidentReport r : list) {
                tableModel.addRow(new Object[]{
                        r.getReportId(),
                        r.getReportType(),
                        r.getStatus(),
                        r.getDisasterType() + " (" + r.getDisasterLocation() + ")",
                        r.getVolunteerName(),
                        r.getReportTime(),
                        r.getDescription()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading reports: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchRecords() {
        String kw = searchField.getText().trim();
        if (kw.isEmpty()) { loadData(); return; }

        tableModel.setRowCount(0);
        try {
            List<IncidentReport> list = IncidentReportDAO.search(kw);
            for (IncidentReport r : list) {
                tableModel.addRow(new Object[]{
                        r.getReportId(),
                        r.getReportType(),
                        r.getStatus(),
                        r.getDisasterType() + " (" + r.getDisasterLocation() + ")",
                        r.getVolunteerName(),
                        r.getReportTime(),
                        r.getDescription()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRecord() {
        try {
            String desc = descArea.getText().trim();
            String type = (String) typeCombo.getSelectedItem();
            String status = (String) statusCombo.getSelectedItem();
            ComboItem selectedDisaster = (ComboItem) disasterCombo.getSelectedItem();
            ComboItem selectedVolunteer = (ComboItem) volunteerCombo.getSelectedItem();

            if (desc.isEmpty() || selectedDisaster == null || selectedVolunteer == null) {
                JOptionPane.showMessageDialog(this, "Please select Disaster, Volunteer, and provide an Incident Description.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check if volunteer is assigned to this disaster
            boolean isAssigned = VolunteerAssignmentDAO.isVolunteerAssigned(selectedVolunteer.getId(), selectedDisaster.getId());
            if (!isAssigned) {
                int proceed = JOptionPane.showConfirmDialog(this,
                        "Note: Volunteer " + selectedVolunteer.getLabel() + " is not formally recorded in VOLUNTEER_ASSIGNMENT for this disaster.\nDo you still want to log this report?",
                        "Assignment Notice", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (proceed != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            IncidentReport report = new IncidentReport(desc, null, type, status, selectedDisaster.getId(), selectedVolunteer.getId());
            if (IncidentReportDAO.insert(report)) {
                JOptionPane.showMessageDialog(this, "Incident report logged successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error submitting incident report: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRecord() {
        String idStr = idField.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an incident report from the table to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            String desc = descArea.getText().trim();
            String type = (String) typeCombo.getSelectedItem();
            String status = (String) statusCombo.getSelectedItem();
            ComboItem selectedDisaster = (ComboItem) disasterCombo.getSelectedItem();
            ComboItem selectedVolunteer = (ComboItem) volunteerCombo.getSelectedItem();

            if (desc.isEmpty() || selectedDisaster == null || selectedVolunteer == null) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            IncidentReport report = new IncidentReport(id, desc, null, type, status, selectedDisaster.getId(), selectedVolunteer.getId());
            if (IncidentReportDAO.update(report)) {
                JOptionPane.showMessageDialog(this, "Incident report updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating report: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        String idStr = idField.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an incident report from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Incident Report #" + idStr + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idStr);
                if (IncidentReportDAO.delete(id)) {
                    JOptionPane.showMessageDialog(this, "Report deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadData();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting report: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        timeField.setText("");
        descArea.setText("");
        typeCombo.setSelectedIndex(0);
        statusCombo.setSelectedIndex(0);
        if (disasterCombo.getItemCount() > 0) disasterCombo.setSelectedIndex(0);
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IncidentReportUI().setVisible(true));
    }
}
