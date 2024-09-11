package in.sp.backend;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/AD_Login")
public class ad_login extends HttpServlet {
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	  String username = req.getParameter("ad_username");
      String password = req.getParameter("ad_password");
      PrintWriter out = resp.getWriter();
      try {
          Class.forName("org.postgresql.Driver");
          Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/EVS", "postgres", "admin");

          PreparedStatement ps = con.prepareStatement("select * from admin where username=? and password=?");
          ps.setString(1, username);
          ps.setString(2, password);

          ResultSet rs = ps.executeQuery();
          if (rs.next()) {
          	
              RequestDispatcher rd=req.getRequestDispatcher("/ad_profile.jsp");
              rd.include(req, resp);
          } else {
              resp.setContentType("text/html");
              out.println("<div style='color:red;'>Invalid username or password...!!</div>");

              RequestDispatcher rd = req.getRequestDispatcher("/ad_login.jsp");
              rd.include(req, resp);
          }
      } catch (Exception e) {
          e.printStackTrace();

          resp.setContentType("text/html");
          out.print(e.getLocalizedMessage());

          RequestDispatcher rd = req.getRequestDispatcher("/ad_login.jsp");
          rd.include(req, resp);
      }

}
}
