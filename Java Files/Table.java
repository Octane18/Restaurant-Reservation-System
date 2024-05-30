import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Table implements Cloneable, Comparable<Table>{
    private String code;
    private int capacity;
    private ArrayList<Reservation> associatedReservations = new ArrayList<>();
    private boolean suggestStatus = false;
    public Table(String code, int capacity) {
        this.code = code;
        this.capacity = capacity;
    }


    public int getCapacity() {
        return capacity;
    }

    public int getTicket(Day d) {
        for (Reservation r : associatedReservations) {
            if(r.compare(d)==0) {
                return r.getTicketNumber();
            }
        }
        return -1;
    }
    public boolean checksuggeststatus() {
        return suggestStatus;
    }
    public void setSuggestStatus() {
        suggestStatus = true;
    }
    public void resetSuggestStatus() {
        suggestStatus = false;
    }

    public boolean equalsCode(String code) {
        return this.code.equals(code);
    }

    public void addReservation(Reservation reservation) {
        this.associatedReservations.add(reservation);
    }

    public boolean checkDate(Day d) {
        for(Reservation r : this.associatedReservations) {
            if(r.compare(d)==0) {
                return true;
            }
        }
        return false;
    }

    public void removeReservation(Reservation reservation) {
        this.associatedReservations.remove(reservation);
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    @Override
    public boolean equals(Object obj) {
        if(obj==null) {
            return false;
        }
        else if(obj.getClass() != this.getClass()) {
            return false;
        }
        Table other = (Table) obj;
        return this.code.equals(other.code) &&
                this.capacity== other.capacity;
    }
    @Override
    public String toString() {
        return code;
    }

    @Override
    public int compareTo(Table o) {
        if(this.capacity<o.capacity) {
            return -1;
        }
        else if(this.capacity>o.capacity) {
            return 1;
        }
        return this.code.compareTo(o.code);
    }

}
