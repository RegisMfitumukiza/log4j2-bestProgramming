package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/display")
public class DisplayServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(DisplayServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String enteredId = request.getParameter("id");

        if (!enteredId.matches("\\d+")) {
            logger.warn("Invalid ID entered: {}", enteredId);
            response.getWriter().println("<p>The id must be a number</p>");
            return;
        }

        Integer id = Integer.parseInt(enteredId);
        logger.info("Processing request for ID: {}", id);

        try {
            String db_url = "jdbc:postgresql://localhost:5432/best_programming_db";
            String username = "postgres";
            String password = "Admin123.";

            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(db_url, username, password);
            PreparedStatement pst = con.prepareStatement("SELECT * FROM person WHERE id = ?");
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();

            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>User Information</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>User Information</h1>");

            if (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");

                logger.info("User found: {} {}", firstName, lastName);
                out.println("<p>My name is " + firstName + " " + lastName + ", and my ID is " + id + ".</p>");
            } else {
                logger.warn("No user found for ID: {}", id);
                out.println("<p>Your ID does not exist</p>");
            }

            out.println("</body>");
            out.println("</html>");

            rs.close();
            pst.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Error processing request", e);
            response.getWriter().println("<p>There was an error processing your request.</p>");
        }
    }
}