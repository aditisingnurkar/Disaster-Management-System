import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class VolunteerUI extends JFrame {

    private JTextField idField, nameField, ageField, contactField, skillsField, searchField;
    private JComboBox<String> genderCombo;
    private JComboBox<String> statusCombo;
    private JTable table;
    private DefaultTableModel tableModel;

    public VolunteerUI() {
        setTitle("Volunteer Registry - Personnel & Skill Management");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.PRIMARY_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel titleLabel = new JLabel("🤝 Volunteer Personnel Registry");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Register volunteers, manage response skills, contact channels, and duty availability");
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
        JPanel formCard = UITheme.createCard("Volunteer Profile", UITheme.PRIMARY_DARK);
        formCard.setPreferredSize(new Dimension(400, 0));

        JPanel formGrid = new JPanel(new GridLayout(7, 2, 8, 12));
        formGrid.setOpaque(false);

        idField = UITheme.createTextField();
        idField.setEditable(false);
        idField.setBackground(new Color(241, 245, 249));

        nameField = UITheme.createTextField();
        ageField = UITheme.createTextField();
        contactField = UITheme.createTextField();
        skillsField = UITheme.createTextField();

        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderCombo.setFont(UITheme.FONT_BODY);
        genderCombo.setBackground(Color.WHITE);

        statusCombo = new JComboBox<>(new String[]{"Available", "Assigned", "On Leave"});
        statusCombo.setFont(UITheme.FONT_BODY);
        statusCombo.setBackground(Color.WHITE);

        formGrid.add(new JLabel("Volunteer ID:"));
        formGrid.add(idField);
        formGrid.add(new JLabel("Full Name:"));
        formGrid.add(nameField);
        formGrid.add(new JLabel("Age (Min 18):"));
        formGrid.add(ageField);
        formGrid.add(new JLabel("Gender:"));
        formGrid.add(genderCombo);
        formGrid.add(new JLabel("Contact Number:"));
        formGrid.add(contactField);
        formGrid.add(new JLabel("Skills / Expertise:"));
        formGrid.add(skillsField);
        formGrid.add(new JLabel("Availability Status:"));
        formGrid.add(statusCombo);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        buttonPanel.setOpaque(false);

        JButton addBtn = UITheme.createPrimaryButton("➕ Add Volunteer", UITheme.ACCENT_BLUE, UITheme.ACCENT_HOVER);
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
        JPanel tableCard = UITheme.createCard("Registered Volunteers Directory", UITheme.PRIMARY_DARK);

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

        searchBar.add(new JLabel("Search Volunteers: "), BorderLayout.WEST);
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchBtnBox, BorderLayout.EAST);

        // Table
        String[] columns = {"ID", "Name", "Age", "Gender", "Contact", "Skills", "Status"};
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
                    ageField.setText(tableModel.getValueAt(row, 2).toString());
                    genderCombo.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                    contactField.setText(tableModel.getValueAt(row, 4).toString());
                    skillsField.setText(tableModel.getValueAt(row, 5).toString());
                    statusCombo.setSelectedItem(tableModel.getValueAt(row, 6).toString());
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
            List<Volunteer> list = VolunteerDAO.getAll();
            for (Volunteer v : list) {
                tableModel.addRow(new Object[]{
                        v.getVolunteerId(),
                        v.getName(),
                        v.getAge(),
                        v.getGender(),
                        v.getContactNumber(),
                        v.getSkills(),
                        v.getAvailabilityStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading volunteers: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchRecords() {
        String kw = searchField.getText().trim();
        if (kw.isEmpty()) { loadData(); return; }

        tableModel.setRowCount(0);
        try {
            List<Volunteer> list = VolunteerDAO.search(kw);
            for (Volunteer v : list) {
                tableModel.addRow(new Object[]{
                        v.getVolunteerId(),
                        v.getName(),
                        v.getAge(),
                        v.getGender(),
                        v.getContactNumber(),
                        v.getSkills(),
                        v.getAvailabilityStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRecord() {
        try {
            String name = nameField.getText().trim();
            String ageStr = ageField.getText().trim();
            String gender = (String) genderCombo.getSelectedItem();
            String contact = contactField.getText().trim();
            String skills = skillsField.getText().trim();
            String status = (String) statusCombo.getSelectedItem();

            if (name.isEmpty() || ageStr.isEmpty() || contact.isEmpty() || skills.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in Name, Age, Contact, and Skills.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int age = Integer.parseInt(ageStr);
            if (age < 18 || age > 100) {
                JOptionPane.showMessageDialog(this, "Volunteer age must be 18 or above (Check Constraint).", "Age Restriction", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Volunteer v = new Volunteer(name, age, gender, contact, skills, status);
            if (VolunteerDAO.insert(v)) {
                JOptionPane.showMessageDialog(this, "Volunteer registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a valid number.", "Invalid Number", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding volunteer: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRecord() {
        String idStr = idField.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a volunteer from the table to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            String name = nameField.getText().trim();
            String ageStr = ageField.getText().trim();
            String gender = (String) genderCombo.getSelectedItem();
            String contact = contactField.getText().trim();
            String skills = skillsField.getText().trim();
            String status = (String) statusCombo.getSelectedItem();

            if (name.isEmpty() || ageStr.isEmpty() || contact.isEmpty() || skills.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int age = Integer.parseInt(ageStr);
            if (age < 18 || age > 100) {
                JOptionPane.showMessageDialog(this, "Volunteer age must be 18 or above.", "Age Restriction", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Volunteer v = new Volunteer(id, name, age, gender, contact, skills, status);
            if (VolunteerDAO.update(v)) {
                JOptionPane.showMessageDialog(this, "Volunteer updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating volunteer: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        String idStr = idField.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a volunteer from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Volunteer ID " + idStr + "?\nAssociated assignments and reports will also be removed.",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idStr);
                if (VolunteerDAO.delete(id)) {
                    JOptionPane.showMessageDialog(this, "Volunteer deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadData();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting volunteer: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        genderCombo.setSelectedIndex(0);
        contactField.setText("");
        skillsField.setText("");
        statusCombo.setSelectedIndex(0);
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VolunteerUI().setVisible(true));
    }
}