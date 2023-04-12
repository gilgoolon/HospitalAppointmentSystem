package com.maorgil.hospitalappointmentsystem;

import com.maorgil.hospitalappointmentsystem.entity.AppointmentsEntity;
import com.maorgil.hospitalappointmentsystem.entity.WorkingHoursEntity;

import javax.faces.context.FacesContext;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
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
            return DBHandler.getInstance().getAppointment(Integer.parseInt(id));
        } catch (Exception e) {
            return null;
        }
    }

    public static String getAppointmentFileName(String doctorId, Timestamp startTime) {
        return "A" + doctorId + "_" + new SimpleDateFormat("dd-MM-yyyy").format(new Date(startTime.getTime())) + ".pdf";
    }

    public static List<Pair<Pair<LocalDateTime,LocalDateTime>, Integer>> getWorkingHoursRange(LocalDateTime start, LocalDateTime end, String doctorId) {
        List<WorkingHoursEntity> whs = DBHandler.getInstance().getDoctorHours(doctorId);

        List<Pair<Pair<LocalDateTime, LocalDateTime>, Integer>> result = new ArrayList<>();
        LocalDateTime curr = start;
        while (curr.isBefore(end) || curr.toLocalDate().isEqual(end.toLocalDate())) {

            WorkingHoursEntity currDayWH = getWHFromWeekday(whs, curr.getDayOfWeek());
            if (currDayWH != null) {
                LocalDateTime whStartDayTime, whEndDayTime;
                if (curr.with(currDayWH.getStartTime().toLocalTime()).isBefore(start)) {
                     whStartDayTime = start;
                }
                else {
                    whStartDayTime = curr.with(currDayWH.getStartTime().toLocalTime());
                }

                if (curr.with(currDayWH.getEndTime().toLocalTime()).isAfter(end)) {
                    whEndDayTime = end;
                }
                else {
                    whEndDayTime = curr.with(currDayWH.getEndTime().toLocalTime());
                }
                List<Pair<LocalDateTime,LocalDateTime>> occupiedRanges = getOccupiedRanges(curr.toLocalDate(), doctorId);
                if (occupiedRanges == null)
                    result.add(new Pair<>(new Pair<>(whStartDayTime, whEndDayTime), currDayWH.getAptLength()));
                else {
                    List<Pair<LocalDateTime,LocalDateTime>> freeRanges = getFreeRanges(whStartDayTime, whEndDayTime, occupiedRanges);
                    for (Pair<LocalDateTime,LocalDateTime> range : freeRanges) {
                        result.add(new Pair<>(range, currDayWH.getAptLength()));
                    }
                }
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

    public static List<AppointmentsEntity> splitFreeRange(Pair<Pair<LocalDateTime, LocalDateTime>, Integer> freeRange, String doctorId, String patientId) {
        List<AppointmentsEntity> freeAppointments = new ArrayList<>();

        LocalDateTime curr = freeRange.getFirst().getFirst();
        while (curr.isBefore((freeRange.getFirst().getSecond()))) {
            LocalDateTime currEnd = curr.plusMinutes(freeRange.getSecond());
            if (currEnd.isAfter(freeRange.getFirst().getSecond())) {
                break;
            }

            AppointmentsEntity appointmentsEntity = new AppointmentsEntity();
            appointmentsEntity.setStartTime(Timestamp.valueOf(curr));
            appointmentsEntity.setEndTime(Timestamp.valueOf(currEnd));
            appointmentsEntity.setDoctorId(doctorId);
            appointmentsEntity.setPatientId(patientId);

            freeAppointments.add(appointmentsEntity);
            curr = currEnd;
        }

        return freeAppointments;
    }

    public static List<Pair<LocalDateTime, LocalDateTime>> getOccupiedRanges(LocalDate date, String doctorId) {
        List<AppointmentsEntity> appointments = DBHandler.getInstance().getDoctorAppointmentAtDate(date, doctorId);
        List<Pair<LocalDateTime, LocalDateTime>>  occupiedRanges = new ArrayList<>();

        if (appointments.size() == 0)
            return null; // There are no occupied ranges for this date and doctor.

        for (AppointmentsEntity appointment: appointments) {
            occupiedRanges.add(new Pair<>(appointment.getStartTime().toLocalDateTime(), appointment.getEndTime().toLocalDateTime()));
        }

        return occupiedRanges;
    }

    public static List<Pair<LocalDateTime,LocalDateTime>> getFreeRanges(LocalDateTime start, LocalDateTime end, List<Pair<LocalDateTime,LocalDateTime>> occupiedRanges) {
        // Sort occupied slots by start time
        occupiedRanges.sort(Comparator.comparing(Pair::getFirst));

        // Initialize free slots to the entire time range
        List<Pair<LocalDateTime,LocalDateTime>> freeSlots = new ArrayList<>();
        freeSlots.add(new Pair<>(start, end));

        // Iterate over occupied slots and remove the corresponding time slot from the free slots list
        for (Pair<LocalDateTime,LocalDateTime> occupiedRange : occupiedRanges) {
            List<Pair<LocalDateTime,LocalDateTime>> newFreeSlots = new ArrayList<>();
            for (Pair<LocalDateTime,LocalDateTime> freeSlot : freeSlots) {
                // If occupied slot overlaps with free slot, split the free slot into two parts and keep the non-overlapping part
                if (occupiedRange.getFirst().isBefore(freeSlot.getSecond()) && occupiedRange.getSecond().isAfter(freeSlot.getFirst())) {
                    if (freeSlot.getFirst().isBefore(occupiedRange.getFirst())) {
                        newFreeSlots.add(new Pair<>(freeSlot.getFirst(), occupiedRange.getFirst()));
                    }
                    if (freeSlot.getSecond().isAfter(occupiedRange.getSecond())) {
                        newFreeSlots.add(new Pair<>(occupiedRange.getSecond(), freeSlot.getSecond()));
                    }
                } else {
                    // If occupied slot doesn't overlap with free slot, keep the free slot
                    newFreeSlots.add(freeSlot);
                }
            }
            freeSlots = newFreeSlots;
        }
        return freeSlots;
    }

    public static boolean isFreeAppointment(AppointmentsEntity appointment) {
        List<AppointmentsEntity> appointments = DBHandler.getInstance().getDoctorAppointmentAtDate(appointment.getStartTime().toLocalDateTime().toLocalDate(), appointment.getDoctorId());
        for (AppointmentsEntity a : appointments)
            if (a.getStartTime().toLocalDateTime().isBefore(appointment.getEndTime().toLocalDateTime()) && a.getEndTime().toLocalDateTime().isAfter(appointment.getStartTime().toLocalDateTime()))
                return false;
        return true;
    }

    public static void redirect(String url) {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                .handleNavigation(FacesContext.getCurrentInstance(), null, url);
    }

    private static String convertToGoogleCalendarDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
        return localDateTime.atZone(ZoneId.systemDefault()).format(formatter);
    }

    public static String datesToGoogleCalendarFormat(LocalDateTime start, LocalDateTime end) {
        String startDateTime = convertToGoogleCalendarDateTime(start);
        String endDateTime = convertToGoogleCalendarDateTime(end);
        return startDateTime + "/" + endDateTime;
    }
}
