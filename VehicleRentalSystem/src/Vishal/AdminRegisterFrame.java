package Vishal;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.sql.*;

public class AdminRegisterFrame extends JFrame {
    private String captchaValue;
    private final String ADMIN_CODE = "6300"; // The required code

    public AdminRegisterFrame() {
        setTitle("Admin Registration");
        setSize(650, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GradientPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(24, 24, 24, 24);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Back Button (top left)
        RoundedButton btnBack = new RoundedButton("← Back", new Color(60, 130, 120), Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnBack, gbc);

        // Heading (top row, col 1)
        JLabel lblHeading = new JLabel("Admin Registration");
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblHeading.setForeground(new Color(40, 40, 55));
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblHeading, gbc);

        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.CENTER; gbc.gridx = 0;

        // Name label and field
        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        lblName.setForeground(new Color(93, 98, 110));
        gbc.gridy = 1;
        panel.add(lblName, gbc);

        JTextField txtName = new JTextField(18);
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        gbc.gridx = 1;
        panel.add(txtName, gbc);

        // Email label and field
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        lblEmail.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblEmail, gbc);

        JTextField txtEmail = new JTextField(18);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        // Password label and field
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        lblPass.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(lblPass, gbc);

        JPasswordField txtPass = new JPasswordField(18);
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        gbc.gridx = 1;
        panel.add(txtPass, gbc);

        // Code label and field
        JLabel lblCode = new JLabel("Code:");
        lblCode.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        lblCode.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(lblCode, gbc);

        JTextField txtCode = new JTextField(18);
        txtCode.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        gbc.gridx = 1;
        panel.add(txtCode, gbc);

        // Captcha label and generated value
        JLabel lblCaptcha = new JLabel("Captcha:");
        lblCaptcha.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        lblCaptcha.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(lblCaptcha, gbc);

        captchaValue = generateCaptcha(6);
        JLabel lblCaptchaCode = new JLabel(captchaValue);
        lblCaptchaCode.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblCaptchaCode.setForeground(new Color(197, 153, 36));
        gbc.gridx = 1;
        panel.add(lblCaptchaCode, gbc);

        // Enter captcha field
        JLabel lblEnterCaptcha = new JLabel("Enter Captcha:");
        lblEnterCaptcha.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        lblEnterCaptcha.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(lblEnterCaptcha, gbc);

        JTextField txtCaptcha = new JTextField(18);
        txtCaptcha.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        gbc.gridx = 1;
        panel.add(txtCaptcha, gbc);

        // Register button
        RoundedButton btnRegister = new RoundedButton("Register", new Color(119, 174, 38), Color.WHITE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 28));
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        panel.add(btnRegister, gbc);

        btnRegister.addActionListener(e -> {
            String name = txtName.getText();
            String email = txtEmail.getText();
            String password = String.valueOf(txtPass.getPassword());
            String code = txtCode.getText();
            String enteredCaptcha = txtCaptcha.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || code.isEmpty() || enteredCaptcha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }
            if (!enteredCaptcha.equals(captchaValue)) {
                JOptionPane.showMessageDialog(this, "Captcha does not match. Try again.");
                return;
            }
            if (!code.equals(ADMIN_CODE)) {
                JOptionPane.showMessageDialog(this, "Invalid Admin Code.");
                return;
            }

            // JDBC registration logic here
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/vehicle_rental", "root", "Vikas@123");
                String sql = "INSERT INTO admins (name, email, password) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, password);
                int result = ps.executeUpdate();
                ps.close();
                conn.close();

                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Admin registration successful!");
                    new AdminLoginFrame();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Registration failed. Try again.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            }
        });

        btnBack.addActionListener(e -> {
            new AdminLoginFrame();
            dispose();
        });

        setContentPane(panel);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // <-- add this line for full screen
        setVisible(true);
    }

    private String generateCaptcha(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder captcha = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; i++)
            captcha.append(chars.charAt(rand.nextInt(chars.length())));
        return captcha.toString();
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
        new AdminRegisterFrame();
    }
}
