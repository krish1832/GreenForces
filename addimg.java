package in.sp.backend;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/addimg")
@MultipartConfig
public class addimg extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("I am in do post");

        // Get the image part from the request
        Part filePart = request.getPart("image");

        // Get the submitted file name
        String imageName = filePart.getSubmittedFileName();
        System.out.println("Image Name: " + imageName);

        // Path to save the uploaded image
        String uploadPath = "D:/workspace/GW/src/main/webapp/images/" + imageName;
        System.out.println("Uploaded Path: " + uploadPath);

        // Save the uploaded image to the specified path
        try (FileOutputStream fos = new FileOutputStream(uploadPath); InputStream is = filePart.getInputStream()) {
            byte[] data = new byte[is.available()];
            is.read(data);
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get user ID from session
        HttpSession session = request.getSession();
        int userID = Integer.parseInt((String) session.getAttribute("userID"));
        System.out.println("User ID: " + userID);

        // Update the user's profile image in the database
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/EVS", "postgres", "admin")) {
                PreparedStatement ps = con.prepareStatement("UPDATE users SET profile_img = ? WHERE id = ?");
                ps.setString(1, imageName);
                ps.setInt(2, userID);
                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println("Successful image upload");
                    PrintWriter out = response.getWriter();
                    out.println("<p style='color:green;'>Your profile image updated successfully!</p>");
                   
                } else {
                    System.out.println("Failed to update image");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
