/**
 * Description:     Stores the data for a the conflicts between a pair of two classes. One conflict is counted if a student is interested 
 *                  in both classes in the pair.
 * Last Modified:   Nov 12, 2023
 */

public class Edge implements Comparable<Edge> {
    protected Class c1; 
    protected Class c2; 
    protected int numConflicts; 

    public Edge(Class c1, Class c2, int numConflicts) { 
        this.c1 = c1; 
        this.c2 = c2;
        this.numConflicts = numConflicts; 
    }

    // getter methods
    public int getNumConflicts() { 
        return numConflicts; 
    }

    public Class getc1() {
        return c1;
    }
    
    public Class getc2() {
        return c2;
    }

    /*
     * general implementation of a compareTo method
     */
    @Override
    public int compareTo(Edge other) { 
        return this.numConflicts - other.numConflicts;  
    }

    @Override
    public String toString() {
        return c1.getClassNumber() + "-" + c2.getClassNumber() + ":" + numConflicts;
    }
}
