import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.List;

public class DisasterUI extends JFrame {

    private JTextField idField, typeField, locationField, startDateField, endDateField, searchField;
    private JComboBox<String> severityCombo;
    private JTable table;
    private DefaultTableModel tableModel;

    public DisasterUI() {
        setTitle("Disaster Management - Master Records");
        setSize(1150, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.PRIMARY_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel titleLabel = new JLabel("🌍 Disaster Events Management");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Register, monitor, update and track major disaster occurrences");
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
        JPanel formCard = UITheme.createCard("Disaster Record Details", UITheme.PRIMARY_DARK);
        formCard.setPreferredSize(new Dimension(380, 0));

        JPanel formGrid = new JPanel(new GridLayout(6, 2, 8, 12));
        formGrid.setOpaque(false);

        idField = UITheme.createTextField();
        idField.setEditable(false);
        idField.setBackground(new Color(241, 245, 249));

        typeField = UITheme.createTextField();
        locationField = UITheme.createTextField();
        startDateField = UITheme.createTextField();
        endDateField = UITheme.createTextField();

        severityCombo = new JComboBox<>(new String[]{"Low", "Medium", "High", "Critical"});
        severityCombo.setFont(UITheme.FONT_BODY);
        severityCombo.setBackground(Color.WHITE);

        formGrid.add(new JLabel("Disaster ID:"));
        formGrid.add(idField);
        formGrid.add(new JLabel("Disaster Type:"));
        formGrid.add(typeField);
        formGrid.add(new JLabel("Location:"));
        formGrid.add(locationField);
        formGrid.add(new JLabel("Start Date (YYYY-MM-DD):"));
        formGrid.add(startDateField);
        formGrid.add(new JLabel("End Date (Optional):"));
        formGrid.add(endDateField);
        formGrid.add(new JLabel("Severity Level:"));
        formGrid.add(severityCombo);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        buttonPanel.setOpaque(false);

        JButton addBtn = UITheme.createPrimaryButton("➕ Add Disaster", UITheme.ACCENT_BLUE, UITheme.ACCENT_HOVER);
        JButton updateBtn = UITheme.createPrimaryButton("✏ Update", UITheme.WARNING_ORANGE, new Color(217, 119, 6));
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

        JPanel formWrapper = new JPanel(new BorderLayout(0, 15));
        formWrapper.setOpaque(false);
        formWrapper.add(formGrid, BorderLayout.NORTH);
        formWrapper.add(buttonPanel, BorderLayout.SOUTH);

        formCard.add(formWrapper, BorderLayout.CENTER);
        contentPanel.add(formCard, BorderLayout.WEST);

        // Right Table Card
        JPanel tableCard = UITheme.createCard("Registered Disasters", UITheme.PRIMARY_DARK);

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

        searchBar.add(new JLabel("Search Disasters: "), BorderLayout.WEST);
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchBtnBox, BorderLayout.EAST);

        // Table
        String[] columns = {"ID", "Type", "Location", "Start Date", "End Date", "Severity"};
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
                    idField.setText(tableModel.getValueAt(row, 0).toString());
                    typeField.setText(tableModel.getValueAt(row, 1).toString());
                    locationField.setText(tableModel.getValueAt(row, 2).toString());
                    startDateField.setText(tableModel.getValueAt(row, 3).toString());
                    Object endVal = tableModel.getValueAt(row, 4);
                    endDateField.setText(endVal != null ? endVal.toString() : "");
                    severityCombo.setSelectedItem(tableModel.getValueAt(row, 5).toString());
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

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<Disaster> list = DisasterDAO.getAll();
            for (Disaster d : list) {
                tableModel.addRow(new Object[]{
                        d.getDisasterId(),
                        d.getDisasterType(),
                        d.getLocation(),
                        d.getStartDate(),
                        d.getEndDate() != null ? d.getEndDate() : "-",
                        d.getSeverity()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading disasters: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchRecords() {
        String kw = searchField.getText().trim();
        if (kw.isEmpty()) { loadData(); return; }

        tableModel.setRowCount(0);
        try {
            List<Disaster> list = DisasterDAO.search(kw);
            for (Disaster d : list) {
                tableModel.addRow(new Object[]{
                        d.getDisasterId(),
                        d.getDisasterType(),
                        d.getLocation(),
                        d.getStartDate(),
                        d.getEndDate() != null ? d.getEndDate() : "-",
                        d.getSeverity()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRecord() {
        try {
            String type = typeField.getText().trim();
            String loc = locationField.getText().trim();
            String startStr = startDateField.getText().trim();
            String endStr = endDateField.getText().trim();
            String severity = (String) severityCombo.getSelectedItem();

            if (type.isEmpty() || loc.isEmpty() || startStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in Type, Location, and Start Date.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Date startDate = Date.valueOf(startStr);
            Date endDate = endStr.isEmpty() ? null : Date.valueOf(endStr);

            Disaster d = new Disaster(type, loc, startDate, endDate, severity);
            if (DisasterDAO.insert(d)) {
                JOptionPane.showMessageDialog(this, "Disaster record added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Date format must be YYYY-MM-DD.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding disaster: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRecord() {
        String idStr = idField.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a disaster from the table to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            String type = typeField.getText().trim();
            String loc = locationField.getText().trim();
            String startStr = startDateField.getText().trim();
            String endStr = endDateField.getText().trim();
            String severity = (String) severityCombo.getSelectedItem();

            Date startDate = Date.valueOf(startStr);
            Date endDate = endStr.isEmpty() || endStr.equals("-") ? null : Date.valueOf(endStr);

            Disaster d = new Disaster(id, type, loc, startDate, endDate, severity);
            if (DisasterDAO.update(d)) {
                JOptionPane.showMessageDialog(this, "Disaster record updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating disaster: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        String idStr = idField.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a disaster from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Disaster ID " + idStr + "?\nWarning: All associated shelters, victims, assignments, and reports will also be deleted (CASCADE).",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idStr);
                if (DisasterDAO.delete(id)) {
                    JOptionPane.showMessageDialog(this, "Disaster deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadData();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting disaster: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        typeField.setText("");
        locationField.setText("");
        startDateField.setText("");
        endDateField.setText("");
        severityCombo.setSelectedIndex(1);
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DisasterUI().setVisible(true));
    }
}