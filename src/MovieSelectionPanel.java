import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MovieSelectionPanel extends JPanel {
    private final CinemaAppWindow app;
    private final JPanel moviesContainer;

    public MovieSelectionPanel(CinemaAppWindow app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(CinemaAppWindow.BG_COLOR);

        JLabel header = new JLabel("Select Movie", JLabel.CENTER);
        header.setFont(new Font("Tahoma", Font.BOLD, 24));
        header.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        moviesContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        moviesContainer.setBackground(CinemaAppWindow.BG_COLOR);
        add(moviesContainer, BorderLayout.CENTER);

        refreshData();
    }

    public void refreshData() {
        moviesContainer.removeAll();

        List<Product> movies = ProductManager.getAllProducts();
        if (movies.isEmpty()) {
            JLabel empty = new JLabel("No movies available", JLabel.CENTER);
            empty.setFont(new Font("Tahoma", Font.PLAIN, 16));
            moviesContainer.add(empty);
        } else {
            for (Product m : movies) {
                moviesContainer.add(createMovieCard(m));
            }
        }

        revalidate();
        repaint();
    }

    private JPanel createMovieCard(Product m) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(150, 150));
        card.setLayout(new BorderLayout());
        card.setBorder(new LineBorder(java.awt.Color.LIGHT_GRAY, 1, true));
        card.setBackground(java.awt.Color.WHITE);

        JLabel imgPlaceholder = new JLabel(m.getTitle(), JLabel.CENTER);
        imgPlaceholder.setOpaque(true);
        imgPlaceholder.setBackground(java.awt.Color.DARK_GRAY);
        imgPlaceholder.setForeground(java.awt.Color.WHITE);
        imgPlaceholder.setPreferredSize(new Dimension(200, 150));
        card.add(imgPlaceholder, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(java.awt.Color.WHITE);

        // JLabel priceLabel = new JLabel("Price: " + m.getPrice() + " THB", JLabel.CENTER);ราคาหนนัง(มีทำไม?)

        JButton btnSelect = new JButton("Select");
        btnSelect.setBackground(CinemaAppWindow.PRIMARY_COLOR);
        btnSelect.setForeground(java.awt.Color.WHITE);
        btnSelect.setFocusPainted(false);

        btnSelect.addActionListener(e -> {
            app.getSession().setSelectedMovie(m);
            app.switchScreen("SHOWTIME");
        });

        // infoPanel.add(priceLabel);ส่วนเรียกใช้ราคาหนัง
        infoPanel.add(btnSelect);
        card.add(infoPanel, BorderLayout.SOUTH);

        return card;
    }
}
