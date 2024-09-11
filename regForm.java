package in.sp.backend;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/regForm")
public class regForm extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fName = req.getParameter("f_name");
        String mName = req.getParameter("m_name");
        String lName = req.getParameter("l_name");
        String email = req.getParameter("email");
        String phNo = req.getParameter("ph_no");
        String address = req.getParameter("address");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String cPassword = req.getParameter("c_password");
        String birthdateString = req.getParameter("birthdate");
        Date birthdate = null;

        try {
            birthdate = new SimpleDateFormat("yyyy-MM-dd").parse(birthdateString);
        } catch (ParseException e) {
            e.printStackTrace();
            showError(req, resp, "Invalid birth date format. Please enter in yyyy-MM-dd format.");
            return;
        }

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/EVS", "postgres", "admin")) {
                if (isUsernameExists(con, username)) {
                    showError(req, resp, "Username already exists.");
                    return;
                }

                if (!password.equals(cPassword)) {
                    showError(req, resp, "Passwords do not match.");
                    return;
                }

                insertUser(con, fName, mName, lName, email, phNo, address, birthdate, username, password, cPassword);

                // Redirect to index.jsp
                resp.sendRedirect("after_reg.jsp");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            resp.getWriter().println(e);
            showError(req, resp, "An error occurred. Please try again later.");
        }
    }

    private boolean isUsernameExists(Connection con, String username) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void insertUser(Connection con, String fName, String mName, String lName, String email, String phNo,
            String address, Date birthdate, String username, String password, String cPassword) throws SQLException {
        String sql = "INSERT INTO users (f_name, m_name, l_name, email, ph_no, birthdate, address, username, password, c_password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fName);
            ps.setString(2, mName);
            ps.setString(3, lName);
            ps.setString(4, email);
            ps.setString(5, phNo);
            ps.setDate(6, new java.sql.Date(birthdate.getTime()));
            ps.setString(7, address);
            ps.setString(8, username);
            ps.setString(9, password);
            ps.setString(10, cPassword);

            ps.executeUpdate();
        }
    }

    private void showError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws IOException, ServletException {
        resp.setContentType("text/html");
        resp.getWriter().println("<div style=\"color: #ff0000; text-align: center; margin-top: 10px;\">");
        resp.getWriter().println("<strong>Error:</strong> " + message);
        resp.getWriter().println("</div>");
        RequestDispatcher rd = req.getRequestDispatcher("/registration.jsp");
        rd.include(req, resp);
    }
}
