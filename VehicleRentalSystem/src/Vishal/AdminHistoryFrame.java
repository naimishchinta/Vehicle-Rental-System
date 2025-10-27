package Vishal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AdminHistoryFrame extends JFrame {

    private final String DB_URL = "jdbc:mysql://localhost:3306/vehicle_rental"; // Change as necessary
    private final String DB_USERNAME = "root";  // your username
    private final String DB_PASSWORD = "Vikas@123"; // your password

    public AdminHistoryFrame() {
        setTitle("Booking History");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GradientPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());

        // Top panel for Back button and Heading
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false); // preserve gradient

        // Back button (top left)
        RoundedButton btnBack = new RoundedButton("← Back", new Color(60, 130, 120), Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnBack.setPreferredSize(new Dimension(120, 45));
        topPanel.add(btnBack, BorderLayout.WEST);

        JLabel lblHeading = new JLabel("All Booking History", SwingConstants.CENTER);
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblHeading.setForeground(new Color(40, 40, 55));
        lblHeading.setBorder(BorderFactory.createEmptyBorder(18, 0, 18, 0));
        topPanel.add(lblHeading, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Booking ID", "User ID", "Vehicle ID", "Start Date", "End Date", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 20));
        table.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Fetch data and populate table
        fetchBookings(tableModel);

        btnBack.addActionListener(e -> {
            new AdminDashboardFrame();
            dispose();
        });

        setContentPane(panel);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen
        setVisible(true);
    }

    private void fetchBookings(DefaultTableModel tableModel) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT booking_id, user_id, vehicle_id, start_date, end_date, status FROM bookings";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Object[] row = new Object[6];
                row[0] = rs.getInt("booking_id");
                row[1] = rs.getInt("user_id");
                row[2] = rs.getInt("vehicle_id");
                row[3] = rs.getDate("start_date");
                row[4] = rs.getDate("end_date");
                row[5] = rs.getString("status");
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching booking history:\n" + e.getMessage());
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
            setFont(new Font("Segoe UI", Font.BOLD, 18));
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
        new AdminHistoryFrame();
    }
}
