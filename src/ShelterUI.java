import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ShelterUI extends JFrame {

    private JTextField idField, nameField, addressField, capacityField, coordinatorField, contactField, searchField;
    private JComboBox<ComboItem> disasterCombo;
    private JTable table;
    private DefaultTableModel tableModel;

    public ShelterUI() {
        setTitle("Shelter Management - Relief Camps");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.PRIMARY_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel titleLabel = new JLabel("⛺ Relief Shelter & Camp Management");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Manage relief camps, accommodation capacity, coordinators, and disaster links");
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
        JPanel formCard = UITheme.createCard("Shelter Camp Details", UITheme.PRIMARY_DARK);
        formCard.setPreferredSize(new Dimension(400, 0));

        JPanel formGrid = new JPanel(new GridLayout(7, 2, 8, 12));
        formGrid.setOpaque(false);

        idField = UITheme.createTextField();
        idField.setEditable(false);
        idField.setBackground(new Color(241, 245, 249));

        nameField = UITheme.createTextField();
        addressField = UITheme.createTextField();
        capacityField = UITheme.createTextField();
        coordinatorField = UITheme.createTextField();
        contactField = UITheme.createTextField();
        disasterCombo = UITheme.createComboBox();

        formGrid.add(new JLabel("Shelter ID:"));
        formGrid.add(idField);
        formGrid.add(new JLabel("Shelter Name:"));
        formGrid.add(nameField);
        formGrid.add(new JLabel("Address / Area:"));
        formGrid.add(addressField);
        formGrid.add(new JLabel("Bed Capacity:"));
        formGrid.add(capacityField);
        formGrid.add(new JLabel("Coordinator Name:"));
        formGrid.add(coordinatorField);
        formGrid.add(new JLabel("Contact Number:"));
        formGrid.add(contactField);
        formGrid.add(new JLabel("Disaster Event:"));
        formGrid.add(disasterCombo);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        buttonPanel.setOpaque(false);

        JButton addBtn = UITheme.createPrimaryButton("➕ Add Shelter", UITheme.ACCENT_BLUE, UITheme.ACCENT_HOVER);
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
        JPanel tableCard = UITheme.createCard("Registered Shelters & Camp Status", UITheme.PRIMARY_DARK);

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

        searchBar.add(new JLabel("Search Shelters: "), BorderLayout.WEST);
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchBtnBox, BorderLayout.EAST);

        // Table
        String[] columns = {"ID", "Shelter Name", "Address", "Capacity", "Coordinator", "Contact", "Disaster ID"};
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
                    nameField.setText(tableModel.getValueAt(row, 1).toString());
                    addressField.setText(tableModel.getValueAt(row, 2).toString());
                    capacityField.setText(tableModel.getValueAt(row, 3).toString());
                    coordinatorField.setText(tableModel.getValueAt(row, 4).toString());
                    contactField.setText(tableModel.getValueAt(row, 5).toString());
                    int disId = Integer.parseInt(tableModel.getValueAt(row, 6).toString());
                    selectDisasterInCombo(disId);
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
        } catch (Exception e) {
            System.err.println("Error loading disasters into combo: " + e.getMessage());
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
            List<Shelter> list = ShelterDAO.getAll();
            for (Shelter s : list) {
                tableModel.addRow(new Object[]{
                        s.getShelterId(),
                        s.getShelterName(),
                        s.getAddress(),
                        s.getCapacity(),
                        s.getCoordinatorName(),
                        s.getContactNumber(),
                        s.getDisasterId()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading shelters: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchRecords() {
        String kw = searchField.getText().trim();
        if (kw.isEmpty()) { loadData(); return; }

        tableModel.setRowCount(0);
        try {
            List<Shelter> list = ShelterDAO.search(kw);
            for (Shelter s : list) {
                tableModel.addRow(new Object[]{
                        s.getShelterId(),
                        s.getShelterName(),
                        s.getAddress(),
                        s.getCapacity(),
                        s.getCoordinatorName(),
                        s.getContactNumber(),
                        s.getDisasterId()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRecord() {
        try {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String capStr = capacityField.getText().trim();
            String coord = coordinatorField.getText().trim();
            String contact = contactField.getText().trim();
            ComboItem selectedDisaster = (ComboItem) disasterCombo.getSelectedItem();

            if (name.isEmpty() || address.isEmpty() || capStr.isEmpty() || coord.isEmpty() || contact.isEmpty() || selectedDisaster == null) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields and select a Disaster.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int capacity = Integer.parseInt(capStr);
            if (capacity <= 0) {
                JOptionPane.showMessageDialog(this, "Capacity must be greater than 0.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Shelter s = new Shelter(name, address, capacity, coord, contact, selectedDisaster.getId());
            if (ShelterDAO.insert(s)) {
                JOptionPane.showMessageDialog(this, "Shelter registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Capacity must be a valid number.", "Invalid Number", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding shelter: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRecord() {
        String idStr = idField.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a shelter from the table to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String capStr = capacityField.getText().trim();
            String coord = coordinatorField.getText().trim();
            String contact = contactField.getText().trim();
            ComboItem selectedDisaster = (ComboItem) disasterCombo.getSelectedItem();

            if (name.isEmpty() || address.isEmpty() || capStr.isEmpty() || coord.isEmpty() || contact.isEmpty() || selectedDisaster == null) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int capacity = Integer.parseInt(capStr);
            if (capacity <= 0) {
                JOptionPane.showMessageDialog(this, "Capacity must be greater than 0.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Shelter s = new Shelter(id, name, address, capacity, coord, contact, selectedDisaster.getId());
            if (ShelterDAO.update(s)) {
                JOptionPane.showMessageDialog(this, "Shelter updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating shelter: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        String idStr = idField.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a shelter from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Shelter ID " + idStr + "?\nAssociated victims will have their shelter assignment set to NULL.",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idStr);
                if (ShelterDAO.delete(id)) {
                    JOptionPane.showMessageDialog(this, "Shelter deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadData();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting shelter: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        addressField.setText("");
        capacityField.setText("");
        coordinatorField.setText("");
        contactField.setText("");
        if (disasterCombo.getItemCount() > 0) disasterCombo.setSelectedIndex(0);
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ShelterUI().setVisible(true));
    }
}
