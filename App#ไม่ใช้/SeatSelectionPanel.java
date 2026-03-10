import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

public class SeatSelectionPanel extends JPanel {
    private final CinemaAppWindow app;
    private final JPanel gridPanel;
    private final JLabel statusLabel;
    private final JButton btnConfirm;

    public SeatSelectionPanel(CinemaAppWindow app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(CinemaAppWindow.BG_COLOR);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(CinemaAppWindow.BG_COLOR);
        JButton btnBack = new JButton("<< Back");
        btnBack.addActionListener(e -> app.switchScreen("SHOWTIME"));

        JLabel title = new JLabel("Select Seats", JLabel.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 18));

        topPanel.add(btnBack, BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(topPanel, BorderLayout.NORTH);

        JPanel screenArea = new JPanel();
        screenArea.setPreferredSize(new java.awt.Dimension(750, 30));
        screenArea.setBackground(CinemaAppWindow.PRIMARY_COLOR);
        JLabel screenTxt = new JLabel("SCREEN", JLabel.CENTER);
        screenTxt.setForeground(java.awt.Color.WHITE);
        screenArea.add(screenTxt);
        add(screenArea, BorderLayout.SOUTH);

        gridPanel = new JPanel(new GridLayout(4, 10, 10, 10));
        gridPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        gridPanel.setBackground(CinemaAppWindow.BG_COLOR);
        add(gridPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusLabel = new JLabel("Selected: 0 | Total: 0 THB  ");
        statusLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

        btnConfirm = new JButton("Confirm Seats");
        btnConfirm.setBackground(CinemaAppWindow.PRIMARY_COLOR);
        btnConfirm.setForeground(java.awt.Color.WHITE);
        btnConfirm.setEnabled(false);
        btnConfirm.addActionListener(e -> app.switchScreen("PAYMENT"));

        bottomPanel.add(statusLabel);
        bottomPanel.add(btnConfirm);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {
        gridPanel.removeAll();
        app.getSession().clearSelectedSeats();

        Schedule schedule = app.getSession().getSelectedSchedule();
        if (schedule == null) {
            statusLabel.setText("No showtime selected");
            btnConfirm.setEnabled(false);
            revalidate();
            repaint();
            return;
        }

        Set<String> soldSeats = new HashSet<>();
        for (Transaction t : TransactionManager.getTransactionsBySchedule(schedule.getId())) {
            soldSeats.add(t.getSeat());
        }

        updateStatus();

        String[] rows = {"A", "B", "C", "D"};
        for (String r : rows) {
            for (int i = 1; i <= 10; i++) {
                String seatId = r + i;
                JToggleButton btn = new JToggleButton(seatId);
                if (soldSeats.contains(seatId)) {
                    btn.setEnabled(false);
                    btn.setBackground(java.awt.Color.LIGHT_GRAY);
                } else {
                    btn.setBackground(java.awt.Color.GREEN);
                    btn.addActionListener(e -> {
                        if (btn.isSelected()) {
                            btn.setBackground(java.awt.Color.RED);
                            app.getSession().addSelectedSeat(seatId);
                        } else {
                            btn.setBackground(java.awt.Color.GREEN);
                            app.getSession().removeSelectedSeat(seatId);
                        }
                        updateStatus();
                    });
                }
                gridPanel.add(btn);
            }
        }
        revalidate();
        repaint();
    }

    private void updateStatus() {
        int count = app.getSession().getSelectedSeats().size();
        double total = app.getSession().getTotalPrice();
        statusLabel.setText("Selected: " + count + " | Total: " + total + " THB  ");
        btnConfirm.setEnabled(count > 0);
    }
}
