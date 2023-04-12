package com.maorgil.hospitalappointmentsystem;

import com.maorgil.hospitalappointmentsystem.entity.AppointmentsEntity;
import com.maorgil.hospitalappointmentsystem.entity.UsersEntity;

import java.time.LocalDateTime;
import java.util.Date;

public class AppointmentExporter {

    public static final String USER_APPOINTMENTS_PATH = "user.dir\\IdeaProjects\\Hospital-Appointment-System\\src\\main\\resources\\appointments\\";

    private final static String FOOTER = "© 2023 Hospital Appointment System\n" +
            "Contact Us @ 02-345-6789 | Email Us @ clinic@example.com";
    private final static String PUBLISHER = "Hospital Appointment System";
    private final static String TITLE = "Hospital Appointment";

    /**
     * Creates a formal PDF file with the appointment details.
     * @param appointment the appointment entity to create the file for
     * @return the filename (with path) of the created file
     */
    public static String createAppointmentFile(AppointmentsEntity appointment) {
        String filename = Utils.getAppointmentFileName(appointment.getDoctorId(), appointment.getStartTime());

        DBHandler dbHandler = new DBHandler();
        UsersEntity patient = dbHandler.getUserById(appointment.getPatientId());
        UsersEntity doctor = dbHandler.getUserById(appointment.getDoctorId());

        String infoPatientId = patient.getId();
        String infoPatientFirstName = patient.getFirstName();
        String infoPatientLastName = patient.getLastName();
        String infoDoctor = doctor.toString();
        String infoAppointmentDate = Utils.toString(appointment.getStartTime().toLocalDateTime().toLocalDate());
        String infoStartTime = Utils.toString(appointment.getStartTime().toLocalDateTime().toLocalTime());
        String infoEndTime = Utils.toString(appointment.getEndTime().toLocalDateTime().toLocalTime());
        String infoAppointmentDescription = appointment.getDescription();
        if (infoAppointmentDescription == null) infoAppointmentDescription = "No description";

        PDFHandler pdfHandler = new PDFHandler(filename, USER_APPOINTMENTS_PATH);
        pdfHandler.createDocument();

        pdfHandler.setFooter(FOOTER);

        pdfHandler.addPublisher(PUBLISHER);
        pdfHandler.addDate(new Date()); // add current date
        pdfHandler.addTitle(TITLE);

        // create table with bordered cells
        pdfHandler.addInfoTable(4,
                "Patient ID", "Patient First Name", "Patient Last Name", "Doctor",
                infoPatientId, infoPatientFirstName, infoPatientLastName, infoDoctor,
                "Appointment Date", "Start Time", "End Time", "Cancelled",
                infoAppointmentDate, infoStartTime, infoEndTime, appointment.isCancelled() ? "Yes" : "No");

        // description text box
        pdfHandler.addSubTitle("Appointment Description by Doctor");
        pdfHandler.addBigTextBox(infoAppointmentDescription);

        // specify when exactly the document was generated
        pdfHandler.addParagraph("Generated at: " + Utils.toString(LocalDateTime.now()), true);

        pdfHandler.closeDocument();

        return USER_APPOINTMENTS_PATH + filename;
    }

    public static String getFileName(AppointmentsEntity appointment) {
        return Utils.getAppointmentFileName(appointment.getDoctorId(), appointment.getStartTime());
    }
}
