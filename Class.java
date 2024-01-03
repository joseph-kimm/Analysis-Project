/**
 * Description:     Stores the data for a single class available for a schedule, as well as a comparator based on popularity (interested students).
 */

import java.util.ArrayList;

public class Class implements Comparable<Class>{
    protected int classNumber;
    protected int teacher; 
    protected int popularity; 
    protected int timeSlot; 
    protected int roomNumber;
    protected ArrayList<Integer> interestedStudents = new ArrayList<>();
    protected ArrayList<Integer> enrolledStudents = new ArrayList<>();

    public Class (int classNumber, int teacher) { 
        this.classNumber = classNumber;
        this.teacher = teacher; 
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
     * Add a students interested in the course by their ID
     */
    public void addInterestedStudent(int student) {
        this.interestedStudents.add(student);
    }

    /*
     * add students who are enrolled in the course
     */
    public void addEnrolledStudent(int student) {
        this.enrolledStudents.add(student);
    }

    public ArrayList<Integer> getEnrolledStudent() {
        return this.enrolledStudents;
    }

    public int getClassNumber() {
        return this.classNumber;
    }
  
    public int getTeacher() { 
        return this.teacher; 
    }

    public int getPopularity() { 
        return popularity; 
    }
     
    public int getTimeSlot() { 
        return timeSlot; 
    }
    
    public int getRoomNumber() { 
        return roomNumber; 
    }

    public void setClassNumber(int newClassNumber) { 
        this.classNumber = newClassNumber; 
    } 

    public void setTeacher(int newTeacher) { 
        this.teacher = newTeacher;
    }

    public void setPopularity(int newPopularity) { 
        this.popularity = newPopularity; 
    }

    public void setTimeSlot(int newTimeSlot) { 
        this.timeSlot = newTimeSlot; 
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
        return "{class: " + classNumber + ", teacher: "+ teacher + ", popularity: " + popularity + "}"; 
    }
}
