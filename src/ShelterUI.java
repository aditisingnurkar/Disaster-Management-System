import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ShelterUI extends JFrame {
    private JTextField addIdField, addAddressField, addCapacityField, addCoordinatorField, addDisasterField;
    private JTextField updateIdField, updateAddressField, updateCapacityField, updateCoordinatorField;
    private JTextField deleteIdField;
    private JTextArea outputArea;
    private Connection con;

    // Modern color scheme
    private final Color PRIMARY_BLUE = new Color(30, 58, 138);
    private final Color LIGHT_BG = new Color(249, 250, 251);
    private final Color CARD_BG = Color.WHITE;
    private final Color ORANGE = new Color(249, 115, 22);
    private final Color RED = new Color(239, 68, 68);
    private final Color BLUE_BTN = new Color(37, 99, 235);
    private final Color BORDER_COLOR = new Color(229, 231, 235);

    public ShelterUI() {
        setTitle("Shelter Management System");
        setSize(1400, 800);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            con = DatabaseConnection.getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed!");
            e.printStackTrace();
            return;
        }

        // Main container with light background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);

        // Header
        JPanel header = createHeader();
        mainPanel.add(header, BorderLayout.NORTH);

        // Content area
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(LIGHT_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top section with three cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setBackground(LIGHT_BG);
        cardsPanel.setPreferredSize(new Dimension(1400, 320));

        cardsPanel.add(createAddCard());
        cardsPanel.add(createUpdateCard());
        cardsPanel.add(createDeleteCard());

        contentPanel.add(cardsPanel, BorderLayout.NORTH);

        // Bottom section - Display area
        JPanel displayPanel = createDisplayPanel();
        contentPanel.add(displayPanel, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_BLUE);
        header.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setBackground(PRIMARY_BLUE);

        JLabel titleLabel = new JLabel("🏠 Shelter Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Manage and track shelter facilities efficiently");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(191, 219, 254));

        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        header.add(textPanel, BorderLayout.WEST);

        return header;
    }

    private JPanel createAddCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(59, 130, 246), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Add New Shelter Record");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(59, 130, 246));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 12));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        addIdField = new JTextField();
        addAddressField = new JTextField();
        addCapacityField = new JTextField();
        addCoordinatorField = new JTextField();
        addDisasterField = new JTextField();

        addFormField(formPanel, "Shelter ID:", addIdField);
        addFormField(formPanel, "Address:", addAddressField);
        addFormField(formPanel, "Capacity:", addCapacityField);
        addFormField(formPanel, "Coordinator:", addCoordinatorField);
        addFormField(formPanel, "Disaster ID:", addDisasterField);

        // Wrap form panel in scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(CARD_BG);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton addBtn = createStyledButton("➕ Add Record", BLUE_BTN);
        addBtn.addActionListener(e -> insertShelter());

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        card.add(addBtn, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createUpdateCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ORANGE, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Update Record");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ORANGE);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 12));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        updateIdField = new JTextField();
        updateAddressField = new JTextField();
        updateCapacityField = new JTextField();
        updateCoordinatorField = new JTextField();

        addFormField(formPanel, "Shelter ID:", updateIdField);
        addFormField(formPanel, "New Address:", updateAddressField);
        addFormField(formPanel, "New Capacity:", updateCapacityField);
        addFormField(formPanel, "New Coordinator:", updateCoordinatorField);

        // Wrap form in scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(CARD_BG);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton updateBtn = createStyledButton("🔄 Update Shelter", ORANGE);
        updateBtn.addActionListener(e -> updateShelter());
        updateBtn.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));

        JPanel btnWrapper = new JPanel(new BorderLayout());
        btnWrapper.setBackground(CARD_BG);
        btnWrapper.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        btnWrapper.add(updateBtn, BorderLayout.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        card.add(btnWrapper, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createDeleteCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(RED, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Delete Record");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(RED);

        JPanel formPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));

        deleteIdField = new JTextField();
        addFormField(formPanel, "Shelter ID:", deleteIdField);

        // Wrap form in scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(CARD_BG);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton deleteBtn = createStyledButton("🗑️ Delete Record", RED);
        deleteBtn.addActionListener(e -> deleteShelter());
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));

        JPanel btnWrapper = new JPanel(new BorderLayout());
        btnWrapper.setBackground(CARD_BG);
        btnWrapper.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        btnWrapper.add(deleteBtn, BorderLayout.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        card.add(btnWrapper, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BG);

        JLabel titleLabel = new JLabel("Shelter Records");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 58, 138));

        JButton refreshBtn = createStyledButton("🔄 Refresh Records", BLUE_BTN);
        refreshBtn.addActionListener(e -> displayShelters());
        refreshBtn.setPreferredSize(new Dimension(200, 40));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setBackground(new Color(249, 250, 251));
        outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void addFormField(JPanel panel, String label, JTextField field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(55, 65, 81));

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(Color.BLACK);
        field.setBackground(Color.WHITE);
        field.setCaretColor(Color.BLACK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        panel.add(lbl);
        panel.add(field);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    private void insertShelter() {
        try {
            PreparedStatement pst = con.prepareStatement("INSERT INTO Shelter VALUES (?, ?, ?, ?, ?)");
            pst.setInt(1, Integer.parseInt(addIdField.getText()));
            pst.setString(2, addAddressField.getText());
            pst.setInt(3, Integer.parseInt(addCapacityField.getText()));
            pst.setString(4, addCoordinatorField.getText());
            pst.setInt(5, Integer.parseInt(addDisasterField.getText()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Shelter inserted successfully!");
            clearAddFields();
            displayShelters();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error: " + ex.getMessage());
        }
    }

    private void displayShelters() {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Shelter");
            outputArea.setText(String.format("%-12s %-30s %-12s %-25s %-12s\n",
                    "Shelter_ID", "Address", "Capacity", "Coordinator_Name", "Disaster_ID"));
            outputArea.append("─".repeat(100) + "\n");
            while (rs.next()) {
                outputArea.append(String.format("%-12d %-30s %-12d %-25s %-12d\n",
                        rs.getInt(1), rs.getString(2), rs.getInt(3),
                        rs.getString(4), rs.getInt(5)));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error displaying shelters!");
        }
    }

    private void updateShelter() {
        String id = updateIdField.getText();
        String address = updateAddressField.getText();
        String capacity = updateCapacityField.getText();
        String coordinator = updateCoordinatorField.getText();

        if (id.isEmpty() || address.isEmpty() || capacity.isEmpty() || coordinator.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please fill all fields!");
            return;
        }

        try {
            PreparedStatement pst = con.prepareStatement(
                    "UPDATE Shelter SET address = ?, capacity = ?, coordinator_name = ? WHERE shelter_id = ?");
            pst.setString(1, address);
            pst.setInt(2, Integer.parseInt(capacity));
            pst.setString(3, coordinator);
            pst.setInt(4, Integer.parseInt(id));
            int rows = pst.executeUpdate();
            JOptionPane.showMessageDialog(this, rows > 0 ? "✅ Shelter updated!" : "❌ Shelter not found!");
            if (rows > 0) {
                clearUpdateFields();
                displayShelters();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error updating shelter: " + ex.getMessage());
        }
    }

    private void deleteShelter() {
        String id = deleteIdField.getText();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter Shelter ID!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Shelter ID: " + id + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            PreparedStatement pst = con.prepareStatement("DELETE FROM Shelter WHERE shelter_id = ?");
            pst.setInt(1, Integer.parseInt(id));
            int rows = pst.executeUpdate();
            JOptionPane.showMessageDialog(this, rows > 0 ? "🗑️ Deleted successfully!" : "❌ Shelter not found!");
            if (rows > 0) {
                deleteIdField.setText("");
                displayShelters();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error deleting shelter: " + ex.getMessage());
        }
    }

    private void clearAddFields() {
        addIdField.setText("");
        addAddressField.setText("");
        addCapacityField.setText("");
        addCoordinatorField.setText("");
        addDisasterField.setText("");
    }

    private void clearUpdateFields() {
        updateIdField.setText("");
        updateAddressField.setText("");
        updateCapacityField.setText("");
        updateCoordinatorField.setText("");
    }
}
