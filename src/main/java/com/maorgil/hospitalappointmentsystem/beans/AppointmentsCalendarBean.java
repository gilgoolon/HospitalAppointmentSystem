package com.maorgil.hospitalappointmentsystem.beans;

import com.maorgil.hospitalappointmentsystem.DBHandler;
import com.maorgil.hospitalappointmentsystem.Pair;
import com.maorgil.hospitalappointmentsystem.Utils;
import com.maorgil.hospitalappointmentsystem.entity.AppointmentsEntity;
import com.maorgil.hospitalappointmentsystem.entity.DoctorsEntity;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@ViewScoped
@ManagedBean(name = "appointmentsCalendarBean")
public class AppointmentsCalendarBean implements Serializable {
    private ScheduleModel eventModel;

    private AppointmentsEntity lastClickedAppointment;

    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private String doctorDetails;
    private String location;

    public AppointmentsCalendarBean() {
        init();
    }

    // NEED TO RESET THE APPOINTMENT FORM BEAN AFTER CHOOSING AN APPOINTMENT
    public void init() {
        eventModel = new DefaultScheduleModel();
        AppointmentFormBean.FormResults preferences = AppointmentFormBean.latestResults;

        // Get available block times from the database
        List<AppointmentsEntity> availableBlockTimes = genAppointments(preferences);
        DBHandler dbHandler = DBHandler.getInstance();

        // Create a ScheduleEvent for each available block time and add it to the event model
        for (AppointmentsEntity blockTime : availableBlockTimes) {
            LocalDateTime startDate = blockTime.getStartTime().toLocalDateTime();
            LocalDateTime endDate = blockTime.getEndTime().toLocalDateTime();
            String title = dbHandler.getDoctorById(blockTime.getDoctorId()).getPresentableName();
            // create an event
            DefaultScheduleEvent<AppointmentsEntity> event =
                    DefaultScheduleEvent.<AppointmentsEntity>builder()
                            .startDate(startDate)
                            .endDate(endDate)
                            .title(title)
                            .data(blockTime)
                            .build();

            eventModel.addEvent(event);
        }
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDoctorDetails() {
        return doctorDetails;
    }

    public String getLocation() {
        return location;
    }

    private static List<AppointmentsEntity> genAppointments(AppointmentFormBean.FormResults preferences) {
        List<DoctorsEntity> doctors;
        HashSet<DoctorsEntity> doctorsSet;
        DBHandler dbHandler = DBHandler.getInstance();
        if (preferences.isByCategory()) {
            doctorsSet = new HashSet<>();
            for (String category : preferences.getCategories()) {
                if (preferences.getLocations().isEmpty()) {
                       doctorsSet.addAll(dbHandler.getDoctorsByType(category));
                }
                else {
                    for (String location : preferences.getLocations()) {
                        doctorsSet.addAll(dbHandler.getDoctorsByTypeAndLocation(category, location));
                    }
                }
            }
            doctors = new ArrayList<>(doctorsSet);
        }
        else {
            doctors = preferences.getDoctors();
        }

        LocalDateTime start = preferences.getFromTime();
        LocalDateTime end = preferences.getToTime();

        List<AppointmentsEntity> freeAppointments = new ArrayList<>();
        for (DoctorsEntity doctor : doctors) {
            // skip this doctor if they're the connected user
            if (doctor.getId().equals(LoginBean.getInstance().getId()))
                continue;

            List<Pair<Pair<LocalDateTime, LocalDateTime>, Integer>> freeRanges = Utils.getWorkingHoursRange(start, end, doctor.getId());

            for (Pair<Pair<LocalDateTime, LocalDateTime>, Integer> range : freeRanges) {
                freeAppointments.addAll(Utils.splitFreeRange(range, doctor.getId(), LoginBean.getInstance().getId()));
            }
        }
        return freeAppointments;
    }

    public void onEventSelect(SelectEvent<ScheduleEvent<AppointmentsEntity>> event) {
        lastClickedAppointment = event.getObject().getData();

        startTime = lastClickedAppointment.getStartTime().toLocalDateTime().toLocalTime();
        endTime = lastClickedAppointment.getEndTime().toLocalDateTime().toLocalTime();
        date = lastClickedAppointment.getStartTime().toLocalDateTime().toLocalDate();
        DoctorsEntity doctor = DBHandler.getInstance().getDoctorById(lastClickedAppointment.getDoctorId());
        doctorDetails = doctor.getPresentableName() + "/" + doctor.getType();
        location = doctor.getCity();

        PrimeFaces.current().ajax().update("eventDetailsDialog");

        // Show the dialog
        PrimeFaces.current().executeScript("PF('eventDetailsDialog').show();");
    }

    public void reserve() {
        // reserve event
        AppointmentsEntity toReserve = lastClickedAppointment;

        // check if the appointment is still available
        boolean isFree = Utils.isFreeAppointment(toReserve);

        // reserve event and add to the DB
        if (isFree) {
            toReserve.setId(DBHandler.getInstance().genAppointmentUUID());
            DBHandler.getInstance().persistEntity(toReserve, AppointmentsEntity.class, toReserve.getId());
        }

        // reset the appointment form bean
        AppointmentFormBean.reset();

        if (isFree) {
            // redirect to success page
            Utils.redirect("appointmentSuccess.xhtml?faces-redirect=true&appId=" + toReserve.getId());
        } else {
            // redirect to fail page
            Utils.redirect("appointmentFailure.xhtml?faces-redirect=true");
        }
    }
}
