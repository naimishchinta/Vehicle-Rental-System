package Vishal;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddVehicleFrame extends JFrame {
    public AddVehicleFrame() {
        setTitle("Add Vehicle Details");
        setSize(600, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GradientPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(13, 13, 13, 13);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Back button (top left)
        RoundedButton btnBack = new RoundedButton("← Back", new Color(60, 130, 120), Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnBack, gbc);

        // Heading next to back button
        JLabel lblHeading = new JLabel("Add Vehicle Details");
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblHeading.setForeground(new Color(40, 40, 55));
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblHeading, gbc);

        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.CENTER; // For rest

        // Vehicle ID
        JLabel lblId = new JLabel("Vehicle ID:");
        lblId.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblId.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblId, gbc);
        JTextField txtId = new JTextField(15);
        txtId.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 1;
        panel.add(txtId, gbc);

        // Brand/Model
        JLabel lblBrand = new JLabel("Brand/Model:");
        lblBrand.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblBrand.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblBrand, gbc);
        JTextField txtBrand = new JTextField(15);
        txtBrand.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 1;
        panel.add(txtBrand, gbc);

        // Location
        JLabel lblLocation = new JLabel("Location:");
        lblLocation.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblLocation.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(lblLocation, gbc);
        JTextField txtLocation = new JTextField(15);
        txtLocation.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 1;
        panel.add(txtLocation, gbc);

        // Type
        JLabel lblType = new JLabel("Type:");
        lblType.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblType.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(lblType, gbc);
        String[] vehicleTypes = { "Car", "Bike" };
        JComboBox<String> cmbType = new JComboBox<>(vehicleTypes);
        cmbType.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 1;
        panel.add(cmbType, gbc);

        // Registration Number
        JLabel lblReg = new JLabel("Registration No:");
        lblReg.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblReg.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(lblReg, gbc);
        JTextField txtReg = new JTextField(15);
        txtReg.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 1;
        panel.add(txtReg, gbc);

        // Year
        JLabel lblYear = new JLabel("Year:");
        lblYear.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblYear.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(lblYear, gbc);
        JTextField txtYear = new JTextField(15);
        txtYear.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 1;
        panel.add(txtYear, gbc);

        // Color
        JLabel lblColor = new JLabel("Color:");
        lblColor.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblColor.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(lblColor, gbc);
        JTextField txtColor = new JTextField(15);
        txtColor.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 1;
        panel.add(txtColor, gbc);

        // Fuel Type
        JLabel lblFuel = new JLabel("Fuel Type:");
        lblFuel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblFuel.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 8;
        panel.add(lblFuel, gbc);
        JTextField txtFuel = new JTextField(15);
        txtFuel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 1;
        panel.add(txtFuel, gbc);

        // Seating Capacity
        JLabel lblSeats = new JLabel("Seating Capacity:");
        lblSeats.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblSeats.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 9;
        panel.add(lblSeats, gbc);
        JTextField txtSeats = new JTextField(15);
        txtSeats.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 1;
        panel.add(txtSeats, gbc);

        // Image URL
        JLabel lblImage = new JLabel("Image URL:");
        lblImage.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblImage.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 10;
        panel.add(lblImage, gbc);
        JTextField txtImage = new JTextField(15);
        txtImage.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 1;
        panel.add(txtImage, gbc);

        // Rate Per Day
        JLabel lblRate = new JLabel("Rate Per Day:");
        lblRate.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblRate.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 11;
        panel.add(lblRate, gbc);
        JTextField txtRate = new JTextField(15);
        txtRate.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 1;
        panel.add(txtRate, gbc);

        // Status
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblStatus.setForeground(new Color(93, 98, 110));
        gbc.gridx = 0; gbc.gridy = 12;
        panel.add(lblStatus, gbc);
        String[] statuses = { "Available", "Booked", "Not Available" };
        JComboBox<String> cmbStatus = new JComboBox<>(statuses);
        cmbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 1;
        panel.add(cmbStatus, gbc);

        RoundedButton btnSubmit = new RoundedButton("Submit", new Color(119, 174, 38), Color.WHITE);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 13; gbc.gridwidth = 2;
        panel.add(btnSubmit, gbc);

        btnSubmit.addActionListener(e -> {
            String id = txtId.getText();
            String model = txtBrand.getText();
            String location = txtLocation.getText();
            String type = (String) cmbType.getSelectedItem();
            String reg = txtReg.getText();
            String year = txtYear.getText();
            String color = txtColor.getText();
            String fuel = txtFuel.getText();
            String seats = txtSeats.getText();
            String image = txtImage.getText();
            String rate = txtRate.getText();
            String status = (String) cmbStatus.getSelectedItem();

            if (id.isEmpty() || model.isEmpty() || location.isEmpty() || type.isEmpty() || reg.isEmpty() ||
                    year.isEmpty() || color.isEmpty() || fuel.isEmpty() || seats.isEmpty() || image.isEmpty() || rate.isEmpty() || status.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/vehicle_rental", "root", "Vikas@123");
                String sql = "INSERT INTO vehicles "
                        + "(vehicle_id, model, location, type, registration_no, year, color, fuel_type, seating_capacity, image_url, rate_per_day, status) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(id));
                ps.setString(2, model);
                ps.setString(3, location);
                ps.setString(4, type);
                ps.setString(5, reg);
                ps.setInt(6, Integer.parseInt(year));
                ps.setString(7, color);
                ps.setString(8, fuel);
                ps.setInt(9, Integer.parseInt(seats));
                ps.setString(10, image);
                ps.setBigDecimal(11, new java.math.BigDecimal(rate));
                ps.setString(12, status);
                int result = ps.executeUpdate();
                ps.close();
                conn.close();

                if (result > 0) {
                    showPostAddOptions();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add vehicle. Try again!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            }
        });

        btnBack.addActionListener(e -> {
            new AdminDashboardFrame();
            dispose();
        });

        setContentPane(panel);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Make full screen
        setVisible(true);
    }

    private void showPostAddOptions() {
        JFrame postFrame = new JFrame();
        postFrame.setTitle("Vehicle Added");
        postFrame.setSize(400, 200);
        postFrame.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(2, 1, 20, 20));
        RoundedButton btnAddAnother = new RoundedButton("Add Another Vehicle", new Color(119, 174, 38), Color.WHITE);
        RoundedButton btnDashboard = new RoundedButton("Go to Dashboard", new Color(40, 130, 201), Color.WHITE);

        btnAddAnother.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnDashboard.setFont(new Font("Segoe UI", Font.BOLD, 20));

        btnAddAnother.addActionListener(e -> {
            postFrame.dispose();
            new AddVehicleFrame();
            dispose();
        });

        btnDashboard.addActionListener(e -> {
            postFrame.dispose();
            new AdminDashboardFrame();
            dispose();
        });

        panel.add(btnAddAnother);
        panel.add(btnDashboard);
        postFrame.add(panel);
        postFrame.setVisible(true);
    }

    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            int width = getWidth();
            int height = getHeight();

            GradientPaint gp = new GradientPaint(0, 0, new Color(220, 240, 220), 0, height, new Color(180, 230,180));
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
            setFont(new Font("Segoe UI", Font.BOLD, 22));
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
        new AddVehicleFrame();
    }
}
