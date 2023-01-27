package com.maorgil.hospitalappointmentsystem;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean(name = "loginBean")
public class loginBean {
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
