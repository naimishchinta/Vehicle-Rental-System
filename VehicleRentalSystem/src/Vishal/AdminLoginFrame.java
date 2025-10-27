package Vishal;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AdminLoginFrame extends JFrame {
    public AdminLoginFrame() {
        setTitle("Admin Login");
        setSize(550, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GradientPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(26, 26, 26, 26);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Back Button (top left)
        RoundedButton btnBack = new RoundedButton("← Back", new Color(60, 130, 120), Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnBack, gbc);

        // Heading (top center/right of Back button)
        JLabel lblHeading = new JLabel("Admin Login");
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblHeading.setForeground(new Color(40, 40, 55));
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblHeading, gbc);

        // User Label and Field
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel lblUser = new JLabel("Email:");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        lblUser.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblUser, gbc);

        JTextField txtUser = new JTextField(18);
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        gbc.gridx = 1;
        panel.add(txtUser, gbc);

        // Password Label and Field
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        lblPass.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblPass, gbc);

        JPasswordField txtPass = new JPasswordField(18);
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        gbc.gridx = 1;
        panel.add(txtPass, gbc);

        // Login Button
        RoundedButton btnLogin = new RoundedButton("Login", new Color(119, 174, 38), Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 26));
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(btnLogin, gbc);

        // Register Button
        RoundedButton btnRegister = new RoundedButton("Register", new Color(197, 153, 36), Color.WHITE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 26));
        gbc.gridx = 1;
        panel.add(btnRegister, gbc);

        // Action Listeners
        btnLogin.addActionListener(e -> {
            String email = txtUser.getText();
            String password = new String(txtPass.getPassword());
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/vehicle_rental", "root", "Vikas@123");
                String sql = "SELECT * FROM admins WHERE email = ? AND password = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, email);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    new AdminDashboardFrame();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials. If not registered, click Register.");
                }
                rs.close();
                ps.close();
                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            }
        });

        btnRegister.addActionListener(e -> {
            new AdminRegisterFrame();
            dispose();
        });

        btnBack.addActionListener(e -> {
            new WelcomePage();
            dispose();
        });

        setContentPane(panel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // <-- Make frame full screen
        setVisible(true);
    }

    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(230, 240, 235),
                0, getHeight(), new Color(195, 220, 195));
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.dispose();
        }
    }

    // RoundedButton class consistent with WelcomePage styling
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
            setFont(new Font("Segoe UI", Font.BOLD, 26));
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
            // prevent default content area fill
        }
    }

    public static void main(String[] args) {
        new AdminLoginFrame();
    }
}
