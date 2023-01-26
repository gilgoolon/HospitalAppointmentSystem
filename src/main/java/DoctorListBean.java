import com.maorgil.hospitalappointmentsystem.DBHandler;
import com.maorgil.hospitalappointmentsystem.entity.DoctorsEntity;

import javax.faces.bean.ManagedBean;
import java.util.Comparator;
import java.util.List;

@ManagedBean(name = "doctorsListBean")
public class DoctorListBean {
    private List<DoctorsEntity> doctors = new DBHandler().getDoctors();
    public String searchTerm = "";
    public String output;

    public void setInput(String s) {
        searchTerm = s;
    }

    public String getInput() {
        return searchTerm;
    }

    public void submit() {
        doctors = new DBHandler().getDoctorsBySearch(searchTerm);
        // sort by first name then last name
        doctors.sort(Comparator.comparing(DoctorsEntity::getFirstName).thenComparing(DoctorsEntity::getLastName));
    }

    public String getOutput() {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (DoctorsEntity doctor : doctors) {
            sb.append("<li>\n" +
            "<div class=\"dropdown title\">" +
            doctor.getTitle() +
            doctor.getHoursHTML() +
            "</div>\n" +
            "<br/>" +
            "<a class=\"about\">" + doctor.getAbout() + "</a>\n" +
            "</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
}
