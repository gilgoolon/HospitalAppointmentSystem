import com.maorgil.hospitalappointmentsystem.DBHandler;
import com.maorgil.hospitalappointmentsystem.entity.DoctorsEntity;

import javax.faces.bean.ManagedBean;
import java.util.List;

@ManagedBean(name = "doctorsListBean")
public class DoctorListBean {
    private final List<DoctorsEntity> doctors;

    public DoctorListBean() {
        DBHandler dbHandler = new DBHandler();
        doctors = dbHandler.getDoctors();
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
