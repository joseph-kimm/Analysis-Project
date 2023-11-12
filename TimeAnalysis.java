public class TimeAnalysis { // number of times to run because each test. 
    final int numRuns = MakeDemoFiles.numRuns; // the number of times to run and average each time. 
    final int[] X = MakeDemoFiles.X;
    public static void main(String[] args) {
        TimeAnalysis ta = new TimeAnalysis(); 
        
        MakeDemoFiles mdf = new MakeDemoFiles(args); 

        if (args.length == 0 || args[0].equals("s")) { 
            int JITtotal = 0; 
            for (int i = 1; i <= ta.numRuns; i ++ ) { 
                ScheduleMaker sm = new ScheduleMaker(new String("demo_files/Students/10/const-" + i + ".txt"), new String("demo_files/Students/10/pref-" + i + ".txt"));
                JITtotal += sm.getNanoSecondsElapsed();
            }
            if (JITtotal != 0) { 
                System.out.print("s\tavgMicroSeconds\n");
                for (int s : ta.X) { 
                    ta.printStudentTimes(s); 
                }
            }
            else { 
                System.out.println("Oops! Something went wrong-- nanoseconds returned equals 0. That shouldn't happen!");
            }  
        }
        if (args.length == 0 || args[0].equals("c")) { 
            // JIT runs:  
            int JITtotal = 0; 
            for (int i = 1; i <= ta.numRuns; i ++ ) { 
                ScheduleMaker sm = new ScheduleMaker(new String("demo_files/Classes/10/const-" + i + ".txt"), new String("demo_files/Classes/10/pref-" + i + ".txt"));
                JITtotal += sm.getNanoSecondsElapsed();
            }
            if (JITtotal != 0) { 
                System.out.print("s\tavgMicroSeconds\n");
                for (int c : ta.X) { 
                    ta.printClassTimes(c); 
                }
            }
            else { 
                System.out.println("Oops! Something went wrong-- nanoseconds returned equals 0. That shouldn't happen!");
            }            
        }  
    }

    public void printStudentTimes(int s) {
        long nanoSecondsElapsed = 0; 
        for (int i = 1; i <= 5; i++ ) { // for each randomized demo file
            ScheduleMaker sm = new ScheduleMaker(new String("demo_files/Students/" + s + "/const-" + i + ".txt"), new String("demo_files/Students/" + s + "/pref-" + i + ".txt"));
            nanoSecondsElapsed += sm.getNanoSecondsElapsed();
        }
        long averageMicroSecondsElapsed = (nanoSecondsElapsed / 1000) / 5;
        System.out.println(s + "\t" + averageMicroSecondsElapsed); 
    }

    public void printClassTimes(int c) { 
        long nanoSecondsElapsed = 0; 
        for (int i = 1; i <= 5; i++ ) { // for each randomized demo file
            ScheduleMaker sm = new ScheduleMaker(new String("demo_files/Classes/" + c + "/const-" + i + ".txt"), new String("demo_files/Classes/" + c + "/pref-" + i + ".txt"));
            nanoSecondsElapsed += sm.getNanoSecondsElapsed();
        }
        long averageMicroSecondsElapsed = (nanoSecondsElapsed / 1000) / 5;
        System.out.println(c + "\t" + averageMicroSecondsElapsed); 
    }
}
