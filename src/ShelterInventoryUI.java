import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ShelterInventoryUI extends JFrame {

    private JComboBox<ComboItem> shelterCombo;
    private JComboBox<ComboItem> resourceCombo;
    private JTextField quantityField, searchField;
    private JTable table;
    private DefaultTableModel tableModel;

    // Track active selection
    private Integer selectedShelterId = null;
    private Integer selectedResourceId = null;

    public ShelterInventoryUI() {
        setTitle("Shelter Inventory - Resource Logistics & Stock Allocation");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.PRIMARY_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel titleLabel = new JLabel("📊 Shelter Inventory Management (M:N Shelter ── Resource)");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Allocate and track relief supplies and inventory stock levels across emergency shelters");
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
        JPanel formCard = UITheme.createCard("Stock Allocation Entry", UITheme.PRIMARY_DARK);
        formCard.setPreferredSize(new Dimension(400, 0));

        JPanel formGrid = new JPanel(new GridLayout(3, 2, 8, 14));
        formGrid.setOpaque(false);

        shelterCombo = UITheme.createComboBox();
        resourceCombo = UITheme.createComboBox();
        quantityField = UITheme.createTextField();

        formGrid.add(new JLabel("Target Shelter [FK]:"));
        formGrid.add(shelterCombo);
        formGrid.add(new JLabel("Relief Resource [FK]:"));
        formGrid.add(resourceCombo);
        formGrid.add(new JLabel("Allocated Quantity:"));
        formGrid.add(quantityField);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        buttonPanel.setOpaque(false);

        JButton saveBtn = UITheme.createPrimaryButton("💾 Save / Update Stock", UITheme.ACCENT_BLUE, UITheme.ACCENT_HOVER);
        JButton deleteBtn = UITheme.createPrimaryButton("🗑 Remove Item", UITheme.DANGER_RED, UITheme.DANGER_HOVER);
        JButton clearBtn = UITheme.createPrimaryButton("🔄 Clear Form", new Color(100, 116, 139), new Color(71, 85, 105));
        JButton refreshBtn = UITheme.createPrimaryButton("⟳ Reload", UITheme.PRIMARY_DARK, UITheme.PRIMARY_NAVY);

        saveBtn.addActionListener(e -> saveStock());
        deleteBtn.addActionListener(e -> deleteStock());
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
        JPanel tableCard = UITheme.createCard("Shelter Inventory Stock Levels", UITheme.PRIMARY_DARK);

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

        searchBar.add(new JLabel("Search Inventory: "), BorderLayout.WEST);
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchBtnBox, BorderLayout.EAST);

        // Table
        String[] columns = {"Shelter ID", "Shelter Name", "Resource ID", "Resource Item", "Quantity", "Unit"};
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
                    int sId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    int rId = Integer.parseInt(tableModel.getValueAt(row, 2).toString());
                    String qty = tableModel.getValueAt(row, 4).toString();

                    selectedShelterId = sId;
                    selectedResourceId = rId;

                    selectShelterInCombo(sId);
                    selectResourceInCombo(rId);
                    quantityField.setText(qty);
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
        shelterCombo.removeAllItems();
        resourceCombo.removeAllItems();

        try {
            List<ComboItem> shelters = ShelterDAO.getComboItems();
            for (ComboItem item : shelters) shelterCombo.addItem(item);

            List<ComboItem> resources = ResourceDAO.getComboItems();
            for (ComboItem item : resources) resourceCombo.addItem(item);
        } catch (Exception e) {
            System.err.println("Error loading inventory combos: " + e.getMessage());
        }
    }

    private void selectShelterInCombo(int shelterId) {
        for (int i = 0; i < shelterCombo.getItemCount(); i++) {
            ComboItem item = shelterCombo.getItemAt(i);
            if (item != null && item.getId() == shelterId) {
                shelterCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void selectResourceInCombo(int resourceId) {
        for (int i = 0; i < resourceCombo.getItemCount(); i++) {
            ComboItem item = resourceCombo.getItemAt(i);
            if (item != null && item.getId() == resourceId) {
                resourceCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<ShelterInventory> list = ShelterInventoryDAO.getAll();
            for (ShelterInventory si : list) {
                tableModel.addRow(new Object[]{
                        si.getShelterId(),
                        si.getShelterName(),
                        si.getResourceId(),
                        si.getResourceName(),
                        si.getQuantity(),
                        si.getUnit()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading inventory: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchRecords() {
        String kw = searchField.getText().trim();
        if (kw.isEmpty()) { loadData(); return; }

        tableModel.setRowCount(0);
        try {
            List<ShelterInventory> list = ShelterInventoryDAO.search(kw);
            for (ShelterInventory si : list) {
                tableModel.addRow(new Object[]{
                        si.getShelterId(),
                        si.getShelterName(),
                        si.getResourceId(),
                        si.getResourceName(),
                        si.getQuantity(),
                        si.getUnit()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching inventory: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveStock() {
        ComboItem selectedShelter = (ComboItem) shelterCombo.getSelectedItem();
        ComboItem selectedResource = (ComboItem) resourceCombo.getSelectedItem();
        String qtyStr = quantityField.getText().trim();

        if (selectedShelter == null || selectedResource == null || qtyStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a Shelter, a Resource, and enter Quantity.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int quantity = Integer.parseInt(qtyStr);
            if (quantity < 0) {
                JOptionPane.showMessageDialog(this, "Quantity cannot be negative.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (ShelterInventoryDAO.saveOrUpdate(selectedShelter.getId(), selectedResource.getId(), quantity)) {
                JOptionPane.showMessageDialog(this, "Inventory stock saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity must be a valid non-negative integer.", "Invalid Number", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving inventory: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStock() {
        if (selectedShelterId == null || selectedResourceId == null) {
            JOptionPane.showMessageDialog(this, "Please select an inventory record from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this resource stock allocation from Shelter ID " + selectedShelterId + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (ShelterInventoryDAO.delete(selectedShelterId, selectedResourceId)) {
                    JOptionPane.showMessageDialog(this, "Inventory record removed successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadData();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting inventory: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        quantityField.setText("");
        selectedShelterId = null;
        selectedResourceId = null;
        if (shelterCombo.getItemCount() > 0) shelterCombo.setSelectedIndex(0);
        if (resourceCombo.getItemCount() > 0) resourceCombo.setSelectedIndex(0);
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ShelterInventoryUI().setVisible(true));
    }
}