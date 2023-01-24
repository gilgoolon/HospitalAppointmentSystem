package com.maorgil.hospitalappointmentsystem.entity;


import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

public class AppointmentsEntityPK implements Serializable {
    @Column(name = "start_time")
    @Id
    private Timestamp startTime;
    @Column(name = "doctor_id")
    @Id
    private String doctorId;

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppointmentsEntityPK that = (AppointmentsEntityPK) o;

        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;
        if (doctorId != null ? !doctorId.equals(that.doctorId) : that.doctorId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startTime != null ? startTime.hashCode() : 0;
        result = 31 * result + (doctorId != null ? doctorId.hashCode() : 0);
        return result;
    }
}
