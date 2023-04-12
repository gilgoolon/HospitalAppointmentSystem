package com.maorgil.hospitalappointmentsystem.entity;


import com.maorgil.hospitalappointmentsystem.DBHandler;

import javax.persistence.*;
import java.util.Objects;

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
        return DBHandler.getInstance().getUserById(getId()).getFirstName();
    }

    public String getLastName() {
        return DBHandler.getInstance().getUserById(getId()).getLastName();
    }

    public int getAge() {
        return DBHandler.getInstance().getUserById(getId()).getAge();
    }

    public String getTitle() {
        return getFirstName() + " " + getLastName() + ", " + getAge() + ", " + getType();
    }

    public String getPresentableName() {
        return "Dr. " + getFirstName() + " " + getLastName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoctorsEntity that = (DoctorsEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(type, that.type)) return false;
        if (!Objects.equals(about, that.about)) return false;
        return Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (about != null ? about.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getPresentableName();
    }
}
