
public class QuickCheckSchedules {
    public static void main(String[] args) {
        System.out.println("Listing schedules from DB:");
        for (Schedule s : ScheduleManager.getAllSchedules()) {
            Product p = ProductManager.getProduct(s.getProductId());
            String title = (p != null) ? p.getTitle() : "Unknown";
            System.out.printf("id=%d date_id=%d time=%s product_id=%s title=%s theater=%s price=%.2f\n",
                    s.getId(), s.getDateId(), s.getTime(), s.getProductId(), title, s.getTheater(), s.getPrice());
        }
    }
}
