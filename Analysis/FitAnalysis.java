public class FitAnalysis { // number of times to run because each test. 
    final int numRuns = MakeDemoFiles.numRuns; // the number of times to run and average each time. 
    final int[] X = MakeDemoFiles.X;
    public static void main(String[] args) {
        FitAnalysis fa = new FitAnalysis(); 
        
        MakeDemoFiles mdf = new MakeDemoFiles(args);        

        if (args.length == 0 || args[0].equals("s")) { 
            int JITtotal = 0; 
            for (int i = 1; i <= fa.numRuns; i ++ ) { 
                ScheduleMaker sm = new ScheduleMaker(new String("demo_files/Students/10/const-" + i + ".txt"), new String("demo_files/Students/10/pref-" + i + ".txt"));
                JITtotal += sm.getFit();
            }
            if (JITtotal != 0) { 
                System.out.print("s\tavgFit\n");
                for (int s : fa.X) { 
                    fa.printStudentFit(s); 
                }
            }
            else { 
                System.out.println("Oops! Something went wrong-- fit returned equals 0. That shouldn't happen!");
            }  
        }
        if (args.length == 0 || args[0].equals("c")) { 
            // JIT runs:  
            int JITtotal = 0; 
            for (int i = 1; i <= fa.numRuns; i ++ ) { 
                ScheduleMaker sm = new ScheduleMaker(new String("demo_files/Classes/10/const-" + i + ".txt"), new String("demo_files/Classes/10/pref-" + i + ".txt"));
                JITtotal += sm.getFit();
            }
            if (JITtotal != 0) { 
                System.out.print("c\tavgFit\n");
                int i = 0;
                while (fa.X[i] <= 10000) { 
                    fa.printClassFit(fa.X[i]); 
                    i++; 
                }
            }
            else { 
                System.out.println("Oops! Something went wrong-- fit  returned equals 0. That shouldn't happen!");
            }            
        }  
        if (args.length == 0 || args[0].equals("t")) { 
            // JIT runs:  
            int JITtotal = 0; 
            for (int i = 1; i <= fa.numRuns; i ++ ) { 
                ScheduleMaker sm = new ScheduleMaker(new String("demo_files/Classes/10/const-" + i + ".txt"), new String("demo_files/Classes/10/pref-" + i + ".txt"));
                JITtotal += sm.getFit();
            }
            if (JITtotal != 0 ) { 
                System.out.print("c\tavgFit\n");
                for (int t = 1; t < 10; t++) { 
                    fa.printTimeSlotFit(t); 
                }
                for (int i = 10; i < 100; i += 10) {
                    fa.printTimeSlotFit(t);     
                } 
                for (int i = 100; i <= 500; i += 50) {
                    fa.printTimeSlotFit(t);     
                } 
            }
            else {
                System.out.println("Oops! Something went wrong-- fit returned equals 0. That shouldn't happen!");
            }
        }   
        if (args.length == 0 || args[0].equals("r")) { 
            
        }
    }

    public void printStudentFit(int s) {
        double totalFit = 0; 
        for (int i = 1; i <= 5; i++ ) { // for each randomized demo file
            ScheduleMaker sm = new ScheduleMaker(new String("demo_files/Students/" + s + "/const-" + i + ".txt"), new String("demo_files/Students/" + s + "/pref-" + i + ".txt"));
            totalFit += sm.getFit();
        }
        double averageFit = totalFit / 5;
        System.out.println(s + "\t" + averageFit); 
    }

    public void printClassFit(int c) { 
        double totalFit = 0; 
        for (int i = 1; i <= 5; i++ ) { // for each randomized demo file
            ScheduleMaker sm = new ScheduleMaker(new String("demo_files/Classes/" + c + "/const-" + i + ".txt"), new String("demo_files/Classes/" + c + "/pref-" + i + ".txt"));
            totalFit += sm.getFit();
        }
        double averageFit = totalFit / 5;
        System.out.println(c + "\t" + averageFit); 
    }
}
