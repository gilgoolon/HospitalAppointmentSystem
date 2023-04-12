package com.maorgil.hospitalappointmentsystem.beans;

import com.maorgil.hospitalappointmentsystem.DBHandler;
import com.maorgil.hospitalappointmentsystem.entity.AppointmentsEntity;

import javax.faces.bean.ManagedBean;
import java.util.List;

@ManagedBean(name = "profileDoctorBean")
public class ProfileDoctorBean {
    private final String id; // user id but the user is a doctor
    private final DBHandler dbHandler = new DBHandler();

    public ProfileDoctorBean() {
        // get the logged-in user id from the login bean
        id = LoginBean.getInstance().getId();
    }

    public String getLastName() {
        return dbHandler.getDoctorById(id).getLastName();
    }

    public int getMaxDoctorAppointments() {
        return DBHandler.MAX_RESULTS_DOCTOR_UPCOMING_APPOINTMENTS;
    }

    public List<AppointmentsEntity> getUpcomingAppointments() {
        return dbHandler.getUpcomingAppointmentsByDoctorId(id);
    }
}
