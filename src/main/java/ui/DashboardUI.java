import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class DashboardUI extends JFrame {

    private JLabel disasterStatVal, victimStatVal, shelterStatVal, volunteerStatVal, inventoryStatVal, incidentStatVal;
    private JTable victimsSummaryTable;
    private DefaultTableModel victimsModel;
    private JTable shelterSummaryTable;
    private DefaultTableModel shelterModel;
    private JTable incidentsSummaryTable;
    private DefaultTableModel incidentsModel;

    public DashboardUI() {
        setTitle("Central Command - Disaster Management System");
        setSize(1350, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.PRIMARY_NAVY);
        headerPanel.setPreferredSize(new Dimension(1350, 75));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, UITheme.ACCENT_BLUE));

        JLabel headerTitle = new JLabel("  🌐 DISASTER MANAGEMENT & RELIEF COMMAND");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 18));
        headerRight.setOpaque(false);

        JButton dbSettingsBtn = UITheme.createPrimaryButton("⚙ DB Settings", new Color(51, 65, 85), new Color(71, 85, 105));
        dbSettingsBtn.addActionListener(e -> showDBSettingsDialog());

        JButton refreshBtn = UITheme.createPrimaryButton("⟳ Refresh Live Metrics", UITheme.SUCCESS_GREEN, new Color(5, 150, 105));
        refreshBtn.addActionListener(e -> refreshDashboard());

        headerRight.add(dbSettingsBtn);
        headerRight.add(refreshBtn);

        headerPanel.add(headerTitle, BorderLayout.WEST);
        headerPanel.add(headerRight, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Main Dashboard Content
        JPanel mainContent = new JPanel(new BorderLayout(15, 15));
        mainContent.setBackground(UITheme.BG_LIGHT);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Statistics Panel (6 Dynamic Cards)
        JPanel statsPanel = createStatsPanel();
        mainContent.add(statsPanel, BorderLayout.NORTH);

        // Center Split Overview Tables
        JPanel overviewPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        overviewPanel.setOpaque(false);

        // Left Table: Victims by Disaster
        JPanel victimsCard = UITheme.createCard("👥 Victims Impact Breakdown (SQL Aggregations)", UITheme.PRIMARY_DARK);
        String[] victimCols = {"Disaster Event", "Location", "Severity", "Victims Count"};
        victimsModel = new DefaultTableModel(victimCols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        victimsSummaryTable = new JTable(victimsModel);
        UITheme.styleTable(victimsSummaryTable);
        victimsCard.add(new JScrollPane(victimsSummaryTable), BorderLayout.CENTER);
        overviewPanel.add(victimsCard);

        // Right Table: Shelter Occupancy vs Capacity
        JPanel shelterCard = UITheme.createCard("⛺ Shelter Occupancy vs Capacity", UITheme.PRIMARY_DARK);
        String[] shelterCols = {"Shelter Name", "Disaster", "Capacity", "Occupied", "Available"};
        shelterModel = new DefaultTableModel(shelterCols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        shelterSummaryTable = new JTable(shelterModel);
        UITheme.styleTable(shelterSummaryTable);
        shelterCard.add(new JScrollPane(shelterSummaryTable), BorderLayout.CENTER);
        overviewPanel.add(shelterCard);

        // Bottom Table: Recent Incidents
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(0, 180));

        JPanel incidentsCard = UITheme.createCard("🚨 Recent Live Emergency Incident Reports", UITheme.PRIMARY_DARK);
        String[] incCols = {"Report ID", "Incident Type", "Status", "Disaster Event", "Volunteer", "Logged Time"};
        incidentsModel = new DefaultTableModel(incCols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        incidentsSummaryTable = new JTable(incidentsModel);
        UITheme.styleTable(incidentsSummaryTable);
        incidentsCard.add(new JScrollPane(incidentsSummaryTable), BorderLayout.CENTER);
        bottomPanel.add(incidentsCard, BorderLayout.CENTER);

        JPanel centerWrapper = new JPanel(new BorderLayout(0, 15));
        centerWrapper.setOpaque(false);
        centerWrapper.add(overviewPanel, BorderLayout.CENTER);
        centerWrapper.add(bottomPanel, BorderLayout.SOUTH);

        mainContent.add(centerWrapper, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);

        // Load metrics on startup
        refreshDashboard();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(UITheme.PRIMARY_DARK);
        sidebar.setPreferredSize(new Dimension(270, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        String[][] menuItems = {
                {"Disasters", "🌍", "Disaster Management"},
                {"Shelters", "⛺", "Shelter Management"},
                {"Victims", "🧍", "Victim Management"},
                {"Resources", "📦", "Resource Allocation"},
                {"Shelter Inventory", "📊", "Shelter Inventory"},
                {"Volunteers", "🤝", "Volunteer Management"},
                {"Assignments", "🎯", "Volunteer Assignments"},
                {"Incident Reports", "🚨", "Incident Reports"},
                {"DBMS Query Runner", "⚡", "DBMS Query Runner"},
                {"Support Mission", "💖", "Donate"},
                {"About System", "ℹ", "About"},
                {"Exit System", "🚪", "Exit"}
        };

        for (String[] item : menuItems) {
            JButton btn = createSidebarButton(item[0], item[1]);
            btn.addActionListener(e -> handleNavigation(item[2]));
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        }

        return sidebar;
    }

    private JButton createSidebarButton(String text, String icon) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(10, 0));
        btn.setMaximumSize(new Dimension(250, 42));
        btn.setPreferredSize(new Dimension(250, 42));
        btn.setBackground(UITheme.PRIMARY_DARK);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(" " + icon + " ");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        iconLabel.setForeground(new Color(148, 163, 184));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setForeground(Color.WHITE);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setOpaque(false);
        panel.add(iconLabel);
        panel.add(textLabel);

        btn.add(panel, BorderLayout.WEST);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(51, 65, 85)); // Slate 700
                iconLabel.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(UITheme.PRIMARY_DARK);
                iconLabel.setForeground(new Color(148, 163, 184));
            }
        });

        return btn;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 6, 12, 0));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(0, 95));

        disasterStatVal = new JLabel("0", SwingConstants.CENTER);
        victimStatVal = new JLabel("0", SwingConstants.CENTER);
        shelterStatVal = new JLabel("0", SwingConstants.CENTER);
        volunteerStatVal = new JLabel("0", SwingConstants.CENTER);
        inventoryStatVal = new JLabel("0", SwingConstants.CENTER);
        incidentStatVal = new JLabel("0", SwingConstants.CENTER);

        panel.add(createStatCard("Active Disasters", disasterStatVal, new Color(239, 68, 68)));
        panel.add(createStatCard("Registered Victims", victimStatVal, new Color(59, 130, 246)));
        panel.add(createStatCard("Relief Shelters", shelterStatVal, new Color(16, 185, 129)));
        panel.add(createStatCard("Volunteers", volunteerStatVal, new Color(168, 85, 247)));
        panel.add(createStatCard("Inventory Items", inventoryStatVal, new Color(245, 158, 11)));
        panel.add(createStatCard("Open Incidents", incidentStatVal, new Color(225, 29, 72)));

        return panel;
    }

    private JPanel createStatCard(String label, JLabel valLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UITheme.BORDER_GRAY, 1),
                        BorderFactory.createEmptyBorder(10, 12, 10, 12)
                )
        ));

        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valLabel.setForeground(accentColor);

        JLabel titleLabel = new JLabel(label, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(UITheme.TEXT_MUTED);

        card.add(valLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        return card;
    }

    public void refreshDashboard() {
        // Run asynchronously to keep UI responsive
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            private DashboardDAO.SystemStats stats;
            private List<Object[]> victimsList;
            private List<Object[]> sheltersList;
            private List<Object[]> incidentsList;

            @Override
            protected Void doInBackground() {
                stats = DashboardDAO.getSystemStats();
                try {
                    victimsList = DashboardDAO.getVictimsByDisaster();
                    sheltersList = DashboardDAO.getShelterOccupancy();
                    incidentsList = DashboardDAO.getRecentIncidents(8);
                } catch (Exception e) {
                    System.err.println("Error fetching dashboard data: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                if (stats != null) {
                    disasterStatVal.setText(String.valueOf(stats.totalDisasters));
                    victimStatVal.setText(String.valueOf(stats.totalVictims));
                    shelterStatVal.setText(String.valueOf(stats.totalShelters));
                    volunteerStatVal.setText(String.valueOf(stats.totalVolunteers));
                    inventoryStatVal.setText(String.valueOf(stats.totalInventoryUnits));
                    incidentStatVal.setText(String.valueOf(stats.openIncidents));
                }

                if (victimsList != null) {
                    victimsModel.setRowCount(0);
                    for (Object[] row : victimsList) victimsModel.addRow(row);
                }

                if (sheltersList != null) {
                    shelterModel.setRowCount(0);
                    for (Object[] row : sheltersList) shelterModel.addRow(row);
                }

                if (incidentsList != null) {
                    incidentsModel.setRowCount(0);
                    for (Object[] row : incidentsList) incidentsModel.addRow(row);
                }
            }
        };
        worker.execute();
    }

    private void openModule(JFrame moduleUI) {
        moduleUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        moduleUI.setVisible(true);
        moduleUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                refreshDashboard();
            }
        });
    }

    private void handleNavigation(String module) {
        switch (module) {
            case "Disaster Management":
                openModule(new DisasterUI());
                break;
            case "Shelter Management":
                openModule(new ShelterUI());
                break;
            case "Victim Management":
                openModule(new VictimUI());
                break;
            case "Resource Allocation":
                openModule(new ResourceUI());
                break;
            case "Shelter Inventory":
                openModule(new ShelterInventoryUI());
                break;
            case "Volunteer Management":
                openModule(new VolunteerUI());
                break;
            case "Volunteer Assignments":
                openModule(new Volunteer_AssignmentsUI());
                break;
            case "Incident Reports":
                openModule(new IncidentReportUI());
                break;
            case "DBMS Query Runner":
                openModule(new DBMSQueryRunnerUI());
                break;
            case "Donate":
                showDonateDialog();
                break;
            case "About":
                showAboutDialog();
                break;
            case "Exit":
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to exit the Disaster Management System?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
                break;
        }
    }

    private void showDBSettingsDialog() {
        JDialog dialog = new JDialog(this, "Database Connection Settings", true);
        dialog.setSize(450, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        panel.setBackground(Color.WHITE);

        JTextField hostField = UITheme.createTextField();
        hostField.setText(DatabaseConnection.getHost());

        JTextField portField = UITheme.createTextField();
        portField.setText(String.valueOf(DatabaseConnection.getPort()));

        JTextField dbField = UITheme.createTextField();
        dbField.setText(DatabaseConnection.getDbName());

        JTextField userField = UITheme.createTextField();
        userField.setText(DatabaseConnection.getUser());

        JPasswordField passField = new JPasswordField();
        passField.setText(DatabaseConnection.getPassword());

        panel.add(new JLabel("Host:"));
        panel.add(hostField);
        panel.add(new JLabel("Port:"));
        panel.add(portField);
        panel.add(new JLabel("Database:"));
        panel.add(dbField);
        panel.add(new JLabel("Username:"));
        panel.add(userField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);

        JButton testBtn = UITheme.createPrimaryButton("Test Connection", UITheme.WARNING_ORANGE, new Color(217, 119, 6));
        JButton saveBtn = UITheme.createPrimaryButton("Save & Apply", UITheme.SUCCESS_GREEN, new Color(5, 150, 105));

        testBtn.addActionListener(e -> {
            DatabaseConnection.saveConfig(hostField.getText().trim(), Integer.parseInt(portField.getText().trim()),
                    dbField.getText().trim(), userField.getText().trim(), new String(passField.getPassword()));
            boolean ok = DatabaseConnection.testConnection();
            if (ok) {
                JOptionPane.showMessageDialog(dialog, "✓ Connected to MySQL database successfully!", "Connection Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, "✗ Connection failed! Please check credentials or ensure MySQL is running.", "Connection Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        saveBtn.addActionListener(e -> {
            DatabaseConnection.saveConfig(hostField.getText().trim(), Integer.parseInt(portField.getText().trim()),
                    dbField.getText().trim(), userField.getText().trim(), new String(passField.getPassword()));
            dialog.dispose();
            refreshDashboard();
            JOptionPane.showMessageDialog(this, "Settings updated.", "Saved", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel btnBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnBox.setBackground(Color.WHITE);
        btnBox.add(testBtn);
        btnBox.add(saveBtn);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(btnBox, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showDonateDialog() {
        JDialog dialog = new JDialog(this, "Support Disaster Relief", true);
        dialog.setSize(480, 560);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("Contribute to Relief Operations", SwingConstants.CENTER);
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.DANGER_RED);

        JLabel qrLabel = new JLabel();
        qrLabel.setHorizontalAlignment(SwingConstants.CENTER);

        File qrFile = new File("assets/qr_code.png");
        if (!qrFile.exists()) qrFile = new File("qr_code.png");

        if (qrFile.exists()) {
            ImageIcon icon = new ImageIcon(qrFile.getAbsolutePath());
            Image scaled = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            qrLabel.setIcon(new ImageIcon(scaled));
        } else {
            qrLabel.setText("<html><center><b>UPI QR Code</b><br>(Place qr_code.png in workspace)</center></html>");
        }

        JLabel noteLabel = new JLabel("<html><center>Every contribution helps provide food, emergency medicine, and shelter to affected families.</center></html>", SwingConstants.CENTER);
        noteLabel.setFont(UITheme.FONT_BODY);
        noteLabel.setForeground(UITheme.TEXT_MUTED);

        JButton closeBtn = UITheme.createPrimaryButton("Close", UITheme.PRIMARY_DARK, UITheme.PRIMARY_NAVY);
        closeBtn.addActionListener(e -> dialog.dispose());

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(qrLabel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(0, 10));
        bottom.setOpaque(false);
        bottom.add(noteLabel, BorderLayout.NORTH);
        bottom.add(closeBtn, BorderLayout.SOUTH);
        panel.add(bottom, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showAboutDialog() {
        JDialog dialog = new JDialog(this, "About Disaster Management System", true);
        dialog.setSize(520, 460);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel("Disaster Management System", SwingConstants.CENTER);
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.PRIMARY_DARK);

        JTextArea desc = new JTextArea(
                "A normalized 3NF relational database management system designed for emergency relief operations.\n\n" +
                "Core Database Modules (8 Normalized Tables):\n" +
                "• DISASTER ── Disasters catalog\n" +
                "• SHELTER ── Relief camps & capacity\n" +
                "• VICTIM ── Affected persons tracking\n" +
                "• RESOURCE ── Emergency relief supply master\n" +
                "• SHELTER_INVENTORY ── M:N Shelter ── Resource\n" +
                "• VOLUNTEER ── Personnel & skills registry\n" +
                "• VOLUNTEER_ASSIGNMENT ── M:N Volunteer ── Disaster\n" +
                "• INCIDENT_REPORT ── Real-time incident logs\n\n" +
                "Technology Stack: Java Swing (GUI), JDBC, MySQL 8.0 (InnoDB)"
        );
        desc.setFont(UITheme.FONT_BODY);
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);

        JButton closeBtn = UITheme.createPrimaryButton("Close", UITheme.PRIMARY_DARK, UITheme.PRIMARY_NAVY);
        closeBtn.addActionListener(e -> dialog.dispose());

        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(desc), BorderLayout.CENTER);
        panel.add(closeBtn, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new DashboardUI().setVisible(true);
        });
    }
}