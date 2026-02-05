public class Product {
    private String id;
    private String title;
    private String category;
    private double price;

    public Product(String id, String title, String category, double price) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.price = price;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return title;
    }
}
