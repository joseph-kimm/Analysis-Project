import java.util.ArrayList;

public class Class implements Comparable<Class>{
    protected int index;
    protected String classNumber;
    protected Professor professor; 
    protected int popularity; 
    protected Time timeslot; 
    protected int roomNumber;
    protected ArrayList<String> interestedStudents = new ArrayList<>();
    protected ArrayList<String> enrolledStudents = new ArrayList<>();

    public Class (int index, String classNumber, Professor professor) { 
        this.index = index;
        this.classNumber = classNumber;
        this.professor = professor; 
    }

    /*
     * Increment the popularity of the class by a given amount
     * @param incBy the amount to increment the popularity by
     */
    public void incrementPopularity(int incBy) { 
        this.popularity += incBy; 
    }
    
    /*
     * Increment the popularity of the class by 1. 
     */
    public void incrementPopularity() { 
        this.popularity++; 
    }

    /*
     * add students who are interested in the course
     */
    public void addInterestedStudent(String student) {
        this.interestedStudents.add(student);
    }

    /*
     * add students who are enrolled in the course
     */
    public void addEnrolledStudent(String student) {
        this.enrolledStudents.add(student);
    }

    public ArrayList<String> getEnrolledStudent() {
        return this.enrolledStudents;
    }

    public int getIndex() {
        return index;
    }
    

    public String getClassNumber() {
        return this.classNumber;
    }
  
    public Professor getProfessor() { 
        return this.professor; 
    }

    public int getPopularity() { 
        return popularity; 
    }
     
    public Time getTimeSlot() { 
        return timeslot; 
    }
    
    public int getRoomNumber() { 
        return roomNumber; 
    }

    public void setClassNumber(String newClassNumber) { 
        this.classNumber = newClassNumber; 
    } 

    public void setTeacher(Professor professor) { 
        this.professor = professor;
    }

    public void setPopularity(int newPopularity) { 
        this.popularity = newPopularity; 
    }

    public void setTimeSlot(Time newTimeSlot) { 
        this.timeslot = newTimeSlot; 
    }
    
    public void setRoomNumber(int newRoomNumber) { 
        this.roomNumber = newRoomNumber;
    }

    /* 
     * compare to method. If positive, it means this is larger than that. 
     * @return The popularity of the input class - the popularity of this class. 
     */
    @Override 
    public int compareTo(Class thatClass) { 
        // if this returns a positive number, it means that thatClass is more popular than this class. 
        // This makes a little less sense than I'd typically do, but it means that when we call .sort() on the arraylist, 
        // it sorts in descending order, which is very handy. - R. 
        return thatClass.getPopularity() - this.popularity; 
    }

    public String toString() { 
        return index + ") class: " + classNumber + ", teacher: "+ professor.getProfessorNumber() + ", popularity: " + popularity + "}"; 
    }
}
