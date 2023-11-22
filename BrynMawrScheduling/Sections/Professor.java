/**
 * Description:     Stores the identification number and teaching times for a single professor. 
 */

import java.util.ArrayList;
import java.util.List;

public class Professor {
    protected String professorNumber;
    protected ArrayList<Integer> teachingTimes = new ArrayList<>();

    public Professor(String professorN) {
        this.professorNumber = professorN;
    }

    public List<Integer> getTeachingTimes() {
        return teachingTimes;
    }

    public String getProfessorNumber() {
        return professorNumber;
    }

    public void addTeachingTime(int time_index) {
        teachingTimes.add(time_index);
    }
}
