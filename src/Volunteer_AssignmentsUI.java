import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Volunteer_AssignmentsUI extends JFrame {

    private JComboBox<ComboItem> volunteerCombo;
    private JComboBox<ComboItem> disasterCombo;
    private JTextField roleField, shiftField, searchField;
    private JTable table;
    private DefaultTableModel tableModel;

    // Track active selection
    private Integer selectedVolunteerId = null;
    private Integer selectedDisasterId = null;

    public Volunteer_AssignmentsUI() {
        setTitle("Volunteer Deployment - Disaster Assignments");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.PRIMARY_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel titleLabel = new JLabel("🎯 Volunteer Field Assignments (M:N Volunteer ── Disaster)");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Deploy registered volunteers to specific disaster zones, allocate operational roles and shifts");
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
        JPanel formCard = UITheme.createCard("Assignment Details", UITheme.PRIMARY_DARK);
        formCard.setPreferredSize(new Dimension(420, 0));

        JPanel formGrid = new JPanel(new GridLayout(4, 2, 8, 14));
        formGrid.setOpaque(false);

        volunteerCombo = UITheme.createComboBox();
        disasterCombo = UITheme.createComboBox();
        roleField = UITheme.createTextField();
        shiftField = UITheme.createTextField();

        formGrid.add(new JLabel("Volunteer [FK]:"));
        formGrid.add(volunteerCombo);
        formGrid.add(new JLabel("Disaster Event [FK]:"));
        formGrid.add(disasterCombo);
        formGrid.add(new JLabel("Operational Role:"));
        formGrid.add(roleField);
        formGrid.add(new JLabel("Shift Timing:"));
        formGrid.add(shiftField);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        buttonPanel.setOpaque(false);

        JButton saveBtn = UITheme.createPrimaryButton("💾 Assign / Update", UITheme.ACCENT_BLUE, UITheme.ACCENT_HOVER);
        JButton deleteBtn = UITheme.createPrimaryButton("🗑 Remove Deployment", UITheme.DANGER_RED, UITheme.DANGER_HOVER);
        JButton clearBtn = UITheme.createPrimaryButton("🔄 Clear Form", new Color(100, 116, 139), new Color(71, 85, 105));
        JButton refreshBtn = UITheme.createPrimaryButton("⟳ Reload", UITheme.PRIMARY_DARK, UITheme.PRIMARY_NAVY);

        saveBtn.addActionListener(e -> saveAssignment());
        deleteBtn.addActionListener(e -> deleteAssignment());
        clearBtn.addActionListener(e -> clearForm());
        refreshBtn.addActionListener(e -> { loadCombos(); loadData(); });

        buttonPanel.add(saveBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(refreshBtn);

        JPanel formWrapper = new JPanel(new BorderLayout(0, 15));
        formWrapper.setOpaque(false);
        formWrapper.add(formGrid, BorderLayout.NORTH);
        formWrapper.add(buttonPanel, BorderLayout.SOUTH);

        formCard.add(formWrapper, BorderLayout.CENTER);
        contentPanel.add(formCard, BorderLayout.WEST);

        // Right Table Card
        JPanel tableCard = UITheme.createCard("Active Volunteer Deployments", UITheme.PRIMARY_DARK);

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

        searchBar.add(new JLabel("Search Deployments: "), BorderLayout.WEST);
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchBtnBox, BorderLayout.EAST);

        // Table
        String[] columns = {"Vol ID", "Volunteer Name", "Disaster ID", "Disaster Type", "Role", "Shift Timing"};
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
                    int vId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    int dId = Integer.parseInt(tableModel.getValueAt(row, 2).toString());
                    String role = tableModel.getValueAt(row, 4).toString();
                    String shift = tableModel.getValueAt(row, 5).toString();

                    selectedVolunteerId = vId;
                    selectedDisasterId = dId;

                    selectVolunteerInCombo(vId);
                    selectDisasterInCombo(dId);
                    roleField.setText(role);
                    shiftField.setText(shift);
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

        loadCombos();
        loadData();
    }

    private void loadCombos() {
        volunteerCombo.removeAllItems();
        disasterCombo.removeAllItems();

        try {
            List<ComboItem> volunteers = VolunteerDAO.getComboItems();
            for (ComboItem item : volunteers) volunteerCombo.addItem(item);

            List<ComboItem> disasters = DisasterDAO.getComboItems();
            for (ComboItem item : disasters) disasterCombo.addItem(item);
        } catch (Exception e) {
            System.err.println("Error loading assignment combos: " + e.getMessage());
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

    private void selectDisasterInCombo(int disasterId) {
        for (int i = 0; i < disasterCombo.getItemCount(); i++) {
            ComboItem item = disasterCombo.getItemAt(i);
            if (item != null && item.getId() == disasterId) {
                disasterCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<VolunteerAssignment> list = VolunteerAssignmentDAO.getAll();
            for (VolunteerAssignment va : list) {
                tableModel.addRow(new Object[]{
                        va.getVolunteerId(),
                        va.getVolunteerName(),
                        va.getDisasterId(),
                        va.getDisasterType(),
                        va.getRole(),
                        va.getShiftTiming()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading assignments: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchRecords() {
        String kw = searchField.getText().trim();
        if (kw.isEmpty()) { loadData(); return; }

        tableModel.setRowCount(0);
        try {
            List<VolunteerAssignment> list = VolunteerAssignmentDAO.search(kw);
            for (VolunteerAssignment va : list) {
                tableModel.addRow(new Object[]{
                        va.getVolunteerId(),
                        va.getVolunteerName(),
                        va.getDisasterId(),
                        va.getDisasterType(),
                        va.getRole(),
                        va.getShiftTiming()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching assignments: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveAssignment() {
        ComboItem selectedVolunteer = (ComboItem) volunteerCombo.getSelectedItem();
        ComboItem selectedDisaster = (ComboItem) disasterCombo.getSelectedItem();
        String role = roleField.getText().trim();
        String shift = shiftField.getText().trim();

        if (selectedVolunteer == null || selectedDisaster == null || role.isEmpty() || shift.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a Volunteer, a Disaster, and enter Role and Shift Timing.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (VolunteerAssignmentDAO.saveOrUpdate(selectedVolunteer.getId(), selectedDisaster.getId(), role, shift)) {
                JOptionPane.showMessageDialog(this, "Volunteer assignment saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving assignment: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAssignment() {
        if (selectedVolunteerId == null || selectedDisasterId == null) {
            JOptionPane.showMessageDialog(this, "Please select an assignment record from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this volunteer deployment (Volunteer ID: " + selectedVolunteerId + ", Disaster ID: " + selectedDisasterId + ")?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (VolunteerAssignmentDAO.delete(selectedVolunteerId, selectedDisasterId)) {
                    JOptionPane.showMessageDialog(this, "Deployment record removed successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadData();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error removing assignment: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        roleField.setText("");
        shiftField.setText("");
        selectedVolunteerId = null;
        selectedDisasterId = null;
        if (volunteerCombo.getItemCount() > 0) volunteerCombo.setSelectedIndex(0);
        if (disasterCombo.getItemCount() > 0) disasterCombo.setSelectedIndex(0);
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Volunteer_AssignmentsUI().setVisible(true));
    }
}