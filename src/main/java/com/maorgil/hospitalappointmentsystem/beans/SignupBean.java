package com.maorgil.hospitalappointmentsystem.beans;

import com.maorgil.hospitalappointmentsystem.DBHandler;
import com.maorgil.hospitalappointmentsystem.entity.UsersEntity;

import javax.faces.bean.ManagedBean;
import java.util.Date;

@ManagedBean(name = "signupBean")
public class SignupBean {

    // user properties
    private String id = "";
    private String firstName = "";
    private String lastName = "";

    private String password = "";
    private String phoneNumber = "";
    private String email = "";
    private Date birthDate;
    private String output = "";
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
    public String getOutput() {
        return output;
    }

    public void setPassword(String password) { this.password = password; }
    public String getPhoneNumber() {
        // insert separators in the right spots
        if (!phoneNumber.isEmpty())
            return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6);
        return "";
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

    /**
     * persist the data to the DB and switch to back to preview mode, reload the page
     * @return the page to redirect to (same page with reload of the bean)
     */
    public String submit() {
        UsersEntity user = new UsersEntity(); //id, firstName, lastName, new java.sql.Date(birthDate.getTime()), password, phoneNumber, email, false);
        DBHandler dbHandler = DBHandler.getInstance();

        if (dbHandler.getUserById(id) != null) {
            output = "Id already exist";
            return "";
        }
        // persist changes to DB
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setBirthDate(new java.sql.Date(birthDate.getTime()));
        user.setAdmin(false);

        if (!dbHandler.persistEntity(user, UsersEntity.class, user.getId())) {
            // init before sending in case of a DB error
            output = "Error while sending data to DB";
            return "";
        }

        return "index.xhtml?faces-redirect=true";
    }
}
