package in.sp.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/delete")
public class delete extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        PrintWriter out = resp.getWriter();
        Connection con = null;
        PreparedStatement ps = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/EVS", "postgres", "admin");

            String query = "DELETE FROM users WHERE username=? AND password=?";
            ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Deletion successful
                resp.sendRedirect("index.jsp");
            } else {
                resp.setContentType("text/html");
                out.println("<div style='color:red;'>Invalid username or password...!!</div>");

                RequestDispatcher rd = req.getRequestDispatcher("/delprofile.jsp");
                rd.include(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setContentType("text/html");
            out.print(e.getLocalizedMessage());

            RequestDispatcher rd = req.getRequestDispatcher("/delprofile.jsp");
            rd.include(req, resp);
        } finally {
            // Close resources in finally block
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
