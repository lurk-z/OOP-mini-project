import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BookingSession {
    private Product selectedMovie;
    private Schedule selectedSchedule;
    private DateModel selectedDate;
    private String selectedTime = "";
    private String theaterName = "";
    private final Set<String> selectedSeats = new HashSet<>();

    public void clear() {
        selectedMovie = null;
        selectedSchedule = null;
        selectedDate = null;
        selectedTime = "";
        theaterName = "";
        selectedSeats.clear();
    }

    public Product getSelectedMovie() {
        return selectedMovie;
    }

    public void setSelectedMovie(Product selectedMovie) {
        this.selectedMovie = selectedMovie;
    }

    public Schedule getSelectedSchedule() {
        return selectedSchedule;
    }

    public void setSelectedSchedule(Schedule selectedSchedule) {
        this.selectedSchedule = selectedSchedule;
    }

    public DateModel getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(DateModel selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }

    public String getTheaterName() {
        return theaterName;
    }

    public void setTheaterName(String theaterName) {
        this.theaterName = theaterName;
    }

    public Set<String> getSelectedSeats() {
        return Collections.unmodifiableSet(selectedSeats);
    }

    public void clearSelectedSeats() {
        selectedSeats.clear();
    }

    public void addSelectedSeat(String seat) {
        selectedSeats.add(seat);
    }

    public void removeSelectedSeat(String seat) {
        selectedSeats.remove(seat);
    }

    public double getSeatPrice() {
        if (selectedSchedule != null && selectedSchedule.getPrice() > 0) {
            return selectedSchedule.getPrice();
        }
        if (selectedMovie != null) {
            return selectedMovie.getPrice();
        }
        return 0;
    }

    public double getTotalPrice() {
        return selectedSeats.size() * getSeatPrice();
    }
}
