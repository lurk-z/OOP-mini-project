import java.sql.*;

public class CreateDatabase {
    public static void main(String[] args) {
        String dbUrl = "jdbc:sqlite:wigshop_new.db";
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
            return;
        }

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS products (" +
                        "id TEXT PRIMARY KEY, " +
                        "title TEXT NOT NULL, " +
                        "category TEXT NOT NULL, " +
                        "price REAL NOT NULL" +
                        ")");

                stmt.execute("CREATE TABLE IF NOT EXISTS dates (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "date_str TEXT NOT NULL UNIQUE" +
                        ")");

                stmt.execute("CREATE TABLE IF NOT EXISTS schedules (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "date_id INTEGER NOT NULL, " +
                        "time TEXT NOT NULL, " +
                        "product_id TEXT NOT NULL, " +
                        "theater TEXT NOT NULL, " +
                        "price REAL NOT NULL, " +
                        "FOREIGN KEY(date_id) REFERENCES dates(id), " +
                        "FOREIGN KEY(product_id) REFERENCES products(id)" +
                        ")");

                stmt.execute("CREATE TABLE IF NOT EXISTS transactions (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "schedule_id INTEGER NOT NULL, " +
                        "seat TEXT NOT NULL, " +
                        "price REAL NOT NULL, " +
                        "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY(schedule_id) REFERENCES schedules(id)" +
                        ")");

                System.out.println("Created wigshop_new.db and initialized tables.");
            }
        } catch (SQLException ex) {
            System.err.println("SQL error: " + ex.getMessage());
        }
    }
}
