

import java.lang.ProcessBuilder; 
import java.io.IOException;
import java.io.File; 

public class MakeDemoFiles {
    final int[] X = {10, 50, 100, 250, 500, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000};

    final int r = 5;
    final int c = 20;
    final int t = 10; 
    public static void main(String[] args) { 
        MakeDemoFiles mdf = new MakeDemoFiles();
        mdf.makeStudentFiles(); 
    }

    public void makeStudentFiles() { 
        System.out.println("");
        try { 
            for (int s : X) { 
                for (int i = 1; i <= 5; i ++ ) { 
                    ProcessBuilder pb = new ProcessBuilder("Perl", 
                                                        "make_random_input.pl",
                                                        Integer.toString(r), 
                                                        Integer.toString(c), 
                                                        Integer.toString(t),
                                                        Integer.toString(s),
                                                        "demo_files/" + s + "Students/s" + s + "-" + i + "_const.txt",
                                                        "demo_files/" + s + "Students/s" + s + "-" + i + "_pref.txt"
                                                        );
                    pb.redirectErrorStream(true);
                    File log = new File("log.txt"); 
                    pb.redirectOutput(log);
                    Process p = pb.start(); 
                }
            }
            for (int c : X) { 
                ProcessBuilder mkBuilder = new ProcessBuilder("mkdir", 
                                                        "demo_files/" + c + "Classes",
                                                        "demo_files/" + c + "Classes"
                                                        );
                mkBuilder.redirectErrorStream(true);
                File mklog = new File("log.txt"); 
                mkBuilder.redirectOutput(mklog);
                mkBuilder.start(); 
            
                for (int i = 1; i <= 5; i ++ ) { 
                    ProcessBuilder pb = new ProcessBuilder("Perl", 
                                                        "make_random_input.pl",
                                                        Integer.toString(c/10), 
                                                        Integer.toString(c), 
                                                        Integer.toString(t),
                                                        Integer.toString(1000),
                                                        "demo_files/" + c + "Classes/c" + c + "-" + i + "_const.txt",
                                                        "demo_files/" + c + "Classes/c" + c + "-" + i + "_pref.txt"
                                                        );
                    pb.redirectErrorStream(true);
                    File log = new File("log.txt"); 
                    pb.redirectOutput(log);
                    Process p = pb.start(); 
                }
            }
            
        }
        catch (IOException ioe) { 
            System.err.println("IO Exception" + ioe);
        }
    }
}
