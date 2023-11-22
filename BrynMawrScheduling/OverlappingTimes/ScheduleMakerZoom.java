import java.io.BufferedReader; 
import java.io.FileNotFoundException;
import java.io.FileReader; 
import java.io.IOException; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.io.FileWriter;

public class ScheduleMakerZoom {
    private static float bestCaseValue = 0;
    private static float studentEnrolledValue = 0;
    
    private int numTimeSlots; // stores the time slots specified in the input file.
    private ArrayList<Time> timeSlots = new ArrayList<Time>(); 
    private boolean[][] timeConflict; // 2d array storing whether there is conflict between two tiemslows

    private int numRooms; // stores the number of rooms specified in the input file. 
    private ArrayList<Room> rooms = new ArrayList<Room>(); //rooms, the only one with 0 index

    private int numClasses; // number of classes specified in the input file.
    private ArrayList<String> classNumbers = new ArrayList<>(); // accessing each class by index rather than class number
    private HashMap<String, Class> classes = new HashMap<String, Class>();
    private int[][] conflict; // a 2d array storing the number of conflicts between every class and every other class. 

    private int numProfessors; // number of teachers specified in the input file. 
    private HashMap<String, Professor> professors = new HashMap<>(); //

    private int numStudents; // number of students specified in the input file. 
    private ArrayList<Edge> edges = new ArrayList<>(); // conflict between 2 classes in increasing order of popularity
    private long nanoSecondsElapsed; 

    private int zeroConflictEdges = 0;
    private int nonZeroConflictEdges = 0;

    private int moreInterestedThanSize = 0;

    public static void main(String[] args) { 

        if (args.length != 4) {
            System.out.println("Usage: java <o(overlap) / n(non-overlap)> <constraints.txt> <student_preferences.txt> <schedule.txt>");
            return;
        }

        String timeChoice = args[0];
        String constraint = args[1]; // store constraint and preference files. 
        String studentPref = args[2];

        ScheduleMakerZoom maker = new ScheduleMakerZoom();
        maker.readingInput(constraint, studentPref); // read in the input from the constrainta and preference files to instance variables. 
        maker.createClassPairs();
        maker.createTimeMatrix(timeChoice);
        maker.makeSchedule();
        maker.writeSchedule(args[3]);

        System.out.println("Student Preference Value: " + studentEnrolledValue);
        System.out.println("Best Case Student Value: " + bestCaseValue);
        System.out.printf("Fit: %2.2f%%%n", studentEnrolledValue/bestCaseValue * 100);
    }
    
    /* 
     * Given a filename, will initialize class instance variables. 
     * @param constraintsFile The file containing information about constraints
     * @param studentFile The file containing information about student preferences. 
     */
    public void readingInput(String constraintsFile, String studentFile) { 

        // Read values from the constraints file:  
        try (BufferedReader br = new BufferedReader(new FileReader(constraintsFile))) { 

            // store number of TimeSlots
            numTimeSlots = Integer.parseInt(br.readLine().split("\\s+")[2]); 

            // add dummy time slot for indexing
            timeSlots.add(new Time(0, 0, 0, 0, 0, Collections.<String>emptySet()));

            // initialize days of the week
            char[] daysOfTheWeek = {'M', 'T', 'W', 'H', 'F'};
            HashMap<Character, Integer> findingDays = new HashMap<>();
            findingDays.put('M', 0);
            findingDays.put('T', 1);
            findingDays.put('W', 2);
            findingDays.put('H', 3);
            findingDays.put('F', 4);

            // taking information about each time slot
            for (int i = 1; i <= numTimeSlots; i ++) { // for each time slot: 
                String[] timeslot = br.readLine().split("\\s+"); 

                // get start time
                String[] startTime = timeslot[1].split(":");
                String startTimeM = timeslot[2];

                int startHour = Integer.parseInt(startTime[0]);
                int startMinute = Integer.parseInt(startTime[1]);

                if (startTimeM.equals("PM") && startHour != 12) {
                    startHour += 12;
                }

                // get end time
                String[] endTime = timeslot[3].split(":");
                String endTimeM = timeslot[4];

                int endHour = Integer.parseInt(endTime[0]);
                int endMinute = Integer.parseInt(endTime[1]);

                if (endTimeM.equals("PM") && endHour != 12) {
                    endHour += 12;
                }

                // get days of the week
                String daysString = timeslot[5];

                // replacing TH to H
                daysString = daysString.replaceAll("TH", "H");
                HashSet<String> days = new HashSet<>();

                // if its a range of days
                if (daysString.contains("-")) {
                    int index = daysString.indexOf('-');

                    int start = findingDays.get(daysString.charAt(index-1));
                    int end = findingDays.get(daysString.charAt(index+1));

                    for (;start <= end; start++) {
                        days.add(Character.toString(daysOfTheWeek[start]));
                    }
                }

                // if it is a list of days
                else {
                    for (int j = 0; j < daysString.length(); j++) {
                        days.add(Character.toString(daysString.charAt(j)));
                    }
                }

                // add time slots
                this.timeSlots.add(new Time(i, startHour, startMinute, endHour, endMinute, days));
            }

            // taking the number of rooms
            numRooms = Integer.parseInt(br.readLine().split("\\s")[1]);

            // taking name and capacity of each room 
            for (int i = 0; i < numRooms; i ++) { 
                String[] roomAndSize = br.readLine().split("\\s");
                this.rooms.add(new Room(roomAndSize[0], Integer.parseInt(roomAndSize[1])));
            }

            // sorting room by popularity (r logr)
            this.rooms.sort(null); 

            // adding dummy index for class Numbers
            classNumbers.add("dummy");

            // read number of classes
            numClasses = Integer.parseInt(br.readLine().split("\\s")[1]); 

            // read the number teachers.
            numProfessors = Integer.parseInt(br.readLine().split("\\s")[1]);  

            // taking each class and its professor 
            for (int i = 1; i <= numClasses; i ++) { 
                String[] classAndTeacher = br.readLine().split("\\s");
                
                if (!professors.containsKey(classAndTeacher[1])) {
                    professors.put(classAndTeacher[1], new Professor(classAndTeacher[1]));
                }

                classes.put(classAndTeacher[0], new Class(i, classAndTeacher[0], classAndTeacher[1]));
                classNumbers.add(classAndTeacher[0]);
            }

            /*
            for (String classNum: classes.keySet()) {
                System.out.println(classes.get(classNum));
            }
            */
            
        // error in case file does not open
        } catch (FileNotFoundException fnf) { 
            System.err.println("Could not open the file" + fnf); 
        } catch (IOException ioe) { 
            System.err.println("Reading problem" + ioe);
        }
        
        //Initialize the conflicts array: 
        // Java initializes all values of 2D array to 0 in O(1)
        this.conflict = new int[this.numClasses + 1][this.numClasses + 1]; 

        // Then, read in values from the students file: 
        try (BufferedReader br = new BufferedReader(new FileReader(studentFile))) {

            //first line is numStudents
            this.numStudents = Integer.parseInt(br.readLine().split("\\s")[1]);

            // for every student (s)
            for (int i = 1; i <= this.numStudents; i++) {

                // read classes in student's preference list. 
                String[] line = br.readLine().split("\\s");
                String student = line[0];

                // stores preferred classes for each student
                ArrayList<String> studentPref = new ArrayList<>();
                for (int c = 1; c < line.length; c++) {
                    bestCaseValue += 1;
                    studentPref.add(line[c]);
                }
                    
                // for each class that the student is interested in: 
                for (int j = 0; j < studentPref.size(); j++) {

                    // increase class popularity add interested student to class
                    Class preferredClass = this.classes.get(studentPref.get(j));
                    preferredClass.incrementPopularity(); 
                    preferredClass.addInterestedStudent(student);
                    
                    // for the other classes
                    for (int k = j+1; k < studentPref.size(); k++) {

                        Class otherClass = this.classes.get(studentPref.get(k));

                        // if two classes does not have professor conflict
                        if (!preferredClass.getProfessor().equals(otherClass.getProfessor())) { 

                            // update the 2d array symmetrically to cover both diagonals
                            this.conflict[preferredClass.getIndex()][otherClass.getIndex()] += 1;
                            this.conflict[otherClass.getIndex()][preferredClass.getIndex()] += 1;
                        }

                        // if two classes do have professor conflict
                        else { 
                            this.conflict[preferredClass.getIndex()][otherClass.getIndex()] = -1;
                            this.conflict[otherClass.getIndex()][preferredClass.getIndex()] = -1;
                        }
                    }
                } 
            }

        // error in case file does not open
        } catch (FileNotFoundException fnf) { 
            System.err.println("Could not open the file" + fnf); 
        } catch (IOException ioe) { 
            System.err.println("Reading problem" + ioe);
        }
    }

    //creating an ArrayList of all edges and sorting it
    public void createClassPairs() {


        // for each pair of classes
        for (int row = 1; row <= this.numClasses; row++) { 
            for (int col = 1; col < row; col++) { 

                // if they do not conflict, add it to the Arraylist of edges
                if (this.conflict[row][col] != -1){
                    edges.add(new Edge(classes.get(classNumbers.get(row)), classes.get(classNumbers.get(col)), conflict[row][col]));
                }
            }
        }

        // sort edges in increasing order of conflicts.
        edges.sort(null); 
    }

    // creating a 2d array that stores whether a 2 times conflict or not
    public void createTimeMatrix(String timeChoice) {
        this.timeConflict = new boolean[this.numTimeSlots + 1][this.numTimeSlots + 1];

        if (!timeChoice.equals("n")) {
            for (int t1 = 1; t1 <= this.numTimeSlots; t1++) {
                for (int t2 = 1; t2 <= t1; t2++) {

                    if (t1 == t2) {
                        this.timeConflict[t1][t2] = true;
                        break;
                    }

                    Time time1 = timeSlots.get(t1);
                    Time time2 = timeSlots.get(t2);
                    boolean timeConflict = checkingTimeConflict(time1, time2);

                    this.timeConflict[t1][t2] = timeConflict;
                    this.timeConflict[t2][t1] = timeConflict;
                }
            }
        }

        else {
            for (int t1 = 1; t1 <= this.numTimeSlots; t1++) {
                for (int t2 = 1; t2 <= this.numTimeSlots; t2++) {

                    if (t1 == t2) {
                        this.timeConflict[t1][t2] = true;
                    }
                    else {
                        this.timeConflict[t1][t2] = false;
                    }
                }
            }
        }

        for (int t1 = 1; t1 <= this.numTimeSlots; t1++) {
                for (int t2 = 1; t2 <= this.numTimeSlots; t2++) {
                    if (timeConflict[t1][t2]) {
                        System.out.print("Y ");
                    }
                    else {
                        System.out.print("N ");
                    }
                }
                System.out.println();
            
        }
    }

    public static boolean checkingTimeConflict(Time time1, Time time2) {
        boolean matchingDay = false;
        for (String day : time1.getDays()) {
            if (time2.getDays().contains(day)) {
                matchingDay = true; 
            }
        }
        
        boolean conflict = false;

        if (matchingDay) {
            // if time conflicts
            if ((time1.getEndHour() > time2.getStartHour() || (time1.getEndHour() == time2.getStartHour() && time1.getEndMinute() > time2.getStartMinute())) &&
            (time2.getEndHour() > time1.getStartHour() ||  (time2.getEndHour() == time1.getStartHour() && time2.getEndMinute() > time1.getStartMinute()))) {
                conflict = true;
            }
        }
        
        return conflict;
    }

     /*
     * assign classes to room and time slot, assign students to classes
     */
    public void makeSchedule() { 
        // now that we have a list of edges and the classes sorted by popularity, we want to begin sorting classes into time slots by order of least to most conflicts. 

        // array of time slots, where each time slot have an ArrayList of classes 
        ArrayList<Class>[] timeSlotClasses = new ArrayList[numTimeSlots+1];
        for (int i = 1; i  <= numTimeSlots; i++) { 
            timeSlotClasses[i] = new ArrayList<Class>();
        }

        // current time slot
        int currentTimeSlot = 1;
        int tConflictCount = 0;

        // for each edge, in order of increasing conflicts. 
        for (Edge e : edges) {

            if (e.getNumConflicts() ==0) {
                this.zeroConflictEdges ++;
            } 

            else {
                this.nonZeroConflictEdges ++;
            }

            // get two classes with smallest conflict
            Class classOne = e.getc1();
            Class classTwo = e.getc2(); 

            boolean tConflict = false;

            // if there is an empty time slot & neither class has been placed.
            if (currentTimeSlot <= numTimeSlots && !classOne.getPlaced() && !classTwo.getPlaced()) {

                Professor professorOne = professors.get(classOne.getProfessor());
                Professor professorTwo = professors.get(classTwo.getProfessor());

                //check if professors of both classes do not conflict with this time
                for (int time_index : professorOne.getTeachingTimes()) {
                    if (timeConflict[currentTimeSlot][time_index]) {
                        tConflict = true;
                        break;
                    }
                }
                for (int time_index : professorTwo.getTeachingTimes()) {
                    if (timeConflict[currentTimeSlot][time_index]) {
                        tConflict = true;
                        break;
                    }
                }

                //add both classes to that time slot if no time conflict: 
                if (!tConflict) {
                    timeSlotClasses[currentTimeSlot].add(classOne); 
                    classOne.setTimeSlot(currentTimeSlot);
                    classOne.setPlaced(true);
                    professorOne.addTeachingTime(currentTimeSlot);
                    
                    timeSlotClasses[currentTimeSlot].add(classTwo); 
                    classTwo.setTimeSlot(currentTimeSlot);
                    classTwo.setPlaced(true);
                    professorTwo.addTeachingTime(currentTimeSlot);
                    
                    //incrementing current timeslot
                    currentTimeSlot ++;
                }
            }

            else if (currentTimeSlot <= numTimeSlots) {
                continue;
            }

            // else if 1 IS placed and 2 is NOT: 
            else if (classOne.getPlaced() && !classTwo.getPlaced()) {

                Professor professorTwo = professors.get(classTwo.getProfessor());

                // checking that professor for classtwo does not cnflict with class1 timeslot
                for (int time_index : professorTwo.getTeachingTimes()) {
                    if (timeConflict[classOne.getTimeSlot()][time_index]) {
                        tConflict = true;
                    }
                }
 
                if (!tConflict && timeSlotClasses[classOne.getTimeSlot()].size() < numRooms) {
                    timeSlotClasses[classOne.getTimeSlot()].add(classTwo); 
                    classTwo.setTimeSlot(classOne.getTimeSlot());
                    classTwo.setPlaced(true);
                    professorTwo.addTeachingTime(classOne.getTimeSlot());
                }
            }

            // else if 1 is NOT placed and 2 IS.
            else if (!classOne.getPlaced() && classTwo.getPlaced()) { 

                Professor professorOne = professors.get(classOne.getProfessor());

                // checking that professor for classtwo does not cnflict with class1 timeslot
                for (int time_index : professorOne.getTeachingTimes()) {
                    if (timeConflict[classTwo.getTimeSlot()][time_index]) {
                        tConflict = true;
                    }
                }
 
                if (!tConflict && timeSlotClasses[classTwo.getTimeSlot()].size() < numRooms) {
                    timeSlotClasses[classTwo.getTimeSlot()].add(classOne); 
                    classOne.setTimeSlot(classTwo.getTimeSlot());
                    classOne.setPlaced(true);
                    professorOne.addTeachingTime(classOne.getTimeSlot());
                }
            }

            if (tConflict) {
                tConflictCount++;
            }

        // else neither class has been placed but there are no empty time slots. Therefore, do nothing. 
        }

        System.out.println(tConflictCount);

        Hashtable<String, ArrayList<Integer>> studentsTimeSlots = new Hashtable<String, ArrayList<Integer>>(); // associates student string with the list of classes they are interested in. 

        int sConflictsCount = 0;
        // once we have class schedule set, start adding students to classes: O(s)
        for (int t = 1; t <= this.numTimeSlots; t++ ) {

            // sort each of classes in the time slots by popularity. O(c log(c)), because Java uses merge/quicksort. 
            timeSlotClasses[t].sort(null); 


            // each class in time slot from most popular class
            for (int r = 0; r < timeSlotClasses[t].size(); r++) {

                boolean zoom = false;
                // get room
                Class classInSlot = timeSlotClasses[t].get(r); 

                Room assignedRoom = rooms.get(r);
                int limit;

                if (classInSlot.getInterestedStudents().size() > assignedRoom.getRoomSize()) {
                    classInSlot.setRoomName("zoom");
                    limit = 9999;
                    zoom = true;
                    moreInterestedThanSize++;
                }

                else {
                    classInSlot.setRoomName(rooms.get(r).getRoomName());
                    limit = rooms.get(r).getRoomSize();
                }
            
                //get the room size
                classInSlot.setRoomSize(limit);

                // for each student who is interested in the class
                if (zoom) {
                    classInSlot.enrollEveryone();
                    studentEnrolledValue += classInSlot.getInterestedStudents().size();
                }

                else {
                    for (String student: classInSlot.interestedStudents) {

                        boolean sConflict = false; 
                        // if student has not been placed into any class, room, or timeslot yet. Therefore, no possible time conflict: 
                        if (!studentsTimeSlots.containsKey(student)) { 
                            studentsTimeSlots.put(student, new ArrayList<Integer>(numTimeSlots)); // start with initial capacity of the number of timeslots, so the arraylist never needs to double in underlying array size. 
                            // add student to the time slot and class
                            studentsTimeSlots.get(student).add(t); // mark that the student is in this timeSlot. 
                            classInSlot.addEnrolledStudent(student);  // enroll the student into the class. 
                            studentEnrolledValue++;
                            
                        }
                        else { // student has already been placed into a timeslot. 
                            //for every time slot the student is already enrolled in: 
                            for (int enrolledTimeSlot : studentsTimeSlots.get(student))  {
                                if (this.timeConflict[enrolledTimeSlot][t]) { // if the time that it's already in conflicts with this time slot:
                                    sConflict = true;  // then the student can't be placed into this time slot, because it is already busy during this time slot. 
                                    break;
                                }
                            }
                            if (!sConflict) { // if student doesn't have any other conflicts during this timeSlot. 
                                // add student to the time slot and class
                                studentsTimeSlots.get(student).add(t); // mark that the student is in this timeSlot. 
                                classInSlot.addEnrolledStudent(student);  // enroll the student into the class.                   
                                studentEnrolledValue++; // increase the pref value
                            }
                                        
                        }
                        // decrease room limit by 1

                        if (!sConflict) {
                            limit--;
                        }

                        if (sConflict) {
                            sConflictsCount++;
                        }

                        // if room is full, do not put any more students
                        if (limit == 0) {break;}
                    }
                }
            }
        }

        System.out.println(sConflictsCount);
    }

    public void printSchedule() { 
        System.out.println("Course\tRoom\tTeacher\tTime\tStudent");
        for(int i = 1; i < classNumbers.size(); i++) { 
            String classNumber = classNumbers.get(i);
            Class c = classes.get(classNumber);
            System.out.printf("%s\t%s\t%s\t%s\t", c.getClassNumber(), c.getRoomName(), c.getProfessor(), c.getTimeSlot()); 
            for (String student : c.getEnrolledStudent()) { 
                System.out.print(student + " ");
            }
            System.out.println();
        }
    }

    /*
     * write schedule for in schedule.txt
     */
    public void writeSchedule(String fileName) {
    
            try {
                // Create a FileWriter with the given file name
                FileWriter fileWriter = new FileWriter(fileName);
        
                fileWriter.write(studentEnrolledValue/bestCaseValue * 100 + "\n");
                fileWriter.write("Zero Conflict:" + zeroConflictEdges + "\n");
                fileWriter.write("Nonzero Conflict:" + nonZeroConflictEdges + "\n");
                fileWriter.write("More students interested than room limit: " + moreInterestedThanSize + "\n");

                // Write the header to the file
                fileWriter.write("Course\tRoom\tTeacher\tTime\tStudents\n");
    
                // for each class, write all the info of the class 
                for(int i = 1; i < classNumbers.size(); i++) { 
                    String classNumber = classNumbers.get(i);
                    Class c = classes.get(classNumber); 

                    if (c.getTimeSlot() == 0) {
                        continue;
                    }

                    String formatText = String.format("%s\t%d / %d / %d\t%s\t%s\t%s\t", c.getClassNumber(), c.getEnrolledStudent().size(), c.getRoomSize(), c.getInterestedStudents().size(), c.getRoomName(), c.getProfessor(), c.getTimeSlot()); 
                    fileWriter.write(formatText);
                    for (String student : c.getEnrolledStudent()) { 
                        fileWriter.write(student + " ");
                    }
                    fileWriter.write("\n");
                }
    
               // Close the FileWriter to save the changes
                fileWriter.close();
                
            // catch error
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
   

    public long getNanoSecondsElapsed() { 
        return nanoSecondsElapsed; 
    }
}
