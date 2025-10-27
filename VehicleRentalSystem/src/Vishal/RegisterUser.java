package Vishal;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.util.Random;

public class RegisterUser extends JFrame {
    private String captchaText; // store generated captcha text

    public RegisterUser() {
        setTitle("Register");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GradientPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(32, 32, 32, 32);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        // Back button at top-left with arrow mark
        JButton btnBack = new JButton("\u2190 Back");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        btnBack.setForeground(new Color(40, 40, 55));
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnBack, gbc);

        btnBack.addActionListener(e -> {
            new WelcomePage(); // Your welcome page frame class
            dispose();
        });

        JLabel lblHeading = new JLabel("User Registration");
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblHeading.setForeground(new Color(40, 40, 55));
        gbc.gridx = 1;  // shifted right to avoid overlap with back button
        gbc.gridwidth = 2;
        panel.add(lblHeading, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;

        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        lblUser.setForeground(new Color(50, 60, 90));
        gbc.gridy = 1;
        panel.add(lblUser, gbc);

        JTextField txtUser = new JTextField(18);
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        gbc.gridx = 1;
        panel.add(txtUser, gbc);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        lblEmail.setForeground(new Color(50, 60, 90));
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(lblEmail, gbc);

        JTextField txtEmail = new JTextField(18);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        lblPass.setForeground(new Color(50, 60, 90));
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(lblPass, gbc);

        JPasswordField txtPass = new JPasswordField(18);
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        gbc.gridx = 1;
        panel.add(txtPass, gbc);

        // CAPTCHA image panel at row 4, col 0
        gbc.gridy = 4;
        gbc.gridx = 0;
        CaptchaPanel captchaPanel = new CaptchaPanel();
        panel.add(captchaPanel, gbc);

        // CAPTCHA input text field at row 4, col 1 (side by side with CAPTCHA image)
        gbc.gridx = 1;
        JTextField txtCaptcha = new JTextField(18);
        txtCaptcha.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        txtCaptcha.setToolTipText("Enter the letters shown in the image");
        panel.add(txtCaptcha, gbc);

        RoundedButton btnRegister = new RoundedButton("Register", new Color(197, 153, 36), Color.WHITE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 32));
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(btnRegister, gbc);

        btnRegister.addActionListener(e -> {
            String username = txtUser.getText();
            String email = txtEmail.getText();
            String password = String.valueOf(txtPass.getPassword());
            String enteredCaptcha = txtCaptcha.getText();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || enteredCaptcha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields including CAPTCHA are required.");
                return;
            }
            if (!enteredCaptcha.equalsIgnoreCase(captchaText)) {
                JOptionPane.showMessageDialog(this, "CAPTCHA is incorrect. Please try again.");
                captchaText = generateCaptchaText();
                captchaPanel.repaint();
                txtCaptcha.setText("");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registration successful!");
                new LoginFrame();
                dispose();
            } catch(SQLException ex) {
                JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
            }
        });

        setContentPane(panel);
        setVisible(true);

        captchaText = generateCaptchaText();
    }

    // Generate random captcha string
    private String generateCaptchaText() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder captcha = new StringBuilder();
        Random rnd = new Random();
        while (captcha.length() < 6) { // length of the captcha
            int index = (int) (rnd.nextFloat() * chars.length());
            captcha.append(chars.charAt(index));
        }
        return captcha.toString();
    }

    // Panel to draw the captcha image
    class CaptchaPanel extends JPanel {
        private final int width = 250;
        private final int height = 80;

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(width, height);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();

            // Background
            g2d.setColor(new Color(220, 240, 220));
            g2d.fillRect(0, 0, width, height);

            // Noise lines
            Random rand = new Random();
            g2d.setColor(new Color(180, 230, 180));
            for (int i = 0; i < 20; i++) {
                int x1 = rand.nextInt(width);
                int y1 = rand.nextInt(height);
                int x2 = rand.nextInt(width);
                int y2 = rand.nextInt(height);
                g2d.drawLine(x1, y1, x2, y2);
            }

            // Draw captcha text
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 48));
            FontMetrics fm = g2d.getFontMetrics();
            int x = (width - fm.stringWidth(captchaText)) / 2;
            int y = ((height - fm.getHeight()) / 2) + fm.getAscent();
            g2d.drawString(captchaText, x, y);

            g2d.dispose();
            g.drawImage(image, 0, 0, null);
        }
    }

    // GradientPanel and RoundedButton classes remain unchanged
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            int width = getWidth();
            int height = getHeight();
            GradientPaint gp = new GradientPaint(0, 0, new Color(220, 240, 220), 0, height, new Color(180, 230, 180));
            g2d.setPaint(gp);
            g2d.fillRoundRect(0, 0, width, height, 32, 32);
            g2d.dispose();
        }
    }

    class RoundedButton extends JButton {
        private final Color bgColor;
        private final Color fgColor;
        private final Color hoverColor;

        public RoundedButton(String text, Color bg, Color fg) {
            super(text);
            this.bgColor = bg;
            this.fgColor = fg;
            this.hoverColor = bg.brighter();

            setFocusPainted(false);
            setForeground(fgColor);
            setFont(new Font("Segoe UI", Font.BOLD, 32));
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder());
            setBorderPainted(false);

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
            if (getModel().isPressed())
                g2.setColor(bgColor.darker());
            else if (getModel().isRollover())
                g2.setColor(hoverColor);
            else
                g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
            g2.dispose();
            super.paintComponent(g);
        }
        @Override
        public void setContentAreaFilled(boolean b) {}
    }

    public static void main(String[] args) {
        new RegisterUser();
    }
}
