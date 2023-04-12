package com.maorgil.hospitalappointmentsystem.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class WorkingHoursEntityPK implements Serializable {
    @Column(name = "doctor_id")
    @Id
    private String doctorId;
    @Column(name = "week_day")
    @Id
    private int weekDay;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkingHoursEntityPK that = (WorkingHoursEntityPK) o;

        if (weekDay != that.weekDay) return false;
        return Objects.equals(doctorId, that.doctorId);
    }

    @Override
    public int hashCode() {
        int result = doctorId != null ? doctorId.hashCode() : 0;
        result = 31 * result + weekDay;
        return result;
    }
}
