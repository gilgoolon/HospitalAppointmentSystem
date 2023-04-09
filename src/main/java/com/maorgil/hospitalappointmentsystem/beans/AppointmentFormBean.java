package com.maorgil.hospitalappointmentsystem.beans;

import com.maorgil.hospitalappointmentsystem.DBHandler;
import com.maorgil.hospitalappointmentsystem.entity.DoctorsEntity;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@ManagedBean(name = "appointmentFormBean")
public class AppointmentFormBean implements Serializable {

    private static AppointmentFormBean instance;

    protected static FormResults latestResults;

    private boolean byCategory = true;

    private String selectedCategory; // selected doctor - doctor id
    private final List<String> categories = new ArrayList<>();
    private final List<String> selectedCategories = new ArrayList<>();

    private String selectedDoctor; // selected doctor - doctor id
    private final List<DoctorsEntity> doctors = new ArrayList<>();
    private final List<DoctorsEntity> selectedDoctors = new ArrayList<>();

    private boolean submitted;

    public AppointmentFormBean() {
        instance = this;
        initSelections();
    }

    public String getSelectedDoctor() {
        return selectedDoctor;
    }

    public void setSelectedDoctor(String selectedValue) {
        this.selectedDoctor = selectedValue;
    }

    public List<DoctorsEntity> getSelectedDoctors() {
        return selectedDoctors;
    }

    public List<DoctorsEntity> getDoctors() {
        return doctors;
    }

    public void onDoctorChanged(AjaxBehaviorEvent event) {
        // Add selected item to selected items list
        DoctorsEntity selected = doctors.stream().filter(doctor -> doctor.getId().equals(selectedDoctor)).findFirst().orElse(null);
        doctors.remove(selected);
        selectedDoctors.add(selected);
    }

    public void onRemoveDoctor(String id) {
        // Remove item from selected items list
        DoctorsEntity selected = selectedDoctors.stream().filter(doctor -> doctor.getId().equals(id)).findFirst().orElse(null);
        selectedDoctors.remove(selected);
        doctors.add(selected);
        selectedDoctor = "";
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedValue) {
        this.selectedCategory = selectedValue;
    }

    public List<String> getSelectedCategories() {
        return selectedCategories;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void onCategoryChanged(AjaxBehaviorEvent event) {
        // Add selected item to selected items list
        categories.remove(selectedCategory);
        selectedCategories.add(selectedCategory);
    }

    public void onRemoveCategory(String cat) {
        selectedCategories.remove(cat);
        categories.add(cat);
    }

    public boolean isByCategory() {
        return byCategory;
    }

    public void setByCategory(boolean category) {
        byCategory = category;
    }

    public String submit() {
        // save the form results to usable form for calendar loading
        latestResults = new FormResults(selectedDoctors, selectedCategories, byCategory);

        // refresh the page for loading calendar
        submitted = true;
        return "make-appointment.xhtml?faces-redirect=true";
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public String back() {
        submitted = false;
        return "make-appointment.xhtml?faces-redirect=true";
    }

    public void initSelections() {
        DBHandler dbHandler = new DBHandler();
        doctors.clear();
        selectedDoctors.clear();
        doctors.addAll(dbHandler.getDoctors());
        // remove the logged in doctor from the list if they are a doctor
        doctors.removeIf(doctor -> doctor.getId().equals(LoginBean.getInstance().getId()));
        categories.clear();
        selectedCategories.clear();
        categories.addAll(dbHandler.getCategories());
    }

    public static void reset() {
        latestResults = null;
        instance.initSelections();
        instance.submitted = false;
    }

    protected static class FormResults {
        private final List<DoctorsEntity> doctors;
        private final List<String> categories;
        private final boolean byCategory;

        public FormResults(List<DoctorsEntity> doctors, List<String> categories, boolean byCategory) {
            this.doctors = new ArrayList<>(doctors);
            this.categories = new ArrayList<>(categories);
            this.byCategory = byCategory;
        }

        public List<DoctorsEntity> getDoctors() {
            return doctors;
        }

        public List<String> getCategories() {
            return categories;
        }

        public boolean isByCategory() {
            return byCategory;
        }
    }
}