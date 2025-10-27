package Vishal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class MyBookingsFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JLabel imageLabel;

    public MyBookingsFrame(int userId) {
        setTitle("My Bookings");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        GradientPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout(20, 10));

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        JLabel lblHeading = new JLabel("My Bookings");
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblHeading.setForeground(new Color(40, 40, 55));
        topPanel.add(lblHeading);

        panel.add(topPanel, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("Booking ID");
        model.addColumn("Vehicle ID");
        model.addColumn("Model");
        model.addColumn("Type");
        model.addColumn("Brand");
        model.addColumn("Registration No");
        model.addColumn("Year");
        model.addColumn("Color");
        model.addColumn("Fuel");
        model.addColumn("Seats");
        model.addColumn("Start Date");
        model.addColumn("End Date");
        model.addColumn("Status");
        model.addColumn("Image Path");

        table.setRowHeight(32);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        // Set column widths for better readability
        int[] columnWidths = {90, 90, 130, 90, 110, 120, 70, 90, 85, 70, 110, 110, 120, 0};
        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
        // Hide image path column (last column)
        table.getColumnModel().getColumn(13).setMinWidth(0);
        table.getColumnModel().getColumn(13).setMaxWidth(0);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        panel.add(scrollPane, BorderLayout.CENTER);

        imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createTitledBorder("Vehicle Image"));
        imageLabel.setPreferredSize(new Dimension(320, 160));
        imageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        imageLabel.setBackground(new Color(240,240,240,0));
        panel.add(imageLabel, BorderLayout.SOUTH);

        fetchBookings(model, userId);

        table.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                String imagePath = (String) table.getValueAt(table.getSelectedRow(), 13);
                showBookingImage(imagePath);
            }
        });

        setContentPane(panel);
        setVisible(true);
    }

    private void fetchBookings(DefaultTableModel model, int userId) {
        String query = "SELECT b.booking_id, v.vehicle_id, v.model, v.type, " +
                "b.brand, b.registration_no, b.year, b.color, b.fuel_type, b.seating_capacity, " +
                "b.start_date, b.end_date, b.status, b.image_url " +
                "FROM bookings b LEFT JOIN vehicles v ON b.vehicle_id = v.vehicle_id " +
                "WHERE b.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("booking_id"),
                        rs.getInt("vehicle_id"),
                        rs.getString("model"),
                        rs.getString("type"),
                        rs.getString("brand"),
                        rs.getString("registration_no"),
                        rs.getInt("year"),
                        rs.getString("color"),
                        rs.getString("fuel_type"),
                        rs.getInt("seating_capacity"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("status"),
                        rs.getString("image_url")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading booking data: " + e.getMessage());
        }
    }

    private void showBookingImage(String path) {
        if (path == null || path.trim().isEmpty()) {
            imageLabel.setIcon(null);
            imageLabel.setText("No Image Available");
        } else {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(290, 150, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
            imageLabel.setText("");
        }
    }

    // Themed rounded corner gradient background
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

    public static void main(String[] args) {
        new MyBookingsFrame(1); // example user id
    }
}
