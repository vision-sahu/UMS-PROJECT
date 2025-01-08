import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.net.URLEncoder;

public class NewServlet2 extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        try (PrintWriter out = response.getWriter()) {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection to the database
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/company1", "root", "")) {
                // Prepare the SQL statement
                PreparedStatement ps = con.prepareStatement("INSERT INTO contact  VALUES (?, ?, ?, ?)");
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, subject);
                ps.setString(4, message);

                // Execute the update
                int result = ps.executeUpdate();

                // Check if the insert was successful
                if (result > 0) {
                    // Redirect to the thank you page with the name parameter
                    response.sendRedirect("thankyou.html?name=" + URLEncoder.encode(name, "UTF-8"));
                } else {
                    out.println("Error: Unable to submit your message.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles Contact Us form submissions";
    }
}
