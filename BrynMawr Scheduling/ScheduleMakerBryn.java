import java.io.BufferedReader; 
import java.io.FileNotFoundException;
import java.io.FileReader; 
import java.io.IOException; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;


import java.io.FileWriter;

public class ScheduleMakerBryn {
    final int classPerStudent = 4;
    private int numTimeSlots; // stores the time slots specified in the input file.
    private ArrayList<Time> timeSlots = new ArrayList<Time>(); 

    private int numRooms; // stores the number of rooms specified in the input file. 
    private ArrayList<Room> rooms = new ArrayList<Room>(); //rooms in descending order of size

    private int numClasses; // number of classes specified in the input file.
    private ArrayList<String> classNumbers = new ArrayList<>();
    private HashMap<String, Class> classes = new HashMap<String, Class>(); // classes in descending order of popularity. 

    private int numProfessors; // number of teachers specified in the input file. 
    private ArrayList<Professor> professors = new ArrayList<Professor>();

    private int[][] conflict; // a 2d array storing the number of conflicts between every class and every other class. 
    private int numStudents; // number of students specified in the input file. 
    private ArrayList<Edge> edges; // conflict between 2 classes in increasing order of popularity
    private long nanoSecondsElapsed; 

    public static void main(String[] args) { 

        if (args.length != 2) {
            System.out.println("Usage: java ScheduleMakerBryn <constraints.txt> <student_preferences.txt>");
            return;
        }

        String constraint = args[0];
        String studentPref = args[1];
        ScheduleMakerBryn demo = new ScheduleMakerBryn(constraint, studentPref);
        // idea being that each schedule maker is given an input file, which is processed in its constructor
    }
    
    public ScheduleMakerBryn(String constraintsFile, String studentFile) { 
        //Process the input: 
        readingInput(constraintsFile, studentFile); 
        long start = System.nanoTime();
        createClassPairs();
        makeSchedule(); 
        nanoSecondsElapsed = (System.nanoTime() - start);
        //System.out.printf("Time elapsed: %,d microseconds%n", (finish-start)/1000);
        writeSchedule();
    }
    
    /* 
     * Given a filename, will initialize class variables. 
     * @param fileName the file to read
     */
    public void readingInput(String constraintsFile, String studentFile) { 

        // Read values from the constraints file:  
        try (BufferedReader br = new BufferedReader(new FileReader(constraintsFile))) { 

            // taking the number of TimeSlots
            numTimeSlots = Integer.parseInt(br.readLine().split("\\s+")[2]); 

            // dummy time slot for indexing
            timeSlots.add(new Time(0, 0, 0, 0, 0, Collections.<String>emptySet()));

            // initializing days of the week
            char[] daysOfTheWeek = {'M', 'T', 'W', 'H', 'F'};
            HashMap<Character, Integer> findingDays = new HashMap<>();
            findingDays.put('M', 0);
            findingDays.put('T', 1);
            findingDays.put('W', 2);
            findingDays.put('H', 3);
            findingDays.put('F', 4);

            //taking information about each time slot
            for (int i = 1; i <= numTimeSlots; i ++) {
                String[] timeslot = br.readLine().split("\\s+"); 

                // get start time
                String[] startTime = timeslot[1].split(":");
                String startTimeM = timeslot[2];

                int startHour = Integer.parseInt(startTime[0]);
                int startMinute = Integer.parseInt(startTime[1]);

                if (startTimeM.equals("PM")) {
                    startHour += 12;
                }

                // get end time
                String[] endTime = timeslot[3].split(":");
                String endTimeM = timeslot[4];

                int endHour = Integer.parseInt(endTime[0]);
                int endMinute = Integer.parseInt(endTime[1]);

                if (endTimeM.equals("PM")) {
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
                classes.put(classAndTeacher[0], new Class(i, classAndTeacher[0], new Professor(classAndTeacher[1])));
                classNumbers.add(classAndTeacher[0]);
            }
            
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
                        if (!preferredClass.getProfessor().getProfessorNumber().equals(otherClass.getProfessor().getProfessorNumber())) { 

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

    public void createClassPairs() {

        //creating an ArrayList of all edges and sorting it
        edges = new ArrayList<>();

        // for each pair of classes
        for (int row = 1; row <= this.numClasses; row++) { 
            for (int col = 1; col < row; col++) { 

                // if they do not conflict, add it to the Arraylist of edges
                if (this.conflict[row][col] != -1){
                    edges.add(new Edge(classes.get(classNumbers.get(row)), classes.get(classNumbers.get(col)), conflict[row][col]));
                }
            }
        }

        for (int i = 0; i < edges.size(); i++) {
            System.out.println(edges.get(i));
        }

        // sort edges in increasing order of conflicts.
        edges.sort(null); 
    }


    /*
     * assign classes to room and time slot, assign students to classes
     */
    public void makeSchedule() { 
        // now that we have a list of edges and the classes sorted by popularity, we want to begin sorting classes into time slots by order of least to most conflicts. 


        // (4.5) time check for initializing time slots and other things
        //long time45start = System.nanoTime();


        // array of time slots, where each time slot have an ArrayList of classes 
        ArrayList<Class>[] timeSlots = new ArrayList[numTimeSlots+1];
        for (int i = 1; i  <= numTimeSlots; i++) { 
            timeSlots[i] = new ArrayList<Class>();
        }
        // If a professor has been placed, the index of its timeslot
        // Note: This method of checking for professor conflict relies on profs teaching max. 2 courses.
        int profTime[] = new int[numProfessors + 1];

        // true if the class is already in a timeSlot, false if not. 1 through class number to make our lives easier.
        Boolean[] classPlaced = new Boolean[numClasses + 1];


        for (int i = 0; i < classPlaced.length; i++ ) { 
            // every class starts as not already placed in a time slot. 
            classPlaced[i] = false; 
        }

        // when this hits 0, stop combining into a new time slot.  
        int numEmptyTimeSlots = numTimeSlots;

        //long time45end = System.nanoTime();
        //System.out.printf("Check point 4.5 (initializing time slot) %,d microseconds%n", (time45end-time45start)/1000);


        // (5) accesing each pair of classes: O(c^2)
        //long time5start = System.nanoTime();

        // for each edge, in order of increasing conflicts. 
        for (Edge e : edges) {

            // get two classes with smallest conflict
            Class classOne = e.getc1();
            int classOneNum = classOne.get();
            Class classTwo = e.getc2(); 
            int classTwoNum = classTwo.getClassNumber();

            // if there is an empty time slot & neither class has been placed.
            if (numEmptyTimeSlots != 0 && !classPlaced[classOneNum] && !classPlaced[classTwoNum]) {  

                // place them into the first open time slot, and adjust the number of empty time slots.  
                int firstOpenTimeSlot = numTimeSlots - (numEmptyTimeSlots--) +1; 

                //add both classes to that time slot: 
                timeSlots[firstOpenTimeSlot].add(classOne); 
                classOne.setTimeSlot(firstOpenTimeSlot);
                classPlaced[classOneNum] = true; 
                profTime[classOne.getTeacher()] = firstOpenTimeSlot;
                
                timeSlots[firstOpenTimeSlot].add(classTwo); 
                classTwo.setTimeSlot(firstOpenTimeSlot);
                classPlaced[classTwoNum] = true; 
                profTime[classTwo.getTeacher()] = firstOpenTimeSlot;
            }

            // else if 1 IS placed and 2 is NOT: 
            else if ((classPlaced[classOneNum] && !classPlaced[classTwoNum])) {

                // if 1's time slot is not already full and 2's prof is not placed at 1's timeslot: 
                if (timeSlots[classOne.getTimeSlot()].size() < numRooms && profTime[classTwo.getTeacher()] != classOne.getTimeSlot()) { 

                    // add 2 to the same time slot as 1: 
                    timeSlots[classOne.getTimeSlot()].add(classTwo); 
                    classTwo.setTimeSlot(classOne.getTimeSlot());
                    profTime[classTwo.getTeacher()] = classOne.getTimeSlot();
                    classPlaced[classTwoNum] = true; 
                }
            }

            // else if 1 is NOT placed and 2 IS.
            else if (!classPlaced[classOneNum] && classPlaced[classTwoNum]) { 

                // if 2's time slot is not already full and 1's prof is not placed at 2's timeslot: 
                if (timeSlots[classTwo.getTimeSlot()].size() < numRooms && profTime[classOne.getTeacher()] != classTwo.getTimeSlot()) { 

                    // add 1 to the same time slot as 2: 
                    timeSlots[classTwo.getTimeSlot()].add(classOne); 
                    classOne.setTimeSlot(classTwo.getTimeSlot());
                    profTime[classOne.getTeacher()] = classTwo.getTimeSlot();
                    classPlaced[classOneNum] = true; 
                }
            }   

        // else neither class has been placed but there are no empty time slots. Therefore, do nothing. 
        }

        // (5) time end
        //long time5end = System.nanoTime();
        //System.out.printf("Check point 5 (accesing each pair and adding them to time slots): O(c^2) %,d microseconds%n", (time5end-time5start)/1000);
        // (5) time end

        float bestCaseValue = 4 * this.numStudents;
        float studentPrefValue = 0;

        // (6) sorting and addition students O(t rlog r) + O(s)
        //long time6start = System.nanoTime();

        // once we have class schedule set, start adding students to classes: O(s)
        for (int t = 1; t <= this.numTimeSlots; t++ ) {

            // sort each of classes in the time slots by popularity. O(c log(c)), because Java uses merge/quicksort. 
            timeSlots[t].sort(null); 

            // students taking a class in specific time slot
            HashSet<Integer> studentsInTimeSlot = new HashSet<Integer>(); 

            // each class in time slot from most popular class
            for (int r = 0; r < timeSlots[t].size(); r++) {

                // get room
                Class classInSlot = timeSlots[t].get(r); 

                // set the room number of the class
                classInSlot.setRoomNumber(rooms.get(r).getRoomNumber());

                //get the room size
                int limit = rooms.get(r).getRoomSize();

                // for each student who is interested in the class
                for (Integer student: classInSlot.interestedStudents) {

                    // if student is not already in this time slot
                    if (!studentsInTimeSlot.contains(student)) {

                        // add student to the time slot and class
                        classInSlot.addEnrolledStudent(student);
                        studentsInTimeSlot.add(student);

                        // increase the pref value
                        studentPrefValue++;
                    }

                    // decrease room limit by 1
                    limit--;

                    // if room is full, do not put any more students
                    if (limit == 0) {break;}
                }
            }
        }


        // (6) time end
        //long time6end = System.nanoTime();
        //System.out.printf("Check point 6 (sorting time slot and adding students): O(t rlog r) + O(s) %,d microseconds%n", (time6end-time6start)/1000);


        // (7) printing values
        //long time7start = System.nanoTime();

        // print out important values
        System.out.println("Student Preference Value: " + studentPrefValue);
        System.out.println("Best Case Student Value: " + bestCaseValue);
        System.out.printf("Fit: %2.2f%%%n", studentPrefValue/bestCaseValue * 100);
        
        //long time7end = System.nanoTime();
        //System.out.printf("Check point 7 (printing stuff) %,d microseconds%n", (time6end-time6start)/1000);

    }

    /*
     * print schedule
     */
    public void printSchedule() { 
        System.out.println("Course\tRoom\tTeacher\tTime\tStudent");
        for(Class c : classes) { 
            System.out.printf("%d\t%d\t%d\t%d\t", c.getClassNumber(), c.getRoomNumber(), c.getTeacher(), c.getTimeSlot()); 
            for (Integer student : c.getEnrolledStudent()) { 
                System.out.print(student + " ");
            }
            System.out.println();
        }
    }

    /*
     * write schedule for in schedule.txt
     */
    public void writeSchedule() {

    // (7) time check for writing schedule
    //long time7start = System.nanoTime();

        try {
            // Create a FileWriter with the given file name
            FileWriter fileWriter = new FileWriter("schedule.txt");
    
            // Write the header to the file
            fileWriter.write("Course\tRoom\tTeacher\tTime\tStudents\n");

            // for each class, write all the info of the class 
            for(Class c : classes) { 
                String formatText = String.format("%d\t%d\t%d\t%d\t", c.getClassNumber(), c.getRoomNumber(), c.getTeacher(), c.getTimeSlot()); 
                fileWriter.write(formatText);
                for (Integer student : c.getEnrolledStudent()) { 
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

        // (7) time end
        //long time7end = System.nanoTime();
        //System.out.printf("Check point 7 (writing to file): O(s) %,d microseconds%n", (time7end-time7start)/1000);

    }
    

    /* 
     * Returns the number of time slots specified by the problem's input file. 
     * @return the number of time slots. 
     */
    public int getNumTimeSlots() { 
        return numTimeSlots; 
    }
    
    /* 
     * Returns the number of rooms specified by the problem's input file. 
     * @return the number of rooms. 
     */
    public int getNumRooms() { 
        return numRooms; 
    }

    /*
     * Returns the number of classes specified by the problem's input file.
     * @return the number of classes.
     */
    public int getNumClasses() { 
        return numClasses; 
    }

    /*
     * Returns the number of teachers specified by the problem's input file.
     * @return the number of teachers.
     */
    public int getnumProfessorshers() { 
        return numProfessors; 
    }

    public long getNanoSecondsElapsed() { 
        return nanoSecondsElapsed; 
    }
}
