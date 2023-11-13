import java.io.BufferedReader; 
import java.io.FileNotFoundException;
import java.io.FileReader; 
import java.io.IOException; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;


import java.io.FileWriter;

public class Test {

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
            System.out.println("Usage: java <constraints.txt> <student_preferences.txt>");
            return;
        }

        String constraint = args[0];
        String studentPref = args[1];
        // idea being that each schedule maker is given an input file, which is processed in its constructor
        Test test = new Test();
        test.readingInput(constraint, studentPref);
        test.createClassPairs();
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

            for (String classNum: classes.keySet()) {
                System.out.println(classes.get(classNum));
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
                            System.out.println(conflict[otherClass.getIndex()][preferredClass.getIndex()]);
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

        /*
        for (int i = 0; i < edges.size(); i++) {
            System.out.println(edges.get(i));
        }
        */

        // sort edges in increasing order of conflicts.
        edges.sort(null); 
    }
}