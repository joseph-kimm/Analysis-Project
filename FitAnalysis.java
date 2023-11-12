public class FitAnalysis { // number of times to run because each test. 
    final int numRuns = 10; // the number of times to run and average each time. 
    final int[] X = MakeDemoFiles.X;
    public static void main(String[] args) {
        FitAnalysis fa = new FitAnalysis(); 
        if (args.length == 1) { 
            if (args[0].equals("s")) { 
                 // JIT runs:  
                for (int i = 1; i <= fa.numRuns; i ++ ) { 
                    ScheduleMaker sm = new ScheduleMaker(new String("demo_files/10Students/s10-" + i + "_const.txt"), new String("demo_files/10Students/s10-" + i + "_pref.txt"));
                    System.out.println(sm.getFit());
                }

                System.out.print("s\tFit\n");
                for (int s : fa.X) { 
                    fa.printStudentFit(s); 
                }
            }
            if (args[0].equals("c")) { 
                 // JIT runs:  
                for (int i = 1; i <= fa.numRuns; i ++ ) { 
                    ScheduleMaker sm = new ScheduleMaker(new String("demo_files/10Classes/c10-" + i + "_const.txt"), new String("demo_files/10Classes/c10-" + i + "_pref.txt"));
                    System.out.println(sm.getFit());
                }

                System.out.print("s\tavgMicroSeconds\n");
                for (int c : fa.X) { 
                    fa.printClassFit(c); 
                }
            }
        }
        // JIT runs:
        else {   
                for (int i = 1; i <= fa.numRuns; i ++ ) { 
                    ScheduleMaker sm = new ScheduleMaker(new String("demo_files/10Classes/c10-" + i + "_const.txt"), new String("demo_files/10Classes/c10-" + i + "_pref.txt"));
                    System.out.println(sm.getFit());
                }

                System.out.print("c\tavgMicroSeconds\n");
                for (int c : fa.X) { 
                    fa.printClassFit(c); 
                }
        }
        
    }

    public void printStudentFit(int s) {
        double totalFitPercentage = 0; 
        for (int i = 1; i <= numRuns; i++ ) { // for each randomized demo file
            ScheduleMaker sm = new ScheduleMaker(new String("demo_files/" + s + "Students/s" + s + "-" + i + "_const.txt"), new String("demo_files/" + s + "Students/s" + s + "-" + i + "_pref.txt"));
            totalFitPercentage += sm.getFit();
        }
        double averageFit = totalFitPercentage / numRuns;
        System.out.println(s + "\t" + averageFit); 
    }

    public void printClassFit(int c) {
        double totalFitPercentage = 0; 
        for (int i = 1; i <= numRuns; i++ ) { // for each randomized demo file
             ScheduleMaker sm = new ScheduleMaker(new String("demo_files/" + c + "Classes/c" + c + "-" + i + "_const.txt"), new String("demo_files/" + c + "Classes/c" + c + "-" + i + "_pref.txt"));
            totalFitPercentage += sm.getFit();
        }
        double averageFit = totalFitPercentage / numRuns;
        System.out.println(c + "\t" + averageFit); 
    }
   
}
