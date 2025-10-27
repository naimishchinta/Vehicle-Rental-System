package Vishal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class AvailableVehiclesFrame extends JFrame {
    private DefaultTableModel model;
    private JTable table;

    public AvailableVehiclesFrame() {
        setTitle("Available Vehicles");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        GradientPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());
        setContentPane(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 30, 20, 30);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;

        JLabel lblHeading = new JLabel("Available Vehicles", SwingConstants.CENTER);
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblHeading.setForeground(new Color(40, 40, 55));
        panel.add(lblHeading, gbc);

        // Table setup
        model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 8) {
                    return ImageIcon.class;
                }
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        model.setColumnIdentifiers(new Object[] {
            "Vehicle ID", "Model", "Type", "Registration No", "Year",
            "Color", "Fuel Type", "Seating Capacity", "Image", "Rate Per Day", "Available"
        });

        table = new JTable(model);
        table.setRowHeight(90);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.setBackground(Color.WHITE);

        table.getColumnModel().getColumn(8).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof ImageIcon) {
                    JLabel label = new JLabel();
                    label.setIcon((ImageIcon) value);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    return label;
                } else {
                    return new JLabel("No Image");
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        gbc.gridy = 1; gbc.weighty = 1;
        panel.add(scrollPane, gbc);

        RoundedButton btnRefresh = new RoundedButton("Refresh", new Color(119, 174, 38), Color.WHITE);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnRefresh.setPreferredSize(new Dimension(200, 55));

        gbc.gridy = 2; gbc.weighty = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnRefresh, gbc);

        btnRefresh.addActionListener(e -> loadVehicleData());

        loadVehicleData();

        setVisible(true);
    }

    private void loadVehicleData() {
        model.setRowCount(0); // Clear existing data
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM vehicles";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ImageIcon icon = null;
                String imageUrl = rs.getString("image_url");
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    try {
                        ImageIcon rawIcon = new ImageIcon(new java.net.URL(imageUrl));
                        Image img = rawIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        icon = new ImageIcon(img);
                    } catch (Exception ex) {
                        icon = null;
                    }
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
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
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
            setFont(new Font("Segoe UI", Font.BOLD, 24));
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
        new AvailableVehiclesFrame();
    }
}
