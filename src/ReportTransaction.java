public class ReportTransaction {
    private final String timestamp;
    private final String movieTitle;
    private final String seat;
    private final double price;

    public ReportTransaction(String timestamp, String movieTitle, String seat, double price) {
        this.timestamp = timestamp;
        this.movieTitle = movieTitle;
        this.seat = seat;
        this.price = price;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getSeat() {
        return seat;
    }

    public double getPrice() {
        return price;
    }
}
