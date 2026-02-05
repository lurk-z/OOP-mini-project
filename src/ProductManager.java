import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    
    // เพิ่มหนังใหม่
    public static void addProduct(String id, String title, String category, double price) {
        String sql = "INSERT INTO products (id, title, category, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, title);
            pstmt.setString(3, category);
            pstmt.setDouble(4, price);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
        }
    }

    // ดึงหนังทั้งหมด
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product p = new Product(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("category"),
                        rs.getDouble("price")
                );
                products.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error getting products: " + e.getMessage());
        }
        return products;
    }

    // ดึงหนังจาก ID
    public static Product getProduct(String id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getString("id"),
                            rs.getString("title"),
                            rs.getString("category"),
                            rs.getDouble("price")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting product: " + e.getMessage());
        }
        return null;
    }

    // อัปเดตหนัง
    public static void updateProduct(String id, String title, String category, double price) {
        String sql = "UPDATE products SET title = ?, category = ?, price = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, category);
            pstmt.setDouble(3, price);
            pstmt.setString(4, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
        }
    }

    // ลบหนัง
    public static void deleteProduct(String id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
        }
    }
}
