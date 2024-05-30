import java.util.ArrayList;

public class Reservation implements Comparable<Reservation> {
    private String guestName;
    private String phoneNumber;
    private int totalPersons;
    private Day dateDine;
    private Day dateRequest;
    private RState status;
    private int ticketNumber;




    public Reservation(String guestName, String phoneNumber, int totalPersons, String dateDine) throws ExDatePassed {
        this.guestName = guestName;
        this.phoneNumber = phoneNumber;
        this.totalPersons = totalPersons;
        this.dateDine = new Day(dateDine);
        this.dateRequest = SystemDate.getInstance().clone();
        status = new RStatePending();
        ticketNumber = BookingTicketController.getInstance().getTicket(this.dateDine);
        if(this.dateDine.compareTo(this.dateRequest)==-1) {
            throw new ExDatePassed();
        }


    }

    public int getTotalPersons() {
        return totalPersons;
    }

    public Day getDateDine() {
        return dateDine.clone();
    }
    public int getTicketNumber() {
        return ticketNumber;
    }
    public String getStatus() {
        return status.toString();
    }
    public void allocateTable(RState state) throws ExTablesAlreadyAssigned{
        if(!status.toString().equals("Pending")){
            throw new ExTablesAlreadyAssigned();
        }
        else {
            status = state;
        }
    }
    public void revert(RState state) {
        if(status.toString().equals("Pending")){
            return;
        }
        status = state;
    }

    public int compare(Day d){
        return dateDine.compareTo(d);
    }

    public static void list(ArrayList<Reservation> allReservations) {
        System.out.printf("%-13s%-11s%-14s%-25s%-11s%s\n", "Guest Name", "Phone", "Request Date", "Dining Date and Ticket", "#Persons", "Status");
        for (Reservation r : allReservations) {
            System.out.printf("%-13s%-11s%-14s%-25s%4d       %s\n", r.guestName, r.phoneNumber, r.dateRequest, r.dateDine+String.format(" (Ticket %d)", r.ticketNumber), r.totalPersons, r.status);
        }
    }

    public void Ticketremove() {
        BookingTicketController instance = BookingTicketController.getInstance();
        instance.removeTicket(dateDine);
    }
    public void Ticketreacquire() {
        this.ticketNumber = BookingTicketController.getInstance().getTicket(this.dateDine);
    }


    @Override
    public int compareTo(Reservation another) {
        if(this.guestName.compareTo(another.guestName)==0){
            if(this.phoneNumber.compareTo(another.phoneNumber)==0){
                return dateDine.compare(another.dateDine);
            }
            else {
                return this.phoneNumber.compareTo(another.phoneNumber);
            }
        }
        else {
            return this.guestName.compareTo(another.guestName);
        }
    }
    @Override
    public int hashCode() {
        return this.dateRequest.hashCode();
    }
    public void confirmRes() {
        System.out.printf(" Ticket code for %s: %d\n", dateDine, ticketNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null) {
            return false;
        }
        if(!(obj instanceof Reservation)) {
            return false;
        }
        Reservation r = (Reservation) obj;
        return this.guestName.equals(r.guestName) &&
                this.phoneNumber.equals(r.phoneNumber) &&
                this.dateDine.equals(r.dateDine);
    }

}