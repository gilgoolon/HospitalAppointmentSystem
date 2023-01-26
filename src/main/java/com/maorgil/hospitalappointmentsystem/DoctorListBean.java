package com.maorgil.hospitalappointmentsystem;

import com.maorgil.hospitalappointmentsystem.entity.DoctorsEntity;

import javax.faces.bean.ManagedBean;
import java.util.List;

@ManagedBean(name = "doctorsListBean")
public class DoctorListBean {
    private List<DoctorsEntity> doctors = new DBHandler().getDoctors();
    public String searchTerm = "";
    public String output;

    public void setInput(String s) {
        searchTerm = s;
    }

    public String getInput() {
        return searchTerm;
    }

    public void submit() {
        doctors = new DBHandler().getDoctorsBySearch(searchTerm);
    }

    public String getOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (DoctorsEntity doctor : doctors) {
            sb.append("<li>" +
            "<div class=\"dropdown title-small\">" +
            doctor.getTitle() +
            doctor.getHoursHTML() +
            "</div>" +
            "<br/>" +
            "<a class=\"text-small\">" + doctor.getAbout() + "</a>" +
            "</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
}
