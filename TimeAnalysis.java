public class TimeAnalysis { // number of times to run because each test. 
    final int numRuns = 10; // the number of times to run and average each time. 
    final int[] numStudents = {10, 25, 50, 100, 250, 500, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000};
    public static void main(String[] args) {
        TimeAnalysis ta = new TimeAnalysis(); 
         // JIT runs:  
        for (int i = 0; i < ta.numRuns; i ++ ) { 
           ScheduleMaker sm = new ScheduleMaker(new String("demo_files/10Students/s10-" + i + "_const.txt"), new String("demo_files/10Students/s10-" + i + "_pref.txt"));
           System.out.println(sm.getNanoSecondsElapsed());
        }

        System.out.print("s\tavgMicroSeconds\n");
        for (int s : ta.numStudents) { 
            ta.printStudentTimes(s); 
        }
    }

    public void printStudentTimes(int s) {
       
    
        long nanoSecondsElapsed = 0; 
        for (int i = 1; i < 5; i++ ) { // for each randomized demo file
            ScheduleMaker sm = new ScheduleMaker(new String("demo_files/" + s + "Students/s" + s + "-" + i + "_const.txt"), new String("demo_files/" + s + "Students/s" + s + "-" + i + "_pref.txt"));
            nanoSecondsElapsed += sm.getNanoSecondsElapsed();
        }
        long averageMicroSecondsElapsed = (nanoSecondsElapsed / 1000) / 5;
        System.out.println(s + "\t" + averageMicroSecondsElapsed); 
    }
}
