package com.maorgil.hospitalappointmentsystem.beans;

import com.maorgil.hospitalappointmentsystem.AppointmentExporter;
import com.maorgil.hospitalappointmentsystem.Utils;
import com.maorgil.hospitalappointmentsystem.entity.AppointmentsEntity;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;

@ManagedBean(name = "appointmentExportBean")
public class AppointmentExportBean {
    public StreamedContent downloadAppointment(ComponentSystemEvent event) {
        String contentType = "application/pdf";
        // get id from request parameter
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        AppointmentsEntity appointment = Utils.idToAppointment(id);
        // prevent user from downloading the appointment if they are not eligible
        String isEligible = isUserEligible(appointment);
        if (isEligible != null) {
            // faces redirect to isEligible page
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, isEligible);
            return null;
        }

        String fileName = AppointmentExporter.getFileName(appointment);
        String fileLocation = AppointmentExporter.createAppointmentFile(appointment);

        FileInputStream fileInputStream;
        try {
            //noinspection resource
            fileInputStream = new FileInputStream(fileLocation);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileLocation);
            return null;
        }

        StreamedContent file = DefaultStreamedContent.builder()
                .name(fileName)
                .contentType(contentType)
                .stream(() -> fileInputStream)
                .build();

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.responseReset();
        externalContext.setResponseContentType(contentType);
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        externalContext.setResponseHeader("Content-Length", String.valueOf(file.getContentLength()));
        externalContext.addResponseCookie("fileDownload", "true", null);

        try (OutputStream output = externalContext.getResponseOutputStream()) {
            byte[] buffer = new byte[1024];
            // pdf file is binary, so we need to write it as is with no encoding
            while ((file.getStream().get().read(buffer)) != -1)
                output.write(buffer);
            output.flush();
        } catch (Exception e) {
            System.out.println("Error while writing file to output stream: " + e.getMessage());
        }

        FacesContext.getCurrentInstance().responseComplete();

        System.out.println("downloaded file: " + fileName + " with id: " + id);

        return file;
    }

    private String isUserEligible(AppointmentsEntity appointment) {
        // check if the user is eligible to download the appointment
        if (appointment == null || !appointment.getPatientId().equals(LoginBean.getInstance().getId()))
            return "index.xhtml?faces-redirect=true"; // redirect to index page if the appointment doesn't exist or the user is not the patient
        return null;
    }
}
