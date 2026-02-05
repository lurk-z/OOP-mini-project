import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {

    // เพิ่มการขายใหม่
    public static int addTransaction(int scheduleId, String seat, double price) {
        String sql = "INSERT INTO transactions (schedule_id, seat, price) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, scheduleId);
            pstmt.setString(2, seat);
            pstmt.setDouble(3, price);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding transaction: " + e.getMessage());
        }
        return -1;
    }

    // ดึงการขายทั้งหมด
    public static List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY timestamp DESC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getInt("id"),
                        rs.getInt("schedule_id"),
                        rs.getString("seat"),
                        rs.getDouble("price"),
                        rs.getString("timestamp")
                );
                transactions.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error getting transactions: " + e.getMessage());
        }
        return transactions;
    }

    // ดึงการขายตามรอบฉาย
    public static List<Transaction> getTransactionsBySchedule(int scheduleId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE schedule_id = ? ORDER BY timestamp DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, scheduleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction t = new Transaction(
                            rs.getInt("id"),
                            rs.getInt("schedule_id"),
                            rs.getString("seat"),
                            rs.getDouble("price"),
                            rs.getString("timestamp")
                    );
                    transactions.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting transactions by schedule: " + e.getMessage());
        }
        return transactions;
    }

    // คำนวณรายได้รวม
    public static double getTotalRevenue() {
        String sql = "SELECT SUM(price) as total FROM transactions";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total revenue: " + e.getMessage());
        }
        return 0;
    }

    // นับจำนวนตั๋วที่ขายได้
    public static int getTotalTicketsSold() {
        String sql = "SELECT COUNT(*) as count FROM transactions";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total tickets sold: " + e.getMessage());
        }
        return 0;
    }

    // หาหนังที่ขายดีที่สุด
    public static String getBestSellingMovie() {
        String sql = "SELECT p.title FROM transactions t " +
                "JOIN schedules s ON t.schedule_id = s.id " +
                "JOIN products p ON s.product_id = p.id " +
                "GROUP BY p.id " +
                "ORDER BY COUNT(*) DESC LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("title");
            }
        } catch (SQLException e) {
            System.err.println("Error getting best selling movie: " + e.getMessage());
        }
        return "N/A";
    }

    // ลบการขาย
    public static void deleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
        }
    }

    // รายงาน: Top movies (จำนวนตั๋วขายได้)
    public static List<MovieSales> getTopSellingMovies(int limit) {
        List<MovieSales> results = new ArrayList<>();
        String sql = "SELECT p.title AS title, COUNT(*) AS count " +
                "FROM transactions t " +
                "JOIN schedules s ON t.schedule_id = s.id " +
                "JOIN products p ON s.product_id = p.id " +
                "GROUP BY p.id " +
                "ORDER BY count DESC, p.title ASC " +
                "LIMIT ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new MovieSales(
                            rs.getString("title"),
                            rs.getInt("count")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting top selling movies: " + e.getMessage());
        }
        return results;
    }

    // รายงาน: รายการขายล่าสุด
    public static List<ReportTransaction> getRecentTransactions(int limit) {
        List<ReportTransaction> results = new ArrayList<>();
        String sql = "SELECT t.timestamp AS ts, p.title AS title, t.seat AS seat, t.price AS price " +
                "FROM transactions t " +
                "JOIN schedules s ON t.schedule_id = s.id " +
                "JOIN products p ON s.product_id = p.id " +
                "ORDER BY t.timestamp DESC, t.id DESC " +
                "LIMIT ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new ReportTransaction(
                            rs.getString("ts"),
                            rs.getString("title"),
                            rs.getString("seat"),
                            rs.getDouble("price")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting recent transactions: " + e.getMessage());
        }
        return results;
    }
}
