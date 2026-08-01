import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class VolunteerAssignmentsUI extends JFrame {
    private JTextField addVolunteerIdField, addDisasterIdField, addRoleField, addShiftField;
    private JTextField updateVolunteerIdField, updateDisasterIdField, updateRoleField, updateShiftField;
    private JTextField deleteVolunteerIdField, deleteDisasterIdField;
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

    public VolunteerAssignmentsUI() {
        setTitle("Volunteer Assignments Management System");
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

        JLabel titleLabel = new JLabel("📋 Volunteer Assignments Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Assign volunteers to disaster relief operations and manage shift schedules");
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

        JLabel titleLabel = new JLabel("Add New Assignment");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(59, 130, 246));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 12));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        addVolunteerIdField = new JTextField();
        addDisasterIdField = new JTextField();
        addRoleField = new JTextField();
        addShiftField = new JTextField();

        addFormField(formPanel, "Volunteer ID:", addVolunteerIdField);
        addFormField(formPanel, "Disaster ID:", addDisasterIdField);
        addFormField(formPanel, "Role:", addRoleField);
        addFormField(formPanel, "Shift Timing:", addShiftField);

        // Wrap form panel in scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(CARD_BG);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton addBtn = createStyledButton("➕ Add Assignment", BLUE_BTN);
        addBtn.addActionListener(e -> insertAssignment());

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

        JLabel titleLabel = new JLabel("Update Assignment");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ORANGE);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 12));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        updateVolunteerIdField = new JTextField();
        updateDisasterIdField = new JTextField();
        updateRoleField = new JTextField();
        updateShiftField = new JTextField();

        addFormField(formPanel, "Volunteer ID:", updateVolunteerIdField);
        addFormField(formPanel, "Disaster ID:", updateDisasterIdField);
        addFormField(formPanel, "New Role:", updateRoleField);
        addFormField(formPanel, "New Shift:", updateShiftField);

        // Wrap form in scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(CARD_BG);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton updateBtn = createStyledButton("🔄 Update Assignment", ORANGE);
        updateBtn.addActionListener(e -> updateAssignment());

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        card.add(updateBtn, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createDeleteCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(RED, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Delete Assignment");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(RED);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 12));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));

        deleteVolunteerIdField = new JTextField();
        deleteDisasterIdField = new JTextField();

        addFormField(formPanel, "Volunteer ID:", deleteVolunteerIdField);
        addFormField(formPanel, "Disaster ID:", deleteDisasterIdField);

        // Wrap form in scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(CARD_BG);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton deleteBtn = createStyledButton("🗑️ Delete Assignment", RED);
        deleteBtn.addActionListener(e -> deleteAssignment());
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

        JLabel titleLabel = new JLabel("Volunteer Assignments");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 58, 138));

        JButton refreshBtn = createStyledButton("🔄 Refresh Records", BLUE_BTN);
        refreshBtn.addActionListener(e -> displayAssignments());
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

    private void insertAssignment() {
        try {
            if (addVolunteerIdField.getText().isEmpty() || addDisasterIdField.getText().isEmpty() ||
                    addRoleField.getText().isEmpty() || addShiftField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Please fill all fields!");
                return;
            }

            PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO Volunteer_Assignments VALUES (?, ?, ?, ?)");
            pst.setInt(1, Integer.parseInt(addVolunteerIdField.getText()));
            pst.setInt(2, Integer.parseInt(addDisasterIdField.getText()));
            pst.setString(3, addRoleField.getText());
            pst.setString(4, addShiftField.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Assignment inserted successfully!");
            clearAddFields();
            displayAssignments();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter valid numbers for IDs!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error: " + ex.getMessage());
        }
    }

    private void displayAssignments() {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Volunteer_Assignments");
            outputArea.setText(String.format("%-15s %-15s %-25s %-25s\n",
                    "Volunteer_ID", "Disaster_ID", "Role", "Shift_Timing"));
            outputArea.append("─".repeat(85) + "\n");
            while (rs.next()) {
                outputArea.append(String.format("%-15d %-15d %-25s %-25s\n",
                        rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4)));
            }
            rs.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error displaying assignments!");
            ex.printStackTrace();
        }
    }

    private void updateAssignment() {
        try {
            if (updateVolunteerIdField.getText().isEmpty() || updateDisasterIdField.getText().isEmpty() ||
                    updateRoleField.getText().isEmpty() || updateShiftField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Please fill all fields!");
                return;
            }

            PreparedStatement pst = con.prepareStatement(
                    "UPDATE Volunteer_Assignments SET role = ?, shift_timing = ? WHERE volunteer_id = ? AND disaster_id = ?");
            pst.setString(1, updateRoleField.getText());
            pst.setString(2, updateShiftField.getText());
            pst.setInt(3, Integer.parseInt(updateVolunteerIdField.getText()));
            pst.setInt(4, Integer.parseInt(updateDisasterIdField.getText()));
            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "✅ Assignment updated successfully!");
                clearUpdateFields();
                displayAssignments();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Assignment not found!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter valid numbers for IDs!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error updating assignment: " + ex.getMessage());
        }
    }

    private void deleteAssignment() {
        try {
            if (deleteVolunteerIdField.getText().isEmpty() || deleteDisasterIdField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Please enter both IDs!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this assignment?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) return;

            PreparedStatement pst = con.prepareStatement(
                    "DELETE FROM Volunteer_Assignments WHERE volunteer_id = ? AND disaster_id = ?");
            pst.setInt(1, Integer.parseInt(deleteVolunteerIdField.getText()));
            pst.setInt(2, Integer.parseInt(deleteDisasterIdField.getText()));
            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "🗑️ Deleted successfully!");
                clearDeleteFields();
                displayAssignments();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Assignment not found!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter valid numbers for IDs!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error deleting assignment: " + ex.getMessage());
        }
    }

    private void clearAddFields() {
        addVolunteerIdField.setText("");
        addDisasterIdField.setText("");
        addRoleField.setText("");
        addShiftField.setText("");
    }

    private void clearUpdateFields() {
        updateVolunteerIdField.setText("");
        updateDisasterIdField.setText("");
        updateRoleField.setText("");
        updateShiftField.setText("");
    }

    private void clearDeleteFields() {
        deleteVolunteerIdField.setText("");
        deleteDisasterIdField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VolunteerAssignmentsUI ui = new VolunteerAssignmentsUI();
            ui.setVisible(true);
        });
    }
}