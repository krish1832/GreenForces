package in.sp.backend;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/DisplayPdfServlet")
public class DisplayPdf extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Database connection parameters
        String url = "jdbc:postgresql://localhost:5432/EVS";
        String username = "postgres";
        String password = "admin";
        
        // SQL query to select PDF file names
        String sql = "SELECT day_name, task FROM special_day_tasks";

        // Lists to store PDF file names and corresponding day names
        List<String> pdfFiles = new ArrayList<>();
        List<String> names = new ArrayList<>();

        // Initialize JDBC objects
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                
                // Iterate over the result set and add file names and day names to the lists
                while (rs.next()) {
                    String fileName = rs.getString("task");
                    String dayName = rs.getString("day_name");
                    pdfFiles.add(fileName);
                    names.add(dayName);
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

        // Set the lists as attributes in the request
        request.setAttribute("pdfFiles", pdfFiles);
        request.setAttribute("names", names);
        
        // Forward the request to the JSP page for display
        request.getRequestDispatcher("ListOfpdf.jsp").forward(request, response);
    }
}
