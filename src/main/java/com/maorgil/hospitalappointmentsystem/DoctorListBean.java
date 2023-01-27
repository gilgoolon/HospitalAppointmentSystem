package com.maorgil.hospitalappointmentsystem;

import com.maorgil.hospitalappointmentsystem.entity.DoctorsEntity;

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

    public String getOutput() {
        searchTerm = getInput(); // update search term from request parameters
        List<DoctorsEntity> doctors = new DBHandler().getDoctorsBySearch(searchTerm); // query the db

        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (DoctorsEntity doctor : doctors) {
            sb
                    .append("<li>")
                        .append("<div class=\"dropdown title-small\">")
                            .append(doctor.getTitle())
                            .append(doctor.getHoursHTML())
                        .append("</div>")
                        .append("<br/>")
                        .append("<a class=\"text-small\">")
                            .append(doctor.getAbout())
                        .append("</a>")
                    .append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
}
