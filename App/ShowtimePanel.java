import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ShowtimePanel extends JPanel {
    private final CinemaAppWindow app;
    private final JPanel listPanel;
    private final JLabel movieTitleLabel;
    private final JComboBox<DateModel> dateCombo;
    private boolean isUpdating;

    public ShowtimePanel(CinemaAppWindow app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(CinemaAppWindow.BG_COLOR);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(CinemaAppWindow.BG_COLOR);

        JButton btnBack = new JButton("<< Back");
        btnBack.addActionListener(e -> app.switchScreen("MOVIE"));
        topPanel.add(btnBack, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.setOpaque(false);
        movieTitleLabel = new JLabel("Showtimes", JLabel.CENTER);
        movieTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 20));

        dateCombo = new JComboBox<>();
        dateCombo.addActionListener(e -> {
            if (!isUpdating) {
                DateModel selected = (DateModel) dateCombo.getSelectedItem();
                app.getSession().setSelectedDate(selected);
                refreshScheduleList();
            }
        });

        centerPanel.add(movieTitleLabel);
        centerPanel.add(dateCombo);
        topPanel.add(centerPanel, BorderLayout.CENTER);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(topPanel, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(CinemaAppWindow.BG_COLOR);
        listPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        add(new JScrollPane(listPanel), BorderLayout.CENTER);
    }

    public void refreshData() {
        Product m = app.getSession().getSelectedMovie();
        if (m != null) {
            movieTitleLabel.setText("Showtimes: " + m.getTitle());
        } else {
            movieTitleLabel.setText("Showtimes");
        }

        isUpdating = true;
        dateCombo.removeAllItems();
        List<DateModel> dates = DateManager.getAllDates();
        for (DateModel d : dates) {
            dateCombo.addItem(d);
        }
        if (!dates.isEmpty()) {
            dateCombo.setSelectedIndex(0);
            app.getSession().setSelectedDate(dates.get(0));
        } else {
            app.getSession().setSelectedDate(null);
        }
        isUpdating = false;

        refreshScheduleList();
    }

    private void refreshScheduleList() {
        listPanel.removeAll();
        app.getSession().setSelectedSchedule(null);
        app.getSession().setSelectedTime("");
        app.getSession().setTheaterName("");

        Product m = app.getSession().getSelectedMovie();
        DateModel date = app.getSession().getSelectedDate();

        if (m == null) {
            listPanel.add(new JLabel("Please select a movie"));
        } else if (date == null) {
            listPanel.add(new JLabel("No dates available"));
        } else {
            List<Schedule> schedules = ScheduleManager.getSchedulesByDate(date.getId());
            boolean hasSchedule = false;
            for (Schedule s : schedules) {
                if (!s.getProductId().equals(m.getId())) {
                    continue;
                }
                hasSchedule = true;
                JPanel row = createShowtimeRow(s);
                listPanel.add(row);
                listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
            if (!hasSchedule) {
                listPanel.add(new JLabel("No showtimes for selected date"));
            }
        }

        revalidate();
        repaint();
    }

    private JPanel createShowtimeRow(Schedule schedule) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(java.awt.Color.WHITE);
        p.setPreferredSize(new Dimension(600, 60));
        p.setMaximumSize(new Dimension(600, 60));
        p.setBorder(new LineBorder(java.awt.Color.LIGHT_GRAY, 1, true));

        Product m = app.getSession().getSelectedMovie();
        double price = schedule.getPrice();
        if (price <= 0 && m != null) {
            price = m.getPrice();
        }

        JLabel info = new JLabel("  Round " + schedule.getTime() + "  |  " + schedule.getTheater() + "  |  " + price + " THB");
        info.setFont(new Font("Tahoma", Font.PLAIN, 16));

        JButton btn = new JButton("Select");
        btn.setBackground(CinemaAppWindow.PRIMARY_COLOR);
        btn.setForeground(java.awt.Color.WHITE);
        btn.addActionListener(e -> {
            app.getSession().setSelectedSchedule(schedule);
            app.getSession().setSelectedTime(schedule.getTime());
            app.getSession().setTheaterName(schedule.getTheater());
            app.switchScreen("SEAT");
        });

        p.add(info, BorderLayout.CENTER);
        p.add(btn, BorderLayout.EAST);
        return p;
    }
}
