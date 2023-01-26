package com.maorgil.hospitalappointmentsystem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        String password = request.getParameter("password");

        PrintWriter writer = response.getWriter();
        String htmlRespone = "<html>";
        DBHandler dbHandler = new DBHandler();

        if (dbHandler.validateLogin(id, password)) {
            htmlRespone += "<h2>Login success</h2>";
        }
        else {
            htmlRespone += "<h2>Login failed</h2>";
        }

        htmlRespone += "</html>";
        writer.println(htmlRespone);
    }
}
