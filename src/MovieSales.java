public class MovieSales {
    private final String title;
    private final int count;

    public MovieSales(String title, int count) {
        this.title = title;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public int getCount() {
        return count;
    }
}
