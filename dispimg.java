package in.sp.backend;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/dispimg")
public class dispimg extends HttpServlet { 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("I am in disp image");
         String imgName = "";
        
        HttpSession session = req.getSession();
        int id = Integer.parseInt((String) session.getAttribute("userID"));
        
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/EVS", "postgres", "admin");
            
            PreparedStatement ps = con.prepareStatement("SELECT profile_img FROM users WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
               
                imgName = rs.getString("profile_img");
                System.out.println(imgName);
            } else {
                // Image not found
            }
            
            rs.close();
            ps.close();
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        
        RequestDispatcher rd = req.getRequestDispatcher("dispimg.jsp");
        req.setAttribute("id", id);
        req.setAttribute("img", imgName);
        rd.forward(req, resp);
    }
}
