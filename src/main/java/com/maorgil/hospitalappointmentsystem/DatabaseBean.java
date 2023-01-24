package com.maorgil.hospitalappointmentsystem;

import com.maorgil.hospitalappointmentsystem.entity.UsersEntity;

import javax.faces.bean.ManagedBean;

@ManagedBean(name="databaseBean")
public class DatabaseBean {
    public String getInfo() throws ClassNotFoundException {
        System.out.println("Hello world!");
        Class.forName("com.mysql.cj.jdbc.Driver");
        final StringBuilder info = new StringBuilder();
        DBHandler db = new DBHandler();
        db.getDoctorsBySearch("John").forEach((doctor) -> {
            UsersEntity u = db.getUserById(doctor.getId());
            info.append("Entity (Doctor):\n\tI.D: ").append(doctor.getId()).append("\n\tName: ").append(u.getFirstName()).append(" ").append(u.getLastName()).append("\n\tAge: ").append(u.calculateAge()).append("\n\tAbout: ").append(doctor.getAbout()).append("\n");
        });
        return info.toString();
    }
}
