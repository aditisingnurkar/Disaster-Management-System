import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ResourceUI extends JFrame {

    private JTextField idField, nameField, descField, unitField, searchField;
    private JTable table;
    private DefaultTableModel tableModel;

    public ResourceUI() {
        setTitle("Resource Catalog - Relief Supply Master");
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.PRIMARY_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel titleLabel = new JLabel("📦 Relief Resources Master Catalog");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Define standard emergency relief items, descriptions, and packaging units");
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
        JPanel formCard = UITheme.createCard("Resource Item Details", UITheme.PRIMARY_DARK);
        formCard.setPreferredSize(new Dimension(380, 0));

        JPanel formGrid = new JPanel(new GridLayout(4, 2, 8, 12));
        formGrid.setOpaque(false);

        idField = UITheme.createTextField();
        idField.setEditable(false);
        idField.setBackground(new Color(241, 245, 249));

        nameField = UITheme.createTextField();
        descField = UITheme.createTextField();
        unitField = UITheme.createTextField();

        formGrid.add(new JLabel("Resource ID:"));
        formGrid.add(idField);
        formGrid.add(new JLabel("Resource Name:"));
        formGrid.add(nameField);
        formGrid.add(new JLabel("Description:"));
        formGrid.add(descField);
        formGrid.add(new JLabel("Unit (e.g. Bottles/Kits):"));
        formGrid.add(unitField);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        buttonPanel.setOpaque(false);

        JButton addBtn = UITheme.createPrimaryButton("➕ Add Resource", UITheme.ACCENT_BLUE, UITheme.ACCENT_HOVER);
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
        JPanel tableCard = UITheme.createCard("Cataloged Relief Resources", UITheme.PRIMARY_DARK);

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

        searchBar.add(new JLabel("Search Resources: "), BorderLayout.WEST);
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchBtnBox, BorderLayout.EAST);

        // Table
        String[] columns = {"ID", "Resource Name", "Description", "Unit"};
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
                    Object descVal = tableModel.getValueAt(row, 2);
                    descField.setText(descVal != null ? descVal.toString() : "");
                    unitField.setText(tableModel.getValueAt(row, 3).toString());
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
            List<Resource> list = ResourceDAO.getAll();
            for (Resource r : list) {
                tableModel.addRow(new Object[]{
                        r.getResourceId(),
                        r.getResourceName(),
                        r.getDescription() != null ? r.getDescription() : "-",
                        r.getUnit()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading resources: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchRecords() {
        String kw = searchField.getText().trim();
        if (kw.isEmpty()) { loadData(); return; }

        tableModel.setRowCount(0);
        try {
            List<Resource> list = ResourceDAO.search(kw);
            for (Resource r : list) {
                tableModel.addRow(new Object[]{
                        r.getResourceId(),
                        r.getResourceName(),
                        r.getDescription() != null ? r.getDescription() : "-",
                        r.getUnit()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRecord() {
        try {
            String name = nameField.getText().trim();
            String desc = descField.getText().trim();
            String unit = unitField.getText().trim();

            if (name.isEmpty() || unit.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in Resource Name and Unit.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Resource r = new Resource(name, desc.isEmpty() ? null : desc, unit);
            if (ResourceDAO.insert(r)) {
                JOptionPane.showMessageDialog(this, "Resource item registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding resource: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRecord() {
        String idStr = idField.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a resource from the table to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            String name = nameField.getText().trim();
            String desc = descField.getText().trim();
            String unit = unitField.getText().trim();

            if (name.isEmpty() || unit.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in Resource Name and Unit.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Resource r = new Resource(id, name, desc.isEmpty() ? null : desc, unit);
            if (ResourceDAO.update(r)) {
                JOptionPane.showMessageDialog(this, "Resource record updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating resource: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        String idStr = idField.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a resource from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Resource ID " + idStr + "?\nAssociated shelter inventory records will also be removed.",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idStr);
                if (ResourceDAO.delete(id)) {
                    JOptionPane.showMessageDialog(this, "Resource deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadData();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting resource: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        descField.setText("");
        unitField.setText("");
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ResourceUI().setVisible(true));
    }
}