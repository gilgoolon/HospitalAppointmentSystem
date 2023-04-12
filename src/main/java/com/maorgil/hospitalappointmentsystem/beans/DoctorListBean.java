package com.maorgil.hospitalappointmentsystem.beans;

import com.maorgil.hospitalappointmentsystem.DBHandler;
import com.maorgil.hospitalappointmentsystem.entity.DoctorsEntity;
import com.maorgil.hospitalappointmentsystem.entity.WorkingHoursEntity;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean(name = "doctorsListBean")
// use an url request parameter to get the search term and maintain it in the bean
public class DoctorListBean {
    public String searchTerm = "";

    public void setInput(String s) {
        searchTerm = s;
    }

    public String getInput() {
        // get input from request parameters
        String in = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap().get("search");
        return in == null ? "" : in;
    }

    public String submit() {
        return "index.xhtml?faces-redirect=true&search=" + searchTerm;
    }

    public List<DoctorsEntity> getDoctors() {
        searchTerm = getInput(); // update search term from request parameters
        return DBHandler.getInstance().getDoctorsBySearch(searchTerm); // query the db
    }

    public List<WorkingHoursEntity> getWorkingHours(String doctorId) {
        return DBHandler.getInstance().getDoctorHours(doctorId);
    }
}
