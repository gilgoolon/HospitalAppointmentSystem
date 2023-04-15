package com.maorgil.hospitalappointmentsystem.beans;


import com.maorgil.hospitalappointmentsystem.DBHandler;
import com.maorgil.hospitalappointmentsystem.Utils;
import com.maorgil.hospitalappointmentsystem.entity.AppointmentsEntity;
import com.maorgil.hospitalappointmentsystem.entity.DoctorsEntity;
import org.primefaces.PrimeFaces;

import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "profileAppointmentsBean")
public class ProfileAppointmentsBean {
    private Date fromDate;
    private Date toDate;
    private List<AppointmentsEntity> appointments;
    private DoctorsEntity doctor = null;
    private String sortBy = "Date"; // Date, Doctor, Duration (apt length)
    private boolean isAscending = false;
    private boolean includeCancelled = true;
    private final String loggedInUserID;

    private static AppointmentsEntity cancelledAppointment;

    public ProfileAppointmentsBean() {
        // get the logged-in user id from the login bean
        loggedInUserID = LoginBean.getInstance().getId();
        appointments = DBHandler.getInstance().getAppointments(loggedInUserID, null, null, "", true);
        appointments.forEach(app -> System.out.println(app.getStartTime() + " " + app.getEndTime() + " " + app.isCancelled()));
    }

    public String getDoctorId() {
        if (doctor == null)
            return "All";
        return doctor.getId();
    }

    public void setDoctorId(String doctorId) {
        this.doctor = DBHandler.getInstance().getDoctorById(doctorId); // null if "All Doctors" is selected
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

    public boolean isIncludeCancelled() {
        return includeCancelled;
    }

    public void setIncludeCancelled(boolean includeCancelled) {
        this.includeCancelled = includeCancelled;
    }

    public void search() {
        appointments = new ArrayList<>(DBHandler.getInstance().getAppointments(loggedInUserID, fromDate, toDate, doctor == null ? "" : doctor.getId(), includeCancelled));
    }

    public List<AppointmentsEntity> getAppointments() {
        sortAppointments();
        return appointments;
    }

    public String appToId(AppointmentsEntity app) {
        return app.getId() + "";
    }

    public void onCancelAppointment(AppointmentsEntity app) {
        cancelledAppointment = app;

        // ajax call to cancel appointment
        PrimeFaces.current().ajax().update("appCancelDialog");

        // show the dialog
        PrimeFaces.current().executeScript("PF('appCancelDialog').show()");
    }

    public String getCancelDoctor() {
        return cancelledAppointment == null ? "" : DBHandler.getInstance().getDoctorById(cancelledAppointment.getDoctorId()).toString();
    }

    public String getCancelTime() {
        return cancelledAppointment == null ? "" : Utils.toString(cancelledAppointment.getStartTime().toLocalDateTime()) + "-" + Utils.toString(cancelledAppointment.getEndTime().toLocalDateTime());
    }

    public void cancelAppointment() {
        DBHandler.getInstance().cancelAppointment(cancelledAppointment);

        // refresh the page
        Utils.redirect("myprofile.xhtml?faces-redirect=true");
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
                    DoctorsEntity d1 = DBHandler.getInstance().getDoctorById(a1.getDoctorId());
                    DoctorsEntity d2 = DBHandler.getInstance().getDoctorById(a2.getDoctorId());
                    String s1 = d1.getLastName() + d1.getFirstName();
                    String s2 = d2.getLastName() + d2.getFirstName();
                    if (isAscending)
                        return s1.compareTo(s2);
                    return s2.compareTo(s1);
                });
                break;
            case "Duration":
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
        return DBHandler.getInstance().getTreatingDoctors(loggedInUserID);
    }
}
