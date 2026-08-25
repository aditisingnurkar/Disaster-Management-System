import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class VictimUI extends JFrame {

    private JTextField idField, nameField, ageField, contactField, medicalField, searchField;
    private JComboBox<String> genderCombo;
    private JComboBox<ComboItem> disasterCombo;
    private JComboBox<ComboItem> shelterCombo;
    private JTable table;
    private DefaultTableModel tableModel;

    public VictimUI() {
        setTitle("Victim Management - Affected Persons Registry");
        setSize(1250, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.PRIMARY_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel titleLabel = new JLabel("🧍 Victim Records & Shelter Allocation");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Register affected individuals, track medical conditions, and assign emergency shelters");
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
        JPanel formCard = UITheme.createCard("Victim Record Details", UITheme.PRIMARY_DARK);
        formCard.setPreferredSize(new Dimension(420, 0));

        JPanel formGrid = new JPanel(new GridLayout(8, 2, 8, 10));
        formGrid.setOpaque(false);

        idField = UITheme.createTextField();
        idField.setEditable(false);
        idField.setBackground(new Color(241, 245, 249));

        nameField = UITheme.createTextField();
        ageField = UITheme.createTextField();
        contactField = UITheme.createTextField();
        medicalField = UITheme.createTextField();

        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderCombo.setFont(UITheme.FONT_BODY);
        genderCombo.setBackground(Color.WHITE);

        disasterCombo = UITheme.createComboBox();
        shelterCombo = UITheme.createComboBox();

        formGrid.add(new JLabel("Victim ID:"));
        formGrid.add(idField);
        formGrid.add(new JLabel("Full Name:"));
        formGrid.add(nameField);
        formGrid.add(new JLabel("Age:"));
        formGrid.add(ageField);
        formGrid.add(new JLabel("Gender:"));
        formGrid.add(genderCombo);
        formGrid.add(new JLabel("Contact Number:"));
        formGrid.add(contactField);
        formGrid.add(new JLabel("Medical Condition:"));
        formGrid.add(medicalField);
        formGrid.add(new JLabel("Disaster Event [FK]:"));
        formGrid.add(disasterCombo);
        formGrid.add(new JLabel("Assigned Shelter [FK]:"));
        formGrid.add(shelterCombo);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        buttonPanel.setOpaque(false);

        JButton addBtn = UITheme.createPrimaryButton("➕ Add Victim", UITheme.ACCENT_BLUE, UITheme.ACCENT_HOVER);
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
        JPanel tableCard = UITheme.createCard("Victim Directory", UITheme.PRIMARY_DARK);

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

        searchBar.add(new JLabel("Search Victims: "), BorderLayout.WEST);
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchBtnBox, BorderLayout.EAST);

        // Table
        String[] columns = {"ID", "Name", "Age", "Gender", "Contact", "Medical Condition", "Disaster ID", "Shelter ID"};
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
                    Object contactVal = tableModel.getValueAt(row, 4);
                    contactField.setText(contactVal != null ? contactVal.toString() : "");
                    medicalField.setText(tableModel.getValueAt(row, 5).toString());
                    int disId = Integer.parseInt(tableModel.getValueAt(row, 6).toString());
                    selectDisasterInCombo(disId);

                    Object shelterVal = tableModel.getValueAt(row, 7);
                    if (shelterVal != null && !shelterVal.toString().equals("None")) {
                        selectShelterInCombo(Integer.parseInt(shelterVal.toString()));
                    } else {
                        shelterCombo.setSelectedIndex(0); // None
                    }
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
        disasterCombo.removeAllItems();
        shelterCombo.removeAllItems();

        try {
            List<ComboItem> disasters = DisasterDAO.getComboItems();
            for (ComboItem item : disasters) {
                disasterCombo.addItem(item);
            }

            shelterCombo.addItem(new ComboItem(0, "0: None / Field Evacuation"));
            List<ComboItem> shelters = ShelterDAO.getComboItems();
            for (ComboItem item : shelters) {
                shelterCombo.addItem(item);
            }
        } catch (Exception e) {
            System.err.println("Error loading dropdown combos: " + e.getMessage());
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

    private void selectShelterInCombo(int shelterId) {
        for (int i = 0; i < shelterCombo.getItemCount(); i++) {
            ComboItem item = shelterCombo.getItemAt(i);
            if (item != null && item.getId() == shelterId) {
                shelterCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<Victim> list = VictimDAO.getAll();
            for (Victim v : list) {
                tableModel.addRow(new Object[]{
                        v.getVictimId(),
                        v.getName(),
                        v.getAge(),
                        v.getGender(),
                        v.getContactNumber() != null ? v.getContactNumber() : "-",
                        v.getMedicalCondition(),
                        v.getDisasterId(),
                        v.getShelterId() != null ? v.getShelterId() : "None"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading victims: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchRecords() {
        String kw = searchField.getText().trim();
        if (kw.isEmpty()) { loadData(); return; }

        tableModel.setRowCount(0);
        try {
            List<Victim> list = VictimDAO.search(kw);
            for (Victim v : list) {
                tableModel.addRow(new Object[]{
                        v.getVictimId(),
                        v.getName(),
                        v.getAge(),
                        v.getGender(),
                        v.getContactNumber() != null ? v.getContactNumber() : "-",
                        v.getMedicalCondition(),
                        v.getDisasterId(),
                        v.getShelterId() != null ? v.getShelterId() : "None"
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
            String medical = medicalField.getText().trim();
            ComboItem selectedDisaster = (ComboItem) disasterCombo.getSelectedItem();
            ComboItem selectedShelter = (ComboItem) shelterCombo.getSelectedItem();

            if (name.isEmpty() || ageStr.isEmpty() || selectedDisaster == null) {
                JOptionPane.showMessageDialog(this, "Please fill in Name, Age, and select a Disaster.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int age = Integer.parseInt(ageStr);
            if (age < 0 || age > 125) {
                JOptionPane.showMessageDialog(this, "Age must be between 0 and 125.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Integer shelterId = (selectedShelter != null && selectedShelter.getId() > 0) ? selectedShelter.getId() : null;
            if (medical.isEmpty()) medical = "Normal / None";

            Victim v = new Victim(name, age, gender, contact.isEmpty() ? null : contact, medical, selectedDisaster.getId(), shelterId);
            if (VictimDAO.insert(v)) {
                JOptionPane.showMessageDialog(this, "Victim record registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a valid number.", "Invalid Number", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding victim: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRecord() {
        String idStr = idField.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a victim from the table to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            String name = nameField.getText().trim();
            String ageStr = ageField.getText().trim();
            String gender = (String) genderCombo.getSelectedItem();
            String contact = contactField.getText().trim();
            String medical = medicalField.getText().trim();
            ComboItem selectedDisaster = (ComboItem) disasterCombo.getSelectedItem();
            ComboItem selectedShelter = (ComboItem) shelterCombo.getSelectedItem();

            if (name.isEmpty() || ageStr.isEmpty() || selectedDisaster == null) {
                JOptionPane.showMessageDialog(this, "Please fill in Name, Age, and select a Disaster.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int age = Integer.parseInt(ageStr);
            if (age < 0 || age > 125) {
                JOptionPane.showMessageDialog(this, "Age must be between 0 and 125.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Integer shelterId = (selectedShelter != null && selectedShelter.getId() > 0) ? selectedShelter.getId() : null;
            if (medical.isEmpty()) medical = "Normal / None";

            Victim v = new Victim(id, name, age, gender, contact.isEmpty() ? null : contact, medical, selectedDisaster.getId(), shelterId);
            if (VictimDAO.update(v)) {
                JOptionPane.showMessageDialog(this, "Victim record updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating victim: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        String idStr = idField.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a victim from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Victim ID " + idStr + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idStr);
                if (VictimDAO.delete(id)) {
                    JOptionPane.showMessageDialog(this, "Victim deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadData();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting victim: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        genderCombo.setSelectedIndex(0);
        contactField.setText("");
        medicalField.setText("");
        if (disasterCombo.getItemCount() > 0) disasterCombo.setSelectedIndex(0);
        if (shelterCombo.getItemCount() > 0) shelterCombo.setSelectedIndex(0);
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VictimUI().setVisible(true));
    }
}