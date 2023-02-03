package com.maorgil.hospitalappointmentsystem.beans;


import com.maorgil.hospitalappointmentsystem.DBHandler;
import com.maorgil.hospitalappointmentsystem.entity.AppointmentsEntity;
import com.maorgil.hospitalappointmentsystem.entity.DoctorsEntity;

import javax.faces.bean.ManagedBean;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "profileAppointmentsBean")
public class ProfileAppointmentsBean {
    private Date fromDate;
    private Date toDate;
    private List<AppointmentsEntity> appointments;
    private DoctorsEntity doctor = null;
    private String sortBy = "date";
    private boolean isAscending = true;
    private final String loggedInUserID;
    private final DBHandler dbHandler = new DBHandler();

    public ProfileAppointmentsBean() {
        // get the logged-in user id from the login bean
        loggedInUserID = LoginBean.getInstance().getId();
    }

    public String getDoctorId() {
        if (doctor == null)
            return "All";
        return doctor.getId();
    }

    public void setDoctorId(String doctorId) {
        this.doctor = dbHandler.getDoctorById(doctorId); // null if "All Doctors" is selected
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public boolean isAscending() {
        return isAscending;
    }

    public void setAscending(boolean ascending) {
        isAscending = ascending;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public void search() {
        appointments = dbHandler.getAppointments(loggedInUserID, fromDate, toDate, doctor == null ? "" : doctor.getId());
    }

    public String getAppointments() {
        StringBuilder sb = new StringBuilder();
        for (AppointmentsEntity appointment : appointments) {
            sb
                    .append("<div class=\"card\">")
                        .append("<div class=\"card-content\">")
                            .append("<a class=\"title-small\">")
                                .append(appointment.getTitle())
                            .append("</a>")
                            .append("<a class=\"text-small\">")
                                .append(appointment.getDescription())
                            .append("</a>")
                        .append("</div>")
                    .append("</div>");
        }
        return sb.toString();
    }

    public List<DoctorsEntity> getTreatingDoctors() {
        return dbHandler.getTreatingDoctors(loggedInUserID);
    }


}
