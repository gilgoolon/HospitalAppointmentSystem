package com.maorgil.hospitalappointmentsystem.beans;

import com.maorgil.hospitalappointmentsystem.DBHandler;
import com.maorgil.hospitalappointmentsystem.entity.UsersEntity;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.Date;

@SessionScoped
@ManagedBean(name = "profileDataBean")
public class ProfileDataBean {
    //    @ManagedProperty(value = "#{loginBean.user}") // inject the user from the login bean that will track sessions
    private UsersEntity user = new DBHandler().getUserById("123456789"); // temporary assignment
    private boolean previewMode = true;

    // user properties
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Date birthDate;
    private boolean isAdmin;

    public ProfileDataBean() {
        initUserData();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        // insert separators in the right spots
        return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber.substring(0,3) + phoneNumber.substring(4,7) + phoneNumber.substring(8);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String isAdminStr() {
        return isAdmin ? "Admin" : "User";
    }

    public String isUser() {
        return !user.isAdmin() ? "true" : "false";
    }

    public String isPreviewMode() {
        return previewMode ? "true" : "false";
    }

    public String getStyleClass(boolean isEditable) {
        return isEditable ? "" : "readonly";
    }

    /**
     * Switch to edit mode (where some fields are editable) and reload the page
     * @return the page to redirect to (same page with reload of the bean)
     */
    public String edit() {
        previewMode = false;
        return "myprofile.xhtml?faces-redirect=true";
    }

    /**
     * persist the data to the DB and switch to back to preview mode, reload the page
     * @return the page to redirect to (same page with reload of the bean)
     */
    public String submit() {
        previewMode = true;

        // persist changes to DB
        DBHandler dbHandler = new DBHandler();
        dbHandler.connect();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setBirthDate(new java.sql.Date(birthDate.getTime()));
        System.out.println(user.getBirthDate().toString());

        dbHandler.persistEntity(user);
        System.out.println(user.getBirthDate().toString());

        // init before sending in case of a DB error
        initUserData();
        return "myprofile.xhtml?faces-redirect=true";
    }

    public String action() {
        return previewMode ? edit() : submit();
    }

    public void initUserData() {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        phoneNumber = user.getPhoneNumber();
        email = user.getEmail();
        birthDate = user.getBirthDate();
        isAdmin = user.isAdmin();
    }
}
