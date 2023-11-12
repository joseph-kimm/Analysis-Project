public class Room implements Comparable<Room> {
    protected String roomName;
    protected int roomSize;

    public Room (String roomName, int roomSize) { 
        this.roomName = roomName;
        this.roomSize = roomSize; 
    }
    
    public String getRoomNumber() {
        return this.roomName;
    }

    public int getRoomSize() {
        return this.roomSize;
    }

    public int compareTo(Room thatRoom) { 
        // if this returns a positive number, it means that thatClass is more popular than this class. 
        // This makes a little less sense than I'd typically do, but it means that when we call .sort() on the arraylist, 
        // it sorts in descending order, which is very handy. - R. 
        return thatRoom.getRoomSize() - this.roomSize; 
    }
}
