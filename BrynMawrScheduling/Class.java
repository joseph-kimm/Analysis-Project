/**
 * Description:     Stores the data for a single class available for a schedule, as well as a comparator based on popularity (interested students).
 */

import java.util.ArrayList;
import java.util.HashSet;

public class Class implements Comparable<Class>{
    protected int index;
    protected String classNumber;
    protected String professor; 
    protected int popularity; 
    protected int timeslot; 
    protected String roomName;
    protected int numUnenrolledStudents; 
    protected HashSet<Room> possibleRooms = new HashSet<>();
    protected ArrayList<String> interestedStudents = new ArrayList<>();
    protected ArrayList<String> enrolledStudents = new ArrayList<>();
    protected boolean placed = false;

    public Class (int index, String classNumber, String professor) { 
        this.index = index;
        this.classNumber = classNumber;
        this.professor = professor; 
    }

    public Class (int index, String classNumber, String professor, ArrayList<String> interestedStudents) { 
        this.index = index;
        this.classNumber = classNumber;
        this.professor = professor; 
        this.interestedStudents = interestedStudents;
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
     * returns the list of students interested in the class
     */
    public ArrayList<String> getInterestedStudents() {
        return interestedStudents;
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

    public void setNumUnenrolledStudents(int newNumUnenrolledStudents) { 
        this.numUnenrolledStudents = newNumUnenrolledStudents; 
    }

    public int getNumUnenrolledStudents() { 
        return this.numUnenrolledStudents; 
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

    public HashSet<Room> getPossibleRooms() {
        return possibleRooms;
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

    public void setPossibleRoom(Room someRoom) {
        this.possibleRooms.add(someRoom);
    }
    
    /* Check if a class can be placed in this room */
    public boolean safeRoom(Room someRoom) {
        return this.possibleRooms.contains(someRoom);
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
        // if the two have already been placed and have some unenrolled students, it instead sorts based on the difference in the number of unenrolled students. 
        return this.numUnenrolledStudents == 0 && thatClass.getNumUnenrolledStudents() == 0 ? thatClass.getPopularity() - this.popularity : thatClass.getNumUnenrolledStudents() - this.numUnenrolledStudents; 
    }

    public String toString() { 
        return index + ") class: " + classNumber + ", teacher: "+ professor + ", popularity: " + popularity + "}"; 
    }
}
