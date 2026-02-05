public class DateModel {
    private int id;
    private String dateStr;

    public DateModel(int id, String dateStr) {
        this.id = id;
        this.dateStr = dateStr;
    }

    public DateModel(String dateStr) {
        this.dateStr = dateStr;
    }

    // Getters
    public int getId() { return id; }
    public String getDateStr() { return dateStr; }

    // Setters
    public void setDateStr(String dateStr) { this.dateStr = dateStr; }

    @Override
    public String toString() {
        return dateStr;
    }
}
