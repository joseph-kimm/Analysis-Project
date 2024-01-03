/**
 * Description:     Stores the data for a single room, as well as a comparator based on size.
 */

public class Room implements Comparable<Room> {
    protected int roomNumber;
    protected int roomSize;

    public Room (int roomNumber, int roomSize) { 
        this.roomNumber = roomNumber;
        this.roomSize = roomSize; 
    }
    
    public int getRoomNumber() {
        return this.roomNumber;
    }

    public int getRoomSize() {
        return this.roomSize;
    }

    /*
     * Sorts rooms by size in descending order.
     */
    public int compareTo(Room thatRoom) { 
        // if this returns a positive number, it means that thatRoom is more popular than this class. 
        // This makes a little less sense than I'd typically do, but it means that when we call .sort() on the arraylist, 
        // it sorts in descending order, which is very handy. - R. 
        return thatRoom.getRoomSize() - this.roomSize; 
    }
}
