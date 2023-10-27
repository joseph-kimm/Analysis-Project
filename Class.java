import java.util.ArrayList;

public class Class implements Comparable<Class>{
    protected int classNumber;
    protected int teacher; 
    protected int popularity; 
    protected int timeSlot; 
    protected int roomNumber;
    protected boolean[] students; 
    protected ArrayList<Integer> interestedStudents = new ArrayList<>();
    protected ArrayList<Integer> enrolledStudents = new ArrayList<>();

    public Class (int classNumber, int teacher) { 
        this.classNumber = classNumber;
        this.teacher = teacher; 
    }

    /*
     * Initializes the boolean array containing whether or not a student of a given index is in the class or not. 
     * All students begin as in the class, and conflicts are considered and removed once classes are placed into rooms and time slots. 
     * 
     * Originally this step was in the constructor, but at the time that we need to call the constructor, we have not read in the number of students, 
     * so this seemed like a reasonable workaround. 
     */
    public void initializeStudents(int numStudents) { 
        this.students = new boolean[numStudents + 1];
        for(int i = 1; i < students.length; i++ ) { 
            students[i] = false; 
        }
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
     * Mark a given student as in the class.  
     */
    public void addStudent(int student) { 
        students[student] = true; 
    }

    public void addInterestedStudent(int student) {
        this.interestedStudents.add(student);
    }

    public void addEnrolledStudent(int student) {
        this.enrolledStudents.add(student);
    }

    public ArrayList<Integer> getEnrolledStudent() {
        return this.enrolledStudents;
    }

    /*
     * Mark a given student as not in the class. 
     */
    public void removeStudent(int student) { 
        students[student] = false; 
    }

    public ArrayList<Integer> getStudentsList() { 
        ArrayList<Integer> studentsList = new ArrayList<Integer>(); 
        for (int i = 1; i < students.length; i++ ) { 
            if(students[i]) { // if the student is in the class
                studentsList.add(i); 
            }
        }
        return studentsList; 
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
