package com.maorgil.hospitalappointmentsystem.beans;

import com.maorgil.hospitalappointmentsystem.DBHandler;
import com.maorgil.hospitalappointmentsystem.entity.UsersEntity;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Date;

@ManagedBean(name = "signupBean")
public class SignupBean {
//    private UsersEntity user;

    // user properties
    private String id;
    private String firstName;
    private String lastName;

    private String password;
    private String phoneNumber;
    private String email;
    private Date birthDate;
    //private boolean isAdmin;

//    public SignupBean() {
//        initUserData();
//    }

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

    public String getPassword() {return password; }

    public void setPassword(String password) { this.password = password; }
    public String getPhoneNumber() {
        // insert separators in the right spots
        return phoneNumber;//phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;//phoneNumber.substring(0,3) + phoneNumber.substring(4,7) + phoneNumber.substring(8);
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

//    public String isAdminStr() {
//        return isAdmin ? "Admin" : "User";
//    }
//
//    public String isUser() {
//        return isAdmin ? "false" : "true";
//    }

//    public String isPreviewMode() {
//        return previewMode ? "true" : "false";
//    }
//
//    public String getStyleClass(boolean isEditable) {
//        return isEditable ? "" : "readonly";
//    }
//
    /**
     * Switch to edit mode (where some fields are editable) and reload the page
     * @return the page to redirect to (same page with reload of the bean)
     */
//    public String edit() {
//        previewMode = false;
//        return "myprofile.xhtml?faces-redirect=true";
//    }

    /**
     * persist the data to the DB and switch to back to preview mode, reload the page
     * @return the page to redirect to (same page with reload of the bean)
     */
    public String submit() {
        UsersEntity user = new UsersEntity(); //id, firstName, lastName, new java.sql.Date(birthDate.getTime()), password, phoneNumber, email, false);
        // persist changes to DB
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setBirthDate(new java.sql.Date(birthDate.getTime()));
        user.setAdmin(false);

        if (!new DBHandler().persistEntity(user, UsersEntity.class, user.getId())) {
            // init before sending in case of a DB error
            //initUserData();
            return "Signup.xhtml?faces-redirect=true";
        }

        return "index.xhtml?faces-redirect=true";
    }

//    public void initUserData() {
//        getLoggedInId();
//        user = new DBHandler().getUserById(loggedInId);
//        id = user.getId();
//        firstName = user.getFirstName();
//        lastName = user.getLastName();
//        phoneNumber = user.getPhoneNumber();
//        email = user.getEmail();
//        birthDate = user.getBirthDate();
//        isAdmin = user.isAdmin();
//    }

//    private void getLoggedInId() {
//        LoginBean instance = LoginBean.getInstance();
//        if (instance != null && instance.isLoggedIn())
//            loggedInId = instance.getId();
//    }
}
