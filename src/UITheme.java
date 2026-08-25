import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UITheme {

    // Color Palette
    public static final Color PRIMARY_DARK = new Color(30, 41, 59);    // Slate 800
    public static final Color PRIMARY_NAVY = new Color(15, 23, 42);    // Slate 900
    public static final Color ACCENT_BLUE = new Color(37, 99, 235);    // Royal Blue 600
    public static final Color ACCENT_HOVER = new Color(29, 78, 216);   // Royal Blue 700
    public static final Color SUCCESS_GREEN = new Color(16, 185, 129); // Emerald 500
    public static final Color WARNING_ORANGE = new Color(245, 158, 11); // Amber 500
    public static final Color DANGER_RED = new Color(239, 68, 68);     // Red 500
    public static final Color DANGER_HOVER = new Color(220, 38, 38);   // Red 600
    public static final Color BG_LIGHT = new Color(248, 250, 252);     // Slate 50
    public static final Color CARD_BG = Color.WHITE;
    public static final Color BORDER_GRAY = new Color(226, 232, 240);  // Slate 200
    public static final Color TEXT_MAIN = new Color(15, 23, 42);       // Slate 900
    public static final Color TEXT_MUTED = new Color(100, 116, 139);   // Slate 500

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    public static JButton createPrimaryButton(String text, Color bg, Color hoverBg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width, 36));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverBg);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    public static JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_BODY);
        tf.setPreferredSize(new Dimension(tf.getPreferredSize().width, 32));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return tf;
    }

    public static JComboBox<ComboItem> createComboBox() {
        JComboBox<ComboItem> cb = new JComboBox<>();
        cb.setFont(FONT_BODY);
        cb.setBackground(Color.WHITE);
        cb.setPreferredSize(new Dimension(cb.getPreferredSize().width, 32));
        return cb;
    }

    public static JPanel createCard(String title, Color headerColor) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(15, 18, 15, 18)
        ));

        if (title != null && !title.isEmpty()) {
            JLabel lbl = new JLabel(title);
            lbl.setFont(FONT_HEADER);
            lbl.setForeground(headerColor);
            card.add(lbl, BorderLayout.NORTH);
        }
        return card;
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(219, 234, 254)); // Blue 100
        table.setSelectionForeground(TEXT_MAIN);
        table.setGridColor(new Color(241, 245, 249));
        table.setShowGrid(true);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(PRIMARY_DARK);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 34));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Center render ID column if present
        if (table.getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        }
    }
}
