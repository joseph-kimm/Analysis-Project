public class Edge implements Comparable<Edge> {
    protected Class c1; 
    protected Class c2; 
    protected int numConflicts; 

    public Edge(Class c1, Class c2, int numConflicts) { 
        this.c1 = c1; 
        this.c2 = c2; 
        this.numConflicts = numConflicts; 
    }

    public int getNumConflicts() { 
        return numConflicts; 
    }

    @Override
    public int compareTo() { 
        
    }
}
