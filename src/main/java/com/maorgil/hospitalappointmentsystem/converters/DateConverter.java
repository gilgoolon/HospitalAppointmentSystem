package com.maorgil.hospitalappointmentsystem.converters;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("dateConverter")
public class DateConverter implements Converter<Date> {
    @Override
    public Date getAsObject(FacesContext context, UIComponent component, String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return sdf.parse(value);
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public String getAsString(FacesContext context, UIComponent component, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
}