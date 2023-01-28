package com.maorgil.hospitalappointmentsystem.validators;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("birthDateValidator")
public class BirthDateValidator implements Validator<Date> {

    public static final int MAX_AGE = 120;


    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Date date) throws ValidatorException {
        LocalDate now = LocalDate.now();
        LocalDate a120YearsAgo = now.minusYears(120);
        LocalDate dateInLocal = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (dateInLocal.isBefore(a120YearsAgo) || dateInLocal.isAfter(now)) {
            FacesMessage message = new FacesMessage("Invalid date, it should be between " + MAX_AGE + " years ago and now.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
