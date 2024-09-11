package in.sp.backend;

 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/EVS";
        String dbUsername = "postgres";
        String dbPassword = "admin";
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
    }
}
