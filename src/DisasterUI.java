import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DisasterUI extends JFrame {
    // Modern Color Palette
    private static final Color PRIMARY_COLOR = new Color(26, 35, 126); // Deep Blue
    private static final Color SECONDARY_COLOR = new Color(63, 81, 181); // Indigo
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80); // Green
    private static final Color WARNING_COLOR = new Color(255, 152, 0); // Orange
    private static final Color DANGER_COLOR = new Color(244, 67, 54); // Red
    private static final Color BG_COLOR = new Color(245, 245, 250); // Light Gray
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color TEXT_LIGHT = new Color(117, 117, 117);

    private JTextField idField, typeField, locationField, startField, endField, severityField;
    private JTextField updateIDField, deleteIDField;
    private JTextArea outputArea;
    private Disaster disaster;
    private Connection con;

    public DisasterUI() {
        setTitle("Disaster Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        getContentPane().setBackground(BG_COLOR);

        try {
            con = DatabaseConnection.getConnection();
            disaster = new Disaster();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();

        // Center Panel with all three sections side by side
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        centerPanel.setBackground(BG_COLOR);

        // Create scrollable panels for each section
        centerPanel.add(createScrollablePanel(createInputPanel()));
        centerPanel.add(createScrollablePanel(createUpdatePanel()));
        centerPanel.add(createScrollablePanel(createDeletePanel()));

        // Output Panel
        JPanel outputPanel = createOutputPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(outputPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JScrollPane createScrollablePanel(JPanel panel) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("🌍 Disaster Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Manage and track disaster events efficiently");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 200, 220));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setBackground(PRIMARY_COLOR);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        panel.add(textPanel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createInputPanel() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(CARD_COLOR);
        containerPanel.setBorder(createStyledBorder("Add New Disaster Record", SECONDARY_COLOR));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.weightx = 1.0;

        // Create styled input fields
        idField = createStyledTextField();
        typeField = createStyledTextField();
        locationField = createStyledTextField();
        startField = createStyledTextField();
        endField = createStyledTextField();
        severityField = createStyledTextField();

        // Add all fields
        addFormField(panel, "Disaster ID:", idField, gbc, 0);
        addFormField(panel, "Type:", typeField, gbc, 1);
        addFormField(panel, "Location:", locationField, gbc, 2);
        addFormField(panel, "Start Date:", startField, gbc, 3);

        // Add hint label for start date
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 5, 10, 5);
        JLabel hintLabel1 = new JLabel("YYYY-MM-DD");
        hintLabel1.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hintLabel1.setForeground(TEXT_LIGHT);
        panel.add(hintLabel1, gbc);

        gbc.insets = new Insets(10, 5, 10, 5);
        addFormField(panel, "End Date:", endField, gbc, 5);

        // Add hint label for end date
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 5, 10, 5);
        JLabel hintLabel2 = new JLabel("YYYY-MM-DD");
        hintLabel2.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hintLabel2.setForeground(TEXT_LIGHT);
        panel.add(hintLabel2, gbc);

        gbc.insets = new Insets(10, 5, 10, 5);
        addFormField(panel, "Severity:", severityField, gbc, 7);

        // Insert Button
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        JButton insertBtn = createStyledButton("➕ Insert Record", SUCCESS_COLOR);
        insertBtn.addActionListener(e -> insertRecord());
        panel.add(insertBtn, gbc);

        // Add glue to push content to top
        gbc.gridy = 9;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(Box.createVerticalGlue(), gbc);

        containerPanel.add(panel, BorderLayout.CENTER);
        return containerPanel;
    }

    private JPanel createUpdatePanel() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(CARD_COLOR);
        containerPanel.setBorder(createStyledBorder("Update Record", WARNING_COLOR));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.weightx = 1.0;

        updateIDField = createStyledTextField();
        addFormField(panel, "Disaster ID:", updateIDField, gbc, 0);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 10, 5);
        JButton updateBtn = createStyledButton("✏️ Update End Date", WARNING_COLOR);
        updateBtn.addActionListener(e -> updateEndDate());
        panel.add(updateBtn, gbc);

        // Add glue to push content to top
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(Box.createVerticalGlue(), gbc);

        containerPanel.add(panel, BorderLayout.CENTER);
        return containerPanel;
    }

    private JPanel createDeletePanel() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(CARD_COLOR);
        containerPanel.setBorder(createStyledBorder("Delete Record", DANGER_COLOR));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.weightx = 1.0;

        deleteIDField = createStyledTextField();
        addFormField(panel, "Disaster ID:", deleteIDField, gbc, 0);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 10, 5);
        JButton deleteBtn = createStyledButton("🗑️ Delete Record", DANGER_COLOR);
        deleteBtn.addActionListener(e -> deleteRecord());
        panel.add(deleteBtn, gbc);

        // Add glue to push content to top
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(Box.createVerticalGlue(), gbc);

        containerPanel.add(panel, BorderLayout.CENTER);
        return containerPanel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(createStyledBorder("Disaster Records", SECONDARY_COLOR));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JButton displayBtn = createStyledButton("🔄 Refresh Records", SECONDARY_COLOR);
        displayBtn.addActionListener(e -> displayRecords());
        buttonPanel.add(displayBtn);

        outputArea = new JTextArea(10, 70);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        outputArea.setBackground(new Color(250, 250, 255));
        outputArea.setForeground(TEXT_COLOR);
        outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void addFormField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.35;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jLabel.setForeground(TEXT_COLOR);
        panel.add(jLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        panel.add(field, gbc);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(200, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Add focus effects
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
                        BorderFactory.createEmptyBorder(4, 9, 4, 9)
                ));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
        });

        return field;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 40));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private Border createStyledBorder(String title, Color color) {
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(color, 2),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                color
        );
        return BorderFactory.createCompoundBorder(
                titledBorder,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
    }

    private void insertRecord() {
        try {
            // Validate all fields
            if (idField.getText().trim().isEmpty() || typeField.getText().trim().isEmpty() ||
                    locationField.getText().trim().isEmpty() || startField.getText().trim().isEmpty() ||
                    endField.getText().trim().isEmpty() || severityField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Please fill all fields!",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            PreparedStatement pst = con.prepareStatement("INSERT INTO Disaster VALUES (?, ?, ?, ?, ?, ?)");
            pst.setInt(1, Integer.parseInt(idField.getText().trim()));
            pst.setString(2, typeField.getText().trim());
            pst.setString(3, locationField.getText().trim());
            pst.setDate(4, java.sql.Date.valueOf(startField.getText().trim()));
            pst.setDate(5, java.sql.Date.valueOf(endField.getText().trim()));
            pst.setString(6, severityField.getText().trim());
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Record inserted successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear fields
            idField.setText("");
            typeField.setText("");
            locationField.setText("");
            startField.setText("");
            endField.setText("");
            severityField.setText("");

            displayRecords(); // Auto-refresh
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Disaster ID must be a number!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Invalid date format! Use YYYY-MM-DD",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void displayRecords() {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Disaster");

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-12s %-15s %-20s %-12s %-12s %-10s\n",
                    "ID", "Type", "Location", "Start", "End", "Severity"));
            sb.append("═".repeat(95)).append("\n");

            int count = 0;
            while (rs.next()) {
                sb.append(String.format("%-12d %-15s %-20s %-12s %-12s %-10s\n",
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4),
                        rs.getDate(5),
                        rs.getString(6)));
                count++;
            }

            if (count > 0) {
                outputArea.setText(sb.toString());
            } else {
                outputArea.setText(sb.toString() + "\nNo records found.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error displaying records!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateEndDate() {
        try {
            String idText = updateIDField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Please enter a Disaster ID!",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idText);
            String newEndDate = JOptionPane.showInputDialog(this,
                    "Enter new End Date (YYYY-MM-DD):", "Update End Date",
                    JOptionPane.QUESTION_MESSAGE);

            if (newEndDate != null && !newEndDate.trim().isEmpty()) {
                PreparedStatement pst = con.prepareStatement(
                        "UPDATE Disaster SET end_date = ? WHERE disaster_id = ?");
                pst.setDate(1, java.sql.Date.valueOf(newEndDate.trim()));
                pst.setInt(2, id);
                int rows = pst.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "✅ End Date updated successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    updateIDField.setText("");
                    displayRecords();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Record not found!",
                            "Not Found", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter a valid numeric ID!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Invalid date format! Use YYYY-MM-DD",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deleteRecord() {
        try {
            String idText = deleteIDField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Please enter a Disaster ID!",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idText);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete record with ID: " + id + "?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                PreparedStatement pst = con.prepareStatement(
                        "DELETE FROM Disaster WHERE disaster_id = ?");
                pst.setInt(1, id);
                int rows = pst.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "🗑️ Record deleted successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    deleteIDField.setText("");
                    displayRecords();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Record not found!",
                            "Not Found", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter a valid numeric ID!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DisasterUI ui = new DisasterUI();
            ui.setVisible(true);
        });
    }
}