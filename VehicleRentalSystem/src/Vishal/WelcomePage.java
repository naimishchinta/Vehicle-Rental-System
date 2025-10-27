package Vishal;

import javax.swing.*;
import java.awt.*;

public class WelcomePage extends JFrame {
    public WelcomePage() {
        setTitle("Vehicle Rental Booking System");
        setSize(720, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GradientPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblTitle = new JLabel("VEHICLE RENTAL BOOKING");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblTitle.setForeground(new Color(40, 40, 55));
        panel.add(lblTitle, gbc);

        gbc.gridy = 1;
        JLabel tagline = new JLabel("Fast | Easy | Reliable");
        tagline.setFont(new Font("Segoe UI", Font.ITALIC, 28));
        tagline.setForeground(new Color(100, 110, 130));
        panel.add(tagline, gbc);

        gbc.gridy = 2;
        RoundedButton btnLogin = new RoundedButton("Login", new Color(119, 174, 38), Color.WHITE);
        panel.add(btnLogin, gbc);

        gbc.gridy = 3;
        RoundedButton btnRegister = new RoundedButton("Register", Color.WHITE, new Color(119, 174, 38));
        btnRegister.setBorder(BorderFactory.createLineBorder(new Color(119, 174, 38), 2));
        panel.add(btnRegister, gbc);

        gbc.gridy = 4;
        RoundedButton btnAdminLogin = new RoundedButton("Admin Login", new Color(197, 153, 36), Color.WHITE);
        panel.add(btnAdminLogin, gbc);

        btnLogin.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        btnRegister.addActionListener(e -> {
            new RegisterUser();
            dispose();
        });

        btnAdminLogin.addActionListener(e -> {
            new AdminLoginFrame();
            dispose();
        });

        setContentPane(panel);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // <-- Add this line for full screen
        setVisible(true);
    }

    // Custom JPanel with gradient background and rounded corners
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

    // Custom JButton with rounded corners and color fills
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
            setFont(new Font("Segoe UI", Font.BOLD, 28));
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            setBorderPainted(false);
            setBorder(BorderFactory.createEmptyBorder());

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setForeground(fgColor);
                    setBackground(hoverColor);
                    repaint();
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setForeground(fgColor);
                    setBackground(bgColor);
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (getModel().isPressed()) {
                g.setColor(bgColor.darker());
            } else if (getModel().isRollover()) {
                g.setColor(hoverColor);
            } else {
                g.setColor(bgColor);
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.dispose();

            super.paintComponent(g);
        }

        @Override
        public void setContentAreaFilled(boolean b) {
            // Do nothing to prevent default content area fill
        }
    }

    public static void main(String[] args) {
        new WelcomePage();
    }
}
