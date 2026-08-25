import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SplashScreen extends JWindow {

    private JProgressBar progressBar;

    public SplashScreen() {
        int width = 720;
        int height = 480;
        setSize(width, height);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.PRIMARY_NAVY);
        mainPanel.setBorder(BorderFactory.createLineBorder(UITheme.ACCENT_BLUE, 2));

        // Center visual
        JLabel imageLabel = loadImage(width, height - 120);
        mainPanel.add(imageLabel, BorderLayout.CENTER);

        // Bottom progress area
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 8));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        JLabel statusLabel = new JLabel("Initializing Disaster Management DBMS...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setForeground(new Color(203, 213, 225));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(UITheme.ACCENT_BLUE);
        progressBar.setBackground(new Color(30, 41, 59));
        progressBar.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 11));

        bottomPanel.add(statusLabel, BorderLayout.NORTH);
        bottomPanel.add(progressBar, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }

    private JLabel loadImage(int width, int height) {
        String[] paths = {
                "dashboard.png",
                "src/dashboard.png",
                "./dashboard.png"
        };

        for (String path : paths) {
            File f = new File(path);
            if (f.exists()) {
                ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                    Image scaled = icon.getImage().getScaledInstance(width - 40, height, Image.SCALE_SMOOTH);
                    JLabel lbl = new JLabel(new ImageIcon(scaled));
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
                    return lbl;
                }
            }
        }

        // Fallback banner
        JLabel banner = new JLabel("<html><center><h1 style='color:white;'>DISASTER MANAGEMENT SYSTEM</h1><p style='color:#94a3b8;'>Central Command & Relief Operations DBMS (3NF)</p></center></html>");
        banner.setHorizontalAlignment(SwingConstants.CENTER);
        return banner;
    }

    public void launchApp() {
        setVisible(true);
        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i += 5) {
                    final int val = i;
                    SwingUtilities.invokeLater(() -> progressBar.setValue(val));
                    Thread.sleep(60);
                }
            } catch (InterruptedException ignored) {}

            SwingUtilities.invokeLater(() -> {
                setVisible(false);
                dispose();
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {}
                new DashboardUI().setVisible(true);
            });
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SplashScreen().launchApp());
    }
}