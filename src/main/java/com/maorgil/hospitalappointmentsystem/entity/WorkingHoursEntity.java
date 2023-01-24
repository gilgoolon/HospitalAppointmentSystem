package com.maorgil.hospitalappointmentsystem.entity;


import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "working_hours", schema = "hospital")
@IdClass(WorkingHoursEntityPK.class)
public class WorkingHoursEntity {
    @Id
    @Column(name = "doctor_id")
    private String doctorId;
    @Id
    @Column(name = "week_day")
    private int weekDay;
    @Basic
    @Column(name = "start_time")
    private Time startTime;
    @Basic
    @Column(name = "end_time")
    private Time endTime;
    @Basic
    @Column(name = "apt_length")
    private int aptLength;

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

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public int getAptLength() {
        return aptLength;
    }

    public void setAptLength(int aptLength) {
        this.aptLength = aptLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkingHoursEntity that = (WorkingHoursEntity) o;

        if (weekDay != that.weekDay) return false;
        if (aptLength != that.aptLength) return false;
        if (doctorId != null ? !doctorId.equals(that.doctorId) : that.doctorId != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = doctorId != null ? doctorId.hashCode() : 0;
        result = 31 * result + weekDay;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + aptLength;
        return result;
    }
}
