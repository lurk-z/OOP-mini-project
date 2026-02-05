import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:wigshop_new.db";
    private static Connection connection;
    private static volatile boolean driverLoaded = false;

    // เปิดการเชื่อมต่อ Database
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            ensureSqliteDriver();
            connection = DriverManager.getConnection(DB_URL);
            initializeDatabase();
        }
        return connection;
    }

    private static void ensureSqliteDriver() throws SQLException {
        if (driverLoaded) {
            return;
        }
        synchronized (DatabaseManager.class) {
            if (driverLoaded) {
                return;
            }
            try {
                Class.forName("org.sqlite.JDBC");
                driverLoaded = true;
                return;
            } catch (ClassNotFoundException ignored) {
                // Fall back to loading sqlite-jdbc from lib/ when not on the classpath.
            }

            Exception last = null;
            Path libDir = findLibDir();
            if (libDir != null) {
                try (DirectoryStream<Path> jars = Files.newDirectoryStream(libDir, "sqlite-jdbc*.jar")) {
                    for (Path jar : jars) {
                        try {
                            loadDriverFromJar(jar);
                            driverLoaded = true;
                            return;
                        } catch (Exception e) {
                            last = e;
                        }
                    }
                } catch (IOException e) {
                    last = e;
                }
            }

            SQLException ex = new SQLException(
                "SQLite JDBC Driver not found. Add sqlite-jdbc*.jar to the classpath or put it in lib/."
            );
            if (last != null) {
                ex.initCause(last);
            }
            throw ex;
        }
    }

    private static Path findLibDir() {
        Path cwdLib = Paths.get("").toAbsolutePath().resolve("lib");
        if (Files.isDirectory(cwdLib)) {
            return cwdLib;
        }
        try {
            Path codeSource = Paths.get(
                DatabaseManager.class.getProtectionDomain().getCodeSource().getLocation().toURI()
            );
            if (Files.isDirectory(codeSource)) {
                Path directLib = codeSource.resolve("lib");
                if (Files.isDirectory(directLib)) {
                    return directLib;
                }
            }
            Path base = codeSource.getParent();
            if (base != null) {
                Path lib = base.resolve("lib");
                if (Files.isDirectory(lib)) {
                    return lib;
                }
            }
        } catch (Exception ignored) {
            // Ignore and return null below.
        }
        return null;
    }

    private static void loadDriverFromJar(Path jarPath) throws Exception {
        URL jarUrl = jarPath.toUri().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[]{jarUrl}, DatabaseManager.class.getClassLoader());
        Class<?> driverClass = Class.forName("org.sqlite.JDBC", true, loader);
        Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
        DriverManager.registerDriver(new DriverShim(driver));
    }

    private static class DriverShim implements Driver {
        private final Driver driver;

        private DriverShim(Driver driver) {
            this.driver = driver;
        }

        public boolean acceptsURL(String url) throws SQLException {
            return driver.acceptsURL(url);
        }

        public Connection connect(String url, Properties info) throws SQLException {
            return driver.connect(url, info);
        }

        public int getMajorVersion() {
            return driver.getMajorVersion();
        }

        public int getMinorVersion() {
            return driver.getMinorVersion();
        }

        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
            return driver.getPropertyInfo(url, info);
        }

        public boolean jdbcCompliant() {
            return driver.jdbcCompliant();
        }

        public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return driver.getParentLogger();
        }
    }

    // สร้างตาราง ถ้ายังไม่มี
    private static void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {
            // ตาราง Products (หนัง)
            stmt.execute("CREATE TABLE IF NOT EXISTS products (" +
                    "id TEXT PRIMARY KEY, " +
                    "title TEXT NOT NULL, " +
                    "category TEXT NOT NULL, " +
                    "price REAL NOT NULL" +
                    ")");

            // ตาราง Dates (วันฉาย)
            stmt.execute("CREATE TABLE IF NOT EXISTS dates (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "date_str TEXT NOT NULL UNIQUE" +
                    ")");

            // ตาราง Schedules (รอบฉาย)
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

            // ตาราง Transactions (การขาย)
            stmt.execute("CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "schedule_id INTEGER NOT NULL, " +
                    "seat TEXT NOT NULL, " +
                    "price REAL NOT NULL, " +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(schedule_id) REFERENCES schedules(id)" +
                    ")");

            System.out.println("✓ Database initialized successfully");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    // ปิดการเชื่อมต่อ
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
