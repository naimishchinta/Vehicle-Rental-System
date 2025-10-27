package Vishal;

import javax.swing.*;
import java.awt.*;

public class PaymentDialog extends JDialog {

    // First: define the RoundedButton class before using it
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
            setFont(new Font("Segoe UI", Font.BOLD, 20));
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
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.dispose();
            super.paintComponent(g);
        }
        @Override
        public void setContentAreaFilled(boolean b) {}
    }

    // Next: custom gradient panel for welcome theme
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

    public PaymentDialog(Window owner) {
        super(owner, "Payment Method", ModalityType.APPLICATION_MODAL);
        setSize(400, 350);
        setLocationRelativeTo(owner);

        GradientPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblChoose = new JLabel("Select Payment Method:");
        lblChoose.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblChoose, gbc);

        String[] options = {"Credit Card", "Debit Card", "UPI", "Cash on Delivery"};
        JComboBox<String> cmbPayment = new JComboBox<>(options);
        cmbPayment.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridy = 1; 
        panel.add(cmbPayment, gbc);

        JLabel lblCardNumber = new JLabel("Card Number:");
        lblCardNumber.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JTextField txtCardNumber = new JTextField(20);
        JLabel lblExpiry = new JLabel("Expiry Date (MM/YY):");
        lblExpiry.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JTextField txtExpiry = new JTextField(10);
        JLabel lblCVV = new JLabel("CVV:");
        lblCVV.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JTextField txtCVV = new JTextField(5);

        JLabel lblUPI = new JLabel("UPI ID / Mobile:");
        lblUPI.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JTextField txtUPI = new JTextField(20);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblCardNumber, gbc);
        gbc.gridx = 1; 
        panel.add(txtCardNumber, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(lblExpiry, gbc);
        gbc.gridx = 1; 
        panel.add(txtExpiry, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(lblCVV, gbc);
        gbc.gridx = 1; 
        panel.add(txtCVV, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(lblUPI, gbc);
        gbc.gridx = 1; 
        panel.add(txtUPI, gbc);

        lblCardNumber.setVisible(false);
        txtCardNumber.setVisible(false);
        lblExpiry.setVisible(false);
        txtExpiry.setVisible(false);
        lblCVV.setVisible(false);
        txtCVV.setVisible(false);
        lblUPI.setVisible(false);
        txtUPI.setVisible(false);

        cmbPayment.addActionListener(e -> {
            String selected = (String) cmbPayment.getSelectedItem();
            boolean isCard = selected.equals("Credit Card") || selected.equals("Debit Card");
            boolean isUPI = selected.equals("UPI");
            lblCardNumber.setVisible(isCard);
            txtCardNumber.setVisible(isCard);
            lblExpiry.setVisible(isCard);
            txtExpiry.setVisible(isCard);
            lblCVV.setVisible(isCard);
            txtCVV.setVisible(isCard);
            lblUPI.setVisible(isUPI);
            txtUPI.setVisible(isUPI);
            panel.revalidate();
            panel.repaint();
        });

        RoundedButton btnPay = new RoundedButton("Pay Now", new Color(119, 174, 38), Color.WHITE);
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(btnPay, gbc);

        btnPay.addActionListener(ev -> {
            String paymentType = (String) cmbPayment.getSelectedItem();
            if(paymentType.equals("Credit Card") || paymentType.equals("Debit Card")) {
                if(txtCardNumber.getText().isEmpty() || txtExpiry.getText().isEmpty() || txtCVV.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all card details.");
                    return;
                }
                JOptionPane.showMessageDialog(this, paymentType + " payment successful!");
            } else if(paymentType.equals("UPI")) {
                if(txtUPI.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter your UPI ID or Mobile Number.");
                    return;
                }
                JOptionPane.showMessageDialog(this, "UPI payment successful!");
            } else if(paymentType.equals("Cash on Delivery")) {
                JOptionPane.showMessageDialog(this, "Cash on Delivery selected. Please pay on vehicle handover.");
            }
            dispose();
        });

        setContentPane(panel);
        setVisible(true);
    }
}
