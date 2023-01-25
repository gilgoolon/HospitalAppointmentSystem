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
        for (DoctorsEntity doctor : doctors) {
            sb.append("<ul>" +
                    "<li>\n" +
                    "<label class=\"title\">" + doctor.getTitle() + "</label>\n" +
                    "<br/>" +
                    "<label class=\"about\">" + doctor.getAbout() + "</label>\n" +
                    "</li>" +
                    "</ul>");
        }

        return sb.toString();
    }

    /**
     *  Query the database for all the doctors and format them to html source code.
     * @return a string of html source code of a list of doctors.
     */
    public String getResult() {
        StringBuilder sb = new StringBuilder();
        for (DoctorsEntity doctor : doctors) {
            sb.append("<ul>" +
                    "<li>\n" +
                          "<label class=\"title\">" + doctor.getTitle() + "</label>\n" +
                          "<br/>" +
                          "<label class=\"about\">" + doctor.getAbout() +"</label>\n" +
                      "</li>" +
                    "</ul>");
        }

        return sb.toString();
    }
}
