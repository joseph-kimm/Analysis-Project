import java.io.BufferedReader; 
import java.io.FileNotFoundException;
import java.io.FileReader; 
import java.io.IOException; 
import java.util.ArrayList; 
import java.util.Arrays; 

public class ScheduleMaker {
    final int preferredClassesPerStudent = 4; // number of listed preferred classes per student. 
    private int numTimeSlots; // stores the time slots specified in the input file. 
    private int numRooms; // stores the number of rooms specified in the input file. 
    private int[] cap; // cap[i] stores the capacity of room i. 
    private int numClasses; // number of classes specified in the input file.
    private ArrayList<Class> classes; // classes in descending order of popularity. 
    private int numTeachers; // number of teachers specified in the input file. 
    private int[][] conflict; // a 2d array storing the number of conflicts between every class and every other class. 
    private int numStudents; // number of students specified in the input file. 

    public static void main(String[] args) { 
        ScheduleMaker normalCase = new ScheduleMaker("demo_constraints.txt", "demo_studentprefs.txt"); 
        // idea being that Each schedule maker is given an input file, which is processed in its constructor
    }
    
    public ScheduleMaker(String constraintsFile, String studentFile) { 
        //Process the input: 
        processInput(constraintsFile, studentFile); 
    }

    /* 
     * Given a filename, will initialize class variables. 
     * @param fileName the file to read
     */
    public void processInput(String constraintsFile, String studentFile) { 
        // we will assume that all input files will be of the same format as demo_constraints.txt and demo_studentprefs.txt
        
        //Initialize the array of classes: 
        classes = new ArrayList<Class>(); 

        // Read values from the constraints file:  
        try (BufferedReader br = new BufferedReader(new FileReader(constraintsFile))) { 

            //first line of demo is the number of class times. 
            numTimeSlots = Integer.parseInt(br.readLine().split("\\s+")[2]); // read first line, take the 3rd word from it, a.k.a the # of class times. 
            numRooms = Integer.parseInt(br.readLine().split("\\s")[1]);
            cap = new int[numRooms]; // initialize the capacity array. 
            for (int i = 0; i < numRooms; i ++) { // read the rooms capacities --> r iterations. 
                this.cap[i] = Integer.parseInt(br.readLine().split("\\s")[1]); // store the capacity of a room i. 
            }
            Arrays.sort(cap); // Java Arrays library uses either quicksort or mergesort, both of which are n*log(n). Therefore, r*log(r)
            numClasses = Integer.parseInt(br.readLine().split("\\s")[1]);
            numTeachers = Integer.parseInt(br.readLine().split("\\s")[1]);


            for (int i = 0; i < numClasses; i ++) { // c iterations. 
                String[] classAndTeacher = br.readLine().split("\\s");
                classes.add(new Class(Integer.parseInt(classAndTeacher[0]), Integer.parseInt(classAndTeacher[1]) )); // store the class and the  teacher. 
            }
            
        } catch (FileNotFoundException fnf) { 
            System.err.println("Could not open the file" + fnf); 
        } catch (IOException ioe) { 
            System.err.println("Reading problem" + ioe);
        }

        
        //Initialize the conflicts array: 
        // initializing the -1 
        this.conflict = new int[this.numClasses][this.numClasses]; 
        for(int r = 1; r < this.numClasses; r++ ) { // for each class
            this.conflict[r] = new int[this.numClasses]; // initialize each row. 
            for (int c = 0; c < r; c++) { 
                if (classes.get(c).getTeacher() == classes.get(c).getTeacher()) {
                    this.conflict[r][c] = -1;
                } else {
                    this.conflict[r][c] = 0;
                }
            }
        }

        // Then, read in values from the students file: 
        try (BufferedReader br = new BufferedReader(new FileReader(studentFile))) { 
            //first line is numStudents
            this.numStudents = Integer.parseInt(br.readLine().split("\\s")[1]);
            for (int i = 0; i < this.numStudents; i++) { // for every student (s)
                String[] line = br.readLine().split("\\s"); // read classes in student's preference list. 
                int[] studentPref = new int[4]; 
                for (int c = 0; c < 4; c++) { 
                    studentPref[c] = Integer.parseInt(line[c]); // turn into an integer ID. 
                } 
                for (int j = 1; j < 4; j++) { // for each class that the student is interested in: 
                    Class preferredClass = this.classes.get(studentPref[j]-1); // retrieve the preferred class
                    preferredClass.incrementPopularity(); 
                    for (int k = j; k < 4; k++) { // for the other classes
                        if (preferredClass.getTeacher() != this.classes.get(studentPref[k]-1).getTeacher()) { // if they do not have a teacher conflict:
                            this.conflict[studentPref[j]-1][studentPref[k]-1] += 1;
                            /*
                            this.conflict[studentPref[j]-1][studentPref[k]-1] = this.conflict[studentPref[j]-1][studentPref[k]-1] != -1 
                                                                        ? this.conflict[studentPref[j]-1][studentPref[k]-1] + 1 
                                                                        : 1; // if conflicts already added, increment. Else, set to 1.
                            */
                        }
                    }
                } 
            }
        } catch (FileNotFoundException fnf) { 
            System.err.println("Could not open the file" + fnf); 
        } catch (IOException ioe) { 
            System.err.println("Reading problem" + ioe);
        }

        //this.classes.sort(null);  // sort the classes in descending order of popularity 

        for( int i = 0; i < numClasses; i++ ) { // print each class. 
            System.out.println(classes.get(i)); 
        }
        
        for(int r = 1; r < this.numClasses; r++ ) { // for each class
            for (int c = 0; c < r; c++) { 
                System.out.println("# of conflicts between classes " + r + " and " + c + " is " + conflict[r][c]); 
            }
        }

        //creating an ArrayList of all edges and sorting it
        ArrayList<Edge> edges = new ArrayList<>();
        for (int r = 0; r < numClasses; r++) {
            for (int c = 0; c < r; c++) {
                if (conflict[r][c] != -1) {
                    edges.add(new Edge(classes.get(r), classes.get(c), conflict[r][c]));
                    
                }
                
            }
        }

        for (int i = 0; i < edges.size(); i++) {
            System.out.println(edges.get(i));
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
