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

    public void submit() {
        DBHandler dbHandler = new DBHandler();
        if (dbHandler.validateLogin(id,password)) {
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "index.xhtml");

        }
        else {
            if (dbHandler.getUserById(id) != null) {
                output = "Incorrect password, please try again";
            }
            else {
                output = "ID does not exist, please sign up";
            }
        }
    }

    public String getOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append(output);
        return sb.toString();
    }
}
