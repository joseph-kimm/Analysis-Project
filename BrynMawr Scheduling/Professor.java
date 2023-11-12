import java.util.ArrayList;
import java.util.List;

public class Professor {
    protected String professorNumber;
    protected ArrayList<Time> teachingTimes = new ArrayList<>();

    public Professor(String professorN) {
        this.professorNumber = professorN;
    }

    public List<Time> getTeachingTimes() {
        return teachingTimes;
    }

    public String getProfessorNumber() {
        return professorNumber;
    }
}
