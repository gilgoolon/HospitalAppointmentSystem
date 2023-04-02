package com.maorgil.hospitalappointmentsystem;

import com.maorgil.hospitalappointmentsystem.entity.AppointmentsEntity;
import com.maorgil.hospitalappointmentsystem.entity.WorkingHoursEntity;
import com.maorgil.hospitalappointmentsystem.Pair;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {
    public static int count(String source, String delimiter) {
        int count = 0;
        int index = 0;
        while ((index = source.indexOf(delimiter, index)) != -1) {
            index += delimiter.length();
            count++;
        }
        return count;
    }

    public static String toString(LocalDateTime dateTime) {
        return dateTime.toString().replace("T", " ").split("\\.")[0]; // remove milliseconds
    }

    public static String toString(LocalTime time) {
        // remove milliseconds and seconds
        return time.toString().split("\\.")[0].split(":")[0] + ":" + time.toString().split("\\.")[0].split(":")[1];
    }

    public static String toString(LocalDate date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(date.toString().replace("-", "/")));
    }

    public static AppointmentsEntity idToAppointment(String id) {
        try {
            id = id.split("A")[1]; // using "A" as a delimiter for JSF id editing
            String[] parts = id.split("_");
            return new DBHandler().getAppointmentByPK(parts[0], Timestamp.valueOf(parts[1].replace("T", " ").replace("S", ":").replace("M", ".")));
        } catch (Exception e) {
            return null;
        }
    }

    public static String appointmentToId(AppointmentsEntity appointment) {
        return "A" + appointment.getDoctorId() + "_" + appointment.getStartTime().toString().replace(" ", "T").replace(":", "S").replace(".", "M");
    }

    public static String getAppointmentFileName(String doctorId, Timestamp startTime) {
        return "A" + doctorId + "_" + new SimpleDateFormat("dd-MM-yyyy").format(new Date(startTime.getTime())) + ".pdf";
    }

    public static List<Pair<Pair<LocalDateTime,LocalDateTime>, Integer>> getWorkingHoursRange(LocalDateTime start, LocalDateTime end, String doctorId) {
        List<WorkingHoursEntity> whs = new DBHandler().getDoctorHours(doctorId);

        List<Pair<Pair<LocalDateTime, LocalDateTime>, Integer>> result = new ArrayList<>();
        LocalDateTime curr = start;
        while (curr.isBefore(end) || curr.toLocalDate().isEqual(end.toLocalDate())) {

            WorkingHoursEntity currDayWH = getWHFromWeekday(whs, curr.getDayOfWeek());
            if (currDayWH != null) {
                LocalDateTime whStartDayTime = curr.with(currDayWH.getStartTime().toLocalTime());
                LocalDateTime whEndDayTime = curr.with(currDayWH.getEndTime().toLocalTime());
                result.add(new Pair<>(new Pair<>(whStartDayTime, whEndDayTime), currDayWH.getAptLength()));
            }

            // increment start date by one day
            curr = curr.plusDays(1);
        }

        return result;
    }

    public static WorkingHoursEntity getWHFromWeekday(List<WorkingHoursEntity> whs, DayOfWeek day) {
        for (WorkingHoursEntity wh : whs)
            if (wh.getWeekDay() == day.getValue())
                return wh;
        return null; // not working at this dow
    }

    public static List<AppointmentsEntity> splitFreeRange(Pair<Pair<LocalDateTime, LocalDateTime>, Integer> freeRange, String doctorId) {
        List<AppointmentsEntity> freeAppointments = new ArrayList<>();

        LocalDateTime curr = freeRange.getFirst().getFirst();
        while (curr.isBefore((freeRange.getFirst().getSecond()))) {
            LocalDateTime currEnd = curr.plusMinutes(freeRange.getSecond());
            AppointmentsEntity appointmentsEntity = new AppointmentsEntity();
            appointmentsEntity.setStartTime(Timestamp.valueOf(curr));
            appointmentsEntity.setEndTime(Timestamp.valueOf(currEnd));
            appointmentsEntity.setDoctorId(doctorId);

            freeAppointments.add(appointmentsEntity);

            curr = currEnd;
        }

        return freeAppointments;
    }
}
