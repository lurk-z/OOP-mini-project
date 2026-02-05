public class Transaction {
    private int id;
    private int scheduleId;
    private String seat;
    private double price;
    private String timestamp;

    public Transaction(int id, int scheduleId, String seat, double price, String timestamp) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.seat = seat;
        this.price = price;
        this.timestamp = timestamp;
    }

    public Transaction(int scheduleId, String seat, double price) {
        this.scheduleId = scheduleId;
        this.seat = seat;
        this.price = price;
    }

    // Getters
    public int getId() { return id; }
    public int getScheduleId() { return scheduleId; }
    public String getSeat() { return seat; }
    public double getPrice() { return price; }
    public String getTimestamp() { return timestamp; }

    // Setters
    public void setSeat(String seat) { this.seat = seat; }
    public void setPrice(double price) { this.price = price; }
}
