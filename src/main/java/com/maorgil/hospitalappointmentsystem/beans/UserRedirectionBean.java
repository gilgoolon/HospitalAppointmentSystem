package com.maorgil.hospitalappointmentsystem.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import java.io.IOException;

@ViewScoped
@ManagedBean(name = "userRedirectionBean")
public class UserRedirectionBean {

    /**
     * Checks if the user is logged in, and if not, redirects to the login page
     */
    public void checkLoggedIn(ComponentSystemEvent event) {
        LoginBean loginBean = LoginBean.getInstance();
        if (loginBean == null || !loginBean.isLoggedIn()) {
            // redirect to the login page
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}