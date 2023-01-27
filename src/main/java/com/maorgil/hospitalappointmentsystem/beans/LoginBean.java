package com.maorgil.hospitalappointmentsystem.beans;

import com.maorgil.hospitalappointmentsystem.DBHandler;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "loginBean")
public class LoginBean {
    public String id = "";
    public String password = "";
    public String output = "";

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
        if (dbHandler.validateLogin(id,password)) {
            return "index.xhtml?faces-redirect=true";
        }
        else {
            if (dbHandler.getUserById(id) != null) {
                output = "Incorrect password, please try again";
            }
            else {
                output = "ID does not exist, please sign up";
            }
        }
        return "";
    }

    public String getOutput() {
        return output;
    }
}
