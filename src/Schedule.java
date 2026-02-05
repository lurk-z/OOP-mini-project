public class Schedule {
    private int id;
    private int dateId;
    private String time;
    private String productId;
    private String theater;
    private double price;

    public Schedule(int id, int dateId, String time, String productId, String theater, double price) {
        this.id = id;
        this.dateId = dateId;
        this.time = time;
        this.productId = productId;
        this.theater = theater;
        this.price = price;
    }

    public Schedule(int dateId, String time, String productId, String theater, double price) {
        this.dateId = dateId;
        this.time = time;
        this.productId = productId;
        this.theater = theater;
        this.price = price;
    }

    // Getters
    public int getId() { return id; }
    public int getDateId() { return dateId; }
    public String getTime() { return time; }
    public String getProductId() { return productId; }
    public String getTheater() { return theater; }
    public double getPrice() { return price; }

    // Setters
    public void setTime(String time) { this.time = time; }
    public void setTheater(String theater) { this.theater = theater; }
    public void setPrice(double price) { this.price = price; }
}
