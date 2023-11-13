/**
 * Description:     Stores the data for a single timeslot, including the days of the week, and start and end minute and hour.
 * Last Modified:   Nov 12, 2023
 */

import java.util.Set;

public class Time {
    protected int index;

    // times are in military time!!
    protected int startHour;
    protected int startMinute;


    protected int endHour;
    protected int endMinute;

    protected Set<String> days;

    public Time(int index, int startH, int startM, int endH, int endM, Set<String> days) {
        this.index = index;
        this.startHour = startH;
        this.startMinute = startM;
        this.endHour = endH;
        this.endMinute = endM;
        this.days = days;
    }

    public int getIndex() {
        return index;
    }

    public int getStartHour() {
        return startHour;
    }

    public int startMinute() {
        return startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public Set<String> getDays() {
        return days;
    }

    @Override
    public String toString() {
        return startHour + ":" + startMinute + "-" + endHour + ":" + endMinute + " " + days;
    }
}
