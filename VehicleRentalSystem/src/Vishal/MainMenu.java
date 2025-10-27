package Vishal;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    private static final long serialVersionUID = 1L;
    private static int userId;

    public MainMenu(int userId) {
        this.setUserId(userId);
        setTitle("Vehicle Rental - Main Menu");

        // Maximize window for full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        GradientPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(40, 40, 40, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Back button with arrow on top-left
        JButton btnBack = new JButton("\u2190 Back");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btnBack.setForeground(new Color(40, 40, 55));
        btnBack.setToolTipText("Return to Login");
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnBack, gbc);

        btnBack.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        JLabel lblHeading = new JLabel("Main Menu");
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblHeading.setForeground(new Color(40, 40, 55));
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblHeading, gbc);

        // Buttons arranged vertically center
        RoundedButton btnBook = new RoundedButton("Book Vehicle", new Color(119, 174, 38), Color.WHITE);
        btnBook.setFont(new Font("Segoe UI", Font.BOLD, 28));
        btnBook.setToolTipText("Book your preferred vehicle");
        btnBook.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        RoundedButton btnViewBookings = new RoundedButton("My Bookings", new Color(119, 174, 38), Color.WHITE);
        btnViewBookings.setFont(new Font("Segoe UI", Font.BOLD, 28));
        btnViewBookings.setToolTipText("View your booking history");
        btnViewBookings.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Layout buttons with vertical spacing in a separate panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints bGbc = new GridBagConstraints();
        bGbc.insets = new Insets(20, 0, 20, 0); // vertical spacing
        bGbc.gridx = 0; bGbc.gridy = 0;
        buttonPanel.add(btnBook, bGbc);
        bGbc.gridy = 1;
        buttonPanel.add(btnViewBookings, bGbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        btnBook.addActionListener(e -> new BookingForm(userId));
        btnViewBookings.addActionListener(e -> new MyBookingsFrame(userId));

        setContentPane(panel);

        // Focusable and focus traversal keys enabled
        setFocusTraversalPolicy(new DefaultFocusTraversalPolicy());
        
        setVisible(true);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        MainMenu.userId = userId;
    }

    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            int width = getWidth();
            int height = getHeight();

            GradientPaint gp = new GradientPaint(0, 0, new Color(220, 240, 220), 0, height, new Color(180, 230, 180));
            g2d.setPaint(gp);
            g2d.fillRoundRect(0, 0, width, height, 30, 30);

            g2d.dispose();
        }
    }

    class RoundedButton extends JButton {
        private Color bgColor;
        private Color fgColor;
        private Color hoverColor;

        public RoundedButton(String text, Color bg, Color fg) {
            super(text);
            this.bgColor = bg;
            this.fgColor = fg;
            this.hoverColor = bg.brighter();

            setFocusPainted(false);
            setForeground(fgColor);
            setFont(new Font("Segoe UI", Font.BOLD, 24));
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorderPainted(false);
            setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(hoverColor);
                    repaint();
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(bgColor);
                    repaint();
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isPressed()) {
                g2.setColor(bgColor.darker());
            } else if (getModel().isRollover()) {
                g2.setColor(hoverColor);
            } else {
                g2.setColor(bgColor);
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.dispose();
            super.paintComponent(g);
        }
        @Override
        public void setContentAreaFilled(boolean b) {
            // prevent default content fill
        }
    }

    public static void main(String[] args) {
        new MainMenu(1);
    }
}
