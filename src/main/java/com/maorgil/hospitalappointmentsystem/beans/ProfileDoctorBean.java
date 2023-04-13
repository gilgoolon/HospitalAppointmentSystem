package com.maorgil.hospitalappointmentsystem.beans;

import com.maorgil.hospitalappointmentsystem.DBHandler;
import com.maorgil.hospitalappointmentsystem.entity.AppointmentsEntity;
import org.primefaces.PrimeFaces;

import javax.faces.bean.ManagedBean;
import java.util.List;

@ManagedBean(name = "profileDoctorBean")
public class ProfileDoctorBean {
    private final String id; // user id but the user is a doctor

    private static AppointmentsEntity selectedAppointment;
    private static String description = "";

    public ProfileDoctorBean() {
        // get the logged-in user id from the login bean
        id = LoginBean.getInstance().getId();
    }

    public String getLastName() {
        return DBHandler.getInstance().getDoctorById(id).getLastName();
    }

    public int getMaxDoctorAppointments() {
        return DBHandler.MAX_RESULTS_DOCTOR_UPCOMING_APPOINTMENTS;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        ProfileDoctorBean.description = description;
    }

    public List<AppointmentsEntity> getUpcomingAppointments() {
        return DBHandler.getInstance().getUpcomingAppointmentsByDoctorId(id);
    }

    public void onEditDesc(AppointmentsEntity appointment) {
        selectedAppointment = appointment;
        description = selectedAppointment.getDescription() == null ? "" : selectedAppointment.getDescription();

        // update with ajax
        PrimeFaces.current().ajax().update("descEditPopup");

        // show popup wanting description
        PrimeFaces.current().executeScript("PF('descEditPopup').show()");
    }

    public void saveDescription() {
        selectedAppointment.setDescription(description);
        DBHandler.getInstance().persistEntity(selectedAppointment, AppointmentsEntity.class, selectedAppointment.getId());
        PrimeFaces.current().ajax().update("dd" + selectedAppointment.getId());
    }
}
