package Vishal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;

public class AdminDashboardFrame extends JFrame {

    public AdminDashboardFrame() {
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                int width = getWidth();
                int height = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(200, 230, 200), 0, height, new Color(150, 210, 150));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
                g2d.dispose();
            }
        };
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 25, 25, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        RoundedButton btnBack = new RoundedButton("← Back", new Color(60, 130, 120), Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.setPreferredSize(new Dimension(70, 32));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnBack, gbc);

        btnBack.addActionListener(e -> {
            new AdminLoginFrame();
            dispose();
        });

        JLabel lblHeading = new JLabel("Admin Dashboard");
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblHeading.setForeground(new Color(30, 30, 45));
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblHeading, gbc);
        gbc.gridwidth = 1;

        RoundedButton btnBookingHistory = new RoundedButton("Check Booking History", new Color(85, 140, 30), Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnBookingHistory, gbc);

        RoundedButton btnAddVehicle = new RoundedButton("Add Vehicle", new Color(85, 140, 30), Color.WHITE);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(btnAddVehicle, gbc);

        RoundedButton btnDeleteVehicle = new RoundedButton("Delete Vehicle", new Color(165, 120, 30), Color.WHITE);
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(btnDeleteVehicle, gbc);

        RoundedButton btnViewVehicles = new RoundedButton("View All Vehicles", new Color(85, 140, 30), Color.WHITE);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(btnViewVehicles, gbc);

        btnBookingHistory.addActionListener(e -> {
            new AdminHistoryFrame();
            dispose();
        });

        btnAddVehicle.addActionListener(e -> {
            new AddVehicleFrame();
            dispose();
        });

        btnViewVehicles.addActionListener(e -> {
            showViewVehiclesDialog();
        });

        btnDeleteVehicle.addActionListener(e -> {
            showDeleteDialog();
        });

        setContentPane(panel);
        setVisible(true);
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
            setFont(new Font("Segoe UI", Font.BOLD, 28));
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorderPainted(false);
            setBorder(BorderFactory.createEmptyBorder());

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    setForeground(fgColor);
                    setBackground(hoverColor);
                    repaint();
                }
                public void mouseExited(MouseEvent evt) {
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
        public void setContentAreaFilled(boolean b) {}
    }

    private void showViewVehiclesDialog() {
        JDialog dialog = new JDialog(this, "All Vehicles with Edit Panel", true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setBounds(0, 0, screenSize.width, screenSize.height);

        String[] columns = { "Vehicle ID", "Model", "Location", "Type", "Registration No", "Year", "Color", "Fuel Type", "Seating Capacity", "Rate/Day", "Image", "Status", "Image Path" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 10) ? ImageIcon.class : Object.class;
            }
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(80);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.getColumnModel().getColumn(10).setCellRenderer((table1, value, isSelected, hasFocus, row, col) -> {
            if (value instanceof ImageIcon) {
                JLabel label = new JLabel();
                label.setIcon((ImageIcon) value);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            } else {
                return new JLabel("No Image");
            }
        });

        String sql = "SELECT vehicle_id, model, location, type, registration_no, year, color, fuel_type, seating_capacity, rate_per_day, image_url, status FROM vehicles";
        // Populate table
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ImageIcon icon = null;
                String imageUrl = rs.getString("image_url");
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    try {
                        if (imageUrl.startsWith("http")) {
                            ImageIcon rawIcon = new ImageIcon(new URL(imageUrl));
                            Image img = rawIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(img);
                        } else {
                            ImageIcon rawIcon = new ImageIcon(imageUrl);
                            Image img = rawIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(img);
                        }
                    } catch (Exception ex) { icon = null;}
                }
                model.addRow(new Object[] {
                        rs.getInt("vehicle_id"),
                        rs.getString("model"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getString("registration_no"),
                        rs.getInt("year"),
                        rs.getString("color"),
                        rs.getString("fuel_type"),
                        rs.getInt("seating_capacity"),
                        rs.getBigDecimal("rate_per_day"),
                        icon,
                        rs.getString("status"),
                        imageUrl
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(dialog, "Error loading vehicles: " + ex.getMessage());
        }
        JScrollPane scroll = new JScrollPane(table);

        // EDIT PANEL
        JPanel editPanel = new JPanel(new GridBagLayout());
        editPanel.setBorder(BorderFactory.createTitledBorder("Edit Vehicle Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int y = 0;

        JTextField txtVehicleId = new JTextField(10); txtVehicleId.setEditable(false);
        JTextField txtModel = new JTextField(15);
        JTextField txtLocation = new JTextField(15);
        JTextField txtType = new JTextField(15);
        JTextField txtRegistrationNo = new JTextField(15);
        JTextField txtYear = new JTextField(15);
        JTextField txtColor = new JTextField(15);
        JTextField txtFuelType = new JTextField(15);
        JTextField txtSeatingCapacity = new JTextField(15);
        JTextField txtRatePerDay = new JTextField(15);
        JTextField txtStatus = new JTextField(15);
        JTextField txtImageUrl = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = y; editPanel.add(new JLabel("Vehicle ID:"), gbc);
        gbc.gridx = 1; editPanel.add(txtVehicleId, gbc); y++;
        gbc.gridx = 0; gbc.gridy = y; editPanel.add(new JLabel("Model:"), gbc);
        gbc.gridx = 1; editPanel.add(txtModel, gbc); y++;
        gbc.gridx = 0; gbc.gridy = y; editPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1; editPanel.add(txtLocation, gbc); y++;
        gbc.gridx = 0; gbc.gridy = y; editPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; editPanel.add(txtType, gbc); y++;
        gbc.gridx = 0; gbc.gridy = y; editPanel.add(new JLabel("Registration No:"), gbc);
        gbc.gridx = 1; editPanel.add(txtRegistrationNo, gbc); y++;
        gbc.gridx = 0; gbc.gridy = y; editPanel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 1; editPanel.add(txtYear, gbc); y++;
        gbc.gridx = 0; gbc.gridy = y; editPanel.add(new JLabel("Color:"), gbc);
        gbc.gridx = 1; editPanel.add(txtColor, gbc); y++;
        gbc.gridx = 0; gbc.gridy = y; editPanel.add(new JLabel("Fuel Type:"), gbc);
        gbc.gridx = 1; editPanel.add(txtFuelType, gbc); y++;
        gbc.gridx = 0; gbc.gridy = y; editPanel.add(new JLabel("Seating Capacity:"), gbc);
        gbc.gridx = 1; editPanel.add(txtSeatingCapacity, gbc); y++;
        gbc.gridx = 0; gbc.gridy = y; editPanel.add(new JLabel("Rate Per Day:"), gbc);
        gbc.gridx = 1; editPanel.add(txtRatePerDay, gbc); y++;
        gbc.gridx = 0; gbc.gridy = y; editPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; editPanel.add(txtStatus, gbc); y++;
        gbc.gridx = 0; gbc.gridy = y; editPanel.add(new JLabel("Image URL:"), gbc);
        gbc.gridx = 1; editPanel.add(txtImageUrl, gbc); y++;

        JButton btnSave = new JButton("Save Changes");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSave.setBackground(new Color(85,140,30));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        editPanel.add(btnSave, gbc);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, editPanel);
        splitPane.setDividerLocation(screenSize.width / 2);
        dialog.add(splitPane, BorderLayout.CENTER);

        // Table row to edit panel
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    txtVehicleId.setText(String.valueOf(model.getValueAt(selectedRow, 0)));
                    txtModel.setText((String)model.getValueAt(selectedRow, 1));
                    txtLocation.setText((String)model.getValueAt(selectedRow, 2));
                    txtType.setText((String)model.getValueAt(selectedRow, 3));
                    txtRegistrationNo.setText((String)model.getValueAt(selectedRow, 4));
                    txtYear.setText(String.valueOf(model.getValueAt(selectedRow, 5)));
                    txtColor.setText((String)model.getValueAt(selectedRow, 6));
                    txtFuelType.setText((String)model.getValueAt(selectedRow, 7));
                    txtSeatingCapacity.setText(String.valueOf(model.getValueAt(selectedRow, 8)));
                    txtRatePerDay.setText(model.getValueAt(selectedRow, 9).toString());
                    txtStatus.setText((String)model.getValueAt(selectedRow, 11));
                    txtImageUrl.setText((String)model.getValueAt(selectedRow, 12));
                }
            }
        });

        btnSave.addActionListener(e -> {
            try {
                int vehicleId = Integer.parseInt(txtVehicleId.getText().trim());
                String modelName = txtModel.getText().trim();
                String location = txtLocation.getText().trim();
                String type = txtType.getText().trim();
                String regNo = txtRegistrationNo.getText().trim();
                int year = Integer.parseInt(txtYear.getText().trim());
                String color = txtColor.getText().trim();
                String fuelType = txtFuelType.getText().trim();
                int seatingCapacity = Integer.parseInt(txtSeatingCapacity.getText().trim());
                BigDecimal ratePerDay = new BigDecimal(txtRatePerDay.getText().trim());
                String status = txtStatus.getText().trim();
                String imageUrl = txtImageUrl.getText().trim();

                String updateSQL = "UPDATE vehicles SET model=?, location=?, type=?, registration_no=?, year=?, color=?, fuel_type=?, seating_capacity=?, rate_per_day=?, status=?, image_url=? WHERE vehicle_id=?";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                    ps.setString(1, modelName);
                    ps.setString(2, location);
                    ps.setString(3, type);
                    ps.setString(4, regNo);
                    ps.setInt(5, year);
                    ps.setString(6, color);
                    ps.setString(7, fuelType);
                    ps.setInt(8, seatingCapacity);
                    ps.setBigDecimal(9, ratePerDay);
                    ps.setString(10, status);
                    ps.setString(11, imageUrl);
                    ps.setInt(12, vehicleId);

                    int rows = ps.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(dialog, "Vehicle updated successfully.");
                        int selRow = table.getSelectedRow();
                        model.setValueAt(modelName, selRow, 1);
                        model.setValueAt(location, selRow, 2);
                        model.setValueAt(type, selRow, 3);
                        model.setValueAt(regNo, selRow, 4);
                        model.setValueAt(year, selRow, 5);
                        model.setValueAt(color, selRow, 6);
                        model.setValueAt(fuelType, selRow, 7);
                        model.setValueAt(seatingCapacity, selRow, 8);
                        model.setValueAt(ratePerDay, selRow, 9);
                        model.setValueAt(status, selRow, 11);
                        model.setValueAt(imageUrl, selRow, 12);

                        try {
                            ImageIcon icon = null;
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                if (imageUrl.startsWith("http")) {
                                    ImageIcon rawIcon = new ImageIcon(new URL(imageUrl));
                                    Image img = rawIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                                    icon = new ImageIcon(img);
                                } else {
                                    ImageIcon rawIcon = new ImageIcon(imageUrl);
                                    Image img = rawIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                                    icon = new ImageIcon(img);
                                }
                            }
                            model.setValueAt(icon, selRow, 10);
                        } catch (Exception ex) {}
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Update failed. Please try again.");
                    }
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for year, seating capacity and rate per day.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    private void showDeleteDialog() {
        JDialog dialog = new JDialog(this, "Delete Vehicle", true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setBounds(0, 0, screenSize.width, screenSize.height);
        dialog.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton btnBack = new JButton("← Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setPreferredSize(new Dimension(70, 32));
        btnBack.setFocusPainted(false);
        btnBack.setBackground(new Color(60, 130, 120));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> dialog.dispose());
        topPanel.add(btnBack, BorderLayout.WEST);

        JLabel header = new JLabel("Delete Vehicle - Select Row and Press Delete", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        topPanel.add(header, BorderLayout.CENTER);
        dialog.add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        String[] columns = { "Vehicle ID", "Model", "Type", "Registration No", "Year", "Color", "Fuel Type", "Seating Capacity", "Image", "Rate Per Day", "Available" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 8) ? ImageIcon.class : Object.class;
            }
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(80);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.getColumnModel().getColumn(8).setCellRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            if (value instanceof ImageIcon) {
                JLabel label = new JLabel();
                label.setIcon((ImageIcon) value);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            } else {
                return new JLabel("No Image");
            }
        });

        String sql = "SELECT vehicle_id, model, type, registration_no, year, color, fuel_type, seating_capacity, image_url, rate_per_day, available FROM vehicles";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ImageIcon icon = null;
                String imageUrl = rs.getString("image_url");
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    try {
                        ImageIcon rawIcon = new ImageIcon(new URL(imageUrl));
                        Image img = rawIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                        icon = new ImageIcon(img);
                    } catch (Exception ex) { icon = null;}
                }
                model.addRow(new Object[] {
                        rs.getInt("vehicle_id"),
                        rs.getString("model"),
                        rs.getString("type"),
                        rs.getString("registration_no"),
                        rs.getInt("year"),
                        rs.getString("color"),
                        rs.getString("fuel_type"),
                        rs.getInt("seating_capacity"),
                        icon,
                        rs.getBigDecimal("rate_per_day"),
                        rs.getString("available")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(dialog, "Error loading vehicles: " + ex.getMessage());
        }
        JScrollPane scroll = new JScrollPane(table);
        mainPanel.add(scroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(80, 20, 80, 20));

        JButton btnDelete = new JButton("Delete Selected Vehicle");
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnDelete.setBackground(new Color(165, 120, 30));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(btnDelete);
        buttonPanel.add(Box.createVerticalGlue());

        mainPanel.add(buttonPanel, BorderLayout.EAST);
        dialog.add(mainPanel, BorderLayout.CENTER);

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Select a vehicle to delete.");
                return;
            }
            int vehicleId = (int) model.getValueAt(row, 0);

            JOptionPane optionPane = new JOptionPane("Delete vehicle ID " + vehicleId + "?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
            JDialog confirmDialog = optionPane.createDialog(dialog, "Confirm");

            int offsetX = -200; int offsetY = -100;
            Point parentLoc = dialog.getLocationOnScreen();
            int x = Math.max(parentLoc.x + dialog.getWidth() / 2 + offsetX, 0);
            int y = Math.max(parentLoc.y + dialog.getHeight() / 2 + offsetY, 0);
            confirmDialog.setLocation(x, y);

            confirmDialog.setVisible(true);

            Object selectedValue = optionPane.getValue();
            if (selectedValue != null && selectedValue.equals(JOptionPane.YES_OPTION)) {
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement("DELETE FROM vehicles WHERE vehicle_id=?")) {
                    ps.setInt(1, vehicleId);
                    int updated = ps.executeUpdate();
                    if (updated > 0) {
                        JOptionPane.showMessageDialog(dialog, "Vehicle deleted successfully!");
                        model.removeRow(row);
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Delete failed.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Delete error: " + ex.getMessage());
                }
            }
        });
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new AdminDashboardFrame();
    }
}
