public class Class implements Comparable<Class>{
    protected int teacher; 
    protected int popularity; 

    public Class (int teacher) { 
        this.teacher = teacher; 
    }

    public int getPopularity() { 
        return popularity; 
    }

    public void setPopularity(int newPopularity) { 
        this.popularity = newPopularity; 
    }
    
    /*
     * Increment the popularity of the class by a given amount
     * @param incBy the amount to increment the popularity by
     */
    public void incrementPopularity(int incBy) { 
        this.popularity += incBy; 
    }
    
    /*
     * Increment the popularity of the class by 1. 
     */
    public void incrementPopularity() { 
        this.popularity++; 
    }

    public int getTeacher() { 
        return this.teacher; 
    }
    
    public void setTeacher(int newTeacher) { 
        this.teacher = newTeacher;
    }
    

    /* 
     * compare to method. If positive, it means this is larger than that. 
     * @return The popularity of the input class - the popularity of this class. 
     */
    @Override 
    public int compareTo(Class thatClass) { 
        // if this returns a positive number, it means that thatClass is more popular than this class. 
        // This makes a little less sense than I'd typically do, but it means that when we call .sort() on the arraylist, 
        // it sorts in descending order, which is very handy. - R. 
        return thatClass.getPopularity() - this.popularity; 
    }

    public String toString() { 
        return "{teacher: "+ teacher + ", popularity: " + popularity + "}"; 
    }
}
