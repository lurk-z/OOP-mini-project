import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CinemaAppWindow extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final BookingSession currentSession;

    private final MovieSelectionPanel moviePanel;
    private final ShowtimePanel showtimePanel;
    private final SeatSelectionPanel seatPanel;
    private final PaymentPanel paymentPanel;

    public static final Color PRIMARY_COLOR = Color.decode("#BC00A3");
    public static final Color BG_COLOR = Color.decode("#F5F7FA");

    public CinemaAppWindow() {
        setTitle("Cinema Booking System");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            DatabaseManager.getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
        }

        currentSession = new BookingSession();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        moviePanel = new MovieSelectionPanel(this);
        showtimePanel = new ShowtimePanel(this);
        seatPanel = new SeatSelectionPanel(this);
        paymentPanel = new PaymentPanel(this);

        mainPanel.add(moviePanel, "MOVIE");
        mainPanel.add(showtimePanel, "SHOWTIME");
        mainPanel.add(seatPanel, "SEAT");
        mainPanel.add(paymentPanel, "PAYMENT");

        add(mainPanel);
        cardLayout.show(mainPanel, "MOVIE");
    }

    public void switchScreen(String screenName) {
        if ("SHOWTIME".equals(screenName)) {
            showtimePanel.refreshData();
        } else if ("SEAT".equals(screenName)) {
            seatPanel.refreshData();
        } else if ("PAYMENT".equals(screenName)) {
            paymentPanel.refreshData();
        } else if ("MOVIE".equals(screenName)) {
            currentSession.clear();
            moviePanel.refreshData();
        }
        cardLayout.show(mainPanel, screenName);
    }

    public BookingSession getSession() {
        return currentSession;
    }
}
