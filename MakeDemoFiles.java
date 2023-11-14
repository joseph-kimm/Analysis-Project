/**
 * Description:     Generates sets of constraint/student preference files for various values of a desired independent variable.
 *                  Updates other variables proportionally where necessary to adhere to constraint requirements.
 * Usage:           java MakeDemoFiles <s - to only analyze students> <c - to only analyze classes>
 * Last Modified:   Nov 12, 2023
 */

import java.lang.ProcessBuilder; 
import java.io.IOException;
import java.io.File; 

public class MakeDemoFiles {
    final static int[] X = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100,
                            200, 300, 400, 500, 600, 700, 800, 900, 1000,
                            2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 
                            25000, 50000, 75000, 100000, 
                            250000, 500000};
    final static int[] T = {1, 2, 3, 4, 5, 6, 7, 8, 9,
                           10, 20, 30, 40, 50, 60, 70, 80, 90,
                           100, 200, 300, 400, 500};
    final static int numRuns = 5; 
    final int r = 5;
    final int c = 20;
    final int t = 10; 
    final int s = 1000; 
    final static String[] validVarsToChange = {"c", "s", "t"};
    public static void main(String[] args) { 
        MakeDemoFiles mdf = new MakeDemoFiles(args);        
    }
    

    public MakeDemoFiles(String[] args) { 
        makeStudentFiles("t");
        /*if (args.length > 0) { 
            for (String varToChange : args) { 
                if (isValidVariable(varToChange)) { 
                    makeStudentFiles(varToChange); 
                } else { 
                    System.out.println("Unable to generate files for variable " + varToChange + ". Variable not supported.");
                }
            }
        }
        else { 
            for (String validVar : validVarsToChange) { 
                makeStudentFiles(validVar); 
            }
        }*/
    }

    public void makeStudentFiles(String varToChange) { 
        try { 
            new ProcessBuilder("rm", "-r", "demo_files").start(); // make demo_files folder, if it does not already exist. 
            new ProcessBuilder("mkdir", "demo_files").start(); // make demo_files folder, if it does not already exist. 

            if (varToChange.equals("s")) { // if we're changing students: 
                new ProcessBuilder("mkdir", "demo_files/Students").start(); // make folder to hold Student files, if it does not already exist. 
                
                for (int s : X) { 

                    new ProcessBuilder("mkdir", "demo_files/Students/" + s).start(); // make folder to hold files with s number of students, if it does not already exist. 
                    
                    for (int i = 1; i <= numRuns; i ++ ) { // for each run we want to do:
                        ProcessBuilder pb = new ProcessBuilder("Perl", 
                                                            "make_random_input.pl",
                                                            Integer.toString(r), 
                                                            Integer.toString(c), 
                                                            Integer.toString(t),
                                                            Integer.toString(s),
                                                            "demo_files/Students/" + s + "/const-" + i + ".txt",
                                                            "demo_files/Students/" + s + "/pref-" + i + ".txt"
                                                            );
                        pb.redirectErrorStream(true);
                        File log = new File("log.txt"); 
                        pb.redirectOutput(log);
                        pb.start(); 
                    }
                }
            }
            else if (varToChange.equals("c")) { 

                new ProcessBuilder("mkdir", "demo_files/Classes").start(); //  make folder to hold Classes files, if it does not already exist. 
                
                for (int c : X) { 
                    
                    new ProcessBuilder("mkdir", "demo_files/Classes/" + c).start(); // make folder to hold files with c number of classes, if it does not already exist. 
                    
                    for (int i = 1; i <= 5; i ++ ) { 
                        ProcessBuilder pb = new ProcessBuilder("Perl", 
                                                            "make_random_input.pl",
                                                            Integer.toString(c/10), // number of rooms must scale accordingly with classes. 
                                                            Integer.toString(c), 
                                                            Integer.toString(t),
                                                            Integer.toString(s),
                                                            "demo_files/Classes/" + c + "/const-" + i + ".txt",
                                                            "demo_files/Classes/" + c + "/pref-" + i + ".txt"
                                                            );
                        pb.redirectErrorStream(true);
                        File log = new File("log.txt"); 
                        pb.redirectOutput(log);
                        Process p = pb.start(); 
                    }
                }
            }
            else if (varToChange.equals("t")) { 
                new ProcessBuilder("mkdir", "demo_files/TimeSlots").start(); //  make folder to hold Classes files, if it does not already exist. 
                
                for (int t : T) { 
                    
                    new ProcessBuilder("mkdir", "demo_files/TimeSlots/" + t).start(); // make folder to hold files with c number of classes, if it does not already exist. 
                    
                    for (int i = 1; i <= 5; i ++ ) { 
                        ProcessBuilder pb = new ProcessBuilder("Perl", 
                                                            "make_random_input.pl",
                                                            Integer.toString(r), 
                                                            Integer.toString(c), 
                                                            Integer.toString(t),
                                                            Integer.toString(s),
                                                            "demo_files/TimeSlots/" + c + "/const-" + i + ".txt",
                                                            "demo_files/TimeSlots/" + c + "/pref-" + i + ".txt"
                                                            );
                        pb.redirectErrorStream(true);
                        File log = new File("log.txt"); 
                        pb.redirectOutput(log);
                        Process p = pb.start(); 
                    }
                }
            }
        }
        catch (IOException ioe) { 
            System.err.println("IO Exception: " + ioe);
        }
    }

    /*
     * returns true if the given String is within the list of valid variables to change. 
     */
    public static boolean isValidVariable(String v) { 
        for (String validVar : validVarsToChange) { 
            if (validVar.equals(v)) { 
                return true; 
            }
        }
        return false; 
    }
}
