import java.util.*;

public class BookingOffice {
    private ArrayList<Reservation> allReservations;
    private static BookingOffice instance = new BookingOffice();

    private BookingOffice() {
        allReservations = new ArrayList<>();
    }
    public static BookingOffice getInstance() {
        return instance;
    }
    public void listReservations() {
        Reservation.list(allReservations);
    }
    public Reservation addReservation(String guestName, String phoneNumber, int totalPersons, String date_dine)throws ExDatePassed{
        Reservation r = new Reservation(guestName, phoneNumber, totalPersons, date_dine);
        allReservations.add(r);
        Collections.sort(allReservations);
        return r;
    }
    public void removeReservations(Reservation r) {
        allReservations.remove(r);

    }
    public boolean exists(Reservation other){
        for(Reservation r : allReservations){
            if(r.equals(other)){
                return true;
            }
        }
        return false;
    }
    public void addReservation(Reservation r) {
        allReservations.add(r);
        Collections.sort(allReservations);
    }
    public Reservation getReservationTicket(int tnum, Day d) throws ExBookingNotFound{
        for(Reservation r : allReservations){
            if(r.getDateDine().equals(d)){
                if(r.getTicketNumber() == tnum) {
                    return r;
                }
            }
        }
        throw new ExBookingNotFound();
    }
    public void listPending(Day d) {
        int pending=0;
        int totalpersons=0;
        for(Reservation r : allReservations){
            if(r.compare(d)==0) {
                if(r.getStatus().toString().equals("Pending")){
                    pending++;
                    totalpersons += r.getTotalPersons();
                }
            }
        }
        System.out.printf("Total number of pending requests = %d (Total number of persons = %d)\n", pending, totalpersons);
    }
}
