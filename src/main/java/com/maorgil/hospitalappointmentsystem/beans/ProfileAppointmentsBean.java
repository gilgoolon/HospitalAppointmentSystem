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
    private String sortBy = "date"; // date, doctor, length (apt length)
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
        sortAppointments();
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
