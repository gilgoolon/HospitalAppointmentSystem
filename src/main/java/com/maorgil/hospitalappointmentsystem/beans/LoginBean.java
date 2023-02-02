package com.maorgil.hospitalappointmentsystem.beans;

import com.maorgil.hospitalappointmentsystem.DBHandler;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@SessionScoped
@ManagedBean(name = "loginBean")
public class LoginBean {

    private String id = "";
    private String password = "";
    private String output = "";
    private boolean loggedIn = false;

    public void setId(String s) {
        id = s;
    }

    public String getId() {
        return id;
    }

    public void setPassword(String s) {
        password = s;
    }

    public String getPassword() {
        return password;
    }

    public String submit() {
        DBHandler dbHandler = new DBHandler();
        if (dbHandler.validateLogin(id, password)) {
            loggedIn = true;
            System.out.println("Logged in successfully");
            return "index.xhtml?faces-redirect=true";
        }
        else {
            loggedIn = false;
            if (dbHandler.getUserById(id) != null) {
                output = "Incorrect password, please try again";
            }
            else {
                output = "ID does not exist, please sign up";
            }
            return "";
        }
    }

    public String getOutput() {
        return output;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getSignupDisplay() {
        return loggedIn ? "none" : "block";
    }

    public static LoginBean getInstance() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return (LoginBean) facesContext.getExternalContext().getSessionMap().get("loginBean");
    }
}
