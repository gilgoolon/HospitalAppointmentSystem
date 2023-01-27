package com.maorgil.hospitalappointmentsystem.beans;

import com.maorgil.hospitalappointmentsystem.DBHandler;
import com.maorgil.hospitalappointmentsystem.entity.UsersEntity;

import javax.faces.annotation.ManagedProperty;
import javax.faces.bean.ManagedBean;
import java.util.Calendar;
import java.util.Date;

@ManagedBean(name = "profileDataBean")
public class ProfileDataBean {
    //    @ManagedProperty(value = "#{loginBean.user}") // inject the user from the login bean that will track sessions
    private UsersEntity user = new DBHandler().getUserById("123456789"); // temporary assignment
    private boolean previewMode = true;

    public static final int MAX_AGE = 120;

    public String getID() {
        return user.getId();
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getPhoneNumber() {
        return user.getPhoneNumber();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getBirthDate() {
        return user.getBirthDate().toString();
    }

    public String isUser() {
        return !user.isAdmin() ? "true" : "false";
    }

    public String isAdminStr() {
        return user.isAdmin() ? "Admin" : "User";
    }

    // errors for editable fields (only for preview mode)

    public String getPhoneNumberError() {
        return "";
    }

    public String getEmailError() {
        return "";
    }


    public String isPreviewMode() {
        return previewMode ? "true" : "false";
    }

    public boolean[] disableDates(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -MAX_AGE);
        Date date120YearsAgo = cal.getTime();
        boolean[] dates = {true, false};
        if (date.before(new Date()) && date.before(date120YearsAgo))
            dates[0] = false;
        return dates;
    }

    public String getStyleClass(boolean isEditable) {
        return isEditable ? "" : "readonly";
    }
}
