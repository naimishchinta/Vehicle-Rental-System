package Vishal;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GradientPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Back button at top-left with arrow mark
        JButton btnBack = new JButton("\u2190 Back");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btnBack.setForeground(new Color(40, 40, 55));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnBack, gbc);

        btnBack.addActionListener(e -> {
            new WelcomePage(); // Your welcome page frame class
            dispose();
        });

        JLabel lblHeading = new JLabel("User Login");
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 44));
        lblHeading.setForeground(new Color(40, 40, 55));
        lblHeading.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblHeading, gbc);

        gbc.gridwidth = 1;

        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        lblUser.setForeground(new Color(93, 98, 110));
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(lblUser, gbc);

        JTextField txtUser = new JTextField(16);
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        gbc.gridx = 1;
        panel.add(txtUser, gbc);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        lblPass.setForeground(new Color(93, 98, 110));
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(lblPass, gbc);

        JPasswordField txtPass = new JPasswordField(16);
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        gbc.gridx = 1;
        panel.add(txtPass, gbc);

        RoundedButton btnLogin = new RoundedButton("Login", new Color(119, 174, 38), Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 26));
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> {
            String username = txtUser.getText();
            String password = String.valueOf(txtPass.getPassword());
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "SELECT user_id FROM users WHERE username = ? AND password = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    new MainMenu(userId);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials.");
                }
            } catch(SQLException ex) {
                JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
            }
        });

        setContentPane(panel);

        // Maximize the JFrame to full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Uncomment below line to remove window decorations (optional)
        // setUndecorated(true);

        setVisible(true);
    }

    // Custom welcome page theme gradient background
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

    // RoundedButton styled for welcome page
    class RoundedButton extends JButton {
        private Color bgColor, fgColor, hoverColor;

        public RoundedButton(String text, Color bg, Color fg) {
            super(text);
            this.bgColor = bg;
            this.fgColor = fg;
            this.hoverColor = bg.brighter();

            setFocusPainted(false);
            setForeground(fgColor);
            setFont(new Font("Segoe UI", Font.BOLD, 26));
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorderPainted(false);
            setBorder(BorderFactory.createEmptyBorder());

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(hoverColor);
                    repaint();
                }
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
            if (getModel().isPressed())
                g2.setColor(bgColor.darker());
            else if (getModel().isRollover())
                g2.setColor(hoverColor);
            else
                g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public void setContentAreaFilled(boolean b) {}
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
