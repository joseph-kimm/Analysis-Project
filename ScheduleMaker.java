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
    private ArrayList<Class> classes; 
    private int numTeachers; // number of teachers specified in the input file. 
    private int[] pop; // a sorted array storing each class by popularity in descending order. 
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
        // we will assume that all input files will be of the same format as demo_constraints.txt
        
        // First, read values from the constraints file:  
        try (BufferedReader br = new BufferedReader(new FileReader(constraintsFile))) { 
            //first line of demo is the number of class times. 
            this.numTimeSlots = Integer.parseInt(br.readLine().split("\\s+")[2]); // read first line, take the 3rd word from it, a.k.a the # of class times. 
            this.numRooms = Integer.parseInt(br.readLine().split("\\s")[1]);
            this.cap = new int[this.numRooms]; // initialize the capacity array. 
            for (int i = 0; i < this.numRooms; i ++) { // read the rooms capacities --> r iterations. 
                this.cap[i] = Integer.parseInt(br.readLine().split("\\s")[1]); // store the capacity of a room i. 
            }
            Arrays.sort(this.cap); // Java Arrays library uses either quicksort or mergesort, both of which are n*log(n). Therefore, r*log(r)
            this.numClasses = Integer.parseInt(br.readLine().split("\\s")[1]);
            this.numTeachers = Integer.parseInt(br.readLine().split("\\s")[1]);
            for (int i = 0; i < this.numClasses; i ++) { // c iterations. 
                
            }
            
        } catch (FileNotFoundException fnf) { 
            System.err.println("Could not open the file" + fnf); 
        } catch (IOException ioe) { 
            System.err.println("Reading problem" + ioe);
        }
        //Initialize the popularity array.
        this.pop = new int[this.numClasses]; 
        for (int i = 0; i < this.numClasses; i++ ) { 
            
        }

        this.conflict = new int[this.numClasses][this.numClasses]; 
        //Initialize the conflicts array: 
        for(int r = 1; r < this.numClasses; r++ ) { // for each class
            this.conflict[r] = new int[this.numClasses]; // initialize each row. 
            for (int c = 0; c < r; c++) { 
                this.conflict[r][c] = -1; 
            }
        }

        // Then, read in values from the students file: 
        try (BufferedReader br = new BufferedReader(new FileReader(constraintsFile))) { 
            //first line is numStudents
            this.numStudents = Integer.parseInt(br.readLine().split("\\s")[1]);
            for (int i = 0; i < this.numStudents; i++) { // for every student (s)
                String[] studentPref = br.readLine().split("\\s"); 
                for (int j = 1; j < 4; j++) { // for each class that the student is interested in. 
                    this.pop[Integer.parseInt(studentPref[j])]++;
                }
            }

        } catch (FileNotFoundException fnf) { 
            System.err.println("Could not open the file" + fnf); 
        } catch (IOException ioe) { 
            System.err.println("Reading problem" + ioe);
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
