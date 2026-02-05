import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleManager {

    // เพิ่มรอบฉายใหม่
    public static int addSchedule(int dateId, String time, String productId, String theater, double price) {
        String sql = "INSERT INTO schedules (date_id, time, product_id, theater, price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, dateId);
            pstmt.setString(2, time);
            pstmt.setString(3, productId);
            pstmt.setString(4, theater);
            pstmt.setDouble(5, price);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding schedule: " + e.getMessage());
        }
        return -1;
    }

    // ดึงรอบฉายตามวันที่
    public static List<Schedule> getSchedulesByDate(int dateId) {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM schedules WHERE date_id = ? ORDER BY time ASC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, dateId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Schedule s = new Schedule(
                            rs.getInt("id"),
                            rs.getInt("date_id"),
                            rs.getString("time"),
                            rs.getString("product_id"),
                            rs.getString("theater"),
                            rs.getDouble("price")
                    );
                    schedules.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting schedules: " + e.getMessage());
        }
        return schedules;
    }

    // ดึงรอบฉายทั้งหมด
    public static List<Schedule> getAllSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM schedules ORDER BY date_id DESC, time ASC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Schedule s = new Schedule(
                        rs.getInt("id"),
                        rs.getInt("date_id"),
                        rs.getString("time"),
                        rs.getString("product_id"),
                        rs.getString("theater"),
                        rs.getDouble("price")
                );
                schedules.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all schedules: " + e.getMessage());
        }
        return schedules;
    }

    // ลบรอบฉาย
    public static void deleteSchedule(int id) {
        String sql = "DELETE FROM schedules WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting schedule: " + e.getMessage());
        }
    }
}
