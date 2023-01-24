package com.maorgil.hospitalappointmentsystem;

import com.maorgil.hospitalappointmentsystem.entity.UsersEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
//        DBHandler db = new DBHandler();
//        db.getDoctorsBySearch("John").forEach((doctor) -> {
//            UsersEntity u = db.getUserById(doctor.getId());
//            System.out.println("Entity (Doctor):\n\tI.D: " + doctor.getId() + "\n\tName: " + u.getFirstName() + " " + u.getLastName() + "\n\tAge: " + u.calculateAge() + "\n\tAbout: " + doctor.getAbout());
//        });
        Connection dbConnection = null;

        try {
            String url = "jdbc:mysql://localhost:3306/hospital";
            Properties info = new Properties();
            info.put("user", "root");
            info.put("password", "Alpert1704");

            dbConnection = DriverManager.getConnection(url, info);

            if (dbConnection != null) {
                System.out.println("Successfully connected to MySQL database test");
            }

        } catch (SQLException ex) {
            System.out.println("An error occurred while connecting MySQL databse");
            ex.printStackTrace();
        }
    }
}
