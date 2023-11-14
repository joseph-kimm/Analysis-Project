/**
 * Description:     Stores the data for a single class available for a schedule, as well as a comparator based on popularity (interested students).
 * Last Modified:   Nov 12, 2023
 */

import java.util.ArrayList;

public class Class implements Comparable<Class>{
    protected int index;
    protected String classNumber;
    protected String professor; 
    protected int popularity; 
    protected int timeslot; 
    protected String roomName;
    protected ArrayList<String> interestedStudents = new ArrayList<>();
    protected ArrayList<String> enrolledStudents = new ArrayList<>();
    protected boolean placed = false;

    public Class (int index, String classNumber, String professor) { 
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
  
    public String getProfessor() { 
        return this.professor; 
    }

    public int getPopularity() { 
        return popularity; 
    }
     
    public int getTimeSlot() { 
        return timeslot; 
    }
    
    public String getRoomName() { 
        return roomName; 
    }

    public boolean getPlaced() {
        return placed;
    }

    public void setTeacher(String professor) { 
        this.professor = professor;
    }

    public void setPopularity(int newPopularity) { 
        this.popularity = newPopularity; 
    }

    public void setTimeSlot(int newTimeSlot) { 
        this.timeslot = newTimeSlot; 
    }
    
    public void setRoomName(String newRoomName) { 
        this.roomName = newRoomName;
    }

    public void setPlaced(boolean newPlaced) {
        this.placed = newPlaced;
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
        return index + ") class: " + classNumber + ", teacher: "+ professor + ", popularity: " + popularity + "}"; 
    }
}
