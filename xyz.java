package in.sp.backend;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/oop")
public class xyz extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Database connection parameters
        String url = "jdbc:postgresql://localhost:5432/EVS";
        String username = "postgres";
        String password = "admin";
        
        // SQL query to select date  
        String sql = "SELECT task_date FROM special_day_tasks";

        // Initialize JDBC objects
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                
                // Iterate over the result set and add file names and day names to the lists
                while (rs.next()) {
                    String date = rs.getString("task_date");
                    response.getWriter().println(date); // Write date to response
                }
            } catch (SQLException e) {
                // Handle database errors
                e.printStackTrace();
                // You can redirect to an error page or display an error message here
                response.sendRedirect("error.jsp");
            }
        } catch (ClassNotFoundException e) {
            // Handle class not found exception
            e.printStackTrace();
            // You can redirect to an error page or display an error message here
            response.sendRedirect("error.jsp");
        }
    }
}
