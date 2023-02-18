package com.maorgil.hospitalappointmentsystem.beans;


import com.maorgil.hospitalappointmentsystem.DBHandler;
import com.maorgil.hospitalappointmentsystem.Utils;
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
    private String sortBy = "Date"; // Date, Doctor, Length (apt length)
    private boolean isAscending = false;
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
        sortAppointments();
        for (AppointmentsEntity appointment : appointments) {
            sb.append("<div class=\"card").append(appointment.isCancelled() ? " cancelled" : appointment.isPast() ? " past" : "").append("\">")
                    .append("<div class=\"card-content\">")
                    .append("<div style=\"display: flex; flex-direction: row;\">")
                    .append("<a class=\"title-small\">")
                    .append(appointment.getTitleForPatient())
                    .append("</a>");
            if (appointment.isNow() && !appointment.isCancelled()) // add blinking circle to indicate live appointment
                sb.append("<div style=\"position: relative; margin-left: auto; margin-right: 5px; display: flex; align-items: center; justify-content: center;\"><div class=\"live-circle\"></div></div>");
            sb
                    .append("</div>")
                    .append("<div style=\"display: flex; flex-direction: row;\">")
                    .append("<a class=\"text-small\">")
                    .append(appointment.getDescription())
                    .append("</a>");
            if (!appointment.isCancelled() && appointment.isPast())
                sb
                    .append("<button class=\"download-button\" onClick=\"downloadAppointment('")
                    .append(Utils.appointmentToId(appointment))
                    .append("')\">")
                    .append("<img src=\"assets/download-icon.png\" alt=\"\"/>")
                    .append("</button>");
            sb
                    .append("</div>")
                    .append("</div>")
                    .append("</div>");
        }
        return sb.toString();
    }

    public void sortAppointments() {
        switch (sortBy) {
            case "Date":
                appointments.sort((a1, a2) -> {
                    if (isAscending)
                        return a1.getStartTime().compareTo(a2.getStartTime());
                    return a2.getStartTime().compareTo(a1.getStartTime());
                });
                break;
            case "Doctor":
                appointments.sort((a1, a2) -> {
                    DoctorsEntity d1 = dbHandler.getDoctorById(a1.getDoctorId());
                    DoctorsEntity d2 = dbHandler.getDoctorById(a2.getDoctorId());
                    String s1 = d1.getLastName() + d1.getFirstName();
                    String s2 = d2.getLastName() + d2.getFirstName();
                    if (isAscending)
                        return s1.compareTo(s2);
                    return s2.compareTo(s1);
                });
                break;
            case "Length":
                appointments.sort((a1, a2) -> {
                    double l1 = a1.getEndTime().getTime() - a1.getStartTime().getTime();
                    double l2 = a2.getEndTime().getTime() - a2.getStartTime().getTime();
                    if (isAscending)
                        return Double.compare(l1, l2);
                    return Double.compare(l2, l1);
                });
                break;
        }
    }

    public List<DoctorsEntity> getTreatingDoctors() {
        return dbHandler.getTreatingDoctors(loggedInUserID);
    }
}
