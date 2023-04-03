package com.maorgil.hospitalappointmentsystem.beans;

import com.maorgil.hospitalappointmentsystem.DBHandler;
import com.maorgil.hospitalappointmentsystem.Utils;
import com.maorgil.hospitalappointmentsystem.entity.AppointmentsEntity;
import com.maorgil.hospitalappointmentsystem.Pair;
import com.maorgil.hospitalappointmentsystem.entity.DoctorsEntity;

import javax.faces.bean.ManagedBean;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
@ManagedBean(name = "makeAppointmentBean")
public class MakeAppointmentBean {

    public String getOutput() {
        List<AppointmentsEntity> appointments = selectAppointmentsForUser();
        return getAppointments(appointments);

    }

//    public static void main(String[] args) {
//        genFreeAppointments();
//    }

    public List<AppointmentsEntity> selectAppointmentsForUser() {
        List<DoctorsEntity> doctors = new DBHandler().getDoctors();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plus(1, ChronoUnit.WEEKS);
        List<AppointmentsEntity> freeAppointments = genFreeAppointments(start, end, doctors);
        return freeAppointments;
    }

    public static List<AppointmentsEntity> genFreeAppointments(LocalDateTime start, LocalDateTime end, List<DoctorsEntity> doctors) {
        List<AppointmentsEntity> freeAppointments = new ArrayList<>();
        for (DoctorsEntity doctor : doctors) {
            List<Pair<Pair<LocalDateTime, LocalDateTime>, Integer>> freeRanges = Utils.getWorkingHoursRange(start, end, doctor.getId());

            for (Pair<Pair<LocalDateTime, LocalDateTime>, Integer> range : freeRanges) {
                freeAppointments.addAll(Utils.splitFreeRange(range, doctor.getId()));
            }
        }

        for (AppointmentsEntity ap : freeAppointments) {
            System.out.println(ap);
        }
        return freeAppointments;
    }

    public String getAppointments(List<AppointmentsEntity> freeAppointments) {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (AppointmentsEntity appointment : freeAppointments) {
            sb
                    .append("<li>")
                    .append("<div class=\"dropdown title-small\">")
                    .append(appointment.getDoctorId())
                    .append(appointment.getStartTime())
                    .append(appointment.getEndTime())
                    .append("</div>")
                    .append("<br/>")
                    .append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
}
