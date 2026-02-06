import javax.swing.SwingUtilities;

public class CinemaApp {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new CinemaAppWindow().setVisible(true));
	}
}