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
import java.text.SimpleDateFormat;

@WebServlet("/ProcessFormDataServlet1")
@MultipartConfig
public class ProcessFormDataServlet1  extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("I am in do post");

        // Get the image part from the request
        String dateString = request.getParameter("date");
        String dayName = request.getParameter("day_name");
        Part filePart = request.getPart("pdf");
 
        if (dateString != null && !dateString.isEmpty() && dayName != null && !dayName.isEmpty() && filePart != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        	java.util.Date date = null;
        	try {
            	date = dateFormat.parse(dateString);
        	} catch (Exception e) {
            	e.printStackTrace();
        	}
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

        try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Update the user's profile image in the database
        try {
        	Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/EVS", "postgres", "admin");
        	PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO  daily_tasks (task_date, task, day_name) VALUES (?, ?, ?)");
        	preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
        	preparedStatement.setString(2, imageName); // Store file name
        	preparedStatement.setString(3, dayName);
        	preparedStatement.executeUpdate();
        	preparedStatement.close();
        	connection.close();

            // Redirect to a success page
        	response.sendRedirect("success2.jsp");
    	} catch (SQLException e) {
        	e.printStackTrace();
        	
        	// Forward the request to the error page with error message
        	request.setAttribute("errorMessage", "Error storing data: " + e.getMessage());
        	request.getRequestDispatcher("error.jsp").forward(request, response);
    	}

    }
    }}
