package com.maorgil.hospitalappointmentsystem.entity;


import com.maorgil.hospitalappointmentsystem.DBHandler;

import javax.persistence.*;

@Entity
@Table(name = "doctors", schema = "hospital")
public class DoctorsEntity {
    @Id
    @Column(name = "id")
    private String id;
    @Basic
    @Column(name = "type")
    private String type;
    @Basic
    @Column(name = "about")
    private String about;
    @Basic
    @Column(name = "city")
    private String city;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFirstName() {
        DBHandler dbh = new DBHandler();
        return dbh.getUserById(getId()).getFirstName();
    }

    public String getLastName() {
        DBHandler dbh = new DBHandler();
        return dbh.getUserById(getId()).getLastName();
    }

    public int getAge() {
        DBHandler dbh = new DBHandler();
        return dbh.getUserById(getId()).getAge();
    }

    public String getTitle() {
        return getFirstName() + " " + getLastName() + ", " + getAge() + ", " + getType();
    }

    /**
     * Get the working hours of the doctor in html ul format.
     * @return a string of html source code of a list of working hours.
     */
    public String getHoursHTML() {
        DBHandler dbHandler = new DBHandler();
        StringBuilder sb = new StringBuilder();

        sb.append("<div class=\"dropdown-content\">");
        sb.append("<div class=\"content\">");

        // title of dropdown
        sb.append("<a class=\"title-small\">").append(getCity()).append("</a>");

        // working hours
        sb.append("<ul>");
        for (WorkingHoursEntity wh : dbHandler.getDoctorHours(getId()))
            sb.append("<li>").append(wh).append("</li>");
        sb.append("</ul>");
        sb.append("</div>");
        sb.append("</div>");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoctorsEntity that = (DoctorsEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (about != null ? !about.equals(that.about) : that.about != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (about != null ? about.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }
}
