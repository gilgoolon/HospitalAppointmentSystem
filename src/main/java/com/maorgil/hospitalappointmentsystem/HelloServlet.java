package com.maorgil.hospitalappointmentsystem;

import com.maorgil.hospitalappointmentsystem.entity.UsersEntity;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        DBHandler db = new DBHandler();
        message = "";
        db.getDoctorsBySearch("John").forEach((doctor) -> {
            UsersEntity u = db.getUserById(doctor.getId());
            message += "Entity (Doctor):\n\tI.D: " + doctor.getId() + "\n\tName: " + u.getFirstName() + " " + u.getLastName() + "\n\tAge: " + u.calculateAge() + "\n\tAbout: " + doctor.getAbout() + "\n";
        });
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {

    }
}