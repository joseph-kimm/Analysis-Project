/**
 * Description:     Stores the data for a single room, as well as a comparator based on size.
 * Last Modified:   Nov 12, 2023
 */

public class Room implements Comparable<Room> {
    protected String roomName;
    protected int roomSize;
    protected int index;

    public Room (String roomName, int roomSize) { 
        this.roomName = roomName;
        this.roomSize = roomSize; 
    }
    
    public String getRoomName() {
        return this.roomName;
    }

    public int getRoomSize() {
        return this.roomSize;
    }

    public void setIndex(int idx) {
        this.index = idx;
    }
    
    public int getIndex() {
        return this.index;
    }

    public String toString() {
        return this.roomName;
    }

    public int compareTo(Room thatRoom) { 
        // if this returns a positive number, it means that thatRoom is more popular than this class. 
        // This makes a little less sense than I'd typically do, but it means that when we call .sort() on the arraylist, 
        // it sorts in descending order, which is very handy. - R. 
        return thatRoom.getRoomSize() - this.roomSize; 
    }
}
