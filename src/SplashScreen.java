import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SplashScreen extends JWindow {

    public SplashScreen() {
        // Set window size
        int width = 800;
        int height = 600;

        setSize(width, height);
        setLocationRelativeTo(null);

        // Create main panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(41, 53, 65));
        contentPanel.setBorder(BorderFactory.createLineBorder(new Color(231, 76, 60), 3));

        // Load and display the image
        JLabel imageLabel = loadImage(width, height);
        contentPanel.add(imageLabel, BorderLayout.CENTER);

        // Add loading indicator at bottom
        JLabel loadingLabel = new JLabel("Loading System...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        contentPanel.add(loadingLabel, BorderLayout.SOUTH);

        setContentPane(contentPanel);
    }

    private JLabel loadImage(int width, int height) {
        ImageIcon splashIcon = null;

        // Try multiple paths to find the image
        String[] possiblePaths = {
                "dashboard.png",                                           // Current directory
                "./dashboard.png",                                         // Explicit current directory
                "src/dashboard.png",                                       // Source folder
                "../dashboard.png",                                        // Parent directory
                System.getProperty("user.dir") + "/dashboard.png",        // Working directory
                System.getProperty("user.dir") + "/src/dashboard.png",    // Working directory + src
                "C:/Users/ujiba/IdeaProjects/dbmsproject/dashboard.png"   // Your absolute path
        };

        // Try each path
        for (String path : possiblePaths) {
            File file = new File(path);
            if (file.exists()) {
                System.out.println("✓ Found image at: " + file.getAbsolutePath());
                splashIcon = new ImageIcon(file.getAbsolutePath());

                // Check if image loaded successfully
                if (splashIcon.getIconWidth() > 0 && splashIcon.getIconHeight() > 0) {
                    break;
                } else {
                    splashIcon = null;
                }
            } else {
                System.out.println("✗ Not found: " + path);
            }
        }

        // Try loading from classpath
        if (splashIcon == null) {
            try {
                java.net.URL imgURL = getClass().getClassLoader().getResource("dashboard.png");
                if (imgURL != null) {
                    System.out.println("✓ Found image in classpath");
                    splashIcon = new ImageIcon(imgURL);
                }
            } catch (Exception e) {
                System.out.println("✗ Error loading from classpath: " + e.getMessage());
            }
        }

        // Create and return the label
        if (splashIcon != null && splashIcon.getIconWidth() > 0 && splashIcon.getIconHeight() > 0) {
            // Scale image to fit window
            Image img = splashIcon.getImage();
            Image scaledImg = img.getScaledInstance(width - 6, height - 80, Image.SCALE_SMOOTH);

            JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);

            System.out.println("✓ Image loaded successfully!");
            return imageLabel;
        } else {
            // Image not found - show error message
            System.out.println("✗ Image could not be loaded from any location");
            System.out.println("Current working directory: " + System.getProperty("user.dir"));
            return createErrorLabel();
        }
    }

    private JLabel createErrorLabel() {
        JLabel errorLabel = new JLabel();
        errorLabel.setLayout(new BorderLayout());
        errorLabel.setOpaque(true);
        errorLabel.setBackground(new Color(41, 53, 65));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("DISASTER MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Central Command Dashboard");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(189, 195, 199));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

        JLabel errorTitle = new JLabel("⚠ Image Not Found");
        errorTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        errorTitle.setForeground(new Color(231, 76, 60));
        errorTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel noteLabel = new JLabel("Please place 'dashboard.png' in:");
        noteLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        noteLabel.setForeground(new Color(189, 195, 199));
        noteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel pathLabel = new JLabel(System.getProperty("user.dir"));
        pathLabel.setFont(new Font("Consolas", Font.BOLD, 12));
        pathLabel.setForeground(new Color(52, 152, 219));
        pathLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pathLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        textPanel.add(errorTitle);
        textPanel.add(noteLabel);
        textPanel.add(pathLabel);

        centerPanel.add(textPanel);
        errorLabel.add(centerPanel, BorderLayout.CENTER);

        return errorLabel;
    }

    public void showSplashAndLaunchDashboard() {
        // Show splash screen
        toFront();
        setAlwaysOnTop(true);
        setVisible(true);
        repaint();

        System.out.println("Splash screen displayed - waiting 5 seconds...");

        // Create a new thread for the timer to avoid blocking the EDT
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Hide splash screen on EDT
            SwingUtilities.invokeLater(() -> {
                System.out.println("Closing splash screen...");
                setVisible(false);
                dispose();

                // Launch main dashboard
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Launching dashboard...");
                new DashboardUI().setVisible(true);
            });
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.showSplashAndLaunchDashboard();
        });
    }
}