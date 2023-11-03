import java.io.BufferedReader; 
import java.io.FileNotFoundException;
import java.io.FileReader; 
import java.io.IOException; 
import java.util.ArrayList; 
import java.util.Arrays;
import java.util.HashSet; 
import java.io.File;
import java.io.FileWriter;

public class ScheduleMaker {
    final int classPerStudent = 4; // number of listed preferred classes per student. 
    private int numTimeSlots; // stores the time slots specified in the input file. 
    private int numRooms; // stores the number of rooms specified in the input file. 
    private ArrayList<Room> rooms = new ArrayList<Room>(); // rooms in descending order of size
    private int numClasses; // number of classes specified in the input file.
    private ArrayList<Class> classes = new ArrayList<Class>(); // classes in descending order of popularity. 
    private int numTeachers; // number of teachers specified in the input file. 
    private int[][] conflict; // a 2d array storing the number of conflicts between every class and every other class. 
    private int numStudents; // number of students specified in the input file. 
    private ArrayList<Edge> edges; 

    // All arrays go from 1 to the number of classes + 1, with 0 as a buffer. 

    public static void main(String[] args) { 
        ScheduleMaker normalCase = new ScheduleMaker("demo_constraints.txt", "demo_studentprefs.txt"); 
        // idea being that Each schedule maker is given an input file, which is processed in its constructor
    }
    
    public ScheduleMaker(String constraintsFile, String studentFile) { 
        long start = System.nanoTime();
        //Process the input: 
        processInput(constraintsFile, studentFile); 
        makeSchedule(); 
        writeSchedule();
        long finish = System.nanoTime();
        System.out.printf("Time elapsed: %,d milliseconds%n", (finish-start)/1000000);
    }
    /* 
     * Given a filename, will initialize class variables. 
     * @param fileName the file to read
     */
    public void processInput(String constraintsFile, String studentFile) { 
        // we will assume that all input files will be of the same format as demo_constraints.txt and demo_studentprefs.txt
        
        //Initialize the array of classes: 
        classes.add(new Class(4, 2));// add a dummy class at the beginning to make our index math a little easier. 

        // Read values from the constraints file:  
        try (BufferedReader br = new BufferedReader(new FileReader(constraintsFile))) { 

            //first line of demo is the number of class times. 
            numTimeSlots = Integer.parseInt(br.readLine().split("\\s+")[2]); // read first line, take the 3rd word from it, a.k.a the # of class times. 
            numRooms = Integer.parseInt(br.readLine().split("\\s")[1]);
            for (int i = 0; i < numRooms; i ++) { // read the rooms capacities --> r iterations. 
                String[] roomAndSize = br.readLine().split("\\s");
                this.rooms.add(new Room(Integer.parseInt(roomAndSize[0]), Integer.parseInt(roomAndSize[1]))); // store the capacity of a room i. 
            }
            this.rooms.sort(null); // Java Arrays library uses either quicksort or mergesort, both of which are n*log(n). Therefore, r*log(r)
            numClasses = Integer.parseInt(br.readLine().split("\\s")[1]); // read number of classes
            numTeachers = Integer.parseInt(br.readLine().split("\\s")[1]); // read the number teachers. 

            for (int i = 0; i < numClasses; i ++) { // c iterations. Read and store the teacher for each class. 
                String[] classAndTeacher = br.readLine().split("\\s");
                classes.add(new Class(Integer.parseInt(classAndTeacher[0]), Integer.parseInt(classAndTeacher[1]))); // store the class and the  teacher. 
            }
            
        } catch (FileNotFoundException fnf) { 
            System.err.println("Could not open the file" + fnf); 
        } catch (IOException ioe) { 
            System.err.println("Reading problem" + ioe);
        }

        // checking if classes are saved properly
        /*
        for (int i = 1; i < numClasses+1; i++) {
            System.out.println(classes.get(i));
        }
        */
        
        //Initialize the conflicts array: 
        // initializing the -1 
        //currently c^2, include in the later part.  
        this.conflict = new int[this.numClasses + 1][this.numClasses + 1]; // Java initializes all values of 2D array to 0 in O(1)
        //deprecated-- teacher conflict is handled later in code. 
        /*for(int r = 1; r <= this.numClasses; r++ ) { // for each class
            //this.conflict[r] = new int[this.numClasses + 1]; // initialize each row. 
            for (int c = 1; c <= r; c++) { 
                if (classes.get(r).getTeacher() == classes.get(c).getTeacher()) {
                    this.conflict[r][c] = -1;
                } else {
                    this.conflict[r][c] = 0;
                }
            }
        }*/

        // checking if conflicts are saved properly
        /*
        for(int r = 1; r <= this.numClasses; r++ ) {
            for (int c = 1; c <= r; c++) { 
                System.out.print(conflict[r][c] + " ");
            }
            System.out.println();
        }
        */

        // Then, read in values from the students file: 
        try (BufferedReader br = new BufferedReader(new FileReader(studentFile))) { 
            //first line is numStudents
            this.numStudents = Integer.parseInt(br.readLine().split("\\s")[1]); // \\s represents any white space, including spaces and tabs
            for (int c = 1; c < classes.size(); c++ ) { // O(c) -- initialize the student arrays for each class now that we know the number of students. 
                classes.get(c).initializeStudents(numStudents);
            }

            for (int i = 1; i <= this.numStudents; i++) { // for every student (s)
                String[] line = br.readLine().split("\\s"); // read classes in student's preference list. 
                int[] studentPref = new int[classPerStudent + 1]; // 1 more than the amount of prefferred classes per student. 
                for (int c = 1; c < studentPref.length; c++) { 
                    studentPref[c] = Integer.parseInt(line[c]); // turn into an integer ID. 
                }

                for (int j = 1; j < studentPref.length; j++) { // for each class that the student is interested in: 
                    Class preferredClass = this.classes.get(studentPref[j]); // retrieve the preferred class
                    preferredClass.incrementPopularity(); 
                    //preferredClass.addStudent(i);
                    //preferredClass.addInterestedStudent(i);

                    for (int k = j; k < studentPref.length; k++) { // for the other classes
                        if (preferredClass.getTeacher() != this.classes.get(studentPref[k]).getTeacher()) { // if they do not have a teacher conflict:
                            this.conflict[studentPref[j]][studentPref[k]] += 1;
                            this.conflict[studentPref[k]][studentPref[j]] += 1; // update the 2d array symmetrically, so that it doesn't accidentally get placed on the upper half of the 2d array and ignored. 
                        }
                        /*else { 
                            this.conflict[studentPref[j]][studentPref[k]] = -1;
                            this.conflict[studentPref[k]][studentPref[j]] = -1;
                        } Perhaps don't even need to do this, actually! 
                        */
                    }
                } 
            }
        } catch (FileNotFoundException fnf) { 
            System.err.println("Could not open the file" + fnf); 
        } catch (IOException ioe) { 
            System.err.println("Reading problem" + ioe);
        }

        // printing conflict to see
        /*
        for(int r = 1; r <= this.numClasses; r++ ) {
            for (int c = 1; c <= r; c++) { 
                System.out.print(conflict[r][c] + " ");
            }
            System.out.println();
        }


        for(int r = 1; r <= this.numClasses; r++ ) { // for each class
            for (int c = 1; c <= r; c++) { 
                System.out.println("# of conflicts between classes " + r + " and " + c + " is " + conflict[r][c]); 
            }
        }
        */

        //creating an ArrayList of all edges and sorting it
        edges = new ArrayList<>();
        for (int row = 1; row <= this.numClasses; row++) { 
            for (int col = 1; col <= row; col++) { // for each pair of classes: 
                if (classes.get(row).getTeacher() != classes.get(col).getTeacher()) {  // if there is no teacher conflict: 
                    edges.add(new Edge(classes.get(row), classes.get(col), conflict[row][col])); // add it to the list of edges. 
                }
            }
        }


        /*
        for( int i = 0; i < this.numClasses; i++ ) { // print each class. 
            System.out.println(classes.get(i)); 
        }
        */

        classes.remove(0);
        edges.sort(null); // sort edges in increasing order of conflicts. 
        
        /*
        for (int i = 0; i < edges.size(); i++) {
            System.out.println(edges.get(i));
        }    
        */   
    }

    public void makeSchedule() { 
        // now that we have a list of edges and the classes sorted by popularity, we want to begin sorting classes into time slots by order of least to most conflicts. 
        ArrayList<Class>[] timeSlots = new ArrayList[numTimeSlots+1];
        for (int i = 1; i  <= numTimeSlots; i++) { 
            timeSlots[i] = new ArrayList<Class>();
        }

        Boolean[] classPlaced = new Boolean[numClasses + 1]; // true if the class is already in a timeSlot, false if not. 1 through class number to make our lives easier. index 0 stores nothing. 
        for (int i = 0; i < classPlaced.length; i++ ) { 
            classPlaced[i] = false; // every class starts as not already placed in a time slot. 
        }

        int numEmptyTimeSlots = numTimeSlots; // when this hits 0, stop combining into a new time slot.  

        for (Edge e : edges) { // for each edge, in order of increasing conflicts. 
            Class classOne = e.getc1();
            int classOneNum = classOne.getClassNumber();
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
                
                timeSlots[firstOpenTimeSlot].add(classTwo); 
                classTwo.setTimeSlot(firstOpenTimeSlot);
                classPlaced[classTwoNum] = true; 
            }

            /*  this may be the only section of this that does not quite match up with what we discussed before. 
                Let's say we have 3 time slots: 
                        1: A, B
                        2: C, D
                        3: E, F
                    and the next thing we want to match up is B and C. 

                    I think that if A, B, C, and D can all fit in time slot 1 when merged, there's no real reason to not merge them. 
                    It doesn't, to my knowlege, lead to a situation where something later down the line can't be put in that should be.
                    The important thing to me seems to be that we don't make a 4th time slot and try to merge it in later, which we already do!

                    NOTE: WILL NEED TO ADJUST PSEUDOCODE TO MATCH THIS!!!
                */
            /*
            else if (classPlaced[classOneNum] && classPlaced[classTwoNum] && timeSlots[classOne.getTimeSlot()].size() + timeSlots[classTwo.getTimeSlot()].size() <= numRooms) { // if both are placed into time slots, but merging those time slots would be fine: 
                while(!timeSlots[classTwo.getTimeSlot()].isEmpty()) { // for each class in classTwo's time slot. 
                    // remove from classTwo's time slot and add it to the classOne' time slot: 
                    Class classToMove = timeSlots[classTwo.getTimeSlot()].remove(0);
                    timeSlots[classOne.getTimeSlot()].add(classToMove); // add to classOne's time slot. 
                    classToMove.setTimeSlot(classOne.getTimeSlot()); // adjust the moved class's time slot to match. 
                }
            }
            */

            else if ((classPlaced[classOneNum] && !classPlaced[classTwoNum])) { // else if 1 IS placed and 2 is NOT: 
                if (timeSlots[classOne.getTimeSlot()].size() < numRooms) { // if 1's time slot is not already full: 
                    // add 2 to the same time slot as 1: 
                    timeSlots[classOne.getTimeSlot()].add(classTwo); 
                    classTwo.setTimeSlot(classOne.getTimeSlot());
                    classPlaced[classTwoNum] = true; 
                }
                //what do we do if it can't fit in that time slot? Just keep going? yup!
            }
            else if (!classPlaced[classOneNum] && classPlaced[classTwoNum]) { // else if 1 is NOT placed and 2 IS.
                if (timeSlots[classTwo.getTimeSlot()].size() < numRooms) { // if 1's time slot is not already full: 
                    // add 1 to the same time slot as 2: 
                    timeSlots[classTwo.getTimeSlot()].add(classOne); 
                    classOne.setTimeSlot(classTwo.getTimeSlot());
                    classPlaced[classOneNum] = true; 
                }
            }   
            // else neither class has been placed but there are no empty time slots. Therefore, do nothing. 
        }

        float bestCaseValue = 4 * this.numStudents;
        float studentPrefValue = 0;
        // once we have placed the classes into their time slots, remove students from classes if they have a conflict. 
        for (int t = 1; t <= this.numTimeSlots; t++ ) { // for each time slot, O(t)
            timeSlots[t].sort(null); // sort each of classes in the time slots by popularity. O(c log(c)), because Java uses merge/quicksort. 

            HashSet<Integer> studentsInTimeSlot = new HashSet<Integer>(); // number of students taking a class in time slot

            for (int r = 0; r < timeSlots[t].size(); r++) { // for each room in the time slot, O(r).
                Class classInSlot = timeSlots[t].get(r); 
                classInSlot.setRoomNumber(rooms.get(r).getRoomNumber());

                int limit = rooms.get(r).getRoomSize();


                //deprecated. Relies on interested Students in class. 
                /*for (Integer student: classInSlot.interestedStudents) {
                    if (!studentsInTimeSlot.contains(student)) {
                        classInSlot.addEnrolledStudent(student);
                        studentsInTimeSlot.add(student);
                        studentPrefValue++;
                    }

                    limit--;

                    if (limit == 0) {break;}
                }*/
            }
        }
        System.out.println("Student Preference Value: " + studentPrefValue);
        System.out.println("Best Case Student Value: " + bestCaseValue);
        System.out.printf("Fit: %2.2f%%%n", studentPrefValue/bestCaseValue * 100);
    }

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

    public void writeSchedule() {
        try {
            // Create a FileWriter with the given file name
            FileWriter fileWriter = new FileWriter("schedule.txt");
    
            // Write the content to the file
            fileWriter.write("Course\tRoom\tTeacher\tTime\tStudent\n");

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
            
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
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
    public int getNumTeachers() { 
        return numTeachers; 
    }
}
