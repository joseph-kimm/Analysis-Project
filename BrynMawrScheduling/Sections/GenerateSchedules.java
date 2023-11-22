import java.lang.ProcessBuilder;
import java.lang.ProcessBuilder.Redirect; 
import java.io.IOException; 
import java.io.File; 


//import java.lang.Process;

public class GenerateSchedules {
    public static void main(String[] args) { 
        if (args.length == 0) { 
            generateSchedule("F2000"); 
        }
        else { 
            generateSchedule(args[0]);
        }       
    }

    public static void generateSchedule(String semester) { 
        try { 
            
            ProcessBuilder pb = new ProcessBuilder("java",
                                                "ScheduleMakerSections",
                                                "BrynData/Constraints/" + semester + "_con.txt",
                                                "BrynData/Prefs/" + semester + "_pref.txt",
                                                "Schedules/" + semester + ".txt"); 
            pb.inheritIO(); 
            System.out.print(semester + " " ); 
            pb.start(); 
        }
        catch(IOException ioe) { 
            System.err.println("IOException " + ioe); 
        }
    }
}
