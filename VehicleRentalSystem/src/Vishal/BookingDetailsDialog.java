package Vishal;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.math.BigDecimal;
import java.sql.*;

public class BookingDetailsDialog extends JDialog {
    public BookingDetailsDialog(Frame owner, int vehicleId, String fromDate, String toDate) {
        super(owner, "Complete Your Booking", true);

        Object[] vehicleData = fetchVehicleData(vehicleId);
        if (vehicleData == null) {
            JOptionPane.showMessageDialog(this, "Vehicle data not found.");
            dispose();
            return;
        }

        GradientPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        JLabel lblImg = new JLabel((vehicleData[0] instanceof ImageIcon) ? (ImageIcon) vehicleData[0] : null);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridheight = 5; gbc.gridwidth = 1;
        panel.add(lblImg, gbc);
        gbc.gridheight = 1;

        String[] labels = {"Vehicle ID", "Model", "Type", "Location", "Year", "Rate Per Day", "Status", "Registration No", "Color", "Fuel Type", "Seats"};
        for (int i = 1; i <= 11; i++) {
            JLabel label = new JLabel(labels[i - 1] + ": " + vehicleData[i]);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            label.setForeground(new Color(40, 40, 55));
            gbc.gridx = 1; gbc.gridy = row++;
            panel.add(label, gbc);
        }
        row++;

        int tempNumDays = 1;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = df.parse(fromDate);
            Date d2 = df.parse(toDate);
            long diff = d2.getTime() - d1.getTime();
            tempNumDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
            if(tempNumDays < 1) tempNumDays = 1;
        } catch (Exception e) {
            tempNumDays = 1;
        }
        final int numDays = tempNumDays;

        JLabel lblDays = new JLabel("Number of Days:");
        lblDays.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDays.setForeground(new Color(40, 40, 55));
        JTextField txtDays = new JTextField(String.valueOf(numDays), 10);
        txtDays.setEditable(false);
        txtDays.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(lblDays, gbc);
        gbc.gridx = 1;
        panel.add(txtDays, gbc);
        row++;

        BigDecimal rate = (BigDecimal) vehicleData[6];
        final BigDecimal total = rate.multiply(new BigDecimal(numDays));
        JLabel lblPrice = new JLabel("Total Price:");
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPrice.setForeground(new Color(40, 40, 55));
        JLabel lblPriceAmount = new JLabel(total.toString());
        lblPriceAmount.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblPriceAmount.setForeground(new Color(40, 40, 55));

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(lblPrice, gbc);
        gbc.gridx = 1;
        panel.add(lblPriceAmount, gbc);
        row++;

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblName.setForeground(new Color(40, 40, 55));
        JTextField txtName = new JTextField(20);
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(lblName, gbc);
        gbc.gridx = 1;
        panel.add(txtName, gbc);
        row++;

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblEmail.setForeground(new Color(40, 40, 55));
        JTextField txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(lblEmail, gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);
        row++;

        JLabel lblAadhar = new JLabel("Aadhar Number:");
        lblAadhar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblAadhar.setForeground(new Color(40, 40, 55));
        JTextField txtAadhar = new JTextField(20);
        txtAadhar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(lblAadhar, gbc);
        gbc.gridx = 1;
        panel.add(txtAadhar, gbc);
        row++;

        JLabel lblLicense = new JLabel("License Number:");
        lblLicense.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLicense.setForeground(new Color(40, 40, 55));
        JTextField txtLicense = new JTextField(20);
        txtLicense.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(lblLicense, gbc);
        gbc.gridx = 1;
        panel.add(txtLicense, gbc);
        row++;

        RoundedButton btnConfirm = new RoundedButton("Confirm Booking", new Color(119, 174, 38), Color.WHITE);
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(btnConfirm, gbc);

        btnConfirm.addActionListener(ev -> {
            if (txtName.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty() ||
                    txtAadhar.getText().trim().isEmpty() || txtLicense.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields.");
                return;
            }
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO bookings " +
                        "(user_name, email, aadhar, license, vehicle_id, start_date, end_date, total_days, total_price, status, brand, registration_no, year, color, fuel_type, seating_capacity, image_url) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, txtName.getText().trim());
                ps.setString(2, txtEmail.getText().trim());
                ps.setString(3, txtAadhar.getText().trim());
                ps.setString(4, txtLicense.getText().trim());
                ps.setInt(5, vehicleId);
                ps.setString(6, fromDate);
                ps.setString(7, toDate);
                ps.setInt(8, numDays);
                ps.setBigDecimal(9, total);
                ps.setString(10, (String) vehicleData[7]);
                ps.setString(11, (String) vehicleData[2]);
                ps.setString(12, (String) vehicleData[8]);
                ps.setObject(13, vehicleData[5]);
                ps.setString(14, (String) vehicleData[9]);
                ps.setString(15, (String) vehicleData[10]);
                ps.setObject(16, vehicleData[11]);
                ps.setString(17, (String) vehicleData[12]);
                ps.executeUpdate();
                ps.close();

                PaymentDialog paymentDialog = new PaymentDialog(this);
                paymentDialog.setVisible(true);

                JOptionPane.showMessageDialog(this, "Booking confirmed!\nTotal Price: " + lblPriceAmount.getText());
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
            }
        });

        setContentPane(new JScrollPane(panel));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private Object[] fetchVehicleData(int vehicleId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT vehicle_id, model, type, location, year, rate_per_day, rate_per_day, status, registration_no, color, fuel_type, seating_capacity, image_url FROM vehicles WHERE vehicle_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ImageIcon icon = null;
                String imageUrl = rs.getString("image_url");
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    try {
                        if (imageUrl.startsWith("http")) {
                            ImageIcon rawIcon = new ImageIcon(new java.net.URL(imageUrl));
                            Image img = rawIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(img);
                        } else {
                            ImageIcon rawIcon = new ImageIcon(imageUrl);
                            Image img = rawIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(img);
                        }
                    } catch (Exception e) {
                        icon = null;
                    }
                }
                return new Object[] {
                        icon,
                        rs.getInt("vehicle_id"),
                        rs.getString("model"),
                        rs.getString("type"),
                        rs.getString("location"),
                        rs.getInt("year"),
                        rs.getBigDecimal("rate_per_day"),
                        rs.getString("status"),
                        rs.getString("registration_no"),
                        rs.getString("color"),
                        rs.getString("fuel_type"),
                        rs.getInt("seating_capacity"),
                        imageUrl
                };
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading vehicle data: " + ex.getMessage());
        }
        return null;
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
        new BookingDetailsDialog(null, 1, "2025-10-20", "2025-10-22");
    }
}
