package com.maorgil.hospitalappointmentsystem;

import com.maorgil.hospitalappointmentsystem.entity.AppointmentsEntity;

import java.time.LocalDateTime;
import java.util.Date;

public class AppointmentExporter {

    public static final String USER_APPOINTMENTS_PATH = "C:\\Users\\alper\\IdeaProjects\\HospitalAppointmentSystem\\src\\main\\resources\\appointments\\";

    /**
     * Creates a formal PDF file with the appointment details.
     * @param appointment the appointment entity to create the file for
     * @return the filename (with path) of the created file
     */
    public static String createAppointmentFile(AppointmentsEntity appointment) {
        String filename = Utils.getAppointmentFileName(appointment.getDoctorId(), appointment.getStartTime());

        PDFHandler pdfHandler = new PDFHandler(filename, USER_APPOINTMENTS_PATH);
        pdfHandler.createDocument();

        pdfHandler.setFooter(
                "© 2023 Hospital Appointment System\n" +
                "Contact Us @ 02-345-6789 | Email Us @ clinic@example.com");

        pdfHandler.addPublisher("Hospital Appointment System");

        pdfHandler.addDate(new Date()); // add current date
        pdfHandler.addTitle("Appointment Details");


        // create table with bordered cells
        pdfHandler.addInfoTable(4,
                "Patient ID", "Patient First Name", "Patient Last Name", "Doctor",
                "123456789", "John", "Doe", "Dr. John Smith",
                "Appointment Date", "Start Time", "End Time", "Appointment Purpose",
                "2023/01/01", "10:00", "11:00", "Checkup");

        // description text box
        pdfHandler.addSubTitle("Appointment Description by Doctor");
        pdfHandler.addBigTextBox("Skull fracture, left side, with subdural hematoma.");

        // specify when exactly the document was generated
        pdfHandler.addParagraph("Generated at: " + Utils.toString(LocalDateTime.now()), true);

        pdfHandler.closeDocument();

        return USER_APPOINTMENTS_PATH + filename;
    }

    public static String getFileName(AppointmentsEntity appointment) {
        return Utils.getAppointmentFileName(appointment.getDoctorId(), appointment.getStartTime());
    }
}
