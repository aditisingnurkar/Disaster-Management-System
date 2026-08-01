import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class IncidentReportUI extends JFrame {
    private JTextField addReportIdField, addDescField, addTimestampField;
    private JTextField addVolunteerIdField, addDisasterIdField;
    private JTextField updateReportIdField;
    private JComboBox<String> updateFieldChoice;
    private JTextField updateValueField;
    private JTextField deleteReportIdField;
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

    public IncidentReportUI() {
        setTitle("Incident Report Management System");
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

        JLabel titleLabel = new JLabel("📝 Incident Report Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Document and track incident reports during disaster operations");
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

        JLabel titleLabel = new JLabel("Add New Incident Report");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(59, 130, 246));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 12));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        addReportIdField = new JTextField();
        addDescField = new JTextField();
        addTimestampField = new JTextField();
        addVolunteerIdField = new JTextField();
        addDisasterIdField = new JTextField();

        addFormField(formPanel, "Report ID:", addReportIdField);
        addFormField(formPanel, "Description:", addDescField);
        addFormField(formPanel, "Timestamp:", addTimestampField);
        addFormField(formPanel, "Volunteer ID:", addVolunteerIdField);
        addFormField(formPanel, "Disaster ID:", addDisasterIdField);

        // Add helper text for timestamp
        JLabel helperLabel = new JLabel("Format: YYYY-MM-DD HH:MM:SS");
        helperLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        helperLabel.setForeground(new Color(107, 114, 128));

        JPanel timestampHelper = new JPanel(new BorderLayout());
        timestampHelper.setBackground(CARD_BG);
        timestampHelper.add(helperLabel, BorderLayout.WEST);

        // Wrap form panel in scroll pane
        JPanel formWrapper = new JPanel(new BorderLayout(0, 5));
        formWrapper.setBackground(CARD_BG);
        formWrapper.add(formPanel, BorderLayout.CENTER);
        formWrapper.add(timestampHelper, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(formWrapper);
        scrollPane.setBorder(null);
        scrollPane.setBackground(CARD_BG);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton addBtn = createStyledButton("➕ Add Report", BLUE_BTN);
        addBtn.addActionListener(e -> insertIncidentReport());

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

        JLabel titleLabel = new JLabel("Update Incident Report");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ORANGE);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 12));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        updateReportIdField = new JTextField();
        updateFieldChoice = new JComboBox<>(new String[]{
                "Description",
                "Timestamp",
                "Volunteer ID",
                "Disaster ID"
        });
        updateFieldChoice.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        updateFieldChoice.setBackground(Color.WHITE);
        updateFieldChoice.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        updateValueField = new JTextField();

        addFormField(formPanel, "Report ID:", updateReportIdField);

        JLabel fieldLabel = new JLabel("Field to Update:");
        fieldLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fieldLabel.setForeground(new Color(55, 65, 81));
        formPanel.add(fieldLabel);
        formPanel.add(updateFieldChoice);

        addFormField(formPanel, "New Value:", updateValueField);

        // Add helper text that changes based on selection
        JLabel helperLabel = new JLabel("Format: YYYY-MM-DD HH:MM:SS");
        helperLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        helperLabel.setForeground(new Color(107, 114, 128));
        helperLabel.setVisible(false);

        updateFieldChoice.addActionListener(e -> {
            String selected = (String) updateFieldChoice.getSelectedItem();
            helperLabel.setVisible("Timestamp".equals(selected));
        });

        JPanel formWrapper = new JPanel(new BorderLayout(0, 5));
        formWrapper.setBackground(CARD_BG);
        formWrapper.add(formPanel, BorderLayout.CENTER);
        formWrapper.add(helperLabel, BorderLayout.SOUTH);

        // Wrap form in scroll pane
        JScrollPane scrollPane = new JScrollPane(formWrapper);
        scrollPane.setBorder(null);
        scrollPane.setBackground(CARD_BG);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton updateBtn = createStyledButton("🔄 Update Report", ORANGE);
        updateBtn.addActionListener(e -> updateIncidentReport());

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

        JLabel titleLabel = new JLabel("Delete Incident Report");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(RED);

        JPanel formPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));

        deleteReportIdField = new JTextField();
        addFormField(formPanel, "Report ID:", deleteReportIdField);

        // Wrap form in scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(CARD_BG);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton deleteBtn = createStyledButton("🗑️ Delete Report", RED);
        deleteBtn.addActionListener(e -> deleteIncidentReport());
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

        JLabel titleLabel = new JLabel("Incident Reports");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 58, 138));

        JButton refreshBtn = createStyledButton("🔄 Refresh Records", BLUE_BTN);
        refreshBtn.addActionListener(e -> displayIncidentReports());
        refreshBtn.setPreferredSize(new Dimension(200, 40));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
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

    private void insertIncidentReport() {
        try {
            if (addReportIdField.getText().isEmpty() || addDescField.getText().isEmpty() ||
                    addTimestampField.getText().isEmpty() || addVolunteerIdField.getText().isEmpty() ||
                    addDisasterIdField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Please fill all fields!");
                return;
            }

            // Validate timestamp format
            String timestampStr = addTimestampField.getText().trim();
            Timestamp timestamp;
            try {
                timestamp = Timestamp.valueOf(timestampStr);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this,
                        "⚠️ Invalid timestamp format!\nPlease use: YYYY-MM-DD HH:MM:SS\nExample: 2024-11-04 14:30:00");
                return;
            }

            PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO Incident_Report VALUES (?, ?, ?, ?, ?)");
            pst.setInt(1, Integer.parseInt(addReportIdField.getText()));
            pst.setString(2, addDescField.getText());
            pst.setTimestamp(3, timestamp);
            pst.setInt(4, Integer.parseInt(addVolunteerIdField.getText()));
            pst.setInt(5, Integer.parseInt(addDisasterIdField.getText()));
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Incident report inserted successfully!");
            clearAddFields();
            displayIncidentReports();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter valid numbers for IDs!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void displayIncidentReports() {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Incident_Report");
            outputArea.setText(String.format("%-12s %-30s %-22s %-14s %-12s\n",
                    "Report_ID", "Description", "Timestamp", "Volunteer_ID", "Disaster_ID"));
            outputArea.append("─".repeat(95) + "\n");
            while (rs.next()) {
                String desc = rs.getString(2);
                if (desc.length() > 30) desc = desc.substring(0, 27) + "...";
                outputArea.append(String.format("%-12d %-30s %-22s %-14d %-12d\n",
                        rs.getInt(1), desc, rs.getTimestamp(3), rs.getInt(4), rs.getInt(5)));
            }
            rs.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error displaying incident reports!");
            ex.printStackTrace();
        }
    }

    private void updateIncidentReport() {
        String reportId = updateReportIdField.getText();
        String newValue = updateValueField.getText();

        if (reportId.isEmpty() || newValue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please fill all fields!");
            return;
        }

        try {
            String selectedField = (String) updateFieldChoice.getSelectedItem();
            PreparedStatement pst = null;

            switch (selectedField) {
                case "Description":
                    pst = con.prepareStatement(
                            "UPDATE Incident_Report SET Description = ? WHERE Report_ID = ?");
                    pst.setString(1, newValue);
                    pst.setInt(2, Integer.parseInt(reportId));
                    break;

                case "Timestamp":
                    try {
                        Timestamp timestamp = Timestamp.valueOf(newValue.trim());
                        pst = con.prepareStatement(
                                "UPDATE Incident_Report SET Timestamp = ? WHERE Report_ID = ?");
                        pst.setTimestamp(1, timestamp);
                        pst.setInt(2, Integer.parseInt(reportId));
                    } catch (IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(this,
                                "⚠️ Invalid timestamp format!\nPlease use: YYYY-MM-DD HH:MM:SS");
                        return;
                    }
                    break;

                case "Volunteer ID":
                    pst = con.prepareStatement(
                            "UPDATE Incident_Report SET Volunteer_ID = ? WHERE Report_ID = ?");
                    pst.setInt(1, Integer.parseInt(newValue));
                    pst.setInt(2, Integer.parseInt(reportId));
                    break;

                case "Disaster ID":
                    pst = con.prepareStatement(
                            "UPDATE Incident_Report SET Disaster_ID = ? WHERE Report_ID = ?");
                    pst.setInt(1, Integer.parseInt(newValue));
                    pst.setInt(2, Integer.parseInt(reportId));
                    break;
            }

            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "✅ " + selectedField + " updated successfully!");
                clearUpdateFields();
                displayIncidentReports();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Incident report not found!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter valid numbers for numeric fields!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error updating report: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void deleteIncidentReport() {
        String reportId = deleteReportIdField.getText();
        if (reportId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter Report ID!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Report ID: " + reportId + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            PreparedStatement pst = con.prepareStatement(
                    "DELETE FROM Incident_Report WHERE Report_ID = ?");
            pst.setInt(1, Integer.parseInt(reportId));
            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "🗑️ Deleted successfully!");
                deleteReportIdField.setText("");
                displayIncidentReports();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Incident report not found!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter a valid number for Report ID!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error deleting report: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void clearAddFields() {
        addReportIdField.setText("");
        addDescField.setText("");
        addTimestampField.setText("");
        addVolunteerIdField.setText("");
        addDisasterIdField.setText("");
    }

    private void clearUpdateFields() {
        updateReportIdField.setText("");
        updateValueField.setText("");
        updateFieldChoice.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IncidentReportUI ui = new IncidentReportUI();
            ui.setVisible(true);
        });
    }
}
