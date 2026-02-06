import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.jdatepicker.impl.*;
import java.util.Properties;

public class DateManager {

    // เพิ่มวันใหม่ - รับ Calendar object จาก JDatePicker
    public static int addDate(Calendar calendar) {
        if (calendar == null) {
            System.err.println("Calendar object is null");
            return -1;
        }
        
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String dateStr = String.format("%02d %02d %04d", day, month, year);
        
        return addDateString(dateStr);
    }

    // เพิ่มวันใหม่ - รับ String (สำหรับความเข้ากันได้)
    public static int addDate(String dateStr) {
        return addDateString(dateStr);
    }

    // Method ภายใน - บันทึกลงฐานข้อมูล
    private static int addDateString(String dateStr) {
        String sql = "INSERT INTO dates (date_str) VALUES (?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, dateStr);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding date: " + e.getMessage());
        }
        return -1;
    }

    // ดึงวันทั้งหมด
    public static List<DateModel> getAllDates() {
        List<DateModel> dates = new ArrayList<>();
        String sql = "SELECT * FROM dates ORDER BY date_str DESC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                DateModel d = new DateModel(
                        rs.getInt("id"),
                        rs.getString("date_str")
                );
                dates.add(d);
            }
        } catch (SQLException e) {
            System.err.println("Error getting dates: " + e.getMessage());
        }
        return dates;
    }

    // ดึง ID จาก date string
    public static int getDateId(String dateStr) {
        String sql = "SELECT id FROM dates WHERE date_str = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dateStr);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting date ID: " + e.getMessage());
        }
        return -1;
    }

    // ลบวัน
    public static void deleteDate(int id) {
        String sql = "DELETE FROM dates WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting date: " + e.getMessage());
        }
    }
}
