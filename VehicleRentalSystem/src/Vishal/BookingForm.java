package Vishal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingForm extends JFrame {
    private int userId;
    private DefaultTableModel model;
    private JTable vehicleTable;
    private JTextField txtFromDate, txtToDate, txtLocation;
    private JComboBox<String> cmbVehicleType;

    public BookingForm(int userId) {
        this.userId = userId;

        setTitle("Book Your Vehicle");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GradientPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Back button at top-left with arrow mark directing to MainMenu
        JButton btnBack = new JButton("\u2190 Back");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btnBack.setForeground(new Color(40, 40, 55));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnBack, gbc);

        btnBack.addActionListener(e -> {
            new MainMenu(userId); // Redirect to MainMenu with current userId
            dispose();
        });

        JLabel lblHeading = new JLabel("Vehicle Booking");
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblHeading.setForeground(new Color(40, 40, 55));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblHeading, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblLocation = new JLabel("Pickup Location:");
        lblLocation.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblLocation, gbc);

        txtLocation = new JTextField(20);
        txtLocation.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(txtLocation, gbc);

        JLabel lblFromDate = new JLabel("From Date (yyyy-MM-dd):");
        lblFromDate.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblFromDate, gbc);

        txtFromDate = new JTextField(15);
        txtFromDate.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(txtFromDate, gbc);

        JLabel lblToDate = new JLabel("To Date (yyyy-MM-dd):");
        lblToDate.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblToDate, gbc);

        txtToDate = new JTextField(15);
        txtToDate.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(txtToDate, gbc);

        JLabel lblVehicleType = new JLabel("Vehicle Type:");
        lblVehicleType.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lblVehicleType, gbc);

        String[] vehicleTypes = {"All", "Car", "Bike"};
        cmbVehicleType = new JComboBox<>(vehicleTypes);
        cmbVehicleType.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(cmbVehicleType, gbc);

        RoundedButton btnSearch = new RoundedButton("Search Available Vehicles", new Color(119, 174, 38), Color.WHITE);
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        panel.add(btnSearch, gbc);

        String[] columns = {"Image", "Vehicle ID", "Model", "Type", "Location", "Year", "Rate/Day", "Status", "Registration No", "Color", "Fuel Type", "Seats", "Image URL"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return ImageIcon.class;
                return Object.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        vehicleTable = new JTable(model);
        vehicleTable.setRowHeight(80);
        vehicleTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        vehicleTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        vehicleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int[] columnWidths = {90, 80, 100, 80, 100, 70, 90, 88, 120, 80, 92, 72, 130};
        for (int i = 0; i < columnWidths.length; i++) {
            vehicleTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
        vehicleTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setPreferredSize(new Dimension(1050, 250));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        panel.add(scrollPane, gbc);

        RoundedButton btnBook = new RoundedButton("Book Selected Vehicle", new Color(40, 130, 201), Color.WHITE);
        btnBook.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        panel.add(btnBook, gbc);

        btnBook.addActionListener(ev -> {
            int rowIdx = vehicleTable.getSelectedRow();
            if (rowIdx < 0) {
                JOptionPane.showMessageDialog(this, "Please select a vehicle to book.");
                return;
            }
            int vehicleId = (int) model.getValueAt(rowIdx, 1);
            String fromDateValue = txtFromDate.getText().trim();
            String toDateValue = txtToDate.getText().trim();

            if (fromDateValue.isEmpty() || toDateValue.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter From and To dates.");
                return;
            }
            BookingDetailsDialog dialog = new BookingDetailsDialog(this, vehicleId, fromDateValue, toDateValue);
            dialog.setVisible(true);
        });

        btnSearch.addActionListener(e -> {
            model.setRowCount(0);

            String location = txtLocation.getText().trim().toLowerCase();
            String fromDate = txtFromDate.getText().trim();
            String toDate = txtToDate.getText().trim();
            String vehicleType = ((String) cmbVehicleType.getSelectedItem()).toLowerCase();

            if (location.isEmpty() || fromDate.isEmpty() || toDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all search fields.");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            try {
                Date d1 = sdf.parse(fromDate);
                Date d2 = sdf.parse(toDate);
                if (d1.after(d2)) {
                    JOptionPane.showMessageDialog(this, "'From Date' must be before 'To Date'.");
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter dates in valid yyyy-MM-dd format.");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                StringBuilder sql = new StringBuilder("SELECT image_url, vehicle_id, model, type, location, year, rate_per_day, status, registration_no, color, fuel_type, seating_capacity, image_url " +
                        "FROM vehicles WHERE LOWER(status) = 'available' AND LOWER(location) LIKE ? ");
                if (!vehicleType.equals("all")) {
                    sql.append("AND LOWER(type) = ? ");
                }
                PreparedStatement ps = conn.prepareStatement(sql.toString());
                ps.setString(1, "%" + location + "%");
                if (!vehicleType.equals("all")) {
                    ps.setString(2, vehicleType);
                }
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String imagePath = rs.getString("image_url");
                    ImageIcon imgIcon = getThumbnailIcon(imagePath, 70, 80);

                    model.addRow(new Object[]{
                            imgIcon,
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
                            rs.getString("image_url")
                    });
                }
                rs.close();
                ps.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });

        setContentPane(panel);

        // Maximize the JFrame to full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setVisible(true);
    }

    private ImageIcon getThumbnailIcon(String path, int w, int h) {
        try {
            Image image;
            if (path != null && path.startsWith("http")) {
                image = new ImageIcon(new java.net.URL(path)).getImage();
            } else if (path != null) {
                image = new ImageIcon(path).getImage();
            } else {
                return new ImageIcon(new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB));
            }
            Image scaled = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception ex) {
            return new ImageIcon(new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB));
        }
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
            setFont(new Font("Segoe UI", Font.BOLD, 22));
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorderPainted(false);
            setBorder(BorderFactory.createEmptyBorder());

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
        SwingUtilities.invokeLater(() -> new BookingForm(1));
    }
}
