package com.maorgil.hospitalappointmentsystem;

import com.maorgil.hospitalappointmentsystem.entity.AppointmentsEntity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

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

    public static AppointmentsEntity idToAppointment(String id) {
        id = id.split("A")[1]; // using "A" as a delimiter for JSF id editing
        String[] parts = id.split("_");
        return new DBHandler().getAppointmentByPK(parts[0], Timestamp.valueOf(parts[1].replace("T", " ").replace("S", ":").replace("M", ".")));
    }

    public static String appointmentToId(AppointmentsEntity appointment) {
        return "A" + appointment.getDoctorId() + "_" + appointment.getStartTime().toString().replace(" ", "T").replace(":", "S").replace(".", "M");
    }

    public static String getAppointmentFileName(String doctorId, Timestamp startTime) {
        return "A" + doctorId + "_" + new SimpleDateFormat("dd-MM-yyyy").format(new Date(startTime.getTime())) + ".pdf";
    }
}
