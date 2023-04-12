package com.maorgil.hospitalappointmentsystem.entity;


import com.maorgil.hospitalappointmentsystem.DBHandler;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "appointments", schema = "hospital")
public class AppointmentsEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "start_time")
    private Timestamp startTime;
    @Basic
    @Column(name = "end_time")
    private Timestamp endTime;
    @Column(name = "doctor_id")
    private String doctorId;
    @Basic
    @Column(name = "patient_id")
    private String patientId;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "is_cancelled")
    private boolean isCancelled;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public boolean isCancellable() {
        return !isCancelled && !isPast();
    }

    public String getTitleForPatient() {
        return startTime.toLocalDateTime().toLocalDate().toString() + " w/ Dr. " + DBHandler.getInstance().getDoctorById(doctorId).getLastName();
    }

    public String getTitleForDoctor() {
        UsersEntity u = DBHandler.getInstance().getUserById(patientId);
        return startTime.toLocalDateTime().toLocalDate().toString() + " w/ " + u.getFirstName() + " " + u.getLastName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppointmentsEntity that = (AppointmentsEntity) o;

        if (isCancelled != that.isCancelled) return false;
        if (!Objects.equals(startTime, that.startTime)) return false;
        if (!Objects.equals(endTime, that.endTime)) return false;
        if (!Objects.equals(doctorId, that.doctorId)) return false;
        if (!Objects.equals(patientId, that.patientId)) return false;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = startTime != null ? startTime.hashCode() : 0;
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (doctorId != null ? doctorId.hashCode() : 0);
        result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (isCancelled ? 1 : 0);
        return result;
    }

    public boolean isPast() {
        return startTime.toLocalDateTime().isBefore(Timestamp.valueOf(java.time.LocalDateTime.now()).toLocalDateTime());
    }
}
