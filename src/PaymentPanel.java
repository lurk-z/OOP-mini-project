import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane; // เพิ่มการ import
import javax.swing.border.EmptyBorder;

public class PaymentPanel extends JPanel {
    private final CinemaAppWindow app;
    private final JLabel lblDetails;

    public PaymentPanel(CinemaAppWindow app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(java.awt.Color.WHITE);

        JLabel title = new JLabel("Payment Details", JLabel.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // --- ส่วนที่แก้ไข: สร้าง Panel สำหรับเนื้อหา ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(20, 100, 20, 100));
        centerPanel.setBackground(java.awt.Color.WHITE);

        lblDetails = new JLabel();
        lblDetails.setFont(new Font("Monospaced", Font.PLAIN, 16));
        centerPanel.add(lblDetails);

        // --- เพิ่ม JScrollPane ครอบ centerPanel ---
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null); // เอาเส้นขอบของ ScrollPane ออกเพื่อให้กลืนกับพื้นหลัง
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // ทำให้การเลื่อนเมาส์ลื่นไหลขึ้น
        add(scrollPane, BorderLayout.CENTER); // เพิ่ม scrollPane ลงในทิศ CENTER แทน centerPanel โดยตรง
        // ------------------------------------------

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        btnPanel.setBackground(java.awt.Color.WHITE);

        JButton btnCash = new JButton("Cash");
        JButton btnQR = new JButton("QR Code");
        JButton btnCard = new JButton("Credit Card");
        JButton btnHome = new JButton("Cancel / Home");

        Dimension btnSize = new Dimension(120, 40);
        btnCash.setPreferredSize(btnSize);
        btnQR.setPreferredSize(btnSize);
        btnCard.setPreferredSize(btnSize);

        ActionListener payAction = e -> {
            BookingSession s = app.getSession();
            if (s.getSelectedSchedule() == null || s.getSelectedSeats().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No seats selected.");
                return;
            }
            double seatPrice = s.getSeatPrice();
            for (String seat : s.getSelectedSeats()) {
                TransactionManager.addTransaction(s.getSelectedSchedule().getId(), seat, seatPrice);
            }
            JOptionPane.showMessageDialog(this, "Payment Successful! Thank you.");
            app.switchScreen("MOVIE");
        };

        btnCash.addActionListener(payAction);
        btnQR.addActionListener(payAction);
        btnCard.addActionListener(payAction);
        btnHome.addActionListener(e -> app.switchScreen("MOVIE"));

        btnPanel.add(btnCash);
        btnPanel.add(btnQR);
        btnPanel.add(btnCard);
        btnPanel.add(btnHome);

        add(btnPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {
        BookingSession s = app.getSession();
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<b>Movie:</b> ").append(s.getSelectedMovie() != null ? s.getSelectedMovie().getTitle() : "-").append("<br><br>");
        sb.append("<b>Date:</b> ").append(s.getSelectedDate() != null ? s.getSelectedDate().getDateStr() : "-").append("<br><br>");
        sb.append("<b>Theater:</b> ").append(s.getTheaterName()).append("<br><br>");
        sb.append("<b>Showtime:</b> ").append(s.getSelectedTime()).append("<br><br>");
        sb.append("<b>Seats:</b> ").append(s.getSelectedSeats().toString()).append("<br><br>");
        sb.append("<b>Total Seats:</b> ").append(s.getSelectedSeats().size()).append("<br><br>");
        sb.append("<hr>");
        sb.append("<h2>Total Price: ").append(s.getTotalPrice()).append(" THB</h2>");
        sb.append("</html>");

        lblDetails.setText(sb.toString());
    }
}