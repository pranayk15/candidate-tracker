import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // ---- EDIT THESE THREE VALUES to match your local MySQL setup ----
    private static final String URL = "jdbc:mysql://localhost:3306/candidate_tracker";
    private static final String USER = "root";
    private static final String PASSWORD = "tiger";
    // -------------------------------------------------------------

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}