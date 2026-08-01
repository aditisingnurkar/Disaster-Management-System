import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardUI extends JFrame {

    public DashboardUI() {
        // Frame setup
        setTitle("Disaster Management System");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === ENHANCED COLORS ===
        Color sidebarColor = new Color(41, 53, 65);
        Color mainBg = new Color(245, 247, 250);
        Color hoverColor = new Color(52, 152, 219);
        Color cardBg = Color.WHITE;
        Color accentColor = new Color(231, 76, 60);

        // === HEADER PANEL ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 53, 65));
        headerPanel.setPreferredSize(new Dimension(1100, 70));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, accentColor));

        JLabel headerTitle = new JLabel("  DISASTER MANAGEMENT SYSTEM");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JLabel headerSubtitle = new JLabel("Central Command Dashboard  ");
        headerSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        headerSubtitle.setForeground(new Color(189, 195, 199));
        headerSubtitle.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(headerTitle, BorderLayout.WEST);
        headerPanel.add(headerSubtitle, BorderLayout.EAST);

        // === SIDEBAR ===
        JPanel sidebar = new JPanel();
        sidebar.setBackground(sidebarColor);
        sidebar.setPreferredSize(new Dimension(260, 600));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // === MODULE BUTTONS ===
        String[][] modules = {
                {"Dashboard", "▣"},
                {"Disaster Management", "D"},
                {"Volunteer Management", "U"},
                {"Shelter Management", "S"},
                {"Resource Allocation", "R"},
                {"Victim Management", "V"},
                {"Shelter Inventory", "T"},
                {"Incident Reports", "I"},
                {"Donate", "♥"},
                {"About", "?"},
                {"Exit", "X"}
        };

        JButton[] buttons = new JButton[modules.length];

        for (int i = 0; i < modules.length; i++) {
            final int index = i;
            buttons[i] = createStyledButton(modules[i][0], modules[i][1], sidebarColor, hoverColor);

            buttons[i].addActionListener(e -> handleNavigation(modules[index][0]));
            sidebar.add(buttons[i]);

            if (i == 0 || i == modules.length - 2) {
                sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
            } else {
                sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        // === MAIN PANEL ===
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(mainBg);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Welcome Card
        JPanel welcomeCard = new JPanel(new BorderLayout());
        welcomeCard.setBackground(cardBg);
        welcomeCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        JTextArea info = new JTextArea(
                "The Central Disaster Management Dashboard is designed to enhance coordination, " +
                        "efficiency, and transparency in disaster response operations.\n\n" +
                        "Use the left panel to navigate through key modules:\n" +
                        "Victims | Shelters | Volunteers | Resources | Reports\n\n" +
                        "This platform enables authorized personnel to monitor victims, allocate shelters, " +
                        "assign volunteers, manage essential resources, and generate comprehensive reports — " +
                        "ensuring timely and effective decision-making during emergencies.\n\n" +
                        "Together, we strengthen our capacity to respond, recover, and rebuild."
        );
        info.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        info.setBackground(cardBg);
        info.setForeground(new Color(52, 73, 94));
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        welcomeCard.add(info, BorderLayout.CENTER);

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(mainBg);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        Color[] statColors = {
                new Color(231, 76, 60),
                new Color(52, 152, 219),
                new Color(46, 204, 113),
                new Color(241, 196, 15)
        };

        String[][] stats = {
                {"Active Incidents", "12"},
                {"Active Shelters", "8"},
                {"Volunteers", "156"},
                {"Resources", "2.4K"}
        };

        for (int i = 0; i < stats.length; i++) {
            JPanel statCard = createStatCard(stats[i][0], stats[i][1], statColors[i]);
            statsPanel.add(statCard);
        }

        mainPanel.add(welcomeCard, BorderLayout.CENTER);
        mainPanel.add(statsPanel, BorderLayout.SOUTH);

        // === ASSEMBLE FRAME ===
        add(headerPanel, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, String icon, Color bgColor, Color hoverColor) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout(10, 0));
        button.setMaximumSize(new Dimension(260, 50));
        button.setPreferredSize(new Dimension(260, 50));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(" " + icon + " ");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        iconLabel.setForeground(Color.WHITE);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textLabel.setForeground(Color.WHITE);

        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        contentPanel.setOpaque(false);
        contentPanel.add(iconLabel);
        contentPanel.add(textLabel);

        button.add(contentPanel, BorderLayout.WEST);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JPanel createStatCard(String label, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(accentColor);

        JLabel labelText = new JLabel(label, SwingConstants.CENTER);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelText.setForeground(new Color(127, 140, 141));

        card.add(valueLabel, BorderLayout.CENTER);
        card.add(labelText, BorderLayout.SOUTH);

        return card;
    }

    private void showQRCodeWindow() {
        JDialog qrWindow = new JDialog(this, "Scan QR Code to Donate", true);
        qrWindow.setSize(500, 550);
        qrWindow.setLocationRelativeTo(this);
        qrWindow.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("Scan to Donate", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(231, 76, 60));

        // QR Code Image
        JLabel qrLabel = null;
        ImageIcon qrIcon = null;

        try {
            String absolutePath = "C:/Users/ujiba/IdeaProjects/dbmsproject/qr_code.png";

            java.io.File file = new java.io.File(absolutePath);
            if (file.exists()) {
                qrIcon = new ImageIcon(absolutePath);
            } else {
                java.net.URL imgURL = getClass().getResource("/qr_code.png");
                if (imgURL == null) {
                    imgURL = getClass().getResource("qr_code.png");
                }

                if (imgURL != null) {
                    qrIcon = new ImageIcon(imgURL);
                } else {
                    String[] paths = {"qr_code.png", "./qr_code.png", "src/qr_code.png"};
                    for (String path : paths) {
                        file = new java.io.File(path);
                        if (file.exists()) {
                            qrIcon = new ImageIcon(file.getAbsolutePath());
                            break;
                        }
                    }
                }
            }

            if (qrIcon != null && qrIcon.getIconWidth() > 0 && qrIcon.getIconHeight() > 0) {
                Image scaledImage = qrIcon.getImage().getScaledInstance(380, 380, Image.SCALE_SMOOTH);
                qrLabel = new JLabel(new ImageIcon(scaledImage));
                qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                qrLabel = new JLabel("<html><div style='text-align: center; padding: 60px;'>" +
                        "<b style='font-size: 20px; color: #e74c3c;'>QR Code Not Found</b><br><br>" +
                        "<span style='font-size: 12px; color: #95a5a6;'>Please ensure qr_code.png is in the project directory</span>" +
                        "</div></html>");
                qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
                qrLabel.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 2));
            }
        } catch (Exception e) {
            qrLabel = new JLabel("<html><div style='text-align: center; padding: 60px;'>" +
                    "<b style='font-size: 20px; color: #e74c3c;'>Error Loading QR Code</b><br><br>" +
                    "<span style='font-size: 12px; color: #95a5a6;'>" + e.getMessage() + "</span>" +
                    "</div></html>");
            qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
            qrLabel.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 2));
        }

        // Instructions
        JLabel instructionLabel = new JLabel("<html><div style='text-align: center;'>" +
                "Use any UPI app to scan this QR code<br>" +
                "and contribute to disaster relief efforts" +
                "</div></html>", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        instructionLabel.setForeground(new Color(127, 140, 141));

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(qrLabel, BorderLayout.CENTER);
        mainPanel.add(instructionLabel, BorderLayout.SOUTH);

        qrWindow.add(mainPanel);
        qrWindow.setVisible(true);
    }

    private void showAboutDialog() {
        JDialog aboutDialog = new JDialog(this, "About", true);
        aboutDialog.setSize(550, 720);
        aboutDialog.setLocationRelativeTo(this);
        aboutDialog.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Header with icon and title
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(Color.WHITE);

        JLabel iconLabel = new JLabel("i");
        iconLabel.setFont(new Font("Arial", Font.BOLD, 40));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setBackground(new Color(52, 152, 219));
        iconLabel.setOpaque(true);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(50, 50));
        iconLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Disaster Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(41, 53, 65));

        JLabel versionLabel = new JLabel("Version 1.0");
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        versionLabel.setForeground(new Color(127, 140, 141));

        titlePanel.add(titleLabel);
        titlePanel.add(versionLabel);

        headerPanel.add(iconLabel, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.CENTER);

        // Description
        JTextArea descriptionArea = new JTextArea(
                "A comprehensive solution for emergency response coordination, " +
                        "designed to save lives and restore hope in times of crisis."
        );
        descriptionArea.setFont(new Font("Georgia", Font.PLAIN, 14));
        descriptionArea.setForeground(new Color(52, 73, 94));
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Emergency Contacts Panel
        JPanel contactsPanel = new JPanel(new BorderLayout(0, 15));
        contactsPanel.setBackground(new Color(245, 247, 250));
        contactsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel contactsTitle = new JLabel("EMERGENCY CONTACTS");
        contactsTitle.setFont(new Font("Arial", Font.BOLD, 15));
        contactsTitle.setForeground(new Color(231, 76, 60));

        JPanel contactDetailsPanel = new JPanel(new GridLayout(2, 1, 0, 12));
        contactDetailsPanel.setBackground(new Color(245, 247, 250));

        JLabel phoneLabel = new JLabel("<html><b style='font-family: Arial;'>Phone:</b> <span style='font-family: Courier New;'>+91-1209xxx982</span></html>");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneLabel.setForeground(new Color(52, 73, 94));

        JLabel emailLabel = new JLabel("<html><b style='font-family: Arial;'>Email:</b> <span style='font-family: Courier New;'>Disastermanagement@gmail.com</span></html>");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(52, 73, 94));

        contactDetailsPanel.add(phoneLabel);
        contactDetailsPanel.add(emailLabel);

        contactsPanel.add(contactsTitle, BorderLayout.NORTH);
        contactsPanel.add(contactDetailsPanel, BorderLayout.CENTER);

        // Social Media Panel
        JPanel socialPanel = new JPanel(new BorderLayout(0, 15));
        socialPanel.setBackground(new Color(250, 250, 252));
        socialPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel socialTitle = new JLabel("CONNECT WITH US");
        socialTitle.setFont(new Font("Arial", Font.BOLD, 15));
        socialTitle.setForeground(new Color(52, 152, 219));

        JPanel socialDetailsPanel = new JPanel(new GridLayout(2, 1, 0, 12));
        socialDetailsPanel.setBackground(new Color(250, 250, 252));

        JLabel instaLabel = new JLabel("<html><b style='font-family: Arial;'>Instagram:</b> <span style='font-family: Consolas; color: #E1306C;'>@disaster.relief.india</span></html>");
        instaLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instaLabel.setForeground(new Color(52, 73, 94));

        JLabel fbLabel = new JLabel("<html><b style='font-family: Arial;'>Facebook:</b> <span style='font-family: Consolas; color: #1877F2;'>DisasterManagementIndia</span></html>");
        fbLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        fbLabel.setForeground(new Color(52, 73, 94));

        socialDetailsPanel.add(instaLabel);
        socialDetailsPanel.add(fbLabel);

        socialPanel.add(socialTitle, BorderLayout.NORTH);
        socialPanel.add(socialDetailsPanel, BorderLayout.CENTER);

        // Credits Panel
        JPanel creditsPanel = new JPanel(new BorderLayout(0, 10));
        creditsPanel.setBackground(Color.WHITE);
        creditsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel creditsTitle = new JLabel("Made with love by", SwingConstants.CENTER);
        creditsTitle.setFont(new Font("Georgia", Font.ITALIC, 14));
        creditsTitle.setForeground(new Color(127, 140, 141));

        JLabel namesLabel = new JLabel("Jui, Aditi, Bhoomi & Ankita", SwingConstants.CENTER);
        namesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        namesLabel.setForeground(new Color(46, 204, 113));

        creditsPanel.add(creditsTitle, BorderLayout.NORTH);
        creditsPanel.add(namesLabel, BorderLayout.CENTER);

        // Close Button
        JButton closeButton = new JButton("CLOSE");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(52, 152, 219));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setPreferredSize(new Dimension(120, 40));

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(new Color(41, 128, 185));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(new Color(52, 152, 219));
            }
        });

        closeButton.addActionListener(e -> aboutDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(closeButton);

        // Assemble center content
        JPanel centerContent = new JPanel(new BorderLayout(0, 15));
        centerContent.setBackground(Color.WHITE);

        JPanel contactsWrapper = new JPanel(new GridLayout(2, 1, 0, 15));
        contactsWrapper.setBackground(Color.WHITE);
        contactsWrapper.add(contactsPanel);
        contactsWrapper.add(socialPanel);

        centerContent.add(descriptionArea, BorderLayout.NORTH);
        centerContent.add(contactsWrapper, BorderLayout.CENTER);
        centerContent.add(creditsPanel, BorderLayout.SOUTH);

        // Assemble main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerContent, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        aboutDialog.add(mainPanel);
        aboutDialog.setVisible(true);
    }

    private void showDonateDialog() {
        JDialog donateDialog = new JDialog(this, "Support Our Mission", true);
        donateDialog.setSize(600, 650);
        donateDialog.setLocationRelativeTo(this);
        donateDialog.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        // Title
        JLabel titleLabel = new JLabel("Every Contribution Saves Lives", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(231, 76, 60));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Emotional Message
        JTextArea messageArea = new JTextArea(
                "In times of crisis, hope is not lost when compassionate hearts come together.\n\n" +
                        "Your donation provides:\n" +
                        "• Shelter for displaced families\n" +
                        "• Food and water for those in need\n" +
                        "• Medical supplies for the injured\n" +
                        "• Essential resources for recovery\n\n" +
                        "Every rupee you contribute becomes a beacon of hope for someone fighting to rebuild " +
                        "their life. Your generosity doesn't just provide resources—it restores dignity, " +
                        "rebuilds communities, and reminds survivors that they are not alone.\n\n" +
                        "Together, we can turn tragedy into triumph.\n" +
                        "Together, we can be the change."
        );
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageArea.setForeground(new Color(52, 73, 94));
        messageArea.setBackground(Color.WHITE);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // QR Code Button Panel
        JPanel qrButtonPanel = new JPanel(new BorderLayout(0, 20));
        qrButtonPanel.setBackground(Color.WHITE);
        qrButtonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 2),
                BorderFactory.createEmptyBorder(50, 40, 50, 40)
        ));

        JLabel qrInfoLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<b style='font-size: 16px; color: #34495e;'>Scan QR Code to Make Your Donation</b><br><br>" +
                "<span style='font-size: 13px; color: #7f8c8d;'>Click the button below to view the payment QR code</span>" +
                "</div></html>", SwingConstants.CENTER);

        JButton viewQRButton = new JButton("VIEW QR CODE");
        viewQRButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        viewQRButton.setBackground(new Color(231, 76, 60));
        viewQRButton.setForeground(Color.WHITE);
        viewQRButton.setFocusPainted(false);
        viewQRButton.setBorderPainted(false);
        viewQRButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewQRButton.setPreferredSize(new Dimension(280, 50));

        viewQRButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                viewQRButton.setBackground(new Color(192, 57, 43));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                viewQRButton.setBackground(new Color(231, 76, 60));
            }
        });

        viewQRButton.addActionListener(e -> showQRCodeWindow());

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.setBackground(Color.WHITE);
        buttonContainer.add(viewQRButton);

        qrButtonPanel.add(qrInfoLabel, BorderLayout.NORTH);
        qrButtonPanel.add(buttonContainer, BorderLayout.CENTER);

        // Thank you note
        JLabel thankYouLabel = new JLabel("Thank you for being a hero.", SwingConstants.CENTER);
        thankYouLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        thankYouLabel.setForeground(new Color(46, 204, 113));
        thankYouLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Assemble
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(messageArea, BorderLayout.NORTH);
        centerPanel.add(qrButtonPanel, BorderLayout.CENTER);
        centerPanel.add(thankYouLabel, BorderLayout.SOUTH);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        donateDialog.add(mainPanel);
        donateDialog.setVisible(true);
    }

    private void openModule(JFrame moduleUI) {
        this.setVisible(false);
        moduleUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        moduleUI.setVisible(true);
        moduleUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                DashboardUI.this.setVisible(true);
            }
        });
    }

    private void handleNavigation(String module) {
        switch (module) {
            case "Dashboard":
                JOptionPane.showMessageDialog(this,
                        "You're already on the Dashboard!",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Disaster Management":
                openModule(new DisasterUI());
                break;
            case "Victim Management":
                openModule(new VictimUI());
                break;
            case "Shelter Management":
                openModule(new ShelterUI());
                break;
            case "Volunteer Management":
                openModule(new VolunteerUI());
                break;
            case "Resource Allocation":
                openModule(new ResourceUI());
                break;
            case "Incident Reports":
                openModule(new IncidentReportUI());
                break;
            case "Shelter Inventory":
                openModule(new ShelterUI());
                break;
            case "Donate":
                showDonateDialog();
                break;
            case "About":
                showAboutDialog();
                break;
            case "Exit":
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to exit the system?",
                        "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
                break;
        }
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