package com.maorgil.hospitalappointmentsystem.beans;


import com.maorgil.hospitalappointmentsystem.DBHandler;
import com.maorgil.hospitalappointmentsystem.entity.AppointmentsEntity;
import com.maorgil.hospitalappointmentsystem.entity.UsersEntity;

import javax.faces.bean.ManagedBean;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "profileAppointmentsBean")
public class ProfileAppointmentsBean {
    private Date fromDate;
    private Date toDate;
    private List<AppointmentsEntity> appointments;
    private final String loggedInUserID;
    private final DBHandler dbHandler = new DBHandler();

    public ProfileAppointmentsBean() {
        // get the logged-in user id from the login bean
        loggedInUserID = LoginBean.getInstance().getId();
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
        appointments = dbHandler.getAppointmentsInDates(fromDate, toDate, loggedInUserID);
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
}
