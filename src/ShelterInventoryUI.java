import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ShelterInventoryUI extends JFrame {
    private JTextField addShelterIdField, addResourceIdField, addQuantityField;
    private JTextField updateShelterIdField, updateResourceIdField, updateOldResourceIdField, updateNewResourceIdField;
    private JTextField updateOldShelterIdField, updateNewShelterIdField, updateQuantityField;
    private JTextField deleteShelterIdField, deleteResourceIdField;
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

    public ShelterInventoryUI() {
        setTitle("Shelter Inventory Management System");
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
        cardsPanel.setPreferredSize(new Dimension(1400, 380));

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

        JLabel titleLabel = new JLabel("📦 Shelter Inventory Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Manage and track shelter inventory resources efficiently");
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

        JLabel titleLabel = new JLabel("Add New Inventory");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(59, 130, 246));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 12));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        addShelterIdField = new JTextField();
        addResourceIdField = new JTextField();
        addQuantityField = new JTextField();

        addFormField(formPanel, "Shelter ID:", addShelterIdField);
        addFormField(formPanel, "Resource ID:", addResourceIdField);
        addFormField(formPanel, "Quantity:", addQuantityField);

        // Wrap form panel in scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(CARD_BG);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton addBtn = createStyledButton("➕ Add Inventory", BLUE_BTN);
        addBtn.addActionListener(e -> insertInventory());

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

        JLabel titleLabel = new JLabel("Update Inventory");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ORANGE);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        updateShelterIdField = new JTextField();
        updateResourceIdField = new JTextField();
        updateQuantityField = new JTextField();
        updateOldResourceIdField = new JTextField();
        updateNewResourceIdField = new JTextField();
        updateOldShelterIdField = new JTextField();
        updateNewShelterIdField = new JTextField();

        // For Quantity Update
        addFormField(formPanel, "Shelter ID:", updateShelterIdField);
        addFormField(formPanel, "Resource ID:", updateResourceIdField);
        addFormField(formPanel, "New Quantity:", updateQuantityField);

        // Separator label
        JLabel separator1 = new JLabel("─────────────────");
        separator1.setForeground(BORDER_COLOR);
        separator1.setHorizontalAlignment(JLabel.CENTER);
        formPanel.add(separator1);
        formPanel.add(new JLabel());

        // For Resource ID Update
        addFormField(formPanel, "Old Resource ID:", updateOldResourceIdField);
        addFormField(formPanel, "New Resource ID:", updateNewResourceIdField);

        // For Shelter ID Update
        addFormField(formPanel, "Old Shelter ID:", updateOldShelterIdField);

        // Wrap form in scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(CARD_BG);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        buttonPanel.setBackground(CARD_BG);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton updateQuantityBtn = createStyledButton("🔄 Update Quantity", ORANGE);
        JButton updateResourceIdBtn = createStyledButton("🔄 Update Resource ID", ORANGE);
        JButton updateShelterIdBtn = createStyledButton("🔄 Update Shelter ID", ORANGE);

        updateQuantityBtn.addActionListener(e -> updateQuantity());
        updateResourceIdBtn.addActionListener(e -> updateResourceID());
        updateShelterIdBtn.addActionListener(e -> updateShelterID());

        buttonPanel.add(updateQuantityBtn);
        buttonPanel.add(updateResourceIdBtn);
        buttonPanel.add(updateShelterIdBtn);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createDeleteCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(RED, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Delete Inventory");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(RED);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 12));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));

        deleteShelterIdField = new JTextField();
        deleteResourceIdField = new JTextField();

        addFormField(formPanel, "Shelter ID:", deleteShelterIdField);
        addFormField(formPanel, "Resource ID:", deleteResourceIdField);

        // Wrap form in scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(CARD_BG);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton deleteBtn = createStyledButton("🗑️ Delete Inventory", RED);
        deleteBtn.addActionListener(e -> deleteInventory());
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

        JLabel titleLabel = new JLabel("Shelter Inventory Records");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 58, 138));

        JButton refreshBtn = createStyledButton("🔄 Refresh Records", BLUE_BTN);
        refreshBtn.addActionListener(e -> displayInventory());
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

    private void insertInventory() {
        try {
            PreparedStatement pst = con.prepareStatement("INSERT INTO Shelter_Inventory VALUES (?, ?, ?)");
            pst.setInt(1, Integer.parseInt(addShelterIdField.getText()));
            pst.setInt(2, Integer.parseInt(addResourceIdField.getText()));
            pst.setInt(3, Integer.parseInt(addQuantityField.getText()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Inventory inserted successfully!");
            clearAddFields();
            displayInventory();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error: " + ex.getMessage());
        }
    }

    private void displayInventory() {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Shelter_Inventory");
            outputArea.setText(String.format("%-15s %-15s %-15s\n",
                    "Shelter_ID", "Resource_ID", "Quantity"));
            outputArea.append("─".repeat(50) + "\n");
            while (rs.next()) {
                outputArea.append(String.format("%-15d %-15d %-15d\n",
                        rs.getInt(1), rs.getInt(2), rs.getInt(3)));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error displaying inventory!");
        }
    }

    private void updateQuantity() {
        String shelterId = updateShelterIdField.getText();
        String resourceId = updateResourceIdField.getText();
        String quantity = updateQuantityField.getText();

        if (shelterId.isEmpty() || resourceId.isEmpty() || quantity.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please fill Shelter ID, Resource ID, and New Quantity!");
            return;
        }

        try {
            PreparedStatement pst = con.prepareStatement(
                    "UPDATE Shelter_Inventory SET Quantity = ? WHERE Shelter_ID = ? AND Resource_ID = ?");
            pst.setInt(1, Integer.parseInt(quantity));
            pst.setInt(2, Integer.parseInt(shelterId));
            pst.setInt(3, Integer.parseInt(resourceId));
            int rows = pst.executeUpdate();
            JOptionPane.showMessageDialog(this, rows > 0 ? "✅ Quantity updated!" : "❌ Inventory record not found!");
            if (rows > 0) {
                clearUpdateFields();
                displayInventory();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error updating quantity: " + ex.getMessage());
        }
    }

    private void updateResourceID() {
        String shelterId = updateShelterIdField.getText();
        String oldResourceId = updateOldResourceIdField.getText();
        String newResourceId = updateNewResourceIdField.getText();

        if (shelterId.isEmpty() || oldResourceId.isEmpty() || newResourceId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please fill Shelter ID, Old Resource ID, and New Resource ID!");
            return;
        }

        try {
            PreparedStatement pst = con.prepareStatement(
                    "UPDATE Shelter_Inventory SET Resource_ID = ? WHERE Shelter_ID = ? AND Resource_ID = ?");
            pst.setInt(1, Integer.parseInt(newResourceId));
            pst.setInt(2, Integer.parseInt(shelterId));
            pst.setInt(3, Integer.parseInt(oldResourceId));
            int rows = pst.executeUpdate();
            JOptionPane.showMessageDialog(this, rows > 0 ? "✅ Resource ID updated!" : "❌ Inventory record not found!");
            if (rows > 0) {
                clearUpdateFields();
                displayInventory();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error updating Resource ID: " + ex.getMessage());
        }
    }

    private void updateShelterID() {
        String resourceId = updateResourceIdField.getText();
        String oldShelterId = updateOldShelterIdField.getText();
        String newShelterId = updateNewShelterIdField.getText();

        if (resourceId.isEmpty() || oldShelterId.isEmpty() || newShelterId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please fill Resource ID, Old Shelter ID, and New Shelter ID (from bottom field)!");
            return;
        }

        try {
            PreparedStatement pst = con.prepareStatement(
                    "UPDATE Shelter_Inventory SET Shelter_ID = ? WHERE Shelter_ID = ? AND Resource_ID = ?");
            pst.setInt(1, Integer.parseInt(newShelterId));
            pst.setInt(2, Integer.parseInt(oldShelterId));
            pst.setInt(3, Integer.parseInt(resourceId));
            int rows = pst.executeUpdate();
            JOptionPane.showMessageDialog(this, rows > 0 ? "✅ Shelter ID updated!" : "❌ Inventory record not found!");
            if (rows > 0) {
                clearUpdateFields();
                displayInventory();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error updating Shelter ID: " + ex.getMessage());
        }
    }

    private void deleteInventory() {
        String shelterId = deleteShelterIdField.getText();
        String resourceId = deleteResourceIdField.getText();

        if (shelterId.isEmpty() || resourceId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter both Shelter ID and Resource ID!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this inventory?\nShelter ID: " + shelterId + ", Resource ID: " + resourceId,
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            PreparedStatement pst = con.prepareStatement("DELETE FROM Shelter_Inventory WHERE Shelter_ID = ? AND Resource_ID = ?");
            pst.setInt(1, Integer.parseInt(shelterId));
            pst.setInt(2, Integer.parseInt(resourceId));
            int rows = pst.executeUpdate();
            JOptionPane.showMessageDialog(this, rows > 0 ? "🗑️ Deleted successfully!" : "❌ Inventory record not found!");
            if (rows > 0) {
                clearDeleteFields();
                displayInventory();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error deleting inventory: " + ex.getMessage());
        }
    }

    private void clearAddFields() {
        addShelterIdField.setText("");
        addResourceIdField.setText("");
        addQuantityField.setText("");
    }

    private void clearUpdateFields() {
        updateShelterIdField.setText("");
        updateResourceIdField.setText("");
        updateQuantityField.setText("");
        updateOldResourceIdField.setText("");
        updateNewResourceIdField.setText("");
        updateOldShelterIdField.setText("");
        updateNewShelterIdField.setText("");
    }

    private void clearDeleteFields() {
        deleteShelterIdField.setText("");
        deleteResourceIdField.setText("");
    }
}